package com.snap252.vaadin.pivot;

import java.util.List;

import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener;

import com.snap252.org.pivoting.PivotCriteria;
import com.vaadin.data.Item;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.ServerSideCriterion;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.DragAndDropWrapper.WrapperTargetDetails;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.dragdroplayouts.DDHorizontalLayout.HorizontalLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.DDVerticalLayout.VerticalLayoutTargetDetails;

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
			public boolean accept(DragAndDropEvent event) {
				return true;
			}
		};
	}

	@Override
	public void drop(DragAndDropEvent event) {
		final AbstractComponent sourceComponent = (AbstractComponent) event.getTransferable().getSourceComponent();
		final Object data = sourceComponent.getData();
		final int index;

		final TargetDetails targetDetails = event.getTargetDetails();
		if (targetDetails instanceof WrapperTargetDetails) {
			final WrapperTargetDetails wrappedTargetDetails = (WrapperTargetDetails) targetDetails;
			boolean first = !vertical ? wrappedTargetDetails.getHorizontalDropLocation() == HorizontalDropLocation.LEFT
					: wrappedTargetDetails.getVerticalDropLocation() == VerticalDropLocation.TOP;
			index = first ? 0 : -1;
		} else if (targetDetails instanceof VerticalLayoutTargetDetails) {
			final VerticalLayoutTargetDetails verticalLayoutTargetDetails = (VerticalLayoutTargetDetails) targetDetails;
			index = verticalLayoutTargetDetails.getOverIndex();

		} else if (targetDetails instanceof HorizontalLayoutTargetDetails) {
			final HorizontalLayoutTargetDetails horizontalLayoutTargetDetails = (HorizontalLayoutTargetDetails) targetDetails;
			index = horizontalLayoutTargetDetails.getOverIndex();
		} else
			index = -1;

		if (data instanceof NameType) {
			doWithFilteringComponent(ff.createFilter((NameType) data), index);
		} else {
			doWithFilteringComponent(handlerRemove(event), index);
		}
	}

	protected FilteringComponent<?> handlerRemove(DragAndDropEvent event) {
		AbstractComponent sourceComponent = (AbstractComponent) event.getTransferable().getSourceComponent();
		DragAndDropWrapper dndWrapper = (DragAndDropWrapper) event.getTransferable().getSourceComponent();
		AbstractComponent childButton = (AbstractComponent) dndWrapper.iterator().next();
		FilteringComponent<?> data2 = (FilteringComponent<?>) childButton.getData();
		DropHandlerImplementation pivotCriteriaList2 = (DropHandlerImplementation) sourceComponent.getData();
		removeFromList(sourceComponent, data2, pivotCriteriaList2);
		return data2;
	}

	protected static void removeFromList(Component sourceComponent, FilteringComponent<?> data2,
			DropHandlerImplementation pivotCriteriaList) {
		assert pivotCriteriaList.cols.getComponentIndex(sourceComponent) != -1;
		pivotCriteriaList.cols.removeComponent(sourceComponent);
		boolean changed = pivotCriteriaList.pivotCriteriaList.remove(data2);
		assert changed;
	}

	protected void doWithFilteringComponent(FilteringComponent<?> createFilter, int index) {
		final AbstractComponent component = createFilter.getComponent();
		final Button b;
		if (component != null) {
			final PopupButton popupButton = new PopupButton(createFilter.toString());
			final Button deleteButton = new Button("Entfernen", evt -> {
				removeFromList(popupButton.getParent(), createFilter, this);
				popupButton.setPopupVisible(false);
				refresher.run();
			});
			final Button closeButton = new Button("Schließen", evt -> popupButton.setPopupVisible(false));

			HorizontalLayout footer = new HorizontalLayout(deleteButton, closeButton);
			footer.setSpacing(true);
			footer.setWidth("100%");
			footer.setComponentAlignment(deleteButton, Alignment.BOTTOM_LEFT);
			footer.setComponentAlignment(closeButton, Alignment.BOTTOM_RIGHT);

			final VerticalLayout verticalLayout = new VerticalLayout(component, footer);
			popupButton.setContent(verticalLayout);
			final PopupVisibilityListener listener = new PopupVisibilityListener() {
				@Override
				public void popupVisibilityChange(PopupVisibilityEvent _ignore2) {
					refresher.run();
					/* we need a this-context here */
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
		if (index == -1)
			cols.addComponent(moveWrapper);
		else
			cols.addComponent(moveWrapper, index);

		moveWrapper.setDragStartMode(DragStartMode.COMPONENT);
		b.setData(createFilter);
		moveWrapper.setData(this);
		if (vertical) {
			b.setWidth("100%");
		}
		if (index == -1)
			pivotCriteriaList.add(createFilter);
		else
			pivotCriteriaList.add(index, createFilter);

		refresher.run();
	}
}