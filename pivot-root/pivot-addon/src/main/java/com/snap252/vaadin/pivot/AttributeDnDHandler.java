package com.snap252.vaadin.pivot;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;
import com.snap252.vaadin.pivot.xml.data.NotifyingList;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@NonNullByDefault
public class AttributeDnDHandler extends DropHandlerImplementation<Attribute<?>> {

	private final AttributeFactory filterFactory = new AttributeFactory();

	public AttributeDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
			final NotifyingList<Attribute<?>> currentElements) {
		super(cols, vertical, currentElements);
	}

	@Override
	protected Attribute<?> createNew(final Object data) {
		return filterFactory.createAttribute((Property<?, @Nullable ?>) data);
	}

	@Override
	protected AbstractComponent createUIComponent(final Attribute<?> createFilter) {
		final UIConfigurable uiConfigurable = createFilter.createUIConfigurable();
		final AbstractComponent component = uiConfigurable.getComponent();

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

		// TODO: check self
		createFilter.addChangeListener((cl, self) -> popupButton.setCaption(cl.getDisplayName()));
		popupButton.addStyleName(ValoTheme.BUTTON_SMALL);
		return popupButton;
	}

	protected UIConfigurable createUIConfigurable(final FilteringComponent<?, ?> filteringComponent) {
		return (UIConfigurable) filteringComponent;
	}
}
