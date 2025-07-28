package dao;

import java.sql.*;
import java.util.ArrayList;
import bean.NhanVienBean;
import utils.ConnectorDB;

public class NhanVienDao {
    public static ArrayList<NhanVienBean> getAllNhanVien() throws Exception {
        ArrayList<NhanVienBean> ds = new ArrayList<>();
        Connection conn = ConnectorDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM nhanvien");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ds.add(new NhanVienBean(
                    rs.getString("manv"), rs.getString("macv"), rs.getString("email"), rs.getString("tennv"),
                    rs.getString("gtinh"), rs.getString("ngsinh"), rs.getString("dtoc"), rs.getString("trinhdo"),
                    rs.getString("noidkhktt"), rs.getString("dchi"), rs.getString("dchithuongtru"), rs.getString("qtich"),
                    rs.getString("skhoe"), rs.getString("sdt"), rs.getString("sotknh"),
                    rs.getString("hotencha"), rs.getString("hotenme")
            ));
        }
        return ds;
    }

    public int Them(NhanVienBean nv) throws Exception {
        Connection conn = ConnectorDB.getConnection();
        String sql = "INSERT INTO nhanvien VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nv.getManv());
        ps.setString(2, nv.getMacv());
        ps.setString(3, nv.getEmail());
        ps.setString(4, nv.getTennv());
        ps.setString(5, nv.getGioitinh());
        ps.setString(6, nv.getNgsinh());
        ps.setString(7, nv.getDtoc());
        ps.setString(8, nv.getTrinhdo());
        ps.setString(9, nv.getNoidkhktt());
        ps.setString(10, nv.getDchi());
        ps.setString(11, nv.getDchithuongtru());
        ps.setString(12, nv.getQtich());
        ps.setString(13, nv.getSkhoe());
        ps.setString(14, nv.getSdt());
        ps.setString(15, nv.getSotknh());
        ps.setString(16, nv.getHotencha());
        ps.setString(17, nv.getHotenme());
        return ps.executeUpdate();
    }

    public int CapNhat(NhanVienBean nv) throws Exception {
        Connection conn = ConnectorDB.getConnection();
        String sql = "UPDATE nhanvien SET macv=?, email=?, tennv=?, gtinh=?, ngsinh=?, dtoc=?, trinhdo=?, noidkhktt=?, dchi=?, dchithuongtru=?, qtich=?, skhoe=?, sdt=?, sotknh=?, hotencha=?, hotenme=? WHERE manv=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nv.getMacv());
        ps.setString(2, nv.getEmail());
        ps.setString(3, nv.getTennv());
        ps.setString(4, nv.getGioitinh());
        ps.setString(5, nv.getNgsinh());
        ps.setString(6, nv.getDtoc());
        ps.setString(7, nv.getTrinhdo());
        ps.setString(8, nv.getNoidkhktt());
        ps.setString(9, nv.getDchi());
        ps.setString(10, nv.getDchithuongtru());
        ps.setString(11, nv.getQtich());
        ps.setString(12, nv.getSkhoe());
        ps.setString(13, nv.getSdt());
        ps.setString(14, nv.getSotknh());
        ps.setString(15, nv.getHotencha());
        ps.setString(16, nv.getHotenme());
        ps.setString(17, nv.getManv());
        return ps.executeUpdate();
    }

    public static String getMaNhanVienLast() throws Exception {
        Connection conn = ConnectorDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT manv FROM nhanvien ORDER BY manv DESC LIMIT 1");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getString("manv");
        return null;
    }
}
