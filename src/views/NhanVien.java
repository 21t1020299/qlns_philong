package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import bean.NhanVienBean;
import bo.NhanVienBo;

public class NhanVien extends JPanel {
    // Các trường thông tin cơ bản
    private JTextField txtMaNV, txtHoTen, txtEmail, txtNgaySinh;
    private JTextField txtDanToc, txtTrinhDo, txtNoiDKKT, txtDiaChi, txtSDT;
    private JTextField txtDiaChiThuongTru, txtQuocTich, txtTinhTrangSK;
    private JTextField txtHoTenCha, txtHoTenMe;
    
    // ComboBox cho chức vụ, giới tính và các trường có dữ liệu sẵn
    private JComboBox<String> cboChucVu, cboGioiTinh, cboDanToc, cboTrinhDo, cboQuocTich, cboSucKhoe, cboNoiDKKT;
    
    // Buttons
    private JButton btnLuu, btnQuayLai;
    
    private NhanVienBean currentNhanVien;
    private NhanVienBo nvBo = new NhanVienBo();
    private RouterListener routerListener;

    public NhanVien() {
        setLayout(new BorderLayout());
        
        // Panel chính
        JPanel mainPanel = createMainPanel();
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Event listeners
        setupEventListeners();
        
        // Tự động tạo mã nhân viên mới
        try {
            txtMaNV.setText(nvBo.newMaNhanVien());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        mainPanel.setBorder(BorderFactory.createTitledBorder("THÔNG TIN NHÂN VIÊN"));
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("PHIẾU LẬP HỒ SƠ NHÂN VIÊN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.BLUE);
        mainPanel.add(lblTitle);
        
        // Panel thông tin
        JPanel infoPanel = new JPanel(new GridLayout(8, 4, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Dòng 1
        infoPanel.add(new JLabel("Mã nhân viên:"));
        txtMaNV = new JTextField(15);
        txtMaNV.setEditable(false);
        txtMaNV.setToolTipText("Mã nhân viên tự động tạo");
        infoPanel.add(txtMaNV);
        
        infoPanel.add(new JLabel("Họ tên:"));
        txtHoTen = new JTextField(15);
        txtHoTen.setToolTipText("Chỉ nhập chữ cái và dấu tiếng Việt, không chứa số");
        infoPanel.add(txtHoTen);
        
        // Dòng 2
        infoPanel.add(new JLabel("Ngày sinh:"));
        txtNgaySinh = new JTextField(15);
        txtNgaySinh.setToolTipText("Định dạng: YYYY-MM-DD (Ví dụ: 1990-05-15). Tuổi từ 18-55");
        infoPanel.add(txtNgaySinh);
        
        infoPanel.add(new JLabel("Giới tính:"));
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setToolTipText("Chọn giới tính");
        infoPanel.add(cboGioiTinh);
        
        // Dòng 3
        infoPanel.add(new JLabel("Dân tộc:"));
        cboDanToc = new JComboBox<>(new String[]{"Kinh", "Tày", "Thái", "Mường", "Khmer", "Hoa", "Nùng", "H'Mông", "Dao", "Gia Rai", "Ê Đê", "Ba Na", "Xơ Đăng", "Cơ Ho", "Chăm", "Khác"});
        cboDanToc.setToolTipText("Chọn dân tộc");
        infoPanel.add(cboDanToc);
        
        infoPanel.add(new JLabel("Quốc tịch:"));
        cboQuocTich = new JComboBox<>(new String[]{"Việt Nam", "Hoa Kỳ", "Trung Quốc", "Nhật Bản", "Hàn Quốc", "Singapore", "Thái Lan", "Malaysia", "Philippines", "Indonesia", "Campuchia", "Lào", "Myanmar", "Ấn Độ", "Pháp", "Đức", "Anh", "Úc", "Canada", "Khác"});
        cboQuocTich.setToolTipText("Chọn quốc tịch");
        infoPanel.add(cboQuocTich);
        
        // Dòng 4
        infoPanel.add(new JLabel("Trình độ học vấn:"));
        cboTrinhDo = new JComboBox<>(new String[]{"Tiểu học", "THCS", "THPT", "Trung cấp", "Cao đẳng", "Đại học", "Thạc sĩ", "Tiến sĩ"});
        cboTrinhDo.setToolTipText("Chọn trình độ học vấn");
        infoPanel.add(cboTrinhDo);
        
        infoPanel.add(new JLabel("Tình trạng sức khỏe:"));
        cboSucKhoe = new JComboBox<>(new String[]{"Tốt", "Khá", "Trung bình", "Yếu"});
        cboSucKhoe.setToolTipText("Chọn tình trạng sức khỏe");
        infoPanel.add(cboSucKhoe);
        
        // Dòng 5
        infoPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(15);
        txtEmail.setToolTipText("Định dạng: example@domain.com");
        infoPanel.add(txtEmail);
        
        infoPanel.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField(15);
        txtSDT.setToolTipText("10 số, bắt đầu bằng 0 (Ví dụ: 0123456789)");
        infoPanel.add(txtSDT);
        
        // Dòng 6
        infoPanel.add(new JLabel("Địa chỉ thường trú:"));
        txtDiaChiThuongTru = new JTextField(15);
        txtDiaChiThuongTru.setToolTipText("Địa chỉ thường trú theo CMND");
        infoPanel.add(txtDiaChiThuongTru);
        
        infoPanel.add(new JLabel("Nơi đăng ký HKTT:"));
        cboNoiDKKT = new JComboBox<>(new String[]{
            "Hà Nội", "TP.HCM", "Đà Nẵng", "Hải Phòng", "Cần Thơ", "Huế", "Nha Trang", "Đà Lạt", "Vũng Tàu", "Biên Hòa", 
            "Thủ Dầu Một", "Rạch Giá", "Long Xuyên", "Cà Mau", "Sóc Trăng", "Bạc Liêu", "Cao Lãnh", "Sa Đéc", "Mỹ Tho", "Tân An",
            "Bến Tre", "Trà Vinh", "Vĩnh Long", "Cần Thơ", "An Giang", "Kiên Giang", "Bạc Liêu", "Cà Mau", "Sóc Trăng", "Bến Tre",
            "Trà Vinh", "Vĩnh Long", "Đồng Tháp", "Tiền Giang", "Long An", "Tây Ninh", "Bình Dương", "Bình Phước", "Đồng Nai", "Bà Rịa - Vũng Tàu",
            "Bình Thuận", "Ninh Thuận", "Khánh Hòa", "Phú Yên", "Bình Định", "Quảng Ngãi", "Quảng Nam", "Đà Nẵng", "Thừa Thiên Huế", "Quảng Trị",
            "Quảng Bình", "Hà Tĩnh", "Nghệ An", "Thanh Hóa", "Ninh Bình", "Nam Định", "Thái Bình", "Hải Dương", "Hải Phòng", "Quảng Ninh",
            "Lạng Sơn", "Cao Bằng", "Hà Giang", "Tuyên Quang", "Thái Nguyên", "Bắc Kạn", "Lào Cai", "Yên Bái", "Phú Thọ", "Vĩnh Phúc",
            "Bắc Ninh", "Bắc Giang", "Hưng Yên", "Hà Nam", "Hà Nội", "Hòa Bình", "Sơn La", "Điện Biên", "Lai Châu"
        });
        cboNoiDKKT.setToolTipText("Chọn nơi đăng ký hộ khẩu thường trú");
        infoPanel.add(cboNoiDKKT);
        
        // Dòng 7
        infoPanel.add(new JLabel("Địa chỉ hiện tại:"));
        txtDiaChi = new JTextField(15);
        txtDiaChi.setToolTipText("Địa chỉ hiện tại đang sinh sống");
        infoPanel.add(txtDiaChi);
        
        infoPanel.add(new JLabel("Chức vụ:"));
        cboChucVu = new JComboBox<>(new String[]{"CV001 - Nhân viên", "CV002 - Trưởng nhóm", "CV003 - Quản lý", "CV004 - Giám đốc", "CV005 - Tổng giám đốc"});
        cboChucVu.setToolTipText("Chọn chức vụ");
        infoPanel.add(cboChucVu);
        
        // Dòng 8
        infoPanel.add(new JLabel("Họ tên cha:"));
        txtHoTenCha = new JTextField(15);
        txtHoTenCha.setToolTipText("Chỉ nhập chữ cái và dấu tiếng Việt");
        infoPanel.add(txtHoTenCha);
        
        infoPanel.add(new JLabel("Họ tên mẹ:"));
        txtHoTenMe = new JTextField(15);
        txtHoTenMe.setToolTipText("Chỉ nhập chữ cái và dấu tiếng Việt");
        infoPanel.add(txtHoTenMe);
        
        mainPanel.add(infoPanel);
        
        return mainPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnLuu = new JButton("LUU");
        btnQuayLai = new JButton("QUAY LAI");
        
        // Button cơ bản
        btnLuu.setSize(100, 30);
        btnQuayLai.setSize(100, 30);
        
        panel.add(btnLuu);
        panel.add(btnQuayLai);
        
        return panel;
    }
    
    private void setupEventListeners() {
        btnLuu.addActionListener(e -> saveNhanVien());
        btnQuayLai.addActionListener(e -> {
            if (routerListener != null) {
                routerListener.routeTo("dsnv");
            }
        });
    }
    
    private void saveNhanVien() {
        try {
            // Validate dữ liệu
            if (!validateData()) {
                return;
            }
            
            String maNV = txtMaNV.getText();
            String hoTen = txtHoTen.getText();
            String email = txtEmail.getText();
            String gioiTinh = cboGioiTinh.getSelectedItem().toString();
            String ngaySinh = txtNgaySinh.getText();
            String danToc = cboDanToc.getSelectedItem().toString();
            String trinhDo = cboTrinhDo.getSelectedItem().toString();
            String noiDKKT = cboNoiDKKT.getSelectedItem().toString();
            String diaChi = txtDiaChi.getText();
            String sdt = txtSDT.getText();
            String diaChiThuongTru = txtDiaChiThuongTru.getText();
            String quocTich = cboQuocTich.getSelectedItem().toString();
            String tinhTrangSK = cboSucKhoe.getSelectedItem().toString();
            String hoTenCha = txtHoTenCha.getText();
            String hoTenMe = txtHoTenMe.getText();
            
            // Lấy mã chức vụ từ combobox
            String chucVu = cboChucVu.getSelectedItem().toString().split(" - ")[0];
            
            if (currentNhanVien == null) {
                // Thêm mới
                if (maNV.isEmpty()) {
                    maNV = nvBo.newMaNhanVien();
                }
                
                NhanVienBean nv = new NhanVienBean(
                    maNV, chucVu, email, hoTen, gioiTinh, ngaySinh,
                    danToc, trinhDo, noiDKKT, diaChi, sdt,
                    diaChiThuongTru, quocTich, tinhTrangSK,
                    hoTenCha, hoTenMe
                );
                
                nvBo.themNhanVien(nv);
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset form sau khi thêm thành công
                resetForm();
                
            } else {
                // Cập nhật
                currentNhanVien.setTennv(hoTen);
                currentNhanVien.setEmail(email);
                currentNhanVien.setGtinh(gioiTinh);
                currentNhanVien.setNgsinh(ngaySinh);
                currentNhanVien.setDtoc(danToc);
                currentNhanVien.setTrinhdo(trinhDo);
                currentNhanVien.setNoidkhktt(noiDKKT);
                currentNhanVien.setDchi(diaChi);
                currentNhanVien.setSdt(sdt);
                currentNhanVien.setDchithuongtru(diaChiThuongTru);
                currentNhanVien.setQtich(quocTich);
                currentNhanVien.setSkhoe(tinhTrangSK);
                currentNhanVien.setHotencha(hoTenCha);
                currentNhanVien.setHotenme(hoTenMe);
                currentNhanVien.setMacv(chucVu);
                
                nvBo.capNhatNhanVien(currentNhanVien);
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset form sau khi cập nhật thành công
                resetForm();
            }
            
            // Quay lại danh sách để cập nhật
            if (routerListener != null) {
                routerListener.routeTo("dsnv");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateData() {
        // Validate họ tên - chấp nhận tên tiếng Việt có dấu
        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Chấp nhận chữ cái, dấu tiếng Việt, khoảng trắng, dấu gạch ngang
        if (!hoTen.matches("^[\\p{L}\\s\\-]+$")) {
            JOptionPane.showMessageDialog(this, "Họ tên chỉ được chứa chữ cái, dấu tiếng Việt và khoảng trắng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }

        // Validate ngày sinh
        String ngaySinh = txtNgaySinh.getText().trim();
        if (ngaySinh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidDate(ngaySinh)) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ! Vui lòng nhập theo định dạng YYYY-MM-DD", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNgaySinh.requestFocus();
            return false;
        }
        if (!isValidAge(ngaySinh)) {
            // Thông báo lỗi sẽ được hiển thị trong isValidAge()
            txtNgaySinh.requestFocus();
            return false;
        }

        // Validate email
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ! Vui lòng kiểm tra lại định dạng email.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        // Validate số điện thoại
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidPhone(sdt)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! Phải có 10 số và bắt đầu bằng số 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }

        // Validate địa chỉ hiện tại
        String diaChi = txtDiaChi.getText().trim();
        if (diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (diaChi.length() > 200) {
            JOptionPane.showMessageDialog(this, "Địa chỉ hiện tại không được quá 200 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }

        // Validate địa chỉ thường trú
        String diaChiThuongTru = txtDiaChiThuongTru.getText().trim();
        if (diaChiThuongTru.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ thường trú!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (diaChiThuongTru.length() > 200) {
            JOptionPane.showMessageDialog(this, "Địa chỉ thường trú không được quá 200 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaChiThuongTru.requestFocus();
            return false;
        }

        // Validate họ tên cha
        String hoTenCha = txtHoTenCha.getText().trim();
        if (hoTenCha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên cha!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!hoTenCha.matches("^[\\p{L}\\s\\-]+$")) {
            JOptionPane.showMessageDialog(this, "Họ tên cha chỉ được chứa chữ cái, dấu tiếng Việt và khoảng trắng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTenCha.requestFocus();
            return false;
        }
        if (hoTenCha.length() > 100) {
            JOptionPane.showMessageDialog(this, "Họ tên cha không được quá 100 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTenCha.requestFocus();
            return false;
        }

        // Validate họ tên mẹ
        String hoTenMe = txtHoTenMe.getText().trim();
        if (hoTenMe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên mẹ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!hoTenMe.matches("^[\\p{L}\\s\\-]+$")) {
            JOptionPane.showMessageDialog(this, "Họ tên mẹ chỉ được chứa chữ cái, dấu tiếng Việt và khoảng trắng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTenMe.requestFocus();
            return false;
        }
        if (hoTenMe.length() > 100) {
            JOptionPane.showMessageDialog(this, "Họ tên mẹ không được quá 100 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTenMe.requestFocus();
            return false;
        }

        return true;
    }

    // Kiểm tra định dạng ngày tháng
    private boolean isValidDate(String dateStr) {
        try {
            java.time.LocalDate.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Kiểm tra tuổi từ 18-55
    private boolean isValidAge(String dateStr) {
        try {
            java.time.LocalDate birthDate = java.time.LocalDate.parse(dateStr);
            java.time.LocalDate currentDate = java.time.LocalDate.now();
            
            // Tính tuổi chính xác
            int age = currentDate.getYear() - birthDate.getYear();
            
            // Kiểm tra nếu chưa đến sinh nhật trong năm nay
            if (birthDate.plusYears(age).isAfter(currentDate)) {
                age--;
            }
            
            System.out.println("=== DEBUG TUỔI ===");
            System.out.println("Ngày sinh: " + birthDate);
            System.out.println("Ngày hiện tại: " + currentDate);
            System.out.println("Tuổi tính được: " + age);
            System.out.println("==================");
            
            if (age < 18) {
                JOptionPane.showMessageDialog(this, "Tuổi phải từ 18 trở lên! (Hiện tại: " + age + " tuổi)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (age >= 55) {
                JOptionPane.showMessageDialog(this, "Tuổi phải dưới 55! (Hiện tại: " + age + " tuổi)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi tính tuổi: " + e.getMessage());
            return false;
        }
    }

    // Kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        // Kiểm tra có đúng 1 ký tự @
        if (email.indexOf('@') == -1 || email.indexOf('@') != email.lastIndexOf('@')) {
            return false;
        }
        
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        
        String localPart = parts[0];
        String domain = parts[1];
        
        // Kiểm tra local part không rỗng và chỉ chứa A-Z, 0-9, _, ., -
        if (localPart.isEmpty() || !localPart.matches("^[A-Za-z0-9._-]+$")) {
            return false;
        }
        
        // Kiểm tra local part không bắt đầu hoặc kết thúc bằng dấu chấm hoặc dấu gạch ngang
        if (localPart.startsWith(".") || localPart.endsWith(".") || 
            localPart.startsWith("-") || localPart.endsWith("-")) {
            return false;
        }
        
        // Kiểm tra domain không rỗng và có ít nhất 1 dấu chấm
        if (domain.isEmpty() || !domain.contains(".")) {
            return false;
        }
        
        // Kiểm tra domain có định dạng hợp lệ (ít nhất 2 phần: tên miền + TLD)
        String[] domainParts = domain.split("\\.");
        if (domainParts.length < 2) {
            return false;
        }
        
        // Kiểm tra mỗi phần domain chỉ chứa chữ cái, số, dấu gạch ngang
        for (String part : domainParts) {
            if (part.isEmpty() || !part.matches("^[A-Za-z0-9-]+$")) {
                return false;
            }
        }
        
        // Kiểm tra TLD (phần cuối) có ít nhất 2 ký tự
        String tld = domainParts[domainParts.length - 1];
        if (tld.length() < 2) {
            return false;
        }
        
        return true;
    }

    // Kiểm tra số điện thoại
    private boolean isValidPhone(String phone) {
        // Bắt đầu bằng 0, có 10 số, chỉ chứa số
        return phone.matches("^0[0-9]{9}$");
    }

    public void addCustomRouterListener(RouterListener listener) {
        this.routerListener = listener;
    }

    public void setNhanVien(NhanVienBean nv) {
        this.currentNhanVien = nv;
        
        // Tự động tạo mã nhân viên mới nếu là thêm mới
        if (nv == null) {
            try {
                txtMaNV.setText(nvBo.newMaNhanVien());
            } catch (Exception e) {
                e.printStackTrace();
            }
            resetForm();
            return;
        }
        
        // Điền thông tin vào form
        txtMaNV.setText(nv.getManv());
        txtHoTen.setText(nv.getTennv());
        txtEmail.setText(nv.getEmail());
        // Set giới tính
        String gtinh = nv.getGtinh();
        if (gtinh != null && !gtinh.isEmpty()) {
            if (gtinh.equals("Nam")) {
                cboGioiTinh.setSelectedIndex(0);
            } else if (gtinh.equals("Nữ")) {
                cboGioiTinh.setSelectedIndex(1);
            }
        }
        txtNgaySinh.setText(nv.getNgsinh());
        // Set dân tộc
        String dtoc = nv.getDtoc();
        if (dtoc != null && !dtoc.isEmpty()) {
            for (int i = 0; i < cboDanToc.getItemCount(); i++) {
                if (cboDanToc.getItemAt(i).equals(dtoc)) {
                    cboDanToc.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Set quốc tịch
        String qtich = nv.getQtich();
        if (qtich != null && !qtich.isEmpty()) {
            for (int i = 0; i < cboQuocTich.getItemCount(); i++) {
                if (cboQuocTich.getItemAt(i).equals(qtich)) {
                    cboQuocTich.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Set trình độ học vấn
        String trinhdo = nv.getTrinhdo();
        if (trinhdo != null && !trinhdo.isEmpty()) {
            for (int i = 0; i < cboTrinhDo.getItemCount(); i++) {
                if (cboTrinhDo.getItemAt(i).equals(trinhdo)) {
                    cboTrinhDo.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Set tình trạng sức khỏe
        String skhoe = nv.getSkhoe();
        if (skhoe != null && !skhoe.isEmpty()) {
            for (int i = 0; i < cboSucKhoe.getItemCount(); i++) {
                if (cboSucKhoe.getItemAt(i).equals(skhoe)) {
                    cboSucKhoe.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Set nơi đăng ký HKTT
        String noidkhktt = nv.getNoidkhktt();
        if (noidkhktt != null && !noidkhktt.isEmpty()) {
            for (int i = 0; i < cboNoiDKKT.getItemCount(); i++) {
                if (cboNoiDKKT.getItemAt(i).equals(noidkhktt)) {
                    cboNoiDKKT.setSelectedIndex(i);
                    break;
                }
            }
        }
        txtDiaChi.setText(nv.getDchi());
        txtSDT.setText(nv.getSdt());
        txtDiaChiThuongTru.setText(nv.getDchithuongtru());
        txtHoTenCha.setText(nv.getHotencha());
        txtHoTenMe.setText(nv.getHotenme());
        
        // Set chức vụ
        String macv = nv.getMacv();
        if (macv != null && !macv.isEmpty()) {
            for (int i = 0; i < cboChucVu.getItemCount(); i++) {
                if (cboChucVu.getItemAt(i).startsWith(macv)) {
                    cboChucVu.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void resetForm() {
        txtHoTen.setText("");
        txtEmail.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtNgaySinh.setText("");
        cboDanToc.setSelectedIndex(0);
        cboQuocTich.setSelectedIndex(0);
        cboTrinhDo.setSelectedIndex(0);
        cboSucKhoe.setSelectedIndex(0);
        txtSDT.setText("");
        txtDiaChiThuongTru.setText("");
        txtDiaChi.setText("");
        cboNoiDKKT.setSelectedIndex(0);
        cboChucVu.setSelectedIndex(0);
        txtHoTenCha.setText("");
        txtHoTenMe.setText("");
    }
}
