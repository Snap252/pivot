package com.snap252.vaadin.pivot;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.PivotCriteria;
import com.snap252.org.pivoting.RootBucket;
import com.snap252.vaadin.pivot.GridRendererParameter.ParameterChangeListener.ParametersChangedEventArgs;
import com.snap252.vaadin.pivot.valuegetter.DummyAggregator;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtor;

@NonNullByDefault
public final class GridRendererParameter<INPUT_TYPE, VALUE_TYPE> {
	private List<INPUT_TYPE> values = new ArrayList<>();
	private final PropertyProvider<INPUT_TYPE, ?> provider;

	enum GridRendererChangeParameterKind {
		ROW_FNKT, COL_FNKT, VALUES, AGGREGATOR, CONVERTER, RENDERER

		;
	}

	public GridRendererParameter(final PropertyProvider<INPUT_TYPE, ?> provider) {
		this.provider = provider;
	}

	public Collection<? extends Property<INPUT_TYPE, ?>> getProperties() {
		return provider.getProperties();
	}

	@FunctionalInterface
	public static interface ParameterChangeListener<LIST_INPUT_TYPE, VALUE_TYPE> {
		static class ParametersChangedEventArgs<LIST_INPUT_TYPE, VALUE_TYPE> {
			public final GridRendererChangeParameterKind whatChanged;
			public final GridRendererParameter<LIST_INPUT_TYPE, VALUE_TYPE> gridParameter;

			private ParametersChangedEventArgs(final GridRendererChangeParameterKind whatChanged,
					final GridRendererParameter<LIST_INPUT_TYPE, VALUE_TYPE> gridParameter) {
				this.whatChanged = whatChanged;
				this.gridParameter = gridParameter;
			}
		}

		void parametersChanged(ParametersChangedEventArgs<LIST_INPUT_TYPE, VALUE_TYPE> args);
	}

	public void setValues(final List<INPUT_TYPE> values) {
		if (Objects.equals(this.values, values))
			return;

		this.values = values;
		rowFunctionsUpated();
		fireEvent(GridRendererChangeParameterKind.COL_FNKT);
	}

	private List<INPUT_TYPE> getValues() {
		return values;
	}

	private final List<PivotCriteria<INPUT_TYPE, ?>> rowFnkt = new ArrayList<>();
	private final List<PivotCriteria<INPUT_TYPE, ?>> colFnkt = new ArrayList<>();

	public int getColDepth() {
		return colFnkt.size();
	}

	private ModelAggregtor<INPUT_TYPE, ?> modelAggregator = new DummyAggregator<INPUT_TYPE>();

	public ModelAggregtor<?, ?> getModelAggregator() {
		return modelAggregator;
	}

	public Collector<INPUT_TYPE, ?, ?> getCollector() {
		return modelAggregator.getAggregator();
	}

	private final Map<GridRendererChangeParameterKind, Collection<ParameterChangeListener<INPUT_TYPE, VALUE_TYPE>>> listeners = new EnumMap<>(
			GridRendererChangeParameterKind.class);

	public void addParameterChangeListener(final GridRendererChangeParameterKind kindOfChange,
			final ParameterChangeListener<INPUT_TYPE, VALUE_TYPE> listener) {
		this.listeners.computeIfAbsent(kindOfChange, x -> new LinkedList<>()).add(listener);
		assert this.listeners.containsKey(kindOfChange);
		assert this.listeners.get(kindOfChange) != null;
	}

	private void fireEvent(final GridRendererChangeParameterKind kindOfChange) {
		if (!listeners.containsKey(kindOfChange))
			return;

		final ParametersChangedEventArgs<INPUT_TYPE, VALUE_TYPE> args = new ParametersChangedEventArgs<>(kindOfChange,
				this);
		listeners.get(kindOfChange).forEach(listener -> listener.parametersChanged(args));
	}

	// private Object f(final LIST_INPUT_TYPE l, final Object object) {
	// return "";
	// }

	public <T> void setColFnkt(final List<? extends FilteringComponent<?, ?>> colFnkt) {
		this.colFnkt.clear();

		this.colFnkt.addAll(toPivotCriterias(colFnkt));
		colFunctionsUpated();
	}

	@SuppressWarnings("unchecked")
	private static <@Nullable T> T cast(final Object o) {
		return (T) o;
	}

	private Collection<PivotCriteria<INPUT_TYPE, Object>> toPivotCriterias(
			final List<? extends FilteringComponent<?, ?>> colFnkt) {
		return colFnkt.stream().map((Function<FilteringComponent<?, ?>, PivotCriteria<INPUT_TYPE, Object>>) cf -> {
			final Property<Object, ?> property = (Property<@NonNull Object, ?>) cf.getProperty();
			final String name = String.valueOf(property);
			return new PivotCriteria<INPUT_TYPE, Object>() {
				@Override
				public Object apply(final INPUT_TYPE t) {
					return cf.round(cast(property.getValue(t)));
				}

				@Override
				public String format(final Object t) {
					return cf.format(cast(t));
				}

				@Override
				public String toString() {
					return name;
				}
			};
		}).collect(toList());
	}

	public RootBucket<INPUT_TYPE> creatRowBucket(final String SUM_TEXT) {
		return new RootBucket<INPUT_TYPE>(SUM_TEXT, getValues(), rowFnkt);
	}

	public RootBucket<INPUT_TYPE> creatColBucket(final String SUM_TEXT) {
		return new RootBucket<INPUT_TYPE>(SUM_TEXT, getValues(), colFnkt);
	}

	public void setRowFnkt(final List<? extends FilteringComponent<?, ?>> rowFnkt) {
		this.rowFnkt.clear();
		this.rowFnkt.addAll(toPivotCriterias(rowFnkt));
		rowFunctionsUpated();
	}

	public void rowFunctionsUpated() {
		fireEvent(GridRendererChangeParameterKind.ROW_FNKT);
	}

	public void colFunctionsUpated() {
		fireEvent(GridRendererChangeParameterKind.COL_FNKT);
	}

	public void rendererUpated() {
		fireEvent(GridRendererChangeParameterKind.RENDERER);
	}

	public void aggregatorUpated() {
		fireEvent(GridRendererChangeParameterKind.AGGREGATOR);
	}

	public void setModelAggregator(@Nullable final ModelAggregtor<INPUT_TYPE, ?> aggregator) {
		this.modelAggregator = aggregator != null ? aggregator : new DummyAggregator<INPUT_TYPE>();
		fireEvent(GridRendererChangeParameterKind.AGGREGATOR);
	}

}
