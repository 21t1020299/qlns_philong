import React, { useState, useEffect } from 'react';
import { Employee, EmployeeFormData } from '../types/employee';
import { optionsAPI } from '../services/api';
import './EmployeeForm.css';

interface EmployeeFormProps {
  employee?: Employee;
  onSubmit: (data: EmployeeFormData) => void;
  onCancel: () => void;
  isLoading?: boolean;
}

const EmployeeForm: React.FC<EmployeeFormProps> = ({
  employee,
  onSubmit,
  onCancel,
  isLoading = false
}) => {
  const [formData, setFormData] = useState<EmployeeFormData>({
    tennv: '',
    gtinh: '',
    email: '',
    sdt: '',
    ngsinh: '',
    dchi: '',
    dchithuongtru: '',
    noidkhktt: '',
    dtoc: '',
    trinhdo: '',
    qtich: '',
    skhoe: '',
    macv: '',
    hotencha: '',
    hotenme: ''
  });

  const [errors, setErrors] = useState<Partial<EmployeeFormData>>({});
  const [options, setOptions] = useState<{
    dantoc: Array<{value: string; label: string}>;
    suckhoe: Array<{value: string; label: string}>;
    quoctich: Array<{value: string; label: string}>;
    chucvu: Array<{value: string; label: string}>;
  }>({
    dantoc: [],
    suckhoe: [],
    quoctich: [],
    chucvu: []
  });

  useEffect(() => {
    // Load options
    const loadOptions = async () => {
      try {
        const [dantoc, suckhoe, quoctich, chucvu] = await Promise.all([
          optionsAPI.getDantocOptions(),
          optionsAPI.getSuckhoeOptions(),
          optionsAPI.getQuoctichOptions(),
          optionsAPI.getChucvuOptions()
        ]);
        setOptions({ dantoc, suckhoe, quoctich, chucvu });
      } catch (error) {
        console.error('Error loading options:', error);
      }
    };
    loadOptions();
  }, []);

  useEffect(() => {
    if (employee) {
      setFormData({
        tennv: employee.tennv,
        gtinh: employee.gtinh,
        email: employee.email,
        sdt: employee.sdt,
        ngsinh: employee.ngsinh,
        dchi: employee.dchi,
        dchithuongtru: employee.dchithuongtru,
        noidkhktt: employee.noidkhktt,
        dtoc: employee.dtoc,
        trinhdo: employee.trinhdo,
        qtich: employee.qtich,
        skhoe: employee.skhoe,
        macv: employee.macv,
        hotencha: employee.hotencha,
        hotenme: employee.hotenme
      });
    }
  }, [employee]);

  const validateField = (name: keyof EmployeeFormData, value: string): string => {
    switch (name) {
      case 'tennv':
        if (!value.trim()) return 'Họ tên không được để trống';
        if (!/^[a-zA-ZÀ-ỹ\s-]+$/.test(value)) {
          return 'Họ tên chỉ được chứa chữ cái, dấu tiếng Việt và khoảng trắng';
        }
        break;
      
      case 'sdt':
        if (!value.trim()) return 'Số điện thoại không được để trống';
        if (!/^0[0-9]{9}$/.test(value)) {
          return 'Số điện thoại phải có 10 số và bắt đầu bằng 0';
        }
        break;
      
      case 'email':
        if (!value.trim()) return 'Email không được để trống';
        const emailParts = value.split('@');
        if (emailParts.length !== 2) return 'Email không hợp lệ';
        const domainParts = emailParts[1].split('.');
        if (domainParts.length < 2 || domainParts.length > 4) {
          return 'Domain tối đa 4 cấp';
        }
        break;
      
      case 'ngsinh':
        if (!value) return 'Ngày sinh không được để trống';
        const today = new Date();
        const birthDate = new Date(value);
        const age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        const actualAge = monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate()) ? age - 1 : age;
        if (actualAge < 18 || actualAge >= 66) {
          return 'Tuổi phải từ 18-65';
        }
        break;
      
      case 'dchi':
      case 'dchithuongtru':
        if (!value.trim()) return 'Địa chỉ không được để trống';
        if (value.length > 200) return 'Địa chỉ không được quá 200 ký tự';
        break;
      
      case 'hotencha':
      case 'hotenme':
        if (!value.trim()) return 'Họ tên không được để trống';
        if (!/^[a-zA-ZÀ-ỹ\s-]+$/.test(value)) {
          return 'Họ tên chỉ được chứa chữ cái';
        }
        if (value.length > 100) return 'Họ tên không được quá 100 ký tự';
        break;
      
      case 'gtinh':
      case 'macv':
      case 'noidkhktt':
      case 'dtoc':
      case 'trinhdo':
      case 'qtich':
      case 'skhoe':
        if (!value.trim()) return 'Trường này không được để trống';
        break;
    }
    return '';
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    
    // Clear error when user starts typing
    if (errors[name as keyof EmployeeFormData]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleBlur = (e: React.FocusEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    const error = validateField(name as keyof EmployeeFormData, value);
    setErrors(prev => ({ ...prev, [name]: error }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    // Validate all fields
    const newErrors: Partial<EmployeeFormData> = {};
    let hasErrors = false;
    
    Object.keys(formData).forEach(key => {
      const fieldName = key as keyof EmployeeFormData;
      const error = validateField(fieldName, formData[fieldName]);
      if (error) {
        newErrors[fieldName] = error;
        hasErrors = true;
      }
    });
    
    setErrors(newErrors);
    
    if (!hasErrors) {
      onSubmit(formData);
    }
  };

  return (
    <div className="employee-form-overlay">
      <div className="employee-form-modal">
        <div className="form-header">
          <h2>{employee ? 'Sửa thông tin nhân viên' : 'Thêm nhân viên mới'}</h2>
          <button className="close-btn" onClick={onCancel}>&times;</button>
        </div>
        
        <form onSubmit={handleSubmit} className="employee-form">
          <div className="form-grid">
            <div className="form-group">
              <label htmlFor="tennv">Họ tên <span className="required">*</span></label>
              <input
                type="text"
                id="tennv"
                name="tennv"
                value={formData.tennv}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.tennv ? 'error' : ''}
              />
              {errors.tennv && <span className="error-message">{errors.tennv}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="gtinh">Giới tính <span className="required">*</span></label>
              <select
                id="gtinh"
                name="gtinh"
                value={formData.gtinh}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.gtinh ? 'error' : ''}
              >
                <option value="">Chọn giới tính</option>
                <option value="Nam">Nam</option>
                <option value="Nữ">Nữ</option>
              </select>
              {errors.gtinh && <span className="error-message">{errors.gtinh}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="email">Email <span className="required">*</span></label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.email ? 'error' : ''}
              />
              {errors.email && <span className="error-message">{errors.email}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="sdt">Số điện thoại <span className="required">*</span></label>
              <input
                type="tel"
                id="sdt"
                name="sdt"
                value={formData.sdt}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.sdt ? 'error' : ''}
              />
              {errors.sdt && <span className="error-message">{errors.sdt}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="ngsinh">Ngày sinh <span className="required">*</span></label>
              <input
                type="date"
                id="ngsinh"
                name="ngsinh"
                value={formData.ngsinh}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.ngsinh ? 'error' : ''}
              />
              {errors.ngsinh && <span className="error-message">{errors.ngsinh}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="macv">Mã chức vụ <span className="required">*</span></label>
              <select
                id="macv"
                name="macv"
                value={formData.macv}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.macv ? 'error' : ''}
              >
                <option value="">Chọn chức vụ</option>
                {options.chucvu.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.macv && <span className="error-message">{errors.macv}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="dchi">Địa chỉ hiện tại <span className="required">*</span></label>
              <textarea
                id="dchi"
                name="dchi"
                value={formData.dchi}
                onChange={handleChange}
                onBlur={handleBlur}
                rows={3}
                className={errors.dchi ? 'error' : ''}
              />
              {errors.dchi && <span className="error-message">{errors.dchi}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="dchithuongtru">Địa chỉ thường trú <span className="required">*</span></label>
              <textarea
                id="dchithuongtru"
                name="dchithuongtru"
                value={formData.dchithuongtru}
                onChange={handleChange}
                onBlur={handleBlur}
                rows={3}
                className={errors.dchithuongtru ? 'error' : ''}
              />
              {errors.dchithuongtru && <span className="error-message">{errors.dchithuongtru}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="noidkhktt">Nơi đăng ký HKTT <span className="required">*</span></label>
              <input
                type="text"
                id="noidkhktt"
                name="noidkhktt"
                value={formData.noidkhktt}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.noidkhktt ? 'error' : ''}
              />
              {errors.noidkhktt && <span className="error-message">{errors.noidkhktt}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="dtoc">Dân tộc <span className="required">*</span></label>
              <select
                id="dtoc"
                name="dtoc"
                value={formData.dtoc}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.dtoc ? 'error' : ''}
              >
                <option value="">Chọn dân tộc</option>
                {options.dantoc.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.dtoc && <span className="error-message">{errors.dtoc}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="trinhdo">Trình độ <span className="required">*</span></label>
              <select
                id="trinhdo"
                name="trinhdo"
                value={formData.trinhdo}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.trinhdo ? 'error' : ''}
              >
                <option value="">Chọn trình độ</option>
                <option value="Trung cấp">Trung cấp</option>
                <option value="Cao đẳng">Cao đẳng</option>
                <option value="Đại học">Đại học</option>
                <option value="Sau đại học">Sau đại học</option>
              </select>
              {errors.trinhdo && <span className="error-message">{errors.trinhdo}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="qtich">Quốc tịch <span className="required">*</span></label>
              <select
                id="qtich"
                name="qtich"
                value={formData.qtich}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.qtich ? 'error' : ''}
              >
                <option value="">Chọn quốc tịch</option>
                {options.quoctich.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.qtich && <span className="error-message">{errors.qtich}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="skhoe">Sức khỏe <span className="required">*</span></label>
              <select
                id="skhoe"
                name="skhoe"
                value={formData.skhoe}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.skhoe ? 'error' : ''}
              >
                <option value="">Chọn tình trạng sức khỏe</option>
                {options.suckhoe.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.skhoe && <span className="error-message">{errors.skhoe}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="hotencha">Họ tên cha <span className="required">*</span></label>
              <input
                type="text"
                id="hotencha"
                name="hotencha"
                value={formData.hotencha}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.hotencha ? 'error' : ''}
              />
              {errors.hotencha && <span className="error-message">{errors.hotencha}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="hotenme">Họ tên mẹ <span className="required">*</span></label>
              <input
                type="text"
                id="hotenme"
                name="hotenme"
                value={formData.hotenme}
                onChange={handleChange}
                onBlur={handleBlur}
                className={errors.hotenme ? 'error' : ''}
              />
              {errors.hotenme && <span className="error-message">{errors.hotenme}</span>}
            </div>
          </div>

          <div className="form-actions">
            <button type="button" className="btn btn-secondary" onClick={onCancel}>
              ❌ Hủy
            </button>
            <button type="submit" className="btn btn-primary" disabled={isLoading}>
              {isLoading ? '⏳ Đang xử lý...' : (employee ? '💾 Cập nhật' : '💾 Lưu')}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EmployeeForm; 