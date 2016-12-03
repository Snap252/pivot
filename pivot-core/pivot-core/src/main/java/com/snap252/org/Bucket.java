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

	private Function<V, Object> extractor;

	private Bucket<V> parent;

	protected final List<V> values;

	public Bucket(Object bucketValue, Bucket<V> parent, Function<V, Object> extractor, List<V> values) {
		this.bucketValue = bucketValue;
		this.parent = parent;
		this.extractor = extractor;
		this.values = values;
	}

	@Deprecated
	public final Bucket<V> getParent() {
		return parent;
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
				+ "#" + getSize() + "]");
		sb.append("\r\n");
		return sb;
	}

	protected abstract int getSize();

	public abstract Stream<Bucket<V>> stream();

	public abstract Stream<Bucket<V>> reverseStream();

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

		String v = prefix + ":" + getBucketValue();
		if (parent == null) {
			return v;
		}
		return parent.getParentString() + ", " + v;
	}

	@Override
	public boolean test(V v) {
		assert parent != null;
		assert extractor != null;
		Object extractedValue = extractor.apply(v);
		return /* parent.test(v) && */bucketValue.equals(extractedValue);
	}

	public final List<V> filterOwnValues(Predicate<V> f) {
		return filter(values);
	}

	public abstract Collection<Bucket<V>> getChilren();

	protected List<V> filter(List<V> l) {
		if (l.isEmpty()) {
			return l;
		}
		return l.stream().filter(this).collect(toList());
	}

}
