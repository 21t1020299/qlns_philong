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
    private NhanVienBo nvBo = new NhanVienBo();

    public DanhSachHoSo() {
        setLayout(new BorderLayout());

        // Tiêu đề bảng với đầy đủ 16 trường thông tin
        String[] columnNames = {
            "Mã NV", "Họ tên", "Giới tính", "Ngày sinh", "Email", "SĐT", 
            "Địa chỉ", "Trình độ", "Chức vụ", "Dân tộc", "Quốc tịch",
            "Nơi ĐKKT", "Địa chỉ thường trú", "Sức khỏe", "Họ tên cha", "Họ tên mẹ"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Tự động điều chỉnh độ rộng cột
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // Mã NV
        table.getColumnModel().getColumn(1).setPreferredWidth(120);  // Họ tên
        table.getColumnModel().getColumn(2).setPreferredWidth(60);   // Giới tính
        table.getColumnModel().getColumn(3).setPreferredWidth(80);   // Ngày sinh
        table.getColumnModel().getColumn(4).setPreferredWidth(180);  // Email
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // SĐT
        table.getColumnModel().getColumn(6).setPreferredWidth(200);  // Địa chỉ
        table.getColumnModel().getColumn(7).setPreferredWidth(80);   // Trình độ
        table.getColumnModel().getColumn(8).setPreferredWidth(80);   // Chức vụ
        table.getColumnModel().getColumn(9).setPreferredWidth(60);   // Dân tộc
        table.getColumnModel().getColumn(10).setPreferredWidth(80);  // Quốc tịch
        table.getColumnModel().getColumn(11).setPreferredWidth(100); // Nơi ĐKKT
        table.getColumnModel().getColumn(12).setPreferredWidth(200); // Địa chỉ thường trú
        table.getColumnModel().getColumn(13).setPreferredWidth(80);  // Sức khỏe
        table.getColumnModel().getColumn(14).setPreferredWidth(120); // Họ tên cha
        table.getColumnModel().getColumn(15).setPreferredWidth(120); // Họ tên mẹ

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
                    int res = nvBo.XoaNhanVien(maNV);
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
                ArrayList<NhanVienBean> list = nvBo.getAllNhanVien();
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

    // Method để refresh dữ liệu từ bên ngoài
    public void refreshData() {
        loadData();
    }

    private void loadData() {
        try {
            System.out.println("=== DEBUG LOAD DATA ===");
            tableModel.setRowCount(0);
            System.out.println("Đã clear table model");
            
            ArrayList<NhanVienBean> list = nvBo.getAllNhanVien();
            System.out.println("Đã lấy được " + list.size() + " nhân viên từ database");
            
            for (NhanVienBean nv : list) {
                System.out.println("Đang thêm: " + nv.getManv() + " - " + nv.getTennv());
                tableModel.addRow(new Object[]{
                        nv.getManv(),
                        nv.getTennv(),
                        nv.getGtinh(),
                        nv.getNgsinh(),
                        nv.getEmail(),
                        nv.getSdt(),
                        nv.getDchi(),
                        nv.getTrinhdo(),
                        nv.getMacv(),
                        nv.getDtoc(),
                        nv.getQtich(),
                        nv.getNoidkhktt(),
                        nv.getDchithuongtru(),
                        nv.getSkhoe(),
                        nv.getHotencha(),
                        nv.getHotenme()
                });
            }
            System.out.println("Hoàn thành load data");
            System.out.println("=====================");
        } catch (Exception e) {
            System.out.println("=== LỖI LOAD DATA ===");
            e.printStackTrace();
            System.out.println("Chi tiết lỗi: " + e.getMessage());
            System.out.println("====================");
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu: " + e.getMessage());
        }
    }
}
