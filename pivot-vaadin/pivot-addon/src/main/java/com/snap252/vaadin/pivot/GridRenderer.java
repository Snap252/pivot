package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.Bucket;
import com.snap252.vaadin.pivot.GridRendererParameter.GridRendererChangeParameterKind;
import com.snap252.vaadin.pivot.xml.renderers.Aggregator;
import com.vaadin.data.Container.PropertySetChangeListener;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.UserError;
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

	<X> GridRenderer(final GridRendererParameter<X, ?> gridParameter, final Grid grid) throws IllegalArgumentException {
		final BucketContainer<X> bucketContainer = new BucketContainer<>(gridParameter);

		gridParameter.addParameterChangeListener(GridRendererChangeParameterKind.AGGREGATOR,
				_ignore -> updateGridColumns(grid, _ignore.gridParameter.getAggregator()));

		// work around for : https://github.com/vaadin/framework/issues/8638
		final PropertySetChangeListener columnsChanged0 = _ignore -> cleanupGridHeader(grid);
		bucketContainer.addPropertySetChangeListener(columnsChanged0);
		bucketContainer.addPropertySetChangeListener(pcl -> {
			try {
				pcl.getContainer().getContainerPropertyIds();
				grid.setEnabled(true);
				grid.setComponentError(null);
			} catch (final IllegalArgumentException e) {
				grid.setEnabled(false);
				grid.setComponentError(new UserError(e.getMessage(), ContentMode.TEXT, ErrorLevel.INFORMATION));
			}
		});
		grid.setContainerDataSource(bucketContainer);

		final PropertySetChangeListener columnsChanged = _ignore -> {
			updateGridColumns(grid, gridParameter.getAggregator());
			grid.setColumnOrder(grid.getContainerDataSource().getContainerPropertyIds().toArray());
			updateGridHeader(grid, bucketContainer.getColBucket(), gridParameter.getColDepth());
		};
		bucketContainer.addPropertySetChangeListener(columnsChanged);
	}

	protected void updateGridColumns(final Grid grid, final Aggregator<?, ?> modelAggregator) {
		grid.getColumns().forEach(column -> {
			final Object columnPropertyId = column.getPropertyId();
			if (columnPropertyId != COLLAPSE_COL_PROPERTY_ID) {
				final Bucket<?> colBucket = (Bucket<?>) columnPropertyId;
				modelAggregator.updateRendererAndConverter(column, colBucket.getLevel());
				column.setMinimumWidth(75);
			} else {
				column.setRenderer(new org.vaadin.treegrid.HierarchyRenderer("-"));
				column.setMinimumWidth(170);
			}
		});
	}

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

		grid.setRowStyleGenerator(rowref -> {
			final Bucket<?> itemId = (Bucket<?>) rowref.getItemId();
			return "row-depth-" + itemId.getLevel();
		});

		doHeader(grid, colBucket, 0);
		final HeaderCell cell = grid.getDefaultHeaderRow().getCell(COLLAPSE_COL_PROPERTY_ID);
		final Button resizeButton = new Button(FontAwesome.EXPAND);
		resizeButton.setDescription("Maximieren");
		resizeButton.addStyleName(ValoTheme.BUTTON_QUIET);
		resizeButton.addStyleName(ValoTheme.BUTTON_SMALL);
		resizeButton.addClickListener(new ClickListener() {
			private boolean collapsed;

			@Override
			public void buttonClick(final ClickEvent evt) {
				final PivotUI parent = (PivotUI) grid.getParent();
				collapsed = !collapsed;
				parent.setMaximized(collapsed);
				resizeButton.setIcon(collapsed ? FontAwesome.COMPRESS : FontAwesome.EXPAND);
				resizeButton.setDescription(collapsed ? "Minimmieren" : "Maximieren");
			}
		});
		cell.setComponent(resizeButton);
		// TODO: better not to add it
		if (depth != 0)
			grid.removeHeaderRow(0);
	}

	private void cleanupGridHeader(final Grid grid) {
		for (int i = grid.getHeaderRowCount() - 1; i >= 0; i--) {
			grid.removeHeaderRow(i);
		}
	}

	protected void doHeader(final Grid g, final Bucket<?> b, final int depth) {

		final Object @NonNull [] children = b.stream().toArray();

		if (depth != 0) {
			final HeaderRow headerRow = g.getHeaderRow(depth);
			assert headerRow != null;
			final HeaderCell meAndMyChildren;
			if (children.length > 1) {
				meAndMyChildren = headerRow.join(children);

			} else
				meAndMyChildren = headerRow.getCell(b);

			if (meAndMyChildren != null) {
				meAndMyChildren.setText(b.getFormattedBucketValue());
				meAndMyChildren.setStyleName("column-depth-" + depth);
			}
		}

		if (children.length > 1) {
			@Nullable
			final List<? extends Bucket<?>> children$ = b.getChildren();
			if (children$ != null) {
				final int childDepth = depth + 1;
				children$.forEach(c -> doHeader(g, c, childDepth));
				final HeaderRow childRow = g.getHeaderRow(childDepth);

				final HeaderCell cell = childRow.getCell(b);
				if (cell != null) {
					if (depth == 0) {
						cell.setText(SUM_TEXT);
					} else
						cell.setComponent(createChildCollapseButton(g,
								b.stream().filter(b0 -> b0 != b).collect(toList()), SUM_TEXT));

					final HeaderCell cell0 = childRow.getCell(b.getWrappedIfThere());
					if (cell0 != null)
						cell0.setText(SUM_TEXT);
				}
			}
		}

		for (int i = depth + 1; i < g.getHeaderRowCount(); i++) {
			final HeaderCell cell = g.getHeaderRow(i).getCell(b);
			if (cell == null) {
				break;
			}
			cell.setStyleName("column-depth-" + depth);

			final HeaderCell cell0 = g.getHeaderRow(i).getCell(b.getWrappedIfThere());
			if (cell0 != null)
				cell0.setStyleName("column-depth-" + depth);
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