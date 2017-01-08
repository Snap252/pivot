package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;

@NonNullByDefault
public class EnumFilteringComponent<T extends Enum<T>> extends AbstractFilteringComponent<T> {
	public EnumFilteringComponent(NameType nameType) {
		super(nameType);
	}

	@Override
	public @Nullable AbstractComponent getComponent(PopupButton b) {
		return null;
	}

	@SuppressWarnings({ "unchecked" })
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
