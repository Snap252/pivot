package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.BiBucketParameter;
import com.snap252.org.pivoting.Bucket;
import com.snap252.org.pivoting.RootBucket;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtor;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtorDelegate;
import com.vaadin.data.Container.Hierarchical;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

@NonNullByDefault
final class GridRenderer {
	private final RootBucket<Item> rowBucket;
	private final RootBucket<Item> colBucket;

	GridRenderer(final BiBucketParameter<Item> p) throws IllegalArgumentException {
		rowBucket = new RootBucket<>("row", p.values, p.rowFnkt);
		colBucket = new RootBucket<>("col", p.values, p.colFnkt);

		if (rowBucket.getSize(1) > 10000)
			throw new IllegalArgumentException("too many rows: " + rowBucket.getSize(1));
		if (colBucket.getSize(1) > 100)
			throw new IllegalArgumentException("too many columns: " + colBucket.getSize(1));
	}

	public <AGG, REN> GridWriter<AGG, REN> createGridWriter() {
		return new GridWriter<>();
	}

	public final class GridWriter<W, R> {

		private final ModelAggregtorDelegate aggregatorDelegator = new ModelAggregtorDelegate();

		public void setModelAggregator(final ModelAggregtor<?> modelAggregator) {
			this.aggregatorDelegator.setDelegate(modelAggregator);
			fireValueChange();
		}

		public ModelAggregtor<?> getModelAggregator() {
			return this.aggregatorDelegator.getDelegate();
		}

		public void updateRenderer(final Grid g) {

			g.getColumns().stream().filter(c -> c.getPropertyId() != colProp).forEach(col -> {
				aggregatorDelegator.createRendererConverter().setToColumn(col);
			});
			/*
			 * hack for https://vaadin.com/forum#!/thread/9319379
			 */
			// g.setCellStyleGenerator(g.getCellStyleGenerator());
		}

		private final Object colProp = new Object() {
			@Override
			public String toString() {
				return "";
			}
		};

		private final Collection<ValueChangeListener> valueChangeListeners = new HashSet<>();
		private final Collection<Runnable> valueResetter = new HashSet<>();

