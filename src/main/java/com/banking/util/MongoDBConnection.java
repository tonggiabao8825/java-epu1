package com.banking.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoDBConnection {
    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    private MongoDBConnection() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");
            if (input != null) {
                props.load(input);
            }
            
            String connectionString = props.getProperty("mongodb.connection.string", "mongodb://localhost:27017");
            String databaseName = props.getProperty("mongodb.database.name", "banking_system");
            
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);
            
            System.out.println("Connected to MongoDB successfully!");
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            throw new RuntimeException("Failed to connect to MongoDB", e);
        }
    }
    
    public static MongoDBConnection getInstance() {
        if (instance == null) {
            synchronized (MongoDBConnection.class) {
                if (instance == null) {
                    instance = new MongoDBConnection();
                }
            }
        }
        return instance;
    }
    
    public MongoDatabase getDatabase() {
        return database;
    }
    
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
