package com.snap252.vaadin.pivot.xml.renderers;

import java.lang.reflect.Method;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.FormLayout;

public abstract class FormLayoutField<T> extends FormLayout implements DefaultField<T> {
	private static final Method VALUE_CHANGE_METHOD;
	private final Class<? extends T> clazz;

	@SuppressWarnings({ "null", "unchecked" })
	protected FormLayoutField(final T initalValue) {
		value = initalValue;
		this.clazz = (Class<? extends T>) initalValue.getClass();
	}

	@Override
	public final Class<? extends T> getType() {
		return clazz;
	}

	protected T value;

	@Override
	public final T getValue() {
		return value;
	}

	@Override
	public void setValue(final T value) {
		assert value != null;
		fireEvents = false;
		try {
			this.value = value;
			doAfterValueSetWithoutEvents(value);
		} finally {
			fireEvents = true;
		}
	}

	protected abstract void doAfterValueSetWithoutEvents(final T value);

	static {
		try {
			VALUE_CHANGE_METHOD = Property.ValueChangeListener.class.getDeclaredMethod("valueChange",
					new Class[] { Property.ValueChangeEvent.class });
		} catch (final java.lang.NoSuchMethodException e) {
			// This should never happen
			throw new java.lang.RuntimeException("Internal error finding methods in AbstractField");
		}
	}

	@Override
	public final void addValueChangeListener(final Property.ValueChangeListener listener) {
		addListener(AbstractField.ValueChangeEvent.class, listener, VALUE_CHANGE_METHOD);
		// ensure "automatic immediate handling" works
		markAsDirty();
	}

	@Override
	public final void focus() {
		super.focus();
	}

	protected final void fireValueChange() {
		if (fireEvents)
			fireEvent(new AbstractField.ValueChangeEvent(this));
	}

	private boolean fireEvents = true;

}
