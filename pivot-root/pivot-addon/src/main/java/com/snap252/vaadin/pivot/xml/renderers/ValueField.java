package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;

public abstract class ValueField<DATA_TYPE> extends Attribute<@Nullable DATA_TYPE> {

	protected ValueField(final Aggregator<?, ?> defaultValue) {
		agg = defaultValue;
	}

	@XmlTransient
	protected Aggregator<?, ?> agg;

	@XmlAttribute(name = "attribute-name", required = true)
	public String name = "";

	@XmlElement
	public abstract void setAggregator(Aggregator<?, ?> agg);

	public Aggregator<?, ?> getAggregator() {
		return agg;
	}
}
