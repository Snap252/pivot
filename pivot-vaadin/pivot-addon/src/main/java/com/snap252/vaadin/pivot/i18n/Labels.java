package com.snap252.vaadin.pivot.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.vaadin.ui.Component;

public class Labels {
	private static final String BUNDLE_NAME = "com.snap252.org.pivoting.labels"; //$NON-NLS-1$

	private Labels() {
	}

	protected static Locale getLocaleFromComponent(final Component c) {
		final Locale componentLocale = c.getLocale();
		return componentLocale != null ? componentLocale : Locale.getDefault();
	}

	private static final ResourceBundle.Control control = LanguageControl.getInstance();

	public static String getString(final String key, final Component c) {
		return getString(key, getLocaleFromComponent(c));
	}

	public static String getString(final String key, final Locale locale) {
		try {
			return getBundle(locale).getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	protected static ResourceBundle getBundle(final Locale locale) {
		return ResourceBundle.getBundle(BUNDLE_NAME, locale, control);
	}
}
