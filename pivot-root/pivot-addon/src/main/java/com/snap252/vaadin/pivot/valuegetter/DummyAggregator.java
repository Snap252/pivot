package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.PivotCellReference;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;

public final class DummyAggregator<INPUT_TYPE> implements ModelAggregtor<INPUT_TYPE, @Nullable Object> {

	@Override
	public Collector<INPUT_TYPE, ?, BigDecimal> getAggregator() {
		return Collectors.collectingAndThen(Collectors.counting(), BigDecimal::valueOf);
	}

	@Override
	public RendererConverter<?, ? extends @NonNull BigDecimal> createRendererConverter() {
		final BigDecimalRenderer renderer = new BigDecimalRenderer("-");
		renderer.setFormat("0");

		return new RendererConverter<@Nullable BigDecimal, BigDecimal>(renderer, PivotCellReference::getValue,
				BigDecimal.class);
	}
}