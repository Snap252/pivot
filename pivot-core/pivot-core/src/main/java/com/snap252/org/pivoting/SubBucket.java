package com.snap252.org.pivoting;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class SubBucket<V> extends Bucket<V> {
	@Nullable
	public final List<Bucket<V>> children;
	public final int depth;

	public SubBucket(@Nullable final Object bucketValue,
			final List<? extends PivotCriteria<V, @Nullable ?>> partitionCriterionsAndSubCriterions,
			@Nullable final SubBucket<V> parent, final PivotCriteria<V, @Nullable ?> extractor,
			final Collection<V> values, final int level) {
		super(bucketValue, parent, extractor, values, level);
		// assert !partitionCriterionsAndSubCriterions.isEmpty();
		this.depth = partitionCriterionsAndSubCriterions.size();

		if (!partitionCriterionsAndSubCriterions.isEmpty())
			this.children = createChildren(partitionCriterionsAndSubCriterions, parent, values, level);
		else
			children = null;
	}

	private <@Nullable A> List<Bucket<V>> createChildren(final PivotCriteria<V, A> ownCriterion,
			final List<? extends PivotCriteria<V, @Nullable ?>> childCriterions, @Nullable final SubBucket<V> parent,
			final Collection<V> values, final int level) {

		final Collector<@NonNull V, ?, Map<Optional<A>, List<V>>> groupingBy = Collectors
				.groupingBy(t -> Optional.ofNullable(ownCriterion.apply(t)), LinkedHashMap::new, Collectors.toList());
		final Map<Optional<A>, List<V>> collect = values.stream().filter(this).collect(groupingBy);

		// Function<Optional<Comparable<?>>, Comparable<?>> fo= x->x.get();
		// final Comparator<Optional<Comparable<?>>> comp =
		// Comparator.comparing((Optional<Comparable<?>> x) -> x.get());
		// fixme: sorting
		final List<Bucket<V>> children$ = collect.entrySet().stream()
				/* .sorted(comparingByKey) */.map(e -> {
					return new SubBucket<V>(e.getKey().orElse(null), childCriterions, this, ownCriterion, e.getValue(),
							level + 1);
				}).collect(Collectors.toList());

		assert children$.stream().flatMap(c -> c.values.stream()).collect(toSet())
				.equals(new HashSet<V>(values)) : parent + "=> own: " + new HashSet<V>(values);
		return children$;
	}

	private List<Bucket<V>> createChildren(
			final List<? extends PivotCriteria<V, @Nullable ?>> partitionCriterionsAndSubCriterions,
			@Nullable final SubBucket<V> parent, final Collection<V> values, final int level) {

		@NonNull
		final PivotCriteria<V, @Nullable ?> ownCriterion = partitionCriterionsAndSubCriterions.get(0);
		final List<? extends PivotCriteria<V, ?>> childCriterions = partitionCriterionsAndSubCriterions.subList(1,
				partitionCriterionsAndSubCriterions.size());
		assert childCriterions.size() == partitionCriterionsAndSubCriterions.size() - 1;
		return createChildren(ownCriterion, childCriterions, parent, values, level);
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
