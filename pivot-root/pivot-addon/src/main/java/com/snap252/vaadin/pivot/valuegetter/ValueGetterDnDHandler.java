package com.snap252.vaadin.pivot.valuegetter;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener;

import com.snap252.vaadin.pivot.DropHandlerImplementation;
import com.snap252.vaadin.pivot.Property;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ValueGetterDnDHandler<INPUT_TYPE>
		extends DropHandlerImplementation<FilteringRenderingComponent<INPUT_TYPE, ?>> {

	private final ValueFactory valueFactory = new ValueFactory();
	private final Runnable upateAggregator;
	private final Runnable upateRenderer;

	public ValueGetterDnDHandler(final AbstractOrderedLayout cols, final boolean vertical,
			final Consumer<List<FilteringRenderingComponent<INPUT_TYPE, ?>>> refresher, final Runnable upateAggregator,
			final Runnable upateRenderer) {
		super(cols, vertical, refresher);
		this.upateAggregator = upateAggregator;
		this.upateRenderer = upateRenderer;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected FilteringRenderingComponent<INPUT_TYPE, ?> createNew(final Object data) {
		return valueFactory.createFilter((Property<INPUT_TYPE, @Nullable ?>) data);
	}

	@Override
	protected AbstractComponent createUIComponent(final FilteringRenderingComponent<INPUT_TYPE, ?> createFilter) {
		final AbstractComponent component = createFilter.getComponent();

		final Button b;
		if (component != null) {
			final PopupButton popupButton = new PopupButton(createFilter.toString());
			final Button deleteButton = new Button("Entfernen", evt -> {
				removeFromList(Objects.requireNonNull(popupButton.getParent()), createFilter, this);
				popupButton.setPopupVisible(false);
				upateAggregator.run();
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
						upateAggregator.run();
						/* we need a this-context here */
						popupButton.removePopupVisibilityListener(this);
						popupButton.setCaption(createFilter.toString());
						popupButton.addStyleName(createFilter.getButtonStyles());
					}
				};

				createFilter.addValueChangeListener(_ignore -> {
					popupButton.removePopupVisibilityListener(listener);
					popupButton.addPopupVisibilityListener(listener);
				});
			}

			{
				final PopupVisibilityListener listener = new PopupVisibilityListener() {
					@Override
					public void popupVisibilityChange(final PopupVisibilityEvent _ignore2) {
						upateRenderer.run();
						/* we need a this-context here */
						popupButton.removePopupVisibilityListener(this);
						popupButton.setCaption(createFilter.toString());
						popupButton.addStyleName(createFilter.getButtonStyles());
					}
				};

				createFilter.addRendererChangeListener(_ignore -> {
					popupButton.removePopupVisibilityListener(listener);
					popupButton.addPopupVisibilityListener(listener);
				});
			}
			b = popupButton;
		} else
			b = new Button(createFilter.toString());
		b.addStyleName(ValoTheme.BUTTON_SMALL);
		return b;
	}

}
