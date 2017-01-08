package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;

@NonNullByDefault
public class StringFilteringComponent extends AbstractFilteringComponent<String> {
	private FormLayout comp;
	private Slider slider;

	public StringFilteringComponent(NameType nameType) {
		super(nameType);
		FormLayout formLayout = new FormLayout();
		slider = new Slider(0, 10);
		slider.setCaption("SubString");
		formLayout.addComponent(slider);
		formLayout.setWidth("200px");
		formLayout.addComponent(new Button("Close"));
		this.comp = formLayout;
	}

	@Override
	public @NonNull AbstractComponent getComponent(PopupButton b) {
		return comp;
	}

	@SuppressWarnings("null")
	@Override
	public PivotCriteria<Item, String> getCriteria() {
		return item -> {
			int sliderValue = slider.getValue().intValue();
			String ret = (String) item.getItemProperty(propertyId).getValue();
			if (sliderValue > 0 && sliderValue < ret.length()) {
				return ret.substring(0, sliderValue);
			}
			return ret;
		};
	}

	@Override
	public void addValueChangeListener(ValueChangeListener l) {
		slider.addValueChangeListener(l);
	}

	@Override
	public @NonNull String toString() {
		Double v = slider.getValue();
		if (v == null || v.intValue() == 0)
			return super.toString();
		return super.toString() + " (" + v.intValue() + ")";
	}
}
