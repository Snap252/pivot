package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public abstract class AbstractFilteringComponent<T> implements FilteringComponent<T> {

	protected final NameType nameType;
	protected final Object propertyId;

	@Override
	public Object getPropertyId() {
		return propertyId;
	}

	public AbstractFilteringComponent(final NameType nameType) {
		this.nameType = nameType;
		this.propertyId = nameType.propertyId;
	}

	@Override
	public String toString() {
		return nameType.propertyId.toString();
	}

}
