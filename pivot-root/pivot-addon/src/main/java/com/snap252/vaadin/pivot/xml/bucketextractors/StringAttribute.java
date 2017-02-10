package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

public class StringAttribute extends Attribute {
	@XmlAttribute(name = "sub-string")
	public Integer subString = 0;
}
