package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

import com.snap252.vaadin.pivot.Property;

public class ValueFactory {
	protected FilteringRenderingComponent<?> createFilter(final Property n) {
		if (BigDecimal.class.isAssignableFrom(n.getType()))
			return new BigDecimalValueExtractor(n);
		if (Integer.class.isAssignableFrom(n.getType()))
			return new IntValueExtractor(n);

		return new ObjectValueExtractor(n);
	}
}
