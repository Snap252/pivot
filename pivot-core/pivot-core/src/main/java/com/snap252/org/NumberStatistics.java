package com.snap252.org;

import java.text.MessageFormat;
import java.util.function.Function;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class NumberStatistics<N extends Number> {
	public final int cnt;
	public final N max;
	public final N min;
	public final N sum;
	public final N sumSqr;

	private static final NumberStatistics<?> NEUTRAL_ELEMENT = new NumberStatistics<Integer>(0,
			new IntegerArithmetics()) {
		@Override
		public String toString() {
			return "---";
		}

		public boolean isNeutralElement() {
			return true;
		}
	};
	private Arithmetics<N> arithmetics;

	public boolean isNeutralElement() {
		return false;
	}

	@SuppressWarnings({ "unchecked" })
	public static <N extends Number> NumberStatistics<N> getNeutralElement() {
		return (NumberStatistics<N>) NEUTRAL_ELEMENT;
	}

	@SuppressWarnings("null")
	public static <P, N extends Number> Collector<P, MutableValue<N>, NumberStatistics<N>> getReducer(Function<P, N> f,
			Arithmetics<N> arithmetics) {
		// Function<V, NumberStatistics<N>> transformer = valueExtractor
		// .andThen(n -> new NumberStatistics<>(n, arithmetics));

		return Collector.of(() -> MutableValue.getNeutralElement(arithmetics), (t, u) -> t.addSingle(f.apply(u)),
				MutableValue::merge, MutableValue::createNumberStatistics);
		// return Collectors.reducing(getNeutralElement(), transformer, (f1, f2)
		// -> aggregate(f1, f2, arithmetics));
	}

	public static final class MutableValue<N extends Number> {
		public final Arithmetics<N> arithmetics;
		public N sum;
		public N sqrSum;

		public int cnt;

		@Nullable
		public N min;
		@Nullable
		public N max;

		public MutableValue(N n, Arithmetics<N> arithmetics) {
			this.arithmetics = arithmetics;
			this.sum = n;
			this.sqrSum = arithmetics.sqr(n);
			cnt = 1;
		}

		/* neutral element */
		public static <N extends Number> MutableValue<N> getNeutralElement(Arithmetics<N> ar) {
			return new MutableValue<N>(ar);
		}

		private MutableValue(Arithmetics<N> arithmetics) {
			this.arithmetics = arithmetics;
			this.sum = sqrSum = arithmetics.getNeutralAddElement();
			cnt = 0;
		}

		MutableValue<N> addSingle(N other) {
			this.sum = arithmetics.add(sum, other);
			this.sqrSum = arithmetics.add(sqrSum, arithmetics.sqr(other));

			if (min != null) {
				min = arithmetics.compare(min, other) < 0 ? min : other;
			} else {
				min = other;
			}
			if (max != null) {
				max = arithmetics.compare(max, other) < 0 ? max : other;
			} else {
				max = other;
			}
			cnt++;
			return this;
		}

		MutableValue<N> merge(MutableValue<N> other) {
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
			N max$ = max;
			result = prime * result + ((max$ == null) ? 0 : max$.hashCode());
			@Nullable
			N min$ = min;
			result = prime * result + ((min$ == null) ? 0 : min$.hashCode());
			result = prime * result + ((sqrSum == null) ? 0 : sqrSum.hashCode());
			result = prime * result + ((sum == null) ? 0 : sum.hashCode());
			return result;
		}

		@Override
		public boolean equals(@Nullable Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MutableValue<?> other = (MutableValue<?>) obj;

			if (!arithmetics.equals(other.arithmetics)) {
				return false;
			}

			if (cnt != other.cnt)
				return false;
			@Nullable
			N max$ = max;
			if (max$ == null) {
				if (other.max != null)
					return false;
			} else if (!max$.equals(other.max))
				return false;
			@Nullable
			N min$ = min;
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

		protected void doMax(@Nullable N otherMax) {
			if (max == null) {
				max = otherMax;
			} else if (otherMax != null) {
				assert max != null;
				max = arithmetics.compare(max, otherMax) < 0 ? max : otherMax;
			}
		}

		protected void doMin(@Nullable N otherMin) {
			if (min == null) {
				min = otherMin;
			} else if (otherMin != null) {
				assert min != null;
				min = arithmetics.compare(min, otherMin) < 0 ? min : otherMin;
			}
		}

		public NumberStatistics<N> createNumberStatistics() {
			if (cnt == 0) {
				return NumberStatistics.getNeutralElement();
			}
			assert arithmetics.compare(arithmetics.sqr(sum), sqrSum) >= 0 : arithmetics.sqr(sum) + " => " + sqrSum;

			assert max != null && min != null;
			return new NumberStatistics<N>(cnt, max, min, sum, sqrSum, arithmetics);
		}
	}

	/**
	 * Creates an atomic cell.
	 */
	public NumberStatistics(N n, Arithmetics<N> arithmetics) {
		this.arithmetics = arithmetics;
		max = min = sum = n;
		sumSqr = arithmetics.sqr(n);
		cnt = 1;
	}

	public NumberStatistics(int cnt, N max, N min, N sum, N sumSqr, Arithmetics<N> arithmetics) {
		this.cnt = cnt;
		this.max = max;
		this.min = min;
		this.sum = sum;
		this.sumSqr = sumSqr;
		this.arithmetics = arithmetics;
	}

	public N avg() {
		assert !isNeutralElement();
		return arithmetics.part(sum, cnt);
	}

	public N varianz() {
		assert !isNeutralElement();
		// see:
		// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance

		return arithmetics.varianz(sum, sumSqr, cnt);
	}

	private static final MessageFormat mf = new MessageFormat(
			"NumberStatistics [cnt={0}, max={1}, min={2}, sum={3}, sumSqr={4}, avg()={5}, varianz()={6}]");

	@Override
	public String toString() {
		assert !isNeutralElement();
		return mf.format(new Object[] { cnt, max, min, sum, sumSqr, avg(), varianz() });
	}

}
