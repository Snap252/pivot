package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

import com.snap252.vaadin.pivot.Property;

public class ValueFactory {
	protected <INPUT_TYPE> FilteringRenderingComponent<INPUT_TYPE, ?> createFilter(final Property<INPUT_TYPE, ?> n) {

		if (BigDecimal.class.isAssignableFrom(n.getType()))
			return new BigDecimalValueExtractor<>((Property<INPUT_TYPE, BigDecimal>) n);

		if (Integer.class.isAssignableFrom(n.getType()))
			return new IntValueExtractor<>((Property<INPUT_TYPE, BigDecimal>) n);

		return new ObjectValueExtractor<>(n);
	}
}
