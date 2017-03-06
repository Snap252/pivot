package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.renderers.NumberStatisticsAggregator.NumberStatisticsConfig;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;

public class DecimalValueField extends ValueField<BigDecimal> {
	public DecimalValueField() {
		super(new NumberStatisticsAggregator());
	}

	@XmlElements({ @XmlElement(name = "counting", type = CountingAggregator.class),
			@XmlElement(name = "statistics", type = NumberStatisticsAggregator.class) })
	public void setAggregator(final Aggregator<?, ?> agg) {
		this.agg = agg;
	}

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
	}

	@Override
	protected BigDecimal roundImpl(final BigDecimal input) {
		return input;
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return new StringConfigurable();
	}

	private class StringConfigurable implements UIConfigurable {

		private final TabSheet comp;

		public StringConfigurable() {
			final CountingAggregator.CountingAggConfig countingAggConfig = new CountingAggregator.CountingAggConfig();
			countingAggConfig.addValueChangeListener(vce -> {
				agg = (Aggregator<?, ?>) vce.getProperty().getValue();
				fireChange();
			});
			final NumberStatisticsConfig statisticsAggConfig = new NumberStatisticsConfig();
			statisticsAggConfig.addValueChangeListener(vce -> {
				agg = (Aggregator<?, ?>) vce.getProperty().getValue();
				fireChange();
			});

			final TabSheet allTabSheet = new TabSheet(
					getWrapper("Allgemein", createForDisplayName(DecimalValueField.this)),
					getWrapper("Statistiken", statisticsAggConfig), getWrapper("Zählung", countingAggConfig));
			if (agg instanceof CountingAggregator) {
				countingAggConfig.setValue((CountingAggregator) agg);
				allTabSheet.setSelectedTab(2);
			} else if (agg instanceof NumberStatisticsAggregator) {
				statisticsAggConfig.setValue((NumberStatisticsAggregator) agg);
				allTabSheet.setSelectedTab(1);
			}
			allTabSheet.setWidth("500px");
			this.comp = allTabSheet;
		}

		@Override
		public @Nullable AbstractComponent getComponent() {
			return comp;
		}
	}
}
