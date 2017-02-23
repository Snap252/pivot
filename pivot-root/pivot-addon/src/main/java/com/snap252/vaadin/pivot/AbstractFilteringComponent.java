package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public abstract class AbstractFilteringComponent<INPUT_TYPE, @Nullable DATA_TYPE>
		implements FilteringComponent<INPUT_TYPE, DATA_TYPE>, UIConfigurable {

	protected final Property<INPUT_TYPE, DATA_TYPE> property;

	@Override
	public Property<INPUT_TYPE, DATA_TYPE> getProperty() {
		return property;
	}

	public AbstractFilteringComponent(final Property<INPUT_TYPE, @Nullable DATA_TYPE> property) {
		this.property = property;
	}

	@Override
	public String toString() {
		return property.getName();
	}
}
