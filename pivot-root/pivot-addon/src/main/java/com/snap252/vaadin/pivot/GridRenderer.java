package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

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
import com.snap252.vaadin.pivot.GridRenderer.BucketContainer.BucketItem.CellProperty;
import com.snap252.vaadin.pivot.GridRendererParameter.GridRendererChangeParameterKind;
import com.snap252.vaadin.pivot.utils.ClassUtils;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtor;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtor.RendererConverter;
import com.vaadin.data.Collapsible;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Hierarchical;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Container.PropertySetChangeListener;
import com.vaadin.data.Container.PropertySetChangeNotifier;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

@NonNullByDefault
final class GridRenderer {

	private static final String SUM_TEXT = "\u2211";

	<X> GridRenderer(final GridRendererParameter<X> gridParameter, final Grid grid) throws IllegalArgumentException {
		final BucketContainer<X> bucketContainer = new BucketContainer<>(gridParameter);
		grid.setContainerDataSource(bucketContainer);

		gridParameter.addParameterChangeListener(GridRendererChangeParameterKind.AGGREGATOR,
				_ignore -> updateGridColumns(grid, _ignore.gridParameter.getModelAggregator()));

		final PropertySetChangeListener columnsChanged = _ignore -> {
			updateGridColumns(grid, gridParameter.getModelAggregator());
			grid.setColumnOrder(grid.getContainerDataSource().getContainerPropertyIds().toArray());
			updateGridHeader(grid, bucketContainer.colBucket, gridParameter.getColDepth());

		};
		bucketContainer.addPropertySetChangeListener(columnsChanged);
	}

	protected void updateGridColumns(final Grid grid, final ModelAggregtor<?> modelAggregator) {
		grid.getColumns().forEach(column -> {
			final Object columnPropertyId = column.getPropertyId();
			if (columnPropertyId != COLLAPSE_COL_PROPERTY_ID) {
				final RendererConverter<?, ?> rc = modelAggregator.createRendererConverter();
				final Bucket<?> colBucket = (Bucket<?>) columnPropertyId;
				rc.setToColumn(column, colBucket.getLevel());
				column.setMinimumWidth(75);
			} else
				column.setMinimumWidth(170);
		});
		grid.setFrozenColumnCount(1);
	}

	// private final ModelAggregtorDelegate aggregatorDelegator = new
	// ModelAggregtorDelegate();
	//
	// private void setModelAggregator(final ModelAggregtor<?> modelAggregator)
	// {
	// this.aggregatorDelegator.setDelegate(modelAggregator);
	// fireValueChange();
	// }
	//
	// private ModelAggregtor<?> getModelAggregator() {
	// return this.aggregatorDelegator.getDelegate();
	// }
	//
	// public void updateRenderer(final Grid g) {
	//
	// g.getColumns().forEach(col -> {
	// col.setResizable(false);
	// col.setSortable(false);
	// if (col.getPropertyId() != colProp) {
	// aggregatorDelegator.createRendererConverter().setToColumn(col);
	// col.setMinimumWidth(50);
	// } else
	// col.setMinimumWidth(170);
	//
	// });
	// /*
	// * hack for https://vaadin.com/forum#!/thread/9319379
	// */
	// // g.setCellStyleGenerator(g.getCellStyleGenerator());
	// }

	private static final Object COLLAPSE_COL_PROPERTY_ID = new Object() {
		@Override
		public String toString() {
			return "";
		}
	};

