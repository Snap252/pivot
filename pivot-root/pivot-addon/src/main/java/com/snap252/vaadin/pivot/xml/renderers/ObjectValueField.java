package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.UIConfigurable;

public class ObjectValueField extends ValueField<Object> {
	public ObjectValueField() {
		super(new CountingAggregator());
	}

	@XmlElements(@XmlElement(name = "counting", type = CountingAggregator.class))
	@Override
	public void setAggregator(@NonNull final Aggregator<?, ?> agg) {
		this.agg = agg;
	}

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
	}

	@Override
	protected @NonNull Object roundImpl(@NonNull final Object input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NonNull UIConfigurable createUIConfigurable() {
		// TODO Auto-generated method stub
		return null;
	}
}
