package com.snap252.vaadin.pivot.xml.data;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.FilteringComponent;
import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;

@NonNullByDefault
public class FilteringComponentImpl<INPUT_TYPE, DATA_TYPE>
		implements FilteringComponent<INPUT_TYPE, @Nullable DATA_TYPE>, UIConfigurable {
	private final Property<INPUT_TYPE, @Nullable DATA_TYPE> property;
	private final UIConfigurable uiConfigurable;

	public FilteringComponentImpl(final Property<INPUT_TYPE, @Nullable DATA_TYPE> property,
			final UIConfigurable uiConfigurable) {
		this.property = property;
		this.uiConfigurable = uiConfigurable;
	}

	@Override
	public Property<INPUT_TYPE, @Nullable DATA_TYPE> getProperty() {
		return property;
	}

	@Override
	public @Nullable AbstractComponent getComponent() {
		return uiConfigurable.getComponent();
	}

	@Override
	public void addValueChangeListener(@NonNull final ValueChangeListener valueChangeListener) {
		uiConfigurable.addValueChangeListener(valueChangeListener);
	}

	@Override
	public String getButtonStyles() {
		return uiConfigurable.getButtonStyles();
	}

}
