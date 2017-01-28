package com.snap252.org.aggregators;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class NullableArithmeticsWrapper<@Nullable N extends Number> implements Arithmetics<N> {

	private final Arithmetics<@NonNull N> wrapped;

	public NullableArithmeticsWrapper(final Arithmetics<@NonNull N> wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public @Nullable N getNeutralAddElement() {
		return null;
	}

	@Override
	public N add(@Nullable final N n1, @Nullable final N n2) {
		if (n1 == null)
			return n2;
		if (n2 == null)
			return n1;
		return wrapped.add(n1, n2);
	}

	@Override
	public N mul(@Nullable final N n1, @Nullable final N n2) {
		if (n1 == null)
			return n2;
		if (n2 == null)
			return n1;
		return wrapped.mul(n1, n2);
	}

	@Override
	public N part(@Nullable final N n1, final int n2) throws ArithmeticException {
		return n1 == null ? null : wrapped.part(n1, n2);
	}

	@Override
	public N multi(@Nullable final N n1, final int n2) {
		return n1 == null ? null : wrapped.multi(n1, n2);
	}

	@Override
	public N negate(@Nullable final N n) {
		return n == null ? null : wrapped.negate(n);
	}

	@Override
	public int compare(@Nullable final N n1, @Nullable final N n2) {
		if (n1 == null && n2 == null)
			return 0;
		if (n1 == null)
			return 1;
		if (n2 == null)
			return -1;

		return wrapped.compare(n1, n2);
	}

}
