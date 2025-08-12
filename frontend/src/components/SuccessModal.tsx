import React, { useEffect } from 'react';
import './SuccessModal.css';

interface SuccessModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  message: string;
  employeeInfo?: {
    id: string;
    name: string;
    email: string;
  };
}

const SuccessModal: React.FC<SuccessModalProps> = ({
  isOpen,
  onClose,
  title,
  message,
  employeeInfo
}) => {
  useEffect(() => {
    if (isOpen) {
      // Auto close after 3 seconds
      const timer = setTimeout(() => {
        onClose();
      }, 3000);

      return () => clearTimeout(timer);
    }
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <div className="success-modal-overlay" onClick={onClose}>
      <div className="success-modal-content" onClick={(e) => e.stopPropagation()}>
        {/* Success Icon */}
        <div className="success-icon">
          <div className="checkmark">
            <div className="checkmark-stem"></div>
            <div className="checkmark-kick"></div>
          </div>
        </div>

        {/* Title */}
        <h2 className="success-title">{title}</h2>

        {/* Message */}
        <p className="success-message">{message}</p>

        {/* Employee Info (if provided) */}
        {employeeInfo && (
          <div className="employee-info-card">
            <div className="info-item">
              <span className="info-label">👤 Mã nhân viên:</span>
              <span className="info-value">{employeeInfo.id}</span>
            </div>
            <div className="info-item">
              <span className="info-label">📝 Họ tên:</span>
              <span className="info-value">{employeeInfo.name}</span>
            </div>
            <div className="info-item">
              <span className="info-label">📧 Email:</span>
              <span className="info-value">{employeeInfo.email}</span>
            </div>
          </div>
        )}

        {/* Action Buttons */}
        <div className="success-modal-footer">
          <button className="btn btn-success" onClick={onClose}>
            ✅ Hoàn tất
          </button>
        </div>

        {/* Auto-close indicator */}
        <div className="auto-close-indicator">
          <div className="progress-bar">
            <div className="progress-fill"></div>
          </div>
          <span className="auto-close-text">Tự động đóng sau 3 giây</span>
        </div>
      </div>
    </div>
  );
};

export default SuccessModal;
