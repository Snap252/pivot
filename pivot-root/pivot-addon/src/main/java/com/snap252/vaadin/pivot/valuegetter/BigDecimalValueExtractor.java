package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.Arithmetics;
import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.vaadin.pivot.NameType;
import com.snap252.vaadin.pivot.PivotCellReference;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.snap252.vaadin.pivot.renderer.WhatToRender;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class BigDecimalValueExtractor extends AbstractNumberValueGetterRenderingComponent<BigDecimal> {
	private final FormLayout comp;
	private final Slider slider;
	private final CheckBox roundingEnabledCheckBox;
	private final Label sliderValueLabel = new Label("0");

	private boolean roundingEnabled;
	private int sliderValue;
	private final ComboBox howToRenderComboBox = new ComboBox("Anzeige", Arrays.asList(WhatToRender.values()));

	private final TextField numberFormatTextField = new TextField("Format", "0.00##");

	private final CheckBox relativeCheckBox = new CheckBox("Relativ", false);

	private WhatToRender whatToRender = WhatToRender.sum;

	@SuppressWarnings("null")
	public BigDecimalValueExtractor(final NameType nameType) {
		super(nameType);
		roundingEnabledCheckBox = new CheckBox("Rundung", false);
		final FormLayout formLayout = new FormLayout();
		slider = new Slider(-4, 4);
		slider.setVisible(false);
		sliderValueLabel.setVisible(false);

		roundingEnabledCheckBox.addValueChangeListener(e -> {
			final boolean roundingEnabled = (boolean) e.getProperty().getValue();
			slider.setVisible(roundingEnabled);
			this.roundingEnabled = roundingEnabled;
			sliderValueLabel.setVisible(roundingEnabled);
		});
		roundingEnabledCheckBox.setDescription("Hinweis:<br/>Die Rundung bezieht sich auf die Einzelwerte.");

		slider.setCaption("auf Stellen");
		formLayout.addComponents(roundingEnabledCheckBox, slider, sliderValueLabel);
		formLayout.setWidth("400px");
		this.comp = formLayout;

		slider.addValueChangeListener(_ignore -> {
			final int sliderValue = ((Number) _ignore.getProperty().getValue()).intValue();
			this.sliderValue = sliderValue;
			this.sliderValueLabel.setValue(Integer.toString(sliderValue));
		});

		howToRenderComboBox.setNullSelectionAllowed(false);
		howToRenderComboBox.setValue(WhatToRender.sum);

		howToRenderComboBox.addValueChangeListener(_ignore -> {
			this.whatToRender = (WhatToRender) _ignore.getProperty().getValue();
		});
		numberFormatTextField.addValidator(value -> {
			try {
				new DecimalFormat((String) value);
			} catch (final Exception e) {
				throw new InvalidValueException(e.getMessage());
			}

		});
		formLayout.addComponents(howToRenderComboBox, numberFormatTextField, relativeCheckBox);
	}

	@Override
	public @NonNull AbstractComponent getComponent() {
		return comp;
	}

	@Override
	public BigDecimal apply(final Item item) {
		final BigDecimal ret = getValueIntern(item);
		if (roundingEnabled) {
			return ret.setScale(sliderValue, RoundingMode.HALF_UP);// .setScale(-sliderValue,
																	// RoundingMode.HALF_UP);
			// return ret.round(roundingContext);
		}
		return ret;
	}

	protected BigDecimal getValueIntern(final Item item) {
		return (BigDecimal) getValueFromItem(item);
	}

	protected final Object getValueFromItem(final Item item) {
		return super.apply(item);
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener l) {
		roundingEnabledCheckBox.addValueChangeListener(l);
		slider.addValueChangeListener(l);
		relativeCheckBox.addValueChangeListener(l);
	}

	@Override
	public void addRendererChangeListener(final ValueChangeListener l) {
		howToRenderComboBox.addValueChangeListener(l);
		numberFormatTextField.addValueChangeListener(l);
		relativeCheckBox.addValueChangeListener(l);
	}

	@Override
	public String getButtonStyles() {
		if (roundingEnabled)
			return ValoTheme.BUTTON_FRIENDLY;

		return super.getButtonStyles();
	}

	@Override
	protected final Arithmetics<BigDecimal> createArithmetics() {
		return new BigDecimalArithmetics();
	}

	// @Override
	// public RendererConverter<NumberStatistics<BigDecimal>, BigDecimal>
	// createRenderer() {
	// final StatisticsRenderer statisticsRenderer = new
	// StatisticsRenderer("---");
	// assert whatToRender != null;
	// final String value = numberFormatTextField.getValue();
	// assert value != null;
	//// statisticsRenderer.setWhatToRender(whatToRender).setFormat(value);
	// new BigDecimalValueExtractor(nameType)
	//
	// new RendererConverter<>(new StatisticsRenderer(),
	// whatToRender::getValue);
	// }

	@Override
	public RendererConverter<BigDecimal, ? extends NumberStatistics<BigDecimal>> createRendererConverter() {
		final BigDecimalRenderer renderer = new BigDecimalRenderer("---");
		final String numberFormat = numberFormatTextField.getValue();
		assert numberFormat != null;
		renderer.setFormat(numberFormat);

		final Function<@Nullable NumberStatistics<BigDecimal>, @Nullable BigDecimal> singleExtractor = t -> t == null ? null
				: whatToRender.getValue(t);

		final Arithmetics<BigDecimal> createArithmetics = createArithmetics();
		final Function<PivotCellReference<@Nullable NumberStatistics<BigDecimal>>, @Nullable BigDecimal> f = (
				final PivotCellReference<@Nullable NumberStatistics<BigDecimal>> x) -> {

			final NumberStatistics<BigDecimal> ownValue = x.getValue();
			if (ownValue == null)
				return null;

			final BigDecimal ownSum = singleExtractor.apply(ownValue);
			final PivotCellReference<@Nullable NumberStatistics<BigDecimal>> parentReference = x.ofParentRow();
			assert parentReference != null;
			final NumberStatistics<BigDecimal> parentValue = parentReference.getValue();
			assert parentValue != null;
			final BigDecimal parentSum = singleExtractor.apply(parentValue);

			return createArithmetics.div(ownSum, parentSum);
		};
		if (relativeCheckBox.getValue())
			return new RendererConverter<>(renderer, f, BigDecimal.class);

		return new RendererConverter<>(renderer, x -> singleExtractor.apply(x.getValue()), BigDecimal.class);
	}
}
