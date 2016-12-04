package com.snap252.org;

import java.util.Collection;

public interface Aggregator<T> {
	T aggregate(Collection<T> values);
}
