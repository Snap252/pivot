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

@NonNullByDefault
public class PivotGrid extends TreeGrid {

	@SuppressWarnings("null")
	public <@Nullable T> void setContainerDataSource(BiBucketParameter<T> bucketParams,
			Collector<T, MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> c) {
		new BiBucketExtension<>(bucketParams).createGridWriter(c).writeGrid(this, NumberStatistics.class,
				column -> column.setRenderer(new StatisticsRenderer("---", this)));
	}
}
