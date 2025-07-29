package utils;

import bean.NhanVienBean;
import java.util.ArrayList;
import java.util.List;

public class MockDatabase {
    private static List<NhanVienBean> mockData = new ArrayList<>();
    
    static {
        // Tạo dữ liệu mẫu
        mockData.add(new NhanVienBean(
            "NV001", "CV001", "nguyenvanan@gmail.com", "Nguyễn Văn An", "Nam", "1990-05-15",
            "Kinh", "Đại học", "Hà Nội", "123 Đường ABC, Hà Nội", "0123456789",
            "123 Đường ABC, Hà Nội", "Việt Nam", "Tốt", "Nguyễn Văn Bố", "Trần Thị Mẹ"
        ));
        
        mockData.add(new NhanVienBean(
            "NV002", "CV002", "tranthibinh@gmail.com", "Trần Thị Bình", "Nữ", "1992-08-20",
            "Kinh", "Cao đẳng", "TP.HCM", "456 Đường XYZ, TP.HCM", "0987654321",
            "456 Đường XYZ, TP.HCM", "Việt Nam", "Tốt", "Trần Văn Bố", "Lê Thị Mẹ"
        ));
        
        mockData.add(new NhanVienBean(
            "NV003", "CV001", "levanminh@gmail.com", "Lê Văn Minh", "Nam", "1988-12-10",
            "Kinh", "Đại học", "Đà Nẵng", "789 Đường DEF, Đà Nẵng", "0369852147",
            "789 Đường DEF, Đà Nẵng", "Việt Nam", "Tốt", "Lê Văn Bố", "Phạm Thị Mẹ"
        ));
    }
    
    public static List<NhanVienBean> getAllNhanVien() {
        return new ArrayList<>(mockData);
    }
    
    public static void addNhanVien(NhanVienBean nv) {
        mockData.add(nv);
    }
    
    public static void updateNhanVien(NhanVienBean nv) {
        for (int i = 0; i < mockData.size(); i++) {
            if (mockData.get(i).getManv().equals(nv.getManv())) {
                mockData.set(i, nv);
                break;
            }
        }
    }
    
    public static void deleteNhanVien(String maNV) {
        mockData.removeIf(nv -> nv.getManv().equals(maNV));
    }
    
    public static String getLastMaNhanVien() {
        if (mockData.isEmpty()) {
            return "NV001";
        }
        String lastMa = mockData.get(mockData.size() - 1).getManv();
        // Tạo mã mới dựa trên mã cuối cùng
        if (lastMa != null && lastMa.startsWith("NV")) {
            try {
                int num = Integer.parseInt(lastMa.substring(2)) + 1;
                return String.format("NV%03d", num);
            } catch (NumberFormatException e) {
                // Nếu không parse được số, trả về NV001
                return "NV001";
            }
        }
        return "NV001";
    }
    
    public static boolean isConnected() {
        return true; // Mock database luôn sẵn sàng
    }
}