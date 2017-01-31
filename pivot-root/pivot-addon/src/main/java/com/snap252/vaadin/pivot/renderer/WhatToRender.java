package com.snap252.vaadin.pivot.renderer;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.aggregators.NumberStatistics;

@NonNullByDefault
public enum WhatToRender {
	avg("Durchschnitt") {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.avg();
		}
	},
	max("Maximum") {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.max;
		}
	},
	min("Minimum") {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.min;
		}
	},
	sum("Summe") {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.sum;
		}
	},
	cnt("Anzahl Datensätze") {
		@Override
		public <N extends Number> N getValue(final NumberStatistics<N> n) {
			return n.toN(n.cnt);
		}
	};

	private final String s;

	private WhatToRender(final String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return s;
	}

	public abstract <N extends Number> N getValue(NumberStatistics<N> n);

}