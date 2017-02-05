package com.snap252.vaadin.pivot.demo;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.snap252.org.testing.RandomDataGenerator;
import com.snap252.org.testing.RandomDataGenerator.Person;
import com.snap252.vaadin.pivot.GridRendererParameter;
import com.snap252.vaadin.pivot.PivotUI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("PivotGrid Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = false)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
	public static class Servlet extends VaadinServlet {
	}

	GridRendererParameter<Item> gridRendererParameter = new GridRendererParameter<>();

	@Override
	protected void init(VaadinRequest request) {

		// Initialize our new UI component
		final PivotUI pivotGrid = new PivotUI(gridRendererParameter);
		pivotGrid.setSizeFull();

		setDS(pivotGrid);

		// Show it in the middle of the screen
		final VerticalLayout layout = new VerticalLayout();
		Button b = new Button(" DS ");
		b.addClickListener(i -> setDS(pivotGrid));
		layout.setStyleName("demoContentLayout");
		layout.setSizeFull();
		{
			layout.addComponents(b, pivotGrid);
			layout.setExpandRatio(pivotGrid, 1);
			// layout.setComponentAlignment(pivotGrid, Alignment.MIDDLE_CENTER);
		}
		pivotGrid.setMargin(true);
		setContent(layout);
	}

	protected void setDS(final PivotUI pivotGrid) {
		List<Person> personen = RandomDataGenerator.createPersons(4000);
		BeanItemContainer<Person> container = new BeanItemContainer<>(Person.class);
		container.addAll(personen);
		List<Item> containerItems = pivotGrid.setContainerDataSource(PivotUI.cloneContainer(container));
		gridRendererParameter.setValues(containerItems);
	}

}
