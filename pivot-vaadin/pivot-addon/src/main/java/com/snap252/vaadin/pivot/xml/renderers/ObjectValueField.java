package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.renderers.CountingAggregator.CountingAggConfig;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;

public final class ObjectValueField extends ValueField<Object> {
	public ObjectValueField() {
		super(new CountingAggregator());
	}

	@XmlElements(@XmlElement(name = "counting", type = CountingAggregator.class))
	public void setAggregator(final Aggregator<?, ?> agg) {
		this.agg = agg;
	}

	@Override
	public Aggregator<?, ?> getAggregator() {
		return super.getAggregator();
	}


	@Override
	public UIConfigurable createUIConfigurable() {
		return new ObjectConfigurable();
	}

	private class ObjectConfigurable implements UIConfigurable {

		private final TabSheet comp;

		public ObjectConfigurable() {
			final CountingAggConfig countingAggConfig = new CountingAggConfig();
			countingAggConfig.addValueChangeListener(vce -> {
				agg = Objects.requireNonNull((CountingAggregator) vce.getProperty().getValue());
				fireChange();
			});

			final TabSheet allTabSheet = new TabSheet(
					getWrapperForTab("common", false, createForDisplayName(ObjectValueField.this)),
					getWrapperForTab("counting", false, countingAggConfig));
			if (agg instanceof CountingAggregator) {
				countingAggConfig.setValue((CountingAggregator) agg);
				allTabSheet.setSelectedTab(1);
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
