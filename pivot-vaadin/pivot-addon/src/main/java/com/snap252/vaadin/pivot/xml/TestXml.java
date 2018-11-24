package com.snap252.vaadin.pivot.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class TestXml {

	public static void main(final String[] args) throws Exception {
		final JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
		final Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
		final Config unmarshal = (Config) jaxbMarshaller.unmarshal(TestXml.class.getResourceAsStream("Config.xml"));
		System.err.println(unmarshal);
	}
}
