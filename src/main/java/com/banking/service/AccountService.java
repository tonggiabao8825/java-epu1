package com.banking.service;

import com.banking.model.Account;
import com.banking.repository.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;
    
    public AccountService() {
        this.accountRepository = new AccountRepository();
    }
    
    public Account createAccount(String accountNumber, String accountName, double initialBalance, String bankName) {
        // Check if account already exists
        if (accountRepository.findByAccountNumber(accountNumber) != null) {
            throw new IllegalArgumentException("Số tài khoản đã tồn tại!");
        }
        
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Số dư ban đầu không thể âm!");
        }
        
        Account account = new Account(accountNumber, accountName, initialBalance, bankName);
        return accountRepository.create(account);
    }
    
    public Account getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản!");
        }
        return account;
    }
    
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public boolean updateAccount(String accountNumber, String newName, String newBankName) {
        Account account = getAccount(accountNumber);
        account.setAccountName(newName);
        account.setBankName(newBankName);
        return accountRepository.update(account);
    }
    
    public boolean deleteAccount(String accountNumber) {
        Account account = getAccount(accountNumber);
        if (account.getBalance() > 0) {
            throw new IllegalArgumentException("Không thể xóa tài khoản có số dư!");
        }
        return accountRepository.delete(accountNumber);
    }
    
    public boolean accountExists(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber) != null;
    }
    
    public double getBalance(String accountNumber) {
        return getAccount(accountNumber).getBalance();
    }
}
