package com.snap252.org.pivoting;

import java.util.Comparator;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;

@FunctionalInterface
public interface PivotCriteria<F, T> extends Function<@NonNull F, @NonNull T>, Comparator<@NonNull T> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	default int compare(final T f1, final T f2) {
		if (f1 instanceof Comparable) {
			final Comparable comparable = (Comparable) f1;
			return comparable.compareTo(f2);

		}
		return 0;
	}
}
