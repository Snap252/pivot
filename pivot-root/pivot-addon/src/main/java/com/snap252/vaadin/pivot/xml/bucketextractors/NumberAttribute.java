package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;

public class NumberAttribute extends Attribute<@Nullable Number> {

	@XmlAttribute(name = "rounding")
	public @Nullable Integer rounding;

	@Override
	protected Number roundImpl(final Number input) {
		final Integer rounding$ = rounding;
		if (rounding$ != null) {
			assert input instanceof BigDecimal;
			final BigDecimal bd = (BigDecimal) input;
			return bd.setScale(rounding$, RoundingMode.HALF_UP);
		}
		return input;
	}

	@Override
	protected String formatImpl(final Number input) {
		if (input instanceof BigDecimal) {
			final BigDecimal bigDecimal = (BigDecimal) input;
			return bigDecimal.toPlainString();
		}
		return super.formatImpl(input);
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return new NumberUIConfigurable();
	}

	private class NumberUIConfigurable implements UIConfigurable {

		private final AbstractComponent comp;
		private final Slider slider = new Slider(-4, 4);
		private final CheckBox roundingEnabledCheckBox = new CheckBox("Rundung", false);
		private final Label sliderValueLabel = new Label("0");

		public NumberUIConfigurable() {

			final TabSheet allTabSheet = new TabSheet(
					getWrapperForTab("common", false, createForDisplayName(NumberAttribute.this)),
					getWrapperForTab("format", false, getRounder()));
			allTabSheet.setWidth("500px");
			allTabSheet.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
			this.comp = allTabSheet;
		}

		protected FormLayout getRounder() {

			final FormLayout formLayout = new FormLayout();

			slider.setVisible(rounding != null);
			if (rounding != null)
				slider.setValue(rounding.doubleValue());

			sliderValueLabel.setVisible(rounding != null);

			roundingEnabledCheckBox.addValueChangeListener(e -> {
				final boolean roundingEnabled = (boolean) e.getProperty().getValue();
				if (!roundingEnabled) {
					rounding = null;
				} else
					rounding = slider.getValue().intValue();

				slider.setVisible(roundingEnabled);
				sliderValueLabel.setVisible(roundingEnabled);
				fireChange();
			});
			roundingEnabledCheckBox.setDescription("Hinweis:<br/>Die Rundung bezieht sich auf die Einzelwerte.");

			slider.setCaption("auf Stellen");
			formLayout.addComponents(roundingEnabledCheckBox, slider, sliderValueLabel);
			formLayout.setWidth("400px");

			slider.addValueChangeListener(_ignore -> {
				final int sliderValue = ((Number) _ignore.getProperty().getValue()).intValue();
				rounding = sliderValue;
				this.sliderValueLabel.setValue(Integer.toString(sliderValue));
				fireChange();
			});
			return formLayout;
		}

		@Override
		public @NonNull AbstractComponent getComponent() {
			return comp;
		}

	}

}
