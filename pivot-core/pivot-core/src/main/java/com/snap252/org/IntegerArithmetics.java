package com.snap252.org;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
final class IntegerArithmetics implements Arithmetics<Integer> {
	@Override
	public Integer add(Integer n1, Integer n2) {
		return n1 + n2;
	}

	@Override
	public Integer mul(Integer n1, Integer n2) {
		return n1 * n2;
	}

	@Override
	public Integer part(Integer n1, int n2) throws ArithmeticException {
		return n1 / n2;
	}

	@Override
	public Integer negate(Integer n) {
		return -n;
	}

	@Override
	public int compare(Integer n1, Integer n2) {
		return n1.compareTo(n2);
	}
}