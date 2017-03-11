package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.SimpleDateFormat;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public enum DateRounding {
	YEAR(new SimpleDateFormat("yyyy"), "Jahr"),

	MONTH_VERY_SHORT(new SimpleDateFormat("MM/yyyy"), "Monat(sehr kurz)"),

	MONTH_SHORT(new SimpleDateFormat("MMM / yyyy"), "Monat(kurz)"),

	MONTH_LONG(new SimpleDateFormat("MMMM yyyy"), "Monat(lang)"),

	MONTH_ONLY_SHORT(new SimpleDateFormat("MMM"), "nur Monat (kurz)"),

	MONTH_ONLY_LONG(new SimpleDateFormat("MMMM"), "nur Monat (lang)"),

	WEEK(new SimpleDateFormat("'KW' w yyyy"), "Woche"),

	WEEK_ONLY(new SimpleDateFormat("'KW' w"), "nur Woche"),

	DAY(new SimpleDateFormat("dd.MM.yyyy"), "Tag"),

	DAY_ONLY(new SimpleDateFormat("dd"), "nur Tag"),

	DAY_MONTH(new SimpleDateFormat("dd.MM."), "Tag/Monat"),

	HOUR_MINUTE(new SimpleDateFormat("HH:mm"), "Stunde / Minute"),

	HOUR(new SimpleDateFormat("HH"), "Stunde"),

	MINUTE(new SimpleDateFormat("mm"), "Minute"),

	ALL(new SimpleDateFormat("HH:mm dd.MM.yyyy"), null),

	;
	public final SimpleDateFormat df;
	final @Nullable String s;

	DateRounding(final SimpleDateFormat df, @Nullable final String s) {
		this.df = df;
		this.s = s;
	}

	@Override
	public String toString() {
		if (s != null)
			return s;
		return "";
	}
}