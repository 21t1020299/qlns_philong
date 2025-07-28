package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.LamViecBean;
import utils.ConnectorDB;
import java.sql.Date;

public class LamViecDao {
    public ArrayList<LamViecBean> getList() throws Exception {
        ArrayList<LamViecBean> ds = new ArrayList<>();

        String sql = "SELECT * FROM lamviec"; // đảm bảo tên bảng đúng
        PreparedStatement cmd = ConnectorDB.getConnection().prepareStatement(sql);
        ResultSet rs = cmd.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            int nhanVienId = rs.getInt("nhanvienid");
            Date ngayLam = rs.getDate("ngaylam");
            int soGio = rs.getInt("sogio");

            ds.add(new LamViecBean(id, nhanVienId, ngayLam, soGio));
        }

        rs.close();
        cmd.close();
        return ds;
    }
}
