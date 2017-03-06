package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.ui.Label;

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
	protected @NonNull BigDecimal roundImpl(@NonNull final BigDecimal input) {
		return input;
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return () -> new Label("xyz");
	}
}
