package com.snap252.vaadin.pivot.xml.data;

import org.eclipse.jdt.annotation.NonNullByDefault;

public interface ChangeNotifier<T> {
	@NonNullByDefault
	void addChangeListener(ChangeListener<T> cl);
	@NonNullByDefault
	void removeChangeListener(ChangeListener<T> cl);
}
