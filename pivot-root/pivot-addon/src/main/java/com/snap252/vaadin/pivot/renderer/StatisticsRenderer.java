package com.snap252.vaadin.pivot.renderer;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.vaadin.pivot.client.ClientNS;
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

	private final DecimalFormat decimalFormat;

	public StatisticsRenderer(final String nullRepresentation) {
		super(NumberStatistics.class, nullRepresentation);
		decimalFormat = new DecimalFormat("########################0.0");
		setWhatToRender(WhatToRender.min);
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
	public JsonValue encode(final NumberStatistics value) {
		if (value == null)
			return encode(null, ClientNS.class);

		return encode(new ClientNS(toString(value.sum), toString(value.max), toString(value.min), toString(value.avg()),
				toString(value.cnt)), ClientNS.class);
	}

	private String toString(final Number n) {
		if (n == null)
			return null;

		if (n instanceof BigDecimal) {
			final BigDecimal bigDecimal = (BigDecimal) n;
			decimalFormat.setMinimumFractionDigits(bigDecimal.scale());
			// decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(c.getLocale()));
			return decimalFormat.format(bigDecimal);
			// return bigDecimal.toPlainString();
		}
		return n.toString();
	}

}
