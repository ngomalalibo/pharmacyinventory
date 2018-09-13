package mgt.inventory.pharmacy.ui.dialogs;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import mgt.inventory.pharmacy.database.MongoDB;
import mgt.inventory.pharmacy.entities.Customer;
import mgt.inventory.pharmacy.entities.Order;
import mgt.inventory.pharmacy.entities.Product;
import mgt.inventory.pharmacy.entities.StockTaking;
import mgt.inventory.pharmacy.sms.SendSMS;
import mgt.inventory.pharmacy.ui.NumberTextField;

import java.util.HashMap;


public class EditOrderDialog extends MDialog {
    
    private static final long serialVersionUID = -4136556525370459308L;
    
    public EditOrderDialog(final Order orderBean, DialogAction action, OnAction onaction) {
        super(action);
        
        Binder<Order> binder = new Binder<>(Order.class);
        binder.setBean(new Order());
        
        switch (action) {
            case NEW:
                setHeader("New Inventory Order");
                break;
            case EDIT:
                setHeader("Edit Inventory Order");
                break;
            case DELETE:
                setHeader("Delete Inventory Order?");
                break;
            case VIEW:
                setHeader("Inventory Order");
                break;
        }
    
        TextField orderId = new TextField("Order Id");
        orderId.setPlaceholder("XXX-XXXXX");
        binder.forField(orderId).asRequired("Please enter a Order Id")
                .withValidator(new StringLengthValidator("Please add a valid order Id", 4, 15)).bind("orderId");
        
        //Searchable ComboBox
        ComboBox<Customer> customerId = new ComboBox<>("Customer", MongoDB.getCustomers());
        customerId.setItemLabelGenerator(customer -> customer.getFullName());
        customerId.addValueChangeListener(prod ->
        {
            if(prod!=null)
            {
                binder.getBean().setCustomerId(customerId.getValue().getCustomerId());
            }
            else
            {
                binder.getBean().setCustomerId(null);
            }
        });
        binder.forField(customerId).asRequired("Please select a Customer")
                .bind("customerId");
        
        //Searchable ComboBox for products
        ComboBox<Product> productCode = new ComboBox<>("Product", MongoDB.getProductCombo());
        productCode.setItemLabelGenerator(prod -> prod.getProductName());
        productCode.addValueChangeListener(prod ->
        {
            if(prod!=null)
            {
                binder.getBean().setProductCode(prod.getValue().getProductCode());
            }
            else
            {
                binder.getBean().setProductCode(null);
            }
        });
        binder.forField(productCode).asRequired("Please select a Product")
                .bind("productCode");
    
    
        NumberTextField quantity = new NumberTextField("Quantity");
        binder.forField(quantity).asRequired("Please enter Quantity").bind("quantity");
        
        /*TextField quantity = new TextField("Quantity");
        quantity.setPlaceholder("1");
        binder.forField(quantity).asRequired("Please enter Quantity").bind("quantity");*/
    
        TextField unitSellingPrice = new TextField("Unit Selling Price");
        unitSellingPrice.setPlaceholder("1");
        binder.forField(unitSellingPrice).asRequired("Please enter Unit Selling Price").bind("unitSellingPrice");
        
        TextField receiptNo = new TextField("Receipt No");
        binder.forField(receiptNo).bind("receiptNo");
        
        FormLayout form = new FormLayout(orderId, customerId,  productCode, quantity, unitSellingPrice, receiptNo);
        form.setResponsiveSteps(new ResponsiveStep("0", 2));
        form.setWidth("500px");
        setContents(form);
        
        binder.readBean(orderBean);
        
        // all fields will be locked if viewing or in delete mode
        binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);
        
        if (action != DialogAction.VIEW) {
            Button actionbtn = new Button("Save");
            actionbtn.getElement().setAttribute("theme", "small primary");
            actionbtn.addClickListener(e -> {
                if (binder.validate().isOk()) {
                    if (binder.writeBeanIfValid(orderBean)) {
                        if(checkAvailQuantity(orderBean.getProductCode(), orderBean.getQuantity()))
                        {
                            updateStockQuantity(orderBean.getProductCode(), orderBean.getQuantity());
                            //SendSMS.sendSms("+18509295655", "+2348098423619", "Thank you for your Patronage. We look forward to seeing you again. Have a great day");
                        }
                        onaction.action(orderBean);
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
                    onaction.action(orderBean);
                    close();
                }).open());
                addActions(deletebtn);
            }
            
            addTerminalActions(actionbtn);
            
            setCloseOnEsc(false);
            setCloseOnOutsideClick(false);
        }
    }
    
    private void updateStockQuantity(String productCode, Integer orderQuantity)
    {
        StockTaking st = MongoDB.getLatestStockTaken(productCode);
        Integer updateQuantity= st.getQuantityInStock() - orderQuantity;
        
        MongoDB.updateQuantityStock(st.getId(), updateQuantity);
        
        
    }
    
    private boolean checkAvailQuantity(String productCode, Integer orderQuantity)
    {
        boolean check = false;
        StockTaking st = MongoDB.getLatestStockTaken(productCode);
        if(st.getQuantityInStock()>=orderQuantity)
        {
            check = true;
        }
            return check;
    }
    
    public interface OnAction {
        void action(Order c);
    }
    
}
