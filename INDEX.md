# 📚 BANKING MANAGEMENT SYSTEM - INDEX

## 📖 Tài liệu hệ thống

### 🚀 Bắt đầu nhanh
- **[QUICKSTART.md](QUICKSTART.md)** - Hướng dẫn nhanh 5 phút
  - Cài đặt nhanh
  - Test cơ bản
  - Demo scenarios
  - Troubleshooting

### 📋 Tổng quan
- **[README.md](README.md)** - Tài liệu chính
  - Mô tả tính năng đầy đủ
  - Yêu cầu hệ thống
  - Hướng dẫn cài đặt
  - Hướng dẫn sử dụng
  - Technologies used

### 🛠️ Cài đặt chi tiết
- **[SETUP.md](SETUP.md)** - Hướng dẫn setup từng bước
  - Cài đặt MongoDB
  - Cài đặt Java & Maven
  - Build project
  - Chạy ứng dụng (1 hoặc 2 instances)
  - Kiểm tra database
  - Xử lý các lỗi thường gặp

### 📊 Tóm tắt dự án
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Tổng kết dự án
  - Thông tin chung
  - Danh sách yêu cầu đã hoàn thành
  - Kiến trúc hệ thống
  - Công nghệ sử dụng
  - Cấu trúc file
  - Database schema
  - Tính năng nổi bật

### 🏗️ Kiến trúc
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Sơ đồ kiến trúc
  - Sơ đồ tổng quan
  - Sơ đồ luồng giao dịch
  - Sơ đồ class diagram
  - Sơ đồ utilities
  - Sơ đồ multi-instance
  - Luồng nghiệp vụ chi tiết
  - Database schema
  - Use case diagram

---

## 📂 Cấu trúc Source Code

### Model Layer (`src/main/java/com/banking/model/`)
- `Account.java` - Entity tài khoản ngân hàng
- `Transaction.java` - Entity giao dịch
- `TransactionType.java` - Enum loại giao dịch

### Repository Layer (`src/main/java/com/banking/repository/`)
- `AccountRepository.java` - CRUD operations cho Account
- `TransactionRepository.java` - CRUD operations cho Transaction

### Service Layer (`src/main/java/com/banking/service/`)
- `AccountService.java` - Business logic cho Account
- `TransactionService.java` - Business logic cho Transaction

### Utility Layer (`src/main/java/com/banking/util/`)
- `MongoDBConnection.java` - Kết nối MongoDB
- `QRCodeGenerator.java` - Tạo mã QR
- `PDFExporter.java` - Xuất file PDF
- `DOCXExporter.java` - Xuất file DOCX
- `NotificationService.java` - Gửi thông báo
- `InternetChecker.java` - Kiểm tra Internet

### GUI Layer (`src/main/java/com/banking/gui/`)
- `BankingGUI.java` - Giao diện Swing

### Resources (`src/main/resources/`)
- `application.properties` - Cấu hình ứng dụng

---

## 🔧 File cấu hình

### Build Configuration
- **[pom.xml](pom.xml)** - Maven configuration
  - Dependencies
  - Build plugins
  - Compiler settings

### Application Configuration
- **[application.properties](src/main/resources/application.properties)** - App settings
  - MongoDB connection
  - Email settings
  - SMS settings

### Scripts
- **[run.sh](run.sh)** - Script chạy 1 instance
- **[run-dual.sh](run-dual.sh)** - Script chạy 2 instances

---

## 📊 Thống kê Project

| Thành phần | Số lượng |
|------------|----------|
| Java files | 14 |
| Model classes | 3 |
| Repository classes | 2 |
| Service classes | 2 |
| Utility classes | 6 |
| GUI classes | 1 |
| Configuration files | 2 |
| Documentation files | 5 |
| Script files | 2 |
| **Total files** | **25+** |

---

## 🎯 Use Cases chính

### 1. Quản lý tài khoản (CRUD)
```
Files involved:
- Account.java
- AccountRepository.java
- AccountService.java
- BankingGUI.java
```

### 2. Thực hiện giao dịch
```
Files involved:
- Transaction.java
- TransactionRepository.java
- TransactionService.java
- InternetChecker.java
- NotificationService.java
- BankingGUI.java
```

### 3. Xuất sao kê
```
Files involved:
- PDFExporter.java
- DOCXExporter.java
- TransactionService.java
- BankingGUI.java
```

### 4. Tạo QR Code
```
Files involved:
- QRCodeGenerator.java
- BankingGUI.java
```

---

## 🚀 Quick Commands

### Build & Run
```bash
# Build project
mvn clean install

# Run 1 instance
./run.sh

# Run 2 instances
./run-dual.sh
```

### Database
```bash
# Connect to MongoDB
mongosh

# Use database
use banking_system

# View accounts
db.accounts.find()

# View transactions
db.transactions.find()
```

### Development
```bash
# Clean build
mvn clean

# Compile only
mvn compile

# Package JAR
mvn package

# Run tests (if exists)
mvn test
```

---

## 📞 Hỗ trợ

### Nếu gặp vấn đề:

1. **MongoDB không kết nối được**
   - Xem [SETUP.md](SETUP.md) → Bước 1
   - Check: `sudo systemctl status mongodb`

2. **Build lỗi**
   - Xem [SETUP.md](SETUP.md) → Xử lý lỗi
   - Try: `mvn clean install -U`

3. **Runtime errors**
   - Xem [README.md](README.md) → Troubleshooting
   - Check logs trong console

4. **Không hiểu luồng nghiệp vụ**
   - Xem [ARCHITECTURE.md](ARCHITECTURE.md)
   - Study sơ đồ luồng

---

## 🎓 Học tập từ Project

### Các khái niệm được áp dụng:

1. **Design Patterns**
   - Singleton (MongoDBConnection)
   - Repository Pattern
   - Service Layer Pattern
   - MVC (GUI)

2. **Best Practices**
   - Separation of Concerns
   - Single Responsibility
   - Error Handling
   - Input Validation

3. **Technologies**
   - Java Core
   - MongoDB
   - Maven
   - Swing GUI
   - External Libraries

4. **Software Engineering**
   - Layered Architecture
   - Database Design
   - API Design
   - Documentation

---

## 📝 Workflow Development

```
1. Start MongoDB
   ↓
2. Build Project (mvn clean install)
   ↓
3. Run Application
   ↓
4. Test Features
   ↓
5. Check Database (mongosh)
   ↓
6. Debug if needed
   ↓
7. Export Reports
```

---

## 🎉 Kết luận

Project này bao gồm:
- ✅ Tất cả yêu cầu đề bài
- ✅ CRUD operations hoàn chỉnh
- ✅ Giao dịch với validation đầy đủ
- ✅ Multi-instance support
- ✅ Export PDF/DOCX
- ✅ QR Code generation
- ✅ Internet checking
- ✅ Notification system
- ✅ Documentation đầy đủ

---

## 📚 Đọc tiếp theo

**Bước 1:** [QUICKSTART.md](QUICKSTART.md) - Start ngay trong 5 phút  
**Bước 2:** [README.md](README.md) - Hiểu tổng quan hệ thống  
**Bước 3:** [ARCHITECTURE.md](ARCHITECTURE.md) - Hiểu kiến trúc  
**Bước 4:** Xem source code - Học cách implement  

---

**Happy Coding! 🚀**
