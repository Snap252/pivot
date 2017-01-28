package com.snap252.vaadin.pivot;

import java.math.BigDecimal;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Item;

@NonNullByDefault
public class IntValueExtractor extends BigDecimalValueExtractor {
	public IntValueExtractor(final NameType nameType) {
		super(nameType);
	}

	@Override
	protected BigDecimal getValueIntern(final Item item) {
		return new BigDecimal((int) getValueFromItem(item));
	}
}
