package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;
import java.util.stream.Collector;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.Arithmetics;
import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.NullableArithmeticsWrapper;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
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
	public BigDecimal getConvertedValue(final NumberStatistics<BigDecimal> value) {
		return whatToRender.getValue(value);
	}

	@XmlAttribute(name = "null-representation")
	public String nullRepresentation = "-";

	@Override
	public @NonNull Renderer<@Nullable BigDecimal> createRenderer() {
		return new BigDecimalRenderer(nullRepresentation);
	}

	@Override
	public <INPUT_TYPE> @NonNull Collector<INPUT_TYPE, ?, @Nullable NumberStatistics<@NonNull BigDecimal>> getCollector() {
		final Arithmetics<BigDecimal> arithmetics = new NullableArithmeticsWrapper<>(new BigDecimalArithmetics());
		return (@NonNull Collector<INPUT_TYPE, ?, @Nullable NumberStatistics<@NonNull BigDecimal>>) PivotCollectors
				.getNumberReducer(arithmetics);
	}
}
