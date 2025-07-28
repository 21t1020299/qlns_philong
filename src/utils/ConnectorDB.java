package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectorDB {
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://db.wsisepsdhioaikdzrnmd.supabase.co:5432/postgres",
                    "postgres",
                    "03022003Aa"
            );
            System.out.println("✅ Kết nối thành công tới Supabase!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Kết nối thất bại!");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
