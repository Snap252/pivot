package com.snap252.org.testing.rendering;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.Bucket;
import com.snap252.org.pivoting.PivotCriteria;

public class CopyBucket<V, W> extends Bucket<V> {

	private Bucket<V> origBucket;

	public boolean isOrigBucket(final Object b) {
		return b == origBucket;
	}

	@Nullable
	private List<CopyBucket<V, W>> children;

	private final Map<Bucket<V>, CopyBucket<V, W>> childMap = new HashMap<>();

	public @Nullable CopyBucket<V, W> getChild(final Bucket<V> b) {
		// assert !(b instanceof CopyBucket);
		if (b == origBucket) {
			return this;
		}
		if (children == null)
			return null;
		return childMap.computeIfAbsent(b, (final Bucket<V> key) -> {
			assert children != null;
			for (final CopyBucket<V, W> s : children) {
				final CopyBucket<V, W> child = s.getChild(b);
				if (child != null) {
					return child;
				}
			}
			throw new AssertionError();
		});
	}

	public W aggregatedValue;

	public CopyBucket(final Bucket<V> origBucket, final Collection<V> valuesBase, final Collector<V, W, W> collector,
			final Collector<W, W, W> collector2, @Nullable final CopyBucket<V, W> parent) {
		super(origBucket.bucketValue, parent, (PivotCriteria<V, @Nullable String>) (x -> ""),
				origBucket.filter(valuesBase), origBucket.getLevel());
		this.origBucket = origBucket;

		final Collection<? extends Bucket<V>> origChildren = origBucket.getChildren();
		if (origChildren == null) {
			if (values.isEmpty()) {
				aggregatedValue = collector.supplier().get();
			} else
				aggregatedValue = values.stream().collect(collector);
			return;
		}

		final List<CopyBucket<V, W>> children$ = origChildren.stream()
				.map(c -> new CopyBucket<>(c, values, collector, collector2, this)).collect(toList());
		this.children = children$;

		this.aggregatedValue = children$.stream().map(c -> c.aggregatedValue).collect(collector2);
		assert equals(this.aggregatedValue, values.stream().collect(collector)) : this.aggregatedValue + "=>"
				+ values.stream().collect(collector);

		assert children$.stream().flatMap(c -> c.values.stream()).collect(toSet())
				.equals(new HashSet<V>(values)) : origBucket + "=> own: " + new HashSet<V>(values);
	}

	private static boolean equals(@Nullable final Object o1, @Nullable final Object o2) {
		if (o1 == null)
			return o2 == null;

		assert o1.equals(o2) : o1 + "=>" + o2;
		return o1.equals(o2);
	}

	@Override
	public @Nullable List<CopyBucket<V, W>> getChildren() {
		return children;
	}

	@Override
	public boolean test(final V v) {
		return origBucket.test(v);
	}

	@Override
	public int getSize(final int forSelf) {
		return origBucket.getSize(forSelf);
	}

	@Override
	public int getDepth() {
		return origBucket.getDepth();
	}

}
