package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.themes.ValoTheme;

@NonNullByDefault
public class StringFilteringComponent extends AbstractFilteringComponent<String> {
	private final FormLayout comp;
	private final Slider slider;

	public StringFilteringComponent(final NameType nameType) {
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
	public String round(final String ret) {
		final int sliderValue = slider.getValue().intValue();
		if (sliderValue > 0 && sliderValue < ret.length()) {
			return ret.substring(0, sliderValue);
		}
		return ret;
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener l) {
		slider.addValueChangeListener(l);
	}

	@Override
	public @NonNull String toString() {
		final Double v = slider.getValue();
		if (v == null || v.intValue() == 0)
			return super.toString();
		return super.toString() + " (" + v.intValue() + ")";
	}

	@Override
	public @NonNull String getButtonStyles() {
		final Double v = slider.getValue();
		if (v == null || v.intValue() == 0)
			return super.getButtonStyles();
		return ValoTheme.BUTTON_FRIENDLY;
	}
}
