package com.snap252.vaadin.pivot.client;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public enum WhatToRender {
	avg("Durchschnitt"), max("Maximum"), min("Minimum"), sum("Summe"), cnt("Anzahl");

	private final String s;

	private WhatToRender(final String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return s;
	}
}