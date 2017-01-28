package com.snap252.vaadin.pivot;

import java.util.List;
import java.util.function.Consumer;

import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class ValueGetterDnDHandler extends DropHandlerImplementation<FilteringComponent<?>> {

	private final ValueFactory valueFactory = new ValueFactory();

	public ValueGetterDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
			final Consumer<List<FilteringComponent<?>>> refresher) {
		super(cols, vertical, refresher);
	}

	@Override
	protected FilteringComponent<?> createNew(final Object data) {
		return valueFactory.createFilter((NameType) data);
	}

	@Override
	protected AbstractComponent createUIComponent(final FilteringComponent<?> createFilter) {
		final AbstractComponent component = createFilter.getComponent();

		final Button b;
		if (component != null) {
			final PopupButton popupButton = new PopupButton(createFilter.toString());
			final Button deleteButton = new Button("Entfernen", evt -> {
				removeFromList(popupButton.getParent(), createFilter, this);
				popupButton.setPopupVisible(false);
				refresh();
			});
			final Button closeButton = new Button("Schließen", evt -> popupButton.setPopupVisible(false));

			final HorizontalLayout footer = new HorizontalLayout(deleteButton, closeButton);
			footer.setSpacing(true);
			footer.setWidth("100%");
			footer.setComponentAlignment(deleteButton, Alignment.BOTTOM_LEFT);
			footer.setComponentAlignment(closeButton, Alignment.BOTTOM_RIGHT);

			final VerticalLayout verticalLayout = new VerticalLayout(component, footer);
			popupButton.setContent(verticalLayout);
			final PopupVisibilityListener listener = new PopupVisibilityListener() {
				@Override
				public void popupVisibilityChange(final PopupVisibilityEvent _ignore2) {
					refresh();
					/* we need a this-context here */
					popupButton.removePopupVisibilityListener(this);
					popupButton.setCaption(createFilter.toString());
				}
			};

			createFilter.addValueChangeListener(_ignore -> {
				popupButton.removePopupVisibilityListener(listener);
				popupButton.addPopupVisibilityListener(listener);
			});
			b = popupButton;
		} else
			b = new Button(createFilter.toString());
		return b;
	}
}
