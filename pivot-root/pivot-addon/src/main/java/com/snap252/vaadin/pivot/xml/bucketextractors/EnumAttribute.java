package com.snap252.vaadin.pivot.xml.bucketextractors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;

public class EnumAttribute extends Attribute<@Nullable Enum<?>> {

	@Override
	protected Enum<?> roundImpl(final @Nullable Enum<?> input) {
		return input;
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return new NumberUIConfigurable();
	}

	private class NumberUIConfigurable implements UIConfigurable {

		private final FormLayout comp;
		private final Slider slider;
		private final CheckBox roundingEnabledCheckBox;
		private final Label sliderValueLabel = new Label("0");

		private boolean roundingEnabled;
		private int sliderValue;

		public NumberUIConfigurable() {
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

	}

}
