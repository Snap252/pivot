package com.snap252.vaadin.pivot.xml.renderers;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.utils.ClassUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.renderers.TextRenderer;

public class ComparableAggregator<X extends Comparable<X>> extends Aggregator<Optional<X>, @Nullable String> {

	public enum Sorters {
		MIN() {
			@Override
			public <X extends Comparable<X>> BinaryOperator<X> getComparator() {
				return BinaryOperator.minBy(Comparator.naturalOrder());
			}
		},
		MAX() {

			@Override
			public <X extends Comparable<X>> BinaryOperator<X> getComparator() {
				return BinaryOperator.minBy(Comparator.reverseOrder());
			}
		}

		;

		public abstract <X extends Comparable<X>> BinaryOperator<X> getComparator();
	}

	@XmlAttribute(name = "sorter")
	public Sorters sorter = Sorters.MAX;

	@Override
	public String getConvertedValue(final Optional<X> value) {
		return value.isPresent() ? value.get().toString() : "---";
	}

	@Override
	public Renderer<? super String> createRenderer() {
		return new TextRenderer();
	}

	@Override
	public <INPUT_TYPE> Collector<INPUT_TYPE, ?, Optional<X>> getCollector() {
		return (Collector<INPUT_TYPE, ?, Optional<X>>) Collectors.reducing(sorter.<X>getComparator());
	}

	static class ComparableAggConfig extends VerticalLayout implements DefaultField<ComparableAggregator<?>> {
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

		ComparableAggConfig() {
			cb.setNullSelectionAllowed(false);

			addComponents(cb);
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
