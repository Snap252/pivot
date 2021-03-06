package com.snap252.vaadin.pivot;

import java.util.Collection;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public abstract class PropertyProvider<X, @NonNull Z extends Property<X, ?>> {
	public abstract Collection<Z> getProperties();

	public abstract Stream<X> getItems();

	public Z getProperty(final String attributeName) {
		return getProperties().stream().filter(x -> x.getName().equals(attributeName)).findFirst().orElseThrow(
				() -> new IllegalArgumentException(attributeName + " does not exist in " + getProperties()));
	}
}
