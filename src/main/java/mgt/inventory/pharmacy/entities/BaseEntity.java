package mgt.inventory.pharmacy.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class BaseEntity implements Serializable
{
    @Id
    private ObjectId id;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime modifiedDate;
    
    
    public void persist()
    {
    
    }
    
    public void delete()
    {
    
    }
    
    public abstract String toString();
}
