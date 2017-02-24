package com.snap252.vaadin.pivot.xml.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.list.AbstractListDecorator;
import org.eclipse.jdt.annotation.NonNull;

public class NotifyingList<T> extends AbstractListDecorator<T> implements ChangeNotifierSupplier<@NonNull List<T>> {

	public NotifyingList() {
		super(new ArrayList<>());
	}

	@NonNull
	private final ChangeNotifierImpl<@NonNull List<T>> i = new ChangeNotifierImpl<>();

	@Override
	public ChangeNotifier<@NonNull List<T>> getChangeNotifierSupplier() {
		return i;
	}

	@NonNull
	private final ChangeListener<? super Object> cl = _ignore -> fireChange();

	@Override
	public boolean add(final T t) {
		final boolean changed = super.add(t);
		assert changed : "a list should always be changed after an add.";
		addListenerIfSupported(t);
		fireChange();
		return changed;
	}

	protected void fireChange() {
		i.fireChange(this);
	}

	private void addListenerIfSupported(final T t) {
		if (t instanceof ChangeNotifier) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final ChangeNotifier<? super Object> changeNotifier = (ChangeNotifier) t;
			changeNotifier.addChangeListener(cl);
		}
	}

	private void removeListenerIfSupported(final Object t) {
		if (t instanceof ChangeNotifier) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final ChangeNotifier<? super Object> changeNotifier = (ChangeNotifier) t;
			changeNotifier.removeChangeListener(cl);
		}
	}

	@Override
	public void add(final int index, final T t) {
		super.add(index, t);
		addListenerIfSupported(t);
		fireChange();
	}

	@Override
	public boolean remove(final Object object) {
		final boolean changed = super.remove(object);
		// TODO:maybe some assertions here..?
		if (changed) {
			removeListenerIfSupported(object);
			fireChange();
		}
		return changed;
	}

	@Override
	public boolean addAll(final Collection<? extends T> coll) {
		final boolean ret = super.addAll(coll);
		coll.stream().forEach(this::addListenerIfSupported);
		fireChange();
		return ret;
	}

	@Override
	public T set(final int index, final T t) {
		final T oldValue = super.set(index, t);
		if (!Objects.equals(oldValue, t)) {
			removeListenerIfSupported(oldValue);
			addListenerIfSupported(t);
			fireChange();
		}
		return oldValue;
	}

	@Override
	public void clear() {
		if (isEmpty())
			return;
		stream().forEach(this::removeListenerIfSupported);
		super.clear();
		fireChange();
	}

	@Override
	public boolean removeAll(final Collection<?> coll) {
		final boolean changed = super.removeAll(coll);
		// TODO:maybe some assertions here..?
		if (changed) {
			coll.stream().forEach(this::removeListenerIfSupported);
			fireChange();
		}
		return changed;
	}

	@Override
	public List<T> subList(final int fromIndex, final int toIndex) {
		assert false : "subList is implemented wrong - so do not call this yet!";
		return null;
	}

}
