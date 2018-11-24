package com.snap252.vaadin.pivot.xml.renderers;

import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.client.Color;
import com.snap252.vaadin.pivot.client.Gradient;

public class GradientAdapter extends XmlAdapter<@Nullable String, @Nullable Gradient> {

	@Override
	public @Nullable Gradient unmarshal(@Nullable final String v) throws Exception {
		if (v == null || v.isEmpty())
			return null;

		final String[] split = v.split(":");
		assert split.length == 2;
		final String fractionString = split[0];
		final String colorString = split[1];

		final double[] fractions = Stream.of(fractionString.split(",")).mapToDouble(Double::parseDouble).toArray();
		final Color[] colors = Stream.of(colorString.split(",")).map(color -> new Color(Integer.parseUnsignedInt(color, 16)))
				.toArray(i -> new Color[i]);
		assert fractions.length == colors.length;

		return new Gradient(fractions, colors);
	}

	@Override
	public @Nullable String marshal(@Nullable final Gradient v) throws Exception {
		if (v == null)
			return "";

		final String fractions = DoubleStream.of(v.fractions).mapToObj(Double::toString)
				.collect(Collectors.joining(","));
		final String colors = Stream.of(v.colors).map(c -> Integer.toHexString(c.toRGBA()).toUpperCase())
				.collect(Collectors.joining(","));
		final String ret = fractions + ":" + colors;
		return ret;
	}
}
