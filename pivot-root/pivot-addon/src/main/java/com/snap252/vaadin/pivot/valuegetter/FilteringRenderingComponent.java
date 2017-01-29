package com.snap252.vaadin.pivot.valuegetter;

import com.snap252.vaadin.pivot.FilteringComponent;
import com.vaadin.data.Property.ValueChangeListener;

public interface FilteringRenderingComponent<T extends Comparable<T>, VALUE>
		extends FilteringComponent<T>, ModelAggregtor<VALUE> {
	abstract void addRendererChangeListener(ValueChangeListener l);
}
