package com.snap252.vaadin.pivot.valuegetter;

import java.util.stream.Collector;

import com.vaadin.data.Item;
import com.vaadin.ui.renderers.Renderer;

public interface ModelAggregtor<VALUE> {
	public Class<?> getModelType();

	public Collector<Item, ?, ? extends VALUE> getAggregator();

	public Renderer<?> createRenderer();

}