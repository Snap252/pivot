package com.snap252.vaadin.pivot;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.vaadin.pivot.i18n.MessageButton;
import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;
import com.snap252.vaadin.pivot.xml.data.NotifyingList;
import com.vaadin.event.Transferable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@NonNullByDefault
public class AttributeDnDHandler extends DropHandlerImplementation<Attribute<?>> {
	private final AttributeFactory filterFactory;

	public AttributeDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
			final NotifyingList<Attribute<?>> currentElements, final AttributeFactory filterFactory) {
		super(cols, vertical, currentElements);
		this.filterFactory = filterFactory;
	}
	
	public AttributeDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
			final NotifyingList<Attribute<?>> currentElements) {
		this(cols, vertical, currentElements, new AttributeFactory());
	}

	@Override
	protected Collection<String> getSupportedFlavors() {
		return Arrays.asList("property", "filter");
	}
	@Override
	protected Attribute<?> createNew(final Transferable data) {
		if (data.getDataFlavors().contains("filter"))
//			throw new IllegalArgumentException(
//					"not yet implemented:" + data.getData("filter") + "=>" + data.getData("filter").getClass());
			return (Attribute<?>) requireNonNull(data.getData("filter"));
		return filterFactory.createAttribute((Property<?, ?>) requireNonNull(data.getData("property")));
	}

	public void appendProprammatically(final Property<?, ?> data) {
		final Attribute<?> cn = filterFactory.createAttribute((Property<?, ?>) data);
		doWithFilteringComponent(cn, -1);
	}

	@Override
	protected AbstractComponent createUIComponent(final Attribute<?> createFilter) {
		final UIConfigurable uiConfigurable = createFilter.createUIConfigurable();
		final AbstractComponent component = uiConfigurable.getComponent();

		final PopupButton popupButton = new PopupButton(createFilter.getDisplayName());
		final Button deleteButton = new MessageButton("remove", evt -> {
			removeFromList(createFilter);
			popupButton.setPopupVisible(false);
		});
		deleteButton.addStyleName(ValoTheme.BUTTON_SMALL);
		final Button closeButton = new MessageButton("close", evt -> popupButton.setPopupVisible(false));
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
