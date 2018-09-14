package mgt.inventory.pharmacy.ui.dialogs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

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

import javafx.beans.property.Property;
import mgt.inventory.pharmacy.database.MongoDB;
import mgt.inventory.pharmacy.entities.Employee;
import mgt.inventory.pharmacy.entities.IdGenerator;
import mgt.inventory.pharmacy.entities.Product;
import mgt.inventory.pharmacy.entities.StockTaking;

public class EditStockTakingDialog extends MDialog {
    
    private static final long serialVersionUID = -4136556525370459308L;
    
    public EditStockTakingDialog(final StockTaking stockTakingBean, DialogAction action, OnAction onaction) {
        super(action);
        
        Binder<StockTaking> binder = new Binder<>(StockTaking.class);
        binder.setBean(new StockTaking());
        
        switch (action) {
            case NEW:
                setHeader("New Stock Record");
                break;
            case EDIT:
                setHeader("Edit Stock Record");
                break;
            case DELETE:
                setHeader("Delete Stock Record?");
                break;
            case VIEW:
                setHeader("Stock Record");
                break;
        }
    
        //ComboBox
        TextField stockId = new TextField("Stock Id");
        stockId.setValue(IdGenerator.generateId("stock"));
        binder.forField(stockId).bind("stockId");
        stockId.setEnabled(false);
        
        ComboBox<Product> productCode = new ComboBox<>("Products", MongoDB.getProductCombo());
        //productCode.setItems(MongoDB.getProductCombo());
        binder.forField(productCode).asRequired("Please select a product")
                .bind("productCode");
    
        TextField quantityInStock = new TextField("Quantity In Stock");
        quantityInStock.setPlaceholder("1");
        binder.forField(quantityInStock).asRequired("Please enter quantity in Stock")
                .bind("quantityInStock");
        
        TextField reorderLevel = new TextField("Reorder Level");
        reorderLevel.setPlaceholder("1");
        binder.forField(reorderLevel).asRequired("Please enter reorder level")
                .bind("reorderLevel");
    
        ComboBox<Employee> stockTakenBy = new ComboBox<>("Stock Taken By", MongoDB.getEmployeeCombo());
        binder.forField(stockTakenBy).asRequired("Please enter employee taking the stock")
                .bind("stockTakenBy");
        
        
        DatePicker stockTakenDate = new DatePicker("Created Date");
        LocalDateTime crdt = stockTakingBean.getCreatedDate();
        stockTakenDate.setValue(crdt != null ? crdt.toLocalDate() : LocalDate.now());
        stockTakenDate.setReadOnly(true);
        binder.forField(stockTakenDate).bind("stockTakenDate;");
    
        DatePicker discontinueDate = new DatePicker("Discontinue Date");
        binder.forField(discontinueDate).bind("discontinueDate;");
        
        FormLayout form = new FormLayout(stockId, productCode, quantityInStock, reorderLevel, stockTakenBy, stockTakenDate, discontinueDate);
        form.setResponsiveSteps(new ResponsiveStep("0", 2));
        form.setWidth("500px");
        setContents(form);
        
        binder.readBean(stockTakingBean);
        
        // all fields will be locked if viewing or in delete mode
        binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);
        
        if (action != DialogAction.VIEW) {
            Button actionbtn = new Button("Save");
            actionbtn.getElement().setAttribute("theme", "small primary");
            actionbtn.addClickListener(e -> {
                if (binder.validate().isOk()) {
                    if (binder.writeBeanIfValid(stockTakingBean)) {
                        onaction.action(stockTakingBean);
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
                    onaction.action(stockTakingBean);
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
        void action(StockTaking c);
    }
    
}
