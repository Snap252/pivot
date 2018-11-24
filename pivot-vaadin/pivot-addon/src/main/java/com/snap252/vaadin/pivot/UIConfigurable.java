package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.i18n.Labels;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

@NonNullByDefault
public interface UIConfigurable {
	@Nullable
	AbstractComponent getComponent();

	default AbstractOrderedLayout getWrapperForTab(final String caption, final boolean inner,
			final @NonNull Component... children) {
		final FormLayout formLayout = new FormLayout(children) {
			@Override
			public void attach() {
				setCaption(Labels.getString(caption, this));
				super.attach();
			}

			@Override
			public String getCaption() {
				return Labels.getString(caption, this);
			}
		};
		formLayout.setCaption(caption);
		formLayout.setMargin(inner);
		formLayout.setSizeFull();
		return formLayout;
	}
}
