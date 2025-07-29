#!/bin/bash

# PhiLong - Quản Lý Nhân Sự - Script chạy nhanh

echo "🚀 PhiLong - Quản Lý Nhân Sự"
echo "================================"

case "$1" in
    "compile")
        echo "📦 Đang compile project..."
        javac -cp "lib/*:src" -d out src/views/Main.java
        javac -cp "lib/*:src" -d out src/utils/*.java
        echo "✅ Compile hoàn thành!"
        ;;
    "run")
        echo "🎯 Đang chạy ứng dụng..."
        java -cp "lib/*:out" views.Main
        ;;
    "build")
        echo "🔨 Build hoàn toàn..."
        rm -rf out
        javac -cp "lib/*:src" -d out src/views/Main.java
        javac -cp "lib/*:src" -d out src/utils/*.java
        echo "✅ Build hoàn thành!"
        ;;
    "reset")
        echo "🔄 Reset hoàn toàn..."
        pkill -f "java.*views.Main" 2>/dev/null
        pkill -f "java.*H2WebConsole" 2>/dev/null
        rm -f *.db
        rm -rf out
        javac -cp "lib/*:src" -d out src/views/Main.java
        javac -cp "lib/*:src" -d out src/utils/*.java
        echo "✅ Reset hoàn thành!"
        ;;
    "stop")
        echo "⏹️ Dừng ứng dụng..."
        pkill -f "java.*views.Main"
        echo "✅ Đã dừng ứng dụng!"
        ;;
    "view")
        echo "👀 Xem dữ liệu database..."
        # Đảm bảo utility classes được compile
        javac -cp "lib/*:src" -d out src/utils/H2DatabaseViewer.java 2>/dev/null
        java -cp "lib/*:out" utils.H2DatabaseViewer
        ;;
    "fullview")
        echo "👀 Xem đầy đủ dữ liệu database..."
        javac -cp "lib/*:src" -d out src/utils/FullDatabaseViewer.java
        java -cp "lib/*:out" utils.FullDatabaseViewer
        ;;
    "test")
        echo "🧪 Test thêm nhân viên..."
        # Đảm bảo utility classes được compile
        javac -cp "lib/*:src" -d out src/utils/TestAddEmployee.java 2>/dev/null
        java -cp "lib/*:out" utils.TestAddEmployee
        ;;
    "console")
        echo "🌐 Khởi động H2 Web Console..."
        echo "📡 URL: http://localhost:8082"
        echo "🔑 Username: sa, Password: (để trống)"
        # Đảm bảo utility classes được compile
        javac -cp "lib/*:src" -d out src/utils/H2WebConsole.java 2>/dev/null
        java -cp "lib/*:out" utils.H2WebConsole
        ;;
    "console-start")
        echo "🌐 Khởi động H2 Web Console trong background..."
        echo "📡 URL: http://localhost:8082"
        echo "🔑 Username: sa, Password: (để trống)"
        echo "💡 Sử dụng './run.sh console-stop' để dừng"
        # Đảm bảo utility classes được compile
        javac -cp "lib/*:src" -d out src/utils/H2WebConsole.java 2>/dev/null
        nohup java -cp "lib/*:out" utils.H2WebConsole > h2_console.log 2>&1 &
        echo "✅ H2 Console đã khởi động trong background!"
        echo "📝 Log file: h2_console.log"
        ;;
    "console-stop")
        echo "⏹️ Dừng H2 Web Console..."
        pkill -f "java.*H2WebConsole"
        echo "✅ Đã dừng H2 Console!"
        ;;
    "schema")
        echo "🏗️ Tạo đầy đủ schema database..."
        javac -cp "lib/*:src" -d out src/utils/DatabaseSchemaCreator.java
        java -cp "lib/*:out" utils.DatabaseSchemaCreator
        echo "✅ Schema đã được tạo!"
        ;;
    "seed")
        echo "🌱 Tạo dữ liệu mẫu..."
        javac -cp "lib/*:src" -d out src/utils/DataSeeder.java
        java -cp "lib/*:out" utils.DataSeeder
        echo "✅ Dữ liệu mẫu đã được tạo!"
        ;;
    "fix")
        echo "🔧 Sửa lỗi database..."
        javac -cp "lib/*:src" -d out src/utils/DatabaseFixer.java
        java -cp "lib/*:out" utils.DatabaseFixer
        echo "✅ Đã sửa lỗi database!"
        ;;
    "status")
        echo "📊 Trạng thái ứng dụng:"
        if pgrep -f "java.*views.Main" > /dev/null; then
            echo "✅ Ứng dụng đang chạy"
            ps aux | grep "java.*views.Main" | grep -v grep
        else
            echo "❌ Ứng dụng không chạy"
        fi
        
        echo ""
        echo "🌐 Trạng thái H2 Console:"
        if pgrep -f "java.*H2WebConsole" > /dev/null; then
            echo "✅ H2 Console đang chạy trên port 8082"
            echo "📡 URL: http://localhost:8082"
        else
            echo "❌ H2 Console không chạy"
        fi
        
        echo ""
        echo "📁 Database files:"
        ls -la *.db 2>/dev/null || echo "Không có file database"
        
        echo ""
        echo "📦 Compiled classes:"
        if [ -d "out" ]; then
            echo "✅ Thư mục out tồn tại"
            echo "   Views: $(ls out/views/*.class 2>/dev/null | wc -l) files"
            echo "   Utils: $(ls out/utils/*.class 2>/dev/null | wc -l) files"
        else
            echo "❌ Thư mục out không tồn tại"
        fi
        ;;
    *)
        echo "📖 Cách sử dụng:"
        echo "  ./run.sh compile       - Compile project"
        echo "  ./run.sh run          - Chạy ứng dụng"
        echo "  ./run.sh build        - Build hoàn toàn"
        echo "  ./run.sh reset        - Reset hoàn toàn (xóa DB + rebuild)"
        echo "  ./run.sh stop         - Dừng ứng dụng"
        echo "  ./run.sh view         - Xem dữ liệu database"
        echo "  ./run.sh fullview     - Xem đầy đủ dữ liệu database"
        echo "  ./run.sh test         - Test thêm nhân viên"
        echo "  ./run.sh console      - Khởi động H2 Web Console (foreground)"
        echo "  ./run.sh console-start - Khởi động H2 Web Console (background)"
        echo "  ./run.sh console-stop  - Dừng H2 Web Console"
        echo "  ./run.sh schema       - Tạo đầy đủ schema database"
        echo "  ./run.sh seed         - Tạo dữ liệu mẫu"
        echo "  ./run.sh fix          - Sửa lỗi database"
        echo "  ./run.sh status       - Xem trạng thái ứng dụng"
        echo ""
        echo "💡 Ví dụ: ./run.sh reset && ./run.sh run"
        echo "💡 Tạo schema: ./run.sh schema"
        echo "💡 Xem database: ./run.sh view"
        echo "💡 H2 Console: ./run.sh console-start"
        ;;
esac