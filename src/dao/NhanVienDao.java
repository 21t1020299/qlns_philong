package dao;

import java.sql.*;
import java.util.ArrayList;
import bean.NhanVienBean;
import utils.ConnectorDB;

public class NhanVienDao {
    public ArrayList<NhanVienBean> getAllNhanVien() throws Exception {
        ArrayList<NhanVienBean> ds = new ArrayList<>();
        Connection conn = ConnectorDB.getConnection();
        
        if (conn == null) {
            throw new Exception("Không thể kết nối đến database. Vui lòng kiểm tra kết nối internet và cấu hình database.");
        }
        
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM nhanvien");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ds.add(new NhanVienBean(
                    rs.getString("manv"), rs.getString("macv"), rs.getString("email"), rs.getString("tennv"),
                    rs.getString("gtinh"), rs.getDate("ngsinh").toString(), rs.getString("dtoc"), rs.getString("trinhdo"),
                    rs.getString("noidkhktt"), rs.getString("dchi"), rs.getString("sdt"), rs.getString("dchithuongtru"),
                    rs.getString("qtich"), rs.getString("skhoe"), rs.getString("hotencha"), rs.getString("hotenme")
            ));
        }
        return ds;
    }

    public int Them(NhanVienBean nv) throws Exception {
        Connection conn = ConnectorDB.getConnection();
        
        if (conn == null) {
            throw new Exception("Không thể kết nối đến database. Vui lòng kiểm tra kết nối internet và cấu hình database.");
        }
        
        String sql = "INSERT INTO nhanvien VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // 16 parameters
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nv.getManv());
        ps.setString(2, nv.getTennv());
        ps.setString(3, nv.getGtinh());
        ps.setString(4, nv.getEmail());
        ps.setString(5, nv.getDchi());
        ps.setDate(6, java.sql.Date.valueOf(nv.getNgsinh()));
        ps.setString(7, nv.getQtich());
        ps.setString(8, nv.getSdt());
        ps.setString(9, nv.getTrinhdo());
        ps.setString(10, nv.getNoidkhktt());
        ps.setString(11, nv.getDchithuongtru());
        ps.setString(12, nv.getDtoc());
        ps.setString(13, nv.getSkhoe());
        ps.setString(14, nv.getMacv());
        ps.setString(15, nv.getHotencha());
        ps.setString(16, nv.getHotenme());
        return ps.executeUpdate();
    }

    public int CapNhat(NhanVienBean nv) throws Exception {
        Connection conn = ConnectorDB.getConnection();
        
        if (conn == null) {
            throw new Exception("Không thể kết nối đến database. Vui lòng kiểm tra kết nối internet và cấu hình database.");
        }
        
        String sql = "UPDATE nhanvien SET macv=?, email=?, tennv=?, gtinh=?, ngsinh=?, dtoc=?, trinhdo=?, noidkhktt=?, dchi=?, sdt=?, dchithuongtru=?, qtich=?, skhoe=?, hotencha=?, hotenme=? WHERE manv=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nv.getMacv());
        ps.setString(2, nv.getEmail());
        ps.setString(3, nv.getTennv());
        ps.setString(4, nv.getGtinh());
        ps.setDate(5, java.sql.Date.valueOf(nv.getNgsinh()));
        ps.setString(6, nv.getDtoc());
        ps.setString(7, nv.getTrinhdo());
        ps.setString(8, nv.getNoidkhktt());
        ps.setString(9, nv.getDchi());
        ps.setString(10, nv.getSdt());
        ps.setString(11, nv.getDchithuongtru());
        ps.setString(12, nv.getQtich());
        ps.setString(13, nv.getSkhoe());
        ps.setString(14, nv.getHotencha());
        ps.setString(15, nv.getHotenme());
        ps.setString(16, nv.getManv());
        return ps.executeUpdate();
    }

    public int XoaNhanVien(String maNV) throws Exception {
        Connection conn = ConnectorDB.getConnection();
        
        if (conn == null) {
            throw new Exception("Không thể kết nối đến database. Vui lòng kiểm tra kết nối internet và cấu hình database.");
        }
        
        String sql = "DELETE FROM nhanvien WHERE manv=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maNV);
        return ps.executeUpdate();
    }

    public String getMaNhanVienLast() throws Exception {
        Connection conn = ConnectorDB.getConnection();
        
        if (conn == null) {
            throw new Exception("Không thể kết nối đến database. Vui lòng kiểm tra kết nối internet và cấu hình database.");
        }
        
        PreparedStatement ps = conn.prepareStatement("SELECT manv FROM nhanvien ORDER BY manv DESC LIMIT 1");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String lastMa = rs.getString("manv");
            // Tạo mã mới dựa trên mã cuối cùng
            if (lastMa != null && lastMa.startsWith("NV")) {
                try {
                    int num = Integer.parseInt(lastMa.substring(2)) + 1;
                    return String.format("NV%03d", num);
                } catch (NumberFormatException e) {
                    // Nếu không parse được số, trả về NV001
                    return "NV001";
                }
            }
        }
        // Nếu không có nhân viên nào, trả về NV001
        return "NV001";
    }
}
