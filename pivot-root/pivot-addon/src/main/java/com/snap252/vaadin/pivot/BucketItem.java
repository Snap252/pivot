package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.Bucket;
import com.snap252.vaadin.pivot.utils.ClassUtils;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;

@NonNullByDefault
final class BucketItem<INPUT_TYPE> implements Item {
	/**
	 *
	 */
	private final BucketContainer<INPUT_TYPE> bucketContainer;

	final class CellProperty implements Property<PivotCellReference<?, INPUT_TYPE>>, Property.ValueChangeNotifier {

		private final Bucket<INPUT_TYPE> colBucket;
		@Nullable
		private PivotCellReference<?, INPUT_TYPE> cachedPivotCellReference;

		@SuppressWarnings("all")
		public CellProperty(final Bucket<INPUT_TYPE> colBucket) {
			this.colBucket = colBucket;
			assert rowBucket != colBucket;
			/*seems to be a bug in eclipse -> thats why suppress all*/
			bucketContainer.propertyResetter.add(this);
		}

		void resetValue() {
			cachedPivotCellReference = null;
		}

		@Nullable
		private List<INPUT_TYPE> filterOwnValues;

		private Collection<INPUT_TYPE> getOwnItems() {
			if (filterOwnValues != null)
				return filterOwnValues;

			final Bucket<INPUT_TYPE> colParent = colBucket.parent;
			if (colParent != null) {
				final Collection<INPUT_TYPE> itemsInParent = getForColumn(colParent).getOwnItems();
				// TODO: maybe better get if from row parent
				final List<INPUT_TYPE> filterOwnValues$;
				if (itemsInParent.isEmpty())
					filterOwnValues$ = Collections.emptyList();
				else
					filterOwnValues$ = itemsInParent.stream().filter(colBucket).collect(toList());
				filterOwnValues = filterOwnValues$;
				return filterOwnValues$;
			}

			final List<INPUT_TYPE> filterOwnValues$ = rowBucket.filterOwnValues(x -> true).collect(toList());
			filterOwnValues = filterOwnValues$;
			return filterOwnValues$;
		}

		@Override
		public PivotCellReference<?, INPUT_TYPE> getValue() {
			if (cachedPivotCellReference != null)
				return cachedPivotCellReference;

			final Object newValue0 = getOwnItems().stream().collect(bucketContainer.aggregator);
			final PivotCellReference<?, INPUT_TYPE> newValue = new PivotCellReference<@Nullable Object, INPUT_TYPE>(
					newValue0, rowBucket, colBucket, bucketContainer);
			cachedPivotCellReference = newValue;
			return newValue;
		}

		@Override
		public void setValue(@Nullable final PivotCellReference<?, INPUT_TYPE> newValue) throws ReadOnlyException {
			throw new ReadOnlyException();
		}

		@Override
		public Class<PivotCellReference<?, INPUT_TYPE>> getType() {
			return ClassUtils.cast(PivotCellReference.class);
		}

		@Override
		public boolean isReadOnly() {
			return true;
		}

		@Override
		public void setReadOnly(final boolean newStatus) {
		}

		@Override
		public void addValueChangeListener(final com.vaadin.data.Property.ValueChangeListener listener) {
			bucketContainer.valueChangeListeners.add(listener);
		}

		@Override
		public void addListener(final com.vaadin.data.Property.ValueChangeListener listener) {
			addValueChangeListener(listener);

		}

		@Override
		public void removeValueChangeListener(final com.vaadin.data.Property.ValueChangeListener listener) {
			bucketContainer.valueChangeListeners.remove(listener);
		}

		@Override
		public void removeListener(final com.vaadin.data.Property.ValueChangeListener listener) {
			removeValueChangeListener(listener);
		}
	}

	private final Bucket<INPUT_TYPE> rowBucket;

	public BucketItem(final BucketContainer<INPUT_TYPE> bucketContainer, final Bucket<INPUT_TYPE> itemId) {
		this.bucketContainer = bucketContainer;
		this.rowBucket = itemId;
	}

	private final Map<Bucket<INPUT_TYPE>, CellProperty> colCache = new HashMap<>();

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public @NonNull Property<?> getItemProperty(final Object id) {
		if (id == GridRenderer.COLLAPSE_COL_PROPERTY_ID) {
			return new ObjectProperty<@Nullable String>(rowBucket.getFormattedBucketValue(),
					(String.class));
		}
		return getForColumn((Bucket<INPUT_TYPE>) id);
	}

	protected CellProperty getForColumn(final Bucket<INPUT_TYPE> id) {
		return colCache.computeIfAbsent(id, CellProperty::new);
	}

	@SuppressWarnings("null")
	@Override
	public Collection<INPUT_TYPE> getItemPropertyIds() {
		assert false;
		return null;
	}

	@Override
	public boolean addItemProperty(final Object id, @SuppressWarnings("rawtypes") final Property property)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeItemProperty(final Object id) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}