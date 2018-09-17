package mgt.inventory.pharmacy.ui.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import mgt.inventory.pharmacy.database.MongoDB;
import mgt.inventory.pharmacy.entities.*;
import mgt.inventory.pharmacy.sms.SendSMS;
import mgt.inventory.pharmacy.ui.DoubleNumberTextField;
import mgt.inventory.pharmacy.ui.NumberTextField;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EditOrderDialog extends MDialog {

	private static final long serialVersionUID = -4136556525370459308L;

	public EditOrderDialog(final Order orderBean, DialogAction action, OnAction onaction) {
		super(action);

		Binder<Order> binder = new Binder<>(Order.class);
		binder.setBean(new Order());
		
		String gid = IdGenerator.generateId("order");

		switch (action) {
		case NEW:
			setHeader("New Order");
			orderBean.setOrderId(gid);
			break;
		case EDIT:
			setHeader("Edit Order");
			break;
		case DELETE:
			setHeader("Delete Order?");
			break;
		case VIEW:
			setHeader("Inventory Order");
			break;
		}

		TextField orderId = new TextField("Order Id");
		binder.forField(orderId).asRequired("Please enter a Order Id")
				.withValidator(new StringLengthValidator("Please add a valid order Id", 4, 15)).bind("orderId");
		orderId.setEnabled(false);

		// Searchable ComboBox
		List<Customer> allCustomers = MongoDB.getCustomers();
		ComboBox<Customer> customerId = new ComboBox<>("Customer", allCustomers);
		customerId.setItemLabelGenerator(customer -> customer.getFullName());

		binder.forField(customerId).asRequired("Please select a Customer").bind(order -> {
			Optional<Customer> opt = allCustomers.stream()
					.filter(alc -> alc.getCustomerId().equals(order.getCustomerId())).findFirst();
			return opt.isPresent() ? opt.get() : null;
		}, (order, customer) -> {
			if (customer != null) {
				order.setCustomerId(customer.getCustomerId());
			} else {
				order.setCustomerId(null);
			}
		});

		// Searchable ComboBox for products
		List<Product> allproducts = MongoDB.getProductCombo();
		ComboBox<Product> productCode = new ComboBox<>("Product", allproducts);
		productCode.setItemLabelGenerator(prod -> prod.getProductName());

		binder.forField(productCode).asRequired("Please select a Product").bind(order -> {
			Optional<Product> opt = allproducts.stream()
					.filter(prd -> prd.getProductCode().equals(order.getProductCode())).findFirst();
			return opt.isPresent() ? opt.get() : null;
		}, (order, product) -> {
			if (product != null) {
				order.setProductCode(product.getProductCode());
			} else {
				order.setProductCode(null);
			}
		});

		NumberTextField quantity = new NumberTextField("Quantity");
		binder.forField(quantity).asRequired("Please enter Quantity").bind("quantity");

		/*
		 * TextField quantity = new TextField("Quantity"); quantity.setPlaceholder("1");
		 * binder.forField(quantity).asRequired("Please enter Quantity").bind("quantity"
		 * );
		 */

		DoubleNumberTextField unitSellingPrice = new DoubleNumberTextField("Unit Selling Price");
		unitSellingPrice.setPrefixComponent(new Span("₦"));
		binder.forField(unitSellingPrice).asRequired("Please enter Unit Selling Price").bind("unitSellingPrice");

		TextField receiptNo = new TextField("Receipt No");
		binder.forField(receiptNo).bind("receiptNo");

		FormLayout form = new FormLayout(orderId, customerId, productCode, quantity, unitSellingPrice, receiptNo);
		form.setResponsiveSteps(new ResponsiveStep("0", 2));
		form.setWidth("500px");
		setContents(form);

		binder.readBean(orderBean);

		// all fields will be locked if viewing or in delete mode
		binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);

		if (action != DialogAction.VIEW) {
			if (action == DialogAction.DELETE) {
				Button deletebtn = new Button("Delete");
				deletebtn.getElement().setAttribute("theme", "error primary small");
				deletebtn.addClickListener(e -> new ActionConfirmDialog("Delete Order?",
						"Are you sure you want to delete this order?", DialogAction.DELETE, () -> {
					onaction.action(orderBean);
					close();
				}).open());
				addTerminalActions(deletebtn);
			}
			else {
				Button actionbtn = new Button("Save");
				actionbtn.getElement().setAttribute("theme", "small primary");
				actionbtn.addClickListener(e -> {
					if (binder.validate().isOk()) {
						if (binder.writeBeanIfValid(orderBean)) {
							if (checkAvailQuantity(orderBean.getProductCode(), orderBean.getQuantity())) {
								updateStockQuantity(orderBean.getProductCode(), orderBean.getQuantity());
								String message = SendSMS.sendSms("+2348098423619", "+18509295655", "Thank you for your Patronage.");
								new Notification("Order has been placed. SMS has been sent to the customer.", 4000, Notification.Position.MIDDLE).open();
								onaction.action(orderBean);
								close();
							}
							else {
								new Notification("Insufficient Quantity in stock", 4000).open();
							}
							
							
						}
					}
				});
				addTerminalActions(actionbtn);
			}
			
			setCloseOnEsc(false);
			setCloseOnOutsideClick(false);
		}
	}

	private void updateStockQuantity(String productCode, Integer orderQuantity) {
		StockTaking st = MongoDB.getLatestStockTaken(productCode);
		Integer updateQuantity = st.getQuantityInStock() - orderQuantity;
		
		System.out.println("updatedQuantity: "+updateQuantity);

		MongoDB.updateQuantityStock(st.getStockId(), updateQuantity);
		
		st.setQuantityInStock(updateQuantity);
		
		st.persist(st);

	}

	private boolean checkAvailQuantity(String productCode, Integer orderQuantity) {
		boolean check = false;
		StockTaking st = MongoDB.getLatestStockTaken(productCode);
		if (st != null && st.getQuantityInStock() >= orderQuantity) {
			check = true;
		}
		return check;
	}

	public interface OnAction {
		void action(Order c);
	}

}
