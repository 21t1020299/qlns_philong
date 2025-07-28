package bean;

import java.util.Date;

public class LamViecBean {
    private int id;
    private int nhanVienId;
    private Date ngayLam;
    private int soGio;

    public LamViecBean() {}

    public LamViecBean(int id, int nhanVienId, Date ngayLam, int soGio) {
        this.id = id;
        this.nhanVienId = nhanVienId;
        this.ngayLam = ngayLam;
        this.soGio = soGio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNhanVienId() {
        return nhanVienId;
    }

    public void setNhanVienId(int nhanVienId) {
        this.nhanVienId = nhanVienId;
    }

    public Date getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(Date ngayLam) {
        this.ngayLam = ngayLam;
    }

    public int getSoGio() {
        return soGio;
    }

    public void setSoGio(int soGio) {
        this.soGio = soGio;
    }
}
