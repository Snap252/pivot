package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.snap252.org.aggregators.Arithmetics;
import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.vaadin.pivot.NameType;
import com.snap252.vaadin.pivot.client.WhatToRender;
import com.snap252.vaadin.pivot.renderer.StatisticsRenderer;
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
import com.vaadin.ui.renderers.Renderer;
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
		formLayout.addComponents(howToRenderComboBox, numberFormatTextField);
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
	}

	@Override
	public void addRendererChangeListener(final ValueChangeListener l) {
		howToRenderComboBox.addValueChangeListener(l);
		numberFormatTextField.addValueChangeListener(l);
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

	@Override
	public Class<?> getModelType() {
		return NumberStatistics.class;
	}

	@Override
	public Renderer<?> createRenderer() {
		final StatisticsRenderer statisticsRenderer = new StatisticsRenderer("---");
		assert whatToRender != null;
		final String value = numberFormatTextField.getValue();
		assert value != null;
		return statisticsRenderer.setWhatToRender(whatToRender).setFormat(value);
	}
}
