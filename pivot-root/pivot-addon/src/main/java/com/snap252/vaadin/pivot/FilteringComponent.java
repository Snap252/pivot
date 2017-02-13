package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface FilteringComponent<T> extends UIConfigurable {
	Object getPropertyId();
	default T round(final T t){
		return t;
	}
}
