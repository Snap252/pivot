package com.snap252.vaadin.pivot.client;

import java.io.Serializable;
import java.math.BigDecimal;

public final class ClientBigDecimalNumberStatistics implements Serializable {

	public BigDecimal sum;
	public BigDecimal max;
	public BigDecimal min;
	public BigDecimal avg;
	public int cnt;
	
	public ClientBigDecimalNumberStatistics() {
	}

	public ClientBigDecimalNumberStatistics(final BigDecimal sum, final BigDecimal max, final BigDecimal min, final BigDecimal avg, final int cnt) {
		this.sum = sum;
		this.max = max;
		this.min = min;
		this.avg = avg;
		this.cnt = cnt;
	}


}