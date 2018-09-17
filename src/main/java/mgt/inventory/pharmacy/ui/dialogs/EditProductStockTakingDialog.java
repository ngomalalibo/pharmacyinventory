package mgt.inventory.pharmacy.ui.dialogs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;
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

import mgt.inventory.pharmacy.database.MongoDB;
import mgt.inventory.pharmacy.database.MongoDB.ProductStockTaking;
import mgt.inventory.pharmacy.entities.Employee;
import mgt.inventory.pharmacy.entities.IdGenerator;
import mgt.inventory.pharmacy.entities.Product;
import mgt.inventory.pharmacy.entities.StockTaking;
import mgt.inventory.pharmacy.ui.NumberTextField;

public class EditProductStockTakingDialog extends MDialog
{
    
    private static final long serialVersionUID = -4136556525370459308L;
    
    public EditProductStockTakingDialog(final ProductStockTaking psbean, DialogAction action, OnAction onaction)
    {
        super(action);
        
        Binder<StockTaking> binder = new Binder<>(StockTaking.class);
        binder.setBean(new StockTaking());
    
        String gid = IdGenerator.generateId("stock");
        
        switch (action)
        {
            case NEW:
                setHeader("New Inventory Product");
                psbean.stockTaking.setStockId(gid);
                
                break;
            case EDIT:
                setHeader("Edit Product Stock");
                break;
            case DELETE:
                setHeader("Delete Product Stock?");
                break;
            case VIEW:
                setHeader("Product Stock");
                break;
        }
    
        TextField stockId = new TextField("Stock Id");
        binder.forField(stockId).bind("stockId");
        stockId.setEnabled(false);
        
        TextField productName = new TextField("Product Name");
        productName.setValue(psbean.product.getProductName());
        productName.setEnabled(false);
        
        TextField productCat = new TextField("Product Category");
        productCat.setValue(psbean.product.getProductCategory());
        
        TextField manufacturer = new TextField("Product Manufacturer");
        manufacturer.setValue(psbean.product.getProductManufacturer());
        //binder.forField(manufacturer).asRequired("Please enter a manufacturer").bind("productManufacturer");
        manufacturer.setEnabled(false);
        
        NumberTextField quantityInStock = new NumberTextField("QuantityInStock");
        binder.forField(quantityInStock).asRequired("Please quantity").bind("quantityInStock");
        
        List<Employee> allemployees =  MongoDB.getEmployeeCombo();
        ComboBox<Employee> stockTakenBy = new ComboBox<>("Stock Taken By" ,allemployees);
        stockTakenBy.setItemLabelGenerator(emp -> emp.getFullName());
        binder.forField(stockTakenBy).asRequired("Please select stock takers Info")
              .bind("stockTakenBy");
    
        NumberTextField reorderLevel = new NumberTextField("Reorder Level");
        binder.forField(reorderLevel).asRequired("Enter reorder Level").bind("reorderLevel");
        
        DatePicker discDate = new DatePicker("Discontinue Date");
        binder.forField(discDate).bind("discontinueDate");
        
        
        FormLayout form = new FormLayout(stockId, productName, productCat, manufacturer, manufacturer, quantityInStock, stockTakenBy, reorderLevel, discDate);
        form.setResponsiveSteps(new ResponsiveStep("0", 2));
        form.setWidth("500px");
        setContents(form);
        
        if(psbean.stockTaking == null)
        {
            psbean.stockTaking = new StockTaking();
        }
        
        binder.readBean(psbean.stockTaking);
        
        // all fields will be locked if viewing or in delete mode
        binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);
        
        if (action != DialogAction.VIEW)
        {
            if (action == DialogAction.DELETE)
            {
                Button deletebtn = new Button("Delete");
                deletebtn.getElement().setAttribute("theme", "error primary small");
                deletebtn.addClickListener(e -> new ActionConfirmDialog("Delete Stock Taken?",
                        "Are you sure you want to delete this stock Taken?", DialogAction.DELETE, () ->
                {
                    onaction.action(psbean.stockTaking);
                    close();
                }).open());
                addTerminalActions(deletebtn);
            }
            else
            {
                Button actionbtn = new Button("Save");
                actionbtn.getElement().setAttribute("theme", "small primary");
                actionbtn.addClickListener(e ->
                {
                    if (binder.validate().isOk())
                    {
                        if (binder.writeBeanIfValid(psbean.stockTaking))
                        {
                            psbean.stockTaking.setProductCode(psbean.product.getProductCode());
                            psbean.stockTaking.setStockTakenDate(LocalDate.now());
                            onaction.action(psbean.stockTaking);
                            close();
                        }
                    }
                });
                addTerminalActions(actionbtn);
            }
            
            setCloseOnEsc(false);
            setCloseOnOutsideClick(false);
        }
    }
    
    public interface OnAction
    {
        void action(StockTaking c);
    }
    
}
