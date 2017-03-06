package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collector;

import javax.xml.bind.annotation.XmlAttribute;

import com.snap252.vaadin.pivot.valuegetter.ObjectStatistics;
import com.snap252.vaadin.pivot.valuegetter.WhatOfObjectStatisticsToShow;
import com.vaadin.ui.renderers.NumberRenderer;

public class CountingAggregator extends Aggregator<ObjectStatistics, Integer> {

	@XmlAttribute(name = "mode")
	public WhatOfObjectStatisticsToShow whatToRender = WhatOfObjectStatisticsToShow.cntNonNull;

	@Override
	public NumberRenderer createRenderer() {
		return new NumberRenderer("%s", Locale.getDefault(), nullRepresentation);
	}

	@Override
	public Collector<?, ?, ObjectStatistics> getCollector() {
		return Collector.of(() -> new ObjectStatistics(), ObjectStatistics::add, ObjectStatistics::mergeTo,
				Function.identity());
	}

	@Override
	public Integer getConvertedValue(final ObjectStatistics t) {
		return whatToRender.getValue(t);
	}

}
