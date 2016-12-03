package com.snap252.org;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubBucket<V> extends Bucket<V> {
	public final Collection<Bucket<V>> children;

	public SubBucket(Object bucketValue, List<Function<V, Object>> partitionCriterionsAndSubCriterions,
			SubBucket<V> parent, Function<V, Object> extractor, List<V> values) {
		super(bucketValue, parent, extractor, values);
		assert !partitionCriterionsAndSubCriterions.isEmpty();

		Function<V, Object> ownCriterion = partitionCriterionsAndSubCriterions.get(0);
		Map<Object, List<V>> collect = values.stream().filter(this).collect(Collectors.groupingBy(ownCriterion::apply));

		List<Function<V, Object>> childCriterions = partitionCriterionsAndSubCriterions.subList(1,
				partitionCriterionsAndSubCriterions.size());

		assert childCriterions.size() == partitionCriterionsAndSubCriterions.size() - 1;

		this.children = collect.entrySet().stream().map(e -> {
			return childCriterions.isEmpty() ? new LeafBucket<V>(e.getKey(), this, ownCriterion, e.getValue())
					: new SubBucket<V>(e.getKey(), childCriterions, this, ownCriterion, e.getValue());
		}).collect(Collectors.toList());

		assert children.stream().flatMap(c -> c.values.stream()).collect(toSet()).equals(new HashSet<V>(
				values)) : parent/*
									 * .stream().flatMap(c ->
									 * c.values.stream()).collect(toSet())
									 */
						+ "=> own: " + new HashSet<V>(values);
	}

	protected StringBuilder addString(String linePrefix, StringBuilder sb) {
		super.addString(linePrefix, sb);
		for (Bucket<V> child : children) {
			child.addString(linePrefix + "\t", sb);
		}
		return sb;
	}

	@Override
	protected int getSize() {
		return children.stream().mapToInt(Bucket::getSize).sum();
	}

	@Override
	public Stream<Bucket<V>> stream() {
		return Stream.concat(Stream.of(this), children.stream().flatMap(Bucket::stream));
	}

	@Override
	public Stream<Bucket<V>> reverseStream() {
		return Stream.concat(children.stream().flatMap(Bucket::reverseStream), Stream.of(this));
	}

	@Override
	public Collection<Bucket<V>> getChilren() {
		return children;
	}

}
