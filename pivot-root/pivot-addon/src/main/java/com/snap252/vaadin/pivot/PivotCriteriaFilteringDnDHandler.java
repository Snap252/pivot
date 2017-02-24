package com.snap252.vaadin.pivot;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@NonNullByDefault
public class PivotCriteriaFilteringDnDHandler extends DropHandlerImplementation<Attribute<?>> {

	private final AttributeFactory filterFactory = new AttributeFactory();

	public PivotCriteriaFilteringDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
			final List<Attribute<?>> refresher) {
		super(cols, vertical, refresher);
	}

	private void refresh() {
		// refererOfPropertyChanged.run();
	}

	@Override
	protected Attribute<?> createNew(final Object data) {
		return filterFactory.createAttribute((Property<?, @Nullable ?>) data);
	}

	@Override
	protected AbstractComponent createUIComponent(final Attribute<?> createFilter) {
		final UIConfigurable uiConfigurable = createFilter.createUIConfigurable();
		final AbstractComponent component = uiConfigurable.getComponent();

		final Button b;
		if (component != null) {
			final PopupButton popupButton = new PopupButton(createFilter.getDisplayName());
			final Button deleteButton = new Button("Entfernen", evt -> {
				removeFromList(Objects.requireNonNull(popupButton.getParent()), createFilter, this);
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

			b = popupButton;
		} else
			b = new Button(uiConfigurable.toString());
		b.addStyleName(ValoTheme.BUTTON_SMALL);
		return b;
	}

	protected UIConfigurable createUIConfigurable(final FilteringComponent<?, ?> filteringComponent) {
		return (UIConfigurable) filteringComponent;
	}
}
