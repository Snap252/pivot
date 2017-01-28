package com.snap252.vaadin.pivot;

import java.math.BigDecimal;

public class ValueFactory {
	FilteringComponent<?> createFilter(final NameType n) {
		if (BigDecimal.class.isAssignableFrom(n.type))
			return new BigDecimalValueExtractor(n);
		if (Integer.class.isAssignableFrom(n.type))
			return new IntValueExtractor(n);

		// assert false : n.type;
		return null;
	}

}
