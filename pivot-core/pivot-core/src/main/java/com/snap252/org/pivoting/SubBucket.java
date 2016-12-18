package com.snap252.org.pivoting;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class SubBucket<V> extends Bucket<V> {
	public final List<Bucket<V>> children;
	public final int depth;

	public SubBucket(final Object bucketValue, final List<Function<V, Object>> partitionCriterionsAndSubCriterions,
			@Nullable final SubBucket<V> parent, @Nullable final Function<V, Object> extractor,
			final Collection<V> values, final int level) {
		super(bucketValue, parent, extractor, values, level);
		assert !partitionCriterionsAndSubCriterions.isEmpty();
		this.depth = partitionCriterionsAndSubCriterions.size();

		final Function<V, Object> ownCriterion = partitionCriterionsAndSubCriterions.get(0);

		final Collector<V, ?, Map<Object, List<V>>> groupingBy = Collectors.groupingBy(ownCriterion::apply);
		final Map<Object, List<V>> collect = values.stream().filter(this).collect(groupingBy);

		final List<Function<V, Object>> childCriterions = partitionCriterionsAndSubCriterions.subList(1,
				partitionCriterionsAndSubCriterions.size());

		assert childCriterions.size() == partitionCriterionsAndSubCriterions.size() - 1;

		this.children = collect.entrySet().stream().map(e -> {
			return childCriterions.isEmpty()
					? new LeafBucket<V>(e.getKey(), this, ownCriterion, e.getValue(), level + 1)
					: new SubBucket<V>(e.getKey(), childCriterions, this, ownCriterion, e.getValue(), level + 1);
		}).collect(Collectors.toList());

		assert children.stream().flatMap(c -> c.values.stream()).collect(toSet()).equals(new HashSet<V>(
				values)) : parent/*
									 * .stream().flatMap(c ->
									 * c.values.stream()).collect(toSet())
									 */
						+ "=> own: " + new HashSet<V>(values);
	}

	@Override
	protected StringBuilder addString(final String linePrefix, final StringBuilder sb) {
		super.addString(linePrefix, sb);
		for (final Bucket<V> child : children) {
			child.addString(linePrefix + "\t", sb);
		}
		return sb;
	}

	@Override
	protected int getSize(final int forSelf) {
		return children.stream().mapToInt(b -> b.getSize(forSelf)).sum() + forSelf;
	}

	@Override
	public Stream<Bucket<V>> stream() {
		return Stream.concat(Stream.of(this), children.stream().flatMap(Bucket::stream));
	}

	@Override
	public @NonNull List<Bucket<V>> getChilren() {
		return children;
	}

	@Override
	public int getDepth() {
		return depth + 1;
	}
}
