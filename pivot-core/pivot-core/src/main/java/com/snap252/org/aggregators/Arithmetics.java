package com.snap252.org.aggregators;

public interface Arithmetics<N extends Number> {

	public N getNeutralAddElement();

	public N add(N n1, N n2);

	default N sub(final N n1, final N n2) {
		return add(n1, negate(n2));
	};

	public N mul(N n1, N n2);

	default N sqr(final N n) {
		return mul(n, n);
	}

	public N part(N n1, int n2) throws ArithmeticException;

	public N multi(N n1, int n2);

	public N negate(N n);

	public int compare(N n1, N n2);

	default N varianz(final N sum, final N sumSqr, final int cnt) {
		// see:
		// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance

		// return part(sub(sumSqr, part(sqr(sum), cnt)), cnt);
		return part(sub(multi(sumSqr, cnt), sqr(sum)), (cnt * cnt));

		// double doubleSum = sum.doubleValue();
		// return (sumSqr.doubleValue() - doubleSum * doubleSum / cnt) / cnt;

	}

}
