package com.snap252.vaadin.pivot.xml.renderers;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collector;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.snap252.vaadin.pivot.valuegetter.ObjectStatistics;
import com.snap252.vaadin.pivot.valuegetter.WhatOfObjectStatisticsToShow;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;

public final class CountingAggregator extends Aggregator<ObjectStatistics, BigDecimal> {

	@XmlAttribute(name = "mode")
	public WhatOfObjectStatisticsToShow whatToRender = WhatOfObjectStatisticsToShow.cntNonNull;

	@Override
	public BigDecimalRenderer createRenderer() {
		final BigDecimalRenderer bigDecimalRenderer = new BigDecimalRenderer("---");
		bigDecimalRenderer.setFormat("0");
		return bigDecimalRenderer;
	}

	@Override
	public Collector<?, ?, ObjectStatistics> getCollector() {
		return Collector.of(() -> new ObjectStatistics(), ObjectStatistics::add, ObjectStatistics::mergeTo,
				Function.identity());
	}

	@Override
	public BigDecimal getConvertedValue(final ObjectStatistics t) {
		return BigDecimal.valueOf(whatToRender.getValue(t));
	}

	static class CountingAggConfig extends VerticalLayout implements DefaultField<CountingAggregator> {
		private CountingAggregator agg = new CountingAggregator();

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

		final ComboBox cb = new ComboBox("Darstellung", Arrays.asList(WhatOfObjectStatisticsToShow.values()));

		CountingAggConfig() {
			cb.setValue(agg.whatToRender);
			cb.addValueChangeListener(vce -> {
				final WhatOfObjectStatisticsToShow what = (WhatOfObjectStatisticsToShow) vce.getProperty().getValue();
				assert what != null;
				if (what == agg.whatToRender) {
					return;
				}
				agg.whatToRender = what;
				if (fireEvents)
					fireValueChange();
			});
			cb.setNullSelectionAllowed(false);
			addComponent(cb);
		}

		@Override
		public @NonNull CountingAggregator getValue() {
			return agg;
		}

		@Override
		public void setValue(@NonNull final CountingAggregator newValue)
				throws com.vaadin.data.Property.ReadOnlyException {
			fireEvents = false;
			try {
				agg = newValue;
				cb.setValue(agg.whatToRender);
			} finally {
				fireEvents = true;
			}

		}

		@Override
		public Class<? extends CountingAggregator> getType() {
			return CountingAggregator.class;
		}
	}

}
