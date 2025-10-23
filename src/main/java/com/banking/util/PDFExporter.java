package com.banking.util;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFExporter {
    
    public static void exportStatement(Account account, List<Transaction> transactions, 
                                      String filePath, String period) {
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("SAO KE TAI KHOAN NGAN HANG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Add account info
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Thong tin tai khoan:", normalFont));
            document.add(new Paragraph("So tai khoan: " + account.getAccountNumber(), normalFont));
            document.add(new Paragraph("Ten: " + account.getAccountName(), normalFont));
            document.add(new Paragraph("Ngan hang: " + account.getBankName(), normalFont));
            document.add(new Paragraph("So du hien tai: " + String.format("%,.0f VND", account.getBalance()), normalFont));
            document.add(new Paragraph("Ky sao ke: " + period, normalFont));
            document.add(new Paragraph("\n"));
            
            // Create transaction table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Add headers
            addTableHeader(table, "Ngay gio");
            addTableHeader(table, "Loai GD");
            addTableHeader(table, "So tien");
            addTableHeader(table, "Mo ta");
            addTableHeader(table, "Trang thai");
            
            // Add transaction data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (Transaction trans : transactions) {
                table.addCell(trans.getTransactionDate().format(formatter));
                table.addCell(trans.getType().getDescription());
                table.addCell(String.format("%,.0f", trans.getAmount()));
                table.addCell(trans.getDescription() != null ? trans.getDescription() : "");
                table.addCell(trans.getStatus());
            }
            
            document.add(table);
            
            // Add footer
            Paragraph footer = new Paragraph("\nTong so giao dich: " + transactions.size(), normalFont);
            document.add(footer);
            
            document.close();
            System.out.println("Da xuat sao ke PDF thanh cong: " + filePath);
            
        } catch (Exception e) {
            System.err.println("Loi khi xuat PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void addTableHeader(PdfPTable table, String headerTitle) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase(headerTitle, headerFont));
        table.addCell(header);
    }
    
    public static void exportTransactionReceipt(Transaction transaction, Account account, String filePath) {
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("BIEN NHAN GIAO DICH", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("So tai khoan: " + account.getAccountNumber(), normalFont));
            document.add(new Paragraph("Ten: " + account.getAccountName(), normalFont));
            document.add(new Paragraph("Loai giao dich: " + transaction.getType().getDescription(), normalFont));
            document.add(new Paragraph("So tien: " + String.format("%,.0f VND", transaction.getAmount()), normalFont));
            document.add(new Paragraph("Mo ta: " + (transaction.getDescription() != null ? transaction.getDescription() : ""), normalFont));
            document.add(new Paragraph("Thoi gian: " + transaction.getTransactionDate(), normalFont));
            document.add(new Paragraph("Trang thai: " + transaction.getStatus(), normalFont));
            
            document.close();
            System.out.println("Da xuat bien nhan PDF thanh cong: " + filePath);
            
        } catch (Exception e) {
            System.err.println("Loi khi xuat PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
