package com.snap252.vaadin.pivot.xml.bucketextractors;

import javax.xml.bind.annotation.XmlAttribute;

public class NumberAttribute extends Attribute{
	@XmlAttribute(name = "rounding")
	public int rounding = 2;
}
