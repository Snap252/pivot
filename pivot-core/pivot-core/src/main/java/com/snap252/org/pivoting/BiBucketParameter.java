package com.snap252.org.pivoting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public final class BiBucketParameter<V> {
	public final List<V> values;
	public List<PivotCriteria<V, ?>> rowFnkt = new ArrayList<>();
	public List<PivotCriteria<V, ?>> colFnkt = new ArrayList<>();

	public BiBucketParameter(final List<V> values) {
		this.values = values;
	}

	@SafeVarargs
	public final BiBucketParameter<V> setRowFnkt(@NonNull final PivotCriteria<V, ?>... rowFnkt) {
		assert this.rowFnkt.isEmpty();
		this.rowFnkt = Arrays.asList(rowFnkt);
		return this;
	}

	@SafeVarargs
	public final BiBucketParameter<V> setColFnkt(@NonNull final PivotCriteria<V, ?>... colFnkt) {
		assert this.colFnkt.isEmpty();
		this.colFnkt = Arrays.asList(colFnkt);
		return this;
	}
}