package com.snap252.vaadin.pivot.client;

public final class ClientNS {

	public String sum;
	public String max;
	public String cnt;
	public String min;
	public String avg;
	
	public ClientNS() {
	}

	public ClientNS(final String sum, final String max, final String min, final String avg, final String cnt) {
		this.sum = sum;
		this.max = max;
		this.min = min;
		this.avg = avg;
		this.cnt = cnt;
	}


}