package mgt.inventory.pharmacy.ui.dialogs;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import mgt.inventory.pharmacy.database.MongoDB;
import mgt.inventory.pharmacy.entities.Employee;
import mgt.inventory.pharmacy.entities.Product;
import mgt.inventory.pharmacy.entities.PurchaseOrder;
import mgt.inventory.pharmacy.entities.StockTaking;
import mgt.inventory.pharmacy.ui.DoubleNumberTextField;
import mgt.inventory.pharmacy.ui.NumberTextField;

public class EditPurchaseOrderDialog extends MDialog {

	private static final long serialVersionUID = -4136556525370459308L;

	public EditPurchaseOrderDialog(final PurchaseOrder purchaseBean, DialogAction action, OnAction onaction) {
		super(action);

		Binder<PurchaseOrder> binder = new Binder<>(PurchaseOrder.class);
		binder.setBean(new PurchaseOrder());

		switch (action) {
		case NEW:
			setHeader("New Purchase Order");
			break;
		case EDIT:
			setHeader("Edit Purchase Order");
			break;
		case DELETE:
			setHeader("Delete Purchase Order");
			break;
		case VIEW:
			setHeader("Purchase Order");
			break;
		}

		TextField purchaseId = new TextField("Purchase ID");
		purchaseId.setPlaceholder("XXX-XXXXX");
		binder.forField(purchaseId).asRequired("Please enter purchase ID").bind("purchaseId");

		List<Product> allproduct = MongoDB.getProductCombo();
		ComboBox<Product> product = new ComboBox<>("Product", allproduct);
		product.setItemLabelGenerator(prod -> prod.getProductName());

		binder.forField(product).asRequired("Please select product").bind(purchaseOrder -> {
			Optional<Product> opt = allproduct.stream()
					.filter(p -> p.getProductCode().equals(purchaseOrder.getProductCode())).findFirst();
			return (opt.isPresent()) ? opt.get() : null;
		}, (purchaseOrder, product1) -> purchaseOrder
				.setProductCode(product1 != null ? product1.getProductCode() : null));

		List<Employee> allemployee = MongoDB.getEmployeeCombo();

		ComboBox<Employee> purchaseBy = new ComboBox<>("Purchased By", allemployee);
		purchaseBy.setItemLabelGenerator(emp -> emp.getFullName());
		binder.forField(purchaseBy).asRequired("Please select employee making purchase").bind("purchaseBy");

		// .bind(purchaseOrder -> {
		// final String empid;
		// if (purchaseOrder != null && purchaseOrder.getPurchaseBy() != null)
		// empid = purchaseOrder.getPurchaseBy().getEmployeeId();
		// else
		// empid = null;
		//
		// Optional<Employee> opt = allemployee.stream().filter(emp ->
		// emp.getEmployeeId().equals(empid)).findFirst();
		// return opt.isPresent() ? opt.get() : null;
		// }, (purchaseOrder, product2) -> purchaseOrder.setPurchaseBy(product2));

		NumberTextField quantity = new NumberTextField("Quantity");
		binder.forField(quantity).asRequired("Please enter quantity purchased").bind("quantity");

		DatePicker purchaseDate = new DatePicker("Purchase Date");
		binder.forField(purchaseDate).bind("purchaseDate");

		TextField supplier = new TextField("Supplier");
		supplier.setPlaceholder("XXX-XXXXX");
		binder.forField(supplier).asRequired("Please enter supplier").bind("supplierId");

		DoubleNumberTextField unitPrice = new DoubleNumberTextField("Unit Price");
		unitPrice.setPrefixComponent(new Span("₦"));
		unitPrice.setPlaceholder("XXX-XXXXX");
		binder.forField(unitPrice).asRequired("Please enter unit Price purchased").bind("unitPrice");

		FormLayout form = new FormLayout(purchaseId, purchaseBy, product, quantity, purchaseDate, supplier, unitPrice);
		form.setResponsiveSteps(new ResponsiveStep("0", 2));
		form.setWidth("500px");
		setContents(form);

		binder.readBean(purchaseBean);

		// all fields will be locked if viewing or in delete mode
		binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);

		if (action != DialogAction.VIEW) {
			Button actionbtn = new Button("Save");
			actionbtn.getElement().setAttribute("theme", "small primary");
			actionbtn.addClickListener(e -> {

				if (binder.validate().isOk()) {
					if (binder.writeBeanIfValid(purchaseBean)) {
						String productCode = binder.getBean().getProductCode();
						StockTaking st = MongoDB.getLatestStockTaken(productCode);
						if (st == null) {
							new Notification("No Current Stock found for Product " + productCode, 2000, Position.MIDDLE)
									.open();
							StockTaking stocktaken = new StockTaking();
							stocktaken.setProductCode(productCode);
							stocktaken.setQuantityInStock(purchaseBean.getQuantity());
							stocktaken.setStockTakenBy(purchaseBean.getPurchaseBy());
							stocktaken.setStockTakenDate(LocalDate.now());
							stocktaken.persist(stocktaken);
						} else {
							System.out.println("st -> " + st.toString());
							st.setQuantityInStock(purchaseBean.getQuantity() + st.getQuantityInStock());
							// addStockQuantity(purchaseBean.getProductCode(), purchaseBean.getQuantity());
							System.out.println("Purchase Order Recorded");

							st.persist(st);
						}
						purchaseBean.persist(purchaseBean);
						onaction.action();
						close();
					} else {
						new Notification("Bean NOT Valid", 2000).open();
					}
				} else {
					System.out.println("-> " + binder.validate().getBeanValidationErrors().stream()
							.map(ve -> ve.getErrorMessage()).collect(Collectors.joining(",")));
					System.out.println("-> " + binder.getBean().toString());
				}
			});

			if (action == DialogAction.DELETE) {
				Button deletebtn = new Button("Delete");
				deletebtn.getElement().setAttribute("theme", "error small");
				deletebtn.setVisible(action == DialogAction.EDIT);
				deletebtn.addClickListener(e -> new ActionConfirmDialog("Delete Shipping Address?",
						"Are you sure you want to delete this address?", DialogAction.DELETE, () -> {
							purchaseBean.delete(purchaseBean);
							onaction.action();
							close();
						}).open());
				addActions(deletebtn);
			}

			addTerminalActions(actionbtn);

			setCloseOnEsc(false);
			setCloseOnOutsideClick(false);
		}
	}

	private void addStockQuantity(String productCode, Integer quantity) {
		MongoDB.addNewStock(productCode, quantity);
		System.out.println("Purchase Order Stock Updated");
	}

	public interface OnAction {
		void action();
	}

}
