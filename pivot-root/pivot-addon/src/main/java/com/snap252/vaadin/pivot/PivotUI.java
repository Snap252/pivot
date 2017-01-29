package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.miki.mapcontainer.MapContainer;

import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NullableArithmeticsWrapper;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.org.pivoting.BiBucketParameter;
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

import fi.jasoft.dragdroplayouts.DDHorizontalLayout;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;

@NonNullByDefault
public class PivotUI extends GridLayout {

	private final HorizontalLayout properties;
	private BiBucketParameter<Item> p;

	private final Collector<Item, MutableValue<@Nullable BigDecimal>, @Nullable NumberStatistics<@Nullable BigDecimal>> reducer = PivotCollectors
			.<@NonNull Item, @Nullable BigDecimal>getNumberReducer(this::apply,
					new NullableArithmeticsWrapper<>(new BigDecimalArithmetics()));

	@Nullable
	private FilteringComponent<?> valueProperty = null;
	
	private @Nullable Runnable propertyRefresher;

	private @Nullable BigDecimal apply(final Item item) {
		if (valueProperty != null)
			return (@NonNull BigDecimal) valueProperty.apply(item);
		return null;
	}

	@SuppressWarnings("null")
	public PivotUI() {
		super(2, 3);
		setSpacing(true);
		final PivotGrid pivotGrid$ = new PivotGrid();
		{
			final Component renderer = new Label("renderer");
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
				if (!i.isEmpty())
					valueProperty = i.get(0);
				else
					valueProperty = null;

				if (propertyRefresher != null)
					propertyRefresher.run();
				// propertyRefresher = pivotGrid$.setContainerDataSource(p,
				// reducer);
			});

			aggregator.setDropHandler(aggDopHandler);
			aggregatorDragAndDropWrapper.setDropHandler(aggDopHandler);

			final DDHorizontalLayout cols = new DDHorizontalLayout();
			final DropHandler dropHandler = new PivotCriteriaFilteringDnDHandler(cols, false,
					colFnkts -> propertyRefresher = pivotGrid$.setContainerDataSource(p.setColFnkt(colFnkts), reducer));
			cols.setDropHandler(dropHandler);
			cols.setSpacing(true);

			final DragAndDropWrapper colDnDWrapper = new DragAndDropWrapper(cols);
			colDnDWrapper.setDropHandler(dropHandler);
			addComponents(aggregatorDragAndDropWrapper, colDnDWrapper);
		}

		{
			final DDVerticalLayout rows = new DDVerticalLayout();
			final DropHandler dropHandler = new PivotCriteriaFilteringDnDHandler(rows, true,
					rowFnkts -> propertyRefresher = pivotGrid$.setContainerDataSource(p.setRowFnkt(rowFnkts), reducer));
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

	@SuppressWarnings("null")
	public void setContainerDataSource(final Container origContainer) {
		final Map<Object, Class<?>> m0 = origContainer.getContainerPropertyIds().stream()
				.collect(Collectors.toMap(Function.identity(), origContainer::getType, (u, v) -> {
					throw new IllegalStateException(String.format("Duplicate key %s", u));
				}, LinkedHashMap::new));

		final Map<Object, Map<Object, Object>> m1 = origContainer.getItemIds().stream()
				.collect(Collectors.toMap(Function.identity(), itemId -> {
					final Item item = origContainer.getItem(itemId);
					return item.getItemPropertyIds().stream()
							.collect(toMap(Function.identity(), p -> item.getItemProperty(p).getValue(), (u, v) -> {
								throw new IllegalStateException(String.format("Duplicate key %s", u));
							}, LinkedHashMap::new));
				}, (u, v) -> {
					throw new IllegalStateException(String.format("Duplicate key %s", u));
				}, LinkedHashMap::new));
		final Container c = new MapContainer(m0, m1);
		p = new BiBucketParameter<>(c.getItemIds().stream().map(c::getItem).collect(toList()), Collections.emptyList(),
				Collections.emptyList());

		final Component[] labels = c.getContainerPropertyIds().stream().map(propertyId -> {
			final Component button = new Button(propertyId.toString());
			button.setEnabled(false);
			final DragAndDropWrapper wrapper = new DragAndDropWrapper(button);
			wrapper.setDragStartMode(DragStartMode.COMPONENT);
			wrapper.setData(new NameType(propertyId.toString(), c.getType(propertyId)));
			return wrapper;
		}).toArray(i -> new Component[i]);
		properties.removeAllComponents();
		properties.addComponents(labels);

	}

}
