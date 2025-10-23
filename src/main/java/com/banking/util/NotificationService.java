package com.banking.util;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class NotificationService {
    
    public void sendTransactionNotification(Account account, Transaction transaction) {
        // In a real application, you would send SMS/Email here
        // For this demo, we'll just print to console
        String message = formatTransactionMessage(account, transaction);
        System.out.println("\n=== THÔNG BÁO GIAO DỊCH ===");
        System.out.println(message);
        System.out.println("===========================\n");
    }
    
    private String formatTransactionMessage(Account account, Transaction transaction) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tài khoản: ").append(account.getAccountNumber()).append("\n");
        sb.append("Tên: ").append(account.getAccountName()).append("\n");
        sb.append("Loại giao dịch: ").append(transaction.getType().getDescription()).append("\n");
        sb.append("Số tiền: ").append(String.format("%,.0f VNĐ", transaction.getAmount())).append("\n");
        
        if (transaction.getType() == TransactionType.TRANSFER_OUT) {
            sb.append("Người nhận: ").append(transaction.getToAccountNumber()).append("\n");
        } else if (transaction.getType() == TransactionType.TRANSFER_IN) {
            sb.append("Người gửi: ").append(transaction.getFromAccountNumber()).append("\n");
        }
        
        sb.append("Nội dung: ").append(transaction.getDescription()).append("\n");
        sb.append("Thời gian: ").append(transaction.getTransactionDate()).append("\n");
        sb.append("Số dư mới: ").append(String.format("%,.0f VNĐ", account.getBalance()));
        
        return sb.toString();
    }
    
    // Method to send email (requires configuration)
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            
            // You need to configure your email credentials
            String fromEmail = "your-email@gmail.com";
            String password = "your-password";
            
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    // Simulated SMS sending
    public void sendSMS(String phoneNumber, String message) {
        System.out.println("\n=== GỬI SMS ===");
        System.out.println("Số điện thoại: " + phoneNumber);
        System.out.println("Nội dung: " + message);
        System.out.println("===============\n");
    }
}
