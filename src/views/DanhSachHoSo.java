package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import bean.NhanVienBean;
import bo.NhanVienBo;

public class DanhSachHoSo extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnXoa, btnSua, btnTaiLai;
    private RouterListener routerListener;

    public DanhSachHoSo() {
        setLayout(new BorderLayout());

        // Tiêu đề bảng
        String[] columnNames = {"Mã NV", "Họ tên", "Giới tính", "Ngày sinh", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel button
        JPanel panelButtons = new JPanel();
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnTaiLai = new JButton("Tải lại");

        panelButtons.add(btnThem);
        panelButtons.add(btnXoa);
        panelButtons.add(btnSua);
        panelButtons.add(btnTaiLai);
        add(panelButtons, BorderLayout.SOUTH);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện
        btnTaiLai.addActionListener(e -> loadData());

        btnThem.addActionListener(e -> {
            if (routerListener != null) {
                routerListener.routeTo("edit");
            }
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn dòng để xóa.");
                return;
            }
            String maNV = table.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa nhân viên " + maNV + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int res = NhanVienBo.XoaNhanVien(maNV);
                    if (res > 0) {
                        JOptionPane.showMessageDialog(this, "Đã xóa.");
                        loadData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa.");
                }
            }
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn dòng để sửa.");
                return;
            }
            String maNV = table.getValueAt(row, 0).toString();
            try {
                ArrayList<NhanVienBean> list = NhanVienBo.getAllNhanVien();
                for (NhanVienBean nv : list) {
                    if (nv.getManv().equals(maNV)) {
                        if (routerListener != null) {
                            routerListener.routeWithNhanVien("edit", nv);
                        }
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void addCustomRouterListener(RouterListener listener) {
        this.routerListener = listener;
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            ArrayList<NhanVienBean> list = NhanVienBo.getAllNhanVien();
            for (NhanVienBean nv : list) {
                tableModel.addRow(new Object[]{
                        nv.getManv(),
                        nv.getTennv(),
                        nv.getGtinh(),
                        nv.getNgsinh(),
                        nv.getEmail()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu.");
        }
    }
}
