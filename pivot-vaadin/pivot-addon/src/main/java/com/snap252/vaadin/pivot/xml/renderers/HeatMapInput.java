package com.snap252.vaadin.pivot.xml.renderers;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.client.Color;
import com.snap252.vaadin.pivot.client.Gradient;
import com.snap252.vaadin.pivot.xml.renderers.RelativeStasticsAggregator.NumberStatisticsConfig.ColorPickerField;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractColorPicker.Coordinates2Color;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.colorpicker.ColorPickerGradient;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

public class HeatMapInput extends HorizontalLayout {
	private static final Object COLOR_PROPERTY = "COLOR";
	private static final Object VALUE_PROPERTY = "VALUE";
	private final Indexed indexed = new IndexedContainer();
	private final Grid grid = new Grid() {
		@Override
		protected void doEditItem() {
			super.doEditItem();
			final FieldGroup editorFieldGroup = grid.getEditorFieldGroup();
			final AbstractTextField valueEditField = (AbstractTextField) editorFieldGroup.getField(VALUE_PROPERTY);
			valueEditField.focus();
			valueEditField.selectAll();
		};
	};

	@SuppressWarnings({ "null", "unchecked" })
	public HeatMapInput() {
		indexed.addContainerProperty(COLOR_PROPERTY, Color.class, null);
		indexed.addContainerProperty(VALUE_PROPERTY, Double.class, 0);
		grid.setContainerDataSource(indexed);

		final Converter<String, Color> colorToHtmlConverter = new Converter<String, Color>() {

			@Override
			public Color convertToModel(final String value, final Class<? extends Color> targetType,
					final @Nullable Locale locale) {
				assert false;
				return new Color();
			}

			@Override
			public String convertToPresentation(final Color value, final Class<? extends String> targetType,
					final @Nullable Locale locale) {
				return "<div style='background-color:" + value.toRGBACssString(255)
						+ ";width:100%;height: 90%;border-radius:5px;'/>";
			}

			@Override
			public Class<Color> getModelType() {
				return Color.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}

		};

		final Column colorColumn = grid.getColumn(COLOR_PROPERTY);
		colorColumn.setEditable(true);
		colorColumn.setRenderer(new HtmlRenderer(), colorToHtmlConverter);
		colorColumn.setWidth(100);
		colorColumn.setResizable(false);

		final Button addButton = new Button(FontAwesome.PLUS);
		addButton.addStyleName(ValoTheme.BUTTON_SMALL);
		addButton.setEnabled(false);

		final ColorPickerGradient colorPickerGradient = new ColorPickerGradient("rgb-gradient",
				new Coordinates2Color() {
					@Override
					public com.vaadin.shared.ui.colorpicker.Color calculate(final int x, final int y) {
						final float h = (x / 220f);
						float s = 1f;
						float v = 1f;

						if (y < 110) {
							s = y / 110f;
						} else if (y > 110) {
							v = 1f - (y - 110f) / 110f;
						}

						return new com.vaadin.shared.ui.colorpicker.Color(
								com.vaadin.shared.ui.colorpicker.Color.HSVtoRGB(h, s, v));
					}

					@Override
					public int[] calculate(final com.vaadin.shared.ui.colorpicker.Color color) {

						final float[] hsv = color.getHSV();

						final int x = Math.round(hsv[0] * 220f);
						int y = 0;

						// lower half
						if (hsv[1] == 1f) {
							y = Math.round(110f - (hsv[1] + hsv[2]) * 110f);
						} else {
							y = Math.round(hsv[1] * 110f);
						}

						return new int[] { x, y };
					}
				});
		colorPickerGradient.addColorChangeListener(event -> addButton.setEnabled(true));

		grid.setEditorFieldFactory(new DefaultFieldGroupFieldFactory() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public <T extends Field> T createField(final Class<?> type, final Class<T> fieldType) {
				if (type == Color.class) {
					return (T) new ColorPickerField(colorPickerGradient);
				}
				return super.createField(type, fieldType);
			}
		});

