package bean;

public class LoaiCaBean {
    private String id;
    private String tenLoaiCa;

    public LoaiCaBean() {}

    public LoaiCaBean(String id, String tenLoaiCa) {
        this.id = id;
        this.tenLoaiCa = tenLoaiCa;
    }

    public String getId() { return id; }
    public String getTenLoaiCa() { return tenLoaiCa; }

    public void setId(String id) { this.id = id; }
    public void setTenLoaiCa(String tenLoaiCa) { this.tenLoaiCa = tenLoaiCa; }
}
