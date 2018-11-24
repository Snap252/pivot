package com.snap252.vaadin.pivot.renderer;

import com.vaadin.ui.renderers.Renderer;

public interface PivotRenderer<T> extends Renderer<T> {
	void setDepth(int depth);
}
