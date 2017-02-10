package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.snap252.vaadin.pivot.valuegetter.WhatOfNumberStatisticsToRender;
import com.vaadin.ui.renderers.Renderer;

public class NumberStatisticsAggregator
		extends Aggregator<@Nullable NumberStatistics<BigDecimal>, @Nullable BigDecimal> {
	@XmlAttribute(name = "rounding")
	public Integer preRounding = 0;

	@XmlAttribute(name = "ignore-null")
	public Boolean ignoreNull = false;

	@XmlAttribute(name = "mode")
	public WhatOfNumberStatisticsToRender whatToRender = WhatOfNumberStatisticsToRender.sum;

	@Override
	public @Nullable BigDecimal getConvertedValue(@Nullable final NumberStatistics<@NonNull BigDecimal> value) {
		return value == null ? null : whatToRender.getValue(value);
	}

	@Override
	public @NonNull Renderer<? super @Nullable BigDecimal> createRenderer() {
		return new BigDecimalRenderer(nullRepresentation);
	}
}
