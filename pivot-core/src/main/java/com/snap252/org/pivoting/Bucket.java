package com.snap252.org.pivoting;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public abstract class Bucket<V> implements Predicate<@NonNull V> {
	// private final Predicate<V> predicate;

	@Nullable
	public final Object bucketValue;

	private final PivotCriteria<V, @Nullable ?> extractor;

	protected final Collection<V> values;

	private final int level;

	@Nullable
	public Object internalValue;

	@Nullable
	public final Bucket<V> parent;

	public Bucket(@Nullable final Object bucketValue, final @Nullable Bucket<V> parent,
			final PivotCriteria<V, @Nullable ?> extractor, final Collection<V> values, final int level) {
		this.bucketValue = bucketValue;
		this.parent = parent;
		this.extractor = extractor;
		this.values = values;
		this.level = level;
	}

	@Nullable
	public final Object getBucketValue() {
		return bucketValue;
	}

	@SuppressWarnings("unchecked")
	private <T> T cast(final Object o) {
		return (T) o;
	}

	public final @Nullable String getFormattedBucketValue() {
		return bucketValue != null ? extractor.format(cast(bucketValue)) : null;
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

	public Stream<@NonNull ? extends Bucket<V>> stream() {
		return streamWithSubTotals(getSubTotal());
	}

	protected Stream<@NonNull ? extends Bucket<V>> streamWithSubTotals(final ShowingSubtotal showSubtotal)
			throws AssertionError {
		final @Nullable List<? extends @NonNull Bucket<V>> children$ = getChildren();
		if (children$ == null)
			switch (showSubtotal) {
			case DONT_SHOW:
			case AFTER:
			case BOTH:
			case BEFORE:
				return Stream.of(this);
			default:
				throw new AssertionError(showSubtotal);
			}
		else
			switch (showSubtotal) {
			case DONT_SHOW:
				return children$.stream().flatMap(Bucket::stream);
			case AFTER:
				return Stream.concat(children$.stream().flatMap(Bucket::stream), Stream.of(this));
			case BEFORE:
				return Stream.concat(Stream.of(this), children$.stream().flatMap(Bucket::stream));
			case BOTH:
				return Stream.concat(Stream.of(this),
						Stream.concat(children$.stream().flatMap(Bucket::stream), Stream.of(getWrapped())));
			default:
				throw new AssertionError(showSubtotal);
			}
	}

	@Nullable
	private WrappedBucket<V> b;

	public @Nullable Object getWrappedIfThere() {
		return b;
	}

	private Bucket<V> getWrapped() {
		if (b != null) {
			return b;
		}
		return b = new WrappedBucket<>(this);
	}

	protected ShowingSubtotal getSubTotal() {
		final ShowingSubtotal showSubtotal = extractor.showSubtotal();
		if (showSubtotal != ShowingSubtotal.INHERIT) {
			return showSubtotal;
		}
		return parent != null ? parent.getSubTotal() : ShowingSubtotal.AFTER;
	}

	@Override
	public boolean test(final V v) {
		assert parent != null;
		assert extractor != null;
		@Nullable
		final Object extractedValue = extractor.apply(v);
		return bucketValue == null && extractedValue == null
				|| bucketValue != null && bucketValue.equals(extractedValue);
	}

	public final Stream<V> filterOwnValues(final Predicate<V> f) {
		return values.stream().filter(f);
	}

	public abstract @Nullable List<@NonNull ? extends Bucket<V>> getChildren();

	public Collection<V> filter(final Collection<V> l) {
		if (l.isEmpty()) {
			return l;
		}
		return l.stream().filter(this).collect(toList());
	}

	public abstract int getDepth();

	public int getLevel() {
		return level;
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

	private static class WrappedBucket<V> extends Bucket<V> {
		private final Bucket<V> b;

		WrappedBucket(final Bucket<V> b) {
			super(b.bucketValue, b.parent, b.extractor, b.values, b.level);
			this.b = b;
		}

		@Override
		public int getSize(final int forSelf) {
			return b.getSize(forSelf);
		}

		@Override
		public @Nullable List<@NonNull ? extends @NonNull Bucket<V>> getChildren() {
			return b.getChildren();
		}

		@Override
		public int getDepth() {
			return b.getDepth();
		}

	}
}
