package mgt.inventory.pharmacy.entities;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "stocks", noClassnameStored = true)
@Indexes(@Index(fields = {@Field(value = "stockId")}))
public class StockTaking extends BaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String stockId;
    private Employee stockTakenBy;
    private LocalDate stockTakenDate;
    private String productCode;
    private Integer quantityInStock;
    private Integer reorderLevel;
    private LocalDate discontinueDate;
    
    public String getProductCode()
    {
        return productCode;
    }
    
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }
    
    public Employee getStockTakenBy()
    {
        return stockTakenBy;
    }
    
    public void setStockTakenBy(Employee stockTakenBy)
    {
        this.stockTakenBy = stockTakenBy;
    }
    
    public LocalDate getStockTakenDate()
    {
        return stockTakenDate;
    }
    
    public void setStockTakenDate(LocalDate stockTakenDate)
    {
        this.stockTakenDate = stockTakenDate;
    }
    
    public Integer getQuantityInStock()
    {
        return quantityInStock;
    }
    
    public void setQuantityInStock(Integer quantityInStock)
    {
        this.quantityInStock = quantityInStock;
    }
    
    public Integer getReorderLevel()
    {
        return reorderLevel;
    }
    
    public void setReorderLevel(Integer reorderLevel)
    {
        this.reorderLevel = reorderLevel;
    }
    
    public LocalDate getDiscontinueDate()
    {
        return discontinueDate;
    }
    
    public void setDiscontinueDate(LocalDate discontinueDate)
    {
        this.discontinueDate = discontinueDate;
    }
    
    public StockTaking()
    {
    }
    
    @Override
    public String toString()
    {
        return "StockTaking{" +
                ", stockTakenBy=" + stockTakenBy +
                ", stockTakenDate=" + stockTakenDate +
                ", productCode=" + productCode +
                ", quantityInStock=" + quantityInStock +
                ", reorderLevel=" + reorderLevel +
                ", discontinueDate=" + discontinueDate +
                '}';
    }
    
    public String getStockId()
    {
        return stockId;
    }
    
    public void setStockId(String stockId)
    {
        this.stockId = stockId;
    }
}
