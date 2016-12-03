package com.snap252.org;

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

		public Stream<List<V>> getCells() {
			return colBucket.createBucketWithNewValues(rb.values).stream().map(b -> b.values);
		}
	}

	public Stream<RowBucketWrapper> getRows() {
		return rowBucket.stream().map(RowBucketWrapper::new);
	}
	public Stream<List<V>> getCells() {
		return getRows().flatMap(r -> r.getCells());
	}
}
