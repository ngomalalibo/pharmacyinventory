package mgt.inventory.pharmacy.entities;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "item", noClassnameStored = true)
@Indexes(@Index(fields = { @Field(value = "productCode") }))
public class Item extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String productCode;
	private String productName;
	private String productCategory;
	private String productManufacturer;
	private String productDescription;
	private LocalDate productManufactureReleaseDate;
	private boolean prescription;

	public Item() {
	}

	public Item(String productCode) {
		this.productCode = productCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductManufacturer() {
		return productManufacturer;
	}

	public void setProductManufacturer(String productManufacturer) {
		this.productManufacturer = productManufacturer;
	}

	public LocalDate getProductManufactureReleaseDate() {
		return productManufactureReleaseDate;
	}

	public void setProductManufactureReleaseDate(LocalDate productManufactureReleaseDate) {
		this.productManufactureReleaseDate = productManufactureReleaseDate;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	@Override
	public String toString() {
		return "Item [productCode=" + productCode + ", productName=" + productName + ", productCategory="
				+ productCategory + ", productManufacturer=" + productManufacturer + ", productDescription="
				+ productDescription + ", productManufactureReleaseDate=" + productManufactureReleaseDate
				+ ", prescription=" + prescription + "]";
	}

	public boolean isPrescription() {
		return prescription;
	}

	public void setPrescription(boolean prescription) {
		this.prescription = prescription;
	}

	public static final List<String> _categories = Arrays.asList("Narcotics", "Depressants", "Stimulants",
			"Hallucinogens", "Anabolic", "Steroids", "Analgesic", "Other");

}
