package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class H2DatabaseViewer {
    public static void main(String[] args) {
        try {
            System.out.println("🔍 Đang kết nối H2 Database...");
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./qlns_philong", "sa", "");
            
            System.out.println("✅ Kết nối thành công!");
            System.out.println("\n📊 Dữ liệu trong bảng nhanvien:");
            System.out.println("=" .repeat(80));
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM nhanvien ORDER BY manv");
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("\n👤 Nhân viên " + count + ":");
                System.out.println("   Mã NV: " + rs.getString("manv"));
                System.out.println("   Họ tên: " + rs.getString("tennv"));
                System.out.println("   Giới tính: " + rs.getString("gtinh"));
                System.out.println("   Email: " + rs.getString("email"));
                System.out.println("   Ngày sinh: " + rs.getDate("ngsinh"));
                System.out.println("   SĐT: " + rs.getString("sdt"));
                System.out.println("   Địa chỉ: " + rs.getString("dchi"));
                System.out.println("   Chức vụ: " + rs.getString("macv"));
                System.out.println("   Dân tộc: " + rs.getString("dtoc"));
                System.out.println("   Quốc tịch: " + rs.getString("qtich"));
                System.out.println("   Trình độ: " + rs.getString("trinhdo"));
                System.out.println("   Sức khỏe: " + rs.getString("skhoe"));
                System.out.println("   Nơi ĐK HKTT: " + rs.getString("noidkhktt"));
                System.out.println("   Địa chỉ thường trú: " + rs.getString("dchithuongtru"));
                System.out.println("   Họ tên cha: " + rs.getString("hotencha"));
                System.out.println("   Họ tên mẹ: " + rs.getString("hotenme"));
                System.out.println("   Ảnh chân dung: " + rs.getString("anhchandung"));
                System.out.println("   Ảnh CMND: " + rs.getString("anhcmnd"));
                System.out.println("-" .repeat(40));
            }
            
            System.out.println("\n📈 Tổng số nhân viên: " + count);
            
            // Kiểm tra schema
            System.out.println("\n🔧 Thông tin schema:");
            ResultSet schemaRs = stmt.executeQuery("SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NHANVIEN' ORDER BY ORDINAL_POSITION");
            System.out.println("   Cột\t\t\tKiểu dữ liệu\t\tCho phép NULL");
            System.out.println("-" .repeat(60));
            while (schemaRs.next()) {
                String colName = schemaRs.getString("COLUMN_NAME");
                String dataType = schemaRs.getString("DATA_TYPE");
                String isNullable = schemaRs.getString("IS_NULLABLE");
                System.out.printf("   %-20s %-15s %s%n", colName, dataType, isNullable);
            }
            
            conn.close();
            System.out.println("\n✅ Hoàn thành!");
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}