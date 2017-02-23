package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;

public class StringAttribute extends Attribute<@Nullable String> {
	@XmlAttribute(name = "substring")
	public Integer subString = 0;

	@Override
	protected String roundImpl(final String input) {
		return input;
	}

	@Override
	protected UIConfigurable createUIConfigurable() {
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
			this.comp = formLayout;
		}

		@Override
		public @NonNull AbstractComponent getComponent() {
			return comp;
		}

		@Override
		public void addValueChangeListener(@NonNull final ValueChangeListener valueChangeListener) {

		}

	}
}
