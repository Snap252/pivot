package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;

@NonNullByDefault
public interface FilteringComponent<T extends Comparable<T>> extends PivotCriteria<@NonNull Item, T> {
	@Nullable
	abstract AbstractComponent getComponent();

	abstract void addValueChangeListener(ValueChangeListener l);
}
