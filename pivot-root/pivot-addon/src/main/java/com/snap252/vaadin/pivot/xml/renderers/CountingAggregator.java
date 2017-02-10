package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Locale;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.valuegetter.ObjectStatistics;
import com.snap252.vaadin.pivot.valuegetter.WhatOfObjectStatisticsToShow;
import com.vaadin.ui.renderers.NumberRenderer;

public class CountingAggregator extends Aggregator<@Nullable ObjectStatistics, @Nullable Integer> {

	@XmlAttribute(name = "mode")
	public WhatOfObjectStatisticsToShow whatToRender = WhatOfObjectStatisticsToShow.cnt;

	@Override
	public @Nullable Integer getConvertedValue(@Nullable final ObjectStatistics value) {
		return value == null ? null : whatToRender.getValue(value);
	}

	@Override
	public NumberRenderer createRenderer() {
		return new NumberRenderer("%s", Locale.getDefault(), nullRepresentation);
	}

}
