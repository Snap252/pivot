package com.snap252.org;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;

public class RootBucket<V> extends SubBucket<V> {

	public RootBucket(List<V> values, List<Function<V, Object>> partitionCriterionsAndSubCriterions) {
		super("---root---", partitionCriterionsAndSubCriterions, null, null, values, 0);
	}

	@SafeVarargs
	public RootBucket(List<V> values, Function<V, Object>... partitionCriterionsAndSubCriterions) {
		this(values, Arrays.asList(partitionCriterionsAndSubCriterions));
	}

	public <W> CopyBucket<V, W> createBucketWithNewValues(Collection<V> newValuesBase, Collector<V, W, W> collector) {
		Collector<W, W, W> collector2 = Collector.of(collector.supplier(), collector.combiner()::apply,
				collector.combiner());

		
		return new CopyBucket<>(this, newValuesBase, collector, collector2);
	}

	@Override
	public String getParentString() {
		// return super.getParentString();
		return "<r>";
	}

	@Override
	public boolean test(V v) {
		return true;
	}

	@Override
	protected Collection<V> filter(Collection<V> l) {
		return l;
	}
}
