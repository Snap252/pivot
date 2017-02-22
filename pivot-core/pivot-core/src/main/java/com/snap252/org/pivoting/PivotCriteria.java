package com.snap252.org.pivoting;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@FunctionalInterface
public interface PivotCriteria<F, T> extends Function<@NonNull F, @Nullable T>, Comparator<@NonNull T> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	default int compare(final T f1, final T f2) {
		if (f1 instanceof Comparable) {
			final Comparable comparable = (Comparable) f1;
			return comparable.compareTo(f2);

		}
		return 0;
	}

	default @Nullable String format(final T t) {
		return t == null ? null : Objects.toString(t);
	}
}
