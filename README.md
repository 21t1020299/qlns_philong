# PhiLong - Quản Lý Nhân Sự

Ứng dụng Java Swing để quản lý nhân sự với H2 Database (embedded).

## 📁 Cấu trúc Project

```
qlns_philong/
├── src/
│   ├── bean/          # Model classes
│   ├── bo/           # Business Logic
│   ├── dao/          # Data Access Objects
│   ├── utils/        # Utilities & Database
│   └── views/        # GUI Components
├── lib/              # Dependencies (H2 Database)
├── out/              # Compiled classes
├── *.db              # H2 Database files
├── run.sh            # Script chạy nhanh
└── README.md         # This file
```

## 🚀 Cách chạy ứng dụng

### ⚡ Sử dụng Script (Khuyến nghị)
```bash
# Cấp quyền thực thi cho script
chmod +x run.sh

# Các lệnh cơ bản
./run.sh compile    # Compile project
./run.sh run        # Chạy ứng dụng
./run.sh build      # Build hoàn toàn
./run.sh reset      # Reset hoàn toàn (xóa DB + rebuild)
./run.sh stop       # Dừng ứng dụng
./run.sh status     # Xem trạng thái ứng dụng

# Ví dụ: Reset và chạy
./run.sh reset && ./run.sh run
```

### 🔧 Lệnh thủ công
#### 1. Compile toàn bộ project
```bash
javac -cp "lib/*:src" -d out src/views/Main.java
```

#### 2. Chạy ứng dụng chính
```bash
java -cp "lib/*:out" views.Main
```

## 🗄️ Quản lý H2 Database

### 👀 Xem dữ liệu database
```bash
# Sử dụng script
./run.sh view       # Xem dữ liệu cơ bản
./run.sh fullview   # Xem đầy đủ dữ liệu database

# Lệnh thủ công
java -cp "lib/*:out" utils.H2DatabaseViewer
java -cp "lib/*:out" utils.FullDatabaseViewer
```

### 🌐 H2 Web Console
```bash
# Khởi động H2 Console trong background (khuyến nghị)
./run.sh console-start

# Khởi động H2 Console trong foreground
./run.sh console

# Dừng H2 Console
./run.sh console-stop

# Lệnh thủ công
java -cp "lib/*:out" utils.H2WebConsole
```
Sau đó mở trình duyệt: `http://localhost:8082`

**Thông tin đăng nhập H2 Console:**
- **Username:** sa
- **Password:** (để trống)
- **Database:** ./qlns_philong

**💡 Lưu ý quan trọng:**
- Sử dụng `console-start` để chạy H2 Console trong background
- Console sẽ tiếp tục chạy ngay cả khi đóng terminal
- Sử dụng `console-stop` để dừng khi không cần thiết
- Log file: `h2_console.log` (khi chạy background)

### 🏗️ Tạo schema database
```bash
# Sử dụng script
./run.sh schema

# Lệnh thủ công
javac -cp "lib/*:src" -d out src/utils/DatabaseSchemaCreator.java
java -cp "lib/*:out" utils.DatabaseSchemaCreator
```

### 🧪 Test thêm nhân viên
```bash
# Sử dụng script
./run.sh test

# Lệnh thủ công
java -cp "lib/*:out" utils.TestAddEmployee
```

### 🌱 Tạo dữ liệu mẫu
```bash
javac -cp "lib/*:src" -d out src/utils/DataSeeder.java
java -cp "lib/*:out" utils.DataSeeder
```

## 🔧 Lệnh quản lý

### 📊 Xem trạng thái
```bash
./run.sh status
```

### ⏹️ Dừng ứng dụng
```bash
# Sử dụng script
./run.sh stop

# Lệnh thủ công
pkill -f "java.*views.Main"
```

### 🔄 Reset hoàn toàn
```bash
# Sử dụng script
./run.sh reset

# Lệnh thủ công
pkill -f "java.*views.Main" 2>/dev/null
rm -f *.db
rm -rf out
javac -cp "lib/*:src" -d out src/views/Main.java
```

### 🗑️ Xóa database cũ
```bash
rm -f *.db
rm -rf out
```

### 🔨 Rebuild hoàn toàn
```bash
rm -rf out
javac -cp "lib/*:src" -d out src/views/Main.java
java -cp "lib/*:out" views.Main
```

