package com.snap252.vaadin.pivot.client;

import java.math.BigDecimal;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.RendererCellReference;

public class ClientBigDecimalRendererRenderer implements Renderer<BigDecimal> {

	private String nullRepresentation = null;

	@Override
	public void render(final RendererCellReference cell, final BigDecimal value) {
		final TableCellElement element = cell.getElement();
		element.getStyle().setTextAlign(TextAlign.RIGHT);

		element.setInnerText(getTextual(value));
		element.addClassName("column-depth-" + depth);
	}

	private String getTextual(final BigDecimal value) {
		return value == null ? nullRepresentation : getText(value);
	}

	protected String getText(final BigDecimal value) {
		return format(value);
	}

	public void setNumberFormat(final NumberFormat nf) {
		this.nf = nf;
	}

	public void setNullRepresentation(final String nullRepresentation) {
		this.nullRepresentation = nullRepresentation;
	}

	private NumberFormat nf;
	private int depth;

	private String format(final BigDecimal bd) {
		return nf.format(bd);
	}

	protected void handleNull(final TableCellElement element) {
		element.setInnerText(nullRepresentation);
	}

	public void setDepth(final int depth) {
		this.depth = depth;
	}
}
