package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.PhongBanBean;
import utils.ConnectorDB;

public class PhongBanDao {
    public ArrayList<PhongBanBean> getList() throws Exception {
        ArrayList<PhongBanBean> ds = new ArrayList<>();
        String sql = "SELECT * FROM phongban";
        PreparedStatement cmd = ConnectorDB.getConnection().prepareStatement(sql);
        ResultSet rs = cmd.executeQuery();
        while (rs.next()) {
            String maPB = rs.getString("mapban");
            String tenPB = rs.getString("tenpb");
            String maTP = rs.getString("matp");

            ds.add(new PhongBanBean(maPB, tenPB, maTP));
        }
        rs.close();
        cmd.close();
        return ds;
    }
}
