package com.snap252.org;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public final class BiBucket2<V> {

	private final RootBucket<V> rowBucket;
	private final RootBucket<V> colBucket;

	public BiBucket2(List<V> values, Pair<Function<V, Object>[]> rowColsFnkt) {
		rowBucket = new RootBucket<>(values, rowColsFnkt.first);
		colBucket = new RootBucket<>(values, rowColsFnkt.second);
	}

	public final class RowBucketWrapper {
		private final Bucket<V> rb;

		public RowBucketWrapper(Bucket<V> rb) {
			this.rb = rb;
		}

		public Stream<Bucket<V>> getCells() {
			return colBucket.createBucketWithNewValues(rb.values).stream();// .map(b
																			// ->
																			// b.values);
		}
	}

	public Stream<RowBucketWrapper> getRows() {
		return rowBucket.stream().map(RowBucketWrapper::new);
	}

	public Stream<Bucket<V>> getCells() {
		return getRows().flatMap(r -> r.getCells());
	}

	public void printRow(Writer w, List<Bucket<V>> rows, int depth, int spacerColumns, int level) throws IOException {

		List<Bucket<V>> children = new ArrayList<Bucket<V>>();
		w.write("<tr>");
		if (level == 0)
			w.write(MessageFormat.format("<th colspan=''{0}'' rowspan=''{1}''>---columns---</th>", spacerColumns,
					depth + 1));
		for (Bucket<V> row : rows) {
			// render "self" - cell
			if (row == null) {
				w.write(MessageFormat.format("<th rowspan=''{0}''>-</th>", depth + 1));
				continue;
			}

			int colSpan = row.getSize(1);
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

	public void printRowHeader(Bucket<V> b, Writer w, int initDepth) throws IOException {
		if (b.getLevel() != 0) {
			printRowHeader(b.getParent(), w, initDepth);
		}
		w.write(MessageFormat.format("<td colspan=''{0}''>", initDepth - b.getLevel()));
		w.write(b.getBucketValue().toString());
		w.write("</td>");
	}

	public void writeHtml(Writer w, Function<List<V>, ?> cellHandler) throws IOException {
		w.write("<html><body>");
		w.write("<table border='1'>");

		{
			w.write("<thead>");
			printRow(w, Collections.singletonList(colBucket), colBucket.depth, rowBucket.depth + 1, 0);
			w.write("</thead>");
		}
		{
			w.write("<tbody>");

			getRows().forEach(r -> {
				try {
					w.write("<tr>");

					printRowHeader(r.rb, w, r.rb.getDepth());

					// w.write(MessageFormat.format("<td colspan=''{0}''/>",
					// rowBucket.depth));
					r.getCells().forEach(cell -> {
						try {
							w.write("<td>");
							w.write(cellHandler.apply(cell.values).toString());
							w.write("</td>");
						} catch (IOException e) {
							throw new AssertionError(e);
						}
					});
					w.write("</tr>");
				} catch (IOException e) {
					throw new AssertionError(e);
				}
			});

			w.write("</tbody>");
		}
		w.write("</table>");
		w.write("</body></html>");
	}
}
