package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.ContainerPropertyProvider.ItemProperty;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

@NonNullByDefault
public class ContainerPropertyProvider extends PropertyProvider<Item, ItemProperty> {

	class ItemProperty extends Property {
		private final Object itemId;

		public ItemProperty(final Class<?> clazz, final String name, final Object itemId) {
			super(clazz, name);
			this.itemId = itemId;
		}

	}

	private final Container c;

	ContainerPropertyProvider(final Container c) {
		this.c = c;
	}

	@SuppressWarnings("null")
	@Override
	public Stream<Item> getItems() {
		return c.getItemIds().stream().map(c::getItem);
	}

	@Override
	public Collection<ItemProperty> getProperties() {
		return c.getContainerPropertyIds().stream()
				.map(propertyId -> new ItemProperty(c.getType(propertyId), lookUpName(propertyId), propertyId))
				.collect(toList());
	}

	private String lookUpName(final Object o) {
		return o.toString();
	}

	@Override
	public @Nullable Object getValue(final Item item, final ItemProperty p) {
		final com.vaadin.data.Property<?> itemProperty = item.getItemProperty(p.itemId);
		assert itemProperty != null;
		return itemProperty.getValue();
	}

}