/**
 * 表单组件 - 封装表单功能
 */

class FormComponent {
    /**
     * 构造函数
     * @param {string} formId - 表单ID
     * @param {Object} options - 配置选项
     */
    constructor(formId, options = {}) {
        this.formId = formId;
        this.options = {
            fields: [],
            onSubmit: null,
            onReset: null,
            validation: {
                enabled: true,
                rules: {}
            },
            ...options
        };
        
        this.form = document.getElementById(formId);
        if (this.form) {
            this.bindEvents();
        }
    }

    /**
     * 绑定事件
     */
    bindEvents() {
        if (!this.form) return;
        
        // 绑定提交事件
        this.form.addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleSubmit();
        });
        
        // 绑定重置事件
        this.form.addEventListener('reset', (e) => {
            e.preventDefault();
            this.handleReset();
        });
        
        // 绑定字段验证事件
        if (this.options.validation.enabled) {
            this.options.fields.forEach(field => {
                const input = this.form.querySelector(`[name="${field.name}"]`);
                if (input) {
                    input.addEventListener('blur', () => {
                        this.validateField(field.name);
                    });
                    
                    input.addEventListener('input', () => {
                        this.clearError(field.name);
                    });
                }
            });
        }
    }

    /**
     * 处理表单提交
     */
    handleSubmit() {
        if (this.options.validation.enabled) {
            const isValid = this.validate();
            if (!isValid) {
                return;
            }
        }
        
        const formData = this.getData();
        this.options.onSubmit && this.options.onSubmit(formData);
    }

    /**
     * 处理表单重置
     */
    handleReset() {
        this.reset();
        this.options.onReset && this.options.onReset();
    }

    /**
     * 获取表单数据
     * @returns {Object} - 表单数据
     */
    getData() {
        if (!this.form) return {};
        
        const formData = {};
        
        this.options.fields.forEach(field => {
            const input = this.form.querySelector(`[name="${field.name}"]`);
            if (input) {
                switch (input.type) {
                    case 'checkbox':
                        formData[field.name] = input.checked;
                        break;
                    case 'radio':
                        const checkedRadio = this.form.querySelector(`[name="${field.name}"]:checked`);
                        formData[field.name] = checkedRadio ? checkedRadio.value : '';
                        break;
                    case 'select-multiple':
                        const selectedOptions = Array.from(input.selectedOptions);
                        formData[field.name] = selectedOptions.map(option => option.value);
                        break;
                    default:
                        formData[field.name] = input.value;
                }
            }
        });
        
        return formData;
    }

    /**
     * 设置表单数据
     * @param {Object} data - 表单数据
     */
    setData(data) {
        if (!this.form) return;
        
        this.options.fields.forEach(field => {
            const input = this.form.querySelector(`[name="${field.name}"]`);
            if (input && data[field.name] !== undefined) {
                switch (input.type) {
                    case 'checkbox':
                        input.checked = data[field.name];
                        break;
                    case 'radio':
                        const radio = this.form.querySelector(`[name="${field.name}"][value="${data[field.name]}"]`);
                        if (radio) {
                            radio.checked = true;
                        }
                        break;
                    case 'select-multiple':
                        Array.from(input.options).forEach(option => {
                            option.selected = data[field.name].includes(option.value);
                        });
                        break;
                    default:
                        input.value = data[field.name];
                }
            }
        });
    }

    /**
     * 重置表单
     */
    reset() {
        if (!this.form) return;
        
        this.options.fields.forEach(field => {
            const input = this.form.querySelector(`[name="${field.name}"]`);
            if (input) {
                switch (input.type) {
                    case 'checkbox':
                        input.checked = false;
                        break;
                    case 'radio':
                        const radio = this.form.querySelector(`[name="${field.name}"]:checked`);
                        if (radio) {
                            radio.checked = false;
                        }
                        break;
                    case 'select-multiple':
                        Array.from(input.options).forEach(option => {
                            option.selected = false;
                        });
                        break;
                    default:
                        input.value = field.defaultValue || '';
                }
            }
        });
        
        // 清除所有错误
        this.clearErrors();
    }

    /**
     * 验证表单
     * @returns {boolean} - 是否验证通过
     */
    validate() {
        let isValid = true;
        
        this.options.fields.forEach(field => {
            const fieldIsValid = this.validateField(field.name);
            if (!fieldIsValid) {
                isValid = false;
            }
        });
        
        return isValid;
    }

    /**
     * 验证单个字段
     * @param {string} fieldName - 字段名称
     * @returns {boolean} - 是否验证通过
     */
    validateField(fieldName) {
        const field = this.options.fields.find(f => f.name === fieldName);
        if (!field) return true;
        
        const input = this.form.querySelector(`[name="${fieldName}"]`);
        if (!input) return true;
        
        const value = input.value;
        const rules = field.rules || {};
        
        // 验证必填
        if (rules.required && !value) {
            this.showError(fieldName, rules.requiredMessage || '此字段为必填项');
            return false;
        }
        
        // 验证最小长度
        if (rules.minLength && value.length < rules.minLength) {
            this.showError(fieldName, rules.minLengthMessage || `最少需要${rules.minLength}个字符`);
            return false;
        }
        
        // 验证最大长度
        if (rules.maxLength && value.length > rules.maxLength) {
            this.showError(fieldName, rules.maxLengthMessage || `最多允许${rules.maxLength}个字符`);
            return false;
        }
        
        // 验证正则表达式
        if (rules.pattern && !rules.pattern.test(value)) {
            this.showError(fieldName, rules.patternMessage || '输入格式不正确');
            return false;
        }
        
        // 验证自定义规则
        if (rules.custom && typeof rules.custom === 'function') {
            const result = rules.custom(value);
            if (result !== true) {
                this.showError(fieldName, result || '输入验证失败');
                return false;
            }
        }
        
        // 验证通过，清除错误
        this.clearError(fieldName);
        return true;
    }

    /**
     * 显示错误信息
     * @param {string} fieldName - 字段名称
     * @param {string} message - 错误信息
     */
    showError(fieldName, message) {
        const input = this.form.querySelector(`[name="${fieldName}"]`);
        if (!input) return;
        
        // 清除已有的错误
        this.clearError(fieldName);
        
        // 创建错误元素
        const errorElement = document.createElement('div');
        errorElement.className = 'form-error';
        errorElement.style.color = 'var(--ooder-danger)';
        errorElement.style.fontSize = '12px';
        errorElement.style.marginTop = '4px';
        errorElement.textContent = message;
        errorElement.dataset.field = fieldName;
        
        // 插入错误元素
        input.parentNode.appendChild(errorElement);
        
        // 添加错误样式
        input.style.borderColor = 'var(--ooder-danger)';
    }

    /**
     * 清除错误信息
     * @param {string} fieldName - 字段名称
     */
    clearError(fieldName) {
        const input = this.form.querySelector(`[name="${fieldName}"]`);
        if (!input) return;
        
        // 移除错误元素
        const errorElement = input.parentNode.querySelector(`.form-error[data-field="${fieldName}"]`);
        if (errorElement) {
            errorElement.remove();
        }
        
        // 移除错误样式
        input.style.borderColor = '';
    }

    /**
     * 清除所有错误信息
     */
    clearErrors() {
        this.options.fields.forEach(field => {
            this.clearError(field.name);
        });
    }

    /**
     * 添加字段
     * @param {Object} field - 字段配置
     */
    addField(field) {
        this.options.fields.push(field);
    }

    /**
     * 移除字段
     * @param {string} fieldName - 字段名称
     */
    removeField(fieldName) {
        this.options.fields = this.options.fields.filter(field => field.name !== fieldName);
    }

    /**
     * 获取字段值
     * @param {string} fieldName - 字段名称
     * @returns {any} - 字段值
     */
    getFieldValue(fieldName) {
        const input = this.form.querySelector(`[name="${fieldName}"]`);
        if (!input) return null;
        
        switch (input.type) {
            case 'checkbox':
                return input.checked;
            case 'radio':
                const radio = this.form.querySelector(`[name="${fieldName}"]:checked`);
                return radio ? radio.value : '';
            case 'select-multiple':
                const selectedOptions = Array.from(input.selectedOptions);
                return selectedOptions.map(option => option.value);
            default:
                return input.value;
        }
    }

    /**
     * 设置字段值
     * @param {string} fieldName - 字段名称
     * @param {any} value - 字段值
     */
    setFieldValue(fieldName, value) {
        const input = this.form.querySelector(`[name="${fieldName}"]`);
        if (!input) return;
        
        switch (input.type) {
            case 'checkbox':
                input.checked = value;
                break;
            case 'radio':
                const radio = this.form.querySelector(`[name="${fieldName}"][value="${value}"]`);
                if (radio) {
                    radio.checked = true;
                }
                break;
            case 'select-multiple':
                Array.from(input.options).forEach(option => {
                    option.selected = value.includes(option.value);
                });
                break;
            default:
                input.value = value;
        }
    }

    /**
     * 禁用表单
     */
    disable() {
        if (!this.form) return;
        
        const inputs = this.form.querySelectorAll('input, select, textarea, button');
        inputs.forEach(input => {
            input.disabled = true;
        });
    }

    /**
     * 启用表单
     */
    enable() {
        if (!this.form) return;
        
        const inputs = this.form.querySelectorAll('input, select, textarea, button');
        inputs.forEach(input => {
            input.disabled = false;
        });
    }
}

// 导出表单组件
export { FormComponent };
