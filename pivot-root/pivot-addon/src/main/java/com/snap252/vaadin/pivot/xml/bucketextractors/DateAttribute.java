package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.DateRounding;
import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;

public class DateAttribute extends Attribute<@Nullable Date> {
	@XmlElements({ @XmlElement(name = "predefind", type = PredefinedDateFormat.class),
			@XmlElement(name = "custom", type = CustomDateFormat.class) })
	public ConfigDateFormat dateFormat = new PredefinedDateFormat();

	@Override
	protected Date roundImpl(final Date input) {
		// do this with doubleconvertion
		final DateFormat dateFormat = this.dateFormat.getDateformat();
		try {
			return dateFormat.parse(dateFormat.format(input));
		} catch (final ParseException e) {
			throw new AssertionError();
		}
	}

	@Override
	protected UIConfigurable createUIConfigurable() {
		return new DateConfigurable();
	}

	private class DateConfigurable implements UIConfigurable {
		private final TabSheet comp;

		public DateConfigurable() {
			final TabSheet ts = new TabSheet();
			final ComboBox c = new ComboBox("Rundung", Arrays.asList(DateRounding.values()));
			//TODO: "apply from rounding to custom" - button
			final TextField tf = new TextField("Benutzerdefiniertes Format", "yyyy-MM-dd");
			tf.setImmediate(true);
			ts.addComponents(c, tf);
			c.setNullSelectionItemId(DateRounding.DAY);
			c.setNullSelectionAllowed(false);

			c.addValueChangeListener(System.err::println);
			tf.addValueChangeListener(System.err::println);

			this.comp = ts;
		}

		@Override
		public @Nullable AbstractComponent getComponent() {
			return comp;
		}

		@Override
		public void addValueChangeListener(final ValueChangeListener valueChangeListener) {
		}

	}
}
