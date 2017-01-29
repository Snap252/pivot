package com.snap252.vaadin.pivot.valuegetter;

import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;

import com.vaadin.data.Item;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.renderers.TextRenderer;

public final class ModelAggregtorDelegate implements ModelAggregtor<Object> {
	private ModelAggregtor<?> delegate;

	public boolean setDelegate(final ModelAggregtor<?> delegate) {
		if (this.delegate == delegate)
			return false;

		this.delegate = delegate;
		return true;
	}
	
	public ModelAggregtor<?> getDelegate() {
		return delegate;
	}

	public ModelAggregtorDelegate() {
		this(new DummyAggregator());
	}

	private static class DummyAggregator implements ModelAggregtor<String> {

		@Override
		public Class<Object> getModelType() {
			return Object.class;
		}

		@Override
		public Collector<@NonNull Item, ?, String> getAggregator() {
			return Collector.of(() -> {
				return "";
			}, (x, y) -> {
			}, (x, y) -> "");
		}

		@SuppressWarnings("null")
		@Override
		public Renderer<String> createRenderer() {
			return new TextRenderer();
		}
	}

	public ModelAggregtorDelegate(final ModelAggregtor<?> delegate) {
		this.delegate = delegate;

	}

	@Override
	public Class<?> getModelType() {
		return delegate.getModelType();
	}

	@Override
	public Collector<Item, ?, ?> getAggregator() {
		return delegate.getAggregator();
	}

	@Override
	public Renderer<?> createRenderer() {
		return delegate.createRenderer();
	}

}
