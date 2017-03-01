package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.PivotCriteria;
import com.snap252.org.pivoting.ShowingSubtotal;
import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.PropertyProvider;
import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifier;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifierImpl;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifierSupplier;
import com.snap252.vaadin.pivot.xml.data.DataExtractor;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public abstract class Attribute<@Nullable DATA_TYPE>
		implements DataExtractor<DATA_TYPE>, ChangeNotifierSupplier<Attribute<?>> {
	@XmlAttribute(name = "name", required = true)
	public String attributeName = "";

	@XmlAttribute(name = "display-name")
	@Nullable
	public String displayName;

	@Nullable
	@XmlAttribute(name = "subtotal")
	public ShowingSubtotal subtotal = ShowingSubtotal.After;

	@Nullable
	@XmlAttribute(name = "sort")
	public Boolean sort;

	protected final DATA_TYPE round(final DATA_TYPE input) {
		return input != null ? roundImpl(input) : null;
	}

	protected final String format(final DATA_TYPE input) {
		return input != null ? formatImpl(input) : "";
	}

	protected abstract @NonNull DATA_TYPE roundImpl(@NonNull DATA_TYPE input);

	protected String formatImpl(@NonNull final DATA_TYPE input) {
		return input.toString();
	}

	@Override
	public <INPUT_TYPE> PivotCriteria<INPUT_TYPE, DATA_TYPE> createPivotCriteria(
			final PropertyProvider<INPUT_TYPE, ?> pp) {
		assert !attributeName.isEmpty();
		@SuppressWarnings("unchecked")
		final Property<INPUT_TYPE, DATA_TYPE> property = (Property<INPUT_TYPE, @Nullable DATA_TYPE>) pp
				.getProperty(attributeName);

		return new PivotCriteria<INPUT_TYPE, DATA_TYPE>() {
			@Override
			public @Nullable DATA_TYPE apply(final INPUT_TYPE t) {
				return round(property.getValue(t));
			}

			@Override
			public @Nullable String format(final DATA_TYPE t) {
				return Attribute.this.format(t);
			}

			@Override
			public String toString() {
				return property.getName();
			}

			@Override
			public @Nullable ShowingSubtotal showSubtotal() {
				return subtotal;
			}
		};
	}

	public abstract UIConfigurable createUIConfigurable();

	@XmlTransient
	public String getDisplayName() {
		final String displayName$ = displayName;
		return displayName$ != null && !displayName$.isEmpty() ? displayName$ : attributeName;
	}

	@XmlTransient
	@Override
	public ChangeNotifier<Attribute<?>> getChangeNotifierSupplier() {
		return cn;
	}

	private final ChangeNotifierImpl<Attribute<?>> cn = new ChangeNotifierImpl<>();

	protected final void fireChange() {
		cn.fireChange(this);
	}

	protected static AbstractComponent createForDisplayName(final Attribute<?> att) {
		final TextField tf = new TextField("Anzeige-Name", att.displayName);
		tf.addValueChangeListener(v -> {
			final String name = (String) v.getProperty().getValue();
			att.displayName = name;
			att.fireChange();
		});

		final ComboBox cb = new ComboBox("Zwischensumme", Arrays.asList(ShowingSubtotal.values()));
		cb.setValue(att.subtotal);
		
		cb.setNullSelectionAllowed(true);
		cb.addValueChangeListener(vce -> {
			final ShowingSubtotal sst = (ShowingSubtotal) vce.getProperty().getValue();
			if (att.subtotal == sst)
				return;
			
			att.subtotal = sst;
			att.fireChange();
			
		});
		final FormLayout fl = new FormLayout(tf, cb);
//		fl.setSizeUndefined();
		fl.setWidth(500, Unit.PIXELS);
		return fl;
	}

}
