package com.snap252.vaadin.pivot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.pivoting.PivotCriteria;
import com.snap252.org.pivoting.RootBucket;
import com.snap252.vaadin.pivot.GridRendererParameter.ParameterChangeListener.ParametersChangedEventArgs;
import com.snap252.vaadin.pivot.xml.Config;
import com.snap252.vaadin.pivot.xml.renderers.Aggregator;

@NonNullByDefault
public final class GridRendererParameter<INPUT_TYPE, VALUE_TYPE> {
	private List<INPUT_TYPE> values = new ArrayList<>();
	private final PropertyProvider<INPUT_TYPE, ?> provider;

	enum GridRendererChangeParameterKind {
		ROW_FNKT, COL_FNKT, VALUES, AGGREGATOR/* , CONVERTER, RENDERER */

		;
	}

	public final Config config = new Config();

	public GridRendererParameter(final PropertyProvider<INPUT_TYPE, ?> provider) {
		this.provider = provider;

		config.rows.attributes.addChangeListener((_l, self) -> fireEvent(GridRendererChangeParameterKind.ROW_FNKT));
		config.columns.attributes.addChangeListener((_l, self) -> fireEvent(GridRendererChangeParameterKind.COL_FNKT));
		config.getRendererAsNotifyingList()
				.addChangeListener((_l, self) -> fireEvent(GridRendererChangeParameterKind.AGGREGATOR));
	}

	public Collection<? extends Property<INPUT_TYPE, @NonNull ?>> getProperties() {
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
		// TODO: maybe exchange
		fireEvent(GridRendererChangeParameterKind.ROW_FNKT);
		fireEvent(GridRendererChangeParameterKind.COL_FNKT);
	}

	private List<INPUT_TYPE> getValues() {
		return values;
	}

	// private final List<PivotCriteria<INPUT_TYPE, ?>> rowFnkt = new
	// ArrayList<>();
	// private final List<PivotCriteria<INPUT_TYPE, ?>> colFnkt = new
	// ArrayList<>();

	public int getColDepth() {
		return config.columns.attributes.size();
	}

	// private final ModelAggregtor<INPUT_TYPE, ?> modelAggregator = new
	// DummyAggregator<INPUT_TYPE>();
	//
	// public ModelAggregtor<?, ?> getModelAggregator() {
	// return modelAggregator;
	// }

	public Aggregator<?, ?> getAggregator() {
		return config.getRenderer().getAggregator();
	}

	public Collector<INPUT_TYPE, ?, ?> getCollector() {
		return config.getRenderer().createMappingFunctionCriteria(provider);
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

	@Deprecated
	public <T> void setColFnkt(final List<? extends FilteringComponent<INPUT_TYPE, ?>> colFnkt) {
		System.out.println("GridRendererParameter.setColFnkt()");
		// this.colFnkt.clear();
		//
		// this.colFnkt.addAll(toPivotCriterias(colFnkt));
		// colFunctionsUpated();
	}

	public RootBucket<INPUT_TYPE> creatRowBucket(final String SUM_TEXT) {
		final List<PivotCriteria<INPUT_TYPE, ?>> collect = config.rows.attributes.stream()
				.map(x -> (PivotCriteria<INPUT_TYPE, ?>) x.createPivotCriteria(provider)).collect(Collectors.toList());
		return new RootBucket<INPUT_TYPE>(SUM_TEXT, getValues(), collect, true);
	}

	public RootBucket<INPUT_TYPE> creatColBucket(final String SUM_TEXT) {
		final List<PivotCriteria<INPUT_TYPE, ?>> collect = config.columns.attributes.stream()
				.map(x -> (PivotCriteria<INPUT_TYPE, ?>) x.createPivotCriteria(provider)).collect(Collectors.toList());
		return new RootBucket<INPUT_TYPE>(SUM_TEXT, getValues(), collect, false);
	}

	@Deprecated
	public void setRowFnkt(final List<? extends FilteringComponent<INPUT_TYPE, ?>> rowFnkt) {
		System.out.println("GridRendererParameter.setRowFnkt()");
		//
		// this.rowFnkt.clear();
		// this.rowFnkt.addAll(toPivotCriterias(rowFnkt));
		// rowFunctionsUpated();
	}

	// public void rowFunctionsUpated() {
	// fireEvent(GridRendererChangeParameterKind.ROW_FNKT);
	// }
	//
	// public void colFunctionsUpated() {
	// fireEvent(GridRendererChangeParameterKind.COL_FNKT);
	// }

	// public void rendererUpated() {
	// fireEvent(GridRendererChangeParameterKind.RENDERER);
	// }

	// public void aggregatorUpated() {
	// fireEvent(GridRendererChangeParameterKind.AGGREGATOR);
	// }

	// public void setModelAggregator(@Nullable final ModelAggregtor<INPUT_TYPE,
	// ?> aggregator) {
	// this.modelAggregator = aggregator != null ? aggregator : new
	// DummyAggregator<INPUT_TYPE>();
	// fireEvent(GridRendererChangeParameterKind.AGGREGATOR);
	// }

}
