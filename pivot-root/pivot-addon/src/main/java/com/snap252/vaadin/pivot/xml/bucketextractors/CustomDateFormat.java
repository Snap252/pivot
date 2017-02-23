package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.XmlAttribute;

public class CustomDateFormat extends ConfigDateFormat {
	@XmlAttribute(name = "date-format")
	public String dateFormat = "dd.MM.yyyy HH:mm";

	@Override
	protected DateFormat getDateformat() {
		//FIXME: do this in setter
		return new SimpleDateFormat(dateFormat);
	}
}
