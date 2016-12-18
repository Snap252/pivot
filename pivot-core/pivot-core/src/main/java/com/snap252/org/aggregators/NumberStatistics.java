package com.snap252.org.aggregators;

import java.text.MessageFormat;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.Nullable;

public final class NumberStatistics<N extends Number> {
	public final int cnt;
	public final N max;
	public final N min;
	public final N sum;
	public final N sumSqr;

	private final Arithmetics<N> arithmetics;

	public static <P, N extends Number> Collector<P, MutableValue<N>, @Nullable NumberStatistics<N>> getReducer(
			final Function<P, N> f, final Arithmetics<N> arithmetics) {

		final Supplier<MutableValue<N>> supplier = () -> MutableValue.getNeutralElement(arithmetics);
		final BiConsumer<MutableValue<N>, P> accumulator = (t, u) -> t.addSingle(f.apply(u));
		final BinaryOperator<MutableValue<N>> combiner = MutableValue::merge;
		final Function<MutableValue<N>, @Nullable NumberStatistics<N>> finisher = MutableValue::createNumberStatistics;

		return Collector.of(supplier, accumulator, combiner, finisher);
	}

	public static final class MutableValue<N extends Number> {
		private final Arithmetics<N> arithmetics;
		private N sum;
		private N sqrSum;

		private int cnt;

		@Nullable
		private N min;
		@Nullable
		private N max;

		public MutableValue(final N n, final Arithmetics<N> arithmetics) {
			this.arithmetics = arithmetics;
			this.min = this.max = n;
			this.sum = n;
			this.sqrSum = arithmetics.sqr(n);
			cnt = 1;
		}

		/* neutral element */
		private static <N extends Number> MutableValue<N> getNeutralElement(final Arithmetics<N> arithmetics) {
			return new MutableValue<N>(arithmetics);
		}

		private MutableValue(final Arithmetics<N> arithmetics) {
			this.arithmetics = arithmetics;
			this.sum = sqrSum = arithmetics.getNeutralAddElement();
			this.min = this.max = null;
			cnt = 0;
		}

		private MutableValue<N> addSingle(final N other) {
			this.sum = arithmetics.add(sum, other);
			this.sqrSum = arithmetics.add(sqrSum, arithmetics.sqr(other));

			min = min != null ? arithmetics.min(min, other) : other;
			max = max != null ? arithmetics.max(max, other) : other;

			cnt++;
			return this;
		}

		private MutableValue<N> merge(final MutableValue<N> other) {
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((arithmetics == null) ? 0 : arithmetics.hashCode());
			result = prime * result + cnt;
			@Nullable
			final N max$ = max;
			result = prime * result + ((max$ == null) ? 0 : max$.hashCode());
			@Nullable
			final N min$ = min;
			result = prime * result + ((min$ == null) ? 0 : min$.hashCode());
			result = prime * result + ((sqrSum == null) ? 0 : sqrSum.hashCode());
			result = prime * result + ((sum == null) ? 0 : sum.hashCode());
			return result;
		}

		@Override
		public boolean equals(@Nullable final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final MutableValue<?> other = (MutableValue<?>) obj;

			if (!arithmetics.equals(other.arithmetics)) {
				return false;
			}

			if (cnt != other.cnt)
				return false;
			@Nullable
			final N max$ = max;
			if (max$ == null) {
				if (other.max != null)
					return false;
			} else if (!max$.equals(other.max))
				return false;
			@Nullable
			final N min$ = min;
			if (min$ == null) {
				if (other.min != null)
					return false;
			} else if (!min$.equals(other.min))
				return false;
			if (!sqrSum.equals(other.sqrSum))
				return false;
			if (!sum.equals(other.sum))
				return false;
			return true;
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
			assert arithmetics.compare(arithmetics.sqr(sum), sqrSum) >= 0 : arithmetics.sqr(sum) + " => " + sqrSum;

			assert max != null && min != null;
			return new NumberStatistics<N>(cnt, max, min, sum, sqrSum, arithmetics);
		}
	}

	/**
	 * Creates an atomic cell.
	 */
	public NumberStatistics(final N n, final Arithmetics<N> arithmetics) {
		this.arithmetics = arithmetics;
		max = min = sum = n;
		sumSqr = arithmetics.sqr(n);
		cnt = 1;
	}

	public NumberStatistics(final int cnt, final N max, final N min, final N sum, final N sumSqr,
			final Arithmetics<N> arithmetics) {
		this.cnt = cnt;
		this.max = max;
		this.min = min;
		this.sum = sum;
		this.sumSqr = sumSqr;
		this.arithmetics = arithmetics;
	}

	public N avg() {
		return arithmetics.part(sum, cnt);
	}

	public N varianz() {
		return arithmetics.varianz(sum, sumSqr, cnt);
	}

	private static final MessageFormat MF = new MessageFormat(
			"NumberStatistics [cnt={0}, max={1}, min={2}, sum={3}, sumSqr={4}, avg()={5}, varianz()={6}]");

	@Override
	public String toString() {
		return MF.format(new Object[] { cnt, max, min, sum, sumSqr, avg(), varianz() });
	}

}
