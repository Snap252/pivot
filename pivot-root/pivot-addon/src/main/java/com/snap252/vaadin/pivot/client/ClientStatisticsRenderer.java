package com.snap252.vaadin.pivot.client;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.TableCellElement;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.RendererCellReference;

public class ClientStatisticsRenderer implements Renderer<ClientNS> {

	private WhatToRender toRender;

	@Override
	public void render(final RendererCellReference cell, final ClientNS text) {
		final TableCellElement element = cell.getElement();
		element.getStyle().setTextAlign(TextAlign.RIGHT);

		if (text == null) {
			handleNull(element);
			return;
		}

		element.setInnerText(getKind(text));
	}

	protected String getKind(final ClientNS text) {
		switch (toRender) {
		case avg:
			return text.avg;
		case max:
			return text.max;
		case min:
			return text.min;
		case sum:
			return text.sum;
		default:
			return "k.a.";

		}
	}

	protected void handleNull(final TableCellElement element) {
		element.setInnerText(null);
	}

	public void setWhatToRender(final WhatToRender toRender) {
		this.toRender = toRender;
	}
}
