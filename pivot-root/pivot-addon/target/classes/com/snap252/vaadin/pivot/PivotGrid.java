package com.snap252.vaadin.pivot;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.treegrid.TreeGrid;

import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.pivoting.BiBucketParameter;
import com.vaadin.data.util.converter.Converter;

// This is the server-side UI component that provides public API 
// for PivotGrid
@NonNullByDefault
public class PivotGrid extends TreeGrid {

	public PivotGrid() {
		// addColumn("c1");
		// addColumn("c2");
		// HeaderRow headerRow = addHeaderRowAt(0);
		// headerRow.join("c1", "c2");
		// headerRow.getCell("c1").setText("t1");
	}

	public <@Nullable T> void setContainerDataSource(BiBucketParameter<T> bucketParams,
			Collector<T, MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> c) {

		@SuppressWarnings("rawtypes")
		Converter<@Nullable Number, @Nullable NumberStatistics> converter = new Converter<@Nullable Number, @Nullable NumberStatistics>() {

			@SuppressWarnings("rawtypes")
			@Override
			public @Nullable NumberStatistics convertToModel(@Nullable Number value,
					@Nullable Class<? extends @Nullable NumberStatistics> targetType, @Nullable Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				throw new com.vaadin.data.util.converter.Converter.ConversionException();
			}

			@Override
			public @Nullable Number convertToPresentation(@SuppressWarnings("rawtypes") @Nullable NumberStatistics value,
					@Nullable Class<? extends @Nullable Number> targetType, @Nullable Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return value != null ? value.sum : null;
			}

			@SuppressWarnings({ "null", "rawtypes" })
			@Override
			public Class<@Nullable NumberStatistics> getModelType() {
				return NumberStatistics.class;
			}

			@SuppressWarnings("null")
			@Override
			public Class<@Nullable Number> getPresentationType() {
				return Number.class;
			}
		};

		new BiBucketExtension<>(bucketParams).createGridWriter(c).writeGrid(this, converter);
	}
}
