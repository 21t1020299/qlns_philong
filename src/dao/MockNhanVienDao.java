package dao;

import bean.NhanVienBean;
import utils.MockDatabase;
import java.util.ArrayList;

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
        return MockDatabase.getLastMaNhanVien();
    }
}