package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;

public class SimpleCountingAggregator extends Aggregator<BigDecimal, BigDecimal> {

	@Override
	public BigDecimal getConvertedValue(final BigDecimal value) {
		return value;
	}

	@Override
	public BigDecimalRenderer createRenderer() {
		final BigDecimalRenderer bigDecimalRenderer = new BigDecimalRenderer("---");
		bigDecimalRenderer.setFormat("0");
		return bigDecimalRenderer;
	}

	@Override
	public Collector<?, ?, BigDecimal> getCollector() {
		return Collectors.collectingAndThen(Collectors.counting(), BigDecimal::valueOf);
	}

}
