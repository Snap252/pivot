package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IntegerValueField extends DecimalValueField {

	@Override
	protected BigDecimal roundImpl(final Number input) {
		assert input instanceof Integer || input instanceof Long || input instanceof BigInteger || input instanceof Byte;
		return BigDecimal.valueOf(input.longValue());
	}
}
