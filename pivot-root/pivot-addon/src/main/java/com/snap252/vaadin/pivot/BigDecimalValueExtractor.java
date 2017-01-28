package com.snap252.vaadin.pivot;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.themes.ValoTheme;

@NonNullByDefault
public class BigDecimalValueExtractor extends AbstractFilteringComponent<BigDecimal> {
	private final FormLayout comp;
	private final Slider slider;
	private final CheckBox roundingEnabledCheckBox;
	private final Label sliderValueLabel = new Label("0");

	private boolean roundingEnabled;
	private int sliderValue;

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
	public String getButtonStyles() {
		if (roundingEnabled)
			return ValoTheme.BUTTON_FRIENDLY;

		return super.getButtonStyles();
	}
}
