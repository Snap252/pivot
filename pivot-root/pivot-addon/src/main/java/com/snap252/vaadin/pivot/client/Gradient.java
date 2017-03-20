package com.snap252.vaadin.pivot.client;

import java.io.Serializable;
import java.util.Arrays;

public class Gradient implements Serializable {
	public float[] fractions;
	public Color[] colors;

	public Gradient() {

	}

	public Gradient(final float[] fractions, final Color[] colors) {
		assert fractions.length == colors.length;
		this.fractions = fractions;
		this.colors = colors;

	}

	public Color interpolate(final float value) {
		if (fractions.length != colors.length) {
			throw new IllegalArgumentException();
		}

		int index = Arrays.binarySearch(fractions, value);

		if (index >= 0) {
			return colors[index];
		}

		index = -index - 1;

		if (index >= colors.length) {
			return colors[colors.length - 1];
		}

		final Color c0 = colors[index - 1];
		final Color c1 = colors[index];
		final float factor = 1 - (value - fractions[index - 1]) / (fractions[index] - fractions[index - 1]);

		final float factor1 = 1 - factor;
		return new Color((int) (c0.getRed() * factor + c1.getRed() * factor1),
				(int) (c0.getGreen() * factor + c1.getGreen() * factor1),
				(int) (c0.getBlue() * factor + c1.getBlue() * factor1),
				(int) (c0.getAlpha() * factor + c1.getAlpha() * factor1));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(colors);
		result = prime * result + Arrays.hashCode(fractions);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Gradient other = (Gradient) obj;
		if (!Arrays.equals(colors, other.colors))
			return false;
		if (!Arrays.equals(fractions, other.fractions))
			return false;
		return true;
	}

}
