package com.snap252.vaadin.pivot;

import java.math.BigDecimal;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.treegrid.TreeGrid;

import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.pivoting.BiBucketParameter;
import com.snap252.vaadin.pivot.renderer.StatisticsRenderer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.UserError;

@NonNullByDefault
public class PivotGrid extends TreeGrid {
	public PivotGrid() {
		setPrimaryStyleName("v-grid-tiny");
	}

	@SuppressWarnings("null")
	public <@Nullable T> void setContainerDataSource(BiBucketParameter<T> bucketParams,
			Collector<T, MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> c) {
		try {
			setComponentError(null);
			new BiBucketExtension<>(bucketParams).createGridWriter(c).writeGrid(this, NumberStatistics.class,
					column -> column.setRenderer(new StatisticsRenderer("---", this)));
		} catch (IllegalArgumentException e) {
			removeAllColumns();
			setContainerDataSource(new IndexedContainer());
			setComponentError(new UserError(e.getMessage()));
		}
	}
}
