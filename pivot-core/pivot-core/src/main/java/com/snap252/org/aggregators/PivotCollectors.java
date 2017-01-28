package com.snap252.org.aggregators;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

public class PivotCollectors {

	@NonNullByDefault({})
	public static <@NonNull RawType, N extends Number> @NonNull Collector<RawType, @NonNull MutableValue<N>, @Nullable NumberStatistics<N>> getNumberReducer(
			final Function<RawType, N> extractorFunction, @NonNull final Arithmetics<N> arithmetics) {

		final Supplier<@NonNull MutableValue<N>> supplier = () -> MutableValue.getNeutralElement(arithmetics);
		final BiConsumer<@NonNull MutableValue<N>, RawType> accumulator = (t, u) -> t
				.addSingle(extractorFunction.apply(u));
		final BinaryOperator<@NonNull MutableValue<N>> combiner = MutableValue::merge;

		final Function<@NonNull MutableValue<N>, @Nullable NumberStatistics<N>> finisher = MutableValue::createNumberStatistics;

		return Collector.of(supplier, accumulator, combiner, finisher);
	}

}
