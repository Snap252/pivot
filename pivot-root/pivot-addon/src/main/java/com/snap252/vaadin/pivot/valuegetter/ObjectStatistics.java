package com.snap252.vaadin.pivot.valuegetter;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

public class ObjectStatistics {
	public int numberOfValues;
	public int numberOfNonNullValues;
	private final Set<Object> distinctValues = new HashSet<>();

	public void add(@Nullable final Object o) {
		numberOfValues++;
		if (o != null) {
			distinctValues.add(o);
			numberOfNonNullValues++;
		}
	}

	public int getDistinct(){
		return distinctValues.size();
	}

	public ObjectStatistics mergeTo(final ObjectStatistics o2) {
		numberOfValues += o2.numberOfValues;
		numberOfNonNullValues += o2.numberOfNonNullValues;
		distinctValues.addAll(o2.distinctValues);
		return this;
	}

	@Override
	public String toString() {
		return "ObjectStatistics [numberOfValues=" + numberOfValues + ", numberOfNonNullValues=" + numberOfNonNullValues
				+ ", distinctValues=" + distinctValues + "]";
	}

}
