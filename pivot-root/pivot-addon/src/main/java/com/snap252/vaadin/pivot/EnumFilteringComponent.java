package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;

@NonNullByDefault
public class EnumFilteringComponent<T extends Enum<T>> extends AbstractFilteringComponent<T> {
	public EnumFilteringComponent(final Property nameType) {
		super(nameType);
	}

	@Override
	public @Nullable AbstractComponent getComponent() {
		return null;
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener l) {
	}
}
