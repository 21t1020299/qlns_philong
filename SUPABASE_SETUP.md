# 🗄️ Supabase Setup Guide

## 🤔 Hiện tại đang dùng SQLite

**Render deployment** hiện tại đang sử dụng **SQLite** (file database local). Nếu bạn muốn dùng **Supabase**, hãy làm theo các bước sau:

## 🚀 Cách 1: Tạo Supabase Project mới

### Bước 1: Truy cập Supabase
1. Mở: https://supabase.com
2. Đăng nhập/Sign up
3. Click "New Project"

### Bước 2: Tạo Project
1. **Organization**: Chọn org của bạn
2. **Name**: `philong-employee-db`
3. **Database Password**: Tạo password mạnh
4. **Region**: Chọn gần nhất (Singapore)
5. Click "Create new project"

### Bước 3: Lấy thông tin kết nối
Sau khi tạo xong, vào **Settings** → **Database**:
- **Host**: `db.[project-ref].supabase.co`
- **Database name**: `postgres`
- **Port**: `5432`
- **User**: `postgres`
- **Password**: (password bạn đã tạo)

## 🔑 Lấy API Keys

Vào **Settings** → **API**:
- **Project URL**: `https://[project-ref].supabase.co`
- **Anon public**: `[your-anon-key]`
- **Service role**: `[your-service-role-key]`

## ⚙️ Cấu hình Environment Variables

### Tạo file `backend/.env`:

```env
# Supabase Configuration
DATABASE_URL=postgresql://postgres:[YOUR-PASSWORD]@db.[YOUR-PROJECT-REF].supabase.co:5432/postgres
SUPABASE_URL=https://[YOUR-PROJECT-REF].supabase.co
SUPABASE_ANON_KEY=[YOUR-ANON-KEY]
SUPABASE_SERVICE_ROLE_KEY=[YOUR-SERVICE-ROLE-KEY]

# Security
SECRET_KEY=a8f0e55e8601a369c47713448e0493889761a7e6c859e752aba8e21e0af3be2e

# Environment
DEBUG=False
ENVIRONMENT=production
```

## 🔄 Cập nhật Render

### Cách 1: Render Dashboard
1. Vào https://render.com
2. Chọn service `qlns_philong`
3. Vào **Environment**
4. Thêm các biến môi trường Supabase

### Cách 2: Cập nhật render.yaml
Thay thế trong `render.yaml`:

```yaml
envVars:
  - key: DATABASE_URL
    value: postgresql://postgres:[PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres
  - key: SUPABASE_URL
    value: https://[PROJECT-REF].supabase.co
  - key: SUPABASE_ANON_KEY
    value: [YOUR-ANON-KEY]
  - key: SUPABASE_SERVICE_ROLE_KEY
    value: [YOUR-SERVICE-ROLE-KEY]
```

## 🗄️ Tạo Database Schema

Supabase sẽ tự động tạo bảng khi API chạy lần đầu, hoặc bạn có thể chạy SQL:

```sql
CREATE TABLE employees (
    id VARCHAR(36) PRIMARY KEY,
    manv VARCHAR(10) UNIQUE NOT NULL,
    tennv VARCHAR(100) NOT NULL,
    gtinh VARCHAR(10) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    sdt VARCHAR(15) NOT NULL,
    ngsinh DATE NOT NULL,
    dchi TEXT NOT NULL,
    dchithuongtru TEXT NOT NULL,
    noidkhktt VARCHAR(100) NOT NULL,
    dtoc VARCHAR(50) NOT NULL,
    trinhdo VARCHAR(50) NOT NULL,
    qtich VARCHAR(50) NOT NULL,
    skhoe VARCHAR(50) NOT NULL,
    macv VARCHAR(10) NOT NULL,
    hotencha VARCHAR(100) NOT NULL,
    hotenme VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

## ✅ Lợi ích của Supabase

- 🗄️ **Database cloud** - Không mất dữ liệu
- 🔐 **Authentication** - Có thể thêm login
- 📊 **Real-time** - Có thể thêm real-time updates
- 🛡️ **Security** - Row Level Security
- 📈 **Scalable** - Tự động scale

## 🤷‍♂️ Hoặc giữ SQLite

Nếu bạn muốn giữ SQLite (đơn giản hơn):
- ✅ Không cần setup gì thêm
- ✅ Database file được lưu trên Render
- ✅ Hoạt động bình thường

**Bạn muốn dùng Supabase hay giữ SQLite? 🤔** 