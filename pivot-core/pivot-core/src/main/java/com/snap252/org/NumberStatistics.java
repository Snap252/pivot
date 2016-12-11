package com.snap252.org;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class NumberStatistics<N extends Number & Comparable<N>> {
	public final int cnt;
	public final N max;
	public final N min;
	public final N sum;
	public final N sumSqr;

	private static final NumberStatistics<?> NEUTRAL_ELEMENT = new NumberStatistics<Integer>(-1, -1);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <N extends Number & Comparable<N>> NumberStatistics<N> getNeutralElement() {
		return (NumberStatistics) NEUTRAL_ELEMENT;
	}

	private static <N extends Number & Comparable<N>> NumberStatistics<N> aggregate(NumberStatistics<N> ns1,
			NumberStatistics<N> ns2, BinaryOperator<N> addFunction) {
		assert ns2 != NEUTRAL_ELEMENT;

		if (ns1 == NEUTRAL_ELEMENT) {
			assert ns2 != NEUTRAL_ELEMENT;
			return ns2;
		}

		return new NumberStatistics<N>(ns1, ns2, addFunction);
	}

	private NumberStatistics(NumberStatistics<N> ns1, NumberStatistics<N> ns2, BinaryOperator<N> addFunction) {
		cnt = ns1.cnt + ns2.cnt;
		this.min = ns1.min.compareTo(ns2.min) < 0 ? ns1.min : ns2.min;
		this.max = ns1.max.compareTo(ns2.max) > 0 ? ns1.max : ns2.max;

		this.sum = addFunction.apply(ns1.sum, ns2.sum);
		this.sumSqr = addFunction.apply(ns1.sumSqr, ns2.sumSqr);
	}

	public static <N extends Number & Comparable<N>> Collector<NumberStatistics<N>, ?, NumberStatistics<N>> getReducer(
			BinaryOperator<N> addFnkt) {
		return Collectors.reducing(NumberStatistics.getNeutralElement(), Function.identity(),
				(f1, f2) -> aggregate(f1, f2, addFnkt));
	}

	public NumberStatistics(N n, N sqr) {
		max = min = sum = n;
		sumSqr = sqr;
		cnt = 1;
	}

	public NumberStatistics(int cnt, N max, N min, N sum, N sumSqr) {
		this.cnt = cnt;
		this.max = max;
		this.min = min;
		this.sum = sum;
		this.sumSqr = sumSqr;
	}

	@Nullable
	public Double avg() {
		if (this == NEUTRAL_ELEMENT)
			return null;

		return sum.doubleValue() / cnt;
	}

	@Nullable
	public Double varianz() {
		if (this == NEUTRAL_ELEMENT)
			return null;

		final Double avg = avg();
		assert avg != null;
		return Math.sqrt((sumSqr.doubleValue() - avg * avg) / cnt);
	}

	@Override
	public String toString() {
		return "NumberStatistics [cnt=" + cnt + ", max=" + max + ", min=" + min + ", sum=" + sum + ", sumSqr=" + sumSqr
				+ ", avg()=" + avg() + ", varianz()=" + varianz() + "]";
	}

}