		addButton.addClickListener(evt -> {
			try {
				grid.saveEditor();
			} catch (final CommitException e) {
				grid.cancelEditor();
			}

			final Object itemId = indexed.addItem();
			final com.vaadin.shared.ui.colorpicker.Color color2 = colorPickerGradient.getColor();

			indexed.getContainerProperty(itemId, COLOR_PROPERTY)
					.setValue(new Color(color2.getRed(), color2.getGreen(), color2.getBlue()));
			grid.editItem(itemId);
		});

		final Button removeButton = new Button(FontAwesome.MINUS);
		removeButton.addStyleName(ValoTheme.BUTTON_SMALL);
		removeButton.addClickListener(btnEvt -> {
			final Collection<Object> selectedRows = grid.getSelectedRows();
			if (!selectedRows.isEmpty()) {
				@NonNull
				final Object firstSelected = selectedRows.iterator().next();
				Object nextOrLastItemId = indexed.nextItemId(firstSelected);
				if (nextOrLastItemId == null)
					nextOrLastItemId = indexed.lastItemId();

				selectedRows.forEach(indexed::removeItem);
				if (nextOrLastItemId != null)
					grid.select(nextOrLastItemId);
			} else {
				final Object firstItemId = indexed.firstItemId();

				if (firstItemId != null) {
					Object nextOrLastItemId = indexed.nextItemId(firstItemId);
					if (nextOrLastItemId == null)
						nextOrLastItemId = indexed.lastItemId();
					if (nextOrLastItemId != null)
						grid.select(nextOrLastItemId);
					indexed.removeItem(firstItemId);
				}

			}
		});

		final HorizontalLayout buttons = new HorizontalLayout(addButton, removeButton);
		buttons.setSpacing(true);
		final VerticalLayout left = new VerticalLayout(buttons, colorPickerGradient);
		left.setSpacing(true);
		setSpacing(true);
		addComponents(left, grid);
		setExpandRatio(grid, 1);
		grid.setEditorEnabled(true);
		grid.sort(VALUE_PROPERTY);
		// setHeight(280, Unit.PIXELS);
		setHeightUndefined();
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(evt -> {
			final Set<Object> selected = evt.getSelected();
			assert selected.size() <= 1;
			if (selected.isEmpty())
				return;

			final Object itemId = selected.iterator().next();

			final Property<Color> containerProperty = indexed.getContainerProperty(itemId, COLOR_PROPERTY);
			assert containerProperty != null;

			final Color c = containerProperty.getValue();
			colorPickerGradient.setColor(new com.vaadin.shared.ui.colorpicker.Color(c.toRGBA()));
		});
	}

	@SuppressWarnings({ "null", "unchecked" })
	public void setValue(@Nullable final Gradient gradient) {
		indexed.removeAllItems();

		if (gradient == null)
			return;

		final Color[] colors = gradient.colors;
		final double[] fractions = gradient.fractions;
		for (int j = 0; j < colors.length; j++) {
			final Object itemId = indexed.addItem();
			assert itemId != null;
			indexed.getContainerProperty(itemId, COLOR_PROPERTY).setValue(colors[j]);
			indexed.getContainerProperty(itemId, VALUE_PROPERTY).setValue(fractions[j]);
		}
	}

	public @Nullable Gradient getGradient() {
		final Collection<@NonNull ?> itemIds = indexed.getItemIds();
		if (itemIds.isEmpty())
			return null;

		final Color[] colors = new Color[itemIds.size()];
		final double[] fractions = new double[itemIds.size()];
		itemIds.stream().map(indexed::getItem).sorted(Comparator.comparing(HeatMapInput::getValue))
				.forEach(new Consumer<Item>() {
					int i = 0;

					@Override
					public void accept(final Item item) {
						fractions[i] = getValue(item);
						colors[i] = (Color) Objects.requireNonNull(item.getItemProperty(COLOR_PROPERTY)).getValue();
						i++;

					}
				});
		return new Gradient(fractions, colors);
	}

	private static double getValue(final Item item) {
		return ((Number) Objects.requireNonNull(item.getItemProperty(VALUE_PROPERTY)).getValue()).doubleValue();
	}
}