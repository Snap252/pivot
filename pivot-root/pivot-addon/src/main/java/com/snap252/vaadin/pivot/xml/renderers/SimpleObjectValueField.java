package com.snap252.vaadin.pivot.xml.renderers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.PivotCriteria;
import com.snap252.org.pivoting.ShowingSubtotal;
import com.snap252.vaadin.pivot.PropertyProvider;
import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.ui.Label;

public class SimpleObjectValueField extends ValueField<Object> {
	public SimpleObjectValueField() {
		super(new SimpleCountingAggregator());
	}

	@Override
	protected @NonNull Object roundImpl(@NonNull final Object input) {
		return input;
	}

	@Override
	public @NonNull UIConfigurable createUIConfigurable() {
		return () -> new Label("test");
	}

	@Override
	public <INPUT_TYPE> PivotCriteria<INPUT_TYPE, Object> createPivotCriteria(
			final PropertyProvider<INPUT_TYPE, ?> pp) {

		return new PivotCriteria<INPUT_TYPE, Object>() {
			@Override
			public @Nullable Object apply(final INPUT_TYPE t) {
				return t;
			}

			@Override
			public @Nullable String format(final Object t) {
				return t != null ? t.toString() : null;
			}

			@Override
			public String toString() {
				return "---";
			}

			@Override
			public ShowingSubtotal showSubtotal() {
				return subtotal;
			}
		};
	}
}