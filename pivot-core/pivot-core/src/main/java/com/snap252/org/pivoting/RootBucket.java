package com.snap252.org.pivoting;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class RootBucket<V> extends SubBucket<V> {

	private final ShowingSubtotal subTotal;

	public RootBucket(final String prefix, final List<V> values,
			final List<? extends PivotCriteria<V, @Nullable ?>> partitionCriterionsAndSubCriterions,
			final boolean showBefore) {
		super(prefix, partitionCriterionsAndSubCriterions, null, createUniquePivotCriteria(), values, 0);
		this.subTotal = showBefore ? ShowingSubtotal.BEFORE : ShowingSubtotal.AFTER;
	}

	public static <V> NamedPivotCriteria<V, @Nullable String> createUniquePivotCriteria() {
		return new NamedPivotCriteria<V, @Nullable String>(_ignore -> "", "r");
	}

	@Override
	public boolean test(final V v) {
		return true;
	}

	@Override
	public Collection<V> filter(final Collection<V> l) {
		return l;
	}

	/**
	 * Used for inheritance
	 */
	@Override
	protected ShowingSubtotal getSubTotal() {
		return subTotal;
	}

	/**
	 * Always stream for having self at the end.
	 */
	@Override
	public Stream<@NonNull ? extends Bucket<V>> stream() {
		return streamWithSubTotals(ShowingSubtotal.AFTER);
	}
}
