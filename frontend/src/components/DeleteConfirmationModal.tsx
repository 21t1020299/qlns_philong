import React, { useState } from 'react';
import './DeleteConfirmationModal.css';

interface DeleteConfirmationModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: (confirmation: string) => void;
  employeeId: string;
  employeeName: string;
}

const DeleteConfirmationModal: React.FC<DeleteConfirmationModalProps> = ({
  isOpen,
  onClose,
  onConfirm,
  employeeId,
  employeeName
}) => {
  const [confirmationText, setConfirmationText] = useState('');
  const [error, setError] = useState('');

  const handleConfirm = () => {
    if (confirmationText !== 'TÔI HIỂU') {
      setError('Từ khóa xác nhận không đúng. Vui lòng nhập chính xác "TÔI HIỂU"');
      return;
    }
    onConfirm(confirmationText);
    setConfirmationText('');
    setError('');
  };

  const handleClose = () => {
    setConfirmationText('');
    setError('');
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="modal-header">
          <div className="modal-icon">🚨</div>
          <h2 className="modal-title">Xác nhận xóa nhân viên</h2>
          <button className="modal-close" onClick={handleClose}>
            ✕
          </button>
        </div>

        {/* Body */}
        <div className="modal-body">
          {/* Employee Info */}
          <div className="employee-info">
            <div className="info-section">
              <h3>📋 Thông tin nhân viên</h3>
              <div className="info-grid">
                <div className="info-item">
                  <span className="info-label">Mã nhân viên:</span>
                  <span className="info-value">{employeeId}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Họ tên:</span>
                  <span className="info-value">{employeeName}</span>
                </div>
              </div>
            </div>
          </div>

          {/* Warning Section */}
          <div className="warning-section">
            <h3>⚠️ Cảnh báo quan trọng</h3>
            <div className="warning-list">
              <div className="warning-item">
                <span className="warning-icon">🗑️</span>
                <span>Xóa vĩnh viễn nhân viên khỏi hệ thống</span>
              </div>
              <div className="warning-item">
                <span className="warning-icon">❌</span>
                <span>Dữ liệu KHÔNG THỂ KHÔI PHỤC</span>
              </div>
            </div>
          </div>

          {/* Confirmation Section */}
          <div className="confirmation-section">
            <h3>🔐 Xác nhận an toàn</h3>
            <p className="confirmation-instruction">
              Để xác nhận, vui lòng nhập chính xác: <strong>"TÔI HIỂU"</strong>
            </p>
            
            <div className="input-group">
              <input
                type="text"
                className={`confirmation-input ${error ? 'error' : ''}`}
                placeholder="Nhập 'TÔI HIỂU' để xác nhận"
                value={confirmationText}
                onChange={(e) => {
                  setConfirmationText(e.target.value);
                  if (error) setError('');
                }}
                onKeyPress={(e) => {
                  if (e.key === 'Enter') {
                    handleConfirm();
                  }
                }}
              />
              {error && <div className="error-message">{error}</div>}
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={handleClose}>
            ❌ Hủy bỏ
          </button>
          <button 
            className={`btn btn-danger ${confirmationText === 'TÔI HIỂU' ? 'active' : 'disabled'}`}
            onClick={handleConfirm}
            disabled={confirmationText !== 'TÔI HIỂU'}
          >
            🗑️ Xác nhận xóa
          </button>
        </div>
      </div>
    </div>
  );
};

export default DeleteConfirmationModal;
