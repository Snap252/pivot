package com.snap252.org.pivoting;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

public class BiBucket<RAW> {

	public final RootBucket<RAW> rowBucket;
	public final RootBucket<RAW> colBucket;
	public final int maxRowDepth;

	public BiBucket(final BiBucketParameter<RAW> p) {
		rowBucket = new RootBucket<>("row", p.values, p.rowFnkt);
		this.maxRowDepth = p.rowFnkt.size();
		colBucket = new RootBucket<>("col", p.values, p.colFnkt);
	}

	public static class RowBucketWrapper<V, W> {
		public final Bucket<V> rowBucket;
		public final CopyBucket<V, W> cells;

		public RowBucketWrapper(final Bucket<V> rowBucket, final RootBucket<V> colBucket,
				final Collector<V, W, W> collectorWithoutFinisher,
				final Collector<W, W, W> collectorWithoutTransformer) {
			this.rowBucket = rowBucket;
			this.cells = colBucket.createBucketWithNewValues(rowBucket.values, collectorWithoutFinisher,
					collectorWithoutTransformer);

			assert colBucket.stream().allMatch(r -> cells.getChild(r) != null);
		}

		public Stream<CopyBucket<V, W>> getCells() {
			return cells.stream();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + rowBucket.hashCode();
			return result;
		}

		@Override
		public boolean equals(@Nullable final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final RowBucketWrapper<?, ?> other = (RowBucketWrapper<?, ?>) obj;
			return rowBucket.equals(other.rowBucket);
		}

	}

	public <AGG, REN> HtmlWriter<AGG, REN> createHtmlWriter(final Collector<RAW, AGG, REN> aggregator) {
		return new HtmlWriter<AGG, REN>(aggregator);
	}

	public final class HtmlWriter<W, R> {

		private final List<RowBucketWrapper<RAW, W>> rows;
		private final Function<W, R> finisher;

		private HtmlWriter(final Collector<RAW, W, R> collector) {
			this.finisher = collector.finisher();
			final Collector<RAW, W, W> collectorWithoutFinisher = Collector.of(collector.supplier(),
					collector.accumulator(), collector.combiner());

			final Collector<W, W, W> collectorWithoutTransformer = Collector.of(collectorWithoutFinisher.supplier(),
					collectorWithoutFinisher.combiner()::apply, collectorWithoutFinisher.combiner());
			this.rows = rowBucket.stream().map(t -> new RowBucketWrapper<RAW, W>(t, colBucket, collectorWithoutFinisher,
					collectorWithoutTransformer)).collect(toList());
		}

		public void printColumnHeaders(final Bucket<RAW> parent, final Writer w, final List<@Nullable Bucket<RAW>> rows,
				final int depth, final int spacerColumns, final int level) throws IOException {

			final List<@Nullable Bucket<RAW>> children = new ArrayList<>();
			w.write("<tr>");
			if (level == 0)
				w.write(MessageFormat.format(
						"<th colspan=''{0}'' rowspan=''{1}''>---columns---<br/>/<br/>---rows---</th>", spacerColumns,
						depth + 1));
			w.write(MessageFormat.format("<th class=''{1}''>{0}</th>", parent.toString(), parent.getStyleClass()));

			for (final Bucket<RAW> row : rows) {
				// render "self" - cell
				if (row == null) {
					w.write(MessageFormat.format(
							"<th class=''ges col ges-col'' title=''gesamt'' rowspan=''{0}''>-</th>", depth + 1 + 1));
					continue;
				}

				final int colSpan = row.getSize(1);
				w.write(MessageFormat.format("<th colspan=''{0}'' rowspan=''{1}'' class=''{2}''>", colSpan,
						row.getChildren() == null ? 2 : 1, row.getStyleClass()));
				w.write(row.bucketValue.toString());
				w.write("</th>");

				if (row.getChildren() != null) {
					// add "self" cell
					children.add(null);
					children.addAll(row.getChildren());
				}
			}
			w.write("</tr>");

			if (!children.isEmpty()) {
				// first child is dummy
				final Bucket<RAW> bucket = children.size() > 1 ? children.get(1) : children.get(0);
				assert bucket != null;
				printColumnHeaders(bucket, w, children, depth - 1, spacerColumns, level + 1);
			}
		}

		public void printRowHeader(final Bucket<RAW> b, final Writer w, final int colSpan) throws IOException {
			if (b.getChildren() != null) {
				w.write(MessageFormat.format("<th rowSpan=''{0}'' class=''{2}''>{1}</th>", b.getSize(1),
						b.getBucketValue(), b.getStyleClass()));
				w.write(MessageFormat.format("<th colSpan=''{0}'' class=''row ges row-ges'' title=''gesamt''>-</th>",
						colSpan));
			} else
				w.write(MessageFormat.format("<th rowSpan=''{0}'' class=''{2}''>{1}</th>", b.getSize(1),
						b.getBucketValue(), b.getStyleClass()));
		}

		public void writeHtml(final Writer w, final BiConsumer<Writer, R> cellWriter) throws IOException {
			w.write("<html>");
			w.write("<head><style>");
			w.write("	td {text-align: right; padding: 0px 5px;} ");
			w.write("</style></head>");
			w.write("<body>");
			w.write("<table border='1'>");

			{
				w.write("<thead>");
				printColumnHeaders(colBucket, w, Collections.singletonList(colBucket), colBucket.depth, rowBucket.depth,
						0);
				{
					w.write("<tr>");
					Bucket<RAW> b = rowBucket;
					do {
						assert b != null;
						final List<? extends Bucket<RAW>> children = b.getChildren();
						w.write(MessageFormat.format("<th class=''{1}''>{0}</th>", b, b.getStyleClass()));
						if (children != null) {
							b = children.get(0);
						} else
							b = null;
					} while (b != null);
					w.write("</tr>");
				}
				w.write("</thead>");
			}
			{
				w.write("<tbody>");

				rows.forEach(r -> {
					try {
						w.write("<tr>");

						printRowHeader(r.rowBucket, w, maxRowDepth - r.rowBucket.getLevel());

						r.getCells().forEachOrdered((final CopyBucket<RAW, W> cell) -> {
							final R collect = finisher.apply(cell.aggregatedValue);
							try {
								w.write("<td>");
								cellWriter.accept(w, collect);
								// w.write(cellHandler.apply(collect));
								w.write("</td>");
							} catch (final IOException e) {
								throw new AssertionError(e);
							}
						});
						w.write("</tr>");
					} catch (final IOException e) {
						throw new AssertionError(e);
					}
				});

				w.write("</tbody>");
			}
			w.write("</table>");
			w.write("</body></html>");
		}
	}
}
