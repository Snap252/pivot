package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

import com.snap252.vaadin.pivot.NameType;

public class ValueFactory {
	protected FilteringRenderingComponent<?> createFilter(final NameType n) {
		if (BigDecimal.class.isAssignableFrom(n.type))
			return new BigDecimalValueExtractor(n);
		if (Integer.class.isAssignableFrom(n.type))
			return new IntValueExtractor(n);

		return new ObjectValueExtractor(n);
	}

	protected boolean isAccepted(final NameType n) {
		return true;
	}

}
