package mgt.inventory.pharmacy.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("StartPage")
@Route(value = "", layout = BasePage.class)
public class StartPage extends Div {

	private static final long serialVersionUID = 1L;

	public StartPage() {

		add(new Span("StartPage"));

		getStyle().set("background-color", "#f1f1f1").set("padding", "1em");

	}

}
