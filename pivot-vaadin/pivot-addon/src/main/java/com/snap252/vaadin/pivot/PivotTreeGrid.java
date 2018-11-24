package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.vaadin.treegrid.TreeGrid;

import com.vaadin.shared.ui.grid.ColumnResizeMode;

@NonNullByDefault
public class PivotTreeGrid extends TreeGrid implements PivotGridIfc {

	public PivotTreeGrid(final GridRendererParameter<?, ?> bucketParams) {
		setPrimaryStyleName("v-grid-tiny");

		setSelectionMode(SelectionMode.NONE);
		setColumnResizeMode(ColumnResizeMode.SIMPLE);
		new GridRenderer(bucketParams, this);
		setFrozenColumnCount(1);
	}

	// public @Nullable <@Nullable T> GridWriter<?, ?>
	// setContainerDataSource(final BiBucketParameter<Item> bucketParams,
	// @Nullable final GridWriter<?, ?> oldGridWriter) {
	// try {
	// setComponentError(null);
	// final GridRenderer gridRenderer = new GridRenderer(bucketParams);
	// final GridRenderer.GridWriter<Object, Object> gridWriter =
	// gridRenderer.createGridWriter();
	// gridWriter.writeGrid(this);
	// if (oldGridWriter != null) {
	// gridWriter.setModelAggregator(oldGridWriter.getModelAggregator());
	// gridWriter.updateRenderer(this);
	// }
	// return gridWriter;
	// } catch (final IllegalArgumentException e) {
	// removeAllColumns();
	// setContainerDataSource(new IndexedContainer());
	// setComponentError(new UserError(e.getMessage()));
	// return oldGridWriter;
	// }
	// }
}
