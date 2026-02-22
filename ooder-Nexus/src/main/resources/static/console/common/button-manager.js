/**
 * 按钮状态管理器
 * 统一管理按钮的加载、禁用、文本切换等状态
 */
function ButtonManager(options = {}) {
    this.buttons = options.buttons || {};
    this.callbacks = {
        onClick: options.onClick || null,
        onLoadingStart: options.onLoadingStart || null,
        onLoadingEnd: options.onLoadingEnd || null,
        onDisable: options.onDisable || null,
        onEnable: options.onEnable || null
    };
}

/**
 * 注册按钮
 * @param {string} id - 按钮ID
 * @param {Object} config - 配置项
 */
ButtonManager.prototype.register = function(id, config = {}) {
    const element = typeof id === 'string' ? document.getElementById(id) : id;
    
    if (!element) {
        console.error(`Button "${id}" not found`);
        return;
    }
    
    this.buttons[id] = {
        element: element,
        originalText: element.innerHTML,
        originalDisabled: element.disabled,
        loadingText: config.loadingText || '<i class="ri-loader-3-line"></i> 加载中...',
        config: {
            ...config
        }
    };
};

/**
 * 设置按钮为加载状态
 * @param {string} id - 按钮ID
 * @param {string} loadingText - 加载文本
 */
ButtonManager.prototype.setLoading = function(id, loadingText = null) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return;
    }
    
    button.element.disabled = true;
    button.element.innerHTML = loadingText || button.loadingText;
    button.element.classList.add('btn-loading');
    
    if (this.callbacks.onLoadingStart) {
        this.callbacks.onLoadingStart(id);
    }
};

/**
 * 移除按钮的加载状态
 * @param {string} id - 按钮ID
 */
ButtonManager.prototype.removeLoading = function(id) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return;
    }
    
    button.element.disabled = button.originalDisabled;
    button.element.innerHTML = button.originalText;
    button.element.classList.remove('btn-loading');
    
    if (this.callbacks.onLoadingEnd) {
        this.callbacks.onLoadingEnd(id);
    }
};

/**
 * 禁用按钮
 * @param {string} id - 按钮ID
 */
ButtonManager.prototype.disable = function(id) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return;
    }
    
    button.element.disabled = true;
    button.element.classList.add('btn-disabled');
    
    if (this.callbacks.onDisable) {
        this.callbacks.onDisable(id);
    }
};

/**
 * 启用按钮
 * @param {string} id - 按钮ID
 */
ButtonManager.prototype.enable = function(id) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return;
    }
    
    button.element.disabled = button.originalDisabled;
    button.element.classList.remove('btn-disabled');
    
    if (this.callbacks.onEnable) {
        this.callbacks.onEnable(id);
    }
};

/**
 * 切换按钮禁用状态
 * @param {string} id - 按钮ID
 */
ButtonManager.prototype.toggle = function(id) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return;
    }
    
    if (button.element.disabled) {
        this.enable(id);
    } else {
        this.disable(id);
    }
};

/**
 * 设置按钮文本
 * @param {string} id - 按钮ID
 * @param {string} text - 新文本
 */
ButtonManager.prototype.setText = function(id, text) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return;
    }
    
    button.element.innerHTML = text;
};

/**
 * 获取按钮文本
 * @param {string} id - 按钮ID
 * @returns {string} - 按钮文本
 */
ButtonManager.prototype.getText = function(id) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return '';
    }
    
    return button.element.innerHTML;
};

/**
 * 恢复按钮原始文本
 * @param {string} id - 按钮ID
 */
ButtonManager.prototype.resetText = function(id) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return;
    }
    
    button.element.innerHTML = button.originalText;
};

/**
 * 检查按钮是否被禁用
 * @param {string} id - 按钮ID
 * @returns {boolean}
 */
ButtonManager.prototype.isDisabled = function(id) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return true;
    }
    
    return button.element.disabled;
};

/**
 * 检查按钮是否在加载状态
 * @param {string} id - 按钮ID
 * @returns {boolean}
 */
ButtonManager.prototype.isLoading = function(id) {
    const button = this.buttons[id];
    
    if (!button || !button.element) {
        return false;
    }
    
    return button.element.classList.contains('btn-loading');
};

/**
 * 执行带加载状态的异步操作
 * @param {string} id - 按钮ID
 * @param {Function} asyncFn - 异步函数
 * @param {Object} options - 选项
 */
ButtonManager.prototype.executeWithLoading = async function(id, asyncFn, options = {}) {
    const { 
        loadingText, 
        onSuccess, 
        onError, 
        finally: finallyFn 
    } = options;
    
    this.setLoading(id, loadingText);
    
    try {
        const result = await asyncFn();
        
        this.removeLoading(id);
        
        if (onSuccess) {
            onSuccess(result);
        }
        
        return result;
    } catch (error) {
        this.removeLoading(id);
        
        if (onError) {
            onError(error);
        }
        
        throw error;
    } finally {
        if (finallyFn) {
            finallyFn();
        }
    }
};

/**
 * 批量设置按钮状态
 * @param {Array<string>} ids - 按钮ID数组
 * @param {string} state - 状态: 'loading', 'disabled', 'enabled'
 */
ButtonManager.prototype.batchSetState = function(ids, state) {
    ids.forEach(id => {
        switch (state) {
            case 'loading':
                this.setLoading(id);
                break;
            case 'disabled':
                this.disable(id);
                break;
            case 'enabled':
                this.enable(id);
                break;
        }
    });
};

/**
 * 批量移除按钮状态
 * @param {Array<string>} ids - 按钮ID数组
 * @param {string} state - 状态: 'loading', 'disabled'
 */
ButtonManager.prototype.batchRemoveState = function(ids, state) {
    ids.forEach(id => {
        switch (state) {
            case 'loading':
                this.removeLoading(id);
                break;
            case 'disabled':
                this.enable(id);
                break;
        }
    });
};

/**
 * 设置回调函数
 * @param {Object} callbacks - 回调函数
 */
ButtonManager.prototype.setCallbacks = function(callbacks) {
    this.callbacks = {
        ...this.callbacks,
        ...callbacks
    };
};

/**
 * 获取按钮元素
 * @param {string} id - 按钮ID
 * @returns {HTMLElement|null}
 */
ButtonManager.prototype.getElement = function(id) {
    const button = this.buttons[id];
    return button ? button.element : null;
};

/**
 * 销毁按钮管理器
 */
ButtonManager.prototype.destroy = function() {
    for (const id in this.buttons) {
        const button = this.buttons[id];
        if (button.element) {
            button.element.innerHTML = button.originalText;
            button.element.disabled = button.originalDisabled;
            button.element.classList.remove('btn-loading', 'btn-disabled');
        }
    }
    this.buttons = {};
};

if (typeof window !== 'undefined') {
    window.ButtonManager = ButtonManager;
}
