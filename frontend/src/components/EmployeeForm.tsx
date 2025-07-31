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
        
        // Kiểm tra ký tự đặc biệt không hợp lệ
        if (/[<>"']/.test(value)) {
          return 'Email không được chứa ký tự đặc biệt: < > " \'';
        }
        
        // Kiểm tra format email cơ bản
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(value)) {
          return 'Email không đúng định dạng';
        }
        
        // Kiểm tra độ dài email
        if (value.length > 254) {
          return 'Email quá dài (tối đa 254 ký tự)';
        }
        
        const emailParts = value.split('@');
        if (emailParts.length !== 2) return 'Email không hợp lệ';
        
        const localPart = emailParts[0];
        const domain = emailParts[1];
        
        // Kiểm tra local part
        if (localPart.length > 64) {
          return 'Phần trước @ quá dài (tối đa 64 ký tự)';
        }
        if (localPart.startsWith('.') || localPart.endsWith('.')) {
          return 'Phần trước @ không được bắt đầu hoặc kết thúc bằng dấu chấm';
        }
        
        // Kiểm tra domain
        const domainParts = domain.split('.');
        if (domainParts.length < 2 || domainParts.length > 4) {
          return 'Domain phải có 2-4 phần';
        }
        
        // Kiểm tra TLD
        const tld = domainParts[domainParts.length - 1];
        if (tld.length < 2) {
          return 'Domain cấp cao nhất phải có ít nhất 2 ký tự';
        }
        break;
      
      case 'ngsinh':
        if (!value) return 'Ngày sinh không được để trống';
        
        const birthDate = new Date(value);
        const today = new Date();
        
        // Kiểm tra ngày sinh không được trong tương lai
        if (birthDate > today) {
          return 'Ngày sinh không được trong tương lai';
        }
        
        // Kiểm tra ngày sinh không được quá xa trong quá khứ
        if (birthDate.getFullYear() < 1900) {
          return 'Ngày sinh không hợp lệ (trước năm 1900)';
        }
        
        // Tính tuổi chính xác
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
        
        // Kiểm tra ký tự đặc biệt không hợp lệ
        if (/[<>"']/.test(value)) {
          return 'Địa chỉ không được chứa ký tự đặc biệt: < > " \'';
        }
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
        
        // Kiểm tra ký tự đặc biệt không hợp lệ
        if (/[<>"']/.test(value)) {
          return 'Trường này không được chứa ký tự đặc biệt: < > " \'';
        }
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