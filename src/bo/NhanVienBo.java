package bo;

import bean.NhanVienBean;
import dao.NhanVienDao;
import dao.MockNhanVienDao;
import utils.ConnectorDB;
import java.util.ArrayList;

public class NhanVienBo {
    private NhanVienDao nhanVienDao = new NhanVienDao();
    private MockNhanVienDao mockNhanVienDao = new MockNhanVienDao();
    
    public NhanVienBo() {
        // Không cần làm gì, ConnectorDB đã xử lý việc kiểm tra kết nối
    }

    public ArrayList<NhanVienBean> getAllNhanVien() throws Exception {
        if (ConnectorDB.isUsingMockDatabase()) {
            return mockNhanVienDao.getAllNhanVien();
        }
        return nhanVienDao.getAllNhanVien();
    }

    public int themNhanVien(NhanVienBean nv) throws Exception {
        if (ConnectorDB.isUsingMockDatabase()) {
            return mockNhanVienDao.Them(nv);
        }
        return nhanVienDao.Them(nv);
    }

    public int capNhatNhanVien(NhanVienBean nv) throws Exception {
        if (ConnectorDB.isUsingMockDatabase()) {
            return mockNhanVienDao.CapNhat(nv);
        }
        return nhanVienDao.CapNhat(nv);
    }

    public int XoaNhanVien(String maNV) throws Exception {
        if (ConnectorDB.isUsingMockDatabase()) {
            return mockNhanVienDao.XoaNhanVien(maNV);
        }
        return nhanVienDao.XoaNhanVien(maNV);
    }

    public String newMaNhanVien() throws Exception {
        if (ConnectorDB.isUsingMockDatabase()) {
            String lastMa = mockNhanVienDao.getMaNhanVienLast();
            if (lastMa == null || lastMa.equals("NV000")) {
                return "NV001";
            }
            int num = Integer.parseInt(lastMa.substring(2)) + 1;
            return String.format("NV%03d", num);
        }
        // Sử dụng logic tạo mã mới từ DAO thật
        return nhanVienDao.getMaNhanVienLast();
    }
    
    public boolean isUsingMockDatabase() {
        return ConnectorDB.isUsingMockDatabase();
    }
}
