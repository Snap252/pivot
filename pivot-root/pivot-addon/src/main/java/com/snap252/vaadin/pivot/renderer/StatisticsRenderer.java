package com.snap252.vaadin.pivot.renderer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.vaadin.pivot.client.ClientNS;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.AbstractRenderer;

import elemental.json.JsonValue;

@SuppressWarnings("rawtypes")
public class StatisticsRenderer extends AbstractRenderer<NumberStatistics> {

	private final DecimalFormat decimalFormat;
	private final Component c;

	public StatisticsRenderer(final String nullRepresentation, final Component c) {
		super(NumberStatistics.class, nullRepresentation);
		this.c = c;
		decimalFormat = new DecimalFormat("########################0.0");
	}

	@SuppressWarnings("null")
	@Override
	public JsonValue encode(final NumberStatistics value) {
		if (value == null)
			return encode(null, ClientNS.class);

		return encode(new ClientNS(toString(value.sum), toString(value.max)), ClientNS.class);
	}

	private String toString(final Number n) {
		if (n == null)
			return "";
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
