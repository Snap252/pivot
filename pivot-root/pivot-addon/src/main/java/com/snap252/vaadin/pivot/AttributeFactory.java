package com.snap252.vaadin.pivot;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.DateAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.DateRounding;
import com.snap252.vaadin.pivot.xml.bucketextractors.NumberAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.ObjectAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.StringAttribute;

@NonNullByDefault
public class AttributeFactory {
	<INPUT_TYPE> Attribute<?> createAttribute(final Property<INPUT_TYPE, ?> n) {
		final Attribute<?> ret = createAttributeImpl(n);
		ret.attributeName = n.getName();
		return ret;
	}

	protected <INPUT_TYPE> Attribute<?> createAttributeImpl(final Property<INPUT_TYPE, ?> n) {
		if (n.getType() == String.class)
			return new StringAttribute();

		if (Date.class.isAssignableFrom(n.getType()))
			return new DateAttribute(DateRounding.MONTH_SHORT);
		if (Number.class.isAssignableFrom(n.getType()))
			return new NumberAttribute();

		return new ObjectAttribute();
	}

}
