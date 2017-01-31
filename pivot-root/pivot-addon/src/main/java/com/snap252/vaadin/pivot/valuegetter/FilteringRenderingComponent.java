package com.snap252.vaadin.pivot.valuegetter;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.data.Property.ValueChangeListener;

public interface FilteringRenderingComponent<VALUE> extends UIConfigurable, ModelAggregtor<VALUE> {
	abstract void addRendererChangeListener(ValueChangeListener l);
}
