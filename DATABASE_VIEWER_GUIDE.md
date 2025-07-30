# 🗄️ Database Viewer Guide - Giao diện xem Database như Supabase

## 🎯 **Giao diện Database Viewer đã sẵn sàng!**

### 📂 **File giao diện:**
- **`database-viewer.html`** - Giao diện web để xem database

### 🌐 **Cách sử dụng:**

#### **1. Mở giao diện:**
```bash
# Mở file HTML trong browser
open database-viewer.html
```

#### **2. Tính năng chính:**

##### **📊 Dashboard Statistics:**
- **Tổng nhân viên**: Số lượng employees
- **Nam/Nữ**: Phân bố giới tính
- **Cập nhật cuối**: Thời gian cập nhật gần nhất

##### **📋 Bảng dữ liệu:**
- **Mã NV**: Mã nhân viên (NV001, NV002...)
- **Họ tên**: Tên đầy đủ
- **Email**: Địa chỉ email
- **SĐT**: Số điện thoại
- **Giới tính**: Nam/Nữ
- **Ngày sinh**: Ngày sinh
- **Chức vụ**: Mã chức vụ
- **Trình độ**: Trình độ học vấn
- **Thao tác**: Nút xem chi tiết

##### **🔍 Tìm kiếm:**
- Tìm kiếm theo tên, email
- Tự động refresh khi gõ

##### **📄 Phân trang:**
- Hiển thị 10 records/trang
- Nút điều hướng trang

##### **📥 Export:**
- Export dữ liệu ra file CSV
- Tải về máy tính

### 🎨 **Giao diện giống Supabase:**

#### **✅ Tính năng tương tự:**
- 📊 **Dashboard stats** - Thống kê tổng quan
- 📋 **Data table** - Bảng dữ liệu với sorting
- 🔍 **Search & filter** - Tìm kiếm và lọc
- 📄 **Pagination** - Phân trang
- 📥 **Export** - Xuất dữ liệu
- 🎨 **Modern UI** - Giao diện đẹp, responsive

#### **🎯 Khác biệt với Supabase:**
- **Database**: SQLite thay vì PostgreSQL
- **Hosting**: Render thay vì Supabase
- **Authentication**: Chưa có (có thể thêm sau)
- **Real-time**: Chưa có (có thể thêm sau)

### 🚀 **Cách deploy giao diện:**

#### **Option 1: Local (Đơn giản)**
```bash
# Mở file HTML trực tiếp
open database-viewer.html
```

#### **Option 2: Deploy lên web (Nâng cao)**
1. Upload `database-viewer.html` lên GitHub Pages
2. Hoặc deploy lên Netlify/Vercel
3. Hoặc thêm vào Render static site

### 📊 **API Endpoints sử dụng:**

#### **1. Lấy danh sách employees:**
```bash
curl https://qlns-philong.onrender.com/api/employees/
```

#### **2. Tìm kiếm employees:**
```bash
curl "https://qlns-philong.onrender.com/api/employees/?search=Nguyen"
```

#### **3. Phân trang:**
```bash
curl "https://qlns-philong.onrender.com/api/employees/?page=1&size=10"
```

#### **4. Thống kê (sẽ có sau khi deploy):**
```bash
curl https://qlns-philong.onrender.com/api/employees/stats
```

### 🎉 **Kết quả:**

Bây giờ bạn có:
- ✅ **Giao diện database** đẹp như Supabase
- ✅ **Real-time data** từ API
- ✅ **Search & filter** functionality
- ✅ **Export data** ra CSV
- ✅ **Responsive design** cho mobile

### 🔄 **Cập nhật tự động:**

Giao diện sẽ tự động:
- Load data từ API khi mở trang
- Refresh khi click nút "Refresh"
- Tìm kiếm real-time khi gõ
- Cập nhật thống kê tự động

**Bây giờ bạn có thể xem database với giao diện đẹp như Supabase! 🎉** 