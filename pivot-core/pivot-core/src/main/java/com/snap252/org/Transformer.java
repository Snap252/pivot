package com.snap252.org;

import java.util.Collection;

public interface Transformer<T, U> {
	U transform(Collection<T> values);
}
