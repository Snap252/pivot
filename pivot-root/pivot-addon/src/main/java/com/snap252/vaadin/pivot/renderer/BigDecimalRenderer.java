package com.snap252.vaadin.pivot.renderer;

import java.math.BigDecimal;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.vaadin.pivot.client.ClientRendererSharedState;
import com.vaadin.ui.Grid.AbstractRenderer;

import elemental.json.JsonValue;

/**
 * For client Side see
 * 
 * @see {com.snap252.vaadin.pivot.client.ClientStatisticsRendererConnector}
 * @author Snap252
 *
 */
public class BigDecimalRenderer extends AbstractRenderer<BigDecimal> {

	public BigDecimalRenderer(final String nullRepresentation) {
		super(BigDecimal.class, nullRepresentation);
	}

	@NonNullByDefault
	public BigDecimalRenderer setFormat(final String numberFormat) {
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
	public JsonValue encode(final BigDecimal value) {
		if (value == null)
			return encode(null, BigDecimal.class);
		return encode(value, BigDecimal.class);
	}
}
