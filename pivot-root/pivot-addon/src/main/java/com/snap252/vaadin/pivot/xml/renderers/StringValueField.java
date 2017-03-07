package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.renderers.StringConcatAggregator.ConcatAggConfig;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;

public class StringValueField extends ValueField<String> {
	public StringValueField() {
		super(new StringConcatAggregator());
	}

	@XmlElements({ @XmlElement(name = "counting", type = CountingAggregator.class),
			@XmlElement(name = "concat", type = StringConcatAggregator.class) })
	public void setAggregator(final Aggregator<?, ?> agg) {
		this.agg = agg;
	}

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
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

			final ConcatAggConfig concatAggConfig = new ConcatAggConfig();
			concatAggConfig.addValueChangeListener(vce -> {
				agg = (Aggregator<?, ?>) vce.getProperty().getValue();
				fireChange();
			});

			final TabSheet allTabSheet = new TabSheet(
					getWrapper("Allgemein", createForDisplayName(StringValueField.this)),
					getWrapper("Zählung", countingAggConfig), getWrapper("Verketten", concatAggConfig));
			if (agg instanceof CountingAggregator) {
				countingAggConfig.setValue((CountingAggregator) agg);
				allTabSheet.setSelectedTab(1);
			} else if (agg instanceof StringConcatAggregator) {
				concatAggConfig.setValue((StringConcatAggregator) agg);
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
