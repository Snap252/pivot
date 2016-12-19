package com.snap252.org.pivoting;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

public class LeafBucket<V> extends Bucket<V> {

	public LeafBucket(final Object bucketValue, final Bucket<V> parent, final PivotCriteria<V, ?> extractor,
			final List<V> values, final int level) {
		super(bucketValue, parent, extractor, values, level);
	}

	@Override
	protected int getSize(final int forSelf) {
		return 1;
	}

	@Override
	public Stream<Bucket<V>> stream() {
		return Stream.of(this);
	}

	@Override
	public @Nullable List<Bucket<V>> getChilren() {
		return null;
	}

	@Override
	public int getDepth() {
		return 1;
	}
}
