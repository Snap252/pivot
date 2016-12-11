package com.snap252.org;

import java.util.stream.Collector;
import java.util.stream.Stream;

public interface Aggregator<T> extends Collector<T, Object, T> {
	T aggregate(Stream<T> values);
}
