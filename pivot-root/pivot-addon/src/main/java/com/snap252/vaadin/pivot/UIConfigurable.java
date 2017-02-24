package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@NonNullByDefault
public interface UIConfigurable {
	@Nullable
	AbstractComponent getComponent();

	default AbstractOrderedLayout getWrapper(final String caption, final @NonNull Component... children) {
		final VerticalLayout verticalLayout = new VerticalLayout(children);
		verticalLayout.setCaption(caption);
//		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		return verticalLayout;
	}
}
