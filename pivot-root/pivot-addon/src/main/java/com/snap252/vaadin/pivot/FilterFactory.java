package com.snap252.vaadin.pivot;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class FilterFactory {
	FilteringComponent<?> createFilter(final NameType n) {

		if (n.type == String.class)
			return new StringFilteringComponent(n);

		if (Date.class.isAssignableFrom(n.type))
			return new DateFilteringComponent(n);
		if (Number.class.isAssignableFrom(n.type))
			return new NumberFilteringComponent<>(n);

		if (n.type.isEnum())
			return new EnumFilteringComponent<>(n);

		throw new AssertionError(n.type);
	}

}
