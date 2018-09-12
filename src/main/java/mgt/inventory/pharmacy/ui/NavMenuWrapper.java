package mgt.inventory.pharmacy.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

@ParentLayout(BasePage.class)
@Tag("nav-men")
public class NavMenuWrapper extends FlexLayout implements RouterLayout {
    
    private static final long serialVersionUID = 1L;
    
    public NavMenuWrapper() {
        getStyle().set("flexDirection", "row").set("background-color", "rgb(241, 241, 241)").set("flex-grow", "1")
                .set("paddingTop", "2em").set("paddingBottom", "2em");
        
        RouterLink products = new RouterLink("Products", ProductCRUDPage.class);
        products.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink customers = new RouterLink("Customers", CustomerCRUDPage.class);
        RouterLink orders = new RouterLink("Orders", OrderCRUDPage.class);
        RouterLink purchases = new RouterLink("Purchases", PurchaseOrderCRUDPage.class);
        RouterLink employees = new RouterLink("Employees", EmployeeCRUDPage.class);
        
        Div menudiv = new Div(
                new StyledRouteLink(products),
                new StyledRouteLink(customers),
                new StyledRouteLink(orders),
                new StyledRouteLink(purchases),
                new StyledRouteLink(employees));
        menudiv.setWidth("200px");
        add(menudiv);
    }
    
    public class StyledRouteLink extends Div {
        
        private static final long serialVersionUID = 1L;
        
        public StyledRouteLink(RouterLink rlink) {
            getStyle().set("color", "black").set("borderBottom", "1px solid #b9cfea");
            rlink.getStyle().set("background", "white").set("display", "block").set("padding", "1em").set("fontWeight",
                    "600");
            add(rlink);
        }
        
    }
    
}