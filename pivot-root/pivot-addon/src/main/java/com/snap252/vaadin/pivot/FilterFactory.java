package com.snap252.vaadin.pivot;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class FilterFactory {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	<INPUT_TYPE> FilteringComponent<INPUT_TYPE, ?> createFilter(final Property<INPUT_TYPE, ?> n) {

		if (n.getType() == String.class)
			return new StringFilteringComponent<>((Property<INPUT_TYPE, @Nullable String>) n);

		if (Date.class.isAssignableFrom(n.getType()))
			return new DateFilteringComponent<>((Property<INPUT_TYPE, @Nullable Date>) n);
		if (Number.class.isAssignableFrom(n.getType()))
			return new NumberFilteringComponent<>((Property<INPUT_TYPE, @Nullable Number>) n);

		if (n.getType().isEnum())
			return new EnumFilteringComponent<>((Property<INPUT_TYPE, @Nullable Enum>) n);

		throw new AssertionError(n.getType());
	}

}
