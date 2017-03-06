package com.snap252.vaadin.pivot.xml.renderers;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collector;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.Arithmetics;
import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.NullableArithmeticsWrapper;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.snap252.vaadin.pivot.valuegetter.WhatOfNumberStatisticsToRender;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.Renderer;

public class NumberStatisticsAggregator
		extends Aggregator<@Nullable NumberStatistics<BigDecimal>, @Nullable BigDecimal> {
	@XmlAttribute(name = "rounding")
	public Integer preRounding = 0;

	@XmlAttribute(name = "ignore-null")
	public Boolean ignoreNull = false;

	@XmlAttribute(name = "mode")
	public WhatOfNumberStatisticsToRender whatToRender = WhatOfNumberStatisticsToRender.sum;

	@Override
	public BigDecimal getConvertedValue(final NumberStatistics<BigDecimal> value) {
		return whatToRender.getValue(value);
	}

	@XmlAttribute(name = "null-representation")
	public String nullRepresentation = "-";

	@XmlAttribute(name = "format")
	public String format = "0.00#";

	@Override
	public Renderer<@Nullable BigDecimal> createRenderer() {
		final BigDecimalRenderer ret = new BigDecimalRenderer(nullRepresentation);
		ret.setFormat(format);
		return ret;
	}

	@Override
	public <INPUT_TYPE> @NonNull Collector<INPUT_TYPE, ?, @Nullable NumberStatistics<@NonNull BigDecimal>> getCollector() {
		final Arithmetics<BigDecimal> arithmetics = new NullableArithmeticsWrapper<>(new BigDecimalArithmetics());
		return (@NonNull Collector<INPUT_TYPE, ?, @Nullable NumberStatistics<@NonNull BigDecimal>>) PivotCollectors
				.getNumberReducer(arithmetics);
	}

	static class NumberStatisticsConfig extends VerticalLayout implements DefaultField<NumberStatisticsAggregator> {
		private NumberStatisticsAggregator agg = new NumberStatisticsAggregator();

		@Override
		public void focus() {
			super.focus();
		}

		private static final Method VALUE_CHANGE_METHOD;

		static {
			try {
				VALUE_CHANGE_METHOD = Property.ValueChangeListener.class.getDeclaredMethod("valueChange",
						new Class[] { Property.ValueChangeEvent.class });
			} catch (final java.lang.NoSuchMethodException e) {
				// This should never happen
				throw new java.lang.RuntimeException("Internal error finding methods in AbstractField");
			}
		}

		protected void fireValueChange() {
			fireEvent(new AbstractField.ValueChangeEvent(this));
		}

		@Override
		public void addValueChangeListener(final Property.ValueChangeListener listener) {
			addListener(AbstractField.ValueChangeEvent.class, listener, VALUE_CHANGE_METHOD);
			// ensure "automatic immediate handling" works
			markAsDirty();
		}

		private boolean fireEvents = true;

		private final ComboBox cb = new ComboBox("Typ", Arrays.asList(WhatOfNumberStatisticsToRender.values()));
		private final TextField formatTextField = new TextField("Formatierung");
		private final TextField nullFormatTextField = new TextField("Leere Werte");

		NumberStatisticsConfig() {
			nullFormatTextField.setInputPrompt("-");
			formatTextField.setInputPrompt("0.00");

			cb.setValue(agg.whatToRender);
			cb.addValueChangeListener(vce -> {
				final WhatOfNumberStatisticsToRender what = (WhatOfNumberStatisticsToRender) vce.getProperty()
						.getValue();
				assert what != null;
				if (what == agg.whatToRender) {
					return;
				}
				agg.whatToRender = what;
				if (fireEvents)
					fireValueChange();
			});
			cb.setNullSelectionAllowed(false);

			nullFormatTextField.addValueChangeListener(vce -> {
				final String nullRepresentation = (String) vce.getProperty().getValue();
				assert nullRepresentation != null;
				if (Objects.equals(nullRepresentation, agg.nullRepresentation)) {
					return;
				}
				agg.nullRepresentation = nullRepresentation;
				if (fireEvents)
					fireValueChange();
			});

			formatTextField.addValueChangeListener(vce -> {
				final String formatString = (String) vce.getProperty().getValue();
				assert formatString != null;
				if (Objects.equals(formatString, agg.format)) {
					return;
				}
				agg.format = formatString;
				if (fireEvents)
					fireValueChange();
			});
			addComponents(cb, formatTextField, nullFormatTextField);
		}

		@Override
		public NumberStatisticsAggregator getValue() {
			return agg;
		}

		@Override
		public void setValue(@NonNull final NumberStatisticsAggregator newValue)
				throws com.vaadin.data.Property.ReadOnlyException {
			fireEvents = false;
			try {
				agg = newValue;
				cb.setValue(agg.whatToRender);
				formatTextField.setValue(agg.format);
				nullFormatTextField.setValue(agg.nullRepresentation);
			} finally {
				fireEvents = true;
			}

		}

		@Override
		public Class<NumberStatisticsAggregator> getType() {
			return NumberStatisticsAggregator.class;
		}
	}
}
