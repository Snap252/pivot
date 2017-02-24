package com.snap252.vaadin.pivot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.pivoting.PivotCriteria;
import com.snap252.org.pivoting.RootBucket;
import com.snap252.vaadin.pivot.GridRendererParameter.ParameterChangeListener.ParametersChangedEventArgs;
import com.snap252.vaadin.pivot.valuegetter.DummyAggregator;
import com.snap252.vaadin.pivot.valuegetter.ModelAggregtor;
import com.snap252.vaadin.pivot.xml.Config;
import com.snap252.vaadin.pivot.xml.bucketextractors.Attribute;

@NonNullByDefault
public final class GridRendererParameter<INPUT_TYPE, VALUE_TYPE> {
	private List<INPUT_TYPE> values = new ArrayList<>();
	private final PropertyProvider<INPUT_TYPE, ?> provider;

	enum GridRendererChangeParameterKind {
		ROW_FNKT, COL_FNKT, VALUES, AGGREGATOR/* , CONVERTER, RENDERER */

		;
	}

	public final Config config;

	public GridRendererParameter(final PropertyProvider<INPUT_TYPE, ?> provider) {
		this.provider = provider;

		try {
			config = Config.fromXml("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\r\n"
					+ "<config display-name='New Config'>\r\n" + "	<object name=''>\r\n"
					+ "		<aggregator xsi:type='countingAggregator' mode='cnt' null-representation='-' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'/>\r\n"
					+ "		<counting mode='cnt' null-representation='-'/>\r\n" + "	</object>\r\n"
					+ "	<columns colorize='false'><enum name='e2' display-name='2. Spalte!'/></columns>\r\n" + "	<rows colorize='false'>\r\n"
					+ "		<enum name='e1' display-name='1. Spalte!'/><enum name='e3' display-name='3. Spalte!'/>\r\n" + "	</rows>\r\n" + "</config>\r\n" + "");
		} catch (final Exception e) {
			throw new AssertionError(e);
		}

		config.rows.attributes.addChangeListener(_l -> fireEvent(GridRendererChangeParameterKind.ROW_FNKT));
		config.columns.attributes.addChangeListener(_l -> fireEvent(GridRendererChangeParameterKind.COL_FNKT));
		config.getRendererAsNotifyingList()
				.addChangeListener(_l -> fireEvent(GridRendererChangeParameterKind.AGGREGATOR));
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

	@Deprecated
	public <T> void setColFnkt(final List<? extends FilteringComponent<INPUT_TYPE, ?>> colFnkt) {
		System.out.println("GridRendererParameter.setColFnkt()");
		// this.colFnkt.clear();
		//
		// this.colFnkt.addAll(toPivotCriterias(colFnkt));
		// colFunctionsUpated();
	}

	private <@Nullable X> PivotCriteria<INPUT_TYPE, X> cast(final FilteringComponent<INPUT_TYPE, X> cf) {

		final Property<INPUT_TYPE, X> property = cf.getProperty();

		return new PivotCriteria<INPUT_TYPE, @Nullable X>() {
			@Override
			public @Nullable X apply(final INPUT_TYPE t) {
				return cf.rounded(t);
			}

			@Override
			public @Nullable String format(final X t) {
				return cf.format(t);
			}

			@Override
			public String toString() {
				return property.getName();
			}
		};
	}

	private Collection<PivotCriteria<INPUT_TYPE, @Nullable ?>> toPivotCriterias(
			final List<? extends FilteringComponent<INPUT_TYPE, @Nullable ?>> colFnkt) {
		return colFnkt.stream().map(t -> cast(t)).collect(Collectors.toList());
	}

	public RootBucket<INPUT_TYPE> creatRowBucket(final String SUM_TEXT) {
		final Function<Attribute<?>, FilteringComponent<INPUT_TYPE, ?>> mapper = x -> x
				.createFilteringComonent(provider);

		final List<PivotCriteria<INPUT_TYPE, ?>> collect = config.rows.attributes.stream()
				.map(mapper.andThen(this::cast)).collect(Collectors.toList());
		return new RootBucket<INPUT_TYPE>(SUM_TEXT, getValues(), collect);
	}

	public RootBucket<INPUT_TYPE> creatColBucket(final String SUM_TEXT) {
		final Function<Attribute<?>, FilteringComponent<INPUT_TYPE, ?>> mapper = x -> x
				.createFilteringComonent(provider);
		final List<PivotCriteria<INPUT_TYPE, ?>> collect = config.columns.attributes.stream()
				.map(mapper.andThen(this::cast)).collect(Collectors.toList());
		return new RootBucket<INPUT_TYPE>(SUM_TEXT, getValues(), collect);

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

	public void setModelAggregator(@Nullable final ModelAggregtor<INPUT_TYPE, ?> aggregator) {
		this.modelAggregator = aggregator != null ? aggregator : new DummyAggregator<INPUT_TYPE>();
		fireEvent(GridRendererChangeParameterKind.AGGREGATOR);
	}

}
