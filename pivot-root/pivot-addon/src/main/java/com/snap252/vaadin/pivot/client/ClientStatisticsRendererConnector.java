package com.snap252.vaadin.pivot.client;

import com.snap252.org.aggregators.NumberStatistics;
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
public class ClientStatisticsRendererConnector extends AbstractRendererConnector<ClientNS> {
	// no implementation needed

	@Override
	public ClientStatisticsRenderer getRenderer() {
		return (ClientStatisticsRenderer) super.getRenderer();
	}
}
