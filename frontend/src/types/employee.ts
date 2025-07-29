export interface Employee {
  id: string;
  manv: string;
  tennv: string;
  gtinh: string;
  email: string;
  sdt: string;
  ngsinh: string;
  dchi: string;
  dchithuongtru: string;
  noidkhktt: string;
  dtoc: string;
  trinhdo: string;
  qtich: string;
  skhoe: string;
  macv: string;
  hotencha: string;
  hotenme: string;
  created_at: string;
  updated_at: string;
}

export interface EmployeeFormData {
  tennv: string;
  gtinh: string;
  email: string;
  sdt: string;
  ngsinh: string;
  dchi: string;
  dchithuongtru: string;
  noidkhktt: string;
  dtoc: string;
  trinhdo: string;
  qtich: string;
  skhoe: string;
  macv: string;
  hotencha: string;
  hotenme: string;
}

export interface EmployeeListResponse {
  employees: Employee[];
  total: number;
  page: number;
  size: number;
}

export interface EmployeeStats {
  total: number;
  male: number;
  female: number;
  male_percentage: number;
  female_percentage: number;
}