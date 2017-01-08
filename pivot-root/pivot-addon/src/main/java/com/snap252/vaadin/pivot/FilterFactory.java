package com.snap252.vaadin.pivot;

import java.util.Date;

public class FilterFactory {
	FilteringComponent<?> createFilter(NameType n) {

		if (n.type == String.class)
			return new StringFilteringComponent(n);

		if (n.type == Date.class)
			return new DateFilteringComponent(n);
		if (Number.class.isAssignableFrom(n.type))
			return new NumberFilteringComponent<>(n);
		
		if (n.type.isEnum())
			return new EnumFilteringComponent<>(n);

		assert false : n.type;
		return null;
	}

}
