package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class Attribute {
	@XmlAttribute(name="name", required=true)
	public String attributeName = "";
}
