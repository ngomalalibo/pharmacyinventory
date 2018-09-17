package mgt.inventory.pharmacy.ui;

import java.io.Serializable;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServletConfiguration;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@HtmlImport("styles/shared-styles.html")
@Viewport("user-scalable=no, initial-scale=1.0, shrink-to-fit=no")
@VaadinServletConfiguration(productionMode = true)
//@Theme(value = Lumo.class, variant = Lumo.DARK)
@Push
public class BasePage extends FlexLayout implements Serializable, RouterLayout {

	private static final long serialVersionUID = 1L;

	public BasePage() {
		
		
		
		getStyle()//
				.set("flexDirection", "row")//
				.set("height", "100vh")//
				// .set("maxWidth", "1240px")//
				.set("margin-left", "auto")//
				.set("margin-right", "auto");
		Div appname = new Div(new Span("PharmaServe"));
		appname.getStyle().set("fontSize", "var(--lumo-font-size-xl)").set("padding", "1em").set("fontWeight", "600");
		
		RouterLink products = new RouterLink("Products", ProductCRUDPage.class);
		products.setHighlightCondition(HighlightConditions.sameLocation());
		RouterLink customers = new RouterLink("Customers", CustomerCRUDPage.class);
		customers.setHighlightCondition(HighlightConditions.sameLocation());
		RouterLink orders = new RouterLink("Orders", OrderCRUDPage.class);
		orders.setHighlightCondition(HighlightConditions.sameLocation());
		RouterLink purchases = new RouterLink("Purchases", PurchaseOrderCRUDPage.class);
		purchases.setHighlightCondition(HighlightConditions.sameLocation());
		RouterLink employees = new RouterLink("Employees", EmployeeCRUDPage.class);
		employees.setHighlightCondition(HighlightConditions.sameLocation());
		
		Div menudiv = new Div(appname, new StyledRouteLink(products), new StyledRouteLink(customers),
				new StyledRouteLink(orders), new StyledRouteLink(purchases), new StyledRouteLink(employees));
		menudiv.getElement().setAttribute("theme", "dark");
		menudiv.getStyle().set("paddingLeft", "20px");
		menudiv.setWidth("200px");
		add(menudiv);
		
		/*getStyle()//
				.set("flexDirection", "column")//
				.set("height", "100vh")//
				.set("maxWidth", "1240px")//
				.set("margin-left", "auto")//
//				.set("background-color", "rgb(247, 247, 247)")//
				.set("fontSize", "12")
				.set("margin-right", "auto");

		// this is the parent page to all pages in this application

		Div appname = new Div(new Span("Pharmacy Inventory Management"));
		appname.getStyle().set("fontSize", "var(--lumo-font-size-xxl)");

		Div othercontrols = new Div(new Span("version 1.0"));

		FlexLayout flexlayout = new FlexLayout(appname, othercontrols); 
		flexlayout.setAlignItems(Alignment.CENTER);
		flexlayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		flexlayout.getStyle().set("marginLeft", "1em").set("marginRight", "1em");
		add(flexlayout); */
	}
	
	public class StyledRouteLink extends Div {
		
		private static final long serialVersionUID = 1L;
		
		public StyledRouteLink(RouterLink rlink) {
			rlink.getStyle().set("display", "block").set("fontWeight", "600");
			add(rlink);
		}
		
	}
	
	
}
