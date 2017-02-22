package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.Property;

public class ValueFactory {
	@SuppressWarnings({ "unchecked" })
	protected <INPUT_TYPE> FilteringRenderingComponent<INPUT_TYPE, @Nullable ?> createFilter(
			final Property<INPUT_TYPE, @Nullable ?> n) {

		if (BigDecimal.class.isAssignableFrom(n.getType()))
			return new BigDecimalValueExtractor<>((Property<INPUT_TYPE, @Nullable BigDecimal>) n);

		if (Integer.class.isAssignableFrom(n.getType()))
			return new IntValueExtractor<>((Property<INPUT_TYPE, @Nullable BigDecimal>) n);

		return new ObjectValueExtractor<>((Property<INPUT_TYPE, @Nullable Object>) n);
	}
}
