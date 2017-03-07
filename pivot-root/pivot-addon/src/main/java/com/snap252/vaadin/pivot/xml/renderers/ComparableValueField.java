package com.snap252.vaadin.pivot.xml.renderers;

import static java.util.Objects.requireNonNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.renderers.ComparableAggregator.ComparableAggConfig;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;

public class ComparableValueField extends ValueField<Comparable<?>> {
	public ComparableValueField() {
		super(new ComparableAggregator<>());
	}

	@XmlElements({ @XmlElement(name = "counting", type = CountingAggregator.class),
			@XmlElement(name = "comparable", type = ComparableAggregator.class) })
	public void setAggregator(final Aggregator<?, ?> agg) {
		this.agg = agg;
	}

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
	}


	@Override
	public UIConfigurable createUIConfigurable() {
		return new ComparableConfigurable();
	}

	private class ComparableConfigurable implements UIConfigurable {

		private final TabSheet comp;

		public ComparableConfigurable() {
			final CountingAggregator.CountingAggConfig countingAggConfig = new CountingAggregator.CountingAggConfig();
			countingAggConfig.addValueChangeListener(vce -> {
				agg = requireNonNull((Aggregator<?, ?>) vce.getProperty().getValue());
				fireChange();
			});

			final ComparableAggConfig comparableAggConfig = new ComparableAggConfig();
			comparableAggConfig.addValueChangeListener(vce -> {
				agg = requireNonNull((Aggregator<?, ?>) vce.getProperty().getValue());
				fireChange();
			});

			final TabSheet allTabSheet = new TabSheet(
					getWrapper("Allgemein", createForDisplayName(ComparableValueField.this)),
					getWrapper("Zählung", countingAggConfig), getWrapper("Vergleich", comparableAggConfig));
			if (agg instanceof CountingAggregator) {
				countingAggConfig.setValue((CountingAggregator) agg);
				allTabSheet.setSelectedTab(1);
			} else if (agg instanceof ComparableAggregator) {
				comparableAggConfig.setValue((ComparableAggregator<?>) agg);
				allTabSheet.setSelectedTab(2);
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
