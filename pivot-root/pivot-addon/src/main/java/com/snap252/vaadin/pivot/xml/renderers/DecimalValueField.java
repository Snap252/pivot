package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.renderers.RelativeStasticsAggregator.NumberStatisticsConfig;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;

public class DecimalValueField extends ValueField<Number> {
	public DecimalValueField() {
		super(new RelativeStasticsAggregator());
	}

	@XmlElements({ @XmlElement(name = "counting", type = CountingAggregator.class),
			@XmlElement(name = "statistics", type = RelativeStasticsAggregator.class),
	})
	public void setAggregator(final Aggregator<?, ?> agg) {
		this.agg = agg;
	}

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
	}


	@Override
	public UIConfigurable createUIConfigurable() {
		return new DecimalConfigurable();
	}

	private class DecimalConfigurable implements UIConfigurable {

		private final TabSheet comp;

		public DecimalConfigurable() {
			final CountingAggregator.CountingAggConfig countingAggConfig = new CountingAggregator.CountingAggConfig();
			countingAggConfig.addValueChangeListener(vce -> {
				agg = Objects.requireNonNull((CountingAggregator) vce.getProperty().getValue());
				fireChange();
			});
			final NumberStatisticsConfig statisticsAggConfig = new NumberStatisticsConfig();
			statisticsAggConfig.addValueChangeListener(vce -> {
				agg = Objects.requireNonNull((RelativeStasticsAggregator) vce.getProperty().getValue());
				fireChange();
			});

			final TabSheet allTabSheet = new TabSheet(
					getWrapperForTab("common", false, createForDisplayName(DecimalValueField.this)),
					getWrapperForTab("statistics", false, statisticsAggConfig), getWrapperForTab("counting", false, countingAggConfig));
			if (agg instanceof CountingAggregator) {
				countingAggConfig.setValue((CountingAggregator) agg);
				allTabSheet.setSelectedTab(2);
			} else if (agg instanceof RelativeStasticsAggregator) {
				statisticsAggConfig.setValue((RelativeStasticsAggregator) agg);
				allTabSheet.setSelectedTab(1);
			}
			allTabSheet.setWidth("500px");
			allTabSheet.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
			this.comp = allTabSheet;
		}

		@Override
		public @Nullable AbstractComponent getComponent() {
			return comp;
		}
	}
}
