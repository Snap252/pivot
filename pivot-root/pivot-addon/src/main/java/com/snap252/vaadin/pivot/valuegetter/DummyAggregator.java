package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.PivotCellReference;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;

//TODO: extract
public class DummyAggregator implements ModelAggregtor<Object> {

	@Override
	public Collector<Object, ?, BigDecimal> getAggregator(final BiFunction<Object, Object, Object> f) {
		return Collectors.collectingAndThen(Collectors.counting(), BigDecimal::valueOf);
	}

	@SuppressWarnings("null")
	@Override
	public RendererConverter<?, ? extends @NonNull BigDecimal> createRendererConverter() {
		final BigDecimalRenderer renderer = new BigDecimalRenderer("-");
		renderer.setFormat("0");

		return new RendererConverter<BigDecimal, BigDecimal>(renderer, PivotCellReference::getValue,
				BigDecimal.class);
	}
}