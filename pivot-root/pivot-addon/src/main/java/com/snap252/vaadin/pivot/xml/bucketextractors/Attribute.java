package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.FilteringComponent;
import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.PropertyProvider;
import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifier;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifierImpl;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifierSupplier;
import com.snap252.vaadin.pivot.xml.data.DataExtractor;
import com.snap252.vaadin.pivot.xml.data.FilteringComponentImpl;

public abstract class Attribute<@Nullable DATA_TYPE>
		implements DataExtractor<DATA_TYPE>, ChangeNotifierSupplier<Attribute<?>> {
	@XmlAttribute(name = "name", required = true)
	public String attributeName = "";

	@XmlAttribute(name = "display-name")
	@Nullable
	public String displayName;

	@Nullable
	@XmlAttribute(name = "subtotal")
	public Boolean subtotal;

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
	public final <INPUT_TYPE> FilteringComponent<INPUT_TYPE, DATA_TYPE> createFilteringComonent(
			final PropertyProvider<INPUT_TYPE, ?> pp) {
		assert !attributeName.isEmpty();
		@SuppressWarnings("unchecked")
		final Property<INPUT_TYPE, DATA_TYPE> property = (Property<INPUT_TYPE, @Nullable DATA_TYPE>) pp
				.getProperty(attributeName);
		// TODO: assert type

		return new FilteringComponentImpl<>(property, this::createUIConfigurable, this::round, this::format);
	}

	public abstract UIConfigurable createUIConfigurable();

	@XmlTransient
	public String getDisplayName() {
		return displayName != null && !displayName.isEmpty() ? displayName : attributeName;
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

}
