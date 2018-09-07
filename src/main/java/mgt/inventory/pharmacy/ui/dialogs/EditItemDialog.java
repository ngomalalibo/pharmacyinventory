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

import mgt.inventory.pharmacy.entities.Item;

public class EditItemDialog extends MDialog {

	private static final long serialVersionUID = -4136556525370459308L;

	public EditItemDialog(final Item itembean, DialogAction action, OnAction onaction) {
		super(action);

		Binder<Item> binder = new Binder<>(Item.class);
		binder.setBean(new Item());

		switch (action) {
		case NEW:
			setHeader("New Inventory Item");
			break;
		case EDIT:
			setHeader("Edit Inventory Item");
			break;
		case DELETE:
			setHeader("Delete Inventory Item?");
			break;
		case VIEW:
			setHeader("Inventory Item");
			break;
		}

		TextField productCode = new TextField("Product Code");
		productCode.setPlaceholder("XXX-XXXXX");
		binder.forField(productCode).asRequired("Please enter a product code")
				.withValidator(new StringLengthValidator("Please add a valid product code", 4, 15)).bind("productCode");

		DatePicker createdDate = new DatePicker("Created Date");
		LocalDateTime crdt = itembean.getCreatedDate();
		createdDate.setValue(crdt != null ? crdt.toLocalDate() : LocalDate.now());
		createdDate.setReadOnly(true);

		TextField productName = new TextField("Product Name");
		binder.forField(productName).asRequired("Please enter a product name").bind("productName");

		TextField manufacturer = new TextField("Product Manufacturer");
		binder.forField(manufacturer).asRequired("Please enter a manufacturer").bind("productManufacturer");

		DatePicker releaseDate = new DatePicker("Manufacturer Market Date");
		binder.forField(releaseDate).bind("productManufactureReleaseDate");

		ComboBox<String> category = new ComboBox<>("Product Category", Item._categories);
		binder.forField(category).asRequired("Please choose a category").bind("productCategory");

		TextArea productDesc = new TextArea("Product Description");
		productDesc.getElement().setAttribute("colspan", "2").setAttribute("theme", "small");
		productDesc.getStyle().set("minHeight", "12em");
		binder.forField(productDesc).bind("productDescription");

		Checkbox isprescription = new Checkbox("Prescription Required");
		binder.forField(isprescription).bind("prescription");

		FormLayout form = new FormLayout(productCode, createdDate, productName, manufacturer, releaseDate, category,
				isprescription, productDesc);
		form.setResponsiveSteps(new ResponsiveStep("0", 2));
		form.setWidth("500px");
		setContents(form);

		binder.readBean(itembean);

		// all fields will be locked if viewing or in delete mode
		binder.setReadOnly(action == DialogAction.DELETE || action == DialogAction.VIEW);

		if (action != DialogAction.VIEW) {
			Button actionbtn = new Button("Save");
			actionbtn.getElement().setAttribute("theme", "small primary");
			actionbtn.addClickListener(e -> {
				if (binder.validate().isOk()) {
					if (binder.writeBeanIfValid(itembean)) {
						onaction.action(itembean);
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
							onaction.action(itembean);
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
		void action(Item c);
	}

}
