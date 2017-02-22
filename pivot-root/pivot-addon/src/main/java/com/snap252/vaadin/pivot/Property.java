package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public abstract class Property<RawType> {
	private final Class<?> clazz;
	private final String name;

	public Property(final Class<?> clazz, final String name) {
		this.clazz = clazz;
		this.name = name;
	}

	public Class<?> getType() {
		return clazz;
	}

	String getName() {
		return name;
	}

	@Override
	public final String toString() {
		return name;
	}

	public abstract @Nullable Object getValue(RawType o);
}