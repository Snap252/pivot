package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class NameType {
	public final Object propertyId;
	public final Class<?> type;

	public NameType(final Object propertyId, final Class<?> type) {
		this.propertyId = propertyId;
		this.type = type;
	}

	@Override
	public String toString() {
		return "[id=" + propertyId + ", type=" + type.getSimpleName() + "]";
	}
}