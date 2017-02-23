package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;

import javax.xml.bind.annotation.XmlAttribute;

import com.snap252.vaadin.pivot.DateRounding;

public class PredefinedDateFormat extends ConfigDateFormat {
	@XmlAttribute(name = "date-rounding")
	public DateRounding dateRounding = DateRounding.DAY;

	@Override
	protected DateFormat getDateformat() {
		return dateRounding.df;
	}
}
