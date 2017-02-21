package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;

@NonNullByDefault
public class NumberFilteringComponent<T extends Number> extends AbstractFilteringComponent<T> {
	private final FormLayout comp;
	private final Slider slider;
	private int sliderValue;

	public NumberFilteringComponent(final Property nameType) {
		super(nameType);
		final FormLayout formLayout = new FormLayout();
		slider = new Slider(0, 10);
		slider.setCaption("Rundung");
		formLayout.addComponent(slider);
		formLayout.setWidth("200px");
		this.comp = formLayout;
		this.sliderValue = 0;
		slider.addValueChangeListener(_ignore -> sliderValue = ((Number)_ignore.getProperty().getValue()).intValue());
	}

	@Override
	public @NonNull AbstractComponent getComponent() {
		return comp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T round(final T ret) {
		if (sliderValue > 0) {
			final int exp = (int) Math.pow(10, sliderValue);
			final Integer i = ret.intValue() / exp * exp;
			return (T) i;
		}
		return ret;
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener l) {
		slider.addValueChangeListener(l);
	}

	@Override
	public String toString() {
		if (sliderValue == 0)
			return super.toString();
		return super.toString() + " (" + sliderValue + ")";
	}

}
