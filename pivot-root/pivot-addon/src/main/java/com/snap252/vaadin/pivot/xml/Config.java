package com.snap252.vaadin.pivot.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.snap252.vaadin.pivot.xml.renderers.DecimalValueField;
import com.snap252.vaadin.pivot.xml.renderers.IntegerValueField;
import com.snap252.vaadin.pivot.xml.renderers.ObjectObjectValueField;
import com.snap252.vaadin.pivot.xml.renderers.ValueField;

@XmlRootElement(name = "config")
public class Config {

	@XmlAttribute(name = "display-name")
	public String displayName = "New Config";

	@XmlElements({ @XmlElement(name = "object", type = ObjectObjectValueField.class),
			@XmlElement(name = "decimal", type = DecimalValueField.class),
			@XmlElement(name = "integer", type = IntegerValueField.class),

	})
	public ValueField renderer = new ObjectObjectValueField();

	@XmlElement(name = "columns")
	public ValuesConfig columns = new ValuesConfig();

	@XmlElement(name = "rows")
	public ValuesConfig rows = new ValuesConfig();

}
