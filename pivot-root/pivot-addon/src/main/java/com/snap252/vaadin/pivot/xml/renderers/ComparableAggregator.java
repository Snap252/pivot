package com.snap252.vaadin.pivot.xml.renderers;

import static java.util.Objects.requireNonNull;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.i18n.LookupComboBox;
import com.snap252.vaadin.pivot.i18n.LookupTextField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.renderers.TextRenderer;

public class ComparableAggregator<X extends Comparable<X>> extends Aggregator<Optional<X>, @Nullable String> {

	public enum Sorters {
		MIN() {
			@Override
			protected <X extends Comparable<X>> BinaryOperator<X> getComparator() {
				return BinaryOperator.minBy(Comparator.naturalOrder());
			}
		},
		MAX() {

			@Override
			protected <X extends Comparable<X>> BinaryOperator<X> getComparator() {
				return BinaryOperator.minBy(Comparator.reverseOrder());
			}
		};

		protected abstract <X extends Comparable<X>> BinaryOperator<X> getComparator();
	}

	@XmlAttribute(name = "sorter")
	public Sorters sorter = Sorters.MAX;

	@XmlTransient
	@Nullable
	private MessageFormat messageFormat;

	@XmlAttribute(name = "format")
	public void setFormat(final @Nullable String pattern) {
		messageFormat = pattern != null ? new MessageFormat(pattern) : null;
	}

	public @Nullable String getFormat() {
		return messageFormat != null ? messageFormat.toPattern() : null;
	}

	private enum DEFAULT_FORMATS {
		DATE(new MessageFormat("{0,date,medium}"));
		private final MessageFormat mf;

		DEFAULT_FORMATS(final MessageFormat mf) {
			this.mf = mf;
		}

		public String format(final Object... o) {
			return mf.format(o);
		}
	}

	@Override
	public String getConvertedValue(final Optional<X> value) {

		if (!value.isPresent())
			return "---";

		final X v = value.get();
		if (messageFormat != null)
			return messageFormat.format(new Object[] { v });

		if (v instanceof Date) {
			return DEFAULT_FORMATS.DATE.format(v);
		}
		return v.toString();
	}

	@Override
	public Renderer<? super String> createRenderer() {
		return new TextRenderer();
	}

	@Override
	public Collector<?, ?, Optional<X>> getCollector() {
		return Collectors.reducing(sorter.<X>getComparator());
	}

	static class ComparableAggConfig extends FormLayoutField<ComparableAggregator<?>> {

		private final AbstractSelect displayCheckbox = new LookupComboBox("display", Sorters.values());
		private final TextField formatTextfield = new LookupTextField("format");

		ComparableAggConfig() {
			super(new ComparableAggregator<>());
			displayCheckbox.setNullSelectionAllowed(false);
			formatTextfield.setNullRepresentation("");
			formatTextfield.addValueChangeListener(vce -> {
				String newPattern = (String) vce.getProperty().getValue();
				if (newPattern != null && newPattern.isEmpty()) {
					newPattern = null;
				}
				final ComparableAggregator<?> value$ = value;
				assert value$ != null;
				if (Objects.equals(value$.getFormat(), newPattern))
					return;

				value$.setFormat(newPattern);
				fireValueChange();
			});

			addComponents(displayCheckbox, formatTextfield);
			displayCheckbox.addValueChangeListener(vce -> {
				final Sorters sorter = requireNonNull((Sorters) vce.getProperty().getValue());
				final ComparableAggregator<?> value$ = value;
				assert value$ != null;
				if (value$.sorter == sorter) {
					return;
				}
				value$.sorter = sorter;
				fireValueChange();
			});
		}

		@Override
		protected void doAfterValueSetWithoutEvents(final ComparableAggregator<?> value) {
			displayCheckbox.setValue(value.sorter);
		}
	}
}
