package com.snap252.vaadin.pivot.valuegetter;

import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.vaadin.pivot.DropHandlerImplementation;
import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.xml.data.NotifyingList;
import com.snap252.vaadin.pivot.xml.renderers.ValueField;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ValueFieldDnDHandler extends DropHandlerImplementation<ValueField<?>> {

	private final ValueFactory valueFactory = new ValueFactory();

	public ValueFieldDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
			final NotifyingList<ValueField<?>> refresher) {
		super(cols, vertical, refresher);
	}

	@SuppressWarnings("null")
	@Override
	protected ValueField<?> createNew(final Object data) {
		return valueFactory.createFilter((Property<?, ?>) data);
	}

	@Override
	protected AbstractComponent createUIComponent(final ValueField<?> createFilter) {
		final AbstractComponent component = createFilter.createUIConfigurable().getComponent();

		final PopupButton popupButton = new PopupButton(createFilter.getDisplayName());
		final Button deleteButton = new Button("Entfernen", evt -> {
			removeFromList(createFilter, this);
			popupButton.setPopupVisible(false);
		});
		deleteButton.addStyleName(ValoTheme.BUTTON_SMALL);

		final Button closeButton = new Button("Schließen", evt -> popupButton.setPopupVisible(false));
		closeButton.addStyleName(ValoTheme.BUTTON_SMALL);

		final HorizontalLayout footer = new HorizontalLayout(deleteButton, closeButton);
		footer.setSpacing(true);
		footer.setWidth("100%");
		footer.setComponentAlignment(deleteButton, Alignment.BOTTOM_LEFT);
		footer.setComponentAlignment(closeButton, Alignment.BOTTOM_RIGHT);

		popupButton.setContent(component != null ? new VerticalLayout(component, footer) : footer);

		// TODO: check self?
		createFilter.addChangeListener((cl, self) -> popupButton.setCaption(cl.getDisplayName()));
		popupButton.addStyleName(ValoTheme.BUTTON_SMALL);
		return popupButton;
	}

}
