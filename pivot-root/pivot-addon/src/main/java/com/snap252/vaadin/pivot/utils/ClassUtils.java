package com.snap252.vaadin.pivot.utils;

public class ClassUtils {

	@SuppressWarnings("unchecked")
	public static <T, U> Class<T> cast(final Class<?> c) {
		return (Class<T>) c;
	}

}
