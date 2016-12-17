package com.snap252.org;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Bucket<V> implements Predicate<V> {
	// private final Predicate<V> predicate;

	protected final Object bucketValue;

	private final Function<V, Object> extractor;

	protected final Collection<V> values;

	private final int level;

	private final Bucket<V> parent;

	public Bucket(Object bucketValue, Bucket<V> parent, Function<V, Object> extractor, Collection<V> values,
			int level) {
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

	protected StringBuilder addString(String linePrefix, StringBuilder sb) {
		sb.append(linePrefix).append(getClass().getSimpleName() + " [bucketValue=" + bucketValue + ", kvps="
		// + Arrays.deepToString(kvps.toArray())
				+ "#" + getSize(0) + "]");
		sb.append("\r\n");
		return sb;
	}

	protected abstract int getSize(int forSelf);

	public abstract Stream<? extends Bucket<V>> stream();

	public abstract Stream<? extends Bucket<V>> reverseStream();

	// public abstract Bucket<V> getMaterialized();

	public String getParentString() {
		// char prefix = getClass().getSimpleName().charAt(0);
		String prefix;
		if (this instanceof LeafBucket) {
			prefix = "L";
		} else if (this instanceof SubBucket) {
			prefix = "S";
		} else {
			assert false;
			prefix = "<unknown>";
		}

		final String v = prefix + ":" + getBucketValue();
		if (parent == null) {
			return v;
		}
		return parent.getParentString() + ", " + v;
	}

	@Override
	public boolean test(V v) {
		assert parent != null;
		assert extractor != null;
		final Object extractedValue = extractor.apply(v);
		return /* parent.test(v) && */bucketValue.equals(extractedValue);
	}

	public final Collection<V> filterOwnValues(Predicate<V> f) {
		return filter(values);
	}

	public abstract List<? extends Bucket<V>> getChilren();

	protected Collection<V> filter(Collection<V> l) {
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
