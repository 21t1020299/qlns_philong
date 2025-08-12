import React from 'react';
import { Employee } from '../types/employee';
import './EmployeeDetailModal.css';

interface EmployeeDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  employee: Employee | null;
}

const EmployeeDetailModal: React.FC<EmployeeDetailModalProps> = ({
  isOpen,
  onClose,
  employee
}) => {
  if (!isOpen || !employee) return null;

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN');
  };

  return (
    <div className="detail-modal-overlay" onClick={onClose}>
      <div className="detail-modal-content" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="detail-modal-header">
          <div className="detail-modal-icon">👤</div>
          <h2 className="detail-modal-title">Chi tiết nhân viên</h2>
          <button className="detail-modal-close" onClick={onClose}>
            ✕
          </button>
        </div>

        {/* Body */}
        <div className="detail-modal-body">
          {/* Employee Avatar & Basic Info */}
          <div className="employee-avatar-section">
            <div className="employee-avatar">
              {employee.anhchandung ? (
                <img src={employee.anhchandung} alt="Ảnh chân dung" />
              ) : (
                <div className="avatar-placeholder">
                  {employee.tennv.split(' ').map(n => n[0]).join('').toUpperCase()}
                </div>
              )}
            </div>
            <div className="employee-basic-info">
              <h3 className="employee-name">{employee.tennv}</h3>
              <p className="employee-id">{employee.manv}</p>
              <span className={`employee-gender ${employee.gtinh === 'Nam' ? 'male' : 'female'}`}>
                {employee.gtinh === 'Nam' ? '👨' : '👩'} {employee.gtinh}
              </span>
            </div>
          </div>

          {/* Contact Information */}
          <div className="detail-section">
            <h3 className="section-title">📞 Thông tin liên lạc</h3>
            <div className="detail-grid">
              <div className="detail-item">
                <span className="detail-label">📧 Email:</span>
                <span className="detail-value">{employee.email}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">📱 Số điện thoại:</span>
                <span className="detail-value">{employee.sdt}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">🎂 Ngày sinh:</span>
                <span className="detail-value">{formatDate(employee.ngsinh)}</span>
              </div>
            </div>
          </div>

          {/* Address Information */}
          <div className="detail-section">
            <h3 className="section-title">🏠 Thông tin địa chỉ</h3>
            <div className="detail-grid">
              <div className="detail-item full-width">
                <span className="detail-label">📍 Địa chỉ hiện tại:</span>
                <span className="detail-value">{employee.dchi}</span>
              </div>
              <div className="detail-item full-width">
                <span className="detail-label">🏘️ Địa chỉ thường trú:</span>
                <span className="detail-value">{employee.dchithuongtru}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">🏛️ Nơi đăng ký HKTT:</span>
                <span className="detail-value">{employee.noidkhktt}</span>
              </div>
            </div>
          </div>

          {/* Personal Information */}
          <div className="detail-section">
            <h3 className="section-title">👤 Thông tin cá nhân</h3>
            <div className="detail-grid">
              <div className="detail-item">
                <span className="detail-label">👥 Dân tộc:</span>
                <span className="detail-value">{employee.dtoc}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">🎓 Trình độ:</span>
                <span className="detail-value">{employee.trinhdo}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">🌍 Quốc tịch:</span>
                <span className="detail-value">{employee.qtich}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">💪 Sức khỏe:</span>
                <span className="detail-value">{employee.skhoe}</span>
              </div>
            </div>
          </div>

          {/* Work Information */}
          <div className="detail-section">
            <h3 className="section-title">💼 Thông tin công việc</h3>
            <div className="detail-grid">
              <div className="detail-item">
                <span className="detail-label">🏢 Chức vụ:</span>
                <span className="detail-value">{employee.macv}</span>
              </div>
            </div>
          </div>

          {/* Family Information */}
          <div className="detail-section">
            <h3 className="section-title">👨‍👩‍👧‍👦 Thông tin gia đình</h3>
            <div className="detail-grid">
              <div className="detail-item">
                <span className="detail-label">👨 Họ tên cha:</span>
                <span className="detail-value">{employee.hotencha}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">👩 Họ tên mẹ:</span>
                <span className="detail-value">{employee.hotenme}</span>
              </div>
            </div>
          </div>

          {/* Documents */}
          <div className="detail-section">
            <h3 className="section-title">📄 Tài liệu</h3>
            <div className="detail-grid">
              <div className="detail-item">
                <span className="detail-label">🆔 Ảnh CMND:</span>
                <span className="detail-value">
                  {employee.anhcmnd ? (
                    <a href={employee.anhcmnd} target="_blank" rel="noopener noreferrer" className="document-link">
                      Xem ảnh CMND
                    </a>
                  ) : (
                    'Chưa có'
                  )}
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="detail-modal-footer">
          <button className="btn btn-primary" onClick={onClose}>
            ✅ Đóng
          </button>
        </div>
      </div>
    </div>
  );
};

export default EmployeeDetailModal;
