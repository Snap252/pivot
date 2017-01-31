package com.snap252.vaadin.pivot.valuegetter;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.NameType;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.renderers.TextRenderer;

public class ObjectValueExtractor
		implements FilteringRenderingComponent<ObjectStatistics>, Function<Item, @Nullable Object> {
	protected final NameType nameType;
	protected final Object propertyId;

	public ObjectValueExtractor(final NameType nameType) {
		this.nameType = nameType;
		this.propertyId = nameType.propertyId;
	}

	@Override
	public @Nullable AbstractComponent getComponent() {
		assert false;
		return null;
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collector<Item, ObjectStatistics, ObjectStatistics> getAggregator() {
		return Collector.of((Supplier<@NonNull ObjectStatistics>) () -> new ObjectStatistics(),
				(os1, os2) -> os1.add(apply(os2)), (os1, os2) -> os1.mergeTo(os2), Function.identity());
	}

	@SuppressWarnings("null")
	@Override
	public RendererConverter<?, ? extends @NonNull ObjectStatistics> createRendererConverter() {
		return new RendererConverter<String, ObjectStatistics>(new TextRenderer(), Object::toString, String.class);
	}

	@Override
	public void addRendererChangeListener(final ValueChangeListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public @Nullable Object apply(@NonNull final Item item) {
		return Objects.requireNonNull(item.getItemProperty(propertyId)).getValue();
	}

	@Override
	public @NonNull String toString() {
		return super.toString();
	}

}
