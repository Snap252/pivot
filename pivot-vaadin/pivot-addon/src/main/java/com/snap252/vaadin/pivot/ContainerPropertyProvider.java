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
public class ContainerPropertyProvider extends PropertyProvider<Item, ItemProperty<?>> {

	static class ItemProperty<OUTPUT_TYPE> extends Property<Item, @Nullable OUTPUT_TYPE> {
		private final Object itemPropertyId;

		public ItemProperty(final Class<@Nullable OUTPUT_TYPE> clazz, final String name, final Object itemPropertyId) {
			super(clazz, name);
			this.itemPropertyId = itemPropertyId;
		}

		@SuppressWarnings("unchecked")
		@Override
		public @Nullable OUTPUT_TYPE getValue(final Item o) {
			final com.vaadin.data.Property<?> itemProperty = o.getItemProperty(itemPropertyId);
			assert itemProperty != null : itemPropertyId + ": does not exist.";
			final Object rawValue = itemProperty.getValue();
			assert rawValue == null || clazz.isAssignableFrom(rawValue.getClass());
			return (@Nullable OUTPUT_TYPE) rawValue;
		}
	}

	private final Container c;

	public ContainerPropertyProvider(final Container c) {
		this.c = c;
	}

	@SuppressWarnings("null")
	@Override
	public Stream<Item> getItems() {
		return c.getItemIds().stream().map(c::getItem);
	}

	@SuppressWarnings("null")
	@Override
	public Collection<ItemProperty<?>> getProperties() {
		return c.getContainerPropertyIds().stream().map(propertyId -> {
			assert propertyId != null;
			final String lookUpName = lookUpName(propertyId);
			if (lookUpName != null)
				return new ItemProperty<>(c.getType(propertyId), lookUpName, propertyId);
			return null;
		}).filter(p -> p != null).collect(toList());
	}

	protected @Nullable String lookUpName(final Object o) {
		return o.toString();
	}
}
