package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class H2Connector {
    private static Connection connection;
    private static final String DB_URL = "jdbc:h2:./qlns_philong;AUTO_SERVER=TRUE";
    
    static {
        try {
            System.out.println("🔍 Đang khởi tạo H2 Database...");
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, "sa", "");
            
            // Tạo bảng nhanvien nếu chưa có
            createTableIfNotExists();
            
            // Thêm dữ liệu mẫu nếu bảng trống
            insertSampleDataIfEmpty();
            
            System.out.println("✅ H2 Database sẵn sàng!");
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi khởi tạo H2: " + e.getMessage());
            connection = null;
        }
    }
    
    private static void createTableIfNotExists() throws Exception {
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
        
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        System.out.println("✅ Bảng nhanvien đã sẵn sàng (16 trường)");
    }
    
    private static void insertSampleDataIfEmpty() throws Exception {
        // Kiểm tra xem bảng có dữ liệu không
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM nhanvien");
        rs.next();
        int count = rs.getInt(1);
        
        if (count == 0) {
            System.out.println("📝 Thêm dữ liệu mẫu...");
            
            String[] inserts = {
                "INSERT INTO nhanvien VALUES ('NV001', 'Nguyễn Văn An', 'Nam', 'nguyenvanan@gmail.com', '123 Đường ABC, Hà Nội', '1990-05-15', 'Việt Nam', '0123456789', 'Đại học', 'Hà Nội', '123 Đường ABC, Hà Nội', 'Kinh', 'Tốt', 'CV001', 'Nguyễn Văn Bố', 'Trần Thị Mẹ')",
                "INSERT INTO nhanvien VALUES ('NV002', 'Trần Thị Bình', 'Nữ', 'tranthibinh@gmail.com', '456 Đường XYZ, TP.HCM', '1992-08-20', 'Việt Nam', '0987654321', 'Cao đẳng', 'TP.HCM', '456 Đường XYZ, TP.HCM', 'Kinh', 'Tốt', 'CV002', 'Trần Văn Bố', 'Lê Thị Mẹ')",
                "INSERT INTO nhanvien VALUES ('NV003', 'Lê Văn Minh', 'Nam', 'levanminh@gmail.com', '789 Đường DEF, Đà Nẵng', '1988-12-10', 'Việt Nam', '0369852147', 'Đại học', 'Đà Nẵng', '789 Đường DEF, Đà Nẵng', 'Kinh', 'Tốt', 'CV001', 'Lê Văn Bố', 'Phạm Thị Mẹ')"
            };
            
            for (String insert : inserts) {
                stmt.execute(insert);
            }
            
            System.out.println("✅ Đã thêm 3 nhân viên mẫu");
        } else {
            System.out.println("📊 Bảng đã có " + count + " nhân viên");
        }
    }
    
    public static Connection getConnection() {
        return connection;
    }
    
    public static boolean isConnected() {
        return connection != null;
    }
}