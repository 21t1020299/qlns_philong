package bean;

public class NhanVienBean {
    private String manv, macv, email, tennv, gioitinh, ngaysinh;
    private String dantoc, trinhdo, noidkhktt, dchi, sdt;
    private String dchithuongtru, qtich, skhoe;
    private String hotencha, hotenme;

    public NhanVienBean() {}

    public NhanVienBean(String manv, String macv, String email, String tennv, String gioitinh, String ngaysinh,
                        String dantoc, String trinhdo, String noidkhktt, String dchi, String sdt,
                        String dchithuongtru, String qtich, String skhoe,
                        String hotencha, String hotenme) {
        this.manv = manv;
        this.macv = macv;
        this.email = email;
        this.tennv = tennv;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.dantoc = dantoc;
        this.trinhdo = trinhdo;
        this.noidkhktt = noidkhktt;
        this.dchi = dchi;
        this.sdt = sdt;
        this.dchithuongtru = dchithuongtru;
        this.qtich = qtich;
        this.skhoe = skhoe;
        this.hotencha = hotencha;
        this.hotenme = hotenme;
    }

    // Getters and setters
    public String getManv() { return manv; }
    public void setManv(String manv) { this.manv = manv; }

    public String getMacv() { return macv; }
    public void setMacv(String macv) { this.macv = macv; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTennv() { return tennv; }
    public void setTennv(String tennv) { this.tennv = tennv; }

    public String getGioitinh() { return gioitinh; }
    public void setGioitinh(String gioitinh) { this.gioitinh = gioitinh; }

    public String getNgsinh() { return ngaysinh; }
    public void setNgsinh(String ngaysinh) { this.ngaysinh = ngaysinh; }

    public String getDtoc() { return dantoc; }
    public void setDtoc(String dantoc) { this.dantoc = dantoc; }

    public String getTrinhdo() { return trinhdo; }
    public void setTrinhdo(String trinhdo) { this.trinhdo = trinhdo; }

    public String getNoidkhktt() { return noidkhktt; }
    public void setNoidkhktt(String noidkhktt) { this.noidkhktt = noidkhktt; }

    public String getDchi() { return dchi; }
    public void setDchi(String dchi) { this.dchi = dchi; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDchithuongtru() { return dchithuongtru; }
    public void setDchithuongtru(String dchithuongtru) { this.dchithuongtru = dchithuongtru; }

    public String getQtich() { return qtich; }
    public void setQtich(String qtich) { this.qtich = qtich; }

    public String getSkhoe() { return skhoe; }
    public void setSkhoe(String skhoe) { this.skhoe = skhoe; }

    public String getHotencha() { return hotencha; }
    public void setHotencha(String hotencha) { this.hotencha = hotencha; }

    public String getHotenme() { return hotenme; }
    public void setHotenme(String hotenme) { this.hotenme = hotenme; }
}
