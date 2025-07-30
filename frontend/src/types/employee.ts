export interface Employee {
  manv: string;
  tennv: string;
  gtinh: string;
  email: string;
  dchi: string;
  ngsinh: string;
  qtich: string;
  sdt: string;
  trinhdo: string;
  noidkhktt: string;
  dchithuongtru: string;
  dtoc: string;
  skhoe: string;
  macv: string;
  hotencha: string;
  hotenme: string;
  anhchandung?: string;
  anhcmnd?: string;
}

export interface EmployeeFormData {
  tennv: string;
  gtinh: string;
  email: string;
  dchi: string;
  ngsinh: string;
  qtich: string;
  sdt: string;
  trinhdo: string;
  noidkhktt: string;
  dchithuongtru: string;
  dtoc: string;
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
  male_count: number;
  female_count: number;
  education_distribution: Record<string, number>;
  age_distribution: Record<string, number>;
  recent_additions: number;
  last_updated: string;
} 