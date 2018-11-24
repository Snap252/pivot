package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

public enum WhatOfObjectStatisticsToShow {
	COUNT() {
		@Override
		public int getValue(final ObjectStatistics n) {
			return n.numberOfValues;
		}
	},
	COUNT_NON_NULL() {
		@Override
		public int getValue(final ObjectStatistics n) {
			return n.numberOfNonNullValues;
		}
	},
	COUNT_DISTINCT() {
		@Override
		public int getValue(final ObjectStatistics n) {
			return n.getDistinct();
		}
	};


	public abstract int getValue(ObjectStatistics n);

	public BigDecimal getValueAsBigDecimal(final ObjectStatistics n) {
		return BigDecimal.valueOf(getValue(n));
	}

}