package com.snap252.org;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class CopyBucket<V> extends Bucket<V> {

	private Bucket<V> origBucket;
	private List<Bucket<V>> children;

	public CopyBucket(Bucket<V> origBucket, List<V> valuesBase) {
		super(origBucket.bucketValue, null, null, origBucket.filter(valuesBase), origBucket.getLevel());
		this.origBucket = origBucket;

		final Collection<Bucket<V>> origChildren = origBucket.getChilren();
		if (origChildren == null || origChildren.isEmpty()) {
			return;
		}

		this.children = origChildren.stream().map(c -> new CopyBucket<>(c, values)).collect(toList());

		assert this.children.stream().flatMap(c -> c.values.stream()).collect(toSet())
				.equals(new HashSet<V>(values)) : origBucket + "=> own: " + new HashSet<V>(values);
	}

	@Override
	public List<Bucket<V>> getChilren() {
		return children;
	}

	@Override
	public boolean test(V v) {
		return origBucket.test(v);
	}

	@Override
	protected int getSize(int forSelf) {
		return origBucket.getSize(forSelf);
	}

	@Override
	public Stream<Bucket<V>> stream() {
		if (children == null || children.isEmpty()) {
			return Stream.of(this);
		}
		return Stream.concat(Stream.of(this), children.stream().flatMap(Bucket::stream));
	}

	@Override
	public Stream<Bucket<V>> reverseStream() {
		if (children == null || children.isEmpty()) {
			return Stream.of(this);
		}
		return Stream.concat(children.stream().flatMap(Bucket::stream), Stream.of(this));
	}

	@Override
	public String getParentString() {
		return origBucket.getParentString();
	}
	
	@Override
	public int getDepth() {
		return origBucket.getDepth();
	}

}
