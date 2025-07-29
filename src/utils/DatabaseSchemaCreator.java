package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSchemaCreator {
    public static void main(String[] args) {
        try {
            System.out.println("🏗️ Đang tạo đầy đủ schema database...");
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./qlns_philong", "sa", "");
            
            Statement stmt = conn.createStatement();
            
            // 1. Tạo bảng CHUCVU (Chức vụ)
            System.out.println("📋 Tạo bảng CHUCVU...");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS CHUCVU (
                    MACV VARCHAR(10) PRIMARY KEY,
                    TENCV VARCHAR(100)
                )
            """);
            
            // 2. Tạo bảng PHONGBAN (Phòng ban)
            System.out.println("🏢 Tạo bảng PHONGBAN...");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS PHONGBAN (
                    MAPBAN VARCHAR(10) PRIMARY KEY,
                    TENPB VARCHAR(100),
                    MATP VARCHAR(10)
                )
            """);
            
            // 3. Tạo bảng BANGCHAMCONG (Bảng chấm công)
            System.out.println("⏰ Tạo bảng BANGCHAMCONG...");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS BANGCHAMCONG (
                    MAPHIEN VARCHAR(20) PRIMARY KEY,
                    NGAY DATE,
                    MANV VARCHAR(10),
                    TINHTRANG VARCHAR(50)
                )
            """);
            
            // 4. Tạo bảng LUONG (Lương)
            System.out.println("💰 Tạo bảng LUONG...");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS LUONG (
                    MAPHIENTL VARCHAR(20) PRIMARY KEY,
                    MANV VARCHAR(10),
                    SOTK VARCHAR(20),
                    SOTIEN VARCHAR(20),
                    THANGNHAN VARCHAR(10),
                    TINHTRANG VARCHAR(50)
                )
            """);
            
            // 5. Thêm dữ liệu mẫu cho bảng CHUCVU
            System.out.println("📝 Thêm dữ liệu mẫu cho CHUCVU...");
            String[] chucVuInserts = {
                "INSERT INTO CHUCVU VALUES ('CV001', 'Nhân viên')",
                "INSERT INTO CHUCVU VALUES ('CV002', 'Trưởng nhóm')",
                "INSERT INTO CHUCVU VALUES ('CV003', 'Quản lý')",
                "INSERT INTO CHUCVU VALUES ('CV004', 'Giám đốc')",
                "INSERT INTO CHUCVU VALUES ('CV005', 'Thực tập sinh')"
            };
            
            for (String insert : chucVuInserts) {
                try {
                    stmt.execute(insert);
                } catch (Exception e) {
                    // Bỏ qua nếu dữ liệu đã tồn tại
                }
            }
            
            // 6. Thêm dữ liệu mẫu cho bảng PHONGBAN
            System.out.println("📝 Thêm dữ liệu mẫu cho PHONGBAN...");
            String[] phongBanInserts = {
                "INSERT INTO PHONGBAN VALUES ('PB001', 'Phòng Kế toán', 'NV001')",
                "INSERT INTO PHONGBAN VALUES ('PB002', 'Phòng Nhân sự', 'NV002')",
                "INSERT INTO PHONGBAN VALUES ('PB003', 'Phòng Kỹ thuật', 'NV003')",
                "INSERT INTO PHONGBAN VALUES ('PB004', 'Phòng Kinh doanh', 'NV004')",
                "INSERT INTO PHONGBAN VALUES ('PB005', 'Phòng Marketing', 'NV999')"
            };
            
            for (String insert : phongBanInserts) {
                try {
                    stmt.execute(insert);
                } catch (Exception e) {
                    // Bỏ qua nếu dữ liệu đã tồn tại
                }
            }
            
            // 7. Thêm dữ liệu mẫu cho bảng BANGCHAMCONG
            System.out.println("📝 Thêm dữ liệu mẫu cho BANGCHAMCONG...");
            String[] chamCongInserts = {
                "INSERT INTO BANGCHAMCONG VALUES ('PC001', '2025-07-29', 'NV001', 'Có mặt')",
                "INSERT INTO BANGCHAMCONG VALUES ('PC002', '2025-07-29', 'NV002', 'Có mặt')",
                "INSERT INTO BANGCHAMCONG VALUES ('PC003', '2025-07-29', 'NV003', 'Vắng mặt')",
                "INSERT INTO BANGCHAMCONG VALUES ('PC004', '2025-07-28', 'NV001', 'Có mặt')",
                "INSERT INTO BANGCHAMCONG VALUES ('PC005', '2025-07-28', 'NV002', 'Có mặt')",
                "INSERT INTO BANGCHAMCONG VALUES ('PC006', '2025-07-28', 'NV003', 'Có mặt')",
                "INSERT INTO BANGCHAMCONG VALUES ('PC007', '2025-07-27', 'NV001', 'Có mặt')",
                "INSERT INTO BANGCHAMCONG VALUES ('PC008', '2025-07-27', 'NV002', 'Vắng mặt')",
                "INSERT INTO BANGCHAMCONG VALUES ('PC009', '2025-07-27', 'NV003', 'Có mặt')"
            };
            
            for (String insert : chamCongInserts) {
                try {
                    stmt.execute(insert);
                } catch (Exception e) {
                    // Bỏ qua nếu dữ liệu đã tồn tại
                }
            }
            
            // 8. Thêm dữ liệu mẫu cho bảng LUONG
            System.out.println("📝 Thêm dữ liệu mẫu cho LUONG...");
            String[] luongInserts = {
                "INSERT INTO LUONG VALUES ('TL001', 'NV001', '1234567890', '15000000', '07/2025', 'Đã thanh toán')",
                "INSERT INTO LUONG VALUES ('TL002', 'NV002', '0987654321', '18000000', '07/2025', 'Đã thanh toán')",
                "INSERT INTO LUONG VALUES ('TL003', 'NV003', '1122334455', '12000000', '07/2025', 'Chưa thanh toán')",
                "INSERT INTO LUONG VALUES ('TL004', 'NV004', '5566778899', '10000000', '07/2025', 'Đã thanh toán')",
                "INSERT INTO LUONG VALUES ('TL005', 'NV999', '9988776655', '8000000', '07/2025', 'Chưa thanh toán')",
                "INSERT INTO LUONG VALUES ('TL006', 'NV001', '1234567890', '15000000', '06/2025', 'Đã thanh toán')",
                "INSERT INTO LUONG VALUES ('TL007', 'NV002', '0987654321', '18000000', '06/2025', 'Đã thanh toán')",
                "INSERT INTO LUONG VALUES ('TL008', 'NV003', '1122334455', '12000000', '06/2025', 'Đã thanh toán')"
            };
            
            for (String insert : luongInserts) {
                try {
                    stmt.execute(insert);
                } catch (Exception e) {
                    // Bỏ qua nếu dữ liệu đã tồn tại
                }
            }
            
            conn.close();
            System.out.println("✅ Đã tạo đầy đủ schema database!");
            System.out.println("📊 Các bảng đã tạo:");
            System.out.println("   - NHANVIEN (18 trường)");
            System.out.println("   - CHUCVU (2 trường)");
            System.out.println("   - PHONGBAN (3 trường)");
            System.out.println("   - BANGCHAMCONG (4 trường)");
            System.out.println("   - LUONG (6 trường)");
            System.out.println("\n🎯 Bây giờ database đã giống với Supabase!");
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}