package com.banking.repository;

import com.banking.model.Transaction;
import com.banking.model.TransactionType;
import com.banking.util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionRepository {
    private final MongoCollection<Document> collection;
    
    public TransactionRepository() {
        this.collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("transactions");
    }
    
    public Transaction create(Transaction transaction) {
        Document doc = new Document()
                .append("fromAccountNumber", transaction.getFromAccountNumber())
                .append("toAccountNumber", transaction.getToAccountNumber())
                .append("amount", transaction.getAmount())
                .append("type", transaction.getType().name())
                .append("description", transaction.getDescription())
                .append("transactionDate", Date.from(transaction.getTransactionDate().atZone(ZoneId.systemDefault()).toInstant()))
                .append("status", transaction.getStatus())
                .append("failureReason", transaction.getFailureReason());
        
        collection.insertOne(doc);
        transaction.setId(doc.getObjectId("_id"));
        return transaction;
    }
    
    public List<Transaction> findByAccountNumber(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        Bson filter = Filters.or(
                Filters.eq("fromAccountNumber", accountNumber),
                Filters.eq("toAccountNumber", accountNumber)
        );
        collection.find(filter).forEach(doc -> transactions.add(documentToTransaction(doc)));
        return transactions;
    }
    
    public List<Transaction> findByAccountNumberAndDateRange(String accountNumber, 
                                                             LocalDateTime startDate, 
                                                             LocalDateTime endDate) {
        List<Transaction> transactions = new ArrayList<>();
        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        
        Bson filter = Filters.and(
                Filters.or(
                        Filters.eq("fromAccountNumber", accountNumber),
                        Filters.eq("toAccountNumber", accountNumber)
                ),
                Filters.gte("transactionDate", start),
                Filters.lte("transactionDate", end)
        );
        
        collection.find(filter).forEach(doc -> transactions.add(documentToTransaction(doc)));
        return transactions;
    }
    
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        collection.find().forEach(doc -> transactions.add(documentToTransaction(doc)));
        return transactions;
    }
    
    private Transaction documentToTransaction(Document doc) {
        Transaction transaction = new Transaction();
        transaction.setId(doc.getObjectId("_id"));
        transaction.setFromAccountNumber(doc.getString("fromAccountNumber"));
        transaction.setToAccountNumber(doc.getString("toAccountNumber"));
        transaction.setAmount(doc.getDouble("amount"));
        transaction.setType(TransactionType.valueOf(doc.getString("type")));
        transaction.setDescription(doc.getString("description"));
        transaction.setStatus(doc.getString("status"));
        transaction.setFailureReason(doc.getString("failureReason"));
        
        Date transactionDate = doc.getDate("transactionDate");
        if (transactionDate != null) {
            transaction.setTransactionDate(
                    LocalDateTime.ofInstant(transactionDate.toInstant(), ZoneId.systemDefault())
            );
        }
        
        return transaction;
    }
}
