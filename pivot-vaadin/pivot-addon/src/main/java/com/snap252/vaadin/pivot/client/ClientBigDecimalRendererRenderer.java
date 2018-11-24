package com.snap252.vaadin.pivot.client;

import java.math.BigDecimal;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.RendererCellReference;

public class ClientBigDecimalRendererRenderer implements Renderer<BigDecimal> {

	private String nullRepresentation = null;
	private Gradient gradient;

	@Override
	public void render(final RendererCellReference cell, final BigDecimal value) {
		final TableCellElement element = cell.getElement();

		final Style elementStyle = element.getStyle();
		elementStyle.setTextAlign(TextAlign.RIGHT);
		element.addClassName("column-depth-" + depth);

		if (gradient != null) {
			final Color color = gradient.interpolate(value != null ? value.floatValue() : 0f);
			elementStyle.setBackgroundImage("linear-gradient(to bottom, " + (color.toRGBACssString(100) + " 0%, ")
					+ (color.toRGBACssString(120) + " 25%,") + (color.toRGBACssString(200) + " 50%,")
					+ (color.toRGBACssString(180) + " 95%,") + (color.toRGBACssString(100) + " 100%") + ")");
			elementStyle.setBorderColor(color.toRGBACssString(255));
		} else {
			elementStyle.setBorderColor(null);
			elementStyle.setBackgroundImage(null);
		}

		elementStyle.setColor("black");
		element.setInnerText(getTextual(value));
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

	public void setGradient(final Gradient gradient) {
		this.gradient = gradient;
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
