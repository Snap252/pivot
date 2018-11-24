package com.snap252.vaadin.pivot.valuegetter;

import com.snap252.org.aggregators.NumberStatistics;

public enum WhatOfNumberStatisticsToRender {
	AVG() {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.avg();
		}
	},
	MAX() {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.max;
		}
	},
	MIN() {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.min;
		}
	},
	SUM() {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.sum;
		}
	},
	VAR() {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.varianz();
		}
	},
	CNT() {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.toN(n.cnt);
		}
	};

	public abstract <N extends Number> N getValue(NumberStatistics<N> n);

}