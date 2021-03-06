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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import mgt.inventory.pharmacy.database.MongoDB;
import mgt.inventory.pharmacy.database.MongoDB.ProductStockTaking;
import mgt.inventory.pharmacy.entities.Product;
import mgt.inventory.pharmacy.entities.StockTaking;
import mgt.inventory.pharmacy.ui.dialogs.EditProductDialog;
import mgt.inventory.pharmacy.ui.dialogs.EditProductStockTakingDialog;
import mgt.inventory.pharmacy.ui.dialogs.MDialog.DialogAction;

@PageTitle("Products")
@Route(value = "product", layout = NavMenuWrapper.class)
public class ProductCRUDPage extends FlexLayout {

	private static final long serialVersionUID = 1L;

	private Span stockCount = new Span();

	private final List<ProductStockTaking> allProductStock = new ArrayList<>();

	private Grid<ProductStockTaking> grid = new Grid<>();

	public ProductCRUDPage() {

		getStyle()//
				.set("padding", "1em")//
				.set("flex-grow", "1")//
				.set("flexDirection", "column");

		stockCount.getStyle().set("color", "#777777");
		
		Button createProduct = new Button("Add New Product");
		createProduct.getElement().setAttribute("theme", "small primary");
		createProduct.addClickListener(click -> newProductView());

		FlexLayout flex = new FlexLayout(new H3("Products"), createProduct);
		flex.setAlignItems(Alignment.CENTER);
		flex.setJustifyContentMode(JustifyContentMode.BETWEEN);
		add(flex);

		grid.setDataProvider(new ListDataProvider<>(allProductStock));
		grid.setSelectionMode(SelectionMode.NONE);
		grid.addComponentColumn(ps -> {
			Button pcode = new Button(ps.product.getProductCode());
			pcode.getElement().setAttribute("theme", "tertiary");
			pcode.setClassName("font-size-s");
			pcode.addClickListener(clk -> openProductView(ps.product));
			return pcode;
		}).setHeader("Product Code").setWidth("6em").setFooter(stockCount).setFlexGrow(0).setResizable(true);
 
		grid.addColumn(ps -> ps.product.getProductName()).setHeader("Product Name").setWidth("10em").setFlexGrow(0).setResizable(true);
		grid.addColumn(ps -> ps.product.getProductCategory()).setHeader("Product Category").setWidth("8em").setFlexGrow(0).setResizable(true);
		grid.addColumn(ps -> ps.product.getProductManufacturer()).setHeader("Manufacturer").setWidth("6em").setFlexGrow(0).setResizable(true);
		//grid.addColumn(ps -> ps.product.getProductManufactureReleaseDate()).setHeader("Release Date").setWidth("8em");
		grid.addColumn(ps -> ps.stockTaking != null ? ps.stockTaking.getQuantityInStock().toString() : "")
				.setHeader("Quanitity Available").setWidth("5em").setFlexGrow(0).setResizable(true);
		//grid.addColumn(ps -> ps.stockTaking != null ? ps.stockTaking.getReorderLevel() : "").setHeader("Reorder Level").setWidth("5em");
		//grid.addColumn(ps -> ps.stockTaking != null ? ps.stockTaking.getDiscontinueDate() : "")
				//.setHeader("Discontinue Date").setWidth("9em");
		grid.addColumn(ps -> ps.stockTaking != null && ps.stockTaking.getStockTakenBy() != null ? ps.stockTaking.getStockTakenBy().getFullName() : "").setHeader("Taken By").setWidth("10em").setFlexGrow(0).setResizable(true);
		//grid.addColumn(ps -> ps.stockTaking != null ? (ps.stockTaking.getStockTakenBy().getFullName()) : "").setHeader("Taken By").setWidth("15em");
		grid.addColumn(ps -> ps.product.getCreatedDate().format(DateTimeFormatter.ISO_DATE)).setHeader("Created").setWidth("8em").setFlexGrow(0).setResizable(true);

		grid.addComponentColumn(ps -> {
			Div fl = new Div();
			fl.getStyle().set("textAlign", "right");

			Button edit = new Button(new IronIcon("lumo", "edit"));
			edit.getElement().setAttribute("theme", "small success icon");
			edit.getStyle().set("marginRight", "0.4em");
			edit.setText("Edit Product");
			edit.addClickListener(click -> editProductView(ps.product));

			Button editStock = new Button(new IronIcon("lumo", "edit"));
			editStock.getElement().setAttribute("theme", "small icon");
			editStock.getStyle().set("marginRight", "0.4em");
			editStock.setText("Edit Stock");
			editStock.addClickListener(click -> editStockView(ps));

			Button delete = new Button(new IronIcon("lumo", "cross"));
			delete.getElement().setAttribute("theme", "small icon error");
			delete.addClickListener(click -> deleteProductView(ps.product));

			fl.add(edit, editStock, delete);
			return fl;
		}).setFlexGrow(0).setWidth("260px");

		add(grid);
		refreshDataTable();
	}

	private void openProductView(Product product) {
		new EditProductDialog(product, DialogAction.VIEW, i -> refreshDataTable()).open();
	}

	private void openStockView(ProductStockTaking ps) {
		new EditProductStockTakingDialog(ps, DialogAction.VIEW, i -> refreshDataTable()).open();
	}

	private void editProductView(Product product) {
		new EditProductDialog(product, DialogAction.EDIT, i -> {
			i.persist(i);
			refreshDataTable();
		}).open();
	}

	private void editStockView(ProductStockTaking ps) {
		new EditProductStockTakingDialog(ps, DialogAction.EDIT, i -> {
			i.persist(i);
			refreshDataTable();
		}).open();
	}

	private void deleteProductView(Product product) {
		new EditProductDialog(product, DialogAction.DELETE, i -> {
			i.delete(i);
			refreshDataTable();
		}).open();
	}

	private void newProductView() {
		new EditProductDialog(new Product(), DialogAction.NEW, i -> {
			i.persist(i);
			refreshDataTable();
		}).open();
	}

	private void newStockView() {
		ProductStockTaking ps = new ProductStockTaking();
		new EditProductStockTakingDialog(ps, DialogAction.NEW, i -> {
			i.persist(i);
			refreshDataTable();
		}).open();
	}

	private void refreshDataTable() {
		allProductStock.clear();
		allProductStock.addAll(MongoDB.getProductStock());
		updateStockCount(allProductStock.size());
		grid.getDataProvider().refreshAll();
	}

	private void updateStockCount(int count) {
		stockCount.setText(String.format("Total: %d", count));
	}

}
