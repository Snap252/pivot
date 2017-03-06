package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import com.snap252.vaadin.pivot.UIConfigurable;

public class IntegerValueField extends ValueField<Integer> {

	public IntegerValueField() {
		super(new CountingAggregator());
	}

	@XmlElements(@XmlElement(name = "counting", type = CountingAggregator.class))
	public void setAggregator(final Aggregator<?, ?> agg) {
		this.agg = agg;
	}

	@Override
	protected Integer roundImpl(final Integer input) {
		return input;
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return () -> null;
	}

}
