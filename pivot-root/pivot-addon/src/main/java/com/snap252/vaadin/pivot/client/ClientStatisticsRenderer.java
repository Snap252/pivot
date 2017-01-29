package com.snap252.vaadin.pivot.client;

import java.math.BigDecimal;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.RendererCellReference;

public class ClientStatisticsRenderer implements Renderer<ClientBigDecimalNumberStatistics> {

	private WhatToRender whatToRender;

	@Override
	public void render(final RendererCellReference cell, final ClientBigDecimalNumberStatistics text) {
		final TableCellElement element = cell.getElement();
		element.getStyle().setTextAlign(TextAlign.RIGHT);

		if (text == null) {
			handleNull(element);
			return;
		}

		element.setInnerText(getKind(text));
	}

	protected String getKind(final ClientBigDecimalNumberStatistics text) {
		switch (whatToRender) {
		case avg:
			return format(text.avg);
		case cnt:
			return text.cnt + "";
		case max:
			return format(text.max);
		case min:
			return format(text.min);
		case sum:
			return format(text.sum);
		default:
			assert false;
			return "no value";
		}
	}
	
	public void setNumberFormat(final NumberFormat nf) {
		this.nf = nf;
	}
	private NumberFormat nf;
	private String format(final BigDecimal bd){
		return nf.format(bd);
	}

	protected void handleNull(final TableCellElement element) {
		element.setInnerText(null);
	}

	public void setWhatToRender(final WhatToRender toRender) {
		this.whatToRender = toRender;
	}
}
