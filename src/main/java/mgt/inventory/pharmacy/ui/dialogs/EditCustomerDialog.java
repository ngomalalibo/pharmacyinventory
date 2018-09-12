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
import mgt.inventory.pharmacy.entities.Customer;


public class EditCustomerDialog extends MDialog {
    
    private static final long serialVersionUID = -4136556525370459308L;
    
    public EditCustomerDialog(final Customer customerBean, DialogAction action, OnAction onaction) {
        super(action);
        
        Binder<Customer> binder = new Binder<>(Customer.class);
        binder.setBean(new Customer());
        
        switch (action) {
            case NEW:
                setHeader("New Customer");
                break;
            case EDIT:
                setHeader("Edit Customer");
                break;
            case DELETE:
                setHeader("Delete Customer?");
                break;
            case VIEW:
                setHeader("Customer");
                break;
        }
        
        TextField customerId = new TextField("Customer Id");
        customerId.setPlaceholder("XXX-XXXXX");
        binder.forField(customerId).asRequired("Please enter Customer Id").bind("customerId");
                //.withValidator(new StringLengthValidator("Please add a valid customer Id", 4, 15)).bind("customerId");
    
        TextField fullName = new TextField("Customer Full Name");
        binder.forField(fullName).asRequired("Please enter Customer full Name").bind("fullName");
    
        TextField phoneNo = new TextField("Customer Phone No");
        //phoneNo.setPattern("");
        binder.forField(phoneNo).asRequired("Please enter Customer Phone No").bind("phoneNo");
    
        TextField email = new TextField("Customer Email");
        binder.forField(email).asRequired("Please enter Customer Email").bind("email");
    
        TextArea remark = new TextArea("Remark about Customer");
        remark.getElement().setAttribute("colspan", "2").setAttribute("theme", "small");
        remark.getStyle().set("minHeight", "12em");
        binder.forField(remark).bind("remark");
        
        FormLayout form = new FormLayout(customerId, fullName, phoneNo, email, remark);
        form.setResponsiveSteps(new ResponsiveStep("0", 2));
        form.setWidth("500px");
        setContents(form);
        
        binder.readBean(customerBean);
        
        // all fields will be locked in viewing or in delete mode
        binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);
        
        if (action != DialogAction.VIEW) {
            Button actionbtn = new Button("Save");
            actionbtn.getElement().setAttribute("theme", "small primary");
            actionbtn.addClickListener(e -> {
                if (binder.validate().isOk()) {
                    if (binder.writeBeanIfValid(customerBean)) {
                        onaction.action(customerBean);
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
                    onaction.action(customerBean);
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
        void action(Customer c);
    }
    
}
