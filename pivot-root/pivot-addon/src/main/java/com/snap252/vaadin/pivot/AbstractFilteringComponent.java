package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public abstract class AbstractFilteringComponent<T> implements FilteringComponent<T> {

	protected final Property<Object> property;

	@Override
	public Property<?> getProperty() {
		return property;
	}

	public AbstractFilteringComponent(final Property<?> nameType) {
		this.property = (@NonNull Property<@NonNull Object>) nameType;
	}

	@Override
	public String toString() {
		return property.toString();
	}
}
