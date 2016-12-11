package com.snap252.org;

@FunctionalInterface
public interface Transformer<T, U> {
	U transform(T values);
}
