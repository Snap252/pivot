package com.snap252.vaadin.pivot.xml.renderers;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
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

import com.snap252.vaadin.pivot.utils.ClassUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
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
	public <INPUT_TYPE> Collector<INPUT_TYPE, ?, Optional<X>> getCollector() {
		return (Collector<INPUT_TYPE, ?, Optional<X>>) Collectors.reducing(sorter.<X>getComparator());
	}

	static class ComparableAggConfig extends FormLayout implements DefaultField<ComparableAggregator<?>> {
		private ComparableAggregator<?> agg = new ComparableAggregator<>();

		@Override
		public void focus() {
			super.focus();
		}

		private static final Method VALUE_CHANGE_METHOD;

		static {
			try {
				VALUE_CHANGE_METHOD = Property.ValueChangeListener.class.getDeclaredMethod("valueChange",
						new Class[] { Property.ValueChangeEvent.class });
			} catch (final java.lang.NoSuchMethodException e) {
				// This should never happen
				throw new java.lang.RuntimeException("Internal error finding methods in AbstractField");
			}
		}

		protected void fireValueChange() {
			fireEvent(new AbstractField.ValueChangeEvent(this));
		}

		@Override
		public void addValueChangeListener(final Property.ValueChangeListener listener) {
			addListener(AbstractField.ValueChangeEvent.class, listener, VALUE_CHANGE_METHOD);
			// ensure "automatic immediate handling" works
			markAsDirty();
		}

		private boolean fireEvents = true;

		private final ComboBox cb = new ComboBox("Darstellung", Arrays.asList(Sorters.values()));
		private final TextField tf = new TextField("Formatierung");

		ComparableAggConfig() {
			cb.setNullSelectionAllowed(false);
			tf.setNullRepresentation("");
			tf.addValueChangeListener(vce -> {
				String newPattern = (String) vce.getProperty().getValue();
				if (newPattern != null && newPattern.isEmpty()) {
					newPattern = null;
				}
				if (Objects.equals(agg.getFormat(), newPattern))
					return;

				agg.setFormat(newPattern);
				if (fireEvents)
					fireValueChange();
			});

			addComponents(cb, tf);
			cb.addValueChangeListener(vce -> {
				final Sorters sorter = requireNonNull((Sorters) vce.getProperty().getValue());
				if (agg.sorter == sorter) {
					return;
				}
				agg.sorter = sorter;
				if (fireEvents)
					fireValueChange();
			});
		}

		@Override
		public ComparableAggregator<?> getValue() {
			return agg;
		}

		@Override
		public void setValue(final ComparableAggregator<?> newValue) throws com.vaadin.data.Property.ReadOnlyException {
			fireEvents = false;
			try {
				agg = newValue;
				cb.setValue(agg.sorter);
			} finally {
				fireEvents = true;
			}

		}

		@Override
		public Class<ComparableAggregator<?>> getType() {
			return ClassUtils.cast(ComparableAggregator.class);
		}
	}
}
