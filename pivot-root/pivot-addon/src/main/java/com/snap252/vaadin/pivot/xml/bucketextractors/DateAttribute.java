package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class DateAttribute extends Attribute {
	@XmlElements({ @XmlElement(name = "predefind", type = PredefinedDateFormat.class),
			@XmlElement(name = "custom", type = CustomDateFormat.class) })
	public ConfigDateFormat dateFormat = new PredefinedDateFormat();
}
