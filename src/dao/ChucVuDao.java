package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.ChucVuBean;
import utils.ConnectorDB;

public class ChucVuDao {
    public ArrayList<ChucVuBean> getList() throws Exception {
        ArrayList<ChucVuBean> ds = new ArrayList<>();
        String sql = "SELECT * FROM chucvu"; // ✅ Đảm bảo tên bảng chính xác

        PreparedStatement cmd = ConnectorDB.getConnection().prepareStatement(sql);
        ResultSet rs = cmd.executeQuery();

        while (rs.next()) {
            String maChucVu = rs.getString("maChucVu");
            String maPhongBan = rs.getString("maPhongBan");
            String tenChucVu = rs.getString("tenChucVu");
            long mucLuong = rs.getLong("mucLuong");

            ds.add(new ChucVuBean(maChucVu, maPhongBan, tenChucVu, mucLuong));
        }

        rs.close();
        cmd.close();
        return ds;
    }
}
