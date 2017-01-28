package com.snap252.org.aggregators;

import java.text.MessageFormat;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault({})
public final class NumberStatistics<N extends Number> {
	public final int cnt;
	public final N max;
	public final N min;
	public final N sum;
	public final N sumSqr;

	private final Arithmetics<N> arithmetics;

	/**
	 * Creates an atomic cell.
	 */
	public NumberStatistics(final N n, final Arithmetics<N> arithmetics) {
		this.arithmetics = arithmetics;
		max = min = sum = n;
		sumSqr = arithmetics.sqr(n);
		cnt = 1;
	}

	public NumberStatistics(final int cnt, final N max, final N min, final N sum, final N sumSqr,
			final Arithmetics<N> arithmetics) {
		this.cnt = cnt;
		this.max = max;
		this.min = min;
		this.sum = sum;
		this.sumSqr = sumSqr;
		this.arithmetics = arithmetics;
	}

	public N avg() {
		return arithmetics.part(sum, cnt);
	}

	public N varianz() {
		return arithmetics.varianz(sum, sumSqr, cnt);
	}

	private static final MessageFormat MF = new MessageFormat(
			"NumberStatistics [Anzahl={0}; max={1}; min={2}; sum={3}; avg={5}; var={6}]");

	@Override
	public String toString() {
		return MF.format(new Object[] { cnt, max, min, sum, sumSqr, avg(), varianz() });
	}

}
