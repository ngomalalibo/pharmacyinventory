package mgt.inventory.pharmacy.ui.dialogs;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import mgt.inventory.pharmacy.entities.Employee;
import mgt.inventory.pharmacy.entities.IdGenerator;


public class EditEmployeeDialog extends MDialog {
    
    private static final long serialVersionUID = -4136556525370459308L;
    
    public EditEmployeeDialog(final Employee employeeBean, DialogAction action, OnAction onaction) {
        super(action);
        
        Binder<Employee> binder = new Binder<>(Employee.class);
        binder.setBean(new Employee());
        
        switch (action) {
            case NEW:
                setHeader("New Inventory Employee");
                break;
            case EDIT:
                setHeader("Edit Inventory Employee");
                break;
            case DELETE:
                setHeader("Delete Inventory Employee?");
                break;
            case VIEW:
                setHeader("Inventory Employee");
                break;
        }
        
        TextField employeeId = new TextField("Employee Id");
        employeeId.setValue(IdGenerator.generateId("employee"));
        binder.forField(employeeId).asRequired("Please enter a Employee Id")
                .withValidator(new StringLengthValidator("Please add a valid Employee Id", 4, 15)).bind("employeeId");
        employeeId.setEnabled(false);
        
        TextField fullName = new TextField("Employee Full Name");
        binder.forField(fullName).asRequired("Please enter Employee full Name").bind("fullName");
        
        TextField phoneNo = new TextField("Employee Phone No");
        binder.forField(phoneNo).asRequired("Please enter Employee Phone No").bind("phoneNo");
        
        TextField email = new TextField("Employee Email");
        binder.forField(email).asRequired("Please enter Employee Email").bind("email");
    
        TextField address = new TextField("Employee Address");
        binder.forField(address).asRequired("Please enter Employee Address").bind("address");
    
        TextArea remark = new TextArea("Remark about Employee");
        remark.getElement().setAttribute("colspan", "2").setAttribute("theme", "small");
        remark.getStyle().set("minHeight", "12em");
        binder.forField(remark).bind("remark");
        
        FormLayout form = new FormLayout(employeeId, fullName, phoneNo, email, address, remark);
        form.setResponsiveSteps(new ResponsiveStep("0", 2));
        form.setWidth("500px");
        setContents(form);
        
        binder.readBean(employeeBean);
        
        // all fields will be locked if viewing or in delete mode
        binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);
        
        if (action != DialogAction.VIEW) {
            Button actionbtn = new Button("Save");
            actionbtn.getElement().setAttribute("theme", "small primary");
            actionbtn.addClickListener(e -> {
                if (binder.validate().isOk()) {
                    if (binder.writeBeanIfValid(employeeBean)) {
                        onaction.action(employeeBean);
                        close();
                    }
                }
            });
            
            if (action == DialogAction.DELETE) {
                Button deletebtn = new Button("Delete");
                deletebtn.getElement().setAttribute("theme", "error small");
                deletebtn.setVisible(action == DialogAction.EDIT);
                deletebtn.addClickListener(e -> new ActionConfirmDialog("Delete Shipping Address?",
                        "Are you sure you want to delete this address?", DialogAction.DELETE, () -> {
                    onaction.action(employeeBean);
                    close();
                }).open());
                addActions(deletebtn);
            }
            
            addTerminalActions(actionbtn);
            
            setCloseOnEsc(false);
            setCloseOnOutsideClick(false);
        }
    }
    
    public interface OnAction {
        void action(Employee c);
    }
    
}
