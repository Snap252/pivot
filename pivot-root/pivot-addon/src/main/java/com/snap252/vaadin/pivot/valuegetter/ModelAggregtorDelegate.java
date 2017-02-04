package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.PivotCellReference;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.vaadin.data.Item;

public final class ModelAggregtorDelegate implements ModelAggregtor<Object> {
	private ModelAggregtor<?> delegate;

	public boolean setDelegate(final ModelAggregtor<?> delegate) {
		if (delegate != null && this.delegate == delegate)
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

	//TODO: extract
	public static class DummyAggregator implements ModelAggregtor<Object> {

		@Override
		public Collector<@NonNull Item, ?, BigDecimal> getAggregator() {
			return Collectors.collectingAndThen(Collectors.counting(), BigDecimal::valueOf);
		}

		@SuppressWarnings("null")
		@Override
		public RendererConverter<?, ? extends @NonNull BigDecimal> createRendererConverter() {
			final BigDecimalRenderer renderer = new BigDecimalRenderer("-");
			renderer.setFormat("0");

			return new RendererConverter<BigDecimal, BigDecimal>(renderer, PivotCellReference::getValue,
					BigDecimal.class);
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
