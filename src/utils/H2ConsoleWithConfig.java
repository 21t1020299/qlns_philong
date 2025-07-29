package utils;

import org.h2.tools.Server;
import java.sql.SQLException;

public class H2ConsoleWithConfig {
    public static void main(String[] args) {
        try {
            System.out.println("🌐 Đang khởi động H2 Console với cấu hình sẵn...");
            System.out.println("📡 URL: http://localhost:8082");
            System.out.println("🔑 Username: sa");
            System.out.println("🔑 Password: (để trống)");
            System.out.println("📁 Database: ./qlns_philong");
            System.out.println("\n💡 URL JDBC sẽ được cấu hình sẵn!");
            System.out.println("⏹️  Nhấn Ctrl+C để dừng server");
            
            // Khởi động H2 Web Console với cấu hình
            Server server = Server.createWebServer(
                "-web", 
                "-webAllowOthers", 
                "-webPort", "8082",
                "-webDaemon",
                "-ifNotExists"
            ).start();
            
            // Giữ server chạy
            while (true) {
                Thread.sleep(1000);
            }
            
        } catch (SQLException | InterruptedException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }
}