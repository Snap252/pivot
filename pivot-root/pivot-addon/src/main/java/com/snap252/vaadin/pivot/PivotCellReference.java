package com.snap252.vaadin.pivot;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.pivoting.Bucket;
import com.snap252.vaadin.pivot.GridRenderer.GridWriter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

@NonNullByDefault
public class PivotCellReference<T> {

	private final T value;
	private final Bucket<Item> rowBucket2;
	private final Bucket<Item> colBucket2;
	private final GridWriter<?, ?>.BucketContainer container;

	public PivotCellReference(final T newValue, final Bucket<Item> rowBucket, final Bucket<Item> colBucket,
			final GridWriter<?, ?>.BucketContainer container) {
		this.value = newValue;
		rowBucket2 = rowBucket;
		colBucket2 = colBucket;
		this.container = container;
	}

	private PivotCellReference<T> getReference(final Bucket<Item> rowBucket, final Bucket<Item> colBucket) {
		@SuppressWarnings("unchecked")
		final Property<PivotCellReference<T>> containerProperty = (Property<PivotCellReference<T>>) container
				.getContainerProperty(rowBucket, colBucket);
		assert containerProperty != null;
		final PivotCellReference<T> ret = Objects.requireNonNull(containerProperty.getValue());
		assert ret.rowBucket2 == rowBucket;
		assert ret.colBucket2 == colBucket;
		return ret;
	}

	public PivotCellReference<T> ofCol() {
		return getReference(rowBucket2.getRoot(), colBucket2);
	}

	public PivotCellReference<T> ofParentCol() {
		return getReference(rowBucket2.getParentOrSelf(), colBucket2);
	}

	public PivotCellReference<T> ofRow() {
		return getReference(rowBucket2, colBucket2.getRoot());
	}

	public PivotCellReference<T> ofParentRow() {
		return getReference(rowBucket2, colBucket2.getParentOrSelf());
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "---";
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> cast(final Class<?> c) {
		return (Class<T>) c;
	}
}