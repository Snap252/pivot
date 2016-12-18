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

public final class BiBucket<RAW> {

	private final RootBucket<RAW> rowBucket;
	private final RootBucket<RAW> colBucket;
	private final int maxRowDepth;

	public BiBucket(final BiBucketParameter<RAW> p) {
		rowBucket = new RootBucket<>(p.values, p.rowFnkt);
		this.maxRowDepth = p.rowFnkt.size();
		colBucket = new RootBucket<>(p.values, p.colFnkt);
	}

	private static class RowBucketWrapper<V, W> {
		public final Bucket<V> rb;
		public final CopyBucket<V, W> cells;

		public RowBucketWrapper(final Bucket<V> rb, final RootBucket<V> colBucket, final Collector<V, W, W> x) {
			this.rb = rb;
			this.cells = colBucket.createBucketWithNewValues(rb.values, x);
		}

		public Stream<CopyBucket<V, W>> getCells() {
			return cells.stream();
		}
	}

	public <AGG, REN> BiBucket<RAW>.CollectedValues<AGG, REN> createCollectedValues(
			final Collector<RAW, AGG, REN> aggregator) {
		return new CollectedValues<AGG, REN>(aggregator);
	}

	public final class CollectedValues<W, R> {

		private final List<RowBucketWrapper<RAW, W>> rows;
		private final Function<W, R> finisher;

		public CollectedValues(final Collector<RAW, W, R> x) {
			this.finisher = x.finisher();
			final Collector<RAW, W, W> of = Collector.of(x.supplier(), x.accumulator(), x.combiner());
			this.rows = rowBucket.stream().map(t -> new RowBucketWrapper<RAW, W>(t, colBucket, of)).collect(toList());
		}

		public Stream<CopyBucket<RAW, W>> getCells() {
			return rows.stream().flatMap(RowBucketWrapper::getCells);
		}

		public void printRow(final Writer w, final List<@Nullable Bucket<RAW>> rows, final int depth,
				final int spacerColumns, final int level) throws IOException {

			final List<@Nullable Bucket<RAW>> children = new ArrayList<>();
			w.write("<tr>");
			if (level == 0)
				w.write(MessageFormat.format(
						"<th colspan=''{0}'' rowspan=''{1}''>---columns---<br/>/<br/>---rows---</th>", spacerColumns,
						depth + 1));

			for (final Bucket<RAW> row : rows) {
				// render "self" - cell
				if (row == null) {
					w.write(MessageFormat.format("<th rowspan=''{0}''>-</th>", depth + 1));
					continue;
				}

				final int colSpan = row.getSize(1);
				if (colSpan == 1)
					w.write("<th>");
				else
					w.write(MessageFormat.format("<th colspan=''{0}''>", colSpan));
				w.write(row.bucketValue.toString());
				w.write("</th>");
				if (row.getChilren() != null) {
					// add "self" cell
					children.add(null);
					children.addAll(row.getChilren());
				}
			}
			w.write("</tr>");

			if (!children.isEmpty())
				printRow(w, children, depth - 1, spacerColumns, level + 1);
		}

		public void printRowHeader(final Bucket<RAW> b, final Writer w, final int colSpan) throws IOException {
			w.write(MessageFormat.format("<th rowSpan=''{0}''>{1}</th>", b.getSize(1), b.getBucketValue()));
			if (b.getChilren() != null)
				w.write(MessageFormat.format("<th colSpan=''{0}''>-</th>", colSpan));
		}

		public void writeHtml(final Writer w, final BiConsumer<Writer, R> cellWriter) throws IOException {
			w.write("<html>");
			w.write("<head><style>");
			w.write("	td {text-align: right; padding: 0px 5px;}");
			w.write("</style></head>");
			w.write("<body>");
			w.write("<table border='1'>");

			{
				w.write("<thead>");
				printRow(w, Collections.singletonList(colBucket), colBucket.depth, rowBucket.depth + 1, 0);
				w.write("</thead>");
			}
			{
				w.write("<tbody>");

				rows.forEach(r -> {
					try {
						w.write("<tr>");

						printRowHeader(r.rb, w, maxRowDepth - r.rb.getLevel());

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
