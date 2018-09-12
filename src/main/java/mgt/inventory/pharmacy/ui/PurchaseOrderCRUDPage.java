package mgt.inventory.pharmacy.ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import mgt.inventory.pharmacy.database.MongoDB;
import mgt.inventory.pharmacy.entities.PurchaseOrder;
import mgt.inventory.pharmacy.ui.dialogs.EditOrderDialog;
import mgt.inventory.pharmacy.ui.dialogs.EditPurchaseOrderDialog;
import mgt.inventory.pharmacy.ui.dialogs.MDialog.DialogAction;

@PageTitle("PurchaseOrders")
@Route(value = "purchaseorder", layout = NavMenuWrapper.class)
public class PurchaseOrderCRUDPage extends FlexLayout {
    
    private static final long serialVersionUID = 1L;
    
    private H4 orderCount = new H4();
    
    private final List<PurchaseOrder> allOrders = new ArrayList<>();
    
    private Grid<PurchaseOrder> grid = new Grid<>();
    
    public PurchaseOrderCRUDPage() {
        
        getStyle()//
                .set("padding", "1em")//
                .set("flex-grow", "1")//
                .set("flexDirection", "column");
        
        orderCount.getStyle().set("color", "#777777");
        
        FlexLayout flex = new FlexLayout(new H3("Purchase Orders"), orderCount);
        flex.setAlignItems(Alignment.CENTER);
        flex.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(flex);
        
        Button createOrder = new Button("Add New Purchase Order");
        createOrder.getElement().setAttribute("theme", "small primary");
        createOrder.addClickListener(click -> newOrderView());
        add(new Div(createOrder));
        
        grid.setDataProvider(new ListDataProvider<>(allOrders));
        grid.setSelectionMode(SelectionMode.NONE);
        grid.addComponentColumn(order -> {
            Button pcode = new Button(order.getPurchaseId());
            pcode.getElement().setAttribute("theme", "tertiary");
            pcode.setClassName("font-size-s");
            pcode.addClickListener(clk -> openOrderView(order));
            return pcode;
        }).setHeader("Product Code");
        
        grid.addColumn(PurchaseOrder::getPurchaseBy).setHeader("Purchase Order Id");
        grid.addColumn(PurchaseOrder::getProductCode).setHeader("Product");
        grid.addColumn(PurchaseOrder::getQuantity).setHeader("Quantity in Stock");
        grid.addColumn(PurchaseOrder::getPurchaseDate).setHeader("Purchase Date");
        grid.addColumn(PurchaseOrder::getSupplierId).setHeader("Supplier Id");
        grid.addColumn(PurchaseOrder::getUnitPrice).setHeader("Unit Price");
        
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
    
    private void openOrderView(PurchaseOrder order) {
        new EditPurchaseOrderDialog(order, DialogAction.VIEW, i -> refreshDataTable()).open();
    }
    
    private void editOrderView(PurchaseOrder order) {
        new EditPurchaseOrderDialog(order, DialogAction.EDIT, i -> {
            i.persist(i);
            refreshDataTable();
        }).open();
    }
    
    private void deleteOrderView(PurchaseOrder order) {
        new EditPurchaseOrderDialog(order, DialogAction.DELETE, i -> {
            i.delete(i);
            refreshDataTable();
        }).open();
    }
    
    private void newOrderView() {
        new EditPurchaseOrderDialog(new PurchaseOrder(), DialogAction.NEW, i -> {
            i.persist(i);
            refreshDataTable();
        }).open();
    }
    
    private void refreshDataTable() {
        allOrders.clear();
        allOrders.addAll(MongoDB.getPurchaseOrders());
        updateOrderCount(allOrders.size());
        grid.getDataProvider().refreshAll();
    }
    
    private void updateOrderCount(int count) {
        orderCount.setText(String.format("Total Purchase Orders: %d", count));
    }
    
}
