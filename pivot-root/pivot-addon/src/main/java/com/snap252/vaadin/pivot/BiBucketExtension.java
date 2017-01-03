package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.BiBucket;
import com.snap252.org.pivoting.BiBucketParameter;
import com.snap252.org.pivoting.Bucket;
import com.snap252.vaadin.pivot.BiBucketExtension.BucketContainer.BucketItem.CellProperty;
import com.vaadin.data.Container.Hierarchical;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.renderers.NumberRenderer;

@NonNullByDefault
final class BiBucketExtension<@Nullable RAW> extends BiBucket<RAW> {
	BiBucketExtension(BiBucketParameter<RAW> p) {
		super(p);
	}

	public <AGG, REN> GridWriter<AGG, REN> createGridWriter(final Collector<RAW, AGG, REN> aggregator) {
		return new GridWriter<>(aggregator);
	}

	@NonNullByDefault({})
	class BucketContainer<R, W> implements Indexed, Hierarchical {
		private final Collector<RAW, W, R> collector;
		private Class<? extends R> cellClass;

		public BucketContainer(Collector<RAW, W, R> collector, Class<? extends R> cellClass) {
			this.collector = collector;
			this.cellClass = cellClass;
		}

		@Override
		public Object nextItemId(Object itemId) {
			assert false;
			return null;
		}

		@Override
		public Object prevItemId(Object itemId) {
			assert false;
			return null;
		}

		@Override
		public Object firstItemId() {
			assert false;
			return null;
		}

		@Override
		public Object lastItemId() {
			assert false;
			return null;
		}

		@Override
		public boolean isFirstId(Object itemId) {
			assert false;
			return false;
		}

		@Override
		public boolean isLastId(Object itemId) {
			assert false;
			return false;
		}

		@Override
		public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
			assert false;
			return null;
		}

