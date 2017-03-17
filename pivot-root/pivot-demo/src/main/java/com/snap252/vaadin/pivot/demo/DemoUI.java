package com.snap252.vaadin.pivot.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;

import com.snap252.org.testing.RandomDataGenerator;
import com.snap252.org.testing.RandomDataGenerator.Person;
import com.snap252.vaadin.pivot.ContainerPropertyProvider;
import com.snap252.vaadin.pivot.ExtractOncePropertyProvider;
import com.snap252.vaadin.pivot.ExtractOncePropertyProvider.PropertyOnceItem;
import com.snap252.vaadin.pivot.GridRendererParameter;
import com.snap252.vaadin.pivot.PivotTreeGrid;
import com.snap252.vaadin.pivot.PivotUI;
import com.snap252.vaadin.pivot.PropertyProvider;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("PivotGrid Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = false)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "com.snap252.vaadin.pivotdemo.WidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	private final GridRendererParameter<PropertyOnceItem, ?> gridRendererParameter = new GridRendererParameter<>(xxx());

	@Override
	protected void init(VaadinRequest request) {

		// Initialize our new UI component
		final PivotUI pivotGrid = new PivotUI(PivotTreeGrid::new, gridRendererParameter);
		pivotGrid.setSizeFull();

		setDS(pivotGrid);

		// Show it in the middle of the screen
		final VerticalLayout layout = new VerticalLayout();
		Button b = new Button("set DataSource");
		b.addClickListener(i -> setDS(pivotGrid));

		ComboBox loacleSelection = new ComboBox("Locale",
				Arrays.asList(Locale.US, Locale.GERMANY, Locale.ITALY, Locale.ENGLISH));
		loacleSelection.addValueChangeListener(evt -> {
			getUI().setLocale((Locale) evt.getProperty().getValue());
		});

		layout.setStyleName("demoContentLayout");
		layout.setSizeFull();
		{
			layout.addComponents(b, loacleSelection, pivotGrid);
			layout.setExpandRatio(pivotGrid, 1);
			// layout.setComponentAlignment(pivotGrid, Alignment.MIDDLE_CENTER);
		}
		pivotGrid.setMargin(true);
		setContent(layout);
	}

	protected void setDS(final PivotUI pivotGrid) {
		gridRendererParameter.setValues(xxx().getItems().collect(Collectors.toList()));
	}

	private static PropertyProvider<PropertyOnceItem, ?> xxx() {
		List<Person> personen = RandomDataGenerator.createPersons(100);
		BeanItemContainer<Person> container = new BeanItemContainer<>(Person.class);
		container.addAll(personen);
		return new ExtractOncePropertyProvider<>(new ContainerPropertyProvider(container));
	}

}
