package com.snap252.vaadin.pivot;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class FilterFactory {
	FilteringComponent<?, ?> createFilter(final Property<?, ?> n) {

		if (n.getType() == String.class)
			return new StringFilteringComponent(n);

		if (Date.class.isAssignableFrom(n.getType()))
			return new DateFilteringComponent(n);
		if (Number.class.isAssignableFrom(n.getType()))
			return new NumberFilteringComponent(n);

		if (n.getType().isEnum())
			return new EnumFilteringComponent(n);

		throw new AssertionError(n.getType());
	}

}
