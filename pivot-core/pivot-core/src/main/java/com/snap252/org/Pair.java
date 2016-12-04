package com.snap252.org;

import java.io.Serializable;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class Pair<T> implements Serializable {
	protected final T first;
	protected final T second;
	private static final long serialVersionUID = 1360822168806852921L;
	
	public Pair(T first, T second) {
		assert first != null;
		assert second != null;
		this.first = first;
		this.second = second;
	}

	public Pair(Pair<T> pair) {
		this(pair.first, pair.second);
	}

	@Override
	public String toString() {
		return "(" + this.first + ", " + this.second + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof Pair))
			return false;

		final Pair<?> p = (Pair<?>) o;
		return this.first.equals(p.first) && this.second.equals(p.second);

	}

	@Override
	public int hashCode() {
		return (31 + this.first.hashCode()) * 31 + this.second.hashCode();
	}

	public static <T> Stream<Pair<T>> getCartesianProduct(Stream<T> c1, Supplier<Stream<T>> c2) {
		return c1.flatMap(e1 -> c2.get().map(e2 -> new Pair<T>(e1, e2)));
	}
}