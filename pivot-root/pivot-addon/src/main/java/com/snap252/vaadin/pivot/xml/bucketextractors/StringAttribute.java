package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

public class StringAttribute extends Attribute {
	@XmlAttribute(name = "substring")
	public Integer subString = 0;
}
