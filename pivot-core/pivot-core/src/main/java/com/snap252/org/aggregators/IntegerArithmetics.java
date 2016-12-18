package com.snap252.org.aggregators;

import org.eclipse.jdt.annotation.NonNull;

final class IntegerArithmetics implements Arithmetics<Integer> {
	@Override
	public Integer add(final Integer n1, final Integer n2) {
		return n1 + n2;
	}

	@Override
	public Integer mul(final Integer n1, final Integer n2) {
		return n1 * n2;
	}

	/**
	 * You should not use this in case of integer arithmetics.
	 */
	@Override
	@Deprecated
	public Integer part(final Integer n1, final int n2) throws ArithmeticException {
		return n1 / n2;
	}

	@Override
	public Integer negate(final Integer n) {
		return -n;
	}

	@Override
	public @NonNull Integer sub(final Integer n1, final Integer n2) {
		return n1 - n2;
	}

	@Override
	public int compare(final Integer n1, final Integer n2) {
		return n1.compareTo(n2);
	}

	@Override
	public Integer getNeutralAddElement() {
		return 0;
	}

	@Override
	public Integer multi(final Integer n1, final int n2) {
		return n1 * n2;
	}
}