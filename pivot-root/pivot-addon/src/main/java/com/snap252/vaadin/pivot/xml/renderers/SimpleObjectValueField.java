package com.snap252.vaadin.pivot.xml.renderers;

import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.snap252.vaadin.pivot.PropertyProvider;
import com.snap252.vaadin.pivot.UIConfigurable;

public class SimpleObjectValueField extends ValueField<Object> {
	public SimpleObjectValueField() {
		super(new SimpleCountingAggregator());
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		assert false;
		return () -> null;
	}

	@Override
	public <INPUT_TYPE> Collector<INPUT_TYPE, ?, ?> createMappingFunctionCriteria(
			final PropertyProvider<INPUT_TYPE, ?> pp) {
		return Collectors.mapping(Function.identity(), agg.getCollector());
	}
}
