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

	public PivotUI(final GridRendererParameter<?, ?> gridRendererParameter) {
		this(PivotGrid::new, gridRendererParameter);
	}

	public <INPUT_TYPE> PivotUI(final Function<GridRendererParameter<INPUT_TYPE, ?>, Component> f,
			final GridRendererParameter<INPUT_TYPE, ?> gridRendererParameterx) {
		super(2, 3);
		// this.gridRendererParameter = gridRendererParameter;
		this.config = gridRendererParameterx.config;
		addStyleName("pivot");

		setSpacing(true);

		{
			final PopupButton renderer = new PopupButton("Export/Import");
			{
				renderer.setClosePopupOnOutsideClick(true);

				final VerticalLayout exportWindowContent = new VerticalLayout();
				final TextArea textArea = new TextArea();
				textArea.setRows(20);
				textArea.setColumns(50);
				renderer.addPopupVisibilityListener(_ignore -> textArea.setValue(config.toXml()));
				final Component fromXmlButton = new Button("Aus Xml",
						_ignore -> config.setAll(Config.fromXml(Objects.requireNonNull(textArea.getValue()))));

				final HorizontalLayout horizontalLayout = new HorizontalLayout(fromXmlButton);
				horizontalLayout.setComponentAlignment(fromXmlButton, Alignment.BOTTOM_RIGHT);
				horizontalLayout.setSizeFull();
				exportWindowContent.addComponents(textArea, horizontalLayout);
				renderer.setContent(exportWindowContent);
			}

			renderer.setSizeFull();
			properties = new HorizontalLayout();
			properties.setDescription("properties");
			properties.setCaption("properties");
			properties.setSpacing(true);
			final DragAndDropWrapper rowDndWrapper = new DragAndDropWrapper(properties);
			addComponents(renderer, rowDndWrapper);

			{
				final Component[] labels = gridRendererParameterx.getProperties().stream().map(propertyId -> {
					assert propertyId != null;
					final Component button = new Button(propertyId.toString());
					button.addStyleName(ValoTheme.BUTTON_SMALL);
					button.setEnabled(false);
					final DragAndDropWrapper wrapper = new DragAndDropWrapper(button);
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
			final DragAndDropWrapper aggregatorDragAndDropWrapper = new DragAndDropWrapper(aggregator);
			final DropHandler aggDopHandler = new ValueFieldDnDHandler(aggregator, true,
					config.getRendererAsNotifyingList());

			aggregator.setDropHandler(aggDopHandler);
			aggregatorDragAndDropWrapper.setDropHandler(aggDopHandler);

			final DDHorizontalLayout cols = new DDHorizontalLayout();
			cols.addStyleName("pivot-ui-cols");
			final DropHandler dropHandler = new AttributeDnDHandler(cols, false, config.columns.attributes);
			cols.setDropHandler(dropHandler);
			cols.setSpacing(true);

			final DragAndDropWrapper colDnDWrapper = new DragAndDropWrapper(cols);
			colDnDWrapper.setDropHandler(dropHandler);
			addComponents(aggregatorDragAndDropWrapper, colDnDWrapper);
		}

		{
			final DDVerticalLayout rows = new DDVerticalLayout();
			rows.addStyleName("pivot-ui-rows");

			final DropHandler dropHandler = new AttributeDnDHandler(rows, true, config.rows.attributes);

			rows.setDropHandler(dropHandler);
			rows.setSpacing(true);

			final DragAndDropWrapper rowDnDWrapper = new DragAndDropWrapper(rows);
			rowDnDWrapper.setDropHandler(dropHandler);
			rowDnDWrapper.setWidth(150, Unit.PIXELS);
			rowDnDWrapper.setHeight("100%");

			final Component pivotGrid$ = f.apply(gridRendererParameterx);
			pivotGrid$.setSizeFull();
			addComponents(rowDnDWrapper, pivotGrid$);
		}

		setRowExpandRatio(2, 1);
		setColumnExpandRatio(1, 1);
	}
}
