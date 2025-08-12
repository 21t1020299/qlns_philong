import React, { useEffect } from 'react';
import './ErrorModal.css';

interface ErrorModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  message: string;
  errorType?: 'validation' | 'network' | 'server' | 'general';
}

const ErrorModal: React.FC<ErrorModalProps> = ({
  isOpen,
  onClose,
  title,
  message,
  errorType = 'general'
}) => {
  useEffect(() => {
    if (isOpen) {
      // Auto close after 5 seconds for errors
      const timer = setTimeout(() => {
        onClose();
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [isOpen, onClose]);

  const getErrorIcon = () => {
    switch (errorType) {
      case 'validation':
        return '⚠️';
      case 'network':
        return '🌐';
      case 'server':
        return '🔧';
      default:
        return '❌';
    }
  };

  const getErrorColor = () => {
    switch (errorType) {
      case 'validation':
        return '#f39c12';
      case 'network':
        return '#3498db';
      case 'server':
        return '#e74c3c';
      default:
        return '#e74c3c';
    }
  };

  if (!isOpen) return null;

  return (
    <div className="error-modal-overlay" onClick={onClose}>
      <div className="error-modal-content" onClick={(e) => e.stopPropagation()}>
        {/* Error Icon */}
        <div className="error-icon" style={{ color: getErrorColor() }}>
          <div className="error-symbol">
            <span className="error-emoji">{getErrorIcon()}</span>
          </div>
        </div>

        {/* Title */}
        <h2 className="error-title">{title}</h2>

        {/* Message */}
        <p className="error-message">{message}</p>

        {/* Error Type Badge */}
        <div className="error-type-badge" style={{ backgroundColor: getErrorColor() }}>
          {errorType === 'validation' && 'Lỗi Validation'}
          {errorType === 'network' && 'Lỗi Kết nối'}
          {errorType === 'server' && 'Lỗi Server'}
          {errorType === 'general' && 'Lỗi Hệ thống'}
        </div>

        {/* Action Buttons */}
        <div className="error-modal-footer">
          <button className="btn btn-error" onClick={onClose}>
            🔄 Thử lại
          </button>
          <button className="btn btn-secondary" onClick={onClose}>
            ✕ Đóng
          </button>
        </div>

        {/* Auto-close indicator */}
        <div className="auto-close-indicator">
          <div className="progress-bar">
            <div className="progress-fill error" style={{ backgroundColor: getErrorColor() }}></div>
          </div>
          <span className="auto-close-text">Tự động đóng sau 5 giây</span>
        </div>
      </div>
    </div>
  );
};

export default ErrorModal;
