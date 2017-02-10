package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public abstract class ValueField {
	
	protected ValueField(final Aggregator<?, ?> defaultValue) {
		agg = defaultValue;
	}
	@XmlTransient
	protected Aggregator<?, ?> agg;

	@XmlAttribute(name = "name", required = true)
	public String name = "";

	@XmlElement
	public abstract void setAggregator(Aggregator<?, ?> agg);
}
