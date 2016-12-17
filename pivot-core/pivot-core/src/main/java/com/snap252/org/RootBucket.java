package com.snap252.org;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class RootBucket<V> extends SubBucket<V> {

	public RootBucket(List<V> values, List<Function<V, Object>> partitionCriterionsAndSubCriterions) {
		super("---root---", partitionCriterionsAndSubCriterions, null, null, values, 0);
	}

	@SafeVarargs
	public RootBucket(List<V> values, Function<V, Object>... partitionCriterionsAndSubCriterions) {
		this(values, Arrays.asList(partitionCriterionsAndSubCriterions));
	}

	public Bucket<V> createBucketWithNewValues(Collection<V> newValuesBase) {
		return new CopyBucket<>(this, newValuesBase);
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
