package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;

public class IntegerValueField extends DecimalValueField {

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
	}

	@Override
	protected Number roundImpl(final Number input) {
		assert input instanceof Integer;
		return BigDecimal.valueOf(input.intValue());
	}
}
