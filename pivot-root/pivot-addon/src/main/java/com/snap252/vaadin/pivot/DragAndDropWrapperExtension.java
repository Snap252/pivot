package com.snap252.vaadin.pivot;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.event.Transferable;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;

public final class DragAndDropWrapperExtension extends DragAndDropWrapper {

	private final Map<String, Object> additionalAttributes = new HashMap<>();

	public DragAndDropWrapperExtension(final Component root, final Map<String, Object> m) {
		super(root);
		additionalAttributes.putAll(m);
	}

	Object put (final String key, final Object o){
		return additionalAttributes.put(key, o);
	}
	public DragAndDropWrapperExtension(final Component root, final String key, final Object value) {
		super(root);
		additionalAttributes.put(key, value);
	}

	public DragAndDropWrapperExtension(final Component root, final Property<?, ?> property) {
		super(root);
		additionalAttributes.put("property", property);
	}

	@Override
	public Transferable getTransferable(final Map<String, Object> rawVariables) {
		final Map<String, Object> newMap = new HashMap<>(rawVariables);
		newMap.putAll(additionalAttributes);
		return super.getTransferable(newMap);
	}
}