import React, { useState, useEffect } from 'react';
import { Employee, EmployeeFormData, EmployeeStats } from '../types/employee';
import { employeeAPI } from '../services/api';
import EmployeeForm from './EmployeeForm';
import DeleteConfirmationModal from './DeleteConfirmationModal';
import EmployeeDetailModal from './EmployeeDetailModal';
import SuccessModal from './SuccessModal';
import ErrorModal from './ErrorModal';
import './EmployeeList.css';

const EmployeeList: React.FC = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [stats, setStats] = useState<EmployeeStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');
  
  // Pagination
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [pageSize] = useState(10);
  
  // Search
  const [searchTerm, setSearchTerm] = useState('');
  
  // Form state
  const [showForm, setShowForm] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState<Employee | undefined>();
  const [formLoading, setFormLoading] = useState(false);
  
  // Delete modal state
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<{id: string, name: string} | null>(null);
  
  // Detail modal state
  const [showDetailModal, setShowDetailModal] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState<Employee | null>(null);
  
  // Success modal state
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [successInfo, setSuccessInfo] = useState<{
    title: string;
    message: string;
    employeeInfo?: { id: string; name: string; email: string };
  } | null>(null);
  
  // Error modal state
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [errorInfo, setErrorInfo] = useState<{
    title: string;
    message: string;
    errorType: 'validation' | 'network' | 'server' | 'general';
  } | null>(null);

  // Helper function to determine error type
  const getErrorType = (errorMessage: string): 'validation' | 'network' | 'server' | 'general' => {
    const message = errorMessage.toLowerCase();
    if (message.includes('validation') || message.includes('validation')) {
      return 'validation';
    } else if (message.includes('kết nối') || message.includes('network') || message.includes('connection')) {
      return 'network';
    } else if (message.includes('server') || message.includes('500') || message.includes('502')) {
      return 'server';
    }
    return 'general';
  };

  // Helper function to show error modal
  const displayErrorModal = (title: string, message: string) => {
    const errorType = getErrorType(message);
    setErrorInfo({ title, message, errorType });
    setShowErrorModal(true);
  };

  // Load employees
  const loadEmployees = async () => {
    try {
      setLoading(true);
      setError('');
      
      console.log('🔄 Loading employees with:', { currentPage, pageSize, searchTerm });
      
      const response = await employeeAPI.getEmployees({
        page: currentPage,
        size: pageSize,
        search: searchTerm
      });
      
      console.log('✅ Employees loaded:', response);
      
      setEmployees(response.employees);
      
      // Validate total and calculate totalPages
      const total = response.total || 0;
      const calculatedTotalPages = Math.max(1, Math.ceil(total / pageSize));
      setTotalPages(calculatedTotalPages);
      
      console.log('📊 Updated state:', { 
        employeesCount: response.employees.length, 
        total: total, 
        totalPages: calculatedTotalPages 
      });
    } catch (err) {
      console.error('❌ Error loading employees:', err);
      displayErrorModal('❌ Lỗi tải dữ liệu', 'Lỗi tải dữ liệu: ' + (err as Error).message);
    } finally {
      setLoading(false);
    }
  };

  // Load stats
  const loadStats = async () => {
    try {
      const statsData = await employeeAPI.getStats();
      setStats(statsData);
    } catch (err) {
      console.error('Error loading stats:', err);
      displayErrorModal('❌ Lỗi tải thống kê', 'Không thể tải dữ liệu thống kê: ' + (err as Error).message);
    }
  };

  // Load data on mount and when dependencies change
  useEffect(() => {
    loadEmployees();
    loadStats();
  }, [currentPage, searchTerm]);

  // Add refresh function
  const handleRefresh = async () => {
    await loadEmployees();
    await loadStats();
    setSuccess('Dữ liệu đã được cập nhật!');
    setTimeout(() => setSuccess(''), 3000);
  };

  // Debug function
  const handleDebug = async () => {
    try {
      const debugInfo = await employeeAPI.debugEmployees();
      console.log('🔍 Debug info:', debugInfo);
      alert(`Debug info: ${JSON.stringify(debugInfo, null, 2)}`);
    } catch (err) {
      console.error('Debug error:', err);
      alert('Debug error: ' + (err as Error).message);
    }
  };

  // Handle search
  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
    setCurrentPage(1); // Reset to first page when searching
  };

  // Handle pagination
  const handlePageChange = (delta: number) => {
    const newPage = currentPage + delta;
    console.log('🔄 Page change requested:', { 
      currentPage, 
      delta, 
      newPage, 
      totalPages,
      isValid: newPage >= 1 && newPage <= totalPages 
    });
    
    if (newPage >= 1 && newPage <= totalPages) {
      console.log('✅ Changing to page:', newPage);
      setCurrentPage(newPage);
    } else {
      console.log('❌ Invalid page change:', newPage);
    }
  };

  // Show add form
  const handleAddEmployee = () => {
    setEditingEmployee(undefined);
    setShowForm(true);
  };

  // Show edit form
  const handleEditEmployee = (employee: Employee) => {
    setEditingEmployee(employee);
    setShowForm(true);
  };

  // Handle form submission
  const handleFormSubmit = async (formData: EmployeeFormData) => {
    try {
      setFormLoading(true);
      
      if (editingEmployee) {
        // Update employee
        await employeeAPI.updateEmployee(editingEmployee.manv, formData);
        setSuccessInfo({
          title: '✅ Cập nhật thành công!',
          message: 'Thông tin nhân viên đã được cập nhật thành công.',
          employeeInfo: {
            id: editingEmployee.manv,
            name: editingEmployee.tennv,
            email: editingEmployee.email
          }
        });
      } else {
        // Add new employee
        const newEmployee = await employeeAPI.createEmployee(formData);
        setSuccessInfo({
          title: '🎉 Thêm nhân viên thành công!',
          message: 'Nhân viên mới đã được thêm vào hệ thống.',
          employeeInfo: {
            id: newEmployee.manv,
            name: newEmployee.tennv,
            email: newEmployee.email
          }
        });
        // Reset to first page when adding new employee
        setCurrentPage(1);
      }
      
      setShowForm(false);
      setShowSuccessModal(true);
      // Reload data after form submission
      await loadEmployees();
      await loadStats();
    } catch (err) {
      displayErrorModal('❌ Lỗi thao tác', 'Lỗi: ' + (err as Error).message);
    } finally {
      setFormLoading(false);
    }
  };

  // Handle delete employee with beautiful modal
  const handleDeleteEmployee = (manv: string, employeeName: string) => {
    setDeleteTarget({ id: manv, name: employeeName });
    setShowDeleteModal(true);
  };

  // Handle delete confirmation from modal
  const handleDeleteConfirm = async (confirmation: string) => {
    if (!deleteTarget) return;

    try {
      await employeeAPI.deleteEmployee(deleteTarget.id, confirmation);
      setSuccess(`✅ Xóa nhân viên ${deleteTarget.id} - ${deleteTarget.name} thành công!`);
      
      // Check if current page will be empty after deletion
      if (employees.length === 1 && currentPage > 1) {
        setCurrentPage(currentPage - 1);
      }
      
      // Reload data after deletion
      await loadEmployees();
      await loadStats();
      
      // Close modal
      setShowDeleteModal(false);
      setDeleteTarget(null);
    } catch (err) {
      displayErrorModal('❌ Lỗi xóa nhân viên', '❌ Lỗi xóa nhân viên: ' + (err as Error).message);
      setShowDeleteModal(false);
      setDeleteTarget(null);
    }
  };

  // Handle delete modal close
  const handleDeleteModalClose = () => {
    setShowDeleteModal(false);
    setDeleteTarget(null);
  };

  // View employee details
  const handleViewEmployee = async (manv: string) => {
    try {
      const employee = await employeeAPI.getEmployee(manv);
      setSelectedEmployee(employee);
      setShowDetailModal(true);
    } catch (err) {
      displayErrorModal('❌ Lỗi xem chi tiết', 'Lỗi xem chi tiết: ' + (err as Error).message);
    }
  };

  // Handle detail modal close
  const handleDetailModalClose = () => {
    setShowDetailModal(false);
    setSelectedEmployee(null);
  };

  // Handle success modal close
  const handleSuccessModalClose = () => {
    setShowSuccessModal(false);
    setSuccessInfo(null);
  };

  // Handle error modal close
  const handleErrorModalClose = () => {
    setShowErrorModal(false);
    setErrorInfo(null);
  };

  // Clear messages
  const clearMessages = () => {
    setError('');
    setSuccess('');
  };

  return (
    <div className="employee-list-container">
      {/* Header */}
      <div className="header">
        <h1>🏢 QLNS Phi Long</h1>
        <p>Hệ thống Quản lý Nhân sự</p>
      </div>

      {/* Stats Grid */}
      {stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-number">{stats.total}</div>
            <div className="stat-label">Tổng nhân viên</div>
          </div>
          <div className="stat-card">
            <div className="stat-number">{stats.male_count}</div>
            <div className="stat-label">Nam</div>
          </div>
          <div className="stat-card">
            <div className="stat-number">{stats.female_count}</div>
            <div className="stat-label">Nữ</div>
          </div>
          <div className="stat-card">
            <div className="stat-number">{stats.recent_additions}</div>
            <div className="stat-label">Mới thêm</div>
          </div>
        </div>
      )}

      {/* Main Content */}
      <div className="main-content">
        {/* Controls */}
        <div className="controls">
          <div className="search-box">
            <input
              type="text"
              placeholder="🔍 Tìm kiếm nhân viên..."
              value={searchTerm}
              onChange={handleSearch}
            />
          </div>
          <button className="btn btn-primary" onClick={handleRefresh}>
            🔄 Làm mới
          </button>
          <button className="btn btn-success" onClick={handleAddEmployee}>
            ➕ Thêm nhân viên
          </button>
          <button className="btn btn-info" onClick={handleDebug}>
            🐛 Debug
          </button>
        </div>

        {/* Messages */}
        {error && (
          <div className="error-message" onClick={clearMessages}>
            ❌ {error}
          </div>
        )}
        {success && (
          <div className="success-message" onClick={clearMessages}>
            ✅ {success}
          </div>
        )}

        {/* Loading */}
        {loading && (
          <div className="loading">
            <p>⏳ Đang tải dữ liệu...</p>
          </div>
        )}

        {/* Employees Table */}
        {!loading && (
          <div className="table-container">
            <table className="employees-table">
              <thead>
                <tr>
                  <th>Mã NV</th>
                  <th>Họ tên</th>
                  <th>Giới tính</th>
                  <th>Email</th>
                  <th>SĐT</th>
                  <th>Chức vụ</th>
                  <th>Thao tác</th>
                </tr>
              </thead>
              <tbody>
                {employees.length === 0 ? (
                  <tr>
                    <td colSpan={7} className="no-data">
                      Không có dữ liệu
                    </td>
                  </tr>
                ) : (
                  employees.map((employee) => (
                    <tr key={employee.manv}>
                      <td>
                        <strong>{employee.manv}</strong>
                      </td>
                      <td>{employee.tennv}</td>
                      <td>
                        <span className={`gender-badge gender-${employee.gtinh === 'Nam' ? 'male' : 'female'}`}>
                          {employee.gtinh}
                        </span>
                      </td>
                      <td>{employee.email}</td>
                      <td>{employee.sdt}</td>
                      <td>{employee.macv}</td>
                      <td className="actions">
                        <button
                          className="btn btn-secondary btn-sm"
                          onClick={() => handleViewEmployee(employee.manv)}
                          title="Xem chi tiết"
                        >
                          👁️
                        </button>
                        <button
                          className="btn btn-primary btn-sm"
                          onClick={() => handleEditEmployee(employee)}
                          title="Sửa"
                        >
                          ✏️
                        </button>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => handleDeleteEmployee(employee.manv, employee.tennv)}
                          title="Xóa"
                        >
                          🗑️
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}

        {/* Pagination */}
        {!loading && (
          <div className="pagination">
            <button
              onClick={() => handlePageChange(-1)}
              disabled={currentPage <= 1}
              className="btn btn-secondary"
            >
              ← Trước
            </button>
            <span className="page-info">
              Trang {currentPage} / {totalPages}
            </span>
            <button
              onClick={() => handlePageChange(1)}
              disabled={currentPage >= totalPages}
              className="btn btn-secondary"
            >
              Sau →
            </button>
          </div>
        )}
      </div>

      {/* Employee Form Modal */}
      {showForm && (
        <EmployeeForm
          employee={editingEmployee}
          onSubmit={handleFormSubmit}
          onCancel={() => setShowForm(false)}
          isLoading={formLoading}
        />
      )}

      {/* Delete Confirmation Modal */}
      {showDeleteModal && deleteTarget && (
        <DeleteConfirmationModal
          isOpen={showDeleteModal}
          onClose={handleDeleteModalClose}
          onConfirm={handleDeleteConfirm}
          employeeId={deleteTarget.id}
          employeeName={deleteTarget.name}
        />
      )}

      {/* Employee Detail Modal */}
      {showDetailModal && selectedEmployee && (
        <EmployeeDetailModal
          isOpen={showDetailModal}
          onClose={handleDetailModalClose}
          employee={selectedEmployee}
        />
      )}

      {/* Success Modal */}
      {showSuccessModal && successInfo && (
        <SuccessModal
          isOpen={showSuccessModal}
          onClose={handleSuccessModalClose}
          title={successInfo.title}
          message={successInfo.message}
          employeeInfo={successInfo.employeeInfo}
        />
      )}

      {/* Error Modal */}
      {showErrorModal && errorInfo && (
        <ErrorModal
          isOpen={showErrorModal}
          onClose={handleErrorModalClose}
          title={errorInfo.title}
          message={errorInfo.message}
          errorType={errorInfo.errorType}
        />
      )}
    </div>
  );
};

export default EmployeeList; 