# 🧪 Test API Guide

## 🔍 Kiểm tra trạng thái hiện tại

### 1. Health Check
```bash
curl https://qlns-philong.onrender.com/health
```

### 2. API Info
```bash
curl https://qlns-philong.onrender.com/
```

### 3. Employee List (hiện tại)
```bash
curl https://qlns-philong.onrender.com/api/employees/
```

## 🚨 Vấn đề hiện tại

**Lỗi 500 khi thêm employee** - Có thể do:
1. Database chưa được tạo
2. SQLite file permissions
3. Database schema chưa được apply

## 🔧 Cách khắc phục

### Cách 1: Kiểm tra logs Render
1. Vào https://render.com
2. Chọn service `qlns_philong`
3. Vào tab "Logs"
4. Xem lỗi chi tiết

### Cách 2: Test với data đơn giản
```bash
curl -X POST "https://qlns-philong.onrender.com/api/employees/" \
  -H "Content-Type: application/json" \
  -d '{
    "tennv": "Test",
    "gtinh": "Nam",
    "email": "test@test.com",
    "sdt": "0123456789",
    "ngsinh": "1990-01-01",
    "dchi": "Test",
    "dchithuongtru": "Test",
    "noidkhktt": "Test",
    "dtoc": "Kinh",
    "trinhdo": "Đại học",
    "qtich": "Việt Nam",
    "skhoe": "Tốt",
    "macv": "NV001",
    "hotencha": "Test",
    "hotenme": "Test"
  }'
```

### Cách 3: Sử dụng API Documentation
1. Mở: https://qlns-philong.onrender.com/docs
2. Test endpoint `/api/employees/` (POST)
3. Xem response chi tiết

## 📊 Cách xem data

### 1. List tất cả employees
```bash
curl https://qlns-philong.onrender.com/api/employees/
```

### 2. Tìm kiếm employee
```bash
curl "https://qlns-philong.onrender.com/api/employees/?search=Nguyen"
```

### 3. Lọc theo giới tính
```bash
curl "https://qlns-philong.onrender.com/api/employees/?gender=Nam"
```

### 4. Phân trang
```bash
curl "https://qlns-philong.onrender.com/api/employees/?page=1&size=5"
```

## 🎯 Kết quả mong đợi

Sau khi fix lỗi, bạn sẽ thấy:
```json
{
  "employees": [
    {
      "id": "uuid-here",
      "manv": "NV001",
      "tennv": "Nguyễn Văn A",
      "email": "nguyenvana@example.com",
      // ... other fields
    }
  ],
  "total": 1,
  "page": 1,
  "size": 10
}
```

## 🔍 Debug Steps

1. **Kiểm tra health**: API có hoạt động không?
2. **Kiểm tra logs**: Lỗi gì trong Render logs?
3. **Test đơn giản**: Thử với data tối thiểu
4. **Kiểm tra database**: Schema có được tạo không? 