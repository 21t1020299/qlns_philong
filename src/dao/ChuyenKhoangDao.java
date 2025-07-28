package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.ChuyenKhoang;
import utils.ConnectorDB;

public class ChuyenKhoangDao {
    public ArrayList<ChuyenKhoang> getList() throws Exception {
        ArrayList<ChuyenKhoang> ds = new ArrayList<>();

        String sql = "SELECT * FROM chuyenkhoang"; // tên bảng trong database
        PreparedStatement cmd = ConnectorDB.getConnection().prepareStatement(sql);
        ResultSet rs = cmd.executeQuery();

        while (rs.next()) {
            int maCK = rs.getInt("maCK"); // đổi tên nếu khác
            String tenCK = rs.getString("tenCK"); // đổi tên nếu khác

            ds.add(new ChuyenKhoang(maCK, tenCK));
        }

        rs.close();
        cmd.close();
        return ds;
    }
}
