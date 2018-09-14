package mgt.inventory.pharmacy.entities;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "orders", noClassnameStored = true)
@Indexes(@Index(fields = {@Field(value = "orderId")}))
public class Order extends BaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String orderId;
    private String productCode;
    private String customerId;
    private int quantity;
    private double unitSellingPrice;
    private String receiptNo;
    private LocalDate orderDate;
    
    public Order()
    {
    }
    
    @Override
    public String toString()
    {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", customerId='" + customerId + '\'' +
                ", quantity=" + quantity +
                ", unitSellingPrice=" + unitSellingPrice +
                ", receiptNo='" + receiptNo + '\'' +
                '}';
    }
    
    public Double getUnitSellingPrice()
    {
        return unitSellingPrice;
    }
    
    public void setUnitSellingPrice(Double unitSellingPrice)
    {
        this.unitSellingPrice = unitSellingPrice;
    }
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
    public String getProductCode()
    {
        return productCode;
    }
    
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public Integer getQuantity()
    {
        return quantity;
    }
    
    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }
    
    public String getReceiptNo()
    {
        return receiptNo;
    }
    
    public void setReceiptNo(String receiptNo)
    {
        this.receiptNo = receiptNo;
    }
    
    public LocalDate getOrderDate()
    {
        return orderDate;
    }
    
    public void setOrderDate(LocalDate orderDate)
    {
        this.orderDate = orderDate;
    }
}
