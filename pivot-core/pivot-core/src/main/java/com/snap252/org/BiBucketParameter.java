package com.snap252.org;

import java.util.List;
import java.util.function.Function;

public class BiBucketParameter<V> {
	public final List<V> values;
	public final List<Function<V, Object>> rowFnkt;
	public final List<Function<V, Object>> colFnkt;

	public BiBucketParameter(List<V> values, List<Function<V, Object>> colFnkt, List<Function<V, Object>> rowFnkt) {
		this.values = values;
		this.colFnkt = colFnkt;
		this.rowFnkt = rowFnkt;
	}
}