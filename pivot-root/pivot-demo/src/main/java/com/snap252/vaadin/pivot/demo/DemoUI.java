package com.snap252.vaadin.pivot.demo;

import java.math.BigDecimal;
import java.util.stream.Collector;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jdt.annotation.Nullable;

import com.snap252.org.aggregators.BigDecimalArithmetics;
import com.snap252.org.aggregators.MutableValue;
import com.snap252.org.aggregators.NumberStatistics;
import com.snap252.org.aggregators.PivotCollectors;
import com.snap252.org.pivoting.BiBucketParameter;
import com.snap252.org.pivoting.NamedPivotCriteria;
import com.snap252.org.testing.RandomDataGenerator;
import com.snap252.org.testing.RandomDataGenerator.Person;
import com.snap252.vaadin.pivot.PivotGrid;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("PivotGrid Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		// Initialize our new UI component
		final PivotGrid pivotGrid = new PivotGrid();
		setDS(pivotGrid);

		// Show it in the middle of the screen
		final VerticalLayout layout = new VerticalLayout();
		Button b = new Button(" DS ");
		b.addClickListener(i -> setDS(pivotGrid));
		layout.setStyleName("demoContentLayout");
		layout.setSizeFull();
		layout.addComponents(b, pivotGrid);
		pivotGrid.setSizeFull();
		layout.setComponentAlignment(pivotGrid, Alignment.MIDDLE_CENTER);
		layout.setExpandRatio(pivotGrid, 1);
		setContent(layout);
	}

	@SuppressWarnings("deprecation")
	protected void setDS(final PivotGrid pivotGrid) {
		final BiBucketParameter<Person> parameter = new BiBucketParameter<>(RandomDataGenerator.createPersons(2000))
				.setColFnkt(new NamedPivotCriteria<>(p -> {
					int year = p.getGeburtstag().getYear();
					return year + 1900 - year % 10;
				}, "GebDat(Dekade)"), new NamedPivotCriteria<>(p -> p.getGeburtstag().getYear() + 1900, "GebDat(Jahr)")
				// new NamedPivotCriteria<>(p -> p.getGeburtstag().getMonth() +
				// 1, "GebDat(Monat)")

				)

				.setRowFnkt(

						new NamedPivotCriteria<>(p -> Character.toUpperCase(p.getVorname().charAt(0)), "Vorname(0)"),
						new NamedPivotCriteria<>(Person::getGeschlecht, "Geschlecht", false)

		// ,p -> p.geschlecht
		// ,p -> p.alter / 10

		);

		final Collector<Person, MutableValue<BigDecimal>, @Nullable NumberStatistics<BigDecimal>> reducer = PivotCollectors
				.getReducer(Person::getWert, new BigDecimalArithmetics());

		pivotGrid.setContainerDataSource(parameter, reducer);
	}
}