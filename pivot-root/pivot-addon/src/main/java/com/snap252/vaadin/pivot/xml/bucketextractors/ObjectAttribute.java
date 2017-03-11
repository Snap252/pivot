package com.snap252.vaadin.pivot.xml.bucketextractors;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;

public class ObjectAttribute extends Attribute<@Nullable Object> {

	@Override
	public UIConfigurable createUIConfigurable() {
		return () -> createForDisplayName(this);
	}
}
