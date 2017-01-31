package com.snap252.vaadin.pivot.valuegetter;

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
import com.vaadin.data.Item;

public abstract class AbstractNumberValueGetterRenderingComponent<T extends Number & Comparable<T>>
		extends AbstractFilteringComponent<T> implements FilteringRenderingComponent<NumberStatistics<T>> {

	public AbstractNumberValueGetterRenderingComponent(final NameType nameType) {
		super(nameType);
	}

	@Override
	public Collector<Item, MutableValue<@Nullable T>, @Nullable NumberStatistics<@Nullable T>> getAggregator() {
		return PivotCollectors.<@NonNull Item, @Nullable T>getNumberReducer(this::apply,
				new NullableArithmeticsWrapper<>(createArithmetics()));
	}

	protected abstract Arithmetics<T> createArithmetics();
}
