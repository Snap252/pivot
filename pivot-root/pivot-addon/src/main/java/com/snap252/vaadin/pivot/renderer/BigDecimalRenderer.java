package com.snap252.vaadin.pivot.renderer;

import java.math.BigDecimal;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.client.ClientRendererSharedState;
import com.snap252.vaadin.pivot.client.Gradient;
import com.vaadin.ui.Grid.AbstractRenderer;

import elemental.json.JsonValue;

/**
 * For client Side see
 *
 * @see {com.snap252.vaadin.pivot.client.ClientStatisticsRendererConnector}
 * @author Snap252
 *
 */
public class BigDecimalRenderer extends AbstractRenderer<@Nullable BigDecimal>
		implements PivotRenderer<@Nullable BigDecimal> {

	@SuppressWarnings("null")
	@NonNullByDefault
	public BigDecimalRenderer(final String nullRepresentation) {
		super(BigDecimal.class, nullRepresentation);
		setNullRepresentation(nullRepresentation);
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

	public BigDecimalRenderer setNullRepresentation(final String nullRepresentation) {
		final ClientRendererSharedState state = getState(false);
		if (Objects.equals(state.nullRepresentation, nullRepresentation))
			return this;
		state.nullRepresentation = nullRepresentation;
		markAsDirty();
		return this;
	}

	public BigDecimalRenderer setGradient(@Nullable final Gradient gradient) {
		final ClientRendererSharedState state = getState(false);
		if (Objects.equals(state.gradient, gradient))
			return this;
		state.gradient = gradient;
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

	@Override
	@NonNullByDefault
	public JsonValue encode(final @Nullable BigDecimal value) {
		return encode(value, BigDecimal.class);
	}

	@Override
	public void setDepth(final int depth) {
		final ClientRendererSharedState state = getState(false);
		if (state.depth == depth)
			return;
		state.depth = depth;
		markAsDirty();
		return;

	}
}
