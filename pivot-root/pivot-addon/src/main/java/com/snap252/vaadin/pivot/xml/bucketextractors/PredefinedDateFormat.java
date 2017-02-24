package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.DateRounding;

public class PredefinedDateFormat extends ConfigDateFormat {
	@Nullable
	@XmlAttribute(name = "date-rounding")
	public DateRounding dateRounding;

	@Override
	protected DateFormat getDateformat() {
		return Optional.ofNullable(dateRounding).orElse(DateRounding.DAY).df;
	}
}
