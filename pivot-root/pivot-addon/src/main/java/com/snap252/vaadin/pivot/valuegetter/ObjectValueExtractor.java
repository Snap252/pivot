package com.snap252.vaadin.pivot.valuegetter;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.NameType;
import com.snap252.vaadin.pivot.renderer.BigDecimalRenderer;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

public class ObjectValueExtractor
		implements FilteringRenderingComponent<ObjectStatistics> {
	protected final NameType nameType;
	protected final Object propertyId;
	private final AbstractComponent comp;

	private final ComboBox howToRenderComboBox = new ComboBox("Anzeige",
			Arrays.asList(WhatOfObjectStatisticsToShow.values()));
	private WhatOfObjectStatisticsToShow whatToRender = WhatOfObjectStatisticsToShow.cnt;

	public ObjectValueExtractor(final NameType nameType) {
		this.nameType = nameType;
		this.propertyId = nameType.propertyId;
		final FormLayout formLayout = new FormLayout();

		howToRenderComboBox.setNullSelectionAllowed(false);
		howToRenderComboBox.setValue(WhatOfObjectStatisticsToShow.cnt);

		howToRenderComboBox.addValueChangeListener(value -> {
			this.whatToRender = (WhatOfObjectStatisticsToShow) requireNonNull(value.getProperty().getValue());
		});

		formLayout.addComponents(howToRenderComboBox);
		formLayout.setWidth("400px");
		this.comp = formLayout;
	}

	@Override
	public @NonNull AbstractComponent getComponent() {
		return comp;
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener valueChangeListener) {
		howToRenderComboBox.addValueChangeListener(valueChangeListener);
	}

	@Override
	public Collector<Object, ObjectStatistics, ObjectStatistics> getAggregator(final BiFunction<Object, Object, Object> f) {
		return Collector.of((Supplier<@NonNull ObjectStatistics>) () -> new ObjectStatistics(),
				(os1, os2) -> os1.add(f.apply(os2, propertyId)), (os1, os2) -> os1.mergeTo(os2), Function.identity());
	}

	@SuppressWarnings("null")
	@Override
	public RendererConverter<?, ? extends @NonNull ObjectStatistics> createRendererConverter() {
		final BigDecimalRenderer renderer = new BigDecimalRenderer("---");
		renderer.setFormat("0");
		final Function<@Nullable ObjectStatistics, BigDecimal> singleExtractor = t -> t == null ? null
				: whatToRender.getValueAsBigDecimal(t);

		return new RendererConverter<BigDecimal, ObjectStatistics>(renderer, x -> singleExtractor.apply(x.getValue()),
				BigDecimal.class);
	}

	@Override
	public void addRendererChangeListener(final ValueChangeListener l) {
		// TODO Auto-generated method stub

	}


	@Override
	public String toString() {
		return nameType.propertyId.toString();
	}

}
