package com.snap252.org;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class CopyBucket<V, W> extends Bucket<V> {

	private Bucket<V> origBucket;
	private List<CopyBucket<V, W>> children;

	public W aggregatedValue;

	public CopyBucket(Bucket<V> origBucket, Collection<V> valuesBase, Collector<V, W, W> collector,
			Collector<W, W, W> collector2) {
		super(origBucket.bucketValue, null, null, origBucket.filter(valuesBase), origBucket.getLevel());
		this.origBucket = origBucket;

		final Collection<? extends Bucket<V>> origChildren = origBucket.getChilren();
		if (origChildren == null) {
			if (values.isEmpty()) {
				aggregatedValue = collector.supplier().get();
			} else
				aggregatedValue = values.stream().collect(collector);
			return;
		}

		this.children = origChildren.stream().map(c -> new CopyBucket<>(c, values, collector, collector2))
				.collect(toList());

		this.aggregatedValue = children.stream().map(c -> c.aggregatedValue).collect(collector2);
		assert this.aggregatedValue.equals(values.stream().collect(collector));

		assert this.children.stream().flatMap(c -> c.values.stream()).collect(toSet())
				.equals(new HashSet<V>(values)) : origBucket + "=> own: " + new HashSet<V>(values);
	}

	@Override
	public List<CopyBucket<V, W>> getChilren() {
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
	public Stream<CopyBucket<V, W>> stream() {
		if (children == null || children.isEmpty()) {
			return Stream.of(this);
		}
		return Stream.concat(Stream.of(this), children.stream().flatMap(CopyBucket::stream));
	}

	@Override
	public Stream<CopyBucket<V, W>> reverseStream() {
		if (children == null || children.isEmpty()) {
			return Stream.of(this);
		}
		return Stream.concat(children.stream().flatMap(CopyBucket::reverseStream), Stream.of(this));
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
