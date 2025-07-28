package views;

import javax.swing.*;
import java.awt.event.*;
import bean.NhanVienBean;
import bo.NhanVienBo;

public class NhanVien extends JPanel {
    private JTextField txtHoTen, txtEmail, txtGioiTinh, txtNgaySinh;
    private JButton btnLuu;
    private NhanVienBean currentNhanVien;
    private NhanVienBo nvBo = new NhanVienBo();

    public NhanVien() {
        // add UI setup code...
        btnLuu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String hoTen = txtHoTen.getText();
                    String email = txtEmail.getText();
                    String gioiTinh = txtGioiTinh.getText();
                    String ngaySinh = txtNgaySinh.getText();

                    if (currentNhanVien == null) {
                        String maNV = nvBo.newMaNhanVien();
                        NhanVienBean nv = new NhanVienBean(maNV,  email, hoTen, gioiTinh, ngaySinh,
                                    "", "", "", "", "", "",
                                "", "", "", "", "");
                        nvBo.themNhanVien(nv);
                        JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công!");
                    } else {
                        currentNhanVien.setTennv(hoTen);
                        currentNhanVien.setEmail(email);
                        currentNhanVien.setGioitinh(gioiTinh);
                        currentNhanVien.setNgsinh(ngaySinh);
                        nvBo.capNhatNhanVien(currentNhanVien);
                        JOptionPane.showMessageDialog(null, "Cập nhật nhân viên thành công!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
                }
            }
        });
    }

    public void setNhanVien(NhanVienBean nv) {
        this.currentNhanVien = nv;
        txtHoTen.setText(nv.getTennv());
        txtEmail.setText(nv.getEmail());
        txtGioiTinh.setText(nv.getGioitinh());
        txtNgaySinh.setText(nv.getNgsinh());
    }
}
