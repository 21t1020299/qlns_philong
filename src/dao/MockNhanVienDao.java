package dao;

import bean.NhanVienBean;
import utils.MockDatabase;
import java.util.ArrayList;
import java.util.List;

public class MockNhanVienDao {
    
    public ArrayList<NhanVienBean> getAllNhanVien() throws Exception {
        return new ArrayList<>(MockDatabase.getAllNhanVien());
    }
    
    public int Them(NhanVienBean nv) throws Exception {
        MockDatabase.addNhanVien(nv);
        return 1; // Trả về 1 để báo thành công
    }
    
    public int CapNhat(NhanVienBean nv) throws Exception {
        MockDatabase.updateNhanVien(nv);
        return 1; // Trả về 1 để báo thành công
    }
    
    public int XoaNhanVien(String maNV) throws Exception {
        MockDatabase.deleteNhanVien(maNV);
        return 1; // Trả về 1 để báo thành công
    }
    
    public String getMaNhanVienLast() throws Exception {
        List<String> existingIds = new ArrayList<>();
        for (NhanVienBean nv : MockDatabase.getAllNhanVien()) {
            if (nv.getManv() != null && nv.getManv().startsWith("NV")) {
                existingIds.add(nv.getManv());
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