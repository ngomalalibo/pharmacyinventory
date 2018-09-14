package mgt.inventory.pharmacy.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import java.time.LocalDate;

@Entity(value = "purchases", noClassnameStored = true)
@Indexes(@Index(fields = { @Field(value = "purchaseId") }))
public class PurchaseOrder extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String purchaseId;
	private Employee purchaseBy;

	private LocalDate purchaseDate;
	private String productCode;

	private String supplierId;
	private Integer quantity;
	private Double unitPrice;

	public PurchaseOrder() {

	}

	public Employee getPurchaseBy() {
		return purchaseBy;
	}

	public void setPurchaseBy(Employee purchaseBy) {
		this.purchaseBy = purchaseBy;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Override
	public String toString() {
		return "PurchaseOrder{" + "purchaseId='" + purchaseId + '\'' + ", employee=" + purchaseBy + ", purchaseDate='"
				+ purchaseDate + '\'' + ", supplierId='" + supplierId + '\'' + ", quantity=" + quantity + ", unitPrice="
				+ unitPrice + '}';
	}
}
