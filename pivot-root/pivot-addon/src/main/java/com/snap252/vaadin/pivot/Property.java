package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class Property {
	private final Class<?> clazz;
	private final String name;

	public Property(final Class<?> clazz, final String name) {
		this.clazz = clazz;
		this.name = name;
	}

	Class<?> getType() {
		return clazz;
	}

	String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}