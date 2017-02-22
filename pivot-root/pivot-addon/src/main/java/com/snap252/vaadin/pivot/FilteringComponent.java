package com.snap252.vaadin.pivot;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public interface FilteringComponent<INPUT_TYPE, @Nullable DATA_TYPE> extends UIConfigurable {
	Property<INPUT_TYPE, @Nullable DATA_TYPE> getProperty();

	default DATA_TYPE round(final DATA_TYPE t) {
		return t;
	}

	default @Nullable String format(final DATA_TYPE t) {
		return t == null ? null : Objects.toString(t);
	}
}
