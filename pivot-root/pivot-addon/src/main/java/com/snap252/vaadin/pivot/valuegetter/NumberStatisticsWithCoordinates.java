package com.snap252.vaadin.pivot.valuegetter;

import com.snap252.org.aggregators.Arithmetics;
import com.snap252.org.aggregators.NumberStatistics;

public class NumberStatisticsWithCoordinates<N extends Number> extends NumberStatistics<N> {

	public NumberStatisticsWithCoordinates(final int cnt, final N max, final N min, final N sum, final N sumSqr, final Arithmetics<N> arithmetics) {
		super(cnt, max, min, sum, sumSqr, arithmetics);
	}

}
