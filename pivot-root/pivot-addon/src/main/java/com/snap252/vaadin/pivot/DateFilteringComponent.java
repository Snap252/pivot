package com.snap252.vaadin.pivot;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

@NonNullByDefault
public class DateFilteringComponent extends AbstractFilteringComponent<Date> {
	private final FormLayout comp;
	private final ComboBox c;

	public DateFilteringComponent(final NameType nt) {
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

	//TODO: formatting
	@Override
	public Date round(final Date ret) {
		final DateFormat dateFormat = getDateFormat();
		try {
			return dateFormat.parse(dateFormat.format(ret));
		} catch ( final ParseException e) {
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
	public String format(final Date date) {
		return getDateFormat().format(date);
	}

//	@Override
//	public int compare(String f1, String f2) {
//		DateRounding value = (DateRounding) c.getValue();
//		if (value == null)
//			value = DateRounding.DAY;
//		SimpleDateFormat df = value.df;
//		try {
//			return df.parse(f1).compareTo(df.parse(f2));
//		} catch (ParseException e) {
//			throw new AssertionError();
//		}
//	}

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
