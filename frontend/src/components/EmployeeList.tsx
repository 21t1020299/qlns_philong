import React, { useState, useEffect } from 'react';
import { Employee, EmployeeFormData, EmployeeStats } from '../types/employee';
import { employeeAPI } from '../services/api';
import EmployeeForm from './EmployeeForm';
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
      setError('Lỗi tải dữ liệu: ' + (err as Error).message);
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
        setSuccess('Cập nhật nhân viên thành công!');
      } else {
        // Add new employee
        await employeeAPI.createEmployee(formData);
        setSuccess('Thêm nhân viên thành công!');
        // Reset to first page when adding new employee
        setCurrentPage(1);
      }
      
      setShowForm(false);
      // Reload data after form submission
      await loadEmployees();
      await loadStats();
    } catch (err) {
      setError('Lỗi: ' + (err as Error).message);
    } finally {
      setFormLoading(false);
    }
  };

  // Handle delete employee
  const handleDeleteEmployee = async (manv: string) => {
    if (!window.confirm(`Bạn có chắc muốn xóa nhân viên ${manv}?`)) {
      return;
    }

    try {
      await employeeAPI.deleteEmployee(manv);
      setSuccess('Xóa nhân viên thành công!');
      // Check if current page will be empty after deletion
      if (employees.length === 1 && currentPage > 1) {
        setCurrentPage(currentPage - 1);
      }
      // Reload data after deletion
      await loadEmployees();
      await loadStats();
    } catch (err) {
      setError('Lỗi xóa nhân viên: ' + (err as Error).message);
    }
  };

  // View employee details
  const handleViewEmployee = async (manv: string) => {
    try {
      const employee = await employeeAPI.getEmployee(manv);
      const details = `
🏢 Chi tiết nhân viên: ${employee.manv}

👤 Họ tên: ${employee.tennv}
🚻 Giới tính: ${employee.gtinh}
📧 Email: ${employee.email}
📱 SĐT: ${employee.sdt}
🎂 Ngày sinh: ${employee.ngsinh}
🏠 Địa chỉ: ${employee.dchi}
🏘️ Thường trú: ${employee.dchithuongtru}
📍 HKTT: ${employee.noidkhktt}
👨‍👩‍👧‍👦 Dân tộc: ${employee.dtoc}
🎓 Trình độ: ${employee.trinhdo}
🌍 Quốc tịch: ${employee.qtich}
💪 Sức khỏe: ${employee.skhoe}
💼 Chức vụ: ${employee.macv}
👨 Họ tên cha: ${employee.hotencha}
👩 Họ tên mẹ: ${employee.hotenme}
      `;
      alert(details);
    } catch (err) {
      setError('Lỗi xem chi tiết: ' + (err as Error).message);
    }
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
                          onClick={() => handleDeleteEmployee(employee.manv)}
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
    </div>
  );
};

export default EmployeeList; 