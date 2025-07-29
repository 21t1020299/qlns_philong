package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFixer {
    public static void main(String[] args) {
        try {
            System.out.println("🔧 Đang kiểm tra và sửa lỗi database...");
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./qlns_philong", "sa", "");
            
            // 1. Kiểm tra mã nhân viên trùng lặp
            checkDuplicateEmployeeIds(conn);
            
            // 2. Sửa mã nhân viên trùng lặp
            fixDuplicateEmployeeIds(conn);
            
            // 3. Kiểm tra lại
            checkDuplicateEmployeeIds(conn);
            
            // 4. Hiển thị danh sách nhân viên sau khi sửa
            showAllEmployees(conn);
            
            conn.close();
            System.out.println("✅ Hoàn thành kiểm tra và sửa lỗi!");
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkDuplicateEmployeeIds(Connection conn) throws Exception {
        System.out.println("\n🔍 Kiểm tra mã nhân viên trùng lặp...");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("""
            SELECT manv, COUNT(*) as count 
            FROM nhanvien 
            GROUP BY manv 
            HAVING COUNT(*) > 1
        """);
        
        boolean hasDuplicates = false;
        while (rs.next()) {
            hasDuplicates = true;
            String manv = rs.getString("manv");
            int count = rs.getInt("count");
            System.out.println("❌ Mã " + manv + " xuất hiện " + count + " lần");
        }
        
        if (!hasDuplicates) {
            System.out.println("✅ Không có mã nhân viên trùng lặp");
        }
    }
    
    private static void fixDuplicateEmployeeIds(Connection conn) throws Exception {
        System.out.println("\n🔧 Sửa mã nhân viên trùng lặp...");
        Statement stmt = conn.createStatement();
        
        // Lấy danh sách tất cả nhân viên
        ResultSet rs = stmt.executeQuery("SELECT * FROM nhanvien ORDER BY manv");
        List<String> existingIds = new ArrayList<>();
        List<String> duplicateIds = new ArrayList<>();
        
        while (rs.next()) {
            String manv = rs.getString("manv");
            if (existingIds.contains(manv)) {
                duplicateIds.add(manv);
            } else {
                existingIds.add(manv);
            }
        }
        
        if (!duplicateIds.isEmpty()) {
            System.out.println("📝 Tìm thấy mã trùng lặp: " + duplicateIds);
            
            // Tạo mã mới cho các bản ghi trùng lặp
            int newIdCounter = 1;
            for (String duplicateId : duplicateIds) {
                // Tìm mã mới chưa được sử dụng
                String newId;
                do {
                    newId = String.format("NV%03d", newIdCounter);
                    newIdCounter++;
                } while (existingIds.contains(newId));
                
                // Cập nhật mã đầu tiên thành mã mới
                stmt.execute("UPDATE nhanvien SET manv = '" + newId + "' WHERE manv = '" + duplicateId + "' LIMIT 1");
                existingIds.add(newId);
                System.out.println("✅ Đã sửa " + duplicateId + " thành " + newId);
            }
        } else {
            System.out.println("✅ Không cần sửa mã trùng lặp");
        }
    }
    
    private static void showAllEmployees(Connection conn) throws Exception {
        System.out.println("\n📊 Danh sách nhân viên sau khi sửa:");
        System.out.println("=" .repeat(60));
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM nhanvien ORDER BY manv");
        
        int count = 0;
        while (rs.next()) {
            count++;
            String manv = rs.getString("manv");
            String tennv = rs.getString("tennv");
            String email = rs.getString("email");
            String sdt = rs.getString("sdt");
            
            System.out.println("👤 " + count + ". " + manv + " - " + tennv);
            System.out.println("   📧 " + email + " | 📞 " + sdt);
            System.out.println("-" .repeat(40));
        }
        
        System.out.println("📈 Tổng số nhân viên: " + count);
    }
}