package com.snap252.vaadin.pivot.valuegetter;

import com.snap252.vaadin.pivot.AbstractFilteringComponent;
import com.snap252.vaadin.pivot.NameType;

public abstract class AbstractValueGetterRenderingComponent<T extends Comparable<T>>
		extends AbstractFilteringComponent<T> implements FilteringRenderingComponent<T> {

	public AbstractValueGetterRenderingComponent(final NameType nameType) {
		super(nameType);
	}

}
