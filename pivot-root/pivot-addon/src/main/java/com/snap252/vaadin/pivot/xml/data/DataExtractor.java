package com.snap252.vaadin.pivot.xml.data;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.PivotCriteria;
import com.snap252.vaadin.pivot.PropertyProvider;

@NonNullByDefault
public interface DataExtractor<@Nullable T> {

	<A> PivotCriteria<A, T> createPivotCriteria(final PropertyProvider<A, ?> pp);
}
