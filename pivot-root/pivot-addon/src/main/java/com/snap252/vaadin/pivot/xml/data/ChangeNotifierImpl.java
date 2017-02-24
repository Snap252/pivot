package com.snap252.vaadin.pivot.xml.data;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class ChangeNotifierImpl<T> implements ChangeNotifier<T> {

	@Override
	public void addChangeListener(final ChangeListener<T> l) {
		listChangeListeners.add(l);
	}

	@Override
	public void removeChangeListener(final ChangeListener<T> l) {
		listChangeListeners.remove(l);
	}


	private final List<ChangeListener<T>> listChangeListeners = new LinkedList<>();

	public void fireChange(final T t) {
		for (final ChangeListener<T> listChangeListener : listChangeListeners) {
			listChangeListener.changed(t);
		}
	}
}
