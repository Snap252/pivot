package com.snap252.org;

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

public final class BiBucket<V> {

	private final RootBucket<V> rowBucket;
	private final RootBucket<V> colBucket;
	private final int maxRowDepth;

	public BiBucket(BiBucketParameter<V> p) {
		rowBucket = new RootBucket<>(p.values, p.rowFnkt);
		this.maxRowDepth = p.rowFnkt.size();
		colBucket = new RootBucket<>(p.values, p.colFnkt);
	}

	public static class RowBucketWrapper<V, W> {
		public final Bucket<V> rb;
		public final CopyBucket<V, W> cells;

		public RowBucketWrapper(Bucket<V> rb, final RootBucket<V> colBucket, Collector<V, W, W> x) {
			this.rb = rb;
			this.cells = colBucket.createBucketWithNewValues(rb.values, x);
		}

		public Stream<CopyBucket<V, W>> getCells() {
			return cells.stream();
		}
	}

	public static class BucketWithValues<V, W> {
		public final Bucket<V> bucket;
		public final W aggregatedValue;

		public BucketWithValues(Bucket<V> b, Transformer<V, W> t, Collector<W, ?, W> a) {
			this(b, b.values.stream().map(t::transform).collect(a));
		}

		public BucketWithValues(Bucket<V> bucket, W value) {
			this.bucket = bucket;
			this.aggregatedValue = value;
		}
	}

	public <W, R> BiBucket<V>.X<W, R> createX(Collector<V, W, R> aggregator) {
		return new X<W, R>(aggregator);
	}

	class X<W, R> {

		private List<RowBucketWrapper<V, W>> rows;
		private Function<W, R> finisher;

		public X(Collector<V, W, R> x) {
			this.finisher = x.finisher();
			Collector<V, W, W> of = Collector.of(x.supplier(), x.accumulator(), x.combiner());
			this.rows = rowBucket.stream().map(t -> new RowBucketWrapper<V, W>(t, colBucket, of)).collect(toList());
		}

		public Stream<BucketWithValues<V, W>> getTransformed(Transformer<V, W> t, Collector<W, ?, W> a) {
			return getCells().map(r -> new BucketWithValues<V, W>(r, t, a));
		}

		public Stream<CopyBucket<V, W>> getCells() {
			return rows.stream().flatMap(RowBucketWrapper::getCells);
		}

		public void printRow(Writer w, List<Bucket<V>> rows, int depth, int spacerColumns, int level)
				throws IOException {

			final List<Bucket<V>> children = new ArrayList<Bucket<V>>();
			w.write("<tr>");
			if (level == 0)
				w.write(MessageFormat.format(
						"<th colspan=''{0}'' rowspan=''{1}''>---columns---<br/>/<br/>---rows---</th>", spacerColumns,
						depth + 1));

			for (final Bucket<V> row : rows) {
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

		public void printRowHeader(Bucket<V> b, Writer w, int colSpan) throws IOException {
			w.write(MessageFormat.format("<th rowSpan=''{0}''>{1}</th>", b.getSize(1), b.getBucketValue()));
			if (b.getChilren() != null)
				w.write(MessageFormat.format("<th colSpan=''{0}''>-</th>", colSpan));
		}

		public void writeHtml(Writer w, BiConsumer<Writer, R> cellWriter) throws IOException {
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

						r.getCells().forEachOrdered((CopyBucket<V, W> cell) -> {
							R collect = finisher.apply(cell.aggregatedValue);
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
