package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;

@NonNullByDefault
public class NumberFilteringComponent<T extends Number & Comparable<T>> extends AbstractFilteringComponent<T> {
	private FormLayout comp;

	public NumberFilteringComponent(NameType nameType) {
		super(nameType);
		FormLayout formLayout = new FormLayout();
		formLayout.setWidth("200px");
		this.comp = formLayout;
	}

	@Override
	public @NonNull AbstractComponent getComponent() {
		return comp;
	}

	@Override
	public void addValueChangeListener(ValueChangeListener l) {
	}

}
