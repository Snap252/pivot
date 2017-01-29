package com.snap252.vaadin.pivot.client;

import com.google.gwt.i18n.client.NumberFormat;
import com.snap252.org.aggregators.NumberStatistics;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.communication.StateChangeEvent.StateChangeHandler;
import com.vaadin.client.connectors.AbstractRendererConnector;
import com.vaadin.shared.ui.Connect;

/**
 * A connector for {@link NumberStatistics} .
 * <p>
 * The server-side Renderer operates on numbers, but the data is serialized as a
 * string, and displayed as-is on the client side. This is to be able to support
 * the server's locale.
 *
 * @since 7.4
 * @author Vaadin Ltd
 */
@Connect(com.snap252.vaadin.pivot.renderer.StatisticsRenderer.class)
public class ClientStatisticsRendererConnector extends AbstractRendererConnector<ClientBigDecimalNumberStatistics> {
	// no implementation needed

	public ClientStatisticsRendererConnector() {
		addStateChangeHandler(new StateChangeHandler() {

			@Override
			public void onStateChanged(final StateChangeEvent stateChangeEvent) {
				final ClientStatisticsRenderer ret = getRenderer();
				ret.setWhatToRender(getState().toRender);
				ret.setNumberFormat(NumberFormat.getFormat(getState().numberFormat));
			}
		});
	}

	@Override
	public ClientStatisticsRenderer getRenderer() {
		final ClientStatisticsRenderer ret = (ClientStatisticsRenderer) super.getRenderer();
		ret.setWhatToRender(getState().toRender);
		ret.setNumberFormat(NumberFormat.getFormat(getState().numberFormat));
		return ret;
	}

	@Override
	public ClientRendererSharedState getState() {
		return (ClientRendererSharedState) super.getState();
	}
}
