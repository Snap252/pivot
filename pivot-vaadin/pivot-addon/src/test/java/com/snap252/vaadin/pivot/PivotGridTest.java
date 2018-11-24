package com.snap252.vaadin.pivot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PivotGridTest {

	private static String SEPARATOR_1 = ".separator1 {\n" + "	color: red;\n" + "}";

	private static String SEPARATOR_2 = ".separator2 {\n" + "	color: red;\n" + "}";
	private static String SEPARATOR_3 = ".separator3 {\n" + "	color: red;\n" + "}";

	@Test
	public void assertThatWeDoNotEffectTheDefaultBehaviour() throws Exception {
		final String s = ScssTest.testSingleFile("/VAADIN/themes/demo/styles2.scss");

		assertTrue(s.contains(SEPARATOR_1));
		assertTrue(s.contains(SEPARATOR_2));
		assertTrue(s.contains(SEPARATOR_3));

		final int i0 = s.indexOf(SEPARATOR_1) + SEPARATOR_1.length();
		final int i1 = s.indexOf(SEPARATOR_2);
		final String s1 = s.substring(i0, i1);

		// final int i2 = s.indexOf(SEPARATOR_2) + SEPARATOR_2.length();
		final int i3 = s.indexOf(SEPARATOR_3);

		// System.err.println(s.substring(i2, i3));

		final int i4 = i3 + SEPARATOR_3.length();
		final String s2 = s.substring(i4);

		assertEquals(s1.trim(), s2.trim());
	}
}
