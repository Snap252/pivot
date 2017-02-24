package com.snap252.vaadin.pivot;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.DragAndDropWrapper.WrapperTargetDetails;
import com.vaadin.ui.HasComponents;

import fi.jasoft.dragdroplayouts.DDHorizontalLayout.HorizontalLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.DDVerticalLayout.VerticalLayoutTargetDetails;

@NonNullByDefault
public abstract class DropHandlerImplementation<T> implements DropHandler {

	private final AbstractOrderedLayout cols;
	private final boolean vertical;
	private final List<T> currentElements;

	public DropHandlerImplementation(final AbstractOrderedLayout cols, final boolean vertical,
			final List<T> currentElements) {
		this.cols = cols;
		this.vertical = vertical;
		this.currentElements = currentElements;
		currentElements.forEach(element -> updateUi(element, -1));
	}

	@Override
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	@Override
	public void drop(final DragAndDropEvent event) {
		final AbstractComponent sourceComponent = (AbstractComponent) event.getTransferable().getSourceComponent();
		final Object data = sourceComponent.getData();
		final int index;

		final TargetDetails targetDetails = event.getTargetDetails();
		if (targetDetails instanceof WrapperTargetDetails) {
			final WrapperTargetDetails wrappedTargetDetails = (WrapperTargetDetails) targetDetails;
			final boolean first = !vertical
					? wrappedTargetDetails.getHorizontalDropLocation() == HorizontalDropLocation.LEFT
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

		if (data instanceof Property) {
			doWithFilteringComponent(createNew(data), index);
		} else {
			doWithFilteringComponent(handlerRemove(event), index);
		}
	}

	protected abstract T createNew(final Object data);

	@SuppressWarnings("unchecked")
	protected T handlerRemove(final DragAndDropEvent event) {
		final AbstractComponent sourceComponent = (AbstractComponent) event.getTransferable().getSourceComponent();
		final DragAndDropWrapper dndWrapper = (DragAndDropWrapper) event.getTransferable().getSourceComponent();
		final AbstractComponent childButton = (AbstractComponent) dndWrapper.iterator().next();
		final T data2 = (T) childButton.getData();
		final DropHandlerImplementation<T> pivotCriteriaList2 = (DropHandlerImplementation<T>) sourceComponent
				.getData();
		assert pivotCriteriaList2 != null;
		removeFromList(sourceComponent, data2, pivotCriteriaList2);
		return data2;
	}

	protected static <T> void removeFromList(final Component sourceComponent, final T data2,
			final DropHandlerImplementation<T> pivotCriteriaList) {
		assert pivotCriteriaList.cols.getComponentIndex(sourceComponent) != -1;
		pivotCriteriaList.cols.removeComponent(sourceComponent);
		pivotCriteriaList.updateAllComponentIndices();

		final boolean changed = pivotCriteriaList.currentElements.remove(data2);
		assert changed;
	}

	private void updateAllComponentIndices() {
		final HasComponents hc = cols;
		hc.forEach(new Consumer<Component>() {
			int index = 1;

			@Override
			public void accept(final Component c) {
				c.setStyleName("index-" + index++);
			}
		});
	}

	protected abstract AbstractComponent createUIComponent(T createFilter);

	private final void doWithFilteringComponent(final T createFilter, final int index) {
		updateUi(createFilter, index);
		if (index == -1)
			currentElements.add(createFilter);
		else
			currentElements.add(index, createFilter);
	}

	private void updateUi(final T createFilter, final int index) {
		final AbstractComponent uiComponent = createUIComponent(createFilter);

		final DragAndDropWrapper moveWrapper = new DragAndDropWrapper(uiComponent);
		if (index == -1)
			cols.addComponent(moveWrapper);
		else
			cols.addComponent(moveWrapper, index);

		updateAllComponentIndices();

		moveWrapper.setDragStartMode(DragStartMode.COMPONENT);
		uiComponent.setData(createFilter);
		moveWrapper.setData(this);
		if (vertical) {
			uiComponent.setWidth("100%");
		}
	}
}