package com.banking.util;

import com.banking.model.Account;
import com.banking.model.Transaction;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DOCXExporter {
    
    public static void exportStatement(Account account, List<Transaction> transactions, 
                                      String filePath, String period) {
        try (XWPFDocument document = new XWPFDocument()) {
            
            // Add title
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("SAO KE TAI KHOAN NGAN HANG");
            titleRun.setBold(true);
            titleRun.setFontSize(18);
            titleRun.addBreak();
            titleRun.addBreak();
            
            // Add account info
            XWPFParagraph info = document.createParagraph();
            XWPFRun infoRun = info.createRun();
            infoRun.setText("Thong tin tai khoan:");
            infoRun.setBold(true);
            infoRun.addBreak();
            infoRun.setText("So tai khoan: " + account.getAccountNumber());
            infoRun.addBreak();
            infoRun.setText("Ten: " + account.getAccountName());
            infoRun.addBreak();
            infoRun.setText("Ngan hang: " + account.getBankName());
            infoRun.addBreak();
            infoRun.setText("So du hien tai: " + String.format("%,.0f VND", account.getBalance()));
            infoRun.addBreak();
            infoRun.setText("Ky sao ke: " + period);
            infoRun.addBreak();
            infoRun.addBreak();
            
            // Create transaction table
            XWPFTable table = document.createTable();
            
            // Add headers
            XWPFTableRow headerRow = table.getRow(0);
            headerRow.getCell(0).setText("Ngay gio");
            headerRow.addNewTableCell().setText("Loai GD");
            headerRow.addNewTableCell().setText("So tien");
            headerRow.addNewTableCell().setText("Mo ta");
            headerRow.addNewTableCell().setText("Trang thai");
            
            // Add transaction data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (Transaction trans : transactions) {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(trans.getTransactionDate().format(formatter));
                row.getCell(1).setText(trans.getType().getDescription());
                row.getCell(2).setText(String.format("%,.0f", trans.getAmount()));
                row.getCell(3).setText(trans.getDescription() != null ? trans.getDescription() : "");
                row.getCell(4).setText(trans.getStatus());
            }
            
            // Add footer
            XWPFParagraph footer = document.createParagraph();
            XWPFRun footerRun = footer.createRun();
            footerRun.addBreak();
            footerRun.setText("Tong so giao dich: " + transactions.size());
            
            // Save document
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                document.write(out);
            }
            
            System.out.println("Da xuat sao ke DOCX thanh cong: " + filePath);
            
        } catch (Exception e) {
            System.err.println("Loi khi xuat DOCX: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void exportTransactionReceipt(Transaction transaction, Account account, String filePath) {
        try (XWPFDocument document = new XWPFDocument()) {
            
            // Add title
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("BIEN NHAN GIAO DICH");
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.addBreak();
            titleRun.addBreak();
            
            // Add transaction info
            XWPFParagraph info = document.createParagraph();
            XWPFRun infoRun = info.createRun();
            infoRun.setText("So tai khoan: " + account.getAccountNumber());
            infoRun.addBreak();
            infoRun.setText("Ten: " + account.getAccountName());
            infoRun.addBreak();
            infoRun.setText("Loai giao dich: " + transaction.getType().getDescription());
            infoRun.addBreak();
            infoRun.setText("So tien: " + String.format("%,.0f VND", transaction.getAmount()));
            infoRun.addBreak();
            infoRun.setText("Mo ta: " + (transaction.getDescription() != null ? transaction.getDescription() : ""));
            infoRun.addBreak();
            infoRun.setText("Thoi gian: " + transaction.getTransactionDate());
            infoRun.addBreak();
            infoRun.setText("Trang thai: " + transaction.getStatus());
            
            // Save document
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                document.write(out);
            }
            
            System.out.println("Da xuat bien nhan DOCX thanh cong: " + filePath);
            
        } catch (Exception e) {
            System.err.println("Loi khi xuat DOCX: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
