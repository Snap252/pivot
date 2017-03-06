package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Locale;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.PivotCellReference;
import com.snap252.vaadin.pivot.renderer.PivotRenderer;
import com.snap252.vaadin.pivot.utils.ClassUtils;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.Renderer;

@NonNullByDefault({})
public abstract class Aggregator<T, U> {

	public abstract @NonNull U getConvertedValue(@NonNull T value);

	public abstract @NonNull Renderer<? super U> createRenderer();

	public abstract @NonNull <INPUT_TYPE> Collector<INPUT_TYPE, ?, T> getCollector();

	@NonNullByDefault
	private Converter<? extends U, PivotCellReference<T, ?>> createConverter(final Class<U> presentationType) {
		return new Converter<U, PivotCellReference<T, ?>>() {

			@Override
			public Class<PivotCellReference<T, ?>> getModelType() {
				return ClassUtils.cast(PivotCellReference.class);
			}

			@Override
			public Class<U> getPresentationType() {
				return presentationType;
			}

			@Override
			public PivotCellReference<T, ?> convertToModel(@Nullable final U value,
					final @Nullable Class<? extends PivotCellReference<T, ?>> targetType, final @Nullable Locale locale)
					throws ConversionException {
				throw new Converter.ConversionException();
			}

			@SuppressWarnings("null")
			@Override
			public U convertToPresentation(final PivotCellReference<T, ?> value, final Class<? extends U> targetType,
					final @Nullable Locale locale) throws ConversionException {
				final T value2 = value.getValue();
				return value2 != null ? getConvertedValue(value2) : null;
			}
		};
	}

	public final void updateRendererAndConverter(final Column column, final int depth) {
		@SuppressWarnings("unchecked")
		final Renderer<U> renderer = (Renderer<U>) createRenderer();

		final Class<U> pt = renderer.getPresentationType();
		if (renderer instanceof PivotRenderer) {
			((PivotRenderer<?>) renderer).setDepth(depth);
		}
		column.setRenderer(renderer, createConverter(pt));
	}
}
