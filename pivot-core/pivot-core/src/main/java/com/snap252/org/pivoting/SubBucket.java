package com.snap252.org.pivoting;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

public class SubBucket<V> extends Bucket<V> {
	@Nullable
	public final List<Bucket<V>> children;
	public final int depth;

	public SubBucket(final Object bucketValue,
			final List<? extends PivotCriteria<V, ?>> partitionCriterionsAndSubCriterions,
			@Nullable final SubBucket<V> parent, final PivotCriteria<V, ?> extractor, final Collection<V> values,
			final int level) {
		super(bucketValue, parent, extractor, values, level);
		// assert !partitionCriterionsAndSubCriterions.isEmpty();
		this.depth = partitionCriterionsAndSubCriterions.size();

		if (!partitionCriterionsAndSubCriterions.isEmpty())
			this.children = createChildren(partitionCriterionsAndSubCriterions, parent, values, level);
		else
			children = null;
	}

	protected <A extends Comparable<A>> List<Bucket<V>> createChildren(
			final List<? extends PivotCriteria<V, ?>> partitionCriterionsAndSubCriterions,
			@Nullable final SubBucket<V> parent, final Collection<V> values, final int level) {
		@SuppressWarnings("unchecked")
		final PivotCriteria<V, A> ownCriterion = (PivotCriteria<V, A>) partitionCriterionsAndSubCriterions.get(0);

		@SuppressWarnings("null")
		final Collector<V, ?, Map<A, List<V>>> groupingBy = Collectors.groupingBy(ownCriterion::apply);
		final Map<A, List<V>> collect = values.stream().filter(this).collect(groupingBy);

		final List<? extends PivotCriteria<V, ?>> childCriterions = partitionCriterionsAndSubCriterions.subList(1,
				partitionCriterionsAndSubCriterions.size());

		assert childCriterions.size() == partitionCriterionsAndSubCriterions.size() - 1;

		final List<Bucket<V>> children$ = collect.entrySet().stream().sorted(Entry.comparingByKey(ownCriterion))
				.map(e -> new SubBucket<V>(e.getKey(), childCriterions, this, ownCriterion, e.getValue(), level + 1))
				.collect(Collectors.toList());

		assert children$.stream().flatMap(c -> c.values.stream()).collect(toSet()).equals(new HashSet<V>(
				values)) : parent/*
									 * .stream().flatMap(c ->
									 * c.values.stream()).collect(toSet())
									 */
						+ "=> own: " + new HashSet<V>(values);
		return children$;
	}

	@Override
	protected StringBuilder addString(final String linePrefix, final StringBuilder sb) {
		super.addString(linePrefix, sb);
		if (children != null)
			for (final Bucket<V> child : children) {
				child.addString(linePrefix + "\t", sb);
			}
		return sb;
	}

	@Override
	public int getSize(final int forSelf) {
		if (children != null)
			return children.stream().mapToInt(b -> b.getSize(forSelf)).sum() + forSelf;
		else
			return forSelf;
	}

	@Override
	public @Nullable List<Bucket<V>> getChildren() {
		return children;
	}

	@Override
	public int getDepth() {
		return depth + 1;
	}
}
