package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;

@NonNullByDefault
public class StringFilteringComponent<INPUT_TYPE> extends AbstractFilteringComponent<INPUT_TYPE, @Nullable String> {
	private final FormLayout comp;
	private final Slider slider;

	public StringFilteringComponent(final Property<INPUT_TYPE, @Nullable String> nameType) {
		super(nameType);
		final FormLayout formLayout = new FormLayout();
		slider = new Slider(0, 10);
		slider.setCaption("SubString");
		formLayout.addComponent(slider);
		formLayout.setWidth("200px");
		this.comp = formLayout;
	}

	@Override
	public @NonNull AbstractComponent getComponent() {
		return comp;
	}

	@Override
	public @Nullable String round(final @Nullable String ret) {
		if (ret == null)
			return null;
		final int sliderValue = slider.getValue().intValue();
		if (sliderValue > 0 && sliderValue < ret.length()) {
			return ret.substring(0, sliderValue);
		}
		return ret;
	}

	@Override
	public @NonNull String toString() {
		final Double v = slider.getValue();
		if (v == null || v.intValue() == 0)
			return super.toString();
		return super.toString() + " (" + v.intValue() + ")";
	}
}
