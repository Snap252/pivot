package com.snap252.vaadin.pivot.valuegetter;

import java.util.function.Function;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.Arithmetics;
import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NullableArithmeticsWrapper;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.vaadin.pivot.AbstractFilteringComponent;
import com.snap252.vaadin.pivot.Property;

public abstract class AbstractNumberValueGetterRenderingComponent<INPUT_TYPE, @Nullable T extends @Nullable Number>
		extends AbstractFilteringComponent<INPUT_TYPE, @Nullable T>
		implements FilteringRenderingComponent<INPUT_TYPE, @Nullable NumberStatistics<T>> {

	public AbstractNumberValueGetterRenderingComponent(final Property<INPUT_TYPE, @Nullable T> nameType) {
		super(nameType);
	}

	@Override
	public Collector<INPUT_TYPE, MutableValue<T>, @Nullable NumberStatistics<T>> getAggregator() {
		final Function<INPUT_TYPE, T> f = property::getValue;
		final Arithmetics<T> arithmetics = new NullableArithmeticsWrapper<>(createArithmetics());
		return PivotCollectors.getNumberReducer(f, arithmetics);
	}

	protected abstract Arithmetics<@NonNull T> createArithmetics();

}
