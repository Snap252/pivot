package com.snap252.org.pivoting;

public enum ShowingSubtotal {
	AFTER, BEFORE, INHERIT, DONT_SHOW, BOTH;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Messages.getString(getClass().getSimpleName() + "." + name());
	}
}
