package com.snap252.vaadin.pivot.valuegetter;

import java.util.function.BiFunction;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.Arithmetics;
import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NullableArithmeticsWrapper;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.vaadin.pivot.AbstractFilteringComponent;
import com.snap252.vaadin.pivot.NameType;

public abstract class AbstractNumberValueGetterRenderingComponent<T extends Number & Comparable<T>>
		extends AbstractFilteringComponent<T> implements FilteringRenderingComponent<NumberStatistics<T>> {

	public AbstractNumberValueGetterRenderingComponent(final NameType nameType) {
		super(nameType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collector<Object, MutableValue<@Nullable T>, @Nullable NumberStatistics<@Nullable T>> getAggregator(
			final BiFunction<Object, Object, Object> f) {
		return PivotCollectors.<@NonNull Object, @Nullable T>getNumberReducer(x -> (T) f.apply(x, propertyId), new NullableArithmeticsWrapper<>(createArithmetics()));
	}

	protected abstract Arithmetics<T> createArithmetics();

}
