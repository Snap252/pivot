package com.snap252.vaadin.pivot.valuegetter;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.data.Property.ValueChangeListener;

public interface FilteringRenderingComponent<INPUT_TYPE, @Nullable DATA_TYPE>
		extends UIConfigurable, ModelAggregtor<INPUT_TYPE, DATA_TYPE> {
	abstract void addRendererChangeListener(ValueChangeListener l);
}
