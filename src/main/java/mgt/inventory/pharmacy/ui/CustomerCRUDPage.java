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
import mgt.inventory.pharmacy.entities.Customer;
import mgt.inventory.pharmacy.ui.dialogs.EditCustomerDialog;
import mgt.inventory.pharmacy.ui.dialogs.MDialog.DialogAction;

@PageTitle("Customers")
@Route(value = "customer", layout = NavMenuWrapper.class)
public class CustomerCRUDPage extends FlexLayout {
    
    private static final long serialVersionUID = 1L;
    
    private H4 customersCount = new H4();
    
    private final List<Customer> allCustomers = new ArrayList<>();
    
    private Grid<Customer> grid = new Grid<>();
    
    public CustomerCRUDPage() {
        
        getStyle()//
                .set("padding", "1em")//
                .set("flex-grow", "1")//
                .set("flexDirection", "column");
    
        customersCount.getStyle().set("color", "#777777");
        
        FlexLayout flex = new FlexLayout(new H3("Customers"), customersCount);
        flex.setAlignItems(Alignment.CENTER);
        flex.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(flex);
        
        Button createCustomer = new Button("Add New Customer");
        createCustomer.getElement().setAttribute("theme", "small primary");
        createCustomer.addClickListener(click -> newCustomerView());
        add(new Div(createCustomer));
        
        grid.setDataProvider(new ListDataProvider<>(allCustomers));
        grid.setSelectionMode(SelectionMode.NONE);
        grid.addComponentColumn(customer -> {
            Button pcode = new Button(customer.getCustomerId());
            pcode.getElement().setAttribute("theme", "tertiary");
            pcode.setClassName("font-size-s");
            pcode.addClickListener(clk -> openCustomerView(customer));
            return pcode;
        }).setHeader("Customer Id");
        
        grid.addColumn(Customer::getFullName).setHeader("Full Name");
        grid.addColumn(Customer::getEmail).setHeader("Email");
        grid.addColumn(Customer::getPhoneNo).setHeader("Phone No");
        grid.addColumn(Customer::getRemark).setHeader("Remark");
        grid.addColumn(i -> i.getCreatedDate().format(DateTimeFormatter.ISO_DATE)).setHeader("Created");
        
        grid.addComponentColumn(customer -> {
            Div fl = new Div();
            fl.getStyle().set("textAlign", "right");
            
            Button edit = new Button(new IronIcon("lumo", "edit"));
            edit.getElement().setAttribute("theme", "small icon");
            edit.getStyle().set("marginRight", "0.4em");
            edit.addClickListener(click -> editCustomerView(customer));
            
            Button delete = new Button(new IronIcon("lumo", "cross"));
            delete.getElement().setAttribute("theme", "small icon error");
            delete.addClickListener(click -> deleteCustomerView(customer));
            
            fl.add(edit, delete);
            return fl;
        }).setFlexGrow(0).setWidth("160px");
        
        add(grid);
        refreshDataTable();
    }
    
    private void openCustomerView(Customer customer) {
        new EditCustomerDialog(customer, DialogAction.VIEW, i -> refreshDataTable()).open();
    }
    
    private void editCustomerView(Customer customer) {
        new EditCustomerDialog(customer, DialogAction.EDIT, i -> {
            i.persist(i);
            refreshDataTable();
        }).open();
    }
    
    private void deleteCustomerView(Customer customer) {
        new EditCustomerDialog(customer, DialogAction.DELETE, i -> {
            i.delete(i);
            refreshDataTable();
        }).open();
    }
    
    private void newCustomerView() {
        new EditCustomerDialog(new Customer(), DialogAction.NEW, i -> {
            i.persist(i);
            refreshDataTable();
        }).open();
    }
    
    private void refreshDataTable() {
        allCustomers.clear();
        allCustomers.addAll(MongoDB.getCustomers());
        updateCustomerCount(allCustomers.size());
        grid.getDataProvider().refreshAll();
    }
    
    private void updateCustomerCount(int count) {
        customersCount.setText(String.format("Total Customers: %d", count));
    }
    
}
