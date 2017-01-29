package com.snap252.vaadin.pivot.renderer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.vaadin.pivot.client.ClientNS;
import com.snap252.vaadin.pivot.client.ClientRendererSharedState;
import com.snap252.vaadin.pivot.client.WhatToRender;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.AbstractRenderer;

import elemental.json.JsonValue;

@SuppressWarnings("rawtypes")
/**
 * For client Side see
 * @see {com.snap252.vaadin.pivot.client.ClientStatisticsRendererConnector}
 * @author Snap252
 *
 */
public class StatisticsRenderer extends AbstractRenderer<NumberStatistics> {

	private final DecimalFormat decimalFormat;
	private final Component c;

	public StatisticsRenderer(final String nullRepresentation, final Component c) {
		super(NumberStatistics.class, nullRepresentation);
		this.c = c;
		decimalFormat = new DecimalFormat("########################0.0");
		setWhatToRender(WhatToRender.min);
	}

	public void setWhatToRender(final WhatToRender toRender) {
		getState(true).toRender = toRender;
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

		return encode(
				new ClientNS(toString(value.sum), toString(value.max), toString(value.min), toString(value.avg())),
				ClientNS.class);
	}

	private String toString(final Number n) {
		if (n == null)
			return null;
					
		if (n instanceof BigDecimal) {
			final BigDecimal bigDecimal = (BigDecimal) n;
			decimalFormat.setMinimumFractionDigits(bigDecimal.scale());
			decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(c.getLocale()));
			return decimalFormat.format(bigDecimal);
			// return bigDecimal.toPlainString();
		}
		return n.toString();
	}

}
