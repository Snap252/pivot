package com.snap252.vaadin.pivot;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@NonNullByDefault
public class PivotCriteriaFilteringDnDHandler<INPUT_TYPE>
		extends DropHandlerImplementation<FilteringComponent<INPUT_TYPE, ?>> {

	private final FilterFactory filterFactory = new FilterFactory();
	private final Runnable refererOfPropertyChanged;

	public PivotCriteriaFilteringDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
			final Consumer<List<FilteringComponent<INPUT_TYPE, ?>>> refresher,
			final Runnable refererOfPropertyChanged) {
		super(cols, vertical, refresher);
		this.refererOfPropertyChanged = refererOfPropertyChanged;
	}

	private void refresh() {
		refererOfPropertyChanged.run();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected FilteringComponent<INPUT_TYPE, ?> createNew(final Object data) {
		return filterFactory.createFilter((Property<INPUT_TYPE, @Nullable ?>) data);
	}

	@Override
	protected AbstractComponent createUIComponent(final FilteringComponent<INPUT_TYPE, ?> createFilter) {
		final UIConfigurable uiConfigurable = createUIConfigurable(createFilter);
		final AbstractComponent component = uiConfigurable.getComponent();

		final Button b;
		if (component != null) {
			final PopupButton popupButton = new PopupButton(uiConfigurable.toString());
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
			final PopupVisibilityListener listener = new PopupVisibilityListener() {
				@Override
				public void popupVisibilityChange(final PopupVisibilityEvent _ignore2) {
					refresh();
					/* we need a this-context here */
					popupButton.removePopupVisibilityListener(this);
					popupButton.setCaption(uiConfigurable.toString());
					popupButton.addStyleName(uiConfigurable.getButtonStyles());
				}
			};

			uiConfigurable.addValueChangeListener(_ignore -> {
				popupButton.removePopupVisibilityListener(listener);
				popupButton.addPopupVisibilityListener(listener);
			});
			b = popupButton;
		} else
			b = new Button(uiConfigurable.toString());
		b.addStyleName(ValoTheme.BUTTON_SMALL);
		return b;
	}

	protected UIConfigurable createUIConfigurable(final FilteringComponent<INPUT_TYPE, ?> filteringComponent) {
		return (UIConfigurable) filteringComponent;
	}
}
