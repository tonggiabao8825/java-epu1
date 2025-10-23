package com.banking.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetChecker {
    
    public static boolean isConnected() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static String getConnectionStatus() {
        return isConnected() ? "Đã kết nối Internet" : "Không có kết nối Internet";
    }
}
