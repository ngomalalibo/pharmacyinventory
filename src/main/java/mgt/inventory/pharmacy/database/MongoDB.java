package mgt.inventory.pharmacy.database;

import java.util.HashSet;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import mgt.inventory.pharmacy.entities.Item;

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

		if (!cols.contains("product")) {
			getDb().createCollection("product");
		}
		if (!cols.contains("stock")) {
			getDb().createCollection("stock");
		}
		if (!cols.contains("reorder")) {
			getDb().createCollection("reorder");
		}
		if (!cols.contains("order")) {
			getDb().createCollection("order");
		}
		if (!cols.contains("customer")) {
			getDb().createCollection("customer");
		}
	}

	public static List<Item> getItems() {
		return getDS().createQuery(Item.class).asList();
	}

}