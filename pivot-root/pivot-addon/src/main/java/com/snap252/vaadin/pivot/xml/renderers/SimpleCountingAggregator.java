package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Locale;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.vaadin.ui.renderers.NumberRenderer;

public class SimpleCountingAggregator extends Aggregator<Integer, Integer> {

	@Override
	public Integer getConvertedValue(final Integer value) {
		return value;
	}

	@Override
	public NumberRenderer createRenderer() {
		return new NumberRenderer("%s", Locale.getDefault(), nullRepresentation);
	}

	@Override
	public Collector<?, ?, Integer> getCollector() {
		return Collectors.collectingAndThen(Collectors.counting(), Long::intValue);
	}

}
