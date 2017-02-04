package com.snap252.org.pivoting;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public abstract class Bucket<V> implements Predicate<@NonNull V> {
	// private final Predicate<V> predicate;

	public final Object bucketValue;

	private final PivotCriteria<V, ?> extractor;

	protected final Collection<V> values;

	private final int level;

	@Nullable
	public Object internalValue;

	@Nullable
	public final Bucket<V> parent;

	public final @Nullable String getStyleClass() {
		return extractor.getStyleClass();
	}

	public Bucket(final Object bucketValue, final @Nullable Bucket<V> parent, final PivotCriteria<V, ?> extractor,
			final Collection<V> values, final int level) {
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
		assert extractor != null;
		return extractor.toString() + "=>" + bucketValue;
	}

	protected StringBuilder addString(final String linePrefix, final StringBuilder sb) {
		sb.append(linePrefix).append(getClass().getSimpleName() + " [bucketValue=" + bucketValue + ", kvps="
		// + Arrays.deepToString(kvps.toArray())
				+ "#" + getSize(0) + "]");
		sb.append("\r\n");
		return sb;
	}

	public abstract int getSize(int forSelf);

	public final Stream<? extends Bucket<V>> stream() {
		final @Nullable List<? extends @NonNull Bucket<V>> children$ = getChildren();
		if (children$ != null)
			return Stream.concat(Stream.of(this), children$.stream().flatMap(Bucket::stream));
		else
			return Stream.of(this);
	}

	public final Stream<? extends Bucket<V>> reverseStream() {
		final @Nullable List<? extends @NonNull Bucket<V>> children$ = getChildren();
		if (children$ != null)
			return Stream.concat(children$.stream().flatMap(Bucket::reverseStream), Stream.of(this));
		else
			return Stream.of(this);
	}

	@Override
	public boolean test(final V v) {
		assert parent != null;
		assert extractor != null;
		final Object extractedValue = extractor.apply(v);
		return /* parent.test(v) && */bucketValue.equals(extractedValue);
	}

	public final Stream<V> filterOwnValues(final Predicate<V> f) {
		return values.stream().filter(f);
	}

	public abstract @Nullable List<@NonNull ? extends Bucket<V>> getChildren();

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

	public <W> CopyBucket<V, W> createBucketWithNewValues(final Collection<V> newValuesBase,
			final Collector<V, W, W> collectorWithoutFinisher, final Collector<W, W, W> collectorWithoutTransformer) {
		return new CopyBucket<>(this, newValuesBase, collectorWithoutFinisher, collectorWithoutTransformer, null);
	}

	public final Bucket<V> getParentOrSelf() {
		return parent != null ? parent : this;
	}

	public final Bucket<V> getRoot() {
		Bucket<V> current = this;
		for (; Objects.requireNonNull(current).parent != null; current = current.parent) {

		}
		assert this.parent == null ? current == this : current != this;
		assert current.parent == null;
		return current;
	}
}
