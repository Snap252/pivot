package com.snap252.org.aggregators;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault({})
public final class MutableValue<N extends Number> {
	@NonNull
	private final Arithmetics<N> arithmetics;
	private N sum;
	private N sqrSum;

	private int cnt;

	@Nullable
	private N min;
	@Nullable
	private N max;

	@NonNullByDefault
	public MutableValue(final N n, final Arithmetics<N> arithmetics) {
		this.arithmetics = arithmetics;
		this.min = this.max = n;
		this.sum = n;
		this.sqrSum = arithmetics.sqr(n);
		cnt = 1;
	}

	/* neutral element */
	@NonNull
	static <N extends Number> MutableValue<N> getNeutralElement(@NonNull final Arithmetics<N> arithmetics) {
		return new MutableValue<N>(arithmetics);
	}

	private MutableValue(final @NonNull Arithmetics<N> arithmetics) {
		this.arithmetics = arithmetics;
		this.sum = sqrSum = arithmetics.getNeutralAddElement();
		this.min = this.max = null;
		cnt = 0;
	}

	MutableValue<N> addSingle(final N other) {
		this.sum = arithmetics.add(sum, other);
		this.sqrSum = arithmetics.add(sqrSum, arithmetics.sqr(other));

		min = min != null ? arithmetics.min(min, other) : other;
		max = max != null ? arithmetics.max(max, other) : other;

		cnt++;
		return this;
	}

	@NonNull
	MutableValue<N> merge(final MutableValue<N> other) {
		assert other.arithmetics == this.arithmetics;
		this.sum = arithmetics.add(sum, other.sum);
		this.sqrSum = arithmetics.add(sqrSum, other.sqrSum);
		this.cnt += other.cnt;

		doMin(other.min);
		doMax(other.max);
		return this;
	}

	@Override
	public String toString() {
		return "MutableValue [sum=" + sum + ", sqrSum=" + sqrSum + ", cnt=" + cnt + ", min=" + min + ", max=" + max
				+ ", arithmetics=" + arithmetics + "]";
	}

	private void doMax(@Nullable final N otherMax) {
		if (max == null) {
			max = otherMax;
		} else if (otherMax != null) {
			assert max != null;
			max = arithmetics.max(max, otherMax);
		}
	}

	private void doMin(@Nullable final N otherMin) {
		if (min == null) {
			min = otherMin;
		} else if (otherMin != null) {
			assert min != null;
			min = arithmetics.min(min, otherMin);
		}
	}

	public @Nullable NumberStatistics<N> createNumberStatistics() {
		if (cnt == 0) {
			return null;
		}
		// FIXME:
		// assert arithmetics.compare(arithmetics.sqr(sum), sqrSum) >= 0 :
		// arithmetics.sqr(sum) + " => " + sqrSum;

		@SuppressWarnings("null")
		final N max2 = max;
		@SuppressWarnings("null")
		final N min2 = min;
		return new NumberStatistics<N>(cnt, max2, min2, sum, sqrSum, arithmetics);
	}
}