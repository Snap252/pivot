package com.snap252.vaadin.pivot.valuegetter;

import com.snap252.vaadin.pivot.FilteringComponent;

public interface FilteringRenderingComponent<T extends Comparable<T>, VALUE>
		extends FilteringComponent<T>, ModelAggregtor<VALUE> {

}
