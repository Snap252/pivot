package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

import com.snap252.vaadin.pivot.NameType;
import com.vaadin.data.Item;

public class IntValueExtractor extends BigDecimalValueExtractor {
	public IntValueExtractor(final NameType nameType) {
		super(nameType);
	}

	@Override
	protected BigDecimal getValueIntern(final Item item) {
		return new BigDecimal((int) getValueFromItem(item));
	}
}
