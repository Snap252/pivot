package com.snap252.org.aggregators;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault({})
public interface Arithmetics<N extends Number> {

	N getNeutralAddElement();

	N add(N n1, N n2);

	default N sub(final N n1, final N n2) {
		return add(n1, negate(n2));
	};

	N mul(N n1, N n2);

	default N sqr(final N n) {
		return mul(n, n);
	}

	N part(N n1, int n2) throws ArithmeticException;

	N multi(N n1, int n2);

	N negate(N n);

	int compare(N n1, N n2);

	default N varianz(final N sum, final N sumSqr, final int cnt) {
		assert cnt != 0;
		// see:
		// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance

		// return part(sub(sumSqr, part(sqr(sum), cnt)), cnt);
		if (cnt == 1)
			return getNeutralAddElement();
		return part(sub(sumSqr, part(sqr(sum), cnt)), cnt - 1);

		// double doubleSum = sum.doubleValue();
		// return (sumSqr.doubleValue() - doubleSum * doubleSum / cnt) / cnt;

	}

	default N min(final N n1, final N n2) {
		return compare(n1, n2) <= 0 ? n1 : n2;
	};

	default N max(final N n1, final N n2) {
		return compare(n1, n2) >= 0 ? n1 : n2;
	};

}
