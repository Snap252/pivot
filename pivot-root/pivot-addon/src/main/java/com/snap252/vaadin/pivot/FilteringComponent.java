package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;

@NonNullByDefault
public interface FilteringComponent<T extends Comparable<T>> extends PivotCriteria<@NonNull Item, T>, UIConfigurable {
}
