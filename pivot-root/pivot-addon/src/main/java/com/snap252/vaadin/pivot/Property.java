package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public abstract class Property<INPUT_TYPE, @Nullable OUTPUT_TYPE> {
	protected final Class<@Nullable OUTPUT_TYPE> clazz;
	private final String name;

	public Property(final Class<@Nullable OUTPUT_TYPE> clazz, final String name) {
		this.clazz = clazz;
		this.name = name;
	}

	public Class<@Nullable OUTPUT_TYPE> getType() {
		return clazz;
	}

	String getName() {
		return name;
	}

	@Override
	public final String toString() {
		return name;
	}

	public abstract @Nullable OUTPUT_TYPE getValue(INPUT_TYPE o);
}