package bean;

public class CaBean {
    private int id;
    private String tenCa;

    public CaBean() {}

    public CaBean(int id, String tenCa) {
        this.id = id;
        this.tenCa = tenCa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenCa() { return tenCa; }
    public void setTenCa(String tenCa) { this.tenCa = tenCa; }
}
