package com.snap252.vaadin.pivot.valuegetter;

import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;

import com.vaadin.data.Item;
import com.vaadin.ui.renderers.TextRenderer;

public final class ModelAggregtorDelegate implements ModelAggregtor<Object> {
	private ModelAggregtor<?> delegate;

	public boolean setDelegate(final ModelAggregtor<?> delegate) {
		if (this.delegate == delegate)
			return false;

		this.delegate = delegate == null ? new DummyAggregator() : delegate;
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
		public Collector<@NonNull Item, ?, String> getAggregator() {
			return Collector.of(() -> {
				return "";
			}, (x, y) -> {
			}, (x, y) -> "");
		}

		@SuppressWarnings("null")
		@Override
		public RendererConverter<String, String> createRendererConverter() {
			return new RendererConverter<String, String>(new TextRenderer(), x -> x.getValue(), String.class);
		}
	}

	public ModelAggregtorDelegate(final ModelAggregtor<?> delegate) {
		this.delegate = delegate;

	}

	@Override
	public Collector<Item, ?, ?> getAggregator() {
		return delegate.getAggregator();
	}

	@Override
	public RendererConverter<?, ?> createRendererConverter() {
		return delegate.createRendererConverter();
	}

}
