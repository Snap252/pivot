package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import com.snap252.vaadin.pivot.UIConfigurable;

public final class ObjectValueField extends ValueField<Object> {
	public ObjectValueField() {
		super(new CountingAggregator());
	}

	@XmlElements(@XmlElement(name = "counting", type = CountingAggregator.class))
	public void setAggregator(final Aggregator<?, ?> agg) {
		this.agg = agg;
	}

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
	}

	@Override
	protected Object roundImpl(final Object input) {
		return input;
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return () -> null;
	}
}
