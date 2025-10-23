package com.banking.gui;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.AccountService;
import com.banking.service.TransactionService;
import com.banking.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BankingGUI extends JFrame {
    private AccountService accountService;
    private TransactionService transactionService;
    private JTextField txtAccountNumber, txtAccountName, txtBalance, txtBankName;
    private JTable accountTable, transactionTable;
    private DefaultTableModel accountTableModel, transactionTableModel;
    private JLabel lblCurrentAccount, lblCurrentBalance;
    
    public BankingGUI() {
        accountService = new AccountService();
        transactionService = new TransactionService();
        
        initComponents();
        loadAccounts();
        
        setTitle("BANKING MANAGEMENT SYSTEM");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Top Panel - Account Info
        JPanel topPanel = createAccountPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center Panel - Tables
        JPanel centerPanel = createTablesPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom Panel - Transactions
        JPanel bottomPanel = createTransactionPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Quản lý tài khoản"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Số TK:"), gbc);
        gbc.gridx = 1;
        txtAccountNumber = new JTextField(15);
        panel.add(txtAccountNumber, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 3;
        txtAccountName = new JTextField(15);
        panel.add(txtAccountName, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Số dư:"), gbc);
        gbc.gridx = 1;
        txtBalance = new JTextField(15);
        panel.add(txtBalance, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Ngân hàng:"), gbc);
        gbc.gridx = 3;
        txtBankName = new JTextField(15);
        panel.add(txtBankName, gbc);
        
        // Row 3 - Buttons
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnCreate = new JButton("Tạo TK");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnQRCode = new JButton("Tạo QR");
        
        btnCreate.addActionListener(e -> createAccount());
        btnUpdate.addActionListener(e -> updateAccount());
        btnDelete.addActionListener(e -> deleteAccount());
        btnRefresh.addActionListener(e -> {
            clearForm();
            loadAccounts();
        });
        btnQRCode.addActionListener(e -> generateQRCode());
        
        btnPanel.add(btnCreate);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnQRCode);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        panel.add(btnPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Accounts Table
        JPanel accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBorder(BorderFactory.createTitledBorder("Danh sách tài khoản"));
        
        accountTableModel = new DefaultTableModel(
            new String[]{"Số TK", "Tên", "Số dư", "Ngân hàng"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        accountTable = new JTable(accountTableModel);
        accountTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectAccount();
            }
        });
        accountPanel.add(new JScrollPane(accountTable), BorderLayout.CENTER);
        
        // Transactions Table
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Lịch sử giao dịch"));
        
        transactionTableModel = new DefaultTableModel(
            new String[]{"Ngày giờ", "Loại", "Số tiền", "Mô tả", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(transactionTableModel);
        transactionPanel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        
        panel.add(accountPanel);
        panel.add(transactionPanel);
        
        return panel;
    }
    
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Giao dịch"));
        
        // Current account info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblCurrentAccount = new JLabel("Chưa chọn tài khoản");
        lblCurrentBalance = new JLabel("");
        infoPanel.add(lblCurrentAccount);
        infoPanel.add(new JLabel(" | "));
        infoPanel.add(lblCurrentBalance);
        panel.add(infoPanel);
        
        // Transaction buttons row 1
        JPanel btnPanel1 = new JPanel(new FlowLayout());
        JButton btnDeposit = new JButton("Nạp tiền");
        JButton btnWithdraw = new JButton("Rút tiền");
        JButton btnTransfer = new JButton("Chuyển khoản");
        
        btnDeposit.addActionListener(e -> deposit());
        btnWithdraw.addActionListener(e -> withdraw());
        btnTransfer.addActionListener(e -> transfer());
        
        btnPanel1.add(btnDeposit);
        btnPanel1.add(btnWithdraw);
        btnPanel1.add(btnTransfer);
        panel.add(btnPanel1);
        
        // Transaction buttons row 2
        JPanel btnPanel2 = new JPanel(new FlowLayout());
        JButton btnDaily = new JButton("Sao kê ngày");
        JButton btnWeekly = new JButton("Sao kê tuần");
        JButton btnMonthly = new JButton("Sao kê tháng");
        JButton btnYearly = new JButton("Sao kê năm");
        JButton btnExportPDF = new JButton("Xuất PDF");
        JButton btnExportDOCX = new JButton("Xuất DOCX");
        
        btnDaily.addActionListener(e -> showStatement("DAY"));
        btnWeekly.addActionListener(e -> showStatement("WEEK"));
        btnMonthly.addActionListener(e -> showStatement("MONTH"));
        btnYearly.addActionListener(e -> showStatement("YEAR"));
        btnExportPDF.addActionListener(e -> exportStatement("PDF"));
        btnExportDOCX.addActionListener(e -> exportStatement("DOCX"));
        
        btnPanel2.add(btnDaily);
        btnPanel2.add(btnWeekly);
        btnPanel2.add(btnMonthly);
        btnPanel2.add(btnYearly);
        btnPanel2.add(btnExportPDF);
        btnPanel2.add(btnExportDOCX);
        panel.add(btnPanel2);
        
        return panel;
    }
    
    private void createAccount() {
        try {
            String accountNumber = txtAccountNumber.getText().trim();
            String accountName = txtAccountName.getText().trim();
            String balanceStr = txtBalance.getText().trim();
            String bankName = txtBankName.getText().trim();
            
            if (accountNumber.isEmpty() || accountName.isEmpty() || balanceStr.isEmpty() || bankName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double balance = Double.parseDouble(balanceStr);
            accountService.createAccount(accountNumber, accountName, balance, bankName);
            
            JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadAccounts();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số dư phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateAccount() {
        try {
            String accountNumber = txtAccountNumber.getText().trim();
            String accountName = txtAccountName.getText().trim();
            String bankName = txtBankName.getText().trim();
            
            if (accountNumber.isEmpty() || accountName.isEmpty() || bankName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            accountService.updateAccount(accountNumber, accountName, bankName);
            
            JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadAccounts();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteAccount() {
        try {
            String accountNumber = txtAccountNumber.getText().trim();
            
            if (accountNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa tài khoản này?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                accountService.deleteAccount(accountNumber);
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadAccounts();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deposit() {
        String accountNumber = getCurrentAccountNumber();
        if (accountNumber == null) return;
        
        String amountStr = JOptionPane.showInputDialog(this, "Nhập số tiền nạp:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;
        
        try {
            double amount = Double.parseDouble(amountStr);
            String description = JOptionPane.showInputDialog(this, "Mô tả giao dịch:");
            
            transactionService.deposit(accountNumber, amount, description);
            
            JOptionPane.showMessageDialog(this, "Nạp tiền thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadAccounts();
            selectAccount();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void withdraw() {
        String accountNumber = getCurrentAccountNumber();
        if (accountNumber == null) return;
        
        String amountStr = JOptionPane.showInputDialog(this, "Nhập số tiền rút:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;
        
        try {
            double amount = Double.parseDouble(amountStr);
            String description = JOptionPane.showInputDialog(this, "Mô tả giao dịch:");
            
            transactionService.withdraw(accountNumber, amount, description);
            
            JOptionPane.showMessageDialog(this, "Rút tiền thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadAccounts();
            selectAccount();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void transfer() {
        String fromAccount = getCurrentAccountNumber();
        if (fromAccount == null) return;
        
        String toAccount = JOptionPane.showInputDialog(this, "Nhập số tài khoản người nhận:");
        if (toAccount == null || toAccount.trim().isEmpty()) return;
        
        String amountStr = JOptionPane.showInputDialog(this, "Nhập số tiền chuyển:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;
        
        try {
            double amount = Double.parseDouble(amountStr);
            String description = JOptionPane.showInputDialog(this, "Nội dung chuyển khoản:");
            
            transactionService.transfer(fromAccount, toAccount, amount, description);
            
            JOptionPane.showMessageDialog(this, "Chuyển khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadAccounts();
            selectAccount();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showStatement(String period) {
        String accountNumber = getCurrentAccountNumber();
        if (accountNumber == null) return;
        
        try {
            List<Transaction> transactions;
            switch (period) {
                case "DAY":
                    transactions = transactionService.getDailyStatement(accountNumber);
                    break;
                case "WEEK":
                    transactions = transactionService.getWeeklyStatement(accountNumber);
                    break;
                case "MONTH":
                    transactions = transactionService.getMonthlyStatement(accountNumber);
                    break;
                case "YEAR":
                    transactions = transactionService.getYearlyStatement(accountNumber);
                    break;
                default:
                    transactions = transactionService.getTransactionHistory(accountNumber);
            }
            
            loadTransactions(transactions);
            JOptionPane.showMessageDialog(this, 
                String.format("Đã tải %d giao dịch", transactions.size()), 
                "Thông tin", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportStatement(String format) {
        String accountNumber = getCurrentAccountNumber();
        if (accountNumber == null) return;
        
        try {
            Account account = accountService.getAccount(accountNumber);
            List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
            
            String fileName = String.format("statement_%s_%s.%s", 
                accountNumber, 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")),
                format.toLowerCase());
            
            if (format.equals("PDF")) {
                PDFExporter.exportStatement(account, transactions, fileName, "Tất cả");
            } else {
                DOCXExporter.exportStatement(account, transactions, fileName, "Tất cả");
            }
            
            JOptionPane.showMessageDialog(this, 
                "Đã xuất file: " + fileName, 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateQRCode() {
        String accountNumber = txtAccountNumber.getText().trim();
        String bankName = txtBankName.getText().trim();
        
        if (accountNumber.isEmpty() || bankName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String fileName = String.format("qr_%s.png", accountNumber);
            QRCodeGenerator.saveQRCodeToFile(bankName, accountNumber, fileName, 300, 300);
            JOptionPane.showMessageDialog(this, "Đã tạo QR code: " + fileName, "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadAccounts() {
        accountTableModel.setRowCount(0);
        List<Account> accounts = accountService.getAllAccounts();
        for (Account account : accounts) {
            accountTableModel.addRow(new Object[]{
                account.getAccountNumber(),
                account.getAccountName(),
                String.format("%,.0f", account.getBalance()),
                account.getBankName()
            });
        }
    }
    
    private void loadTransactions(List<Transaction> transactions) {
        transactionTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Transaction trans : transactions) {
            transactionTableModel.addRow(new Object[]{
                trans.getTransactionDate().format(formatter),
                trans.getType().getDescription(),
                String.format("%,.0f", trans.getAmount()),
                trans.getDescription(),
                trans.getStatus()
            });
        }
    }
    
    private void selectAccount() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow >= 0) {
            txtAccountNumber.setText(accountTableModel.getValueAt(selectedRow, 0).toString());
            txtAccountName.setText(accountTableModel.getValueAt(selectedRow, 1).toString());
            String balanceStr = accountTableModel.getValueAt(selectedRow, 2).toString().replace(",", "");
            txtBalance.setText(balanceStr);
            txtBankName.setText(accountTableModel.getValueAt(selectedRow, 3).toString());
            
            lblCurrentAccount.setText("TK: " + txtAccountNumber.getText());
            lblCurrentBalance.setText("Số dư: " + accountTableModel.getValueAt(selectedRow, 2).toString() + " VNĐ");
            
            // Load transactions
            try {
                List<Transaction> transactions = transactionService.getTransactionHistory(txtAccountNumber.getText());
                loadTransactions(transactions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void clearForm() {
        txtAccountNumber.setText("");
        txtAccountName.setText("");
        txtBalance.setText("");
        txtBankName.setText("");
        lblCurrentAccount.setText("Chưa chọn tài khoản");
        lblCurrentBalance.setText("");
        transactionTableModel.setRowCount(0);
    }
    
    private String getCurrentAccountNumber() {
        String accountNumber = txtAccountNumber.getText().trim();
        if (accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return accountNumber;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankingGUI gui = new BankingGUI();
            gui.setVisible(true);
        });
    }
}
