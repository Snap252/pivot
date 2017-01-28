package com.snap252.org.aggregators;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.Nullable;

public class PivotCollectors {

	public static <RawType, N extends Number> Collector<RawType, MutableValue<N>, @Nullable NumberStatistics<N>> getBigDecimalReducer(
			final Function<RawType, N> extractorFunction, final Arithmetics<N> arithmetics) {

		final Supplier<MutableValue<N>> supplier = () -> MutableValue.getNeutralElement(arithmetics);
		final BiConsumer<MutableValue<N>, RawType> accumulator = (t, u) -> t.addSingle(extractorFunction.apply(u));
		final BinaryOperator<MutableValue<N>> combiner = MutableValue::merge;

		final Function<MutableValue<N>, @Nullable NumberStatistics<N>> finisher = MutableValue::createNumberStatistics;

		return Collector.of(supplier, accumulator, combiner, finisher);
	}

}
