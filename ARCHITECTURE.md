# Sơ đồ kiến trúc hệ thống Banking Management System

## 1. Sơ đồ tổng quan (Overview Architecture)

```
┌─────────────────────────────────────────────────────────────────┐
│                     BANKING MANAGEMENT SYSTEM                    │
│                          (Java Swing GUI)                        │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                ┌───────────────┴───────────────┐
                │                               │
┌───────────────▼──────────────┐   ┌───────────▼────────────────┐
│    Account Management        │   │   Transaction Management   │
│    - Create Account          │   │   - Deposit                │
│    - Read Account            │   │   - Withdrawal             │
│    - Update Account          │   │   - Transfer               │
│    - Delete Account          │   │   - Statement Reports      │
└───────────────┬──────────────┘   └───────────┬────────────────┘
                │                               │
                └───────────────┬───────────────┘
                                │
                    ┌───────────▼───────────┐
                    │   Service Layer       │
                    │  - Business Logic     │
                    │  - Validation         │
                    │  - Notifications      │
                    └───────────┬───────────┘
                                │
                    ┌───────────▼───────────┐
                    │  Repository Layer     │
                    │  - Data Access        │
                    │  - CRUD Operations    │
                    └───────────┬───────────┘
                                │
                    ┌───────────▼───────────┐
                    │   MongoDB Database    │
                    │  - accounts           │
                    │  - transactions       │
                    └───────────────────────┘
```

## 2. Sơ đồ luồng giao dịch (Transaction Flow)

```
┌──────────┐      ┌──────────────┐      ┌─────────────┐
│  User    │─────▶│  BankingGUI  │─────▶│  Service    │
└──────────┘      └──────────────┘      └──────┬──────┘
                                                │
                                    ┌───────────▼──────────┐
                                    │  Validations:        │
                                    │  - Check balance     │
                                    │  - Check account     │
                                    │  - Check internet    │
                                    └───────────┬──────────┘
                                                │
                                    ┌───────────▼──────────┐
                                    │  Repository          │
                                    │  - Update balance    │
                                    │  - Create transaction│
                                    └───────────┬──────────┘
                                                │
                                    ┌───────────▼──────────┐
                                    │  MongoDB             │
                                    │  - Save data         │
                                    └───────────┬──────────┘
                                                │
                                    ┌───────────▼──────────┐
                                    │  Notifications       │
                                    │  - Console           │
                                    │  - Email (optional)  │
                                    │  - SMS (optional)    │
                                    └──────────────────────┘
```

## 3. Sơ đồ Class chính (Main Classes)

```
┌─────────────────────┐
│     Account         │
├─────────────────────┤
│ - accountNumber     │
│ - accountName       │
│ - balance           │
│ - bankName          │
│ - createdAt         │
│ - updatedAt         │
└─────────────────────┘
         △
         │ uses
         │
┌─────────────────────┐
│   Transaction       │
├─────────────────────┤
│ - id                │
│ - fromAccountNumber │
│ - toAccountNumber   │
│ - amount            │
│ - type              │
│ - description       │
│ - transactionDate   │
│ - status            │
└─────────────────────┘
         △
         │ uses
         │
┌─────────────────────┐
│  TransactionType    │
├─────────────────────┤
│ + DEPOSIT           │
│ + WITHDRAWAL        │
│ + TRANSFER_IN       │
│ + TRANSFER_OUT      │
└─────────────────────┘
```

## 4. Sơ đồ các Utilities

```
                    ┌─────────────────────┐
                    │   Utility Classes   │
                    └──────────┬──────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
┌───────▼────────┐  ┌─────────▼────────┐  ┌─────────▼────────┐
│ QRCodeGenerator│  │   PDFExporter    │  │  DOCXExporter    │
│ - Generate QR  │  │ - Export PDF     │  │ - Export DOCX    │
└────────────────┘  └──────────────────┘  └──────────────────┘
        │                      │                      │
┌───────▼────────┐  ┌─────────▼────────┐  ┌─────────▼────────┐
│InternetChecker │  │NotificationServ. │  │MongoDBConnection │
│ - Check WiFi   │  │ - Send SMS       │  │ - Connect DB     │
│ - Check 4G     │  │ - Send Email     │  │ - Get Database   │
└────────────────┘  └──────────────────┘  └──────────────────┘
```

## 5. Sơ đồ Multi-Instance (Chạy 2 chương trình)

```
┌─────────────────────────────┐    ┌─────────────────────────────┐
│    Banking GUI Instance 1   │    │    Banking GUI Instance 2   │
│                             │    │                             │
│  ┌─────────────────────┐   │    │   ┌─────────────────────┐  │
│  │  Account A          │   │    │   │  Account B          │  │
│  │  Balance: 5,000,000 │   │    │   │  Balance: 3,000,000 │  │
│  └─────────────────────┘   │    │   └─────────────────────┘  │
│            │                │    │             │               │
│            │ Transfer       │    │             │ Receive       │
│            │ 800,000 VND    │    │             │ 800,000 VND   │
│            │                │    │             │               │
└────────────┼────────────────┘    └─────────────┼───────────────┘
             │                                   │
             │         ┌─────────────┐          │
             └────────▶│   MongoDB   │◀─────────┘
                       │   Database  │
                       │             │
                       │  Shared     │
                       │  Storage    │
                       └─────────────┘
```

