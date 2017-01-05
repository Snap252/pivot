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
						random(Geschl.values()), new BigDecimal(r.nextInt(10001)).scaleByPowerOfTen(-2),
						new Date((RANDOM.nextInt(1200) - 750) * 86400000L))).collect(Collectors.toList());
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
		final Collector<Person, MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> reducer = PivotCollectors
				.getReducer(p -> p.wert, new BigDecimalArithmetics());

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

	public static class Person {
		private final String vorname;
		private final String nachname;
		private final Geschl geschlecht;
		private final int alter;
		private final BigDecimal wert;
		private final Date birthday;

		public Person(final String vorname, final String nachname, final int alter, final Geschl g,
				final BigDecimal wert, final Date birthday) {
			this.vorname = vorname;
			this.nachname = nachname;
			this.alter = alter;
			this.geschlecht = g;
			this.wert = wert;
			this.birthday = birthday;
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

		public Date getGeburtstag() {
			return birthday;
		}

		@Override
		public String toString() {
			return "Person [vorname=" + vorname + ", nachname=" + nachname + ", geschlecht=" + geschlecht + ", alter="
					+ alter + "]";
		}
	}
}