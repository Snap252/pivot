package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.FilteringComponent;
import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.PropertyProvider;
import com.snap252.vaadin.pivot.UIConfigurable;
import com.snap252.vaadin.pivot.xml.data.DataExtractor;
import com.snap252.vaadin.pivot.xml.data.FilteringComponentImpl;
import com.vaadin.data.Property.ValueChangeListener;

public abstract class Attribute<@Nullable DATA_TYPE> implements DataExtractor<DATA_TYPE> {
	@XmlAttribute(name = "name", required = true)
	public String attributeName = "";

	@XmlAttribute(name = "subtotal")
	public Boolean subtotal = true;

	protected final DATA_TYPE round(final DATA_TYPE input) {
		return input != null ? roundImpl(input) : null;
	}

	protected abstract @NonNull DATA_TYPE roundImpl(@NonNull DATA_TYPE input);

	@Override
	public final <A> FilteringComponent<A, DATA_TYPE> createFilteringComonent(
			final PropertyProvider<A, ? extends Property<A, DATA_TYPE>> pp) {
		assert !attributeName.isEmpty();
		final Property<A, DATA_TYPE> property = pp.getProperty(attributeName);

		return new FilteringComponentImpl<>(property, createUIConfigurable());
	}

	protected abstract UIConfigurable createUIConfigurable();


	protected final void fireValueChange(){
		System.out.println("Attribute.fireValueChange()");
	}
	public final void addValueChangeListener(final ValueChangeListener valueChangeListener) {
		System.err.println(valueChangeListener);
	}
}
