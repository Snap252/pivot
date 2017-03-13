package com.snap252.vaadin.pivot.xml.bucketextractors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.UIConfigurable;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class DateAttribute extends Attribute<@Nullable Date> {
	@XmlElements({ @XmlElement(name = "predefind", type = PredefinedDateFormat.class),
			@XmlElement(name = "custom", type = CustomDateFormat.class) })
	public ConfigDateFormat dateFormat = new PredefinedDateFormat();

	@Override
	protected Date roundImpl(final Date input) {
		// do this with doubleconvertion
		final DateFormat dateFormat = getDateFormat();
		try {
			return dateFormat.parse(dateFormat.format(input));
		} catch (final ParseException e) {
			throw new AssertionError();
		}
	}

	private DateFormat getDateFormat() {
		return this.dateFormat.getDateformat();
	}

	@Override
	protected String formatImpl(final Date input) {
		return getDateFormat().format(input);
	}

	@Override
	public UIConfigurable createUIConfigurable() {
		return new DateConfigurable();
	}

	private class DateConfigurable implements UIConfigurable {

		private final TabSheet comp;

		public DateConfigurable() {
			final TabSheet allTabSheet = new TabSheet(getWrapper("Allgemein", false, createForDisplayName(DateAttribute.this)),
					getWrapper("Format", false, getDateFormatConfig()));
			allTabSheet.setWidth("500px");
			allTabSheet.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
			this.comp = allTabSheet;
		}

		private TabSheet getDateFormatConfig() {
			final ComboBox combobox = new ComboBox("Auswahl", Arrays.asList(DateRounding.values()));
			// TODO: "apply from rounding to custom" - button
			final TextField textfield = new TextField("Eingabe");

			final TabSheet ts = new TabSheet(getWrapper("Vorgegebenes Format", true, combobox),
					getWrapper("Benutzerdefiniertes Format", true, textfield));
			ts.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
			ts.setSizeFull();
			if (dateFormat instanceof CustomDateFormat) {
				ts.setSelectedTab(1);

				final CustomDateFormat customDateFormat = (CustomDateFormat) dateFormat;
				textfield.setValue(customDateFormat.getDateFormatString());
			} else if (dateFormat instanceof PredefinedDateFormat) {
				ts.setSelectedTab(0);

				final PredefinedDateFormat predefinedDateFormat = (PredefinedDateFormat) dateFormat;
				combobox.select(predefinedDateFormat.dateRounding);
			}

			{
				combobox.setNullSelectionAllowed(true);

				combobox.addValueChangeListener(valueChangeEvent -> {
					final DateRounding rounding = (DateRounding) valueChangeEvent.getProperty().getValue();
					final PredefinedDateFormat pdf = new PredefinedDateFormat();
					pdf.dateRounding = Optional.ofNullable(rounding).orElse(DateRounding.DAY);
					if (dateFormat == pdf)
						return;

					dateFormat = pdf;
					fireChange();
				});
			}

			{

				textfield.setInputPrompt("dd.MM.yyyy");
				textfield.setImmediate(false);
				// TODO: do this with converter!!
				textfield.addValidator(value -> {
					try {
						new SimpleDateFormat((String) value);
					} catch (final IllegalArgumentException e) {
						throw new InvalidValueException(e.getMessage());
					}
				});

				textfield.addValueChangeListener(valueChangeEvent -> {
					final String textFormat = (String) valueChangeEvent.getProperty().getValue();
					assert textFormat != null;
					final CustomDateFormat cdf = new CustomDateFormat();
					cdf.setDateFormatString(textFormat);
					if (Objects.equals(dateFormat, cdf))
						return;

					dateFormat = cdf;
					fireChange();
				});
			}

			return ts;
		}

		@Override
		public @Nullable AbstractComponent getComponent() {
			return comp;
		}
	}

}
