package com.snap252.org;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class RootBucket<V> extends SubBucket<V> {

	public final Function<V, Object>[] partitionCriterionsAndSubCriterions;

	@SafeVarargs
	public RootBucket(List<V> values, Function<V, Object>... partitionCriterionsAndSubCriterions) {
		super("<root>", Arrays.asList(partitionCriterionsAndSubCriterions), null, null, values);
		this.partitionCriterionsAndSubCriterions = partitionCriterionsAndSubCriterions;
	}
	
	public Bucket<V> createBucketWithNewValues(List<V> newValuesBase) {
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
	protected List<V> filter(List<V> l) {
		return l;
	}
}
