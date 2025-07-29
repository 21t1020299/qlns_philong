import axios from 'axios';
import { Employee, EmployeeFormData, EmployeeListResponse, EmployeeStats } from '../types/employee';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'https://qlns-philong.onrender.com';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const employeeApi = {
  // Get all employees with pagination and filtering
  getEmployees: async (params?: {
    page?: number;
    size?: number;
    search?: string;
    gender?: string;
  }): Promise<EmployeeListResponse> => {
    const response = await api.get('/api/employees', { params });
    return response.data;
  },

  // Get employee by ID
  getEmployee: async (id: string): Promise<Employee> => {
    const response = await api.get(`/api/employees/${id}`);
    return response.data;
  },

  // Create new employee
  createEmployee: async (data: EmployeeFormData): Promise<Employee> => {
    const response = await api.post('/api/employees', data);
    return response.data;
  },

  // Update employee
  updateEmployee: async (id: string, data: Partial<EmployeeFormData>): Promise<Employee> => {
    const response = await api.put(`/api/employees/${id}`, data);
    return response.data;
  },

  // Delete employee
  deleteEmployee: async (id: string): Promise<void> => {
    await api.delete(`/api/employees/${id}`);
  },

  // Get employee statistics
  getStats: async (): Promise<EmployeeStats> => {
    const response = await api.get('/api/employees/stats/summary');
    return response.data;
  },
};

export default api;