package bean;

public class ChucVuBean {

    private String MaChucVu;
    private String MaPhongBan;
    private String TenChucVu;
    private long MucLuong;
    public ChucVuBean(String maChucVu, String maPhongBan, String tenChucVu, long mucLuong) {
        super();
        MaChucVu = maChucVu;
        MaPhongBan = maPhongBan;
        TenChucVu = tenChucVu;
        MucLuong = mucLuong;
    }
    public String getMaChucVu() {
        return MaChucVu;
    }
    public void setMaChucVu(String maChucVu) {
        MaChucVu = maChucVu;
    }
    public String getMaPhongBan() {
        return MaPhongBan;
    }
    public void setMaPhongBan(String maPhongBan) {
        MaPhongBan = maPhongBan;
    }
    public String getTenChucVu() {
        return TenChucVu;
    }
    public void setTenChucVu(String tenChucVu) {
        TenChucVu = tenChucVu;
    }
    public long getMucLuong() {
        return MucLuong;
    }
    public void setMucLuong(long mucLuong) {
        MucLuong = mucLuong;
    }


}
