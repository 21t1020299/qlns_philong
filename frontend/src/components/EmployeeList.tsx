import React, { useState, useEffect } from 'react';
import { Employee } from '../types/employee';
import { employeeAPI } from '../services/api';
import './EmployeeList.css';

const EmployeeList: React.FC = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [showAddForm, setShowAddForm] = useState(false);

  const pageSize = 10;

  useEffect(() => {
    loadEmployees();
  }, [page, searchTerm]);

  const loadEmployees = async () => {
    try {
      setLoading(true);
      const response = await employeeAPI.getEmployees(page, pageSize, searchTerm);
      setEmployees(response.employees);
      setTotal(response.total);
      setError(null);
    } catch (err) {
      setError('Không thể tải danh sách nhân viên');
      console.error('Error loading employees:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setPage(1);
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa nhân viên này?')) {
      try {
        await employeeAPI.deleteEmployee(id);
        loadEmployees();
      } catch (err) {
        setError('Không thể xóa nhân viên');
        console.error('Error deleting employee:', err);
      }
    }
  };

  const totalPages = Math.ceil(total / pageSize);

  if (loading && employees.length === 0) {
    return <div className="loading">Đang tải...</div>;
  }

  return (
    <div className="employee-list">
      <div className="header">
        <h1>📋 Danh sách nhân viên</h1>
        <button 
          className="add-button"
          onClick={() => setShowAddForm(true)}
        >
          ➕ Thêm nhân viên
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="search-section">
        <form onSubmit={handleSearch}>
          <input
            type="text"
            placeholder="Tìm kiếm theo tên, email, số điện thoại..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
          <button type="submit" className="search-button">
            🔍 Tìm kiếm
          </button>
        </form>
      </div>

      <div className="stats">
        <span>📊 Tổng cộng: {total} nhân viên</span>
        <span>📄 Trang {page} / {totalPages}</span>
      </div>

      <div className="table-container">
        <table className="employee-table">
          <thead>
            <tr>
              <th>Mã NV</th>
              <th>Họ tên</th>
              <th>Email</th>
              <th>SĐT</th>
              <th>Giới tính</th>
              <th>Ngày sinh</th>
              <th>Chức vụ</th>
              <th>Trình độ</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {employees.map((employee) => (
              <tr key={employee.id}>
                <td>{employee.manv}</td>
                <td>{employee.tennv}</td>
                <td>{employee.email}</td>
                <td>{employee.sdt}</td>
                <td>{employee.gtinh}</td>
                <td>{new Date(employee.ngsinh).toLocaleDateString('vi-VN')}</td>
                <td>{employee.macv}</td>
                <td>{employee.trinhdo}</td>
                <td>
                  <button 
                    className="edit-button"
                    onClick={() => console.log('Edit:', employee.id)}
                  >
                    ✏️
                  </button>
                  <button 
                    className="delete-button"
                    onClick={() => handleDelete(employee.id)}
                  >
                    🗑️
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {employees.length === 0 && !loading && (
        <div className="no-data">
          📭 Không tìm thấy nhân viên nào
        </div>
      )}

      <div className="pagination">
        <button 
          onClick={() => setPage(page - 1)}
          disabled={page <= 1}
          className="page-button"
        >
          ← Trước
        </button>
        <span className="page-info">
          Trang {page} / {totalPages}
        </span>
        <button 
          onClick={() => setPage(page + 1)}
          disabled={page >= totalPages}
          className="page-button"
        >
          Sau →
        </button>
      </div>
    </div>
  );
};

export default EmployeeList; 