		@Override
		public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
			assert false;
			return null;
		}

		private final Map<Object, BucketItem> cache = new HashMap<>();
		private final Object colProp = new Object();

		@SuppressWarnings("unchecked")
		@Override
		public Item getItem(Object itemId) {
			return cache.computeIfAbsent(itemId, x -> new BucketItem((Bucket<RAW>) x));
		}

		class BucketItem implements Item {
			class CellProperty implements Property<R> {

				private final Bucket<RAW> c;
				private final R v;

				public CellProperty(Bucket<RAW> colBucket) {
					c = colBucket;
					assert rowBucket != c;
					R ret = rowBucket.filterOwnValues(c).collect(collector);
					this.v = ret;

				}

				@Override
				public R getValue() {
					return v;
				}

				@Override
				public void setValue(Object newValue) throws ReadOnlyException {
					throw new ReadOnlyException();
				}

				@Override
				public Class<? extends R> getType() {
					return cellClass;
				}

				@Override
				public boolean isReadOnly() {
					return true;
				}

				@Override
				public void setReadOnly(boolean newStatus) {
				}
			}

			private final Bucket<RAW> rowBucket;

			public BucketItem(Bucket<RAW> itemId) {
				this.rowBucket = itemId;
			}

			private final Map<Object, CellProperty> cache = new HashMap<>();

			@SuppressWarnings("unchecked")
			@Override
			public Property<?> getItemProperty(Object id) {
				if (id == colProp) {
					return new ObjectProperty<>(rowBucket.getBucketValue());
				}
				return cache.computeIfAbsent(id, x -> new CellProperty((Bucket<RAW>) x));
			}

			@Override
			public Collection<?> getItemPropertyIds() {
				assert false;
				return null;
			}

			@Override
			public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}

		}

		@Override
		public Collection<?> getContainerPropertyIds() {
			List<Object> collect = colBucket.stream().collect(toList());
			collect.add(0, colProp);
			return collect;
		}

		@Override
		public Collection<?> getItemIds() {
			return rowBucket.stream().collect(toList());
		}

		@Override
		public Property getContainerProperty(Object itemId, Object propertyId) {
			assert false;
			return null;
		}

		@Override
		public Class<?> getType(Object propertyId) {
			return cellClass;
		}

		@Override
		public int size() {
			return rowBucket.stream().collect(counting()).intValue();
			// assert false;
			// return 0;
		}

		@Override
		public boolean containsId(Object itemId) {
			return true;
			// assert false;
			// return false;
		}

		@Override
		public Item addItem(Object itemId) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object addItem() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeItem(Object itemId) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
				throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAllItems() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public int indexOfId(Object itemId) {
			assert false;
			return 0;
		}

		@Override
		public Object getIdByIndex(int index) {
			assert false;
			return null;
		}

		@Override
		public List<?> getItemIds(int startIndex, int numberOfItems) {
			return rowBucket.stream().skip(startIndex).limit(numberOfItems).collect(toList());
			// assert false;
			// return null;
		}

		@Override
		public Object addItemAt(int index) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<?> getChildren(Object itemId) {
			Bucket<?> c = (Bucket<?>) itemId;
			return c.getChildren();
		}

		@Override
		public Object getParent(Object itemId) {
			Bucket<?> r = (Bucket<?>) itemId;
			return r.parent;
		}

		@Override
		public Collection<?> rootItemIds() {
			assert isRoot(rowBucket);
			return Collections.singleton(rowBucket);
		}

		@Override
		public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean areChildrenAllowed(Object itemId) {
			assert false;
			return false;
		}

		@Override
		public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
				throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isRoot(Object itemId) {
			return itemId == rowBucket;
		}

		@Override
		public boolean hasChildren(Object itemId) {
			return ((Bucket<?>) itemId).getChildren() != null;
		}
	}

	public final class GridWriter<W, R> {

		private Collector<RAW, W, R> collector;

		private GridWriter(final Collector<RAW, W, R> collector) {
			this.collector = collector;
		}

		public void writeGrid(final Grid g, Converter<@Nullable Number, @Nullable ? super R> c0) {
			g.removeAllColumns();

			for (int i = g.getHeaderRowCount() - 1; i >= 0; i--) {
				g.removeHeaderRow(i);
			}

			BiBucketExtension<@Nullable RAW>.BucketContainer<R, W> bc = new BucketContainer<>(collector,
					(Class<R>) c0.getModelType());
			g.setContainerDataSource(bc);
			doHeader(g, colBucket, 0);
			
			g.setCellDescriptionGenerator(cell -> {
				if (cell.getPropertyId() == bc.colProp) {
					return null;
				}

				@SuppressWarnings({ "unchecked", "rawtypes" })
				BucketContainer<R, W>.BucketItem.CellProperty cellProperty = (CellProperty) cell.getProperty();
				R rawValue = cellProperty.getValue();
				if (rawValue == null)
					return null;
				return rawValue.toString().replace("; ", "<br/>").replace("[", "<br/>").replace("]", "<br/>");
			});

			for (Column c : g.getColumns()) {
				if (c.getPropertyId() == bc.colProp) {
					continue;
				}
				NumberRenderer renderer = new NumberRenderer(NumberFormat.getNumberInstance());
				c.setRenderer(renderer, c0);
				// if (c.getPropertyId() instanceof LeafBucket) {
				// c.setHidden(true);
				// c.setHidable(true);
				// }
			}
			g.setFrozenColumnCount(2);
		}

		protected void doHeader(Grid g, Bucket<?> b, int depth) {
			List<? extends Bucket<?>> children = b.getChildren();
			if (children != null) {
				if (depth >= g.getHeaderRowCount()) {
					g.appendHeaderRow();
					assert depth < g.getHeaderRowCount();
				}
				// HeaderRow headerRow = g.appendHeaderRow();
				HeaderRow headerRow = g.getHeaderRow(depth);

				Map<Bucket<?>, Object[]> l = new LinkedHashMap<>();
				for (Bucket<?> x : children) {
					Object @NonNull [] array = x.stream().toArray();
					l.put(x, array);
				}

				l.entrySet().forEach(oa -> {
					HeaderCell join;
					if (oa.getValue().length > 1) {
						join = headerRow.join(oa.getValue());
					} else {
						join = headerRow.getCell(oa.getValue()[0]);
						assert join != null;
					}
					join.setText(String.valueOf(oa.getKey().bucketValue));
				});
				for (Bucket<?> child : children) {
					doHeader(g, child, depth + 1);
				}
			}
		}
	}

}