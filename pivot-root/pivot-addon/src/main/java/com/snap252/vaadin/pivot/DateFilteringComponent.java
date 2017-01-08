package com.snap252.vaadin.pivot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

@NonNullByDefault
public class DateFilteringComponent extends AbstractFilteringComponent<String> {
	private FormLayout comp;
	private ComboBox c;

	public DateFilteringComponent(NameType nt) {
		super(nt);
		FormLayout formLayout = new FormLayout();
		c = new ComboBox("Rundung", Arrays.asList(Rounding.values()));
		c.setNullSelectionItemId(Rounding.DAY);
		c.setNullSelectionAllowed(false);
		System.err.println(c.getContainerPropertyIds());

		formLayout.addComponent(c);
		formLayout.setWidth("400px");
		formLayout.addComponent(new Button("Close"));
		this.comp = formLayout;
	}

	@Override
	public @NonNull AbstractComponent getComponent(PopupButton b) {
		return comp;
	}

	enum Rounding {
		YEAR(new SimpleDateFormat("yyyy"), "Jahr"),

		MONTH_VERY_SHORT(new SimpleDateFormat("MM/yyyy"), "Monat(sehr kurz)"),
		
		MONTH_SHORT(new SimpleDateFormat("MMM / yyyy"), "Monat(kurz)"),
		
		MONTH_LONG(new SimpleDateFormat("MMMM yyyy"), "Monat(lang)"),

		MONTH_ONLY_SHORT(new SimpleDateFormat("MMM"), "nur Monat (kurz)"),
		
		MONTH_ONLY_LONG(new SimpleDateFormat("MMMM"), "nur Monat (lang)"),

		WEEK(new SimpleDateFormat("'KW' w yyyy"), "Woche"),

		WEEK_ONLY(new SimpleDateFormat("'KW' w"), "nur Woche"),

		DAY(new SimpleDateFormat("dd.MM.yyyy"), "Tag"),

		DAY_ONLY(new SimpleDateFormat("dd"), "nur Tag"),

		DAY_MONTH(new SimpleDateFormat("dd.MM."), "Tag/Monat"),

		HOUR_MINUTE(new SimpleDateFormat("HH:mm"), "Stunde / Minute"),

		HOUR(new SimpleDateFormat("HH"), "Stunde"),

		MINUTE(new SimpleDateFormat("mm"), "Minute"),

		ALL(new SimpleDateFormat("HH:mm dd.MM.yyyy"), null),

		;
		private final SimpleDateFormat df;
		private final @Nullable String s;

		Rounding(SimpleDateFormat df, @Nullable String s) {
			this.df = df;
			this.s = s;
		}

		@Override
		public String toString() {
			if (s != null)
				return s;
			return "";
		}
	}

	@SuppressWarnings("null")
	@Override
	public PivotCriteria<Item, String> getCriteria() {
		@NonNull
		PivotCriteria<Item, String> pivotCriteria = new PivotCriteria<Item, String>() {
			@Override
			public @NonNull String apply(Item item) {
				Rounding value = (Rounding) c.getValue();
				if (value == null)
					value = Rounding.DAY;

				return value.df.format((Date) item.getItemProperty(propertyId).getValue());
			}

			@Override
			public int compare(String f1, String f2) {
				Rounding value = (Rounding) c.getValue();
				if (value == null)
					value = Rounding.DAY;
				SimpleDateFormat df = value.df;
				try {
					return df.parse(f1).compareTo(df.parse(f2));
				} catch (ParseException e) {
					throw new AssertionError();
				}
			}
		};
		return pivotCriteria;
	}

	@Override
	public void addValueChangeListener(ValueChangeListener l) {
		c.addValueChangeListener(l);
	}

	@Override
	public String toString() {
		Rounding v = (Rounding) c.getValue();
		if (v == null || v.s == null)
			return super.toString();
		return super.toString() + " (" + v.s + ")";
	}
}
