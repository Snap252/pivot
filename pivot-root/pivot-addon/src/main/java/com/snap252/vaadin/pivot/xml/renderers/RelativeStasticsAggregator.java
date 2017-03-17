package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;
import java.math.MathContext;
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
import com.snap252.vaadin.pivot.i18n.LookupComboBox;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.snap252.vaadin.pivot.valuegetter.WhatOfNumberStatisticsToRender;
import com.vaadin.ui.AbstractSelect;
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
	public WhatOfNumberStatisticsToRender whatToRender = WhatOfNumberStatisticsToRender.SUM;

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

	static class NumberStatisticsConfig extends FormLayoutField<RelativeStasticsAggregator> {
		private final AbstractSelect whatOfNumberStatisticsToRenderCheckBox = new LookupComboBox("Typ",
				WhatOfNumberStatisticsToRender.values());
		private final AbstractSelect ofWhatParentCheckBox = new LookupComboBox("Relativ zu", OfWhatParent.values());
		private final TextField formatTextField = new TextField("Formatierung");
		private final TextField nullFormatTextField = new TextField("Leere Werte");

		NumberStatisticsConfig() {
			super(new RelativeStasticsAggregator());
			nullFormatTextField.setInputPrompt("-");
			nullFormatTextField.setNullRepresentation("");

			formatTextField.setNullRepresentation("");
			formatTextField.setInputPrompt("0.00");

			whatOfNumberStatisticsToRenderCheckBox.setValue(value.whatToRender);
			ofWhatParentCheckBox.setValue(value.ofParent);

			whatOfNumberStatisticsToRenderCheckBox.addValueChangeListener(vce -> {
				final WhatOfNumberStatisticsToRender what = (WhatOfNumberStatisticsToRender) vce.getProperty()
						.getValue();
				assert what != null;
				if (value.whatToRender == what) {
					return;
				}
				value.whatToRender = what;
				fireValueChange();
			});
			whatOfNumberStatisticsToRenderCheckBox.setNullSelectionAllowed(false);

			ofWhatParentCheckBox.addValueChangeListener(vce -> {
				final OfWhatParent what = (OfWhatParent) vce.getProperty().getValue();
				if (value.ofParent == what) {
					return;
				}
				value.ofParent = what;
				fireValueChange();
			});
			ofWhatParentCheckBox.setNullSelectionAllowed(true);

			nullFormatTextField.addValueChangeListener(vce -> {
				String nullRepresentation = (String) vce.getProperty().getValue();
				assert nullRepresentation != null;
				if (nullRepresentation.isEmpty()) {
					nullRepresentation = null;
				}

				if (Objects.equals(nullRepresentation, value.nullRepresentation)) {
					return;
				}

				value.nullRepresentation = nullRepresentation;
				fireValueChange();
			});

			formatTextField.addValueChangeListener(vce -> {
				final String formatString = (String) vce.getProperty().getValue();
				if (Objects.equals(formatString, value.format)) {
					return;
				}
				value.format = formatString;
				fireValueChange();
			});
			addComponents(whatOfNumberStatisticsToRenderCheckBox, ofWhatParentCheckBox, formatTextField,
					nullFormatTextField);
		}

		@Override
		protected void doAfterValueSetWithoutEvents(final RelativeStasticsAggregator value) {
			whatOfNumberStatisticsToRenderCheckBox.setValue(value.whatToRender);
			ofWhatParentCheckBox.setValue(value.ofParent);
			formatTextField.setValue(value.format);
			nullFormatTextField.setValue(value.nullRepresentation);
		}

	}
}
