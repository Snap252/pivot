package com.snap252.org;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface Arithmetics<N extends Number> {
	public N add(N n1, N n2);

	public N mul(N n1, N n2);

	public N part(N n1, int n2) throws ArithmeticException;
	
	public N negate(N n);

	default N varianz(N sum, N sumSqr, int cnt) {
		// see:
		// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance

		return part(add(sumSqr, negate(part(mul(sum, sum), cnt))), cnt);
		
//		double doubleSum = sum.doubleValue();
//		return (sumSqr.doubleValue() - doubleSum * doubleSum / cnt) / cnt;

	};
}
