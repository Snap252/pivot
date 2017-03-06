package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.UIConfigurable;

public class StringValueField extends ValueField<String> {
	public StringValueField() {
		super(new CountingAggregator());
	}

	@XmlElements({ @XmlElement(name = "counting", type = CountingAggregator.class),
			@XmlElement(name = "concat", type = StringConcatAggregator.class) })
	@Override
	public void setAggregator(@NonNull final Aggregator<?, ?> agg) {
		super.setAggregator(agg);
	}

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
	}

	@Override
	protected String roundImpl(final String input) {
		return input;
	}

	@Override
	public @NonNull UIConfigurable createUIConfigurable() {
		// TODO Auto-generated method stub
		return null;
	}
}
