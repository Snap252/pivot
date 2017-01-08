package com.snap252.vaadin.pivot;

import java.math.BigDecimal;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;

@NonNullByDefault
public class NumberFilteringComponent<T extends Number & Comparable<T>> extends AbstractFilteringComponent<T> {
	private FormLayout comp;

	public NumberFilteringComponent(NameType nameType) {
		super(nameType);
		FormLayout formLayout = new FormLayout();
		formLayout.setWidth("200px");
		formLayout.addComponent(new Button("Close"));
		this.comp = formLayout;
	}

	@Override
	public @NonNull AbstractComponent getComponent(PopupButton b) {
		return comp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PivotCriteria<Item, T> getCriteria() {
		return item -> {
			return (T) item.getItemProperty(propertyId).getValue();
		};
	}

	@Override
	public void addValueChangeListener(ValueChangeListener l) {
	}

}
