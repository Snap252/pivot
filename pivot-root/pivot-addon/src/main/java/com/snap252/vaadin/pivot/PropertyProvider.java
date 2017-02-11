package com.snap252.vaadin.pivot;

import java.util.Collection;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public abstract class PropertyProvider<X,  Z extends Property> {
	public abstract Collection<Z> getProperties();

	public abstract Stream<X> getItems();

	public abstract @Nullable Object getValue(X item, Z p);
}
