package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.NonNull;

public class IntegerValueField extends ValueField {

	public IntegerValueField() {
		super(new CountingAggregator());
	}

	@XmlElements(@XmlElement(name = "counting", type = CountingAggregator.class))
	@Override
	public void setAggregator(@NonNull final Aggregator<?, ?> agg) {
		this.agg = agg;

	}

}
