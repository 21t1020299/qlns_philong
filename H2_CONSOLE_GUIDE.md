# H2 Database Console - Hướng dẫn sử dụng

## 🌐 Truy cập H2 Console

### 1. Khởi động H2 Web Console
```bash
./run.sh console
```

### 2. Mở trình duyệt
- **URL:** `http://localhost:8082`
- **Username:** `sa`
- **Password:** (để trống)

## 🔧 Cấu hình kết nối

### Thông tin kết nối chính xác:
- **JDBC Driver:** `org.h2.Driver`
- **URL JDBC:** `jdbc:h2:./qlns_philong`
- **Username:** `sa`
- **Password:** (để trống)

### Các URL JDBC khác có thể dùng:
```
jdbc:h2:./qlns_philong
jdbc:h2:/Users/jayceho/qlns_philong/qlns_philong
jdbc:h2:file:./qlns_philong
```

## 📊 Cách sử dụng H2 Console

### 1. Kết nối database
1. Thay đổi **URL JDBC** thành: `jdbc:h2:./qlns_philong`
2. Nhấn **"Test de connexion"** để kiểm tra
3. Nhấn **"Connecter"** để kết nối

### 2. Xem dữ liệu
1. Trong bảng bên trái, chọn **"NHANVIEN"**
2. Nhấn **"Run"** để xem tất cả dữ liệu
3. Hoặc viết SQL: `SELECT * FROM NHANVIEN;`

### 3. Thực thi SQL
```sql
-- Xem tất cả nhân viên
SELECT * FROM NHANVIEN;

-- Đếm số nhân viên
SELECT COUNT(*) FROM NHANVIEN;

-- Tìm nhân viên theo mã
SELECT * FROM NHANVIEN WHERE MANV = 'NV001';

-- Xem schema bảng
SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NHANVIEN';
```

### 4. Thêm dữ liệu mới
```sql
INSERT INTO NHANVIEN VALUES (
    'NV100', 'Nguyễn Văn Demo', 'Nam', 'demo@philong.com.vn',
    '123 Đường Demo, TP.HCM', '1995-01-01', 'Việt Nam', '0123456789',
    'Đại học', 'TP.HCM', '123 Đường Demo, TP.HCM', 'Kinh', 'Tốt',
    'CV001', 'Nguyễn Văn Bố Demo', 'Trần Thị Mẹ Demo', null, null
);
```

### 5. Cập nhật dữ liệu
```sql
UPDATE NHANVIEN SET TENNV = 'Nguyễn Văn Updated' WHERE MANV = 'NV100';
```

### 6. Xóa dữ liệu
```sql
DELETE FROM NHANVIEN WHERE MANV = 'NV100';
```

## 🎯 Tính năng hữu ích

### Xem schema database
```sql
SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'PUBLIC' 
ORDER BY TABLE_NAME, ORDINAL_POSITION;
```

### Xem tất cả bảng
```sql
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC';
```

### Backup database
- Nhấn **"Tools"** → **"Backup"**
- Chọn thư mục lưu file backup

### Restore database
- Nhấn **"Tools"** → **"Restore"**
- Chọn file backup để khôi phục

## 🐛 Troubleshooting

### Lỗi "Database not found"
- Kiểm tra URL JDBC có đúng không
- Đảm bảo file `qlns_philong.mv.db` tồn tại
- Thử URL: `jdbc:h2:./qlns_philong`

### Lỗi "Connection refused"
- Kiểm tra H2 Console có đang chạy không: `./run.sh status`
- Khởi động lại: `./run.sh console`

### Lỗi "Port already in use"
```bash
# Tìm và dừng process đang sử dụng port 8082
lsof -ti:8082 | xargs kill -9
# Khởi động lại H2 Console
./run.sh console
```

## 📝 Ghi chú

- **Database file:** `qlns_philong.mv.db`
- **Schema:** 18 trường (đồng bộ với Supabase)
- **Encoding:** UTF-8
- **Mode:** Embedded (file-based)

## 🔗 Lệnh nhanh

```bash
./run.sh view      # Xem dữ liệu nhanh
./run.sh test      # Test thêm nhân viên
./run.sh console   # Khởi động H2 Console
./run.sh status    # Kiểm tra trạng thái
```