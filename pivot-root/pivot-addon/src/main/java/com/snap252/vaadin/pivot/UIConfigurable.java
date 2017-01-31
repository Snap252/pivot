package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;

@NonNullByDefault
public interface UIConfigurable {
	@Nullable
	abstract AbstractComponent getComponent();

	abstract void addValueChangeListener(ValueChangeListener l);

	default String getButtonStyles() {
		return "";
	}
}
