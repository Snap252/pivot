package com.snap252.vaadin.pivot.client;

import com.vaadin.shared.communication.SharedState;

public class ClientRendererSharedState extends SharedState {
	public WhatToRender toRender = WhatToRender.avg;
	public String numberFormat = "000000.000000";
}
