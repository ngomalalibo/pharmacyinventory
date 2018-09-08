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
import mgt.inventory.pharmacy.entities.Item;
import mgt.inventory.pharmacy.ui.dialogs.EditItemDialog;
import mgt.inventory.pharmacy.ui.dialogs.MDialog.DialogAction;

@PageTitle("Item Crud")
@Route(value = "itemcrud", layout = BasePage.class)
public class ItemCRUDPage extends FlexLayout {

	private static final long serialVersionUID = 1L;

	private H4 stockCount = new H4();

	private final List<Item> allitems = new ArrayList<>();

	private Grid<Item> grid = new Grid<>();

	public ItemCRUDPage() {

		getStyle()//
				.set("padding", "1em")//
				.set("flex-grow", "1")//
				.set("flexDirection", "column");

		stockCount.getStyle().set("color", "#777777");

		FlexLayout flex = new FlexLayout(new H3("Items"), stockCount);
		flex.setAlignItems(Alignment.CENTER);
		flex.setJustifyContentMode(JustifyContentMode.BETWEEN);
		add(flex);

		Button createItem = new Button("Add New Item");
		createItem.getElement().setAttribute("theme", "small primary");
		createItem.addClickListener(click -> newItemView());
		add(new Div(createItem));

		grid.setDataProvider(new ListDataProvider<>(allitems));
		grid.setSelectionMode(SelectionMode.NONE);
		grid.addComponentColumn(item -> {
			Button pcode = new Button(item.getProductCode());
			pcode.getElement().setAttribute("theme", "tertiary");
			pcode.setClassName("font-size-s");
			pcode.addClickListener(clk -> openItemView(item));
			return pcode;
		}).setHeader("Product Code");

		grid.addColumn(Item::getProductName).setHeader("Name");
		grid.addColumn(Item::getProductManufacturer).setHeader("Manufacturer");
		grid.addColumn(Item::getProductCategory).setHeader("Category");
		grid.addColumn(i -> i.getCreatedDate().format(DateTimeFormatter.ISO_DATE)).setHeader("Created");

		grid.addComponentColumn(item -> {
			Div fl = new Div();
			fl.getStyle().set("textAlign", "right");

			Button edit = new Button(new IronIcon("lumo", "edit"));
			edit.getElement().setAttribute("theme", "small icon");
			edit.getStyle().set("marginRight", "0.4em");
			edit.addClickListener(click -> editItemView(item));

			Button delete = new Button(new IronIcon("lumo", "cross"));
			delete.getElement().setAttribute("theme", "small icon error");
			delete.addClickListener(click -> deleteItemView(item));

			fl.add(edit, delete);
			return fl;
		}).setFlexGrow(0).setWidth("160px");

		add(grid);
		refreshDataTable();
	}

	private void openItemView(Item item) {
		new EditItemDialog(item, DialogAction.VIEW, i -> refreshDataTable()).open();
	}

	private void editItemView(Item item) {
		new EditItemDialog(item, DialogAction.EDIT, i -> {
			i.persist(i);
			refreshDataTable();
		}).open();
	}

	private void deleteItemView(Item item) {
		new EditItemDialog(item, DialogAction.DELETE, i -> {
			i.delete(i);
			refreshDataTable();
		}).open();
	}

	private void newItemView() {
		new EditItemDialog(new Item(), DialogAction.NEW, i -> {
			i.persist(i);
			refreshDataTable();
		}).open();
	}

	private void refreshDataTable() {
		allitems.clear();
		allitems.addAll(MongoDB.getItems());
		updateStockCount(allitems.size());
		grid.getDataProvider().refreshAll();
	}

	private void updateStockCount(int count) {
		stockCount.setText(String.format("Total Stock: %d", count));
	}

}
