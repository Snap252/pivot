package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Collector;

import javax.xml.bind.annotation.XmlAttribute;

import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.snap252.vaadin.pivot.valuegetter.ObjectStatistics;
import com.snap252.vaadin.pivot.valuegetter.WhatOfObjectStatisticsToShow;

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

}
