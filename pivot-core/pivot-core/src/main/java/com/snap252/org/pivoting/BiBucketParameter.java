package com.snap252.org.pivoting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public final class BiBucketParameter<V> {
	public final List<V> values;
	public List<? extends PivotCriteria<V, ?>> rowFnkt = new ArrayList<>();
	public List<? extends PivotCriteria<V, ?>> colFnkt = new ArrayList<>();

	public BiBucketParameter(final List<V> values) {
		this.values = values;
	}

	public BiBucketParameter(final List<V> values, final List<PivotCriteria<V, ?>> rowFnkt,
			final List<PivotCriteria<V, ?>> colFnkt) {
		this.values = values;
		this.rowFnkt = rowFnkt;
		this.colFnkt = colFnkt;
	}

	public final BiBucketParameter<V> setRowFnkt(final List<? extends PivotCriteria<V, ?>> rowFnkt) {
		this.rowFnkt = rowFnkt;
		return this;
	}

	@SafeVarargs
	public final BiBucketParameter<V> setColFnkt(@NonNull final PivotCriteria<V, ?>... colFnkt) {
		return setColFnkt(Arrays.asList(colFnkt));
	}

	@SafeVarargs
	public final BiBucketParameter<V> setRowFnkt(@NonNull final PivotCriteria<V, ?>... rowFnkt) {
		return setRowFnkt(Arrays.asList(rowFnkt));
	}

	public final BiBucketParameter<V> setColFnkt(final List<? extends PivotCriteria<V, ?>> colFnkt) {
		this.colFnkt = colFnkt;
		return this;
	}
}