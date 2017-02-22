package com.snap252.org.pivoting;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class RootBucket<V> extends SubBucket<V> {

	public RootBucket(final String prefix, final List<V> values,
			final List<? extends PivotCriteria<V, @Nullable ?>> partitionCriterionsAndSubCriterions) {
		super(prefix, partitionCriterionsAndSubCriterions, null, createUniquePivotCriteria(), values, 0);
	}

	public static <V> NamedPivotCriteria<V, @NonNull String> createUniquePivotCriteria() {
		return new NamedPivotCriteria<V, String>(_ignore -> "", "r");
	}

	@SafeVarargs
	public RootBucket(final String prefix, final List<V> values,
			@NonNull final PivotCriteria<V, ?>... partitionCriterionsAndSubCriterions) {
		this(prefix, values, Arrays.asList(partitionCriterionsAndSubCriterions));
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
