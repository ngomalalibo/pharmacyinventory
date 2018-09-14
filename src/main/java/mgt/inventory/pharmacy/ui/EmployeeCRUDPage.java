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
import mgt.inventory.pharmacy.entities.Employee;
import mgt.inventory.pharmacy.ui.dialogs.EditEmployeeDialog;
import mgt.inventory.pharmacy.ui.dialogs.MDialog.DialogAction;

@PageTitle("Employees")
@Route(value = "employee", layout = NavMenuWrapper.class)
public class EmployeeCRUDPage extends FlexLayout {
    
    private static final long serialVersionUID = 1L;
    
    private H4 employeecount = new H4();
    
    private final List<Employee> allEmployees = new ArrayList<>();
    
    private Grid<Employee> grid = new Grid<>();
    
    public EmployeeCRUDPage() {
        
        getStyle()//
                .set("padding", "1em")//
                .set("flex-grow", "1")//
                .set("flexDirection", "column");
        
        employeecount.getStyle().set("color", "#777777");
        
        FlexLayout flex = new FlexLayout(new H3("Employees"), employeecount);
        flex.setAlignItems(Alignment.CENTER);
        flex.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(flex);
        
        Button createEmployee = new Button("Add New Employee");
        createEmployee.getElement().setAttribute("theme", "small primary");
        createEmployee.addClickListener(click -> newEmployeeView());
        add(new Div(createEmployee));
        
        grid.setDataProvider(new ListDataProvider<>(allEmployees));
        grid.setSelectionMode(SelectionMode.NONE);
        grid.addComponentColumn(employee -> {
            Button pcode = new Button(employee.getEmployeeId());
            pcode.getElement().setAttribute("theme", "tertiary");
            pcode.setClassName("font-size-s");
            pcode.addClickListener(clk -> openCustomerView(employee));
            return pcode;
        }).setHeader("Employee Id");
        
        grid.addColumn(Employee::getFullName).setHeader("Full Name").setWidth("15em");
        grid.addColumn(Employee::getEmail).setHeader("Email").setWidth("10em");
        grid.addColumn(Employee::getPhoneNo).setHeader("Phone No").setWidth("10em");
        grid.addColumn(Employee::getRemark).setHeader("Remark").setWidth("15em");
        grid.addColumn(i -> i.getCreatedDate().format(DateTimeFormatter.ISO_DATE)).setHeader("Created").setWidth("9em");
        
        grid.addComponentColumn(employee -> {
            Div fl = new Div();
            fl.getStyle().set("textAlign", "right");
            
            Button edit = new Button(new IronIcon("lumo", "edit"));
            edit.getElement().setAttribute("theme", "small icon");
            edit.getStyle().set("marginRight", "0.4em");
            edit.addClickListener(click -> editEmployeeView(employee));
            
            Button delete = new Button(new IronIcon("lumo", "cross"));
            delete.getElement().setAttribute("theme", "small icon error");
            delete.addClickListener(click -> deleteEmployeeView(employee));
            
            fl.add(edit, delete);
            return fl;
        }).setFlexGrow(0).setWidth("160px");
        
        add(grid);
        refreshDataTable();
    }
    
    private void openCustomerView(Employee employee) {
        new EditEmployeeDialog(employee, DialogAction.VIEW, i -> refreshDataTable()).open();
    }
    
    private void editEmployeeView(Employee employee) {
        new EditEmployeeDialog(employee, DialogAction.EDIT, i -> {
            i.persist(i);
            refreshDataTable();
        }).open();
    }
    
    private void deleteEmployeeView(Employee employee) {
        new EditEmployeeDialog(employee, DialogAction.DELETE, i -> {
            i.delete(i);
            refreshDataTable();
        }).open();
    }
    
    private void newEmployeeView() {
        new EditEmployeeDialog(new Employee(), DialogAction.NEW, i -> {
            i.persist(i);
            refreshDataTable();
        }).open();
    }
    
    private void refreshDataTable() {
        allEmployees.clear();
        allEmployees.addAll(MongoDB.getEmployees());
        updateEmployeeCount(allEmployees.size());
        grid.getDataProvider().refreshAll();
    }
    
    private void updateEmployeeCount(int count) {
        employeecount.setText(String.format("Total Employees: %d", count));
    }
    
}
