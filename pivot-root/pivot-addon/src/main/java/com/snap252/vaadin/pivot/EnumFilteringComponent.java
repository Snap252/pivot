package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.ui.AbstractComponent;

@NonNullByDefault
public class EnumFilteringComponent<INPUT_TYPE, T extends Enum<T>>
		extends AbstractFilteringComponent<INPUT_TYPE, @Nullable T> {
	public EnumFilteringComponent(final Property<INPUT_TYPE, @Nullable T> nameType) {
		super(nameType);
	}

	@Override
	public @Nullable AbstractComponent getComponent() {
		return null;
	}

}
