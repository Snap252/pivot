package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.ui.TextArea;
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
	public Collector<?, ?, String> getCollector() {
		return Collectors.collectingAndThen(Collectors.joining(separator),
				s -> s.length() < 100 ? s : s.substring(0, 100));
	}

	static class ConcatAggConfig extends FormLayoutField<StringConcatAggregator> {

		private final TextArea tf = new TextArea("Trennzeichen");

		ConcatAggConfig() {
			super(new StringConcatAggregator());
			tf.setRows(4);
			tf.setColumns(14);
			tf.setValue(value.separator);
			tf.addValueChangeListener(vce -> {
				final String newSeparator = (String) vce.getProperty().getValue();
				assert newSeparator != null;
				if (Objects.equals(value.separator, newSeparator)) {
					return;
				}
				value.separator = newSeparator;
				fireValueChange();
			});
			addComponent(tf);
		}

		@Override
		protected void doAfterValueSetWithoutEvents(final StringConcatAggregator value) {
			tf.setValue(value.separator);
		}
	}
}
