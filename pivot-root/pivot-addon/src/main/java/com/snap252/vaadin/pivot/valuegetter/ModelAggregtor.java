package com.snap252.vaadin.pivot.valuegetter;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.PivotCellReference;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.Renderer;

public interface ModelAggregtor<VALUE> {
	public Collector<Item, ?, ? extends VALUE> getAggregator();

	public RendererConverter<?, ? extends VALUE> createRendererConverter();
	

	public static class RendererConverter<T, VALUE> {
		private final Renderer<T> renderer;
		private final Converter<T, PivotCellReference<@Nullable VALUE>> converter;

		public RendererConverter(final Renderer<T> renderer,
				final Function<PivotCellReference<@Nullable VALUE>, @Nullable T> f, final Class<?> presentationType) {
			this.renderer = renderer;

			converter = new Converter<T, PivotCellReference<@Nullable VALUE>>() {

				@SuppressWarnings("null")
				@Override
				public PivotCellReference<VALUE> convertToModel(final T value,
						final Class<? extends PivotCellReference<@Nullable VALUE>> targetType, final Locale locale)
						throws com.vaadin.data.util.converter.Converter.ConversionException {
					throw new AssertionError();
				}

				@Override
				public T convertToPresentation(final PivotCellReference<@Nullable VALUE> value,
						final Class<? extends T> targetType, final Locale locale) {
					return value != null ? f.apply(value) : null;
				}

				@Override
				public Class<PivotCellReference<VALUE>> getModelType() {
					return PivotCellReference.cast(PivotCellReference.class);
				}

				@SuppressWarnings("unchecked")
				@Override
				public Class<T> getPresentationType() {
					return (Class<T>) presentationType;
				}
			};
		}

		public RendererConverter(final Renderer<T> renderer,
				final Converter<T, PivotCellReference<@Nullable VALUE>> converter) {
			this.renderer = renderer;
			this.converter = converter;
		}

		Renderer<T> getRenderer() {
			return renderer;
		}

		public void setToColumn(final Column col) {
			col.setRenderer(renderer, converter);
		}
	}

}