package mgt.inventory.pharmacy.ui;

import java.io.Serializable;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinServletConfiguration;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@HtmlImport("styles/shared-styles.html")
@Viewport("user-scalable=no, initial-scale=1.0, shrink-to-fit=no")
//@VaadinServletConfiguration(productionMode = true)
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class BasePage extends FlexLayout implements Serializable, RouterLayout {

	private static final long serialVersionUID = 1L;

	public BasePage() {
		getStyle()//
				.set("flexDirection", "column")//
				.set("height", "100vh")//
				.set("maxWidth", "1240px")//
				.set("margin-left", "auto")//
				.set("background-color", "rgb(247, 247, 247)")//
				.set("margin-right", "auto");

		// this is the parent page to all pages in this application

		Div appname = new Div(new Span("Pharmacy Inventory Management"));
		appname.getStyle().set("fontSize", "var(--lumo-font-size-xxl)");

		Div othercontrols = new Div(new Span("version 1.0"));

		FlexLayout flexlayout = new FlexLayout(appname, othercontrols); 
		flexlayout.setAlignItems(Alignment.CENTER);
		flexlayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		flexlayout.getStyle().set("marginLeft", "1em").set("marginRight", "1em");
		add(flexlayout); 
	}

}
