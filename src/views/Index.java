package views;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.BorderLayout;

import bean.NhanVienBean;

public class Index {

    private JFrame frmQlnsPhiLong;
    private NhanVien addNhanVienJPanel;
    private DanhSachHoSo danhSachHoSoJPanel;
    private JPanel contentPanel;

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
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setPanelVisible(JPanel panel) {
        removeAllPanel();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
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
        frmQlnsPhiLong.getContentPane().setLayout(new BorderLayout());

        // Content panel để chứa các view
        contentPanel = new JPanel(new BorderLayout());
        frmQlnsPhiLong.getContentPane().add(contentPanel, BorderLayout.CENTER);

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
                System.out.println("Click Thêm Hồ Sơ");
                route(addNhanVienJPanel);
            }
        });
        menuHoSo.add(menuThem);

        JMenu menuDanhSach = new JMenu("Danh Sách Hồ Sơ");
        menuDanhSach.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Click Danh Sách Hồ Sơ");
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

        // Khởi tạo các panel
        danhSachHoSoJPanel = new DanhSachHoSo();
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
        addNhanVienJPanel.addCustomRouterListener(new RouterListener() {
            @Override
            public void routeTo(String path) {
                if (path.equals("edit")) route(addNhanVienJPanel);
                if (path.equals("dsnv")) {
                    route(danhSachHoSoJPanel);
                    danhSachHoSoJPanel.refreshData(); // Refresh danh sách
                }
            }

            @Override
            public void routeWithNhanVien(String path, NhanVienBean nhanvien) {
                if (path.equals("edit")) {
                    route(addNhanVienJPanel);
                    addNhanVienJPanel.setNhanVien(nhanvien);
                }
            }
        });

        // Hiển thị danh sách hồ sơ mặc định
        setPanelVisible(danhSachHoSoJPanel);
        
        // Hiển thị cửa sổ
        frmQlnsPhiLong.setVisible(true);
    }
}
