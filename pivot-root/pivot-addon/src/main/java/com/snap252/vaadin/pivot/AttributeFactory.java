package com.snap252.vaadin.pivot;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.DateAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.EnumAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.NumberAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.StringAttribute;

@NonNullByDefault
public class AttributeFactory {
	<INPUT_TYPE> Attribute<?> createAttribute(final Property<INPUT_TYPE, ?> n) {
		final Attribute<?> ret = createAttributeImpl(n);
		ret.attributeName = n.getName();
		return ret;
	}

	private <INPUT_TYPE> Attribute<?> createAttributeImpl(final Property<INPUT_TYPE, ?> n) {
		if (n.getType() == String.class)
			return new StringAttribute();

		if (Date.class.isAssignableFrom(n.getType()))
			return new DateAttribute();
		if (Number.class.isAssignableFrom(n.getType()))
			return new NumberAttribute();
		if (n.getType().isEnum())
			return new EnumAttribute();

		throw new AssertionError(n.getType());

	}

}
