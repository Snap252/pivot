package com.snap252.org.pivoting;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public class RootBucket<V> extends SubBucket<V> {

	public RootBucket(final String prefix, final List<V> values,
			final List<PivotCriteria<V, ?>> partitionCriterionsAndSubCriterions) {
		super(prefix + "---root---", partitionCriterionsAndSubCriterions, null,
				new NamedPivotCriteria<V, String>(_ignore -> "", "r") {
					@Override
					public @NonNull String getStyleClass() {
						return "root";
					}
				}, values, 0);
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
