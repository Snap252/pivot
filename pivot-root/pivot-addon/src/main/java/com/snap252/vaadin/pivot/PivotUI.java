package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener;
import org.vaadin.miki.mapcontainer.MapContainer;

import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.org.pivoting.BiBucketParameter;
import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.ServerSideCriterion;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class PivotUI extends GridLayout {
	private final FilterFactory ff = new FilterFactory();

	private final class DropHandlerImplementation implements DropHandler {
		private AbstractOrderedLayout cols;
		private boolean fullWidth;
		private Consumer<PivotCriteria<Item, ?>> c;

		public DropHandlerImplementation(AbstractOrderedLayout cols, boolean fullWidth,
				Consumer<PivotCriteria<Item, ?>> c) {
			this.cols = cols;
			this.fullWidth = fullWidth;
			this.c = c;
		}

		@Override
		public AcceptCriterion getAcceptCriterion() {
			return new ServerSideCriterion() {

				@Override
				public boolean accept(DragAndDropEvent dragEvent) {
					return true;
				}
			};
		}

		@Override
		public void drop(DragAndDropEvent event) {
			AbstractComponent sourceComponent = (AbstractComponent) event.getTransferable().getSourceComponent();
			Object data = sourceComponent.getData();
			FilteringComponent<?> createFilter = ff.createFilter((NameType) data);
			PopupButton button = new PopupButton(createFilter.toString());
			@Nullable
			AbstractComponent component = createFilter.getComponent(button);
			if (component != null) {
				button.setContent(component);
				PopupVisibilityListener listener = new PopupVisibilityListener() {
					@Override
					public void popupVisibilityChange(PopupVisibilityEvent _ignore2) {
						c.accept(null);
						button.removePopupVisibilityListener(this);
						button.setCaption(createFilter.toString());
					}
				};
				createFilter.addValueChangeListener(_ignore -> {
					button.removePopupVisibilityListener(listener);
					button.addPopupVisibilityListener(listener);
				});
			}

			if (fullWidth)
				// TODO: make it work
				button.setWidth("100%");
			cols.addComponent(button);
			c.accept(createFilter.getCriteria());
		}
	}

	public final PivotGrid pivot;
	private HorizontalLayout properties;
	private BiBucketParameter<Item> p;

	final Collector<Item, MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> reducer = PivotCollectors
			.getReducer(item -> (BigDecimal) item.getItemProperty("wert").getValue(), new BigDecimalArithmetics());

	@SuppressWarnings("null")
	public PivotUI() {
		super(2, 3);
		PivotGrid pivotGrid$ = new PivotGrid();
		{
			Component renderer = new Label("renderer");
			renderer.setSizeUndefined();
			properties = new HorizontalLayout();
			properties.setDescription("properties");
			properties.setSpacing(true);
			addComponents(renderer, new DragAndDropWrapper(properties));
		}
		{
			Component aggregator = new Label("aggregator");
			aggregator.setSizeUndefined();

			HorizontalLayout cols = new HorizontalLayout(new Label("cols"));
			cols.setSpacing(true);

			DragAndDropWrapper colDnDWrapper = new DragAndDropWrapper(cols);
			colDnDWrapper.setDropHandler(new DropHandlerImplementation(cols, false, x -> {
				if (x != null)
					p.colFnkt.add(x);
				pivotGrid$.setContainerDataSource(p, reducer);
			}));
			addComponents(aggregator, colDnDWrapper);
		}

		{
			VerticalLayout rows = new VerticalLayout(new Label("rows"));
			rows.setSpacing(true);
			rows.setSizeUndefined();

			pivotGrid$.setSizeFull();
			DragAndDropWrapper rowDnDWrapper = new DragAndDropWrapper(rows);
			rowDnDWrapper.setDropHandler(new DropHandlerImplementation(rows, true, x -> {
				if (x != null)
					p.rowFnkt.add(x);
				pivotGrid$.setContainerDataSource(p, reducer);
			}));
			addComponents(rowDnDWrapper, pivotGrid$);
			this.pivot = pivotGrid$;
		}
	}

	@SuppressWarnings("null")
	public void setContainerDataSource(Container c0) {
		Map<Object, Class<?>> m0 = c0.getContainerPropertyIds().stream()
				.collect(Collectors.toMap(Function.identity(), p -> c0.getType(p)));

		Map<Object, Map<Object, Object>> m1 = c0.getItemIds().stream()
				.collect(Collectors.toMap(Function.identity(), itemId -> {
					Item item = c0.getItem(itemId);
					return item.getItemPropertyIds().stream()
							.collect(toMap(Function.identity(), p -> item.getItemProperty(p).getValue()));
				}));
		Container c = new MapContainer(m0, m1);
		p = new BiBucketParameter<>(c.getItemIds().stream().map(c::getItem).collect(toList()));

		Component[] labels = c.getContainerPropertyIds().stream().map(propertyId -> {
			Component button = new Button(propertyId.toString());
			button.setEnabled(false);
			DragAndDropWrapper wrapper = new DragAndDropWrapper(button);
			wrapper.setDragStartMode(DragStartMode.COMPONENT);
			wrapper.setData(new NameType(propertyId.toString(), c.getType(propertyId)));
			return wrapper;
		}).toArray(i -> new Component[i]);
		properties.addComponents(labels);

	}

}
