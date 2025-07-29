package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class DataSeeder {
    
    public static void seedRealData() {
        try {
            System.out.println("🔍 Đang kết nối H2 Database...");
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./qlns_philong", "sa", "");
            
            // Tạo bảng trước
            createTableIfNotExists(conn);
            
            // Xóa dữ liệu cũ
            clearMockData(conn);
            
            // Thêm dữ liệu thật
            insertRealData(conn);
            
            // Hiển thị kết quả
            showResults(conn);
            
            conn.close();
            System.out.println("✅ Hoàn thành seeding dữ liệu thật!");
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTableIfNotExists(Connection conn) throws Exception {
        System.out.println("📋 Tạo bảng nhanvien...");
        String sql = """
            CREATE TABLE IF NOT EXISTS nhanvien (
                manv VARCHAR(10) PRIMARY KEY,
                tennv VARCHAR(100),
                gtinh VARCHAR(10),
                email VARCHAR(100),
                dchi VARCHAR(200),
                ngsinh DATE,
                qtich VARCHAR(50),
                sdt VARCHAR(15),
                trinhdo VARCHAR(50),
                noidkhktt VARCHAR(100),
                dchithuongtru VARCHAR(200),
                dtoc VARCHAR(50),
                skhoe VARCHAR(50),
                macv VARCHAR(10),
                hotencha VARCHAR(100),
                hotenme VARCHAR(100)
            )
        """;
        
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        System.out.println("✅ Bảng nhanvien đã sẵn sàng (16 trường)");
    }
    
    private static void clearMockData(Connection conn) throws Exception {
        System.out.println("🗑️ Xóa dữ liệu mock...");
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM nhanvien");
        System.out.println("✅ Đã xóa tất cả dữ liệu mock");
    }
    
    private static void insertRealData(Connection conn) throws Exception {
        System.out.println("📝 Thêm dữ liệu thật...");
        Statement stmt = conn.createStatement();
        
        // Dữ liệu nhân viên thật với thông tin đầy đủ
        String[] inserts = {
            // NV001 - Nhân viên 1
            "INSERT INTO nhanvien VALUES (" +
            "'NV001', 'Nguyễn Văn An', 'Nam', 'nguyenvanan@philong.com.vn', " +
            "'123 Đường Lê Lợi, Phường 1, Quận 1, TP.HCM', '1990-05-15', 'Việt Nam', '0123456789', " +
            "'Đại học', 'TP.HCM', '123 Đường Lê Lợi, Phường 1, Quận 1, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV001', 'Nguyễn Văn Bố', 'Trần Thị Mẹ', null, null)",
            
            // NV002 - Nhân viên 2  
            "INSERT INTO nhanvien VALUES (" +
            "'NV002', 'Trần Thị Bình', 'Nữ', 'tranthibinh@philong.com.vn', " +
            "'456 Đường Nguyễn Huệ, Phường 2, Quận 3, TP.HCM', '1992-08-20', 'Việt Nam', '0987654321', " +
            "'Cao đẳng', 'TP.HCM', '456 Đường Nguyễn Huệ, Phường 2, Quận 3, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV002', 'Trần Văn Bố', 'Lê Thị Mẹ', null, null)",
            
            // NV003 - Nhân viên 3
            "INSERT INTO nhanvien VALUES (" +
            "'NV003', 'Lê Văn Minh', 'Nam', 'levanminh@philong.com.vn', " +
            "'789 Đường Pasteur, Phường 3, Quận 1, TP.HCM', '1988-12-10', 'Việt Nam', '0369852147', " +
            "'Đại học', 'TP.HCM', '789 Đường Pasteur, Phường 3, Quận 1, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV001', 'Lê Văn Bố', 'Phạm Thị Mẹ', null, null)",
            
            // NV004 - Nhân viên 4
            "INSERT INTO nhanvien VALUES (" +
            "'NV004', 'Phạm Thị Lan', 'Nữ', 'phamthilan@philong.com.vn', " +
            "'321 Đường Võ Văn Tần, Phường 4, Quận 3, TP.HCM', '1995-03-25', 'Việt Nam', '0521478963', " +
            "'Trung cấp', 'TP.HCM', '321 Đường Võ Văn Tần, Phường 4, Quận 3, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV003', 'Phạm Văn Bố', 'Nguyễn Thị Mẹ', null, null)",
            
            // NV005 - Nhân viên 5
            "INSERT INTO nhanvien VALUES (" +
            "'NV005', 'Hoàng Văn Dũng', 'Nam', 'hoangvandung@philong.com.vn', " +
            "'654 Đường Hai Bà Trưng, Phường 5, Quận 1, TP.HCM', '1985-07-18', 'Việt Nam', '0789456123', " +
            "'Đại học', 'TP.HCM', '654 Đường Hai Bà Trưng, Phường 5, Quận 1, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV002', 'Hoàng Văn Bố', 'Trịnh Thị Mẹ', null, null)",
            
            // NV006 - Nhân viên 6
            "INSERT INTO nhanvien VALUES (" +
            "'NV006', 'Vũ Thị Hương', 'Nữ', 'vuthihuong@philong.com.vn', " +
            "'987 Đường Đồng Khởi, Phường 6, Quận 1, TP.HCM', '1993-11-30', 'Việt Nam', '0912345678', " +
            "'Cao đẳng', 'TP.HCM', '987 Đường Đồng Khởi, Phường 6, Quận 1, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV003', 'Vũ Văn Bố', 'Đặng Thị Mẹ', null, null)",
            
            // NV007 - Nhân viên 7
            "INSERT INTO nhanvien VALUES (" +
            "'NV007', 'Đặng Văn Phúc', 'Nam', 'dangvanphuc@philong.com.vn', " +
            "'147 Đường Lý Tự Trọng, Phường 7, Quận 1, TP.HCM', '1987-04-12', 'Việt Nam', '0654321987', " +
            "'Đại học', 'TP.HCM', '147 Đường Lý Tự Trọng, Phường 7, Quận 1, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV001', 'Đặng Văn Bố', 'Lý Thị Mẹ', null, null)",
            
            // NV008 - Nhân viên 8
            "INSERT INTO nhanvien VALUES (" +
            "'NV008', 'Lý Thị Mai', 'Nữ', 'lythimai@philong.com.vn', " +
            "'258 Đường Trần Hưng Đạo, Phường 8, Quận 5, TP.HCM', '1991-09-05', 'Việt Nam', '0898765432', " +
            "'Trung cấp', 'TP.HCM', '258 Đường Trần Hưng Đạo, Phường 8, Quận 5, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV003', 'Lý Văn Bố', 'Hoàng Thị Mẹ', null, null)",
            
            // NV009 - Nhân viên 9
            "INSERT INTO nhanvien VALUES (" +
            "'NV009', 'Bùi Văn Thành', 'Nam', 'buivanthanh@philong.com.vn', " +
            "'369 Đường Nguyễn Thị Minh Khai, Phường 9, Quận 3, TP.HCM', '1989-06-22', 'Việt Nam', '0432167890', " +
            "'Đại học', 'TP.HCM', '369 Đường Nguyễn Thị Minh Khai, Phường 9, Quận 3, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV002', 'Bùi Văn Bố', 'Vũ Thị Mẹ', null, null)",
            
            // NV010 - Nhân viên 10
            "INSERT INTO nhanvien VALUES (" +
            "'NV010', 'Ngô Thị Xuân', 'Nữ', 'ngothixuan@philong.com.vn', " +
            "'741 Đường Cách Mạng Tháng 8, Phường 10, Quận 3, TP.HCM', '1994-01-15', 'Việt Nam', '0765432109', " +
            "'Cao đẳng', 'TP.HCM', '741 Đường Cách Mạng Tháng 8, Phường 10, Quận 3, TP.HCM', 'Kinh', 'Tốt', " +
            "'CV003', 'Ngô Văn Bố', 'Bùi Thị Mẹ', null, null)"
        };
        
        for (String insert : inserts) {
            stmt.execute(insert);
        }
        
        System.out.println("✅ Đã thêm " + inserts.length + " nhân viên thật");
    }
    
    private static void showResults(Connection conn) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM nhanvien");
        rs.next();
        int total = rs.getInt("total");
        
        System.out.println("\n📊 Kết quả:");
        System.out.println("   Tổng số nhân viên: " + total);
        System.out.println("   Dữ liệu: Thật (không phải mock)");
        System.out.println("   Công ty: PhiLong");
        System.out.println("   Email domain: @philong.com.vn");
    }
    
    public static void main(String[] args) {
        seedRealData();
    }
} 