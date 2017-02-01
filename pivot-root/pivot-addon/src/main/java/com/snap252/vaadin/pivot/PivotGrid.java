package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.treegrid.TreeGrid;

import com.snap252.org.pivoting.BiBucketParameter;
import com.snap252.vaadin.pivot.GridRenderer.GridWriter;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.UserError;

@NonNullByDefault
public class PivotGrid extends TreeGrid {

	public PivotGrid() {
		setPrimaryStyleName("v-grid-tiny");
		setSelectionMode(SelectionMode.NONE);
	}

	public @Nullable <@Nullable T> GridWriter<?, ?> setContainerDataSource(final BiBucketParameter<Item> bucketParams,
			@Nullable final GridWriter<?, ?> oldGridWriter) {
		try {
			setComponentError(null);
			final GridRenderer gridRenderer = new GridRenderer(bucketParams);
			final GridRenderer.GridWriter<Object, Object> gridWriter = gridRenderer.createGridWriter();
			gridWriter.writeGrid(this);
			if (oldGridWriter != null) {
				gridWriter.setModelAggregator(oldGridWriter.getModelAggregator());
				gridWriter.updateRenderer(this);
			}
			return gridWriter;
		} catch (final IllegalArgumentException e) {
			removeAllColumns();
			setContainerDataSource(new IndexedContainer());
			setComponentError(new UserError(e.getMessage()));
			return oldGridWriter;
		}
	}
}
