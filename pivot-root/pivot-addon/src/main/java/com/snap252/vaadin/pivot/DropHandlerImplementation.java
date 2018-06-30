package com.snap252.vaadin.pivot;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.xml.data.NotifyingList;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.ServerSideCriterion;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.DragAndDropWrapper.WrapperTargetDetails;
import com.vaadin.ui.HasComponents;

import fi.jasoft.dragdroplayouts.DDHorizontalLayout.HorizontalLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.DDVerticalLayout.VerticalLayoutTargetDetails;

@NonNullByDefault
public abstract class DropHandlerImplementation<T> implements DropHandler {

	private final AbstractOrderedLayout orderedLayout;
	private final boolean vertical;
	private final List<T> currentElements;

	public DropHandlerImplementation(final AbstractOrderedLayout cols, final boolean vertical,
			final NotifyingList<T> currentElements) {
		this.orderedLayout = cols;
		this.vertical = vertical;
		this.currentElements = currentElements;
		updateUIElementsFromList(currentElements, true);
		currentElements.addChangeListener(this::updateUIElementsFromList);
	}

	protected void updateUIElementsFromList(final List<T> list, final boolean self) {
		if (!self)
			return;

		this.orderedLayout.removeAllComponents();
		list.forEach(this::updateUi);
		updateAllComponentIndices();
	}

	protected abstract Collection<String> getSupportedFlavors();

	@Override
	public AcceptCriterion getAcceptCriterion() {

		final Collection<String> supportedFlavors = getSupportedFlavors();
		// System.out.println("DropHandlerImplementation.getAcceptCriterion()");
		return new ServerSideCriterion() {

			@Override
			public boolean accept(final DragAndDropEvent dragEvent) {
				final Collection<String> dataFlavors = dragEvent.getTransferable().getDataFlavors();
				return supportedFlavors.stream().anyMatch(dataFlavors::contains);
			}
		};
		// return AcceptAll.get();
	}

	@Override
	public void drop(final DragAndDropEvent event) {

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

		final T data = createNew(event.getTransferable());
		if (data != null) {
			final Runnable compToRemove = (Runnable) event.getTransferable().getData("remove");
			if (compToRemove != null)
				compToRemove.run();
			doWithFilteringComponent(data, index);
		}
	}

	protected final @Nullable Property<?, ?> getDataFromDrop(final Transferable event) {
		return event.getDataFlavors().contains("property") ? (Property<?, ?>) event.getData("property") : null;
	}

	protected abstract T createNew(final Transferable data);

	protected void removeFromList(final T data2) {
		final boolean changed = this.currentElements.remove(data2);
		assert changed;
	}

	private void updateAllComponentIndices() {
		final HasComponents hc = orderedLayout;
		hc.forEach(new Consumer<Component>() {
			int index = 1;

			@Override
			public void accept(final Component c) {
				c.setStyleName("index-" + index++);
			}
		});
	}

	protected abstract AbstractComponent createUIComponent(T createFilter);

	protected final void doWithFilteringComponent(final T createFilter, final int index) {
		// updateUi(createFilter, index);
		if (index == -1)
			currentElements.add(createFilter);
		else
			currentElements.add(index, createFilter);
	}

	private void updateUi(final T createFilter) {
		final AbstractComponent uiComponent = createUIComponent(createFilter);

		final DragAndDropWrapperExtension moveWrapper = new DragAndDropWrapperExtension(uiComponent, "filter",
				createFilter);
		moveWrapper.put("remove", (Runnable) () -> removeFromList(createFilter));
		orderedLayout.addComponent(moveWrapper);
		moveWrapper.setDragStartMode(DragStartMode.COMPONENT);
		if (vertical) {
			uiComponent.setWidth("100%");
		}
	}
}