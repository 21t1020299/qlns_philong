# PhiLong Employee Management System

Hệ thống quản lý nhân sự hiện đại với Python FastAPI Backend và React Frontend.

## 🏗️ Kiến trúc

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React App     │    │  FastAPI API    │    │  PostgreSQL     │
│   (Frontend)    │◄──►│   (Backend)     │◄──►│   (Database)    │
│   Port: 3000    │    │   Port: 8000    │    │   Port: 5432    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Cách chạy nhanh

### Sử dụng Docker (Khuyến nghị)

```bash
# Clone repository
git clone <repository-url>
cd philong-app

# Chạy toàn bộ hệ thống
docker-compose up -d

# Truy cập ứng dụng
# Frontend: http://localhost:3000
# Backend API: http://localhost:8000
# API Docs: http://localhost:8000/docs
```

### Chạy thủ công

#### 1. Backend (Python)

```bash
cd backend

# Tạo virtual environment
python -m venv venv
source venv/bin/activate  # Linux/Mac
# hoặc
venv\Scripts\activate     # Windows

# Cài đặt dependencies
pip install -r requirements.txt

# Chạy server
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

#### 2. Frontend (React)

```bash
cd frontend

# Cài đặt dependencies
npm install

# Chạy development server
npm start
```

#### 3. Database (PostgreSQL)

```bash
# Cài đặt PostgreSQL
# Ubuntu/Debian
sudo apt-get install postgresql postgresql-contrib

# macOS
brew install postgresql

# Windows
# Tải từ https://www.postgresql.org/download/windows/

# Tạo database
createdb philong_db
```

## 📁 Cấu trúc Project

```
philong-app/
├── backend/                 # Python FastAPI Backend
│   ├── app/
│   │   ├── main.py         # FastAPI application
│   │   ├── models/         # SQLAlchemy models
│   │   ├── schemas/        # Pydantic schemas
│   │   ├── routes/         # API routes
│   │   ├── database/       # Database connection
│   │   └── utils/          # Utility functions
│   ├── requirements.txt    # Python dependencies
│   └── Dockerfile         # Backend Docker image
├── frontend/               # React Frontend
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── services/       # API services
│   │   ├── types/          # TypeScript types
│   │   ├── App.tsx         # Main app component
│   │   └── index.tsx       # Entry point
│   ├── package.json        # Node.js dependencies
│   └── Dockerfile         # Frontend Docker image
├── docker-compose.yml      # Docker orchestration
└── README.md              # Project documentation
```

## 🔧 API Endpoints

### Employees

- `GET /api/employees` - Lấy danh sách nhân viên
- `GET /api/employees/{id}` - Lấy thông tin nhân viên
- `POST /api/employees` - Tạo nhân viên mới
- `PUT /api/employees/{id}` - Cập nhật nhân viên
- `DELETE /api/employees/{id}` - Xóa nhân viên
- `GET /api/employees/stats/summary` - Thống kê nhân viên

### Query Parameters

- `page` - Trang hiện tại (mặc định: 1)
- `size` - Số lượng item mỗi trang (mặc định: 10)
- `search` - Tìm kiếm theo tên, email, mã nhân viên
- `gender` - Lọc theo giới tính (Nam/Nữ)

## 📊 Database Schema

### Bảng Employees

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Primary key |
| manv | VARCHAR(10) | Mã nhân viên (NV001, NV002, ...) |
| tennv | VARCHAR(100) | Họ tên |
| gtinh | VARCHAR(10) | Giới tính |
| email | VARCHAR(100) | Email |
| sdt | VARCHAR(15) | Số điện thoại |
| ngsinh | DATE | Ngày sinh |
| dchi | TEXT | Địa chỉ hiện tại |
| dchithuongtru | TEXT | Địa chỉ thường trú |
| noidkhktt | VARCHAR(100) | Nơi đăng ký HKTT |
| dtoc | VARCHAR(50) | Dân tộc |
| trinhdo | VARCHAR(50) | Trình độ |
| qtich | VARCHAR(50) | Quốc tịch |
| skhoe | VARCHAR(50) | Sức khỏe |
| macv | VARCHAR(10) | Mã chức vụ |
| hotencha | VARCHAR(100) | Họ tên cha |
| hotenme | VARCHAR(100) | Họ tên mẹ |
| created_at | TIMESTAMP | Thời gian tạo |
| updated_at | TIMESTAMP | Thời gian cập nhật |

## ✅ Validation Rules

### Họ tên
- Không được để trống
- Chỉ chứa chữ cái, dấu tiếng Việt và khoảng trắng

### Email
- Định dạng email hợp lệ
- Domain tối đa 4 cấp
- Unique trong hệ thống

### Số điện thoại
- 10 số, bắt đầu bằng 0
- Format: 0xxxxxxxxx

### Ngày sinh
- Tuổi từ 18-54

### Địa chỉ
- Tối đa 200 ký tự

### Họ tên cha/mẹ
- Tối đa 100 ký tự
- Chỉ chứa chữ cái

## 🛠️ Development

### Backend Development

```bash
cd backend

# Run tests
pytest

# Format code
black .

# Lint code
flake8 .

# Type checking
mypy .
```

### Frontend Development

```bash
cd frontend

# Run tests
npm test

# Build for production
npm run build

# Lint code
npm run lint
```

## 🐳 Docker Commands

```bash
# Build images
docker-compose build

# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild and restart
docker-compose up -d --build
```

## 🔧 Environment Variables

### Backend (.env)

```env
DATABASE_URL=postgresql://postgres:password@localhost:5432/philong_db
SECRET_KEY=your-secret-key
DEBUG=True
```

### Frontend (.env)

```env
REACT_APP_API_URL=http://localhost:8000
```

## 📝 License

MIT License

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request