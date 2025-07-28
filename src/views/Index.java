package views;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;

import bean.NhanVienBean;

public class Index {

    private JFrame frmQlnsPhiLong;
    private NhanVien addNhanVienJPanel;
    private DanhSachHoSo danhSachHoSoJPanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Index window = new Index();
                window.frmQlnsPhiLong.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Index() {
        initialize();
    }

    private void removeAllPanel() {
        frmQlnsPhiLong.getContentPane().removeAll();
        frmQlnsPhiLong.repaint();
    }

    private void setPanelVisible(JPanel panel) {
        frmQlnsPhiLong.getContentPane().add(panel);
        frmQlnsPhiLong.revalidate();
        frmQlnsPhiLong.repaint();
    }

    private void route(JPanel panel) {
        removeAllPanel();
        setPanelVisible(panel);
    }

    private void initialize() {
        int windowHeight = 750;
        int windowWidth = 1000;
        frmQlnsPhiLong = new JFrame();
        frmQlnsPhiLong.setBackground(Color.BLUE);
        frmQlnsPhiLong.setTitle("QLNS Phi Long v1.0");
        frmQlnsPhiLong.setBounds(200, 50, windowWidth, windowHeight);
        frmQlnsPhiLong.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmQlnsPhiLong.getContentPane().setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBounds(0, 0, windowWidth, 23);
        frmQlnsPhiLong.setJMenuBar(menuBar);

        JMenu menuHoSo = new JMenu("Hồ Sơ Nhân Viên");
        menuBar.add(menuHoSo);

        JMenu menuThem = new JMenu("Thêm Hồ Sơ");
        menuThem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                route(addNhanVienJPanel);
            }
        });
        menuHoSo.add(menuThem);

        JMenu menuDanhSach = new JMenu("Danh Sách Hồ Sơ");
        menuDanhSach.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                route(danhSachHoSoJPanel);
            }
        });
        menuHoSo.add(menuDanhSach);

        menuBar.add(new JMenu("Hoạt động làm việc"));

        JMenu menuLuong = new JMenu("Lương");
        menuLuong.add(new JMenuItem("Tính Lương"));
        menuLuong.add(new JMenuItem("Lịch Sử Trả Lương"));
        menuBar.add(menuLuong);

        JMenu menuDanhMuc = new JMenu("Danh Mục");
        menuDanhMuc.add(new JMenuItem("Phòng Ban"));
        menuDanhMuc.add(new JMenuItem("Chức Vụ"));
        menuBar.add(menuDanhMuc);

        JMenu menuThongKe = new JMenu("Thống Kê Báo Cáo");
        menuThongKe.add(new JMenuItem("Nhân Sự"));
        menuThongKe.add(new JMenuItem("Thống kê làm việc"));
        menuBar.add(menuThongKe);

        danhSachHoSoJPanel = new DanhSachHoSo();
        danhSachHoSoJPanel.setBounds(0, 24, windowWidth, windowHeight - 24);
        danhSachHoSoJPanel.setVisible(true);
        danhSachHoSoJPanel.addCustomRouterListener(new RouterListener() {
            @Override
            public void routeTo(String path) {
                if (path.equals("edit")) route(addNhanVienJPanel);
            }

            @Override
            public void routeWithNhanVien(String path, NhanVienBean nhanvien) {
                if (path.equals("edit")) {
                    route(addNhanVienJPanel);
                    addNhanVienJPanel.setNhanVien(nhanvien);
                }
            }
        });

        addNhanVienJPanel = new NhanVien();
        addNhanVienJPanel.setBounds(0, 24, windowWidth, windowHeight - 24);
        addNhanVienJPanel.setVisible(false);
        addNhanVienJPanel.addCustomRouterListener(new RouterListener() {
            @Override
            public void routeTo(String path) {
                if (path.equals("edit")) route(addNhanVienJPanel);
                if (path.equals("dsnv")) route(danhSachHoSoJPanel);
            }

            @Override
            public void routeWithNhanVien(String path, NhanVienBean nhanvien) {
                if (path.equals("edit")) {
                    route(addNhanVienJPanel);
                    addNhanVienJPanel.setNhanVien(nhanvien);
                }
            }
        });

        frmQlnsPhiLong.getContentPane().add(danhSachHoSoJPanel);
    }
}
