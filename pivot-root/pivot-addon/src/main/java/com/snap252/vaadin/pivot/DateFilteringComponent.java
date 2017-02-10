package com.snap252.vaadin.pivot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

@NonNullByDefault
public class DateFilteringComponent extends AbstractFilteringComponent<String> {
	private FormLayout comp;
	private ComboBox c;

	public DateFilteringComponent(NameType nt) {
		super(nt);
		FormLayout formLayout = new FormLayout();
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
	public String apply(Item item) {
		DateRounding value = (DateRounding) c.getValue();
		if (value == null)
			value = DateRounding.DAY;
		return value.df.format(super.apply(item));
	}

	@Override
	public int compare(String f1, String f2) {
		DateRounding value = (DateRounding) c.getValue();
		if (value == null)
			value = DateRounding.DAY;
		SimpleDateFormat df = value.df;
		try {
			return df.parse(f1).compareTo(df.parse(f2));
		} catch (ParseException e) {
			throw new AssertionError();
		}
	}

	@Override
	public void addValueChangeListener(ValueChangeListener l) {
		c.addValueChangeListener(l);
	}

	@Override
	public String toString() {
		DateRounding v = (DateRounding) c.getValue();
		if (v == null || v.s == null)
			return super.toString();
		return super.toString() + " (" + v.s + ")";
	}

}
