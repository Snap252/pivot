package com.snap252.vaadin.pivot.xml.bucketextractors;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;

public class EnumAttribute extends Attribute<@Nullable Enum<?>> {

	@Override
	protected Enum<?> roundImpl(final Enum<?> input) {
		return input;
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return () -> createForDisplayName(this);
	}


}
