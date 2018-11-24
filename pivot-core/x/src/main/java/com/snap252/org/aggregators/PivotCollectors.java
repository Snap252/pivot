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
	public static <N extends Number> @NonNull Collector<N, @NonNull MutableValue<N>, @Nullable NumberStatistics<N>> getNumberReducer(
			/* final Function<RawType, N> extractorFunction, */@NonNull final Arithmetics<N> arithmetics) {

		final Supplier<@NonNull MutableValue<N>> supplier = () -> MutableValue.getNeutralElement(arithmetics);
		final BiConsumer<@NonNull MutableValue<N>, N> accumulator = MutableValue::addSingle;
		final BinaryOperator<@NonNull MutableValue<N>> combiner = MutableValue::merge;

		final Function<@NonNull MutableValue<N>, @Nullable NumberStatistics<N>> finisher = MutableValue::createNumberStatistics;

		return Collector.of(supplier, accumulator, combiner, finisher);
	}

	@NonNullByDefault({})
	public static <INPUT_TYPE, N extends Number> @NonNull Collector<INPUT_TYPE, @NonNull MutableValue<N>, @Nullable NumberStatistics<N>> getNumberReducer(
			@NonNull final Function<INPUT_TYPE, N> f, @NonNull final Arithmetics<N> arithmetics) {
		return mapping(f, getNumberReducer(arithmetics));
	}

	@NonNullByDefault({})
	public static <T, U, A, R> @NonNull Collector<T, A, R> mapping(@NonNull final Function<T, U> mapper,
			@NonNull final Collector<U, A, R> downstream) {
		final Supplier<A> supplier = downstream.supplier();
		final BiConsumer<A, U> accumulator = downstream.accumulator();

		final BinaryOperator<A> combiner = downstream.combiner();
		final Function<A, R> finisher = downstream.finisher();

		return Collector.of(supplier, (r, t) -> accumulator.accept(r, mapper.apply(t)), combiner, finisher);
	}

}
