package com.snap252.org;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class LeafBucket<V> extends Bucket<V> {

	public LeafBucket(Object bucketValue, SubBucket<V> predicate, Function<V, Object> extractor, List<V> values) {
		super(bucketValue, predicate, extractor, values);
	}

	@Override
	protected int getSize() {
		return 1;
	}

	@Override
	public Stream<Bucket<V>> stream() {
		return Stream.of(this);
	}

	@Override
	public Stream<Bucket<V>> reverseStream() {
		return Stream.of(this);
	}
	
	@Override
	public Collection<Bucket<V>> getChilren() {
		return null;
	}
}
