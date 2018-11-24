package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.Nullable;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dateFormat.hashCode();
		return result;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CustomDateFormat other = (CustomDateFormat) obj;
		return dateFormat.equals(other.dateFormat);
	}
}
