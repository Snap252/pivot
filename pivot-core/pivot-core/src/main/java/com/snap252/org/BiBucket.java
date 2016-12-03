package com.snap252.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BiBucket<V> {

	public final Bucket<V> row;
	public final Bucket<V> col;
	// public final Collection<V> data;
	private Function<Pair<Bucket<V>>, BiBucket<V>> parentResolver;

	public BiBucket(Pair<Bucket<V>> p, Function<Pair<Bucket<V>>, BiBucket<V>> parentResolver) {
		this(p.first, p.second, parentResolver);
	}

	// private final List<BiBucket<V>> parents = new ArrayList<>(3);

	public BiBucket<V> rowParent;
	public BiBucket<V> colParent;

	public BiBucket(Bucket<V> row, Bucket<V> column, Function<Pair<Bucket<V>>, BiBucket<V>> parentResolver) {
		assert row != null;
		assert column != null;
		this.row = row;
		this.col = column;
		this.parentResolver = parentResolver;
	}

	private List<V> values;

	protected final List<V> getStreamed(Predicate<V> predicate) {
		List<V> computedValues = computeValues();
		if (computedValues
				.isEmpty()/* || computedValues.stream().allMatch(predicate) */) {
			return computedValues;
		}
		List<V> l = new ArrayList<>(computedValues.size());
		for (V v : computedValues) {
			if (predicate.test(v))
				l.add(v);
		}
		return l;
		// return computedValues.stream().filter(predicate).collect(toList());
	}

	public List<V> computeValues() {
		if (values != null) {
			return values;
		}

		if (rowParent == null)
			rowParent = row.getParent() != null ? parentResolver.apply(new Pair<Bucket<V>>(row.getParent(), col))
					: null;

		if (colParent == null)
			colParent = col.getParent() != null ? parentResolver.apply(new Pair<Bucket<V>>(row, col.getParent()))
					: null;

		if (rowParent == null && colParent == null) {
			/* this is both roots */
			assert row.values.equals(col.values);
			return values = row.values;
		}

		// int parentRowCount = Integer.MAX_VALUE;
		if (rowParent != null) {
			if (colParent == null) {
				return values = rowParent.getStreamed(row);
			}
			// parentRowCount = rowParent.computeValues().size();
		}

		// int parentColCount = Integer.MAX_VALUE;
		if (colParent != null) {
			return values = colParent.getStreamed(col);
		}
		assert false;
		return values = rowParent.getStreamed(row);
	}

	boolean hasValues() {
		return true;
	}

	public String getParentString() {
		return "[" + row.getParentString() + " : " + col.getParentString() + "]";
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + row.getBucketValue() + ":" + col.getBucketValue() + "]";
	}

	public static <V> Stream<BiBucket<V>> getStream(Bucket<V> rowBucket, Bucket<V> colBucket) {
		Map<Pair<Bucket<V>>, BiBucket<V>> cacheMap = new HashMap<>();

		Function<Pair<Bucket<V>>, BiBucket<V>> biBucketResolver = new Function<Pair<Bucket<V>>, BiBucket<V>>() {

			@Override
			public BiBucket<V> apply(Pair<Bucket<V>> v) {
				return new BiBucket<V>(v, t -> cacheMap.computeIfAbsent(t, this));
			}
		};

		return Pair.getCartesianProduct(rowBucket.stream(), () -> colBucket.stream())
				.map(t -> cacheMap.computeIfAbsent(t, biBucketResolver));
	}
}
