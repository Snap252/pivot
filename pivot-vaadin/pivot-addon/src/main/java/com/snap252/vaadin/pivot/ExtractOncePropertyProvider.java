package com.snap252.vaadin.pivot;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.ExtractOncePropertyProvider.ObjectArrayProperty;
import com.snap252.vaadin.pivot.ExtractOncePropertyProvider.PropertyOnceItem;

@NonNullByDefault
public class ExtractOncePropertyProvider<Z> extends PropertyProvider<PropertyOnceItem, ObjectArrayProperty<?>> {
	private final PropertyProvider<Z, ?> pp;

	public static final class ObjectArrayProperty<OUTPUT_TYPE>
			extends Property<PropertyOnceItem, @Nullable OUTPUT_TYPE> {

		private final int index;

		public ObjectArrayProperty(final Class<@Nullable OUTPUT_TYPE> clazz, final String name, final int index) {
			super(clazz, name);
			this.index = index;
		}

		@SuppressWarnings("unchecked")
		@Override
		public @Nullable OUTPUT_TYPE getValue(final PropertyOnceItem o) {
			return (@Nullable OUTPUT_TYPE) o.internalArray[index];
		}
	}

	public static final class PropertyOnceItem {
		private final Object[] internalArray;

		private PropertyOnceItem(final Object[] internalArray) {
			this.internalArray = internalArray;
		}

	}

	public ExtractOncePropertyProvider(final PropertyProvider<Z, ?> pp) {
		this.pp = pp;
	}

	@Override
	public Collection<ObjectArrayProperty<?>> getProperties() {
		return pp.getProperties().stream().map(new Function<Property<?, ?>, ObjectArrayProperty<?>>() {
			int index = 0;

			@Override
			public ObjectArrayProperty<?> apply(final Property<?, ?> property) {
				return new ObjectArrayProperty<>(property.getType(), property.getName(), index++);
			}
		}).collect(Collectors.toList());
	}

	@Override
	public Stream<PropertyOnceItem> getItems() {
		final Collection<? extends Property<Z, ?>> properties = pp.getProperties();
		final Function<? super Z, ? extends PropertyOnceItem> toPropertyOnceItem = item -> new PropertyOnceItem(
				properties.stream().map(p -> p.getValue(item)).toArray());
		return pp.getItems().map(toPropertyOnceItem);
	}
}
