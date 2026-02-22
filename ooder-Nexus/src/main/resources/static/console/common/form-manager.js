/**
 * 表单管理器
 * 抽象表单处理功能，包括验证、提交等
 */
function FormManager(options = {}) {
    this.fields = options.fields || {};
    this.validators = options.validators || {};
    this.errors = {};
    this.submitting = false;
    this.success = false;
    this.message = '';
    this.callbacks = {
        onSubmit: options.onSubmit || null,
        onSuccess: options.onSuccess || null,
        onError: options.onError || null,
        onValidationError: options.onValidationError || null
    };
}

/**
 * 设置字段值
 * @param {string} field - 字段名
 * @param {any} value - 字段值
 */
FormManager.prototype.setField = function(field, value) {
    this.fields[field] = value;
    this.clearError(field);
};

/**
 * 获取字段值
 * @param {string} field - 字段名
 * @returns {any} - 字段值
 */
FormManager.prototype.getField = function(field) {
    return this.fields[field];
};

/**
 * 获取所有字段
 * @returns {Object} - 所有字段
 */
FormManager.prototype.getAllFields = function() {
    return this.fields;
};

/**
 * 验证表单
 * @returns {boolean} - 是否验证通过
 */
FormManager.prototype.validate = function() {
    this.errors = {};
    let isValid = true;

    for (const field in this.validators) {
        const validator = this.validators[field];
        const value = this.fields[field];

        if (typeof validator === 'function') {
            const error = validator(value, this.fields);
            if (error) {
                this.errors[field] = error;
                isValid = false;
            }
        } else if (typeof validator === 'object') {
            if (validator.required && !this._isValuePresent(value)) {
                this.errors[field] = validator.message || `${field} is required`;
                isValid = false;
            } else if (validator.pattern && value) {
                const regex = new RegExp(validator.pattern);
                if (!regex.test(value)) {
                    this.errors[field] = validator.message || `${field} is invalid`;
                    isValid = false;
                }
            } else if (validator.min && value < validator.min) {
                this.errors[field] = validator.message || `${field} must be at least ${validator.min}`;
                isValid = false;
            } else if (validator.max && value > validator.max) {
                this.errors[field] = validator.message || `${field} must be at most ${validator.max}`;
                isValid = false;
            } else if (validator.minLength && value && value.length < validator.minLength) {
                this.errors[field] = validator.message || `${field} must be at least ${validator.minLength} characters`;
                isValid = false;
            } else if (validator.maxLength && value && value.length > validator.maxLength) {
                this.errors[field] = validator.message || `${field} must be at most ${validator.maxLength} characters`;
                isValid = false;
            }
        }
    }

    if (!isValid && this.callbacks.onValidationError) {
        this.callbacks.onValidationError(this.errors);
    }

    return isValid;
};

/**
 * 提交表单
 * @param {Object} options - 提交选项
 * @returns {Promise<any>} - 提交结果
 */
FormManager.prototype.submit = async function(options = {}) {
    if (!this.validate()) {
        throw new Error('Validation failed');
    }

    this.submitting = true;
    this.success = false;
    this.message = '';

    try {
        let result;
        if (this.callbacks.onSubmit) {
            result = await this.callbacks.onSubmit(this.fields, options);
        } else {
            // 默认提交逻辑
            result = await this._defaultSubmit(options);
        }

        this.success = true;
        this.message = 'Submit successful';
        
        if (this.callbacks.onSuccess) {
            this.callbacks.onSuccess(result, this.fields);
        }

        return result;
    } catch (error) {
        this.success = false;
        this.message = error.message || 'Submit failed';
        
        if (this.callbacks.onError) {
            this.callbacks.onError(error, this.fields);
        }

        throw error;
    } finally {
        this.submitting = false;
    }
};

/**
 * 重置表单
 * @param {Object} defaultValues - 默认值
 */
FormManager.prototype.reset = function(defaultValues = {}) {
    this.fields = { ...defaultValues };
    this.errors = {};
    this.success = false;
    this.message = '';
};

/**
 * 清除错误
 * @param {string} field - 字段名
 */
FormManager.prototype.clearError = function(field) {
    if (this.errors[field]) {
        delete this.errors[field];
    }
};

/**
 * 获取错误
 * @param {string} field - 字段名
 * @returns {string|null} - 错误信息
 */
FormManager.prototype.getError = function(field) {
    return this.errors[field] || null;
};

/**
 * 获取所有错误
 * @returns {Object} - 所有错误
 */
FormManager.prototype.getAllErrors = function() {
    return this.errors;
};

/**
 * 是否正在提交
 * @returns {boolean} - 提交状态
 */
FormManager.prototype.isSubmitting = function() {
    return this.submitting;
};

/**
 * 是否提交成功
 * @returns {boolean} - 提交状态
 */
FormManager.prototype.isSuccess = function() {
    return this.success;
};

/**
 * 获取消息
 * @returns {string} - 消息
 */
FormManager.prototype.getMessage = function() {
    return this.message;
};

/**
 * 设置回调函数
 * @param {Object} callbacks - 回调函数
 */
FormManager.prototype.setCallbacks = function(callbacks) {
    this.callbacks = {
        ...this.callbacks,
        ...callbacks
    };
};

/**
 * 设置验证器
 * @param {Object} validators - 验证器
 */
FormManager.prototype.setValidators = function(validators) {
    this.validators = validators;
};

/**
 * 检查值是否存在
 * @private
 * @param {any} value - 字段值
 * @returns {boolean} - 是否存在
 */
FormManager.prototype._isValuePresent = function(value) {
    if (value === null || value === undefined) {
        return false;
    }
    if (typeof value === 'string') {
        return value.trim() !== '';
    }
    if (Array.isArray(value)) {
        return value.length > 0;
    }
    return true;
};

/**
 * 默认提交逻辑
 * @private
 * @param {Object} options - 提交选项
 * @returns {Promise<any>} - 提交结果
 */
FormManager.prototype._defaultSubmit = async function(options = {}) {
    const { url, method = 'POST', resource, operation } = options;

    if (url) {
        // 自定义URL提交
        return await apiClient.request(url, {
            method,
            body: JSON.stringify(this.fields)
        });
    } else if (resource) {
        // 批量API提交
        if (operation === 'create') {
            return await apiClient.batchCreate({
                resource,
                items: [this.fields]
            });
        } else if (operation === 'update') {
            return await apiClient.batchUpdate({
                resource,
                items: [this.fields]
            });
        }
    }

    throw new Error('No submit method specified');
};

// 全局导出
if (typeof window !== 'undefined') {
    window.FormManager = FormManager;
}
