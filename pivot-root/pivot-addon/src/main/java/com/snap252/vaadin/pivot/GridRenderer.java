package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.Bucket;
import com.snap252.vaadin.pivot.GridRendererParameter.GridRendererChangeParameterKind;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtor;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtor.RendererConverter;
import com.vaadin.data.Container.PropertySetChangeListener;
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

	static final String SUM_TEXT = "\u2211";

	<X> GridRenderer(final GridRendererParameter<X> gridParameter, final Grid grid) throws IllegalArgumentException {
		final BucketContainer<X> bucketContainer = new BucketContainer<>(gridParameter);

		gridParameter.addParameterChangeListener(GridRendererChangeParameterKind.AGGREGATOR,
				_ignore -> updateGridColumns(grid, _ignore.gridParameter.getModelAggregator()));

		//work around for : https://github.com/vaadin/framework/issues/8638
		final PropertySetChangeListener columnsChanged0 = _ignore -> {
			cleanupGridHeader(grid);
		};
		bucketContainer.addPropertySetChangeListener(columnsChanged0);
		grid.setContainerDataSource(bucketContainer);

		final PropertySetChangeListener columnsChanged = _ignore -> {
			updateGridColumns(grid, gridParameter.getModelAggregator());
			grid.setColumnOrder(grid.getContainerDataSource().getContainerPropertyIds().toArray());
			updateGridHeader(grid, bucketContainer.getColBucket(), gridParameter.getColDepth());
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
		// grid.setFrozenColumnCount(1);
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

	static final Object COLLAPSE_COL_PROPERTY_ID = new Object() {
		@Override
		public String toString() {
			return "";
		}
	};

	private void updateGridHeader(final Grid grid, final Bucket<?> colBucket, final int depth) {
		assert colBucket != null;
		cleanupGridHeader(grid);
		grid.setDefaultHeaderRow(null);

		for (int i = 0; i <= depth; i++) {
			final HeaderRow row = grid.appendHeaderRow();
			if (i == depth) {
				grid.setDefaultHeaderRow(row);
			}
		}

		grid.setCellDescriptionGenerator(null);
		grid.setRowDescriptionGenerator(null);

		doHeader(grid, colBucket, 0);
	}

	private void cleanupGridHeader(final Grid grid) {
		for (int i = grid.getHeaderRowCount() - 1; i >= 0; i--) {
			grid.removeHeaderRow(i);
		}
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
			}
		} else {
			meAndMyChildren = headerRow.getCell(b);
		}
		meAndMyChildren.setText(b.getFormattedBucketValue());
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