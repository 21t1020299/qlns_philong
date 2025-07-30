import axios from 'axios';
import { Employee, EmployeeFormData, EmployeeListResponse, EmployeeStats } from '../types/employee';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'https://qlns-philong.onrender.com';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Employee API
export const employeeAPI = {
  // Get all employees with pagination
  getEmployees: async (page: number = 1, size: number = 10, search?: string) => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    });
    
    if (search) {
      params.append('search', search);
    }
    
    const response = await api.get<EmployeeListResponse>(`/api/employees/?${params}`);
    return response.data;
  },

  // Get employee by ID
  getEmployee: async (id: string) => {
    const response = await api.get<Employee>(`/api/employees/${id}`);
    return response.data;
  },

  // Create new employee
  createEmployee: async (data: EmployeeFormData) => {
    const response = await api.post<Employee>('/api/employees/', data);
    return response.data;
  },

  // Update employee
  updateEmployee: async (id: string, data: Partial<EmployeeFormData>) => {
    const response = await api.put<Employee>(`/api/employees/${id}`, data);
    return response.data;
  },

  // Delete employee
  deleteEmployee: async (id: string) => {
    await api.delete(`/api/employees/${id}`);
  },

  // Get employee statistics
  getStats: async () => {
    const response = await api.get<EmployeeStats>('/api/employees/stats');
    return response.data;
  },
};

// Health check
export const healthAPI = {
  check: async () => {
    const response = await api.get('/health');
    return response.data;
  },
};

export default api; 