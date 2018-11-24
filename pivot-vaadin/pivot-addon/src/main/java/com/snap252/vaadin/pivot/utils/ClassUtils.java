package com.snap252.vaadin.pivot.utils;

import org.eclipse.jdt.annotation.NonNullByDefault;

public class ClassUtils {

	@NonNullByDefault
	@SuppressWarnings("unchecked")
	public static <T, U> Class<T> cast(final Class<?> c) {
		return (Class<T>) c;
	}

}
