package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifier;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifierImpl;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifierSupplier;
import com.vaadin.ui.TextField;

public abstract class ForAttributeAndValueField<T> implements ChangeNotifierSupplier<ForAttributeAndValueField<T>> {
	@XmlAttribute(name = "property-name", required = true)
	public String attributeName = "";

	private final ChangeNotifierImpl<ForAttributeAndValueField<T>> cn = new ChangeNotifierImpl<>();

	public abstract UIConfigurable createUIConfigurable();

	@XmlTransient
	@Override
	public ChangeNotifier<ForAttributeAndValueField<T>> getChangeNotifierSupplier() {
		return cn;
	}

	protected final void fireChange() {
		cn.fireChange(this);
	}

	@XmlAttribute(name = "display-name")
	@Nullable
	public String displayName;

	@XmlTransient
	public final String getDisplayName() {
		final String displayName$ = displayName;
		return displayName$ != null && !displayName$.isEmpty() ? displayName$ : attributeName;
	}

	protected static TextField createTextField(final ForAttributeAndValueField<?> att) {
		final TextField tf = new TextField("Anzeige-Name", att.displayName);
		tf.setValue(att.getDisplayName());
		tf.addValueChangeListener(v -> {
			final String name = (String) v.getProperty().getValue();
			if (!Objects.equals(att.getDisplayName(), name)) {
				att.displayName = name;
				att.fireChange();
			}
		});
		return tf;
	}
}
