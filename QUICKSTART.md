# 🚀 QUICK START GUIDE

## Cài đặt nhanh (5 phút)

### Bước 1: Cài MongoDB
```bash
# Ubuntu/Debian
sudo apt-get update && sudo apt-get install -y mongodb
sudo systemctl start mongodb
```

### Bước 2: Build & Run
```bash
cd /home/barodev/java_course/exam/banking-system

# Build
mvn clean install

# Chạy 1 instance
./run.sh

# HOẶC chạy 2 instances
./run-dual.sh
```

## Test nhanh

### Tạo tài khoản:
1. Mở ứng dụng
2. Điền thông tin:
   - Số TK: `1234567890`
   - Tên: `Nguyen Van A`
   - Số dư: `5000000`
   - Ngân hàng: `Vietcombank`
3. Click "Tạo TK"

### Giao dịch:
1. Click chọn tài khoản vừa tạo
2. Click "Nạp tiền" → nhập `1000000`
3. Click "Rút tiền" → nhập `500000`

### Tạo tài khoản thứ 2 và chuyển khoản:
1. Tạo TK2: `0987654321` - `Tran Thi B` - `3000000`
2. Chọn TK1, click "Chuyển khoản"
3. Nhập TK đích: `0987654321`
4. Nhập số tiền: `800000`

### Xem sao kê:
1. Chọn tài khoản
2. Click "Sao kê ngày"
3. Click "Xuất PDF" hoặc "Xuất DOCX"

## Files quan trọng

| File | Mô tả |
|------|-------|
| `README.md` | Hướng dẫn đầy đủ |
| `SETUP.md` | Hướng dẫn cài đặt chi tiết |
| `PROJECT_SUMMARY.md` | Tóm tắt dự án |
| `run.sh` | Script chạy 1 instance |
| `run-dual.sh` | Script chạy 2 instances |

## Xử lý lỗi nhanh

```bash
# MongoDB không chạy
sudo systemctl start mongodb

# Build lỗi
mvn clean install -U

# Permission denied
chmod +x run.sh run-dual.sh
```

## Các lệnh hữu ích

```bash
# Kiểm tra MongoDB
mongosh
> show dbs
> use banking_system
> db.accounts.find()
> exit

# Xem logs build
mvn clean install -X

# Clean project
mvn clean
rm -rf target/

# Rebuild from scratch
mvn clean install -U
```

## Demo scenario đầy đủ

### Scenario 1: Giao dịch cơ bản
1. Tạo TK → Nạp tiền → Rút tiền → Xem sao kê → Xuất PDF

### Scenario 2: Chuyển khoản
1. Tạo 2 TK → Chuyển tiền TK1→TK2 → Kiểm tra số dư

### Scenario 3: Multi-instance
1. Mở 2 cửa sổ ứng dụng
2. Tạo TK khác nhau ở mỗi cửa sổ
3. Chuyển tiền qua lại
4. Nhấn "Làm mới" để đồng bộ

### Scenario 4: QR Code
1. Tạo TK → Nhấn "Tạo QR" → File QR được tạo

## Tips

💡 **Tip 1:** Nhấn "Làm mới" để reload dữ liệu từ database  
💡 **Tip 2:** File xuất (PDF/DOCX/QR) lưu ở thư mục project  
💡 **Tip 3:** Dùng `mongosh` để xem trực tiếp database  
💡 **Tip 4:** Xóa tài khoản chỉ khi số dư = 0  

## Liên hệ & Support

- Đọc chi tiết: `README.md`
- Setup đầy đủ: `SETUP.md`
- Tóm tắt: `PROJECT_SUMMARY.md`

---

**Chúc bạn sử dụng thành công! 🎉**
