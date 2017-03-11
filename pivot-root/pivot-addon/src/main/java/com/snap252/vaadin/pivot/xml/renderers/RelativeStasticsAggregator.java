package com.snap252.vaadin.pivot.xml.renderers;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.Arithmetics;
import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.NullableArithmeticsWrapper;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.vaadin.pivot.PivotCellReference;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.snap252.vaadin.pivot.valuegetter.WhatOfNumberStatisticsToRender;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class RelativeStasticsAggregator
		extends Aggregator<@Nullable NumberStatistics<BigDecimal>, @Nullable BigDecimal> {

	public enum OfWhatParent {
		ROW(PivotCellReference::ofParentRow),

		COL(PivotCellReference::ofParentCol),

		ROW_TOTAL(PivotCellReference::ofRow),

		COL_TOTAL(PivotCellReference::ofCol),

		TOTAL(PivotCellReference::ofTotal)

		;
		private final Function<PivotCellReference<?, ?>, PivotCellReference<?, ?>> f;

		@SuppressWarnings("unchecked")
		public <T> T casted(final PivotCellReference<T, ?> r) {
			return (T) f.apply(r).getValue();
		}

		OfWhatParent(final Function<PivotCellReference<?, ?>, PivotCellReference<?, ?>> f) {
			this.f = f;

		}
	}

	@XmlAttribute
	@Nullable
	public OfWhatParent ofParent;

	public RelativeStasticsAggregator() {
	}

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
	@Nullable
	public String nullRepresentation = "-";

	@XmlAttribute(name = "format")
	@Nullable
	public String format;

	@Override
	public BigDecimalRenderer createRenderer() {
		final BigDecimalRenderer ret = new BigDecimalRenderer("");
		if (format != null)
			ret.setFormat(format);
		else if (ofParent != null)
			ret.setFormat("0.00%");
		else
			ret.setFormat("0.00");

		return ret;
	}

	@Override
	public Collector<?, ?, @Nullable NumberStatistics<@Nullable BigDecimal>> getCollector() {
		final Arithmetics<@Nullable BigDecimal> arithmetics = new NullableArithmeticsWrapper<>(
				new BigDecimalArithmetics());
		return PivotCollectors.getNumberReducer(arithmetics);
	}

	private final MathContext mathContextForDividing = new MathContext(20);

	@Override
	protected @Nullable BigDecimal getValueFromPivotCell(
			final PivotCellReference<@Nullable NumberStatistics<BigDecimal>, ?> value) {

		final NumberStatistics<BigDecimal> ownValue = value.getValue();
		if (ownValue == null) {
			return null;
		}

		final OfWhatParent ofParent$ = ofParent;
		final BigDecimal getConvertedOwnValue = getConvertedValue(ownValue);
		if (ofParent$ == null) {
			return getConvertedOwnValue;
		}

		final NumberStatistics<BigDecimal> parent = ofParent$.casted(value);
		assert parent != null;
		return getConvertedOwnValue.divide(getConvertedValue(parent), mathContextForDividing);
	}

	static class NumberStatisticsConfig extends FormLayout implements DefaultField<RelativeStasticsAggregator> {
		private RelativeStasticsAggregator agg = new RelativeStasticsAggregator();

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

		private final ComboBox whatOfNumberStatisticsToRenderCheckBox = new ComboBox("Typ",
				Arrays.asList(WhatOfNumberStatisticsToRender.values()));
		private final ComboBox ofWhatParentCheckBox = new ComboBox("Relativ zu", Arrays.asList(OfWhatParent.values()));
		private final TextField formatTextField = new TextField("Formatierung");
		private final TextField nullFormatTextField = new TextField("Leere Werte");

		NumberStatisticsConfig() {
			nullFormatTextField.setInputPrompt("-");
			nullFormatTextField.setNullRepresentation("");

			formatTextField.setNullRepresentation("");
			formatTextField.setInputPrompt("0.00");

			whatOfNumberStatisticsToRenderCheckBox.setValue(agg.whatToRender);
			ofWhatParentCheckBox.setValue(agg.ofParent);

			whatOfNumberStatisticsToRenderCheckBox.addValueChangeListener(vce -> {
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
			whatOfNumberStatisticsToRenderCheckBox.setNullSelectionAllowed(false);

			ofWhatParentCheckBox.addValueChangeListener(vce -> {
				final OfWhatParent what = (OfWhatParent) vce.getProperty().getValue();
				if (what == agg.ofParent) {
					return;
				}
				agg.ofParent = what;
				if (fireEvents)
					fireValueChange();
			});
			ofWhatParentCheckBox.setNullSelectionAllowed(true);

			nullFormatTextField.addValueChangeListener(vce -> {
				String nullRepresentation = (String) vce.getProperty().getValue();
				assert nullRepresentation != null;
				if (nullRepresentation.isEmpty()) {
					nullRepresentation = null;
				}

				if (Objects.equals(nullRepresentation, agg.nullRepresentation)) {
					return;
				}

				agg.nullRepresentation = nullRepresentation;
				if (fireEvents)
					fireValueChange();
			});

			formatTextField.addValueChangeListener(vce -> {
				final String formatString = (String) vce.getProperty().getValue();
				if (Objects.equals(formatString, agg.format)) {
					return;
				}
				agg.format = formatString;
				if (fireEvents)
					fireValueChange();
			});
			addComponents(whatOfNumberStatisticsToRenderCheckBox, ofWhatParentCheckBox, formatTextField,
					nullFormatTextField);
		}

		@Override
		public RelativeStasticsAggregator getValue() {
			return agg;
		}

		@Override
		public void setValue(final RelativeStasticsAggregator newValue)
				throws com.vaadin.data.Property.ReadOnlyException {
			fireEvents = false;
			try {
				agg = newValue;
				whatOfNumberStatisticsToRenderCheckBox.setValue(agg.whatToRender);
				ofWhatParentCheckBox.setValue(agg.ofParent);
				formatTextField.setValue(agg.format);
				nullFormatTextField.setValue(agg.nullRepresentation);
			} finally {
				fireEvents = true;
			}

		}

		@Override
		public Class<RelativeStasticsAggregator> getType() {
			return RelativeStasticsAggregator.class;
		}
	}
}
