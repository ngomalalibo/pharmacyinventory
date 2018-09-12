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

import mgt.inventory.pharmacy.entities.Product;

public class EditProductDialog extends MDialog {

	private static final long serialVersionUID = -4136556525370459308L;

	public EditProductDialog(final Product productbean, DialogAction action, OnAction onaction) {
		super(action);

		Binder<Product> binder = new Binder<>(Product.class);
		binder.setBean(new Product());

		switch (action) {
		case NEW:
			setHeader("New Inventory Product");
			break;
		case EDIT:
			setHeader("Edit Inventory Product");
			break;
		case DELETE:
			setHeader("Delete Inventory Product?");
			break;
		case VIEW:
			setHeader("Inventory Product");
			break;
		}

		TextField productCode = new TextField("Product Code");
		productCode.setPlaceholder("XXX-XXXXX");
		binder.forField(productCode).asRequired("Please enter a product code")
				.withValidator(new StringLengthValidator("Please add a valid product code", 4, 15)).bind("productCode");
		
		TextField productName = new TextField("Product Name");
		binder.forField(productName).asRequired("Please enter a product name").bind("productName");
		
		ComboBox<String> category = new ComboBox<>("Product Category", Product._categories);
		binder.forField(category).asRequired("Please choose a category").bind("productCategory");
		
		TextField manufacturer = new TextField("Product Manufacturer");
		binder.forField(manufacturer).asRequired("Please enter a manufacturer").bind("productManufacturer");
		
		TextArea productDesc = new TextArea("Product Description");
		productDesc.getElement().setAttribute("colspan", "2").setAttribute("theme", "small");
		productDesc.getStyle().set("minHeight", "12em");
		binder.forField(productDesc).bind("productDescription");
		
		DatePicker manRelDate = new DatePicker("Manufacturer Release Date");
		binder.forField(manRelDate).bind("productManufactureReleaseDate");

		DatePicker createdDate = new DatePicker("Created Date");
		LocalDateTime crdt = productbean.getCreatedDate();
		createdDate.setValue(crdt != null ? crdt.toLocalDate() : LocalDate.now());
		createdDate.setReadOnly(true);

		Checkbox isprescription = new Checkbox("Prescription Required");
		binder.forField(isprescription).bind("prescription");

		FormLayout form = new FormLayout(productCode, productName,  category, manufacturer, productDesc, manRelDate, productDesc, isprescription, createdDate);
		form.setResponsiveSteps(new ResponsiveStep("0", 2));
		form.setWidth("500px");
		setContents(form);

		binder.readBean(productbean);

		// all fields will be locked if viewing or in delete mode
		binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);

		if (action != DialogAction.VIEW) {
			Button actionbtn = new Button("Save");
			actionbtn.getElement().setAttribute("theme", "small primary");
			actionbtn.addClickListener(e -> {
				if (binder.validate().isOk()) {
					if (binder.writeBeanIfValid(productbean)) {
						onaction.action(productbean);
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
							onaction.action(productbean);
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
		void action(Product c);
	}

}
