package com.snap252.vaadin.pivot.client;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.TableCellElement;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.RendererCellReference;

public class ClientStatisticsRenderer implements Renderer<ClientNS> {

	@Override
	public void render(RendererCellReference cell, ClientNS text) {
		TableCellElement element = cell.getElement();
		element.getStyle().setTextAlign(TextAlign.RIGHT);

		if (text == null) {
			handleNull(element);
			return;
		}
		element.setInnerText(text.sum);
	}

	protected void handleNull(TableCellElement element) {
		element.setInnerText(null);
	}
}
