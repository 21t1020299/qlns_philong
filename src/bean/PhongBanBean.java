package bean;

public class PhongBanBean {
    private String maPhongBan;
    private String tenPhongBan;
    private String maTP;

    // ✅ Constructor đầy đủ 3 tham số
    public PhongBanBean(String maPhongBan, String tenPhongBan, String maTP) {
        this.maPhongBan = maPhongBan;
        this.tenPhongBan = tenPhongBan;
        this.maTP = maTP;
    }

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    public String getMaTP() {
        return maTP;
    }

    public void setMaTP(String maTP) {
        this.maTP = maTP;
    }
}
