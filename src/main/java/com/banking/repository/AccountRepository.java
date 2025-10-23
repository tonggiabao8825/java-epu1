package com.banking.repository;

import com.banking.model.Account;
import com.banking.util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountRepository {
    private final MongoCollection<Document> collection;
    
    public AccountRepository() {
        this.collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("accounts");
    }
    
    public Account create(Account account) {
        Document doc = new Document()
                .append("accountNumber", account.getAccountNumber())
                .append("accountName", account.getAccountName())
                .append("balance", account.getBalance())
                .append("bankName", account.getBankName())
                .append("createdAt", Date.from(account.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()))
                .append("updatedAt", Date.from(account.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        
        collection.insertOne(doc);
        account.setId(doc.getObjectId("_id"));
        return account;
    }
    
    public Account findByAccountNumber(String accountNumber) {
        Document doc = collection.find(Filters.eq("accountNumber", accountNumber)).first();
        return doc != null ? documentToAccount(doc) : null;
    }
    
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        collection.find().forEach(doc -> accounts.add(documentToAccount(doc)));
        return accounts;
    }
    
    public boolean update(Account account) {
        return collection.updateOne(
                Filters.eq("accountNumber", account.getAccountNumber()),
                Updates.combine(
                        Updates.set("accountName", account.getAccountName()),
                        Updates.set("balance", account.getBalance()),
                        Updates.set("bankName", account.getBankName()),
                        Updates.set("updatedAt", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                )
        ).getModifiedCount() > 0;
    }
    
    public boolean updateBalance(String accountNumber, double newBalance) {
        return collection.updateOne(
                Filters.eq("accountNumber", accountNumber),
                Updates.combine(
                        Updates.set("balance", newBalance),
                        Updates.set("updatedAt", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                )
        ).getModifiedCount() > 0;
    }
    
    public boolean delete(String accountNumber) {
        return collection.deleteOne(Filters.eq("accountNumber", accountNumber)).getDeletedCount() > 0;
    }
    
    private Account documentToAccount(Document doc) {
        Account account = new Account();
        account.setId(doc.getObjectId("_id"));
        account.setAccountNumber(doc.getString("accountNumber"));
        account.setAccountName(doc.getString("accountName"));
        account.setBalance(doc.getDouble("balance"));
        account.setBankName(doc.getString("bankName"));
        
        Date createdAt = doc.getDate("createdAt");
        if (createdAt != null) {
            account.setCreatedAt(LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault()));
        }
        
        Date updatedAt = doc.getDate("updatedAt");
        if (updatedAt != null) {
            account.setUpdatedAt(LocalDateTime.ofInstant(updatedAt.toInstant(), ZoneId.systemDefault()));
        }
        
        return account;
    }
}
