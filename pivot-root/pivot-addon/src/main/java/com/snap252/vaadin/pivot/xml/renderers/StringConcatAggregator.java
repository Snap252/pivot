package com.snap252.vaadin.pivot.xml.renderers;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.renderers.TextRenderer;

public class StringConcatAggregator extends Aggregator<@Nullable String, String> {

	@Override
	public String getConvertedValue(final String value) {
		return value;
	}

	@Override
	public Renderer<? super String> createRenderer() {
		return new TextRenderer();
	}

	@XmlAttribute
	public String separator = ", ";

	@Override
	public  Collector<?, ?, String> getCollector() {
		return Collectors.collectingAndThen(Collectors.joining(separator),
				s -> s.length() < 100 ? s : s.substring(0, 100));
	}

	static class ConcatAggConfig extends VerticalLayout implements DefaultField<StringConcatAggregator> {
		private StringConcatAggregator agg = new StringConcatAggregator();

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

		private final TextArea tf = new TextArea("Trennzeichen");

		ConcatAggConfig() {
			tf.setRows(4);
			tf.setColumns(14);
			tf.setValue(agg.separator);
			tf.addValueChangeListener(vce -> {
				final String newSeparator = (String) vce.getProperty().getValue();
				assert newSeparator != null;
				if (Objects.equals(newSeparator, agg.separator)) {
					return;
				}
				agg.separator = newSeparator;
				if (fireEvents)
					fireValueChange();
			});
			addComponent(tf);
		}

		@Override
		public @NonNull StringConcatAggregator getValue() {
			return agg;
		}

		@Override
		public void setValue(@NonNull final StringConcatAggregator newValue)
				throws com.vaadin.data.Property.ReadOnlyException {
			fireEvents = false;
			try {
				agg = newValue;
				tf.setValue(agg.separator);
			} finally {
				fireEvents = true;
			}

		}

		@Override
		public Class<StringConcatAggregator> getType() {
			return StringConcatAggregator.class;
		}
	}
}
