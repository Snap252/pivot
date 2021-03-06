package com.snap252.vaadin.pivot.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.xml.data.NotifyingList;
import com.snap252.vaadin.pivot.xml.renderers.ComparableValueField;
import com.snap252.vaadin.pivot.xml.renderers.DecimalValueField;
import com.snap252.vaadin.pivot.xml.renderers.IntegerValueField;
import com.snap252.vaadin.pivot.xml.renderers.ObjectValueField;
import com.snap252.vaadin.pivot.xml.renderers.SimpleObjectValueField;
import com.snap252.vaadin.pivot.xml.renderers.StringValueField;
import com.snap252.vaadin.pivot.xml.renderers.ValueField;

@XmlRootElement(name = "config")
public class Config {

	@XmlElements({ @XmlElement(name = "object", type = ObjectValueField.class),
			@XmlElement(name = "decimal", type = DecimalValueField.class),
			@XmlElement(name = "integer", type = IntegerValueField.class),
			@XmlElement(name = "string", type = StringValueField.class),
			@XmlElement(name = "comparable", type = ComparableValueField.class),

	})
	@Deprecated
	@Nullable
	public ValueField<?> renderer;

	@XmlElement(name = "columns")
	public ValuesConfig columns = new ValuesConfig();

	@XmlElement(name = "rows")
	public ValuesConfig rows = new ValuesConfig();

	public String toXml() {
		final StringWriter sw = new StringWriter();
		try {
			JAXBContext.newInstance(Config.class).createMarshaller().marshal(this, sw);

			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			// initialize StreamResult with File object to save to file
			final StreamResult result = new StreamResult(new StringWriter());
			final Source source = new StreamSource(new StringReader(sw.toString()));
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (final TransformerException | JAXBException e) {
			System.err.println(sw.toString());
			throw new AssertionError(e);
		}
	}

	// private static final JAXBContext JAXB_CONTEXT;
	// static {
	// try {
	// JAXB_CONTEXT = JAXBContext.newInstance(Config.class);
	// } catch (final JAXBException e) {
	// throw new AssertionError(e);
	// }
	// }

	@SuppressWarnings("null")
	public static Config fromXml(final String xml) {
		try {
			return (@NonNull Config) JAXBContext.newInstance(Config.class).createUnmarshaller()
					.unmarshal(new StringReader(xml));
		} catch (final JAXBException e) {
			// FIXME:
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
			assert index == 0;
			if (renderer == object)
				return;

			removeListenerIfSupported(renderer);
			renderer = object;
			addListenerIfSupported(object);
			fireChange();
		}

		@Override
		public boolean add(final ValueField<?> object) {
			if (renderer == object)
				return false;
			removeListenerIfSupported(renderer);
			renderer = object;
			addListenerIfSupported(object);
			fireChange();
			return true;
		}

		@Override
		public boolean addAll(@SuppressWarnings("null") final Collection<? extends @NonNull ValueField<?>> coll) {
			assert false;
			return super.addAll(coll);
		}

		@Override
		public void clear() {
			removeListenerIfSupported(renderer);
			renderer = new SimpleObjectValueField();
			fireChange();
		}

		@Override
		public @Nullable ValueField<?> remove(final int index) {
			assert index == 0;
			removeListenerIfSupported(renderer);
			final ValueField<?> oldRenderer = renderer;
			renderer = new SimpleObjectValueField();

			fireChange();
			return oldRenderer;
		}

		@Override
		public boolean remove(final @Nullable Object object) {
			assert object == renderer;
			removeListenerIfSupported(renderer);
			renderer = null;
			fireChange();
			return true;
		}

		@Override
		public int size() {
			return 1;
		}

		@Override
		public ValueField<?> get(final int index) {
			return renderer != null ? renderer : defaultRenderer;
		}

		@Override
		public Iterator<ValueField<?>> iterator() {
			if (renderer != null) {
				final Set<ValueField<?>> singleton = Collections.singleton((ValueField<?>) renderer);
				return singleton.iterator();
			} else {
				return Collections.emptyIterator();
			}
		}
	}

	private final SimpleObjectValueField defaultRenderer = new SimpleObjectValueField();

	public ValueField<?> getRenderer() {
		return renderer != null ? renderer : defaultRenderer;
	}

	@XmlTransient
	public void setAll(final Config newconfig) {
		columns.attributes.setAll(newconfig.columns.attributes);
		rows.attributes.setAll(newconfig.rows.attributes);
		final ValueField<?> renderer$ = newconfig.renderer;
		if (renderer$ != null)
			rendererList.add(renderer$);
	}
}
