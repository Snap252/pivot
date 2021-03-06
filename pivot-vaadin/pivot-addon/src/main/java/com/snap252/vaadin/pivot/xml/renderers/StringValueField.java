package com.snap252.vaadin.pivot.xml.renderers;

import static java.util.Objects.requireNonNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.renderers.StringConcatAggregator.ConcatAggConfig;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;

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
				agg = (Aggregator<?, ?>) requireNonNull(vce.getProperty().getValue());
				fireChange();
			});

			final ConcatAggConfig concatAggConfig = new ConcatAggConfig();
			concatAggConfig.addValueChangeListener(vce -> {
				agg = (Aggregator<?, ?>) requireNonNull(vce.getProperty().getValue());
				fireChange();
			});

			final TabSheet allTabSheet = new TabSheet(
					getWrapperForTab("common", false, createForDisplayName(StringValueField.this)),
					getWrapperForTab("counting", false, countingAggConfig), getWrapperForTab("concatenating", false, concatAggConfig));
			if (agg instanceof CountingAggregator) {
				countingAggConfig.setValue((CountingAggregator) agg);
				allTabSheet.setSelectedTab(1);
			} else if (agg instanceof StringConcatAggregator) {
				concatAggConfig.setValue((StringConcatAggregator) agg);
				allTabSheet.setSelectedTab(2);
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
