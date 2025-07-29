package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectorDB {
    private static Connection connection;
    private static boolean useMockDatabase = false;

    // Cấu hình Supabase
    private static final String SUPABASE_HOST = "wsisepsdhioaikdzrnmd.supabase.co";
    private static final int SUPABASE_PORT = 5432;
    private static final String SUPABASE_DATABASE = "postgres";
    private static final String SUPABASE_USER = "postgres";
    private static final String SUPABASE_PASSWORD = "kiemdinhnhom6";
    
    // Cấu hình Local PostgreSQL
    private static final String LOCAL_HOST = "localhost";
    private static final int LOCAL_PORT = 5432;
    private static final String LOCAL_DATABASE = "qlns_philong";
    private static final String LOCAL_USER = "postgres";
    private static final String LOCAL_PASSWORD = "password";

    static {
        try {
            System.out.println("🔍 Đang thử kết nối database...");
            
            Class.forName("org.postgresql.Driver");
            
            // Thử kết nối Supabase trước
            try {
                System.out.println("\n📡 Thử kết nối Supabase...");
                String supabaseUrl = "jdbc:postgresql://" + SUPABASE_HOST + ":" + SUPABASE_PORT + "/" + SUPABASE_DATABASE;
                System.out.println("URL: " + supabaseUrl);
                connection = DriverManager.getConnection(supabaseUrl, SUPABASE_USER, SUPABASE_PASSWORD);
                System.out.println("✅ Kết nối thành công tới Supabase!");
            } catch (Exception e) {
                System.out.println("❌ Không thể kết nối Supabase: " + e.getMessage());
                
                // Thử kết nối Local database
                try {
                    System.out.println("\n📡 Thử kết nối Local Database...");
                    String localUrl = "jdbc:postgresql://" + LOCAL_HOST + ":" + LOCAL_PORT + "/" + LOCAL_DATABASE;
                    System.out.println("URL: " + localUrl);
                    connection = DriverManager.getConnection(localUrl, LOCAL_USER, LOCAL_PASSWORD);
                    System.out.println("✅ Kết nối thành công tới Local Database!");
                } catch (Exception e2) {
                    System.out.println("❌ Không thể kết nối Local Database: " + e2.getMessage());
                    
                    // Thử kết nối H2 Database
                    try {
                        System.out.println("\n📡 Thử kết nối H2 Database...");
                        Class.forName("org.h2.Driver");
                        connection = H2Connector.getConnection();
                        if (connection != null) {
                            System.out.println("✅ Kết nối thành công tới H2 Database!");
                        } else {
                            throw new Exception("H2 connection is null");
                        }
                    } catch (Exception e3) {
                        System.out.println("❌ Không thể kết nối H2 Database: " + e3.getMessage());
                        
                        // Nếu cả ba đều thất bại, sử dụng Mock Database
                        System.out.println("\n🔧 Chuyển sang sử dụng Mock Database");
                        connection = null;
                        useMockDatabase = true;
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi khởi tạo database: " + e.getMessage());
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
