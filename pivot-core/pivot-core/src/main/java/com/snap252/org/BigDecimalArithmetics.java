package com.snap252.org;

import java.math.BigDecimal;
import java.math.MathContext;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class BigDecimalArithmetics implements Arithmetics<BigDecimal> {

	private MathContext mc;

	BigDecimalArithmetics() {
		this(new MathContext(20));
	}

	BigDecimalArithmetics(MathContext mc) {
		this.mc = mc;
	}

	@Override
	public BigDecimal add(BigDecimal n1, BigDecimal n2) {
		return n1.add(n2);
	}

	@Override
	public BigDecimal mul(BigDecimal n1, BigDecimal n2) {
		return n1.multiply(n2);
	}

	@Override
	public BigDecimal part(BigDecimal n1, int n2) throws ArithmeticException {
		return n1.divide(new BigDecimal(n2), mc);
	}

	@Override
	public BigDecimal negate(BigDecimal n) {
		return n.negate();
	}

	@Override
	public int compare(BigDecimal n1, BigDecimal n2) {
		return n1.compareTo(n2);
	}

}
