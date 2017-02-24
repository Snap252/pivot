package com.snap252.vaadin.pivot.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.DateAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.NumberAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.ObjectAttribute;
import com.snap252.vaadin.pivot.xml.bucketextractors.StringAttribute;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifier;
import com.snap252.vaadin.pivot.xml.data.ChangeNotifierSupplier;
import com.snap252.vaadin.pivot.xml.data.NotifyingList;

public class ValuesConfig implements ChangeNotifierSupplier<List<Attribute<?>>> {
	@XmlElements({ @XmlElement(name = "date", type = DateAttribute.class),
			@XmlElement(name = "object", type = ObjectAttribute.class),
			@XmlElement(name = "number", type = NumberAttribute.class),
			@XmlElement(name = "string", type = StringAttribute.class)

	})
	public final NotifyingList<Attribute<?>> attributes = new NotifyingList<>();

	@XmlAttribute
	public boolean colorize = false;

	@XmlTransient
	@Override
	public ChangeNotifier<List<Attribute<?>>> getChangeNotifierSupplier() {
		return attributes.getChangeNotifierSupplier();
	}
}
