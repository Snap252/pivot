package com.snap252.org.pivoting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;

public final class BiBucketParameter<V> {
	public final List<V> values;
	public List<Function<V, Object>> rowFnkt = new ArrayList<>();
	public List<Function<V, Object>> colFnkt = new ArrayList<>();

	public BiBucketParameter(final List<V> values) {
		this.values = values;
	}

	@SafeVarargs
	public final BiBucketParameter<V> setRowFnkt(@NonNull final Function<V, Object>... rowFnkt) {
		assert this.rowFnkt.isEmpty();
		this.rowFnkt = Arrays.asList(rowFnkt);
		return this;
	}

	@SafeVarargs
	public final BiBucketParameter<V> setColFnkt(@NonNull final Function<V, Object>... colFnkt) {
		assert this.colFnkt.isEmpty();
		this.colFnkt = Arrays.asList(colFnkt);
		return this;
	}
}