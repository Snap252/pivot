package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;

import javax.xml.bind.annotation.XmlTransient;

public abstract class ConfigDateFormat {
	@XmlTransient
	protected abstract DateFormat getDateformat();
}
