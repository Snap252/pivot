package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.xml.renderers.DecimalValueField;
import com.snap252.vaadin.pivot.xml.renderers.IntegerValueField;
import com.snap252.vaadin.pivot.xml.renderers.ObjectValueField;
import com.snap252.vaadin.pivot.xml.renderers.ValueField;

public class ValueFactory {
	protected ValueField<?> createFilter(final Property<?, @Nullable ?> n) {
		final ValueField<?> ret = createImpl(n);
		ret.attributeName = n.getName();
		return ret;
	}

	private final ValueField<?> createImpl(final Property<?, ?> n) {

		if (BigDecimal.class.isAssignableFrom(n.getType()))
			return new DecimalValueField();

		if (Integer.class.isAssignableFrom(n.getType()))
			return new IntegerValueField();

		return new ObjectValueField();

	}
}
