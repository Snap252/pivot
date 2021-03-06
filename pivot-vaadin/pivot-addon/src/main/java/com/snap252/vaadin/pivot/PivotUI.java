package com.snap252.vaadin.pivot;

import java.util.Objects;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.vaadin.hene.popupbutton.PopupButton;

import com.snap252.vaadin.pivot.valuegetter.ValueFieldDnDHandler;
import com.snap252.vaadin.pivot.xml.Config;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import fi.jasoft.dragdroplayouts.DDHorizontalLayout;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;

@NonNullByDefault
public class PivotUI extends GridLayout {

	private final HorizontalLayout properties;
	// private final GridRendererParameter<?, ?> gridRendererParameter;
	private final Config config;
	private Component grid;
	private DragAndDropWrapper rowDnDWrapper;
	private DragAndDropWrapper aggregatorDragAndDropWrapper;
	private DragAndDropWrapper rowDndWrapper2;
	private DragAndDropWrapper colComponent;

	public PivotUI(final GridRendererParameter<?, ?> gridRendererParameter, final AttributeFactory attributeFactory) {
		this(PivotGrid::new, gridRendererParameter, attributeFactory);
	}

	public <INPUT_TYPE> PivotUI(final Function<GridRendererParameter<INPUT_TYPE, ?>, PivotGridIfc> f,
			final GridRendererParameter<INPUT_TYPE, ?> gridRendererParameterx, final AttributeFactory attributeFactory) {
		super(2, 3);
		// this.gridRendererParameter = gridRendererParameter;
		this.config = gridRendererParameterx.config;
		addStyleName("pivot");

		setSpacing(true);

		final DDHorizontalLayout cols = new DDHorizontalLayout();
		final AttributeDnDHandler colDropHandler = new AttributeDnDHandler(cols, false, config.columns.attributes,
				attributeFactory);

		final DDVerticalLayout rows = new DDVerticalLayout();
		final AttributeDnDHandler rowDropHandler = new AttributeDnDHandler(rows, true, config.rows.attributes,
				attributeFactory);

		{
			final PopupButton renderer = new PopupButton("Export/Import");
			{
				renderer.setClosePopupOnOutsideClick(true);

				final VerticalLayout l = new VerticalLayout();
				final TextArea textArea = new TextArea();
				textArea.setRows(20);
				textArea.setColumns(50);
				renderer.addPopupVisibilityListener(_ignore -> textArea.setValue(config.toXml()));
				final Component fromXmlButton = new Button("Aus Xml",
						_ignore -> config.setAll(Config.fromXml(Objects.requireNonNull(textArea.getValue()))));

				final HorizontalLayout horizontalLayout = new HorizontalLayout(fromXmlButton);
				horizontalLayout.setComponentAlignment(fromXmlButton, Alignment.BOTTOM_RIGHT);
				horizontalLayout.setSizeFull();
				l.addComponents(textArea, horizontalLayout);
				renderer.setContent(l);
			}

			renderer.setSizeFull();
			properties = new HorizontalLayout();
			properties.setCaption("properties");
			rowDndWrapper2 = new DragAndDropWrapper(properties);
			// addComponents(renderer, rowDndWrapper);
			addComponents(new Label(), rowDndWrapper2);

			{
				final Component[] labels = gridRendererParameterx.getProperties().stream().map(propertyId -> {
					assert propertyId != null;
					final Component button = new Button(propertyId.toString(),
							btnEvt -> rowDropHandler.appendProprammatically(propertyId));
					button.addStyleName(ValoTheme.BUTTON_QUIET);
					button.addStyleName(ValoTheme.BUTTON_SMALL);
					// button.setEnabled(false);
					final DragAndDropWrapper wrapper = new DragAndDropWrapperExtension(button, propertyId);
					wrapper.setDragStartMode(DragStartMode.COMPONENT);
					wrapper.setData(propertyId);
					return wrapper;
				}).toArray(Component[]::new);
				properties.removeAllComponents();
				properties.addComponents(labels);
			}
		}

		{

			final DDVerticalLayout aggregator = new DDVerticalLayout();
			aggregator.setSpacing(true);
			aggregatorDragAndDropWrapper = new DragAndDropWrapper(aggregator);
			final DropHandler aggDopHandler = new ValueFieldDnDHandler(aggregator, true,
					config.getRendererAsNotifyingList());

			aggregator.setDropHandler(aggDopHandler);
			aggregatorDragAndDropWrapper.setDropHandler(aggDopHandler);

			cols.addStyleName("pivot-ui-cols");

			cols.setDropHandler(colDropHandler);
			cols.setSpacing(true);

			colComponent = new DragAndDropWrapper(cols);
			colComponent.setDropHandler(colDropHandler);
			addComponents(aggregatorDragAndDropWrapper, colComponent);
		}

		{
			rows.addStyleName("pivot-ui-rows");

			rows.setDropHandler(rowDropHandler);
			rows.setSpacing(true);

			rowDnDWrapper = new DragAndDropWrapper(rows);
			rowDnDWrapper.setDropHandler(rowDropHandler);
			rowDnDWrapper.setWidth(150, Unit.PIXELS);
			rowDnDWrapper.setHeight("100%");

			grid = f.apply(gridRendererParameterx);
			grid.setSizeFull();
			addComponents(rowDnDWrapper, grid);
		}

		setRowExpandRatio(2, 1);
		setColumnExpandRatio(1, 1);
	}

	public void setMaximized(final boolean expanded) {
		removeAllComponents();
		if (expanded) {
			addComponent(grid, 0, 0, 1, 2);
		} else {

			addComponents(new Label(), rowDndWrapper2, aggregatorDragAndDropWrapper, colComponent, rowDnDWrapper, grid);
		}
	}
}
