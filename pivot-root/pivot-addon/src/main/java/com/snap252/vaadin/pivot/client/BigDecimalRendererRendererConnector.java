package com.snap252.vaadin.pivot.client;

import java.math.BigDecimal;

import com.google.gwt.i18n.client.CurrencyList;
import com.google.gwt.i18n.client.constants.NumberConstants;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.communication.StateChangeEvent.StateChangeHandler;
import com.vaadin.client.connectors.AbstractRendererConnector;
import com.vaadin.shared.ui.Connect;

/**
 * A connector for {@link BigDecimal} .
 * <p>
 * The server-side Renderer operates on numbers, but the data is serialized as a
 * string, and displayed as-is on the client side. This is to be able to support
 * the server's locale.
 *
 * @since 7.4
 * @author Vaadin Ltd
 */
@Connect(com.snap252.vaadin.pivot.renderer.BigDecimalRenderer.class)
public class BigDecimalRendererRendererConnector extends AbstractRendererConnector<BigDecimal> {
	// no implementation needed

	public BigDecimalRendererRendererConnector() {
		addStateChangeHandler(new StateChangeHandler() {

			@Override
			public void onStateChanged(final StateChangeEvent stateChangeEvent) {
				final ClientBigDecimalRendererRenderer ret = getRenderer();
				final ClientRendererSharedState state = getState();
				ret.setNumberFormat(new LocalizedNumberFormat(state.numberFormat));
				ret.setNullRepresentation(state.nullRepresentation);
				ret.setDepth(state.depth);
			}
		});
	}

	static class LocalizedNumberFormat extends com.google.gwt.i18n.client.NumberFormat {
		protected LocalizedNumberFormat(final String pattern) {
			super(new MyNumberConstants(defaultNumberConstants), pattern, CurrencyList.get().getDefault(), true);
		}

		static class MyNumberConstants implements NumberConstants {
			private final NumberConstants nc;

			MyNumberConstants(final NumberConstants nc) {
				this.nc = nc;
			}

			@Override
			public String notANumber() {
				return nc.notANumber();
			}

			@Override
			public String currencyPattern() {
				return nc.currencyPattern();
			}

			@Override
			public String decimalPattern() {
				return nc.decimalPattern();
			}

			@Override
			public String decimalSeparator() {
				return ",";
			}

			@Override
			public String defCurrencyCode() {
				return nc.defCurrencyCode();
			}

			@Override
			public String exponentialSymbol() {
				return nc.exponentialSymbol();
			}

			@Override
			public String globalCurrencyPattern() {
				return nc.globalCurrencyPattern();
			}

			@Override
			public String groupingSeparator() {
				return nc.groupingSeparator();
			}

			@Override
			public String infinity() {
				return nc.infinity();
			}

			@Override
			public String minusSign() {
				return nc.minusSign();
			}

			@Override
			public String monetaryGroupingSeparator() {
				return nc.monetaryGroupingSeparator();
			}

			@Override
			public String monetarySeparator() {
				return nc.monetarySeparator();
			}

			@Override
			public String percent() {
				return nc.percent();
			}

			@Override
			public String percentPattern() {
				return nc.percentPattern();
			}

			@Override
			public String perMill() {
				return nc.perMill();
			}

			@Override
			public String plusSign() {
				return nc.plusSign();
			}

			@Override
			public String scientificPattern() {
				return nc.scientificPattern();
			}

			@Override
			public String simpleCurrencyPattern() {
				return nc.simpleCurrencyPattern();
			}

			@Override
			public String zeroDigit() {
				return nc.zeroDigit();
			}

		}
	}

	@Override
	public ClientBigDecimalRendererRenderer getRenderer() {
		final ClientBigDecimalRendererRenderer ret = (ClientBigDecimalRendererRenderer) super.getRenderer();
		final ClientRendererSharedState state = getState();
		ret.setNumberFormat(new LocalizedNumberFormat(state.numberFormat));
		ret.setNullRepresentation(state.nullRepresentation);
		ret.setDepth(state.depth);
		return ret;
	}

	@Override
	public ClientRendererSharedState getState() {
		return (ClientRendererSharedState) super.getState();
	}
}
