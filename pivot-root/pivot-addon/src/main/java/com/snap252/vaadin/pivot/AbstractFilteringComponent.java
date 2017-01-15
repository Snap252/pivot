package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Item;

@NonNullByDefault
public abstract class AbstractFilteringComponent<T extends Comparable<T>>
		implements FilteringComponent<T> {

	protected final NameType nameType;
	protected final Object propertyId;

	public AbstractFilteringComponent(NameType nameType) {
		this.nameType = nameType;
		this.propertyId = nameType.propertyId;
	}

	@Override
	public String toString() {
		return nameType.propertyId.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T apply(Item item) {
		return (T) item.getItemProperty(propertyId).getValue();
	}
}
