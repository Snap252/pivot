package com.snap252.vaadin.pivot.xml.renderers;

import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.renderers.TextRenderer;

public class StringConcatAggregator extends Aggregator<@Nullable String, @Nullable String> {

	@Override
	public @Nullable String getConvertedValue(@Nullable final String value) {
		return value;
	}

	@SuppressWarnings("null")
	@Override
	public Renderer<? super @Nullable String> createRenderer() {
		return new TextRenderer();
	}

	@Override
	public <INPUT_TYPE> @NonNull Collector<INPUT_TYPE, ?, @Nullable String> getCollector() {
		// TODO Auto-generated method stub
		return null;
	}

}
