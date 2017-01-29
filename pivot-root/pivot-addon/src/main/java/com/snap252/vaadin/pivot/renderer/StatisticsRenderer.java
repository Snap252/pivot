package com.snap252.vaadin.pivot.renderer;

import java.math.BigDecimal;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.vaadin.pivot.client.ClientBigDecimalNumberStatistics;
import com.snap252.vaadin.pivot.client.ClientRendererSharedState;
import com.snap252.vaadin.pivot.client.WhatToRender;
import com.vaadin.ui.Grid.AbstractRenderer;

import elemental.json.JsonValue;

/**
 * For client Side see
 * 
 * @see {com.snap252.vaadin.pivot.client.ClientStatisticsRendererConnector}
 * @author Snap252
 *
 */
@SuppressWarnings("rawtypes")
public class StatisticsRenderer extends AbstractRenderer<NumberStatistics> {

	public StatisticsRenderer(final String nullRepresentation) {
		super(NumberStatistics.class, nullRepresentation);
	}

	@NonNullByDefault
	public StatisticsRenderer setWhatToRender(final WhatToRender toRender) {
		final ClientRendererSharedState state = getState(false);
		if (state.toRender == toRender)
			return this;
		state.toRender = toRender;
		markAsDirty();
		return this;
	}

	@NonNullByDefault
	public StatisticsRenderer setFormat(final String numberFormat) {
		final ClientRendererSharedState state = getState(false);
		if (Objects.equals(state.numberFormat, numberFormat))
			return this;
		state.numberFormat = numberFormat;
		markAsDirty();
		return this;
	}

	@Override
	protected ClientRendererSharedState getState() {
		return (ClientRendererSharedState) super.getState();
	}

	@Override
	protected ClientRendererSharedState getState(final boolean markAsDirty) {
		return (ClientRendererSharedState) super.getState(markAsDirty);
	}

	@SuppressWarnings("null")
	@Override
	public JsonValue encode(final NumberStatistics value0) {
		if (value0 == null)
			return encode(null, ClientBigDecimalNumberStatistics.class);
		final NumberStatistics<BigDecimal> value = value0;
		return encode(new ClientBigDecimalNumberStatistics(value.sum, value.max, value.min, value.avg(), value.cnt),
				ClientBigDecimalNumberStatistics.class);
	}

}
