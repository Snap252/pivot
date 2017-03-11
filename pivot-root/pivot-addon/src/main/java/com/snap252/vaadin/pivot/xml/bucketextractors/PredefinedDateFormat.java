package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.jdt.annotation.Nullable;

public class PredefinedDateFormat extends ConfigDateFormat {
	@Nullable
	@XmlAttribute(name = "date-rounding")
	public DateRounding dateRounding;

	@Override
	protected DateFormat getDateformat() {
		return Optional.ofNullable(dateRounding).orElse(DateRounding.DAY).df;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateRounding != null) ? dateRounding.hashCode() : 0);
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
		final PredefinedDateFormat other = (PredefinedDateFormat) obj;
		if (dateRounding != other.dateRounding)
			return false;
		return true;
	}


}