## 🛠️ Các lệnh tiện ích khác

### 📋 Xem tất cả lệnh có sẵn
```bash
./run.sh
```

### 🔍 Kiểm tra process đang chạy
```bash
ps aux | grep "java.*views.Main"
```

### 🌐 Kiểm tra port H2 Console
```bash
lsof -i :8082
```

### 📁 Xem file database
```bash
ls -la *.db
```

## 📊 Database Schema

Bảng `nhanvien` có 18 trường:
1. `manv` - Mã nhân viên (Primary Key)
2. `tennv` - Họ tên
3. `gtinh` - Giới tính
4. `email` - Email
5. `dchi` - Địa chỉ
6. `ngsinh` - Ngày sinh
7. `qtich` - Quốc tịch
8. `sdt` - Số điện thoại
9. `trinhdo` - Trình độ học vấn
10. `noidkhktt` - Nơi đăng ký HKTT
11. `dchithuongtru` - Địa chỉ thường trú
12. `dtoc` - Dân tộc
13. `skhoe` - Tình trạng sức khỏe
14. `macv` - Mã chức vụ
15. `hotencha` - Họ tên cha
16. `hotenme` - Họ tên mẹ
17. `anhchandung` - Ảnh chân dung (null)
18. `anhcmnd` - Ảnh CMND (null)

## 🎯 Tính năng chính

### ✅ Đã hoàn thành:
- [x] Thêm nhân viên mới
- [x] Hiển thị danh sách nhân viên
- [x] Validation dữ liệu đầu vào
- [x] Kết nối H2 Database
- [x] ComboBox cho các trường có sẵn lựa chọn
- [x] Auto-generate mã nhân viên
- [x] Reset form sau khi lưu
- [x] Refresh danh sách sau khi thêm/sửa

### 🔍 Validation Rules:
- **Họ tên:** Chỉ chữ cái, dấu tiếng Việt, khoảng trắng, gạch ngang
- **Ngày sinh:** Format YYYY-MM-DD, tuổi 18-54
- **Email:** Format email hợp lệ, không bắt đầu/kết thúc bằng . hoặc -
- **SĐT:** 10 số, bắt đầu bằng 0
- **Địa chỉ:** Không rỗng, tối đa 200 ký tự
- **Họ tên cha/mẹ:** Tương tự họ tên, tối đa 100 ký tự

## 🐛 Troubleshooting

### Lỗi "Column count does not match"
```bash
# Xóa database và rebuild
rm -f *.db
rm -rf out
javac -cp "lib/*:src" -d out src/views/Main.java
java -cp "lib/*:out" views.Main
```

### Lỗi "Table NHANVIEN not found"
```bash
# Database chưa được tạo, chạy lại ứng dụng
java -cp "lib/*:out" views.Main
```

### Lỗi "Address already in use" (H2 Console)
```bash
# Tìm và dừng process đang sử dụng port 8082
lsof -ti:8082 | xargs kill -9
# Hoặc sử dụng lệnh script
./run.sh console-stop
# Khởi động lại H2 Console
./run.sh console-start
```

### Lỗi "Database may be already in use"
```bash
# Dừng tất cả các process Java đang chạy
pkill -f "java.*H2WebConsole"
pkill -f "java.*views.Main"
# Đợi 2 giây
sleep 2
# Thử lại lệnh
./run.sh view
```

### H2 Console không hiển thị bảng mới
```bash
# Refresh trang trong trình duyệt
# Hoặc disconnect và connect lại database
# Hoặc chạy lệnh SQL để xem tất cả bảng:
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC';
```

### Ứng dụng không hiển thị gì
```bash
# Kiểm tra xem ứng dụng có đang chạy không
ps aux | grep "java.*views.Main"
# Nếu có, dừng và chạy lại
pkill -f "java.*views.Main"
java -cp "lib/*:out" views.Main
```

## 📝 Ghi chú

- Database file: `qlns_philong.mv.db`
- Ứng dụng tự động tạo database khi chạy lần đầu
- Dữ liệu mẫu được tạo tự động nếu database trống
- H2 Database là embedded, không cần cài đặt server riêng

## 🔗 Liên kết hữu ích

- [H2 Database Documentation](https://www.h2database.com/html/main.html)
- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)