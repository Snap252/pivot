package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.vaadin.miki.mapcontainer.MapContainer;

import com.snap252.vaadin.pivot.valuegetter.ValueGetterDnDHandler;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import fi.jasoft.dragdroplayouts.DDHorizontalLayout;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;

@NonNullByDefault
public class PivotUI extends GridLayout {

	private final HorizontalLayout properties;

	public PivotUI(final GridRendererParameter<?> gridRendererParameter) {
		super(2, 3);

		setSpacing(true);
		final PivotGrid pivotGrid$ = new PivotGrid(gridRendererParameter);
		{
			final Component renderer = new Label("");
			renderer.setSizeUndefined();
			properties = new HorizontalLayout();
			properties.setCaption("properties");
			properties.setSpacing(true);
			final DragAndDropWrapper rowDndWrapper = new DragAndDropWrapper(properties);
			addComponents(renderer, rowDndWrapper);
		}
		{

			final DDVerticalLayout aggregator = new DDVerticalLayout();
			aggregator.setSpacing(true);
			final DragAndDropWrapper aggregatorDragAndDropWrapper = new DragAndDropWrapper(aggregator);
			final DropHandler aggDopHandler = new ValueGetterDnDHandler(aggregator, true, i -> {
				gridRendererParameter.setModelAggregator(!i.isEmpty() ? i.get(0) : null);
			}, gridRendererParameter::aggregatorUpated, gridRendererParameter::rendererUpated);

			aggregator.setDropHandler(aggDopHandler);
			aggregatorDragAndDropWrapper.setDropHandler(aggDopHandler);

			final DDHorizontalLayout cols = new DDHorizontalLayout();
			cols.addStyleName("pivot-ui-cols");
			final DropHandler dropHandler = new PivotCriteriaFilteringDnDHandler(cols, false,
					gridRendererParameter::setColFnkt, gridRendererParameter::colFunctionsUpated);
			cols.setDropHandler(dropHandler);
			cols.setSpacing(true);

			final DragAndDropWrapper colDnDWrapper = new DragAndDropWrapper(cols);
			colDnDWrapper.setDropHandler(dropHandler);
			addComponents(aggregatorDragAndDropWrapper, colDnDWrapper);
		}

		{
			final DDVerticalLayout rows = new DDVerticalLayout();
			rows.addStyleName("pivot-ui-rows");
			final DropHandler dropHandler = new PivotCriteriaFilteringDnDHandler(rows, true,
					gridRendererParameter::setRowFnkt, gridRendererParameter::rowFunctionsUpated);
			rows.setDropHandler(dropHandler);
			rows.setSpacing(true);

			final DragAndDropWrapper rowDnDWrapper = new DragAndDropWrapper(rows);
			rowDnDWrapper.setDropHandler(dropHandler);
			rowDnDWrapper.setWidth(150, Unit.PIXELS);
			rowDnDWrapper.setHeight("100%");

			pivotGrid$.setSizeFull();
			addComponents(rowDnDWrapper, pivotGrid$);
		}

		setRowExpandRatio(2, 1);
		setColumnExpandRatio(1, 1);
	}

	public List<Item> setContainerDataSource(final Container container) {

		// p = new
		// BiBucketParameter<>(c.getItemIds().stream().map(c::getItem).collect(toList()),
		// Collections.emptyList(),
		// Collections.emptyList());

		final Component[] labels = container.getContainerPropertyIds().stream().map(propertyId -> {
			final Component button = new Button(propertyId.toString());
			button.addStyleName(ValoTheme.BUTTON_SMALL);
			button.setEnabled(false);
			final DragAndDropWrapper wrapper = new DragAndDropWrapper(button);
			wrapper.setDragStartMode(DragStartMode.COMPONENT);
			wrapper.setData(new NameType(propertyId.toString(), container.getType(propertyId)));
			return wrapper;
		}).toArray(i -> new Component[i]);
		properties.removeAllComponents();
		properties.addComponents(labels);

		return container.getItemIds().stream().map(container::getItem).collect(toList());

	}

	public static Container cloneContainer(final Container origContainer) {
		final Map<Object, Class<?>> m0 = origContainer.getContainerPropertyIds().stream()
				.collect(Collectors.toMap(Function.identity(), origContainer::getType, (u, v) -> {
					throw new IllegalStateException(String.format("Duplicate key %s", u));
				}, LinkedHashMap::new));

		final Map<Object, Map<Object, Object>> m1 = origContainer.getItemIds().stream()
				.collect(Collectors.toMap(Function.identity(), itemId -> {
					final Item item = origContainer.getItem(itemId);
					final LinkedHashMap<Object, Object> v = new LinkedHashMap<>();
					item.getItemPropertyIds().forEach(p -> v.put(p, item.getItemProperty(p).getValue()));
					return v;
				}, (u, v) -> {
					throw new IllegalStateException(String.format("Duplicate key %s", u));
				}, LinkedHashMap::new));
		return new MapContainer(m0, m1);
	}

}
