package com.snap252.vaadin.pivot.client;

import com.vaadin.shared.communication.SharedState;

public class ClientRendererSharedState extends SharedState {
	public String numberFormat = "0.00###";
	public String nullRepresentation = null;
	public int depth = -1;
	public Gradient gradient;
}
