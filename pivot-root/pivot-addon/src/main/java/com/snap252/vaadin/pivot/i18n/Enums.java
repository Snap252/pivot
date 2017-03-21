package com.snap252.vaadin.pivot.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.ui.Component;

public class Enums {
	private static final String BUNDLE_NAME = "com.snap252.org.pivoting.enums"; //$NON-NLS-1$

	private Enums() {
	}

	public static String getString(final Enum<?> key, final Locale l) {
		return getString(key.getDeclaringClass().getSimpleName() + "." + key.name(), l);
	}

	public static String getString(final Enum<?> key, final Component c) {
		return getString(key.getDeclaringClass().getSimpleName() + "." + key.name(), getLocaleFromComponent(c));
	}

	protected static Locale getLocaleFromComponent(final Component c) {
		final Locale componentLocale = c.getLocale();
		return componentLocale != null ? componentLocale : Locale.getDefault();
	}

	private static final ResourceBundle.Control control = new ResourceBundle.Control() {
		@Override
		public @Nullable Locale getFallbackLocale(final String baseName, final Locale locale) {
			if (locale == Locale.ROOT)
				return null;

			final Locale ret = Locale.forLanguageTag(locale.getLanguage());
			if (ret.equals(locale)) {
				return Locale.ROOT;
			}
			return ret;
		}

		@Override
		public long getTimeToLive(final String baseName, final Locale locale) {
			return TTL_NO_EXPIRATION_CONTROL;
		}
	};

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
