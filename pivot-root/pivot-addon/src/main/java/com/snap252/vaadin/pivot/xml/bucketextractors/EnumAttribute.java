package com.snap252.vaadin.pivot.xml.bucketextractors;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.ui.TextField;

public class EnumAttribute extends Attribute<@Nullable Enum<?>> {

	@Override
	protected Enum<?> roundImpl(final Enum<?> input) {
		return input;
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return () -> {
			final TextField tf = new TextField("Anzeige-Name", displayName);
			tf.addValueChangeListener(v -> {
				final String name = (String) v.getProperty().getValue();
				displayName = name;
				fireChange();
			});
			return tf;
		};
	}
}
