package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FullDatabaseViewer {
    public static void main(String[] args) {
        try {
            System.out.println("🔍 Đang kết nối H2 Database...");
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./qlns_philong", "sa", "");
            
            System.out.println("✅ Kết nối thành công!");
            System.out.println("\n📊 DỮ LIỆU ĐẦY ĐỦ DATABASE:");
            System.out.println("=" .repeat(80));
            
            Statement stmt = conn.createStatement();
            
            // 1. Xem tất cả bảng
            System.out.println("\n📋 DANH SÁCH CÁC BẢNG:");
            System.out.println("-" .repeat(40));
            ResultSet tables = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC' ORDER BY TABLE_NAME");
            while (tables.next()) {
                System.out.println("   📄 " + tables.getString("TABLE_NAME"));
            }
            
            // 2. Xem dữ liệu bảng NHANVIEN
            System.out.println("\n👥 BẢNG NHANVIEN:");
            System.out.println("-" .repeat(40));
            ResultSet rs = stmt.executeQuery("SELECT * FROM NHANVIEN ORDER BY MANV");
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("   👤 Nhân viên " + count + ": " + rs.getString("MANV") + " - " + rs.getString("TENNV"));
            }
            System.out.println("   📈 Tổng: " + count + " nhân viên");
            
            // 3. Xem dữ liệu bảng CHUCVU
            System.out.println("\n📋 BẢNG CHUCVU:");
            System.out.println("-" .repeat(40));
            rs = stmt.executeQuery("SELECT * FROM CHUCVU ORDER BY MACV");
            while (rs.next()) {
                System.out.println("   🏷️ " + rs.getString("MACV") + " - " + rs.getString("TENCV"));
            }
            
            // 4. Xem dữ liệu bảng PHONGBAN
            System.out.println("\n🏢 BẢNG PHONGBAN:");
            System.out.println("-" .repeat(40));
            rs = stmt.executeQuery("SELECT * FROM PHONGBAN ORDER BY MAPBAN");
            while (rs.next()) {
                System.out.println("   🏢 " + rs.getString("MAPBAN") + " - " + rs.getString("TENPB") + " (Trưởng phòng: " + rs.getString("MATP") + ")");
            }
            
            // 5. Xem dữ liệu bảng BANGCHAMCONG
            System.out.println("\n⏰ BẢNG BANGCHAMCONG:");
            System.out.println("-" .repeat(40));
            rs = stmt.executeQuery("SELECT * FROM BANGCHAMCONG ORDER BY NGAY DESC, MANV");
            while (rs.next()) {
                System.out.println("   ⏰ " + rs.getString("MAPHIEN") + " - " + rs.getDate("NGAY") + " - " + rs.getString("MANV") + " - " + rs.getString("TINHTRANG"));
            }
            
            // 6. Xem dữ liệu bảng LUONG
            System.out.println("\n💰 BẢNG LUONG:");
            System.out.println("-" .repeat(40));
            rs = stmt.executeQuery("SELECT * FROM LUONG ORDER BY THANGNHAN DESC, MANV");
            while (rs.next()) {
                System.out.println("   💰 " + rs.getString("MAPHIENTL") + " - " + rs.getString("MANV") + " - " + rs.getString("SOTIEN") + " - " + rs.getString("THANGNHAN") + " - " + rs.getString("TINHTRANG"));
            }
            
            // 7. Thống kê tổng quan
            System.out.println("\n📊 THỐNG KÊ TỔNG QUAN:");
            System.out.println("-" .repeat(40));
            
            // Đếm nhân viên
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM NHANVIEN");
            rs.next();
            System.out.println("   👥 Tổng nhân viên: " + rs.getInt("total"));
            
            // Đếm chức vụ
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM CHUCVU");
            rs.next();
            System.out.println("   📋 Tổng chức vụ: " + rs.getInt("total"));
            
            // Đếm phòng ban
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM PHONGBAN");
            rs.next();
            System.out.println("   🏢 Tổng phòng ban: " + rs.getInt("total"));
            
            // Đếm bảng chấm công
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM BANGCHAMCONG");
            rs.next();
            System.out.println("   ⏰ Tổng bảng chấm công: " + rs.getInt("total"));
            
            // Đếm bảng lương
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM LUONG");
            rs.next();
            System.out.println("   💰 Tổng bảng lương: " + rs.getInt("total"));
            
            conn.close();
            System.out.println("\n✅ Hoàn thành xem dữ liệu!");
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}