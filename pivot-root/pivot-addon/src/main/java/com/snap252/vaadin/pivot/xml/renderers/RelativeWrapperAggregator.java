package com.snap252.vaadin.pivot.xml.renderers;

import java.math.BigDecimal;
import java.util.stream.Collector;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.vaadin.ui.renderers.Renderer;

//TODO: specify X
public class RelativeWrapperAggregator<X> extends Aggregator<@Nullable X, @Nullable BigDecimal> {

	private final  Aggregator<@Nullable X, @Nullable Number> aggregator;

	RelativeWrapperAggregator(final Aggregator<@Nullable X, @Nullable Number> aggregator) {
		this.aggregator = aggregator;
	}

	@Override
	public @Nullable BigDecimal getConvertedValue(@Nullable final X value) {
		return null;
	}

	@XmlAttribute(name = "null-representation")
	public  String nullRepresentation = "-";

	@Override
	public @NonNull Renderer<? super @Nullable BigDecimal> createRenderer() {
		return new BigDecimalRenderer(nullRepresentation);
	}

	@Override
	public <INPUT_TYPE> @NonNull Collector<INPUT_TYPE, ?, @Nullable X> getCollector() {
		// TODO Auto-generated method stub
		return null;
	}

}
