package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.UIConfigurable;

public class IntegerValueField extends ValueField<Integer> {

	public IntegerValueField() {
		super(new CountingAggregator());
	}

	@XmlElements(@XmlElement(name = "counting", type = CountingAggregator.class))
	@Override
	public void setAggregator(@NonNull final Aggregator<?, ?> agg) {
		super.setAggregator(agg);
	}

	@Override
	protected @NonNull Integer roundImpl(@NonNull final Integer input) {
		return input;
	}

	@Override
	public @NonNull UIConfigurable createUIConfigurable() {
		// TODO Auto-generated method stub
		return null;
	}

}
