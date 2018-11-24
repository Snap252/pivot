package com.snap252.vaadin.pivot.client;

import java.io.Serializable;

public class Color implements Serializable {

	public int red;
	public int green;
	public int blue;
	public int alpha;

	public Color(final int rgba) {
		alpha = (rgba >> 24) & 0xFF;
		red = (rgba >> 16) & 0xFF;
		green = (rgba >> 8) & 0xFF;
		blue = (rgba >> 0) & 0xFF;
	}

	public int toRGBA() {
		return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 0);
	}

	public Color() {

	}

	public Color(final int red, final int green, final int blue, final int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public Color(final int red, final int green, final int blue) {
		this(red, green, blue, 255);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public int getAlpha() {
		return alpha;
	}

	public String toRGBACssString(final int alpha) {
		return "rgba(" + getRed() + ", " + getGreen() + ", " + getBlue() + ", " + (getAlpha() * alpha / (255.f * 255f))
				+ ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + alpha;
		result = prime * result + blue;
		result = prime * result + green;
		result = prime * result + red;
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
		final Color other = (Color) obj;
		if (alpha != other.alpha)
			return false;
		if (blue != other.blue)
			return false;
		if (green != other.green)
			return false;
		if (red != other.red)
			return false;
		return true;
	}

	private static final double FACTOR = 0.7;

	/**
	 * Creates a new <code>Color</code> that is a darker version of this
	 * <code>Color</code>.
	 * <p>
	 * This method applies an arbitrary scale factor to each of the three RGB
	 * components of this <code>Color</code> to create a darker version of this
	 * <code>Color</code>. The {@code alpha} value is preserved. Although
	 * <code>brighter</code> and <code>darker</code> are inverse operations, the
	 * results of a series of invocations of these two methods might be
	 * inconsistent because of rounding errors.
	 *
	 * @return a new <code>Color</code> object that is a darker version of this
	 *         <code>Color</code> with the same {@code alpha} value.
	 * @see java.awt.Color#brighter
	 * @since JDK1.0
	 */
	public Color darker() {
		return new Color(Math.max((int) (getRed() * FACTOR), 0), Math.max((int) (getGreen() * FACTOR), 0),
				Math.max((int) (getBlue() * FACTOR), 0), getAlpha());
	}

	/**
	 * Creates a new <code>Color</code> that is a brighter version of this
	 * <code>Color</code>.
	 * <p>
	 * This method applies an arbitrary scale factor to each of the three RGB
	 * components of this <code>Color</code> to create a brighter version of
	 * this <code>Color</code>. The {@code alpha} value is preserved. Although
	 * <code>brighter</code> and <code>darker</code> are inverse operations, the
	 * results of a series of invocations of these two methods might be
	 * inconsistent because of rounding errors.
	 *
	 * @return a new <code>Color</code> object that is a brighter version of
	 *         this <code>Color</code> with the same {@code alpha} value.
	 * @see java.awt.Color#darker
	 * @since JDK1.0
	 */
	public Color brighter() {
		int r = getRed();
		int g = getGreen();
		int b = getBlue();
		final int alpha = getAlpha();

		/*
		 * From 2D group: 1. black.brighter() should return grey 2. applying
		 * brighter to blue will always return blue, brighter 3. non pure color
		 * (non zero rgb) will eventually return white
		 */
		final int i = (int) (1.0 / (1.0 - FACTOR));
		if (r == 0 && g == 0 && b == 0) {
			return new Color(i, i, i, alpha);
		}
		if (r > 0 && r < i)
			r = i;
		if (g > 0 && g < i)
			g = i;
		if (b > 0 && b < i)
			b = i;

		return new Color(Math.min((int) (r / FACTOR), 255), Math.min((int) (g / FACTOR), 255),
				Math.min((int) (b / FACTOR), 255), alpha);
	}

	@Override
	public String toString() {
		return "Color [red=" + red + ", green=" + green + ", blue=" + blue + ", alpha=" + alpha + "]";
	}

}