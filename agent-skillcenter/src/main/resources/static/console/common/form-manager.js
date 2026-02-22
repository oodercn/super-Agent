/**
 * 表单管理模块
 */

import { showNotification } from './utils.js';

/**
 * 表单管理类
 */
export class FormManager {
    /**
     * 初始化表单
     * @param {string} formSelector - 表单选择器
     * @param {Object} options - 配置选项
     */
    static init(formSelector, options = {}) {
        const form = document.querySelector(formSelector);
        if (!form) return;

        // 添加表单提交事件
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            if (options.beforeSubmit && !options.beforeSubmit()) {
                return;
            }

            try {
                const formData = FormManager.getFormData(form);
                
                if (options.validate && !FormManager.validate(formData, options.validate)) {
                    return;
                }

                if (options.onSubmit) {
                    await options.onSubmit(formData);
                }
            } catch (error) {
                console.error('Form submission error:', error);
                showNotification('表单提交失败', 'error');
            }
        });

        // 添加表单验证事件
        if (options.validate) {
            const inputs = form.querySelectorAll('input, select, textarea');
            inputs.forEach(input => {
                input.addEventListener('blur', () => {
                    const formData = FormManager.getFormData(form);
                    FormManager.validate(formData, options.validate);
                });
            });
        }
    }

    /**
     * 获取表单数据
     * @param {HTMLFormElement} form - 表单元素
     * @returns {Object} 表单数据
     */
    static getFormData(form) {
        const formData = {};
        const inputs = form.querySelectorAll('input, select, textarea');
        
        inputs.forEach(input => {
            if (input.name) {
                if (input.type === 'checkbox') {
                    formData[input.name] = input.checked;
                } else if (input.type === 'radio') {
                    if (input.checked) {
                        formData[input.name] = input.value;
                    }
                } else {
                    formData[input.name] = input.value;
                }
            }
        });
        
        return formData;
    }

    /**
     * 验证表单数据
     * @param {Object} formData - 表单数据
     * @param {Object} rules - 验证规则
     * @returns {boolean} 是否验证通过
     */
    static validate(formData, rules) {
        let isValid = true;
        
        Object.keys(rules).forEach(field => {
            const value = formData[field];
            const fieldRules = rules[field];
            
            // 检查必填
            if (fieldRules.required && !value) {
                FormManager.showError(field, fieldRules.message || '此字段为必填项');
                isValid = false;
            }
            
            // 检查最小长度
            if (fieldRules.minLength && value.length < fieldRules.minLength) {
                FormManager.showError(field, fieldRules.message || `最少需要${fieldRules.minLength}个字符`);
                isValid = false;
            }
            
            // 检查最大长度
            if (fieldRules.maxLength && value.length > fieldRules.maxLength) {
                FormManager.showError(field, fieldRules.message || `最多只能有${fieldRules.maxLength}个字符`);
                isValid = false;
            }
            
            // 检查正则表达式
            if (fieldRules.pattern && !fieldRules.pattern.test(value)) {
                FormManager.showError(field, fieldRules.message || '格式不正确');
                isValid = false;
            }
            
            // 检查自定义验证
            if (fieldRules.validate && !fieldRules.validate(value)) {
                FormManager.showError(field, fieldRules.message || '验证失败');
                isValid = false;
            }
        });
        
        return isValid;
    }

    /**
     * 显示错误信息
     * @param {string} field - 字段名
     * @param {string} message - 错误信息
     */
    static showError(field, message) {
        const input = document.querySelector(`[name="${field}"]`);
        if (!input) return;
        
        // 移除旧的错误信息
        let errorElement = input.nextElementSibling;
        if (errorElement && errorElement.classList.contains('error-message')) {
            errorElement.remove();
        }
        
        // 创建新的错误信息
        errorElement = document.createElement('div');
        errorElement.className = 'error-message';
        errorElement.textContent = message;
        errorElement.style.color = '#F44336';
        errorElement.style.fontSize = '12px';
        errorElement.style.marginTop = '4px';
        
        input.parentNode.insertBefore(errorElement, input.nextSibling);
    }

    /**
     * 清除错误信息
     * @param {string} formSelector - 表单选择器
     */
    static clearErrors(formSelector) {
        const form = document.querySelector(formSelector);
        if (!form) return;
        
        const errorElements = form.querySelectorAll('.error-message');
        errorElements.forEach(element => element.remove());
    }

    /**
     * 设置表单数据
     * @param {string} formSelector - 表单选择器
     * @param {Object} data - 表单数据
     */
    static setFormData(formSelector, data) {
        const form = document.querySelector(formSelector);
        if (!form) return;
        
        Object.keys(data).forEach(field => {
            const input = form.querySelector(`[name="${field}"]`);
            if (!input) return;
            
            if (input.type === 'checkbox') {
                input.checked = data[field];
            } else if (input.type === 'radio') {
                const radio = form.querySelector(`[name="${field}"][value="${data[field]}"]`);
                if (radio) radio.checked = true;
            } else {
                input.value = data[field];
            }
        });
    }

    /**
     * 重置表单
     * @param {string} formSelector - 表单选择器
     */
    static resetForm(formSelector) {
        const form = document.querySelector(formSelector);
        if (form) {
            form.reset();
            FormManager.clearErrors(formSelector);
        }
    }
}