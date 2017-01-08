package com.snap252.vaadin.pivot;

import java.util.List;

import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.ServerSideCriterion;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.DragAndDropWrapper.WrapperTargetDetails;
import com.vaadin.ui.VerticalLayout;

final class DropHandlerImplementation implements DropHandler {
	private final FilterFactory ff = new FilterFactory();

	private final AbstractOrderedLayout cols;
	private final boolean vertical;
	private final Runnable refresher;
	private final List<PivotCriteria<Item, ?>> pivotCriteriaList;

	public DropHandlerImplementation(AbstractOrderedLayout cols, boolean vertical, Runnable refresher,
			List<PivotCriteria<Item, ?>> pivotCriteriaList) {
		this.cols = cols;
		this.vertical = vertical;
		this.refresher = refresher;
		this.pivotCriteriaList = pivotCriteriaList;
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
		WrapperTargetDetails targetDetails = (WrapperTargetDetails) event.getTargetDetails();
		AbstractComponent sourceComponent = (AbstractComponent) event.getTransferable().getSourceComponent();
		Object data = sourceComponent.getData();
		boolean first = !vertical ? targetDetails.getHorizontalDropLocation() == HorizontalDropLocation.LEFT
				: targetDetails.getVerticalDropLocation() == VerticalDropLocation.TOP;

		if (data instanceof NameType) {
			doWithFilteringComponent(ff.createFilter((NameType) data), first);
		} else {
			doWithFilteringComponent(handlerRemove(event), first);
		}
	}

	protected FilteringComponent<?> handlerRemove(DragAndDropEvent event) {
		AbstractComponent sourceComponent = (AbstractComponent) event.getTransferable().getSourceComponent();
		DragAndDropWrapper dndWrapper = (DragAndDropWrapper) event.getTransferable().getSourceComponent();
		AbstractComponent childButton = (AbstractComponent) dndWrapper.iterator().next();
		FilteringComponent<?> data2 = (FilteringComponent<?>) childButton.getData();
		DropHandlerImplementation pivotCriteriaList2 = (DropHandlerImplementation) sourceComponent.getData();
		pivotCriteriaList2.cols.removeComponent(sourceComponent);

		boolean changed = pivotCriteriaList2.pivotCriteriaList.remove(data2);
		assert changed;
		return data2;
	}

	protected void doWithFilteringComponent(FilteringComponent<?> createFilter, boolean first) {
		final AbstractComponent component = createFilter.getComponent();
		final Button b;
		if (component != null) {
			final PopupButton popupButton = new PopupButton(createFilter.toString());
			final Button closeButton = new Button("Schließen", evt -> popupButton.setPopupVisible(false));
			final VerticalLayout verticalLayout = new VerticalLayout(component, closeButton);
			verticalLayout.setComponentAlignment(closeButton, Alignment.BOTTOM_RIGHT);
			popupButton.setContent(verticalLayout);
			final PopupVisibilityListener listener = new PopupVisibilityListener() {
				@Override
				public void popupVisibilityChange(PopupVisibilityEvent _ignore2) {
					refresher.run();
					/*we need a this-context here*/
					popupButton.removePopupVisibilityListener(this);
					popupButton.setCaption(createFilter.toString());
				}
			};
			
			createFilter.addValueChangeListener(_ignore -> {
				popupButton.removePopupVisibilityListener(listener);
				popupButton.addPopupVisibilityListener(listener);
			});
			b = popupButton;
		} else
			b = new Button(createFilter.toString());

		final DragAndDropWrapper moveWrapper = new DragAndDropWrapper(b);
		if (first)
			cols.addComponentAsFirst(moveWrapper);
		else
			cols.addComponent(moveWrapper);

		moveWrapper.setDragStartMode(DragStartMode.COMPONENT);
		b.setData(createFilter);
		moveWrapper.setData(this);
		if (vertical) {
			b.setWidth("100%");
		}
		if (first)
			pivotCriteriaList.add(0, createFilter);
		else
			pivotCriteriaList.add(createFilter);

		refresher.run();
	}
}