	static class BucketContainer<X>
			implements Indexed, Hierarchical, ItemSetChangeNotifier, PropertySetChangeNotifier, Collapsible {
		private RootBucket<X> rowBucket;
		private RootBucket<X> colBucket;
		private Collector<Object, ?, ?> aggregator;

		private final Collection<ValueChangeListener> valueChangeListeners = new HashSet<>();
		private final Collection<BucketContainer<X>.BucketItem.CellProperty> propertyResetter = new HashSet<>();

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

		public BucketContainer(final GridRendererParameter<X> gp) {
			rowBucket = gp.creatRowBucket(SUM_TEXT);
			colBucket = gp.creatColBucket(SUM_TEXT);

			this.aggregator = gp.getCollector();

			gp.addParameterChangeListener(GridRendererChangeParameterKind.ROW_FNKT, e -> {
				rowBucket = e.gridParameter.creatRowBucket(SUM_TEXT);
				{
					final List<@NonNull ?> openIds = rootItemIds().stream().collect(toList());
					expandedItemIds.addAll(openIds);
					visibleItemIds.addAll(rootItemIds());
					openIds.forEach(this::showDescendants);
				}

				fireItemSetChanged();
			});

			gp.addParameterChangeListener(GridRendererChangeParameterKind.COL_FNKT, e -> {
				colBucket = e.gridParameter.creatColBucket(SUM_TEXT);
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

		private final Map<Bucket<X>, BucketItem> cache = new HashMap<>();

		@Override
		public BucketItem getItem(final Object itemId) {
			return getForRow(itemId);
		}

		@SuppressWarnings("unchecked")
		protected BucketItem getForRow(final Object itemId) {
			return cache.computeIfAbsent((Bucket<X>) itemId, x -> new BucketItem(x));
		}

		final class BucketItem implements Item {
			final class CellProperty implements Property<PivotCellReference<?, ?>>, Property.ValueChangeNotifier {

				private final Bucket<X> colBucket;
				@Nullable
				private PivotCellReference<?, ?> cachedPivotCellReference;

				public CellProperty(final Bucket<X> colBucket) {
					this.colBucket = colBucket;
					assert rowBucket != colBucket;
					propertyResetter.add(this);
				}

				private void resetValue() {
					cachedPivotCellReference = null;
				}

				@Nullable
				private List<X> filterOwnValues;

				private Collection<X> getOwnItems() {
					if (filterOwnValues != null)
						return filterOwnValues;

					final Bucket<X> colParent = colBucket.parent;
					if (colParent != null) {
						final Collection<X> itemsInParent = getForColumn(colParent).getOwnItems();
						// TODO: maybe better get if from row parent
						final List<X> v$;
						if (itemsInParent.isEmpty())
							v$ = Collections.emptyList();
						else
							v$ = itemsInParent.stream().filter(colBucket).collect(toList());
						filterOwnValues = v$;
						return v$;
					}

					final List<X> v$ = rowBucket.filterOwnValues(x -> true).collect(toList());
					filterOwnValues = v$;
					return v$;
				}

				@Override
				public PivotCellReference<?, ?> getValue() {
					if (cachedPivotCellReference != null)
						return cachedPivotCellReference;

					final Object newValue0 = getOwnItems().stream().collect(aggregator);
					final PivotCellReference<@Nullable ?, X> newValue = new PivotCellReference<@Nullable Object, X>(
							newValue0, rowBucket, colBucket, BucketContainer.this);
					cachedPivotCellReference = newValue;
					return newValue;
				}

				@Override
				public void setValue(@Nullable final PivotCellReference<?, ?> newValue) throws ReadOnlyException {
					throw new ReadOnlyException();
				}

				@Override
				public Class<PivotCellReference<?, Item>> getType() {
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
					valueChangeListeners.add(listener);
				}

				@Override
				public void addListener(final com.vaadin.data.Property.ValueChangeListener listener) {
					addValueChangeListener(listener);

				}

				@Override
				public void removeValueChangeListener(final com.vaadin.data.Property.ValueChangeListener listener) {
					valueChangeListeners.remove(listener);
				}

				@Override
				public void removeListener(final com.vaadin.data.Property.ValueChangeListener listener) {
					removeValueChangeListener(listener);
				}
			}

			private final Bucket<X> rowBucket;

			public BucketItem(final Bucket<X> itemId) {
				this.rowBucket = itemId;
			}

			private final Map<Bucket<X>, CellProperty> cache = new HashMap<>();

			@SuppressWarnings({ "unchecked" })
			@Override
			public @NonNull Property<?> getItemProperty(final Object id) {
				if (id == COLLAPSE_COL_PROPERTY_ID) {
					return new ObjectProperty<>(rowBucket.getBucketValue());
				}
				return getForColumn((Bucket<X>) id);
			}

			protected CellProperty getForColumn(final Bucket<X> id) {
				return cache.computeIfAbsent(id, CellProperty::new);
			}

			@SuppressWarnings("null")
			@Override
			public Collection<X> getItemPropertyIds() {
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

		@Override
		public Collection<@NonNull ?> getContainerPropertyIds() {
			final List<Object> collect = colBucket.reverseStream().collect(toList());
			collect.add(0, COLLAPSE_COL_PROPERTY_ID);
			return collect;
		}

		@Override
		public List<@NonNull Bucket<X>> getItemIds() {
			return getItemIdStream().collect(toList());
		}

		protected Stream<? extends @NonNull Bucket<X>> getItemIdStream() {
			return rowBucket.stream().filter(visibleItemIds::contains);
		}

		@Override
		public @Nullable Property<?> getContainerProperty(final Object itemId, final Object propertyId) {
			return getItem(itemId).getItemProperty(propertyId);
		}

		private final Class<?> pivotCellReferenceClazz = PivotCellReference.class;

		@SuppressWarnings("unchecked")
		@Override
		public Class<PivotCellReference<?, Item>> getType(final Object propertyId) {
			return (Class<PivotCellReference<?, Item>>) pivotCellReferenceClazz;
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
			return getItemIds().indexOf(itemId);
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
		public Collection<@NonNull Bucket<X>> rootItemIds() {
			assert isRoot(rowBucket);
			return Collections.singleton(rowBucket);
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
			return itemId == rowBucket;
		}

		@Override
		public boolean hasChildren(final Object itemId) {
			return ((Bucket<?>) itemId).getChildren() != null;
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
			cache.clear();
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
			cache.clear();
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

	private void updateGridHeader(final Grid grid, final Bucket<?> colBucket, final int depth) {
		assert colBucket != null;
		for (int i = grid.getHeaderRowCount() - 1; i >= 0; i--) {
			grid.removeHeaderRow(i);
		}
		grid.setDefaultHeaderRow(null);
		for (int i = 0; i <= depth; i++) {
			grid.appendHeaderRow();
		}

		grid.setCellDescriptionGenerator(null);
		grid.setRowDescriptionGenerator(null);
		grid.addStyleName("pivot");

		doHeader(grid, colBucket, 0);
	}

	protected void doHeader(final Grid g, final Bucket<?> b, final int depth) {
		if (g.getColumns().isEmpty())
			return;

		assert g.getColumn(b) != null : g.getColumns();

		final HeaderRow headerRow = g.getHeaderRow(depth);
		assert headerRow != null;

		final Object @NonNull [] children = b.stream().toArray();
		final HeaderCell meAndMyChildren;
		if (children.length > 1) {
			meAndMyChildren = headerRow.join(children);
			@Nullable
			final List<? extends Bucket<?>> children$ = b.getChildren();
			if (children$ != null) {
				final int childDepth = depth + 1;
				children$.forEach(c -> doHeader(g, c, childDepth));
				final HeaderRow childRow = g.getHeaderRow(childDepth);
				// childRow.getCell(b).setText(SUM_TEXT);
				final HeaderCell ownCellInChildRow = childRow.getCell(b);
				ownCellInChildRow.setComponent(
						createChildCollapseButton(g, b.stream().filter(b0 -> b0 != b).collect(toList()), SUM_TEXT));

				ownCellInChildRow.setStyleName("depth-" + depth);
			}
		} else {
			meAndMyChildren = headerRow.getCell(b);
		}
		meAndMyChildren.setText(String.valueOf(b.bucketValue));
		meAndMyChildren.setStyleName("depth-" + depth);
		for (int i = depth + 1; i < g.getHeaderRowCount(); i++) {
			g.getHeaderRow(i).getCell(b).setStyleName("depth-" + depth);
		}
	}

	protected Button createChildCollapseButton(final Grid g, final List<? extends Bucket<?>> children,
			final String caption) {
		final Button collapserButton = new Button(caption, FontAwesome.CARET_DOWN);
		collapserButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		collapserButton.addStyleName("pivot-grid-expander");
		final Collection<Column> childColumns = children.stream().map(g::getColumn).collect(toList());
		assert childColumns != null;
		collapserButton.addClickListener(new ClickListener() {
			private boolean collapsed;

			@Override
			public void buttonClick(final ClickEvent event) {
				collapsed = !collapsed;
				final Button button = event.getButton();
				button.setIcon(collapsed ? FontAwesome.CARET_RIGHT : FontAwesome.CARET_DOWN);
				if (collapsed)
					button.setCaption(caption + ":" + String.valueOf(childColumns.size()));
				else
					button.setCaption(caption);
				childColumns.forEach(c -> c.setHidden(collapsed));
			}
		});
		return collapserButton;
	}
}