package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;

@NonNullByDefault
public class NumberFilteringComponent<T extends Number & Comparable<T>> extends AbstractFilteringComponent<T> {
	private FormLayout comp;
	private Slider slider;
	private int sliderValue;

	public NumberFilteringComponent(NameType nameType) {
		super(nameType);
		FormLayout formLayout = new FormLayout();
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
	public T apply(Item item) {
		T ret = super.apply(item);
		if (sliderValue > 0) {
			int exp = (int) Math.pow(10, sliderValue);
			Integer i = ret.intValue() / exp * exp;
			return (T) i;
		}
		return ret;
	}

	@Override
	public void addValueChangeListener(ValueChangeListener l) {
		slider.addValueChangeListener(l);
	}

	@Override
	public String toString() {
		if (sliderValue == 0)
			return super.toString();
		return super.toString() + " (" + sliderValue + ")";
	}

}
