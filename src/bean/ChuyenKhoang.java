package bean;

public class ChuyenKhoang {
    private int id;
    private String tenChuyen;

    public ChuyenKhoang() {}

    public ChuyenKhoang(int id, String tenChuyen) {
        this.id = id;
        this.tenChuyen = tenChuyen;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenChuyen() { return tenChuyen; }
    public void setTenChuyen(String tenChuyen) { this.tenChuyen = tenChuyen; }
}
