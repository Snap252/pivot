package com.snap252.vaadin.pivot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.PivotCriteria;
import com.snap252.org.pivoting.RootBucket;
import com.snap252.vaadin.pivot.GridRendererParameter.ParameterChangeListener.ParametersChangedEventArgs;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtor;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtorDelegate;

@NonNullByDefault
public final class GridRendererParameter<LIST_INPUT_TYPE> {
	private List<LIST_INPUT_TYPE> values = new ArrayList<>();

	enum GridRendererChangeParameterKind {
		ROW_FNKT, COL_FNKT, VALUES, AGGREGATOR, CONVERTER, RENDERER

		;
	}

	@FunctionalInterface
	public static interface ParameterChangeListener<LIST_INPUT_TYPE> {
		static class ParametersChangedEventArgs<LIST_INPUT_TYPE> {
			public final GridRendererChangeParameterKind whatChanged;
			public final GridRendererParameter<LIST_INPUT_TYPE> gridParameter;

			private ParametersChangedEventArgs(final GridRendererChangeParameterKind whatChanged,
					final GridRendererParameter<LIST_INPUT_TYPE> gridParameter) {
				this.whatChanged = whatChanged;
				this.gridParameter = gridParameter;
			}
		}

		void parametersChanged(ParametersChangedEventArgs<LIST_INPUT_TYPE> args);
	}

	public void setValues(final List<LIST_INPUT_TYPE> values) {
		if (Objects.equals(this.values, values))
			return;

		this.values = values;
		rowFunctionsUpated();
		fireEvent(GridRendererChangeParameterKind.COL_FNKT);
	}

	private List<LIST_INPUT_TYPE> getValues() {
		return values;
	}

	private final List<PivotCriteria<LIST_INPUT_TYPE, ?>> rowFnkt = new ArrayList<>();
	private final List<PivotCriteria<LIST_INPUT_TYPE, ?>> colFnkt = new ArrayList<>();

	public int getColDepth() {
		return colFnkt.size();
	}

	private ModelAggregtor<?> modelAggregator = new ModelAggregtorDelegate.DummyAggregator();

	public ModelAggregtor<?> getModelAggregator() {
		return modelAggregator;
	}

	private final Map<GridRendererChangeParameterKind, Collection<ParameterChangeListener<LIST_INPUT_TYPE>>> listeners = new EnumMap<>(
			GridRendererChangeParameterKind.class);

	public void addParameterChangeListener(final GridRendererChangeParameterKind kindOfChange,
			final ParameterChangeListener<LIST_INPUT_TYPE> listener) {
		this.listeners.computeIfAbsent(kindOfChange, x -> new LinkedList<>()).add(listener);
		assert this.listeners.containsKey(kindOfChange);
		assert this.listeners.get(kindOfChange) != null;
	}

	private void fireEvent(final GridRendererChangeParameterKind kindOfChange) {
		if (!listeners.containsKey(kindOfChange))
			return;

		final ParametersChangedEventArgs<LIST_INPUT_TYPE> args = new ParametersChangedEventArgs<>(kindOfChange, this);
		listeners.get(kindOfChange).forEach(listener -> listener.parametersChanged(args));
	}

	public void setColFnkt(final List<? extends PivotCriteria<LIST_INPUT_TYPE, ?>> colFnkt) {
		if (Objects.equals(colFnkt, this.colFnkt))
			return;

		this.colFnkt.clear();
		this.colFnkt.addAll(colFnkt);
		fireEvent(GridRendererChangeParameterKind.COL_FNKT);
	}

	public RootBucket<LIST_INPUT_TYPE> creatRowBucket(final String SUM_TEXT) {
		return new RootBucket<LIST_INPUT_TYPE>(SUM_TEXT, getValues(), rowFnkt);
	}

	public RootBucket<LIST_INPUT_TYPE> creatColBucket(final String SUM_TEXT) {
		return new RootBucket<LIST_INPUT_TYPE>(SUM_TEXT, getValues(), colFnkt);
	}

	public void setRowFnkt(final List<? extends PivotCriteria<LIST_INPUT_TYPE, ?>> rowFnkt) {
		if (Objects.equals(rowFnkt, this.rowFnkt))
			return;

		this.rowFnkt.clear();
		this.rowFnkt.addAll(rowFnkt);
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

	public void setModelAggregator(@Nullable final ModelAggregtor<?> aggregator) {
		this.modelAggregator = aggregator != null ? aggregator : new ModelAggregtorDelegate.DummyAggregator();
		fireEvent(GridRendererChangeParameterKind.AGGREGATOR);
	}

	// public void Supplier<Stream
}
