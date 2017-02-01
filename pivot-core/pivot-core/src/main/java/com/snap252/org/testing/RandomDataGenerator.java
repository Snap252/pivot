package com.snap252.org.testing;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.org.pivoting.BiBucket;
import com.snap252.org.pivoting.BiBucketParameter;
import com.snap252.org.pivoting.NamedPivotCriteria;

public class RandomDataGenerator {
	private static final Random RANDOM = new Random();

	@SafeVarargs
	public static Stream<Object[]> getAsStream(final int cnt, final Function<Random, Object>... generators) {
		return IntStream.range(0, cnt).mapToObj(_ignore -> Stream.of(generators).map(g -> g.apply(RANDOM)).toArray());
	}

	public static <@NonNull T> Stream<T> getAsStream(final int cnt, final Function<Random, T> generators) {
		return IntStream.range(0, cnt).mapToObj(_ignore -> generators.apply(RANDOM));
	}

	static <T> T random(@NonNull final T[] t) {
		return t[RANDOM.nextInt(t.length)];
	}

	public static void main(final String[] args) throws Exception {
		@SuppressWarnings("deprecation")
		final BiBucketParameter<Person> parameter = new BiBucketParameter<Person>(createPersons(20000))
				.setColFnkt(new NamedPivotCriteria<>(p -> p.birthday.getYear() + 1900, "GebDat(Jahr)"),
						new NamedPivotCriteria<>(p -> p.birthday.getMonth() + 1, "GebDat(Monat)")

				)

				.setRowFnkt(

						new NamedPivotCriteria<>(p -> Character.toUpperCase(p.vorname.charAt(0)), "Vorname(0)"),
						new NamedPivotCriteria<>(p -> p.geschlecht, "Geschlecht", false)

		// ,p -> p.geschlecht
		// ,p -> p.alter / 10

		);

		final BiBucket<Person> biBucket = Timers.printTimer("doing bucket", 20, () -> new BiBucket<Person>(parameter));

		Timers.printTimer("printing", () -> write(createPersons(20000), biBucket));
	}

	public static List<Person> createPersons(final int cnt) {
		return getAsStream(cnt,
				r -> new Person(random(TestData.vorname), random(TestData.nachname), r.nextInt(60) + 10,
						random(Geschl.values()), random(Abteilung.values()),
						new BigDecimal(r.nextInt(10001)).scaleByPowerOfTen(-2), r.nextInt(1793),
						new BigDecimal(r.nextInt(10001) - 10001 / 2).scaleByPowerOfTen(-2),
						r.nextBoolean() ? null : new BigDecimal(r.nextInt(10001) - 10001 / 2).scaleByPowerOfTen(-2),
						new Date((RANDOM.nextInt(1200) - 750) * 86400000L),
						r.nextBoolean() ? null : String.valueOf(r.nextInt(80) + 40), random(TripleEnum.values()),
						random(TripleEnum.values()), r.nextBoolean() ? null : random(TripleEnum.values())

				)).collect(Collectors.toList());
	}

	protected static void write(final List<Person> personen, final BiBucket<Person> biBucket) {
		try (OutputStream os = new BufferedOutputStream(
				new FileOutputStream("C:\\Users\\Snap252\\Documents\\1.html"))) {
			try (Writer writer = new OutputStreamWriter(os)) {
				writeHtml(biBucket, writer);
			}
			os.flush();
		} catch (final IOException e) {
			throw new AssertionError();
		}
		Timers.printTimer("just write to memory", 10, () -> {
			try (Writer writer = new StringWriter(1 << 20)) {
				writeHtml(biBucket, writer);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
	}

	protected static void writeHtml(final BiBucket<Person> biBucket2, final Writer writer) throws IOException {
		// Collector<NumberStatistics<Double>, ?, NumberStatistics<Double>>
		// reducer = NumberStatistics
		// .getReducer((n1, n2) -> n1 + n2);
		final Collector<Person, @NonNull MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> reducer = PivotCollectors
				.getNumberReducer(p -> p.wert, new BigDecimalArithmetics());

		final BiConsumer<Writer, @Nullable NumberStatistics<BigDecimal>> cellWriter = (w, ns) -> {
			if (ns == null) {
				return;
			}
			try {
				w.write("<div title='");
				w.write(ns.toString());
				w.write("'>");
				w.write(ns.sum.toPlainString());
				w.write("</div>");
			} catch (final IOException e) {
				assert false;
				e.printStackTrace();
			}

			// writer.write(ns.sum.toPlainString());
		};
		biBucket2.createHtmlWriter(reducer).writeHtml(writer, cellWriter);
	}

	public enum Geschl {
		m, w, unbekannt
	}

	public enum TripleEnum {
		e1, e2, e3
	}

	public enum Abteilung {
		A, B, C, D, E, F, G, H, I
	}

	public static class Person {
		private final String vorname;
		private final String nachname;
		private final Geschl geschlecht;
		private final int alter;
		private final BigDecimal wert;
		private final Date birthday;
		private final int wertGanzzahl;
		private final BigDecimal wertNeg;
		private final @Nullable BigDecimal wertNullable;
		private final Abteilung abteilung;
		private final @Nullable String nullableString;
		private final TripleEnum e1;
		private final TripleEnum e2;
		private final @Nullable TripleEnum e3;

		public Person(final String vorname, final String nachname, final int alter, final Geschl g, final Abteilung abt,
				final BigDecimal wert, final int wertGanzzahl, final BigDecimal wertNeg,
				@Nullable final BigDecimal wertNullable, final Date birthday, @Nullable final String nullableString,
				final TripleEnum e1, final TripleEnum e2, @Nullable final TripleEnum e3) {
			this.vorname = vorname;
			this.nachname = nachname;
			this.alter = alter;
			this.geschlecht = g;
			this.abteilung = abt;
			this.wert = wert;
			this.wertGanzzahl = wertGanzzahl;
			this.wertNeg = wertNeg;
			this.wertNullable = wertNullable;
			this.birthday = birthday;
			this.nullableString = nullableString;
			this.e1 = e1;
			this.e2 = e2;
			this.e3 = e3;
		}

		public TripleEnum getE1() {
			return e1;
		}

		public TripleEnum getE2() {
			return e2;
		}

		public @Nullable TripleEnum getE3() {
			return e3;
		}

		public @Nullable String getNullableString() {
			return nullableString;
		}

		public BigDecimal getWertNeg() {
			return wertNeg;
		}

		public Abteilung getAbteilung() {
			return abteilung;
		}

		public @Nullable BigDecimal getWertNullable() {
			return wertNullable;
		}

		public String getVorname() {
			return vorname;
		}

		public String getNachname() {
			return nachname;
		}

		public Geschl getGeschlecht() {
			return geschlecht;
		}

		public int getAlter() {
			return alter;
		}

		public BigDecimal getWert() {
			return wert;
		}

		public int getWertGanzzahl() {
			return wertGanzzahl;
		}

		public Date getGeburtstag() {
			return birthday;
		}

		@Override
		public String toString() {
			return "Person [vorname=" + vorname + ", nachname=" + nachname + ", geschlecht=" + geschlecht + ", alter="
					+ alter + ", wert=" + wert + ", birthday=" + birthday + ", wertGanzzahl=" + wertGanzzahl + "]";
		}

	}
}
