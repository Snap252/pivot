package com.snap252.vaadin.pivot.xml.data;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.FilteringComponent;
import com.snap252.vaadin.pivot.PropertyProvider;

@NonNullByDefault
public interface DataExtractor<@Nullable T> {

	<A> FilteringComponent<A, T> createFilteringComonent(final PropertyProvider<A, ?> pp);
}
