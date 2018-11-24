package com.snap252.vaadin.pivot.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jdt.annotation.Nullable;

final class LanguageControl extends ResourceBundle.Control {
	private static LanguageControl instance = new LanguageControl();

    public static ResourceBundle.Control getInstance(){
    	return instance;
    }
    private LanguageControl(){

    }
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
}
