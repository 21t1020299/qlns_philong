package bo;

import bean.NhanVienBean;
import dao.NhanVienDao;
import java.util.ArrayList;

public class NhanVienBo {
    public ArrayList<NhanVienBean> getAllNhanVien() throws Exception {
        return NhanVienDao.getAllNhanVien();
    }

    public int themNhanVien(NhanVienBean nv) throws Exception {
        return new NhanVienDao().Them(nv);
    }

    public int capNhatNhanVien(NhanVienBean nv) throws Exception {
        return new NhanVienDao().CapNhat(nv);
    }

    public String newMaNhanVien() throws Exception {
        String last = NhanVienDao.getMaNhanVienLast();
        if (last == null) return "NV001";
        int num = Integer.parseInt(last.substring(2)) + 1;
        return String.format("NV%03d", num);
    }
}
