package views;

import bean.NhanVienBean;

public interface RouterListener {
    void routeTo(String path);
    void routeWithNhanVien(String path, NhanVienBean nhanvien);
}
