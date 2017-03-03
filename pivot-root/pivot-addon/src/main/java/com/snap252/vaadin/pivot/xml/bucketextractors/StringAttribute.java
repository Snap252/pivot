package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;

public class StringAttribute extends Attribute<@Nullable String> {
	@XmlAttribute(name = "substring")
	public Integer subString = 0;

	@Override
	protected String roundImpl(final String input) {
		if (subString != 0 && input.length() > subString)
			return input.substring(0, subString);
		return input;
	}

	@Override
	public @NonNull UIConfigurable createUIConfigurable() {
		return new StringUIConfigurable();
	}

	private class StringUIConfigurable implements UIConfigurable {

		private final FormLayout comp;
		private final Slider slider;

		public StringUIConfigurable() {
			final FormLayout formLayout = new FormLayout();
			slider = new Slider(0, 10);
			slider.setCaption("SubString");
			formLayout.addComponent(slider);
			formLayout.setWidth("200px");

			slider.addValueChangeListener(vce -> {
				final int i = ((Number)vce.getProperty().getValue()).intValue();
				if (i == subString)
					return;
				subString = i;
				fireChange();
			});
			slider.setValue(subString.doubleValue());
			this.comp = formLayout;
		}

		@Override
		public @NonNull AbstractComponent getComponent() {
			return comp;
		}

	}
}
