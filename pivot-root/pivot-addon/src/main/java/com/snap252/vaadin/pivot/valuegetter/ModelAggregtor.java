package com.snap252.vaadin.pivot.valuegetter;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.PivotCellReference;
import com.snap252.vaadin.pivot.renderer.PivotRenderer;
import com.snap252.vaadin.pivot.utils.ClassUtils;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Grid.Column;

public interface ModelAggregtor<INPUT_TYPE, @Nullable DATA_TYPE> {
	public Collector<INPUT_TYPE, ?, ? extends DATA_TYPE> getAggregator();

	public RendererConverter<?, ? extends DATA_TYPE> createRendererConverter();

	public static class RendererConverter<T, VALUE> {
		private final PivotRenderer<T> renderer;
		private final Converter<T, PivotCellReference<@Nullable VALUE, Item>> converter;

		public RendererConverter(final PivotRenderer<T> renderer,
				final Function<PivotCellReference<@Nullable VALUE, Item>, @Nullable T> f,
				final Class<?> presentationType) {
			this.renderer = renderer;

			converter = new Converter<T, PivotCellReference<@Nullable VALUE, Item>>() {

				@SuppressWarnings("null")
				@Override
				public PivotCellReference<VALUE, Item> convertToModel(final T value,
						final Class<? extends PivotCellReference<@Nullable VALUE, Item>> targetType,
						final Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
					throw new AssertionError();
				}

				@Override
				public T convertToPresentation(final PivotCellReference<@Nullable VALUE, Item> value,
						final Class<? extends T> targetType, final Locale locale) {
					return value != null ? f.apply(value) : null;
				}

				@Override
				public Class<PivotCellReference<VALUE, Item>> getModelType() {
					return ClassUtils.cast(PivotCellReference.class);
				}

				@SuppressWarnings("unchecked")
				@Override
				public Class<T> getPresentationType() {
					return (Class<T>) presentationType;
				}
			};
		}

		public RendererConverter(final PivotRenderer<T> renderer,
				final Converter<T, PivotCellReference<@Nullable VALUE, Item>> converter) {
			this.renderer = renderer;
			this.converter = converter;
		}

		public void setToColumn(final Column col, final int depth) {
			renderer.setDepth(depth);
			col.setRenderer(renderer, converter);
		}
	}

}