package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.LoaiCaBean;
import utils.ConnectorDB;

public class LoaiCaDao {
    public ArrayList<LoaiCaBean> getList() throws Exception {
        ArrayList<LoaiCaBean> ds = new ArrayList<>();

        String sql = "SELECT * FROM loaica"; // ✅ Đảm bảo đúng tên bảng trong DB

        PreparedStatement cmd = ConnectorDB.getConnection().prepareStatement(sql);
        ResultSet rs = cmd.executeQuery();

        while (rs.next()) {
            String id = rs.getString("maLoaiCa");
            String tenLoaiCa = rs.getString("tenLoaiCa");

            ds.add(new LoaiCaBean(id, tenLoaiCa));
        }

        rs.close();
        cmd.close();
        return ds;
    }
}
