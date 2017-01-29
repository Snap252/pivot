package com.snap252.vaadin.pivot.client;

import java.math.BigDecimal;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.RendererCellReference;

public class ClientStatisticsRenderer implements Renderer<BigDecimal> {


	@Override
	public void render(final RendererCellReference cell, final BigDecimal value) {
		final TableCellElement element = cell.getElement();
		element.getStyle().setTextAlign(TextAlign.RIGHT);

		if (value == null) {
			handleNull(element);
			return;
		}
		element.setInnerText(getText(value));
	}

	protected String getText(final BigDecimal value) {
		return format(value);
	}

	public void setNumberFormat(final NumberFormat nf) {
		this.nf = nf;
	}

	private NumberFormat nf;

	private String format(final BigDecimal bd) {
		return nf.format(bd);
	}

	protected void handleNull(final TableCellElement element) {
		element.setInnerText(null);
	}
}
