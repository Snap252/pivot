package com.snap252.vaadin.pivot.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.xml.data.NotifyingList;
import com.snap252.vaadin.pivot.xml.renderers.DecimalValueField;
import com.snap252.vaadin.pivot.xml.renderers.IntegerValueField;
import com.snap252.vaadin.pivot.xml.renderers.ObjectValueField;
import com.snap252.vaadin.pivot.xml.renderers.ValueField;

@XmlRootElement(name = "config")
public class Config {

	@XmlAttribute(name = "display-name")
	public String displayName = "New Config";

	@XmlElements({ @XmlElement(name = "object", type = ObjectValueField.class),
			@XmlElement(name = "decimal", type = DecimalValueField.class),
			@XmlElement(name = "integer", type = IntegerValueField.class),

	})
	public ValueField<?> renderer = new ObjectValueField();

	@XmlElement(name = "columns")
	public ValuesConfig columns = new ValuesConfig();

	@XmlElement(name = "rows")
	public ValuesConfig rows = new ValuesConfig();

	public String toXml() throws JAXBException {
		final StringWriter sw = new StringWriter();
		jaxbContext.createMarshaller().marshal(this, sw);
		return sw.toString();
	}

	private static final JAXBContext jaxbContext;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(Config.class);
		} catch (final JAXBException e) {
			throw new AssertionError(e);
		}
	}

	@SuppressWarnings("null")
	public static Config fromXml(final String xml) {
		try {
			return (@NonNull Config) jaxbContext.createUnmarshaller().unmarshal(new StringReader(xml));
		} catch (final JAXBException e) {
			//FIXME:
			throw new AssertionError(e);
		}
	}

	@XmlTransient
	public NotifyingList<ValueField<?>> getRendererAsNotifyingList() {
		return rendererList;
	}

	private final SingleNotifyingList rendererList = new SingleNotifyingList();

	private final class SingleNotifyingList extends NotifyingList<ValueField<?>> {
		@Override
		public void add(final int index, @NonNull final ValueField<?> object) {
			fireChange();
		}

		@Override
		public boolean add(@NonNull final ValueField<?> object) {
			if (renderer != object)
				return false;
			renderer = object;
			fireChange();
			return true;
		}

		@Override
		public void clear() {
			renderer = new ObjectValueField();
			fireChange();
		}

		@Override
		public @NonNull ValueField<?> remove(final int index) {
			assert index == 0;
			final ValueField<?> oldRenderer = renderer;
			renderer = new ObjectValueField();

			fireChange();
			return oldRenderer;
		}

		@Override
		public boolean remove(final @Nullable Object object) {
			renderer = new ObjectValueField();
			fireChange();
			return true;
		}

		@Override
		public int size() {
			assert renderer != null;
			return 1;
		}

		@Override
		public @NonNull ValueField<?> get(final int index) {
			assert renderer != null;
			return renderer;
		}
	}
}
