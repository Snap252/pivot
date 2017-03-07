package com.snap252.vaadin.pivot;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.pivoting.Bucket;
import com.snap252.vaadin.pivot.utils.ClassUtils;
import com.vaadin.data.Property;

@NonNullByDefault
public class PivotCellReference<T, INPUT_TYPE> {

	private final T value;
	private final Bucket<INPUT_TYPE> rowBucket2;
	private final Bucket<INPUT_TYPE> colBucket2;
	private final BucketContainer<INPUT_TYPE> container;

	public PivotCellReference(final T newValue, final Bucket<INPUT_TYPE> rowBucket, final Bucket<INPUT_TYPE> colBucket,
			final BucketContainer<INPUT_TYPE> container) {
		this.value = newValue;
		rowBucket2 = rowBucket;
		colBucket2 = colBucket;
		this.container = container;
	}

	public static <T, U> Class<PivotCellReference<T, U>> getClazz() {
		return ClassUtils.cast(PivotCellReference.class);
	}

	private PivotCellReference<T, INPUT_TYPE> getReference(final Bucket<INPUT_TYPE> rowBucket,
			final Bucket<INPUT_TYPE> colBucket) {
		@SuppressWarnings("unchecked")
		final Property<PivotCellReference<T, INPUT_TYPE>> containerProperty = (Property<PivotCellReference<T, INPUT_TYPE>>) container
				.getContainerProperty(rowBucket, colBucket);
		assert containerProperty != null;
		final PivotCellReference<T, INPUT_TYPE> ret = Objects.requireNonNull(containerProperty.getValue());
		assert ret.rowBucket2 == rowBucket;
		assert ret.colBucket2 == colBucket;
		return ret;
	}

	public PivotCellReference<T, INPUT_TYPE> ofCol() {
		return getReference(rowBucket2.getRoot(), colBucket2);
	}

	public PivotCellReference<T, INPUT_TYPE> ofParentCol() {
		return getReference(rowBucket2.getParentOrSelf(), colBucket2);
	}

	public PivotCellReference<T, INPUT_TYPE> ofRow() {
		return getReference(rowBucket2, colBucket2.getRoot());
	}

	public PivotCellReference<T, INPUT_TYPE> ofParentRow() {
		return getReference(rowBucket2, colBucket2.getParentOrSelf());
	}

	public PivotCellReference<T, INPUT_TYPE> ofTotal() {
		return getReference(rowBucket2.getParentOrSelf(), colBucket2.getParentOrSelf());
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "---";
	}
}