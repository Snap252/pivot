package com.snap252.org.pivoting;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;

public class RootBucket<V> extends SubBucket<V> {

	public RootBucket(final List<V> values, final List<Function<V, Object>> partitionCriterionsAndSubCriterions) {
		super("---root---", partitionCriterionsAndSubCriterions, null, null, values, 0);
	}

	@SafeVarargs
	public RootBucket(final List<V> values, @NonNull final Function<V, Object>... partitionCriterionsAndSubCriterions) {
		this(values, Arrays.asList(partitionCriterionsAndSubCriterions));
	}

	public <W> CopyBucket<V, W> createBucketWithNewValues(final Collection<V> newValuesBase,
			final Collector<V, W, W> collectorWithoutFinisher, final Collector<W, W, W> collectorWithoutTransformer) {
		return new CopyBucket<>(this, newValuesBase, collectorWithoutFinisher, collectorWithoutTransformer);
	}

	@Override
	public boolean test(final V v) {
		return true;
	}

	@Override
	protected Collection<V> filter(final Collection<V> l) {
		return l;
	}
}
