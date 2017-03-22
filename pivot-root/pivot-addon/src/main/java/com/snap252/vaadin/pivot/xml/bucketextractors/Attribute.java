package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.PivotCriteria;
import com.snap252.org.pivoting.ShowingSubtotal;
import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.PropertyProvider;
import com.snap252.vaadin.pivot.i18n.LookupComboBox;
import com.snap252.vaadin.pivot.xml.renderers.ForAttributeAndValueField;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

@NonNullByDefault({})
public abstract class Attribute<@Nullable DATA_TYPE> extends ForAttributeAndValueField<Attribute<?>> {

	@XmlAttribute(name = "subtotal")
	@Nullable
	public ShowingSubtotal subtotal;

	// TODO: not used yet
	@Nullable
	@XmlAttribute(name = "sort")
	public Boolean sort;

	protected final DATA_TYPE round(final DATA_TYPE input) {
		return input != null ? roundImpl(input) : null;
	}

	protected final @NonNull String format(final DATA_TYPE input) {
		return input != null ? formatImpl(input) : "";
	}

	protected @NonNull DATA_TYPE roundImpl(@NonNull final DATA_TYPE input) {
		return input;
	}

	protected @NonNull String formatImpl(@NonNull final DATA_TYPE input) {
		return input.toString();
	}

	public <INPUT_TYPE> PivotCriteria<INPUT_TYPE, ?> createPivotCriteria(final PropertyProvider<INPUT_TYPE, ?> pp) {
		assert !attributeName.isEmpty();
		@SuppressWarnings("unchecked")
		final Property<INPUT_TYPE, @Nullable DATA_TYPE> property = (Property<INPUT_TYPE, @Nullable DATA_TYPE>) pp
				.getProperty(attributeName);

		return new PivotCriteria<INPUT_TYPE, DATA_TYPE>() {
			@Override
			public DATA_TYPE apply(final INPUT_TYPE t) {
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
			public @NonNull ShowingSubtotal showSubtotal() {
				return subtotal != null ? subtotal : ShowingSubtotal.INHERIT;
			}
		};
	}

	@NonNullByDefault
	protected static AbstractComponent createForDisplayName(final Attribute<?> att) {
		final TextField namingTextField = createNamingTextField(att);

		final AbstractSelect subtotalCombobox = new LookupComboBox("sub_total", ShowingSubtotal.values());

		subtotalCombobox.setNullSelectionItemId(ShowingSubtotal.INHERIT);
		subtotalCombobox.setNullSelectionAllowed(true);
		subtotalCombobox.setValue(att.subtotal);

		subtotalCombobox.addValueChangeListener(valueChangeEvent -> {
			final ShowingSubtotal showSubTotal = (ShowingSubtotal) valueChangeEvent.getProperty().getValue();
			if (att.subtotal == showSubTotal)
				return;

			att.subtotal = showSubTotal;
			att.fireChange();

		});

		final FormLayout formLayout = new FormLayout(namingTextField, subtotalCombobox);
		// fl.setSizeUndefined();
		formLayout.setWidth(500, Unit.PIXELS);
		return formLayout;
	}
}