## 6. Sơ đồ luồng Deposit (Nạp tiền)

```
User Action: Click "Nạp tiền"
     │
     ▼
Input amount + description
     │
     ▼
Validate amount > 0
     │
     ├── NO ──▶ Show error
     │
     ▼ YES
Get account from DB
     │
     ▼
Calculate new balance
     │
     ▼
Update balance in DB
     │
     ▼
Create transaction record
     │
     ▼
Send notification
     │
     ▼
Show success message
     │
     ▼
Reload account list
```

## 7. Sơ đồ luồng Transfer (Chuyển khoản)

```
User Action: Click "Chuyển khoản"
     │
     ▼
Input: toAccount + amount + description
     │
     ▼
Check Internet Connection
     │
     ├── NO ──▶ Show "No Internet" error
     │
     ▼ YES
Validate amount > 0
     │
     ├── NO ──▶ Show error
     │
     ▼ YES
Get fromAccount from DB
     │
     ├── NOT FOUND ──▶ Show error
     │
     ▼ FOUND
Get toAccount from DB
     │
     ├── NOT FOUND ──▶ Show error
     │
     ▼ FOUND
Check balance >= amount
     │
     ├── NO ──▶ Show "Insufficient balance" error
     │
     ▼ YES
Update fromAccount (balance - amount)
     │
     ▼
Update toAccount (balance + amount)
     │
     ▼
Create transaction record
     │
     ▼
Send notifications to both accounts
     │
     ▼
Show success message
     │
     ▼
Reload account list
```

## 8. Sơ đồ luồng Statement Export (Xuất sao kê)

```
User Action: Click "Xuất PDF/DOCX"
     │
     ▼
Select account
     │
     ├── NO ACCOUNT ──▶ Show error
     │
     ▼ HAS ACCOUNT
Get account details
     │
     ▼
Get transaction history
     │
     ▼
Generate file name with timestamp
     │
     ▼
Choose format: PDF or DOCX
     │
     ├──▶ PDF ──▶ PDFExporter.export()
     │
     ├──▶ DOCX ──▶ DOCXExporter.export()
     │
     ▼
Format transaction data
     │
     ▼
Create document with:
- Account info
- Balance
- Transaction table
- Total count
     │
     ▼
Save file to disk
     │
     ▼
Show success message with file name
```

## 9. Sơ đồ Database Schema

```
MongoDB Database: banking_system
│
├── Collection: accounts
│   └── Document: {
│         _id: ObjectId,
│         accountNumber: String (unique, indexed),
│         accountName: String,
│         balance: Double,
│         bankName: String,
│         createdAt: Date,
│         updatedAt: Date
│       }
│
└── Collection: transactions
    └── Document: {
          _id: ObjectId,
          fromAccountNumber: String (indexed),
          toAccountNumber: String (indexed),
          amount: Double,
          type: String,
          description: String,
          transactionDate: Date (indexed),
          status: String,
          failureReason: String
        }
```

## 10. Sơ đồ Use Cases

```
┌──────────────────────────────────────────────────────────┐
│                    Banking System                        │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  Actor: Bank Customer                                    │
│                                                          │
│  Use Cases:                                              │
│  ┌────────────────────────────────────────────────┐    │
│  │ UC1: Quản lý tài khoản                         │    │
│  │   - Tạo tài khoản mới                          │    │
│  │   - Xem thông tin tài khoản                    │    │
│  │   - Cập nhật thông tin                         │    │
│  │   - Xóa tài khoản                              │    │
│  └────────────────────────────────────────────────┘    │
│                                                          │
│  ┌────────────────────────────────────────────────┐    │
│  │ UC2: Thực hiện giao dịch                       │    │
│  │   - Nạp tiền vào tài khoản                     │    │
│  │   - Rút tiền từ tài khoản                      │    │
│  │   - Chuyển khoản giữa các tài khoản            │    │
│  └────────────────────────────────────────────────┘    │
│                                                          │
│  ┌────────────────────────────────────────────────┐    │
│  │ UC3: Xem và xuất sao kê                        │    │
│  │   - Xem lịch sử giao dịch                      │    │
│  │   - Lọc theo ngày/tuần/tháng/năm               │    │
│  │   - Xuất sao kê PDF                            │    │
│  │   - Xuất sao kê DOCX                           │    │
│  └────────────────────────────────────────────────┘    │
│                                                          │
│  ┌────────────────────────────────────────────────┐    │
│  │ UC4: Tạo và quản lý QR Code                    │    │
│  │   - Tạo mã QR cho tài khoản                    │    │
│  │   - Lưu mã QR dưới dạng PNG                    │    │
│  └────────────────────────────────────────────────┘    │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

---

**Các sơ đồ này mô tả đầy đủ kiến trúc và luồng hoạt động của hệ thống Banking Management System**
