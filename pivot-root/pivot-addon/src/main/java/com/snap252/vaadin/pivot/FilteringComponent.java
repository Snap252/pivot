package com.snap252.vaadin.pivot;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface FilteringComponent<T> extends UIConfigurable {
	Property getProperty();
	default T round(final T t){
		return t;
	}

	default String format(final T t){
		return Objects.toString(t);
	}
}
