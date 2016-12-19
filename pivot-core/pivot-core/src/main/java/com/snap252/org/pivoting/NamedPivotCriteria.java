package com.snap252.org.pivoting;

import java.util.function.Function;

public class NamedPivotCriteria<F, T extends Comparable<T>> implements PivotCriteria<F, T> {

	private final String stringValue;
	private final Function<F, T> f;

	public NamedPivotCriteria(final Function<F, T> f, final String stringValue) {
		this.f = f;
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		return stringValue;
	}

	@Override
	public T apply(final F t) {
		return f.apply(t);
	}

}
