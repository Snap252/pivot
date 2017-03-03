package com.snap252.vaadin.pivot.valuegetter;

import java.util.Objects;

import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener;

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

public class ValueGetterDnDHandler<INPUT_TYPE> extends DropHandlerImplementation<ValueField<?>> {

	private final ValueFactory valueFactory = new ValueFactory();

	public ValueGetterDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
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

		final Button b;
		if (component != null) {
			final PopupButton popupButton = new PopupButton(createFilter.getDisplayName());
			final Button deleteButton = new Button("Entfernen", evt -> {
				removeFromList(Objects.requireNonNull(popupButton.getParent()), createFilter, this);
				popupButton.setPopupVisible(false);
			});

			final Button closeButton = new Button("Schließen", evt -> popupButton.setPopupVisible(false));

			final HorizontalLayout footer = new HorizontalLayout(deleteButton, closeButton);
			footer.setSpacing(true);
			footer.setWidth("100%");
			footer.setComponentAlignment(deleteButton, Alignment.BOTTOM_LEFT);
			footer.setComponentAlignment(closeButton, Alignment.BOTTOM_RIGHT);

			final VerticalLayout verticalLayout = new VerticalLayout(component, footer);
			popupButton.setContent(verticalLayout);
			{
				final PopupVisibilityListener listener = new PopupVisibilityListener() {
					@Override
					public void popupVisibilityChange(final PopupVisibilityEvent _ignore2) {
						/* we need a this-context here */
						popupButton.removePopupVisibilityListener(this);
						popupButton.setCaption(createFilter.toString());
						// popupButton.addStyleName(createFilter.getButtonStyles());
					}
				};
			}

			b = popupButton;
		} else
			b = new Button(createFilter.getDisplayName());
		createFilter.addChangeListener(cl -> b.setCaption(cl.getDisplayName()));
		b.addStyleName(ValoTheme.BUTTON_SMALL);
		return b;
	}

}
