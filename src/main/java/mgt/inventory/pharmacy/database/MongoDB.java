package mgt.inventory.pharmacy.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.CreateCollectionOptions;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;

public class MongoDB
{
    private static MongoClient mongoClient;
    private static Datastore dataStore;
    
    @Value("${spring.data.mongodb.uri}")
    private static String mongoDBUrl;
    
    @Value("${mongoDb.database}")
    private static String database;
    
    public static void startDB()
    {
        mongoClient = new MongoClient(new MongoClientURI(mongoDBUrl));
        dataStore = new Morphia().createDatastore(mongoClient, database);
        dataStore.ensureIndexes();
    }
    
    public static void stopDB()
    {
    
    }
    
    public static void createCollections()
    {
        MongoIterable<String> collections = getDb().listCollectionNames();
        HashSet<String> cols = new HashSet<>();
        for (String j : collections)
        {
            cols.add(j);
        }
        
        CreateCollectionOptions opts = new CreateCollectionOptions();
        
        if (!cols.contains("product"))
        {
            getDb().createCollection("", opts);
        }
        
        if (!cols.contains("stock"))
        {
            getDb().createCollection("stock", opts);
        }
        if (!cols.contains("reorder"))
        {
            getDb().createCollection("reorder", opts);
        }
        if (!cols.contains("order"))
        {
            getDb().createCollection("order", opts);
        }
        if (!cols.contains("customer"))
        {
            getDb().createCollection("customer", opts);
        }
    }
    
    private static MongoDatabase getDb()
    {
        return mongoClient.getDatabase(database);
    }
    
}