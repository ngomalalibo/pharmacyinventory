package mgt.inventory.pharmacy.ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.Mongo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import mgt.inventory.pharmacy.database.MongoDB;
import mgt.inventory.pharmacy.entities.Order;
import mgt.inventory.pharmacy.ui.dialogs.EditOrderDialog;
import mgt.inventory.pharmacy.ui.dialogs.MDialog.DialogAction;

@PageTitle("Orders")
@Route(value = "order", layout = NavMenuWrapper.class)
public class OrderCRUDPage extends FlexLayout {
    
    private static final long serialVersionUID = 1L;
    
    private Span orderCount = new Span();
    
    private final List<Order> allOrders = new ArrayList<>();
    
    private Grid<Order> grid = new Grid<>();
    
    public OrderCRUDPage() {
        
        getStyle()//
                .set("padding", "1em")//
                .set("flex-grow", "1")//
                .set("flexDirection", "column");
        
        orderCount.getStyle().set("color", "#777777");
    
        Button createOrder = new Button("Add New Order");
        createOrder.getElement().setAttribute("theme", "small primary");
        createOrder.addClickListener(click -> newOrderView());
        
        FlexLayout flex = new FlexLayout(new H3("Orders"), createOrder);
        flex.setAlignItems(Alignment.CENTER);
        flex.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(flex);
        
        grid.setDataProvider(new ListDataProvider<>(allOrders));
        grid.setSelectionMode(SelectionMode.NONE);
        grid.addComponentColumn(order -> {
            Button pcode = new Button(order.getOrderId());
            pcode.getElement().setAttribute("theme", "tertiary");
            pcode.setClassName("font-size-s");
            pcode.addClickListener(clk -> openOrderView(order));
            return pcode;
        }).setHeader("Product Code").setFooter(orderCount);
        
        grid.addColumn(Order::getOrderId).setHeader("Order Id");
        grid.addColumn(Order::getProductCode).setHeader("Product");
        grid.addColumn(Order::getQuantity).setHeader("Quantity");
        grid.addColumn(Order::getCustomerId).setHeader("Customer Id");
        grid.addColumn(Order::getUnitSellingPrice).setHeader("Unit Selling Price");
        //grid.addColumn(Order::getReceiptNo).setHeader("Receipt No");
        
        grid.addColumn(i -> i.getCreatedDate().format(DateTimeFormatter.ISO_DATE)).setHeader("Created");
        
        grid.addComponentColumn(order -> {
            Div fl = new Div();
            fl.getStyle().set("textAlign", "right");
            
            Button edit = new Button(new IronIcon("lumo", "edit"));
            edit.getElement().setAttribute("theme", "small icon");
            edit.getStyle().set("marginRight", "0.4em");
            edit.addClickListener(click -> editOrderView(order));
            
            Button delete = new Button(new IronIcon("lumo", "cross"));
            delete.getElement().setAttribute("theme", "small icon error");
            delete.addClickListener(click -> deleteOrderView(order));
            
            fl.add(edit, delete);
            return fl;
        }).setFlexGrow(0).setWidth("160px");
        
        add(grid);
        refreshDataTable();
    }
    
    private void openOrderView(Order order) {
        new EditOrderDialog(order, DialogAction.VIEW, i -> refreshDataTable()).open();
    }
    
    private void editOrderView(Order order) {
        new EditOrderDialog(order, DialogAction.EDIT, i -> {
            i.persist(i);
            refreshDataTable();
        }).open();
    }
    
    private void deleteOrderView(Order order) {
        new EditOrderDialog(order, DialogAction.DELETE, i -> {
            i.delete(i);
            refreshDataTable();
        }).open();
    }
    
    private void newOrderView() {
        new EditOrderDialog(new Order(), DialogAction.NEW, i -> {
            i.persist(i);
            refreshDataTable();
        }).open();
    }
    
    private void refreshDataTable() {
        allOrders.clear();
        allOrders.addAll(MongoDB.getOrders());
        updateOrderCount(allOrders.size());
        grid.getDataProvider().refreshAll();
    }
    
    private void updateOrderCount(int count) {
        orderCount.setText(String.format("Total: %d", count));
    }
    
}
