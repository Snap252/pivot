package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.Property;

//something to do here before we can use it
@Deprecated
public class IntValueExtractor<INPUT_TYPE> extends BigDecimalValueExtractor<INPUT_TYPE> {
	public IntValueExtractor(final Property<INPUT_TYPE, @Nullable BigDecimal> nameType) {
		super(nameType);
	}

}
