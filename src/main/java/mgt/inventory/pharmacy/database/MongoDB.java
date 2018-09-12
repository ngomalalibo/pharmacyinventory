package mgt.inventory.pharmacy.database;

import java.util.*;

import mgt.inventory.pharmacy.entities.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

//Manages connection to MongoDB database and creates collections
public class MongoDB {
	private static MongoClient mongoClient;
	private static Datastore dataStore;

	static Logger logger = LoggerFactory.getLogger(MongoDB.class);

	// @Value("${spring.data.mongodb.uri}")
	// private static String mongoDBUrl;
	// @Value("${mongoDb.database}")
	// private static String database;

	private static String mongoDBUrl = "mongodb+srv://project1:YxhAWI55nUImLa2h@cluster0-fcg0z.mongodb.net/admin";
	private static String database = "pharmacy";

	public static void startDB() {
		logger.info("**************** startDB ****************");
		mongoClient = new MongoClient(new MongoClientURI(mongoDBUrl));
		dataStore = new Morphia().createDatastore(mongoClient, database);
		dataStore.ensureIndexes();
		createCollections();
	}

	public static void stopDB() {
		logger.info("**************** stopDB ****************");
		if (mongoClient != null)
			mongoClient.close();
		mongoClient = null;
		dataStore = null;
	}

	public static Datastore getDS() {
		return dataStore;
	}

	public static MongoDatabase getDb() {
		return mongoClient.getDatabase(database);
	}

	public static void createCollections() {
		logger.info("**************** createCollections ****************");
		MongoIterable<String> collections = getDb().listCollectionNames();
		HashSet<String> cols = new HashSet<>();
		for (String j : collections) {
			cols.add(j);
		}

		if (!cols.contains("products")) {
			getDb().createCollection("products");
		}
		if (!cols.contains("stocks")) {
			getDb().createCollection("stocks");
		}
		//Renamed from reorders to products
		if (!cols.contains("purchases")) {
			getDb().createCollection("purchases");
		}
		if (!cols.contains("orders")) {
			getDb().createCollection("orders");
		}
		if (!cols.contains("customers")) {
			getDb().createCollection("customers");
		}
		// Adding Employee table
		if (!cols.contains("employees")) {
			getDb().createCollection("employees");
		}
	}

	public static List<Product> getProducts() {
		return getDS().createQuery(Product.class).asList();
	}
	
	public static List<Customer> getCustomers() {
		return getDS().createQuery(Customer.class).asList();
	}
	
	public static List<Employee> getEmployees() {
		return getDS().createQuery(Employee.class).asList();
	}
	
	public static List<Order> getOrders() {
		return getDS().createQuery(Order.class).asList();
	}
	
	public static List<PurchaseOrder> getPurchaseOrders() {
		return getDS().createQuery(PurchaseOrder.class).asList();
	}
	
	//db.users.find({}, { _id: 0, email: 1 })
	
	//Returns a single column from a collection
	/*Query q = getDS().createQuery(Employee.class);
		return q.filter("productCode", Product.class).asList();*/
		
	public static List<Product> getProductCombo() {
		return getDS().createQuery(Product.class).asList();
	}
	
	/*public static HashMap<String, String> getProductCombo() {
		List<Product> products = getDS().createQuery(Product.class).asList();
		HashMap<String, String> productCombo = new HashMap<>();
		for(Product product: products)
		{
			productCombo.put(product.getProductCode(), product.getProductName());
		}
		return productCombo;
	}*/
	
	public static List<Employee> getEmployeeCombo() {
		return getDS().createQuery(Employee.class).asList();
		
	}
	
	
	public static Product getProduct(String productCode) {
		return getDS().createQuery(Product.class).filter("productCode", productCode).get();
	}
	
	public static List<StockTaking> getStockTaking() {
		return getDS().createQuery(StockTaking.class).order("createdDate").asList();
	}
	
	public static List<StockTaking> getStockTaking(String productCode) {
		return getDS().createQuery(StockTaking.class).filter("productCode", productCode).order("createdDate").asList();
	}
	
	public static class ProductStockTaking {
		public Product product;
		public StockTaking stockTaking;
	}
	
	public static List<ProductStockTaking> getProductStock() {
		List<Product> products = getProducts(); // all item
		List<StockTaking> stockTaking = getStockTaking(); // all stock
		//stock.sort((a, b) -> a.createdDate.compareTo(b.createdDate)); // sort by date ascending
		
		List<ProductStockTaking> productStockTaking = new ArrayList<>();
		
		for (int n = 0; n < products.size(); n++) {
			ProductStockTaking stk = new ProductStockTaking();
			stk.product = products.get(n);
			
			Optional<StockTaking> opt = stockTaking.stream().filter(sc -> sc.getProductCode().equals(stk.product.getProductCode())).findFirst();
			
			if (opt.isPresent())
				stk.stockTaking = opt.get();
			
			productStockTaking.add(stk);
		}
		
		return productStockTaking;
	}
}