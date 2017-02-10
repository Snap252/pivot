package com.snap252.vaadin.pivot.xml.renderers;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.ui.renderers.Renderer;

public abstract class Aggregator<@Nullable T, @Nullable U> {

	public abstract @Nullable U getConvertedValue(@Nullable T value);

	public abstract Renderer<? super @Nullable U> createRenderer();

	@XmlAttribute(name = "null-representation")
	public String nullRepresentation = "-";
}
