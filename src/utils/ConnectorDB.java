package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectorDB {
    private static Connection connection;
    private static boolean useMockDatabase = false;

    static {
        try {
            System.out.println("🔍 Đang kết nối H2 Database...");
            
            // Chỉ sử dụng H2 Database
            Class.forName("org.h2.Driver");
            connection = H2Connector.getConnection();
            
            if (connection != null) {
                System.out.println("✅ Kết nối thành công tới H2 Database!");
            } else {
                throw new Exception("H2 connection is null");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Không thể kết nối H2 Database: " + e.getMessage());
            
            // Nếu H2 thất bại, sử dụng Mock Database
            System.out.println("\n🔧 Chuyển sang sử dụng Mock Database");
            connection = null;
            useMockDatabase = true;
        }
    }

    public static Connection getConnection() {
        return connection;
    }
    
    public static boolean isUsingMockDatabase() {
        return useMockDatabase;
    }
}
