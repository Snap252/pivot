package com.snap252.vaadin.pivot.xml.data;

import org.eclipse.jdt.annotation.NonNullByDefault;

@FunctionalInterface
public interface ChangeNotifierSupplier<T> extends ChangeNotifier<T> {
	@NonNullByDefault
	ChangeNotifier<T> getChangeNotifierSupplier();

	@Override
	@NonNullByDefault
	default void addChangeListener(final ChangeListener<T> cl) {
		getChangeNotifierSupplier().addChangeListener(cl);
	}
	@Override
	@NonNullByDefault
	default void removeChangeListener(final ChangeListener<T> cl) {
		getChangeNotifierSupplier().removeChangeListener(cl);
	}

}