		private void fireValueChange() {
			valueResetter.forEach(Runnable::run);

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

		class BucketContainer implements Indexed, Hierarchical {

			public BucketContainer() {
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

			private final Map<Object, BucketItem> cache = new HashMap<>();

			@SuppressWarnings({ "unchecked" })
			@Override
			public BucketItem getItem(final Object itemId) {
				return cache.computeIfAbsent(itemId, x -> new BucketItem((Bucket<Item>) x));
			}

			class BucketItem implements Item {
				class CellProperty implements Property<PivotCellReference<?>>, Property.ValueChangeNotifier {

					private final Bucket<Item> colBucket;
					@Nullable
					private PivotCellReference<?> v;
					private final List<Item> filterOwnValues;
					private final Class<PivotCellReference<?>> clazz;

					public CellProperty(final Bucket<Item> colBucket) {
						this.colBucket = colBucket;
						assert rowBucket != colBucket;
						valueResetter.add(() -> v = null);
						filterOwnValues = rowBucket.filterOwnValues(colBucket).collect(toList());
						this.clazz = PivotCellReference.cast(PivotCellReference.class);
					}

					@Override
					public PivotCellReference<?> getValue() {
						if (v != null)
							return v;
						else {
							final Collector<Item, ?, ?> aggregator = aggregatorDelegator.getAggregator();
							final Object newValue0 = filterOwnValues.stream().collect(aggregator);
							final PivotCellReference<@Nullable ?> newValue = new PivotCellReference<@Nullable Object>(
									newValue0, rowBucket, colBucket, BucketContainer.this);
							v = newValue;
							return newValue;
						}
					}

					@Override
					public void setValue(@Nullable final PivotCellReference<?> newValue) throws ReadOnlyException {
						throw new ReadOnlyException();
					}

					@Override
					public Class<PivotCellReference<?>> getType() {
						return clazz;
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

				private final Bucket<Item> rowBucket;

				public BucketItem(final Bucket<Item> itemId) {
					this.rowBucket = itemId;
				}

				private final Map<@NonNull Object, @NonNull CellProperty> cache = new HashMap<>();

				@SuppressWarnings({ "unchecked" })
				@Override
				public @NonNull Property<?> getItemProperty(final Object id) {
					if (id == colProp) {
						return new ObjectProperty<>(rowBucket.getBucketValue());
					}
					return cache.computeIfAbsent(id, x -> new CellProperty((Bucket<Item>) x));
				}

				@SuppressWarnings("null")
				@Override
				public Collection<?> getItemPropertyIds() {
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
			public Collection<?> getContainerPropertyIds() {
				final List<Object> collect = colBucket.stream().collect(toList());
				collect.add(0, colProp);
				return collect;
			}

			@Override
			public Collection<?> getItemIds() {
				return rowBucket.stream().collect(toList());
			}

			@Override
			public @Nullable Property<?> getContainerProperty(final Object itemId, final Object propertyId) {
				return getItem(itemId).getItemProperty(propertyId);
			}

			private final Class<?> pivotCellReferenceClazz = PivotCellReference.class;

			@Override
			public Class<?> getType(final Object propertyId) {
				return pivotCellReferenceClazz;
			}

			@Override
			public int size() {
				return rowBucket.stream().collect(counting()).intValue();
				// assert false;
				// return 0;
			}

			@Override
			public boolean containsId(final Object itemId) {
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
				assert false;
				return 0;
			}

			@SuppressWarnings("null")
			@Override
			public Object getIdByIndex(final int index) {
				assert false;
				return null;
			}

			@Override
			public List<?> getItemIds(final int startIndex, final int numberOfItems) {
				return rowBucket.stream().skip(startIndex).limit(numberOfItems).collect(toList());
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
			public Collection<?> rootItemIds() {
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
		}

		private static final String SUM_TEXT = "\u2211";

		public void writeGrid(final Grid g) {
			// g.setFrozenColumnCount(0);
			for (int i = 1; i < g.getHeaderRowCount(); i++) {
				if (g.getDefaultHeaderRow() != g.getHeaderRow(i))
					g.removeHeaderRow(i);
			}
			// g.setDefaultHeaderRow(null);

			g.setCellDescriptionGenerator(null);
			g.setRowDescriptionGenerator(null);
			g.addStyleName("pivot");

			g.removeAllColumns();

			final BucketContainer bc = new BucketContainer();
			g.setContainerDataSource(bc);
			doHeader(g, colBucket, 0);

			g.setCellStyleGenerator(cell -> {
				if (cell.getPropertyId() == colProp) {
					return "row-header";
				}
				return null;
			});

			// TODO:
			// g.setCellDescriptionGenerator(cell -> {
			// if (cell.getPropertyId() == bc.colProp) {
			// return null;
			// }
			//
			// @SuppressWarnings({ "unchecked", "rawtypes" })
			// final BucketContainer<R, W>.BucketItem.CellProperty cellProperty
			// = (CellProperty) cell.getProperty();
			// final R rawValue = cellProperty.getValue();
			// if (rawValue == null)
			// return null;
			// return rawValue.toString().replace("; ",
			// "<br/>").replace("NumberStatistics [", "").replace("]",
			// "<br/>");
			// });

			// g.getColumns().stream().filter(c -> c.getPropertyId() !=
			// bc.colProp).forEach(columnHandler);
		}

		protected void doHeader(final Grid g, final Bucket<?> b, final int depth) {
			if (depth >= g.getHeaderRowCount()) {
				g.appendHeaderRow();
				assert depth < g.getHeaderRowCount();
			}
			final HeaderRow headerRow = g.getHeaderRow(depth);

			final List<? extends Bucket<?>> children = b.getChildren();
			if (children != null) {

				final Map<Bucket<?>, Object[]> l = new LinkedHashMap<>();
				for (final Bucket<?> x : children) {
					final Object @NonNull [] array = x.stream().toArray();
					l.put(x, array);
				}

				l.entrySet().forEach(oa -> {
					final HeaderCell join;
					if (oa.getValue().length > 1) {
						join = headerRow.join(oa.getValue());
						final Button collapser = new Button(String.valueOf(oa.getKey().bucketValue),
								FontAwesome.CARET_DOWN);
						collapser.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						collapser.addStyleName("pivot-grid-expander");
						collapser.addClickListener(new ClickListener() {
							private boolean collapsed;

							@Override
							public void buttonClick(final ClickEvent event) {
								collapsed = !collapsed;
								event.getButton().setIcon(collapsed ? FontAwesome.CARET_RIGHT : FontAwesome.CARET_DOWN);
								Stream.of(oa.getValue()).skip(1).map(g::getColumn).forEach(c -> c.setHidden(collapsed));
							}
						});
						// join.setText("text");
						join.setComponent(collapser);
					} else {
						join = headerRow.getCell(oa.getValue()[0]);
						assert join != null;
						join.setText(String.valueOf(oa.getKey().bucketValue));
					}
				});
				headerRow.getCell(b).setText(SUM_TEXT);
				for (final Bucket<?> child : children) {
					doHeader(g, child, depth + 1);
				}
			} else if (depth == 0) {
				headerRow.getCell(b).setText(SUM_TEXT);
			}
		}

	}
}