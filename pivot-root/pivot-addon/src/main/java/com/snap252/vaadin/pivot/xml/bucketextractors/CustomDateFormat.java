package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.XmlAttribute;

public class CustomDateFormat extends ConfigDateFormat {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	@XmlAttribute(name = "date-format")
	public void setDateFormatString(final String textFormat) {
		this.dateFormat = new SimpleDateFormat(textFormat);
	}
	public String getDateFormatString() {
		return dateFormat.toPattern();
	}


	@Override
	protected DateFormat getDateformat() {
		return dateFormat;
	}

}
