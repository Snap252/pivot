package com.snap252.vaadin.pivot.xml.data;

import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.FilteringComponent;
import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.ui.AbstractComponent;

@NonNullByDefault
public class FilteringComponentImpl<INPUT_TYPE, DATA_TYPE>
		implements FilteringComponent<INPUT_TYPE, @Nullable DATA_TYPE>, UIConfigurable {
	private final Property<INPUT_TYPE, @Nullable DATA_TYPE> property;
	private final Supplier<UIConfigurable> uiConfigurable;
	private final Function<@Nullable DATA_TYPE, @Nullable DATA_TYPE> rounding;
	private final Function<@Nullable DATA_TYPE, @NonNull String> formatter;

	public FilteringComponentImpl(final Property<INPUT_TYPE, @Nullable DATA_TYPE> property,
			final Supplier<UIConfigurable> uiConfigurable,
			final Function<@Nullable DATA_TYPE, @Nullable DATA_TYPE> rounding,
			final Function<@Nullable DATA_TYPE, String> formatter) {
		this.rounding = rounding;
		this.formatter = formatter;
		assert property != null;
		this.property = property;
		this.uiConfigurable = uiConfigurable;
	}

	@Override
	public Property<INPUT_TYPE, @Nullable DATA_TYPE> getProperty() {
		return property;
	}

	@Override
	public @Nullable DATA_TYPE round(@Nullable final DATA_TYPE t) {
		return rounding.apply(t);
	}

	@Override
	public @NonNull String format(@Nullable final DATA_TYPE t) {
		return formatter.apply(t);
	}

	@Override
	public @Nullable AbstractComponent getComponent() {
		return uiConfigurable.get().getComponent();
	}
}
