package com.snap252.org.aggregators;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalArithmetics implements Arithmetics<BigDecimal> {

	private final MathContext mc;

	public BigDecimalArithmetics() {
		this(new MathContext(20));
	}

	BigDecimalArithmetics(final MathContext mc) {
		this.mc = mc;
	}

	@Override
	public BigDecimal add(final BigDecimal n1, final BigDecimal n2) {
		return n1.add(n2);
	}

	@Override
	public BigDecimal mul(final BigDecimal n1, final BigDecimal n2) {
		return n1.multiply(n2);
	}

	@Override
	public BigDecimal part(final BigDecimal n1, final int n2) throws ArithmeticException {
		return n1.divide(new BigDecimal(n2), mc);
	}

	@Override
	public BigDecimal negate(final BigDecimal n) {
		return n.negate();
	}

	@Override
	public BigDecimal sub(final BigDecimal n1, final BigDecimal n2) {
		return n1.subtract(n2);
	}

	@Override
	public int compare(final BigDecimal n1, final BigDecimal n2) {
		return n1.compareTo(n2);
	}

	@Override
	public BigDecimal getNeutralAddElement() {
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal multi(final BigDecimal n1, final int n2) {
		return n1.multiply(new BigDecimal(n2));
	}

}
