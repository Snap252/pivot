package com.snap252.vaadin.pivot;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

@NonNullByDefault
public class DateFilteringComponent<INPUT_TYPE> extends AbstractFilteringComponent<INPUT_TYPE, @Nullable Date> {
	private final FormLayout comp;
	private final ComboBox c;

	public DateFilteringComponent(final Property<INPUT_TYPE, @Nullable Date> nt) {
		super(nt);
		final FormLayout formLayout = new FormLayout();
		c = new ComboBox("Rundung", Arrays.asList(DateRounding.values()));
		c.setNullSelectionItemId(DateRounding.DAY);
		c.setNullSelectionAllowed(false);

		formLayout.addComponent(c);
		formLayout.setWidth("400px");
		this.comp = formLayout;
	}

	@Override
	public @NonNull AbstractComponent getComponent() {
		return comp;
	}

	@Override
	public @Nullable Date round(final @Nullable Date ret) {
		if (ret == null) {
			return null;
		}
		final DateFormat dateFormat = getDateFormat();
		try {
			return dateFormat.parse(dateFormat.format(ret));
		} catch (final ParseException e) {
			throw new AssertionError();
		}
	}

	private DateFormat getDateFormat() {
		DateRounding value = (DateRounding) c.getValue();
		if (value == null)
			value = DateRounding.DAY;
		return value.df;
	}

	@Override
	public @Nullable String format(final Date date) {
		if (date == null)
			return null;
		return getDateFormat().format(date);
	}

	// @Override
	// public int compare(String f1, String f2) {
	// DateRounding value = (DateRounding) c.getValue();
	// if (value == null)
	// value = DateRounding.DAY;
	// SimpleDateFormat df = value.df;
	// try {
	// return df.parse(f1).compareTo(df.parse(f2));
	// } catch (ParseException e) {
	// throw new AssertionError();
	// }
	// }

	@Override
	public void addValueChangeListener(final ValueChangeListener l) {
		c.addValueChangeListener(l);
	}

	@Override
	public String toString() {
		final DateRounding v = (DateRounding) c.getValue();
		if (v == null || v.s == null)
			return super.toString();
		return super.toString() + " (" + v.s + ")";
	}

}
