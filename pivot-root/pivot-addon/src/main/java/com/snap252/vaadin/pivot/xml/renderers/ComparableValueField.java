package com.snap252.vaadin.pivot.xml.renderers;

import static java.util.Objects.requireNonNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.renderers.ComparableAggregator.ComparableAggConfig;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;

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
					getWrapper("Allgemein", false, createForDisplayName(ComparableValueField.this)),
					getWrapper("Zählung", false, countingAggConfig), getWrapper("Vergleich", false, comparableAggConfig));
			if (agg instanceof CountingAggregator) {
				countingAggConfig.setValue((CountingAggregator) agg);
				allTabSheet.setSelectedTab(1);
			} else if (agg instanceof ComparableAggregator) {
				comparableAggConfig.setValue((ComparableAggregator<?>) agg);
				allTabSheet.setSelectedTab(2);
			}
			allTabSheet.setWidth("500px");
			allTabSheet.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
			this.comp = allTabSheet;
		}

		@Override
		public @NonNull AbstractComponent getComponent() {
			return comp;
		}
	}
}
