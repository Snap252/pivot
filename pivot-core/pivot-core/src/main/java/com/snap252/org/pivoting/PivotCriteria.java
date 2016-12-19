package com.snap252.org.pivoting;

import java.util.Comparator;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@FunctionalInterface
public interface PivotCriteria<F, T extends Comparable<T>>
		extends Function<@NonNull F, @NonNull T>, Comparator<@NonNull T> {

	@Override
	default int compare(final T f1, final T f2) {
		final Comparable<T> c1 = f1;
		return c1.compareTo(f2);
	}

	default @Nullable String getStyleClass() {
		return null;
	}
}
