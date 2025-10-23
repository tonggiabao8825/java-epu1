# Banking Management System

Hệ thống quản lý tài khoản ngân hàng đơn giản sử dụng Java và MongoDB.

## Tính năng

### 1. Quản lý tài khoản (CRUD)
- ✅ Tạo tài khoản mới
- ✅ Xem danh sách tài khoản
- ✅ Cập nhật thông tin tài khoản
- ✅ Xóa tài khoản

### 2. Giao dịch
- ✅ Nạp tiền
- ✅ Rút tiền
- ✅ Chuyển khoản giữa các tài khoản
- ✅ Kiểm tra số dư trước khi giao dịch
- ✅ Kiểm tra kết nối Internet
- ✅ Tạo mã QR Code cho tài khoản

### 3. Sao kê & Báo cáo
- ✅ Sao kê theo ngày
- ✅ Sao kê theo tuần
- ✅ Sao kê theo tháng
- ✅ Sao kê theo năm
- ✅ Xuất sao kê ra file PDF
- ✅ Xuất sao kê ra file DOCX

### 4. Thông báo
- ✅ Thông báo giao dịch qua Console
- ✅ Hỗ trợ gửi Email (cần cấu hình)
- ✅ Hỗ trợ gửi SMS (cần cấu hình)

## Yêu cầu hệ thống

- Java JDK 11 trở lên
- Apache Maven 3.6+
- MongoDB 4.0+
- Kết nối Internet (cho một số tính năng)

## Cài đặt

### 1. Cài đặt MongoDB

**Windows:**
```bash
# Download MongoDB từ: https://www.mongodb.com/try/download/community
# Sau khi cài đặt, chạy MongoDB:
mongod
```

**Linux:**
```bash
sudo apt-get update
sudo apt-get install -y mongodb
sudo systemctl start mongodb
sudo systemctl enable mongodb
```

**MacOS:**
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

### 2. Clone và Build Project

```bash
cd /home/barodev/java_course/exam/banking-system
mvn clean install
```

### 3. Cấu hình

Chỉnh sửa file `src/main/resources/application.properties`:

```properties
# Cấu hình MongoDB
mongodb.connection.string=mongodb://localhost:27017
mongodb.database.name=banking_system

# Cấu hình Email (tùy chọn)
email.from=your-email@gmail.com
email.password=your-app-password
```

## Chạy ứng dụng

### Cách 1: Chạy từ Maven
```bash
mvn exec:java -Dexec.mainClass="com.banking.gui.BankingGUI"
```

### Cách 2: Chạy JAR file
```bash
# Build JAR với dependencies
mvn clean package

# Chạy JAR
java -jar target/banking-system-1.0.0-jar-with-dependencies.jar
```

### Cách 3: Chạy 2 instances cùng lúc (giả lập 2 tài khoản)
```bash
# Terminal 1
java -jar target/banking-system-1.0.0-jar-with-dependencies.jar

# Terminal 2 (mở terminal mới)
java -jar target/banking-system-1.0.0-jar-with-dependencies.jar
```

## Cấu trúc Project

```
banking-system/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── banking/
│       │           ├── model/              # Các class model
│       │           │   ├── Account.java
│       │           │   ├── Transaction.java
│       │           │   └── TransactionType.java
│       │           ├── repository/         # Data Access Layer
│       │           │   ├── AccountRepository.java
│       │           │   └── TransactionRepository.java
│       │           ├── service/            # Business Logic Layer
│       │           │   ├── AccountService.java
│       │           │   └── TransactionService.java
│       │           ├── util/               # Utilities
│       │           │   ├── MongoDBConnection.java
│       │           │   ├── QRCodeGenerator.java
│       │           │   ├── PDFExporter.java
│       │           │   ├── DOCXExporter.java
│       │           │   ├── NotificationService.java
│       │           │   └── InternetChecker.java
│       │           └── gui/                # Giao diện
│       │               └── BankingGUI.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

## Hướng dẫn sử dụng

### 1. Tạo tài khoản mới
1. Điền thông tin: Số TK, Tên, Số dư, Ngân hàng
2. Nhấn nút "Tạo TK"

### 2. Xem và chọn tài khoản
- Click vào dòng trong bảng "Danh sách tài khoản"
- Thông tin sẽ tự động điền vào form và hiển thị lịch sử giao dịch

### 3. Thực hiện giao dịch
- **Nạp tiền**: Chọn tài khoản → Nhấn "Nạp tiền" → Nhập số tiền
- **Rút tiền**: Chọn tài khoản → Nhấn "Rút tiền" → Nhập số tiền
- **Chuyển khoản**: Chọn TK nguồn → Nhấn "Chuyển khoản" → Nhập TK đích và số tiền

### 4. Xem sao kê
- Chọn tài khoản
- Nhấn "Sao kê ngày/tuần/tháng/năm" để xem giao dịch theo kỳ
- Nhấn "Xuất PDF" hoặc "Xuất DOCX" để xuất file

### 5. Tạo QR Code
- Chọn tài khoản
- Nhấn "Tạo QR"
- File QR code sẽ được lưu với tên `qr_<số_tk>.png`

## Demo: Chạy 2 chương trình cùng lúc

```bash
# Terminal 1
cd /home/barodev/java_course/exam/banking-system
java -jar target/banking-system-1.0.0-jar-with-dependencies.jar

# Terminal 2 (mở cửa sổ terminal mới)
cd /home/barodev/java_course/exam/banking-system
java -jar target/banking-system-1.0.0-jar-with-dependencies.jar
```

Sau đó:
1. Tạo tài khoản A trong cửa sổ 1
2. Tạo tài khoản B trong cửa sổ 2
3. Thực hiện chuyển khoản từ A sang B trong cửa sổ 1
4. Nhấn "Làm mới" ở cửa sổ 2 để thấy tiền đã nhận

## Troubleshooting

### Lỗi: Could not connect to MongoDB
```bash
# Kiểm tra MongoDB có đang chạy không
sudo systemctl status mongodb   # Linux
brew services list              # MacOS
```

### Lỗi: Port already in use
- MongoDB mặc định chạy trên port 27017
- Kiểm tra: `netstat -an | grep 27017`

### Lỗi: Package không tìm thấy
```bash
# Clean và rebuild project
mvn clean install -U
```

## Technologies Used

- **Java 11**: Programming language
- **MongoDB**: NoSQL Database
- **Maven**: Build tool
- **Swing**: GUI Framework
- **ZXing**: QR Code generation
- **iText**: PDF generation
- **Apache POI**: DOCX generation
- **JavaMail**: Email support

## License

This project is for educational purposes.

## Author

- Barodev
- Date: October 23, 2025
