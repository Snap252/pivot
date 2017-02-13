package com.snap252.org.pivoting;

import java.util.function.Function;

public class NamedPivotCriteria<F, T> implements PivotCriteria<F, T> {

	private final String stringValue;
	private final Function<F, T> f;
	private int inverse;

	public NamedPivotCriteria(final Function<F, T> f, final String stringValue) {
		this(f, stringValue, false);
	}

	public NamedPivotCriteria(final Function<F, T> f, final String stringValue, final boolean inverse) {
		this.f = f;
		this.stringValue = stringValue;
		this.inverse = inverse ? -1 : 1;
	}

	@Override
	public String toString() {
		return stringValue;
	}

	@Override
	public T apply(final F t) {
		return f.apply(t);
	}

	@Override
	public int compare(final T f1, final T f2) {
		return inverse * PivotCriteria.super.compare(f1, f2);
	}

}
