package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

public class CustomDateFormat extends ConfigDateFormat {
	@XmlAttribute(name = "date-format")
	public String dateFormat = "dd.MM.yyyy HH:mm";
}
