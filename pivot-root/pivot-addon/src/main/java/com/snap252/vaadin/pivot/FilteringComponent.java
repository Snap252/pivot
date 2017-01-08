package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;

@NonNullByDefault
public interface FilteringComponent<T extends Comparable<T>> {
	@Nullable
	abstract AbstractComponent getComponent(PopupButton b);

	abstract PivotCriteria<Item, T> getCriteria();

	abstract void addValueChangeListener(ValueChangeListener l);
}
