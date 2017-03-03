package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.Bucket;
import com.snap252.org.pivoting.RootBucket;
import com.snap252.vaadin.pivot.BucketItem.CellProperty;
import com.snap252.vaadin.pivot.GridRendererParameter.GridRendererChangeParameterKind;
import com.vaadin.data.Collapsible;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Hierarchical;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Container.PropertySetChangeNotifier;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;

@NonNullByDefault
class BucketContainer<INPUT_TYPE>
		implements Indexed, Hierarchical, ItemSetChangeNotifier, PropertySetChangeNotifier, Collapsible {
	private RootBucket<INPUT_TYPE> rowBucket;
	private RootBucket<INPUT_TYPE> colBucket;
	Collector<INPUT_TYPE, ?, ?> aggregator;

	public RootBucket<INPUT_TYPE> getColBucket() {
		return colBucket;
	}

	final Collection<ValueChangeListener> valueChangeListeners = new HashSet<>();
	public final Collection<BucketItem<INPUT_TYPE>.CellProperty> propertyResetter = new HashSet<>();

	private void resetPropertiesAndFireValueChange() {
		propertyResetter.forEach(CellProperty::resetValue);

		if (valueChangeListeners.isEmpty())
			return;

		final Property.ValueChangeEvent event = new Property.ValueChangeEvent() {

			@Override
			public @Nullable Property<?> getProperty() {
				assert false;
				return null;
			}
		};

		for (final Property.ValueChangeListener valueChangeListener : valueChangeListeners) {
			valueChangeListener.valueChange(event);
		}
	}

	public BucketContainer(final GridRendererParameter<INPUT_TYPE, ?> gp) {
		rowBucket = gp.creatRowBucket(GridRenderer.SUM_TEXT);
		colBucket = gp.creatColBucket(GridRenderer.SUM_TEXT);

		this.aggregator = gp.getCollector();

		gp.addParameterChangeListener(GridRendererChangeParameterKind.ROW_FNKT, e -> {
			rowBucket = e.gridParameter.creatRowBucket(GridRenderer.SUM_TEXT);
			{
				expandedItemIds.clear();
				visibleItemIds.clear();
				final Collection<Bucket<INPUT_TYPE>> rootItemIds = rootItemIds();
				final List<@NonNull ?> openIds = rootItemIds.stream().collect(toList());
				expandedItemIds.addAll(openIds);
				visibleItemIds.addAll(rootItemIds);
				openIds.forEach(this::showDescendants);
			}

			fireItemSetChanged();
		});

		gp.addParameterChangeListener(GridRendererChangeParameterKind.COL_FNKT, e -> {
			colBucket = e.gridParameter.creatColBucket(GridRenderer.SUM_TEXT);
			firePropertySetSetChanged();
		});

		gp.addParameterChangeListener(GridRendererChangeParameterKind.AGGREGATOR, _ignore -> {
			this.aggregator = _ignore.gridParameter.getCollector();
			resetPropertiesAndFireValueChange();
		});

	}

	@Override
	public @Nullable Object nextItemId(final Object itemId) {
		assert false;
		return null;
	}

	@Override
	public @Nullable Object prevItemId(final Object itemId) {
		assert false;
		return null;
	}

	@Override
	public @Nullable Object firstItemId() {
		assert false;
		return null;
	}

	@Override
	public @Nullable Object lastItemId() {
		assert false;
		return null;
	}

	@Override
	public boolean isFirstId(final Object itemId) {
		assert false;
		return false;
	}

	@Override
	public boolean isLastId(final Object itemId) {
		assert false;
		return false;
	}

	@Override
	public @Nullable Object addItemAfter(final Object previousItemId) throws UnsupportedOperationException {
		assert false;
		return null;
	}

	@Override
	public @Nullable Item addItemAfter(final Object previousItemId, final Object newItemId)
			throws UnsupportedOperationException {
		assert false;
		return null;
	}

	private final Map<Bucket<INPUT_TYPE>, BucketItem<INPUT_TYPE>> rowCache = new HashMap<>();

	@Override
	public BucketItem<INPUT_TYPE> getItem(final Object itemId) {
		return getForRow(itemId);
	}

	@SuppressWarnings("unchecked")
	protected BucketItem<INPUT_TYPE> getForRow(final Object itemId) {
		return rowCache.computeIfAbsent((Bucket<INPUT_TYPE>) itemId, x -> new BucketItem<INPUT_TYPE>(this, x));
	}

	@Override
	public Collection<@NonNull ?> getContainerPropertyIds() {
		final List<Object> collect = colBucket.stream().collect(toList());
		collect.add(0, GridRenderer.COLLAPSE_COL_PROPERTY_ID);
		return collect;
	}

	@Override
	public List<@NonNull Bucket<INPUT_TYPE>> getItemIds() {
		return getItemIdStream().collect(toList());
	}

	protected Stream<? extends @NonNull Bucket<INPUT_TYPE>> getItemIdStream() {
		return rowBucket.stream().filter(visibleItemIds::contains);
	}

	@Override
	public @Nullable Property<?> getContainerProperty(final Object itemId, final Object propertyId) {
		return getItem(itemId).getItemProperty(propertyId);
	}

	private final Class<?> pivotCellReferenceClazz = PivotCellReference.class;

	@Override
	public Class<?> getType(final Object propertyId) {
		if (propertyId instanceof Bucket) {
			return pivotCellReferenceClazz;
		} else
			return String.class;
	}

	@Override
	public int size() {
		return getItemIdStream().collect(counting()).intValue();
		// assert false;
		// return 0;
	}

	@Override
	public boolean containsId(final Object itemId) {
		assert getItemIdStream().anyMatch(id -> id == itemId);
		return true;
		// assert false;
		// return false;
	}

	@Override
	public Item addItem(final Object itemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeItem(final Object itemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addContainerProperty(final Object propertyId, final Class<?> type,
			@Nullable final Object defaultValue) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeContainerProperty(final Object propertyId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOfId(final Object itemId) {
		return itemId == null ? -1 : getItemIds().indexOf(itemId);
	}

	@Override
	public Object getIdByIndex(final int index) {
		return getItemIds().get(index);
	}

	@Override
	public List<?> getItemIds(final int startIndex, final int numberOfItems) {
		return getItemIdStream().skip(startIndex).limit(numberOfItems).collect(toList());
		// assert false;
		// return null;
	}

	@Override
	public Object addItemAt(final int index) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Item addItemAt(final int index, final Object newItemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable Collection<@NonNull ?> getChildren(final Object itemId) {
		final Bucket<?> c = (Bucket<?>) itemId;
		return c.getChildren();
	}

	@Override
	public @Nullable Object getParent(final Object itemId) {
		final Bucket<?> r = (Bucket<?>) itemId;
		return r.parent;
	}

	@Override
	public Collection<@NonNull Bucket<INPUT_TYPE>> rootItemIds() {
		assert isRoot(rowBucket);
		@Nullable
		final List<@NonNull Bucket<INPUT_TYPE>> children = rowBucket.getChildren();
		if (children == null)
			return Collections.singleton(rowBucket);

		final List<Bucket<INPUT_TYPE>> arrayList = new ArrayList<>(children);
		arrayList.add(0, rowBucket);
		return arrayList;
	}

	@Override
	public boolean setParent(final Object itemId, @Nullable final Object newParentId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean areChildrenAllowed(final Object itemId) {
		assert false;
		return false;
	}

	@Override
	public boolean setChildrenAllowed(final Object itemId, final boolean areChildrenAllowed)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRoot(final Object itemId) {
		return itemId == rowBucket || ((Bucket<?>)itemId).parent == rowBucket;
	}

	@Override
	public boolean hasChildren(final Object itemId) {
		return ((Bucket<?>) itemId).getChildren() != null && itemId != rowBucket;
	}

	private final Set<Container.ItemSetChangeListener> itemSetEventListeners = new LinkedHashSet<>();

	@Override
	public void addItemSetChangeListener(final ItemSetChangeListener listener) {
		itemSetEventListeners.add(listener);
	}

	@Deprecated
	@Override
	public void addListener(final ItemSetChangeListener listener) {
		addItemSetChangeListener(listener);
	}

	@Override
	public void removeItemSetChangeListener(final ItemSetChangeListener listener) {
		itemSetEventListeners.remove(listener);
	}

	@Deprecated
	@Override
	public void removeListener(final ItemSetChangeListener listener) {
		removeItemSetChangeListener(listener);
	}

	private void fireItemSetChanged() {
		rowCache.clear();
		propertyResetter.clear();
		final ItemSetChangeEvent event = new ItemSetChangeEvent() {
			@Override
			public Container getContainer() {
				return BucketContainer.this;
			}
		};
		for (final ItemSetChangeListener itemSetEventListener : itemSetEventListeners) {
			itemSetEventListener.containerItemSetChange(event);
		}
	}

	private void firePropertySetSetChanged() {
		rowCache.clear();
		propertyResetter.clear();
		final PropertySetChangeEvent event = new PropertySetChangeEvent() {
			@Override
			public Container getContainer() {
				return BucketContainer.this;
			}
		};
		propertySetEventListeners
				.forEach(itemSetEventListener -> itemSetEventListener.containerPropertySetChange(event));
	}

	private final Set<Container.PropertySetChangeListener> propertySetEventListeners = new LinkedHashSet<>();

	@Override
	public void addPropertySetChangeListener(final PropertySetChangeListener listener) {
		propertySetEventListeners.add(listener);
	}

	@Override
	@Deprecated
	public void addListener(final PropertySetChangeListener listener) {
		addPropertySetChangeListener(listener);

	}

	@Override
	public void removePropertySetChangeListener(final PropertySetChangeListener listener) {
		propertySetEventListeners.remove(listener);
	}

	@Override
	@Deprecated
	public void removeListener(final PropertySetChangeListener listener) {
		removePropertySetChangeListener(listener);
	}

	private final Collection<Object> expandedItemIds = new HashSet<>();
	private final Collection<Object> visibleItemIds = new HashSet<>();

	@Override
	public void setCollapsed(final Object itemId, final boolean collapsed) {
		boolean changed;
		if (collapsed) {
			changed = expandedItemIds.remove(itemId);
			hideDescendants(itemId);
		} else {
			changed = expandedItemIds.add(itemId);
			showDescendants(itemId);
		}

		assert changed;
		if (changed)
			fireItemSetChanged();
	}

	@Override
	public boolean isCollapsed(final Object itemId) {
		return !expandedItemIds.contains(itemId);
	}

	private void showDescendants(final Object parentId) {
		insertChildrenIfParentExpandedRecursively(parentId);
	}

	private void insertChildrenIfParentExpandedRecursively(final Object parentId) {
		if (!isCollapsed(parentId)) {
			for (final Object childId : getChildrenx(parentId)) {
				visibleItemIds.add(childId);
				insertChildrenIfParentExpandedRecursively(childId);
			}
		}
	}

	@SuppressWarnings("null")
	protected Collection<@NonNull ?> getChildrenx(final Object parentId) {
		final Bucket<?> b = (Bucket<?>) parentId;
		final List<@NonNull ?> children = b.getChildren();
		return children == null ? Collections.emptySet() : children;
	}

	private void hideDescendants(final Object parentId) {
		removeChildrenRecursively(parentId);
	}

	private void removeChildrenRecursively(final Object parentId) {
		for (final Object childId : getChildrenx(parentId)) {
			final boolean wasVisible = visibleItemIds.remove(childId);
			if (wasVisible) {
				removeChildrenRecursively(childId);
			}
		}
	}

}