package com.snap252.vaadin.pivot;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.pivoting.Bucket;
import com.snap252.vaadin.pivot.utils.ClassUtils;
import com.vaadin.data.Property;

@NonNullByDefault
public class PivotCellReference<T, ITEM> {

	private final T value;
	private final Bucket<ITEM> rowBucket2;
	private final Bucket<ITEM> colBucket2;
	private final BucketContainer<ITEM> container;

	public PivotCellReference(final T newValue, final Bucket<ITEM> rowBucket, final Bucket<ITEM> colBucket,
			final BucketContainer<ITEM> container) {
		this.value = newValue;
		rowBucket2 = rowBucket;
		colBucket2 = colBucket;
		this.container = container;
	}

	public static <T, U> Class<PivotCellReference<T, U>> getClazz() {
		return ClassUtils.cast(PivotCellReference.class);
	}

	private PivotCellReference<T, ITEM> getReference(final Bucket<ITEM> rowBucket, final Bucket<ITEM> colBucket) {
		@SuppressWarnings("unchecked")
		final Property<PivotCellReference<T, ITEM>> containerProperty = (Property<PivotCellReference<T, ITEM>>) container
				.getContainerProperty(rowBucket, colBucket);
		assert containerProperty != null;
		final PivotCellReference<T, ITEM> ret = Objects.requireNonNull(containerProperty.getValue());
		assert ret.rowBucket2 == rowBucket;
		assert ret.colBucket2 == colBucket;
		return ret;
	}

	public PivotCellReference<T, ITEM> ofCol() {
		return getReference(rowBucket2.getRoot(), colBucket2);
	}

	public PivotCellReference<T, ITEM> ofParentCol() {
		return getReference(rowBucket2.getParentOrSelf(), colBucket2);
	}

	public PivotCellReference<T, ITEM> ofRow() {
		return getReference(rowBucket2, colBucket2.getRoot());
	}

	public PivotCellReference<T, ITEM> ofParentRow() {
		return getReference(rowBucket2, colBucket2.getParentOrSelf());
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "---";
	}
}