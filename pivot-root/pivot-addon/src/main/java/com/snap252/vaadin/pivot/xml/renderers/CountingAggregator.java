package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Collector;

import javax.xml.bind.annotation.XmlAttribute;

import com.snap252.vaadin.pivot.i18n.LookupComboBox;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.snap252.vaadin.pivot.valuegetter.ObjectStatistics;
import com.snap252.vaadin.pivot.valuegetter.WhatOfObjectStatisticsToShow;
import com.vaadin.ui.AbstractSelect;

public final class CountingAggregator extends Aggregator<ObjectStatistics, BigDecimal> {

	@XmlAttribute(name = "mode")
	public WhatOfObjectStatisticsToShow whatToRender = WhatOfObjectStatisticsToShow.COUNT;

	@Override
	public BigDecimalRenderer createRenderer() {
		final BigDecimalRenderer bigDecimalRenderer = new BigDecimalRenderer("-");
		bigDecimalRenderer.setFormat("0");
		return bigDecimalRenderer;
	}

	@Override
	public <INPUT_TYPE> Collector<INPUT_TYPE, ?, ObjectStatistics> getCollector() {
		return Collector.of(() -> new ObjectStatistics(), ObjectStatistics::add, ObjectStatistics::mergeTo,
				Function.identity());
	}

	@Override
	public BigDecimal getConvertedValue(final ObjectStatistics t) {
		return BigDecimal.valueOf(whatToRender.getValue(t));
	}

	static class CountingAggConfig extends FormLayoutField<CountingAggregator> {
		private final AbstractSelect displayCheckbox = new LookupComboBox("Darstellung",
				WhatOfObjectStatisticsToShow.values());

		CountingAggConfig() {
			super(new CountingAggregator());
			displayCheckbox.setValue(value.whatToRender);
			displayCheckbox.addValueChangeListener(vce -> {
				final WhatOfObjectStatisticsToShow what = (WhatOfObjectStatisticsToShow) vce.getProperty().getValue();
				assert what != null;
				if (what == value.whatToRender) {
					return;
				}
				value.whatToRender = what;
				fireValueChange();
			});
			displayCheckbox.setNullSelectionAllowed(false);
			addComponent(displayCheckbox);
		}

		@Override
		protected void doAfterValueSetWithoutEvents(final CountingAggregator value) {
			displayCheckbox.setValue(value.whatToRender);
		}

	}

}
