package com.snap252.vaadin.pivot.xml.renderers;

import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlTransient;

import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.PropertyProvider;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public abstract class ValueField<DATA_TYPE> extends ForAttributeAndValueField<ValueField<?>> {

	protected ValueField(final Aggregator<?, ?> defaultValue) {
		this.agg = defaultValue;
	}

	@XmlTransient
	protected Aggregator<?, ?> agg;

	public Aggregator<?, ?> getAggregator() {
		return agg;
	}

	protected static AbstractComponent createForDisplayName(final ValueField<?> att) {
		final TextField tf = createTextField(att);

		final FormLayout fl = new FormLayout(tf);
		// fl.setSizeUndefined();
		fl.setWidth(500, Unit.PIXELS);
		return fl;
	}

	public <INPUT_TYPE> Collector<INPUT_TYPE, ?, ?> createMappingFunctionCriteria(
			final PropertyProvider<INPUT_TYPE, ?> pp) {
		assert !attributeName.isEmpty();
		final Property<INPUT_TYPE, ?> property = pp.getProperty(attributeName);
		return Collectors.mapping((Function<INPUT_TYPE, ?>) property::getValue, agg.getCollector());
	}

}
