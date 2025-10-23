package com.banking.service;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.util.InternetChecker;
import com.banking.util.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    
    public TransactionService() {
        this.accountRepository = new AccountRepository();
        this.transactionRepository = new TransactionRepository();
        this.notificationService = new NotificationService();
    }
    
    public Transaction deposit(String accountNumber, double amount, String description) {
        validateAmount(amount);
        
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại!");
        }
        
        // Update balance
        double newBalance = account.getBalance() + amount;
        accountRepository.updateBalance(accountNumber, newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(null, accountNumber, amount, 
                TransactionType.DEPOSIT, description);
        transactionRepository.create(transaction);
        
        // Send notification
        notificationService.sendTransactionNotification(account, transaction);
        
        return transaction;
    }
    
    public Transaction withdraw(String accountNumber, double amount, String description) {
        validateAmount(amount);
        
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại!");
        }
        
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Số dư không đủ để thực hiện giao dịch!");
        }
        
        // Update balance
        double newBalance = account.getBalance() - amount;
        accountRepository.updateBalance(accountNumber, newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(accountNumber, null, amount, 
                TransactionType.WITHDRAWAL, description);
        transactionRepository.create(transaction);
        
        // Send notification
        notificationService.sendTransactionNotification(account, transaction);
        
        return transaction;
    }
    
    public Transaction transfer(String fromAccountNumber, String toAccountNumber, 
                               double amount, String description) {
        // Check internet connection
        if (!InternetChecker.isConnected()) {
            throw new IllegalStateException("Không có kết nối Internet!");
        }
        
        validateAmount(amount);
        
        // Validate sender account
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        if (fromAccount == null) {
            throw new IllegalArgumentException("Tài khoản người gửi không tồn tại!");
        }
        
        // Validate receiver account
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        if (toAccount == null) {
            throw new IllegalArgumentException("Tài khoản người nhận không tồn tại!");
        }
        
        // Check balance
        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Số dư không đủ để thực hiện giao dịch!");
        }
        
        // Perform transfer
        accountRepository.updateBalance(fromAccountNumber, fromAccount.getBalance() - amount);
        accountRepository.updateBalance(toAccountNumber, toAccount.getBalance() + amount);
        
        // Create transaction record
        Transaction transaction = new Transaction(fromAccountNumber, toAccountNumber, amount, 
                TransactionType.TRANSFER_OUT, description);
        transactionRepository.create(transaction);
        
        // Send notifications
        notificationService.sendTransactionNotification(fromAccount, transaction);
        notificationService.sendTransactionNotification(toAccount, transaction);
        
        return transaction;
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }
    
    public List<Transaction> getStatementByDateRange(String accountNumber, 
                                                     LocalDateTime startDate, 
                                                     LocalDateTime endDate) {
        return transactionRepository.findByAccountNumberAndDateRange(accountNumber, startDate, endDate);
    }
    
    public List<Transaction> getDailyStatement(String accountNumber) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        return getStatementByDateRange(accountNumber, startOfDay, now);
    }
    
    public List<Transaction> getWeeklyStatement(String accountNumber) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusWeeks(1);
        return getStatementByDateRange(accountNumber, weekAgo, now);
    }
    
    public List<Transaction> getMonthlyStatement(String accountNumber) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthAgo = now.minusMonths(1);
        return getStatementByDateRange(accountNumber, monthAgo, now);
    }
    
    public List<Transaction> getYearlyStatement(String accountNumber) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yearAgo = now.minusYears(1);
        return getStatementByDateRange(accountNumber, yearAgo, now);
    }
    
    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền phải lớn hơn 0!");
        }
    }
}
