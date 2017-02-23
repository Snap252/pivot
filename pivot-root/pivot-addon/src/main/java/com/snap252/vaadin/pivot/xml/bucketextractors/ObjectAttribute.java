package com.snap252.vaadin.pivot.xml.bucketextractors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Label;

public class ObjectAttribute extends Attribute<@Nullable Object> {

	@Override
	protected final Object roundImpl(@NonNull final Object input) {
		return input;
	}

	@Override
	protected @NonNull UIConfigurable createUIConfigurable() {
		return new DummyUIConfigurable();
	}

	private static class DummyUIConfigurable implements UIConfigurable {

		@Override
		public @Nullable AbstractComponent getComponent() {
			return new Label();
		}

		@Override
		public void addValueChangeListener(@NonNull final ValueChangeListener valueChangeListener) {
			// TODO Auto-generated method stub

		}
	}
}
