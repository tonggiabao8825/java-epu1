package com.banking.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeGenerator {
    
    public static BufferedImage generateQRCode(String bankName, String accountNumber, int width, int height) 
            throws WriterException {
        String qrContent = String.format("Bank: %s\nAccount: %s", bankName, accountNumber);
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);
        
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    public static void saveQRCodeToFile(String bankName, String accountNumber, 
                                       String filePath, int width, int height) 
            throws WriterException, IOException {
        BufferedImage qrImage = generateQRCode(bankName, accountNumber, width, height);
        File outputFile = new File(filePath);
        ImageIO.write(qrImage, "PNG", outputFile);
    }
    
    public static String parseQRCode(String qrContent) {
        // Simple parsing: extract account number from QR content
        String[] lines = qrContent.split("\n");
        for (String line : lines) {
            if (line.startsWith("Account:")) {
                return line.substring("Account:".length()).trim();
            }
        }
        return null;
    }
}
