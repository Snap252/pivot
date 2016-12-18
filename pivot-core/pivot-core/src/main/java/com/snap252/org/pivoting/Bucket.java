package com.snap252.org.pivoting;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

public abstract class Bucket<V> implements Predicate<V> {
	// private final Predicate<V> predicate;

	protected final Object bucketValue;

	@Nullable
	private final Function<V, Object> extractor;

	protected final Collection<V> values;

	private final int level;

	@Nullable
	private final Bucket<V> parent;

	public Bucket(final Object bucketValue, final @Nullable Bucket<V> parent,
			final @Nullable Function<V, Object> extractor, final Collection<V> values, final int level) {
		this.bucketValue = bucketValue;
		this.parent = parent;
		this.extractor = extractor;
		this.values = values;
		this.level = level;
	}

	public final Object getBucketValue() {
		return bucketValue;
	}

	@Override
	public String toString() {
		return addString("", new StringBuilder()).toString();
	}

	protected StringBuilder addString(final String linePrefix, final StringBuilder sb) {
		sb.append(linePrefix).append(getClass().getSimpleName() + " [bucketValue=" + bucketValue + ", kvps="
		// + Arrays.deepToString(kvps.toArray())
				+ "#" + getSize(0) + "]");
		sb.append("\r\n");
		return sb;
	}

	protected abstract int getSize(int forSelf);

	public abstract Stream<? extends Bucket<V>> stream();

	@Override
	public boolean test(final V v) {
		assert parent != null;
		assert extractor != null;
		final Object extractedValue = extractor.apply(v);
		return /* parent.test(v) && */bucketValue.equals(extractedValue);
	}

	public final Collection<V> filterOwnValues(final Predicate<V> f) {
		return filter(values);
	}

	public abstract @Nullable List<? extends Bucket<V>> getChilren();

	protected Collection<V> filter(final Collection<V> l) {
		if (l.isEmpty()) {
			return l;
		}
		return l.stream().filter(this).collect(toList());
	}

	public abstract int getDepth();

	public int getLevel() {
		return level;
	}

}
