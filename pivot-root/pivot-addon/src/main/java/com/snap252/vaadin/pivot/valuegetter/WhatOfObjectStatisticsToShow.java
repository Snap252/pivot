package com.snap252.vaadin.pivot.valuegetter;

import java.math.BigDecimal;

public enum WhatOfObjectStatisticsToShow {
	COUNT("Anzahl Datensätze") {
		@Override
		public int getValue(final ObjectStatistics n) {
			return n.numberOfValues;
		}
	},
	COUNT_NON_NULL("Anzahl Werte") {
		@Override
		public int getValue(final ObjectStatistics n) {
			return n.numberOfNonNullValues;
		}
	},
	COUNT_DISTINCT("Anzahl unterschiedlicher Werte") {
		@Override
		public int getValue(final ObjectStatistics n) {
			return n.getDistinct();
		}
	};

	private final String s;

	private WhatOfObjectStatisticsToShow(final String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return s;
	}

	public abstract int getValue(ObjectStatistics n);

	public BigDecimal getValueAsBigDecimal(final ObjectStatistics n) {
		return BigDecimal.valueOf(getValue(n));
	}

}