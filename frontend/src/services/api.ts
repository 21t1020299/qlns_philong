import axios from 'axios';
import { Employee, EmployeeFormData, EmployeeListResponse, EmployeeStats } from '../types/employee';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'https://qlns-philong.onrender.com/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for logging
api.interceptors.request.use(
  (config) => {
    console.log(`🚀 API Request: ${config.method?.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    console.log(`✅ API Response: ${response.status} ${response.config.url}`);
    return response;
  },
  (error) => {
    console.error(`❌ API Error: ${error.response?.status} ${error.config?.url}`, error.response?.data);
    
    // Handle specific error cases
    if (error.response?.status === 404) {
      throw new Error('Không tìm thấy dữ liệu');
    } else if (error.response?.status === 422) {
      // Validation errors
      const validationErrors = error.response.data?.detail;
      if (Array.isArray(validationErrors)) {
        const errorMessages = validationErrors.map((err: any) => err.msg).join(', ');
        throw new Error(`Lỗi validation: ${errorMessages}`);
      }
    } else if (error.response?.status >= 500) {
      throw new Error('Lỗi server, vui lòng thử lại sau');
    } else if (error.code === 'NETWORK_ERROR') {
      throw new Error('Không thể kết nối đến server');
    }
    
    throw new Error(error.response?.data?.detail || error.message || 'Có lỗi xảy ra');
  }
);

export const employeeAPI = {
  // Get employees with pagination and search
  getEmployees: async (params: {
    page?: number;
    size?: number;
    search?: string;
  } = {}): Promise<EmployeeListResponse> => {
    const { page = 1, size = 10, search = '' } = params;
    const response = await api.get('/employees/', {
      params: { page, size, search }
    });
    return response.data;
  },

  // Get single employee by ID
  getEmployee: async (manv: string): Promise<Employee> => {
    const response = await api.get(`/employees/${manv}`);
    return response.data;
  },

  // Create new employee
  createEmployee: async (employeeData: EmployeeFormData): Promise<Employee> => {
    const response = await api.post('/employees/', employeeData);
    return response.data;
  },

  // Update employee
  updateEmployee: async (manv: string, employeeData: EmployeeFormData): Promise<Employee> => {
    const response = await api.put(`/employees/${manv}`, employeeData);
    return response.data;
  },

  // Delete employee
  deleteEmployee: async (manv: string): Promise<void> => {
    await api.delete(`/employees/${manv}`);
  },

  // Get employee statistics
  getStats: async (): Promise<EmployeeStats> => {
    const response = await api.get('/employees/stats');
    return response.data;
  },
};

export const healthAPI = {
  // Health check
  checkHealth: async (): Promise<{ status: string; timestamp: string }> => {
    const response = await api.get('/health');
    return response.data;
  },
};

export default api; 