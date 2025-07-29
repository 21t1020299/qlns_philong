package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestAddEmployee {
    public static void main(String[] args) {
        try {
            System.out.println("🧪 Test thêm nhân viên mới...");
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./qlns_philong", "sa", "");
            
            // Đếm số nhân viên trước khi thêm
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM nhanvien");
            rs.next();
            int countBefore = rs.getInt("total");
            System.out.println("📊 Số nhân viên trước khi thêm: " + countBefore);
            
            // Tạo mã nhân viên mới
            String newManv = generateNewEmployeeId(conn);
            System.out.println("🆔 Mã nhân viên mới: " + newManv);
            
            // Thêm nhân viên mới
            String sql = "INSERT INTO nhanvien VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, newManv); // manv
            ps.setString(2, "Nguyễn Văn Test"); // tennv
            ps.setString(3, "Nam"); // gtinh
            ps.setString(4, "test@philong.com.vn"); // email
            ps.setString(5, "123 Đường Test, TP.HCM"); // dchi
            ps.setDate(6, java.sql.Date.valueOf("1995-01-01")); // ngsinh
            ps.setString(7, "Việt Nam"); // qtich
            ps.setString(8, "0123456789"); // sdt
            ps.setString(9, "Đại học"); // trinhdo
            ps.setString(10, "TP.HCM"); // noidkhktt
            ps.setString(11, "123 Đường Test, TP.HCM"); // dchithuongtru
            ps.setString(12, "Kinh"); // dtoc
            ps.setString(13, "Tốt"); // skhoe
            ps.setString(14, "CV001"); // macv
            ps.setString(15, "Nguyễn Văn Bố Test"); // hotencha
            ps.setString(16, "Trần Thị Mẹ Test"); // hotenme
            ps.setString(17, null); // anhchandung
            ps.setString(18, null); // anhcmnd
            
            int result = ps.executeUpdate();
            System.out.println("✅ Thêm nhân viên thành công! Kết quả: " + result);
            
            // Đếm số nhân viên sau khi thêm
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM nhanvien");
            rs.next();
            int countAfter = rs.getInt("total");
            System.out.println("📊 Số nhân viên sau khi thêm: " + countAfter);
            
            // Hiển thị nhân viên vừa thêm
            System.out.println("\n👤 Nhân viên vừa thêm:");
            rs = stmt.executeQuery("SELECT * FROM nhanvien WHERE manv = '" + newManv + "'");
            if (rs.next()) {
                System.out.println("   Mã NV: " + rs.getString("manv"));
                System.out.println("   Họ tên: " + rs.getString("tennv"));
                System.out.println("   Email: " + rs.getString("email"));
                System.out.println("   SĐT: " + rs.getString("sdt"));
                System.out.println("   Chức vụ: " + rs.getString("macv"));
            }
            
            conn.close();
            System.out.println("\n✅ Test hoàn thành!");
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String generateNewEmployeeId(Connection conn) throws Exception {
        // Lấy tất cả mã nhân viên hiện có
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT manv FROM nhanvien ORDER BY manv");
        
        List<String> existingIds = new ArrayList<>();
        while (rs.next()) {
            String manv = rs.getString("manv");
            if (manv != null && manv.startsWith("NV")) {
                existingIds.add(manv);
            }
        }
        
        if (existingIds.isEmpty()) {
            return "NV001";
        }
        
        // Tìm mã tiếp theo chưa được sử dụng
        int nextId = 1;
        while (true) {
            String candidateId = String.format("NV%03d", nextId);
            if (!existingIds.contains(candidateId)) {
                return candidateId;
            }
            nextId++;
        }
    }
}