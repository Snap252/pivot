package com.snap252.org;

import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class NumberStatistics<N extends Number> {
	public final int cnt;
	public final N max;
	public final N min;
	public final N sum;
	public final N sumSqr;

	private static final NumberStatistics<?> NEUTRAL_ELEMENT = new NumberStatistics<Integer>(0,
			new IntegerArithmetics()) {
		@Override
		public String toString() {
			return "---";
		}

		public boolean isNeutralElement() {
			return true;
		}
	};
	private Arithmetics<N> arithmetics;

	public boolean isNeutralElement() {
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <N extends Number> NumberStatistics<N> getNeutralElement() {
		return (NumberStatistics) NEUTRAL_ELEMENT;
	}

	private static <N extends Number> NumberStatistics<N> aggregate(NumberStatistics<N> ns1, NumberStatistics<N> ns2,
			Arithmetics<N> arithmetics) {
		assert ns2 != NEUTRAL_ELEMENT;

		if (ns1 == NEUTRAL_ELEMENT) {
			assert ns2 != NEUTRAL_ELEMENT;
			return ns2;
		}

		return new NumberStatistics<N>(ns1, ns2, arithmetics);
	}

	private NumberStatistics(NumberStatistics<N> ns1, NumberStatistics<N> ns2, Arithmetics<N> arithmetics) {
		this.arithmetics = arithmetics;
		cnt = ns1.cnt + ns2.cnt;
		this.min = arithmetics.compare(ns1.min, ns2.min) < 0 ? ns1.min : ns2.min;
		this.max = arithmetics.compare(ns1.max, ns2.max) > 0 ? ns1.max : ns2.max;

		this.sum = arithmetics.add(ns1.sum, ns2.sum);
		this.sumSqr = arithmetics.add(ns1.sumSqr, ns2.sumSqr);
	}

	public static <N extends Number, V> Collector<V, ?, NumberStatistics<N>> getReducer(Function<V, N> valueExtractor,
			Arithmetics<N> arithmetics) {
		Function<V, NumberStatistics<N>> transformer = valueExtractor.andThen(n -> new NumberStatistics<>(n, arithmetics));
		return Collectors.reducing(getNeutralElement(), transformer, (f1, f2) -> aggregate(f1, f2, arithmetics));
	}

	/**
	 * Creates an atomic cell.
	 */
	public NumberStatistics(N n, Arithmetics<N> arithmetics) {
		this.arithmetics = arithmetics;
		max = min = sum = n;
		sumSqr = arithmetics.mul(n, n);
		cnt = 1;
	}

	public NumberStatistics(int cnt, N max, N min, N sum, N sumSqr, Arithmetics<N> arithmetics) {
		this.cnt = cnt;
		this.max = max;
		this.min = min;
		this.sum = sum;
		this.sumSqr = sumSqr;
		this.arithmetics = arithmetics;
	}

	public N avg() {
		assert !isNeutralElement();
		return arithmetics.part(sum, cnt);
	}

	public N varianz() {
		assert !isNeutralElement();
		// see:
		// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance

		return arithmetics.varianz(sum, sumSqr, cnt);
	}

	@Override
	public String toString() {
		assert !isNeutralElement();
		return "NumberStatistics [cnt=" + cnt + ", max=" + max + ", min=" + min + ", sum=" + sum + ", sumSqr=" + sumSqr
				+ ", avg()=" + avg() + ", varianz()=" + varianz() + "]";
		// return "NumberStatistics [cnt=" + cnt + ", max=" + max + ", min=" +
		// min + ", sum=" + sum + ", sumSqr=" + sumSqr
		// + ", avg()=" + avg() + ", varianz()=" + varianz() + "]";
	}

}
