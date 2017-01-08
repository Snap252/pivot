package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.miki.mapcontainer.MapContainer;

import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.org.pivoting.BiBucketParameter;
import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class PivotUI extends GridLayout {

	private final HorizontalLayout properties;
	private BiBucketParameter<Item> p;
	
	private final List<PivotCriteria<Item, ?>> rowFnkt = new ArrayList<>();
	private final List<PivotCriteria<Item, ?>> colFnkt = new ArrayList<>();

	final Collector<Item, MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> reducer = PivotCollectors
			.getReducer(item -> (BigDecimal) item.getItemProperty("wert").getValue(), new BigDecimalArithmetics());

	@SuppressWarnings("null")
	public PivotUI() {
		super(2, 3);
		setSpacing(true);
		PivotGrid pivotGrid$ = new PivotGrid();
		{
			Component renderer = new Label("renderer");
			renderer.setSizeUndefined();
			properties = new HorizontalLayout();
			properties.setCaption("properties");
			properties.setSpacing(true);
			DragAndDropWrapper rowDndWrapper = new DragAndDropWrapper(properties);
			addComponents(renderer, rowDndWrapper);
		}
		{
			Component aggregator = new Label("aggregator");
			aggregator.setSizeUndefined();

			HorizontalLayout cols = new HorizontalLayout();
			cols.setCaption("cols");
			cols.setSpacing(true);

			DragAndDropWrapper colDnDWrapper = new DragAndDropWrapper(cols);
			colDnDWrapper.setDropHandler(new DropHandlerImplementation(cols, false,
					() -> pivotGrid$.setContainerDataSource(p, reducer), colFnkt));
			addComponents(aggregator, colDnDWrapper);
		}

		{
			VerticalLayout rows = new VerticalLayout();
			rows.setCaption("rows");
			rows.setSpacing(true);

			pivotGrid$.setSizeFull();
			DragAndDropWrapper rowDnDWrapper = new DragAndDropWrapper(rows);
			rowDnDWrapper.setDropHandler(new DropHandlerImplementation(rows, true,
					() -> pivotGrid$.setContainerDataSource(p, reducer), rowFnkt));
			rowDnDWrapper.setWidth("250px");
			addComponents(rowDnDWrapper, pivotGrid$);
		}

		setRowExpandRatio(2, 1);
		setColumnExpandRatio(1, 1);
	}

	@SuppressWarnings("null")
	public void setContainerDataSource(Container c0) {
		Map<Object, Class<?>> m0 = c0.getContainerPropertyIds().stream()
				.collect(Collectors.toMap(Function.identity(), c0::getType));

		Map<Object, Map<Object, Object>> m1 = c0.getItemIds().stream()
				.collect(Collectors.toMap(Function.identity(), itemId -> {
					Item item = c0.getItem(itemId);
					return item.getItemPropertyIds().stream()
							.collect(toMap(Function.identity(), p -> item.getItemProperty(p).getValue()));
				}));
		Container c = new MapContainer(m0, m1);
		p = new BiBucketParameter<>(c.getItemIds().stream().map(c::getItem).collect(toList()), rowFnkt, colFnkt);

		Component[] labels = c.getContainerPropertyIds().stream().map(propertyId -> {
			Component button = new Button(propertyId.toString());
			button.setEnabled(false);
			DragAndDropWrapper wrapper = new DragAndDropWrapper(button);
			wrapper.setDragStartMode(DragStartMode.COMPONENT);
			wrapper.setData(new NameType(propertyId.toString(), c.getType(propertyId)));
			return wrapper;
		}).toArray(i -> new Component[i]);
		properties.removeAllComponents();
		properties.addComponents(labels);

	}

}
