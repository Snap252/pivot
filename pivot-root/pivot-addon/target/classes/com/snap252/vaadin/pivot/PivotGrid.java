package com.snap252.vaadin.pivot;

import java.math.BigDecimal;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.treegrid.TreeGrid;

import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.pivoting.BiBucketParameter;

// This is the server-side UI component that provides public API 
// for PivotGrid
public class PivotGrid extends TreeGrid {

	public PivotGrid() {
		// addColumn("c1");
		// addColumn("c2");
		// HeaderRow headerRow = addHeaderRowAt(0);
		// headerRow.join("c1", "c2");
		// headerRow.getCell("c1").setText("t1");
	}

	public <T> void setContainerDataSource(BiBucketParameter<T> bucketParams,
			Collector<T, MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> c) {
		new BiBucketExtension<>(bucketParams).createGridWriter(c).writeGrid(this, x  -> x == null ? null :  x.sum);
	}
}
