package com.snap252.vaadin.pivot.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.DateAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.NumberAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.ObjectAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.StringAttribute;

public class ValuesConfig {
	@XmlElements({ @XmlElement(name = "date", type = DateAttribute.class),
			@XmlElement(name = "object", type = ObjectAttribute.class),
			@XmlElement(name = "number", type = NumberAttribute.class),
			@XmlElement(name = "string", type = StringAttribute.class)

	})
	public List<Attribute> attributes = new ArrayList<>();

	@XmlAttribute
	public boolean colorize = false;
}
