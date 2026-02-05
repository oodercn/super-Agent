/**
 * 模态框管理器
 * 统一管理模态框的显示、隐藏和交互
 */
function ModalManager(options = {}) {
    this.modals = options.modals || {};
    this.activeModal = null;
    this.callbacks = {
        onOpen: options.onOpen || null,
        onClose: options.onClose || null,
        onBeforeOpen: options.onBeforeOpen || null,
        onAfterClose: options.onAfterClose || null
    };
    this.overlay = null;
    this.backdrop = true;
    this.closeOnBackdrop = true;
    this.closeOnEscape = true;
}

/**
 * 注册模态框
 * @param {string} id - 模态框ID
 * @param {Object} config - 配置项
 */
ModalManager.prototype.register = function(id, config = {}) {
    this.modals[id] = {
        element: document.getElementById(id),
        config: {
            backdrop: config.backdrop !== undefined ? config.backdrop : this.backdrop,
            closeOnBackdrop: config.closeOnBackdrop !== undefined ? config.closeOnBackdrop : this.closeOnBackdrop,
            closeOnEscape: config.closeOnEscape !== undefined ? config.closeOnEscape : this.closeOnEscape,
            ...config
        }
    };
    
    if (this.modals[id].element) {
        this._setupModal(id);
    }
};

/**
 * 显示模态框
 * @param {string} id - 模态框ID
 * @param {Object} data - 传递的数据
 */
ModalManager.prototype.open = function(id, data = {}) {
    const modal = this.modals[id];
    
    if (!modal || !modal.element) {
        console.error(`Modal "${id}" not found`);
        return;
    }
    
    if (this.callbacks.onBeforeOpen) {
        const result = this.callbacks.onBeforeOpen(id, data);
        if (result === false) {
            return;
        }
    }
    
    this.activeModal = id;
    modal.element.style.display = 'block';
    
    this._createBackdrop();
    this._setupEventListeners(id);
    
    if (this.callbacks.onOpen) {
        this.callbacks.onOpen(id, data);
    }
};

/**
 * 关闭模态框
 * @param {string} id - 模态框ID
 */
ModalManager.prototype.close = function(id) {
    const modalId = id || this.activeModal;
    const modal = this.modals[modalId];
    
    if (!modal || !modal.element) {
        return;
    }
    
    modal.element.style.display = 'none';
    this._removeBackdrop();
    this._removeEventListeners(modalId);
    
    if (this.callbacks.onClose) {
        this.callbacks.onClose(modalId);
    }
    
    if (this.callbacks.onAfterClose) {
        setTimeout(() => {
            this.callbacks.onAfterClose(modalId);
        }, 300);
    }
    
    this.activeModal = null;
};

/**
 * 切换模态框
 * @param {string} id - 模态框ID
 * @param {Object} data - 传递的数据
 */
ModalManager.prototype.toggle = function(id, data = {}) {
    if (this.activeModal === id) {
        this.close(id);
    } else {
        if (this.activeModal) {
            this.close(this.activeModal);
        }
        this.open(id, data);
    }
};

/**
 * 关闭所有模态框
 */
ModalManager.prototype.closeAll = function() {
    for (const id in this.modals) {
        this.close(id);
    }
};

/**
 * 是否有打开的模态框
 * @returns {boolean}
 */
ModalManager.prototype.isOpen = function() {
    return this.activeModal !== null;
};

/**
 * 获取当前打开的模态框ID
 * @returns {string|null}
 */
ModalManager.prototype.getActiveModal = function() {
    return this.activeModal;
};

/**
 * 设置回调函数
 * @param {Object} callbacks - 回调函数
 */
ModalManager.prototype.setCallbacks = function(callbacks) {
    this.callbacks = {
        ...this.callbacks,
        ...callbacks
    };
};

/**
 * 设置全局配置
 * @param {Object} config - 配置项
 */
ModalManager.prototype.setConfig = function(config) {
    if (config.backdrop !== undefined) {
        this.backdrop = config.backdrop;
    }
    if (config.closeOnBackdrop !== undefined) {
        this.closeOnBackdrop = config.closeOnBackdrop;
    }
    if (config.closeOnEscape !== undefined) {
        this.closeOnEscape = config.closeOnEscape;
    }
};

/**
 * 设置模态框配置
 * @param {string} id - 模态框ID
 * @param {Object} config - 配置项
 */
ModalManager.prototype.setModalConfig = function(id, config) {
    if (this.modals[id]) {
        this.modals[id].config = {
            ...this.modals[id].config,
            ...config
        };
    }
};

/**
 * 设置模态框内容
 * @param {string} id - 模态框ID
 * @param {string} content - HTML内容
 */
ModalManager.prototype.setContent = function(id, content) {
    const modal = this.modals[id];
    if (modal && modal.element) {
        const contentElement = modal.element.querySelector('.modal-body');
        if (contentElement) {
            contentElement.innerHTML = content;
        }
    }
};

/**
 * 获取模态框内容
 * @param {string} id - 模态框ID
 * @returns {string} - HTML内容
 */
ModalManager.prototype.getContent = function(id) {
    const modal = this.modals[id];
    if (modal && modal.element) {
        const contentElement = modal.element.querySelector('.modal-body');
        if (contentElement) {
            return contentElement.innerHTML;
        }
    }
    return '';
};

/**
 * 重置模态框表单
 * @param {string} id - 模态框ID
 */
ModalManager.prototype.resetForm = function(id) {
    const modal = this.modals[id];
    if (modal && modal.element) {
        const form = modal.element.querySelector('form');
        if (form) {
            form.reset();
        }
    }
};

/**
 * 获取表单数据
 * @param {string} id - 模态框ID
 * @returns {Object} - 表单数据
 */
ModalManager.prototype.getFormData = function(id) {
    const modal = this.modals[id];
    if (modal && modal.element) {
        const form = modal.element.querySelector('form');
        if (form) {
            const formData = new FormData(form);
            const data = {};
            formData.forEach((value, key) => {
                data[key] = value;
            });
            return data;
        }
    }
    return {};
};

/**
 * 设置回调函数
 * @private
 * @param {string} id - 模态框ID
 */
ModalManager.prototype._setupModal = function(id) {
    const modal = this.modals[id];
    if (!modal.element) return;
    
    const closeBtn = modal.element.querySelector('.modal-close');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            this.close(id);
        });
    }
};

/**
     * 创建背景遮罩
     * @private
     */
    ModalManager.prototype._createBackdrop = function() {
        if (!this.backdrop) return;
        
        // 不创建新的背景遮罩，因为.modal类本身已经有背景样式
        // 只需要确保模态框内容可以点击，不会触发背景的关闭事件
        const modal = this.modals[this.activeModal];
        if (modal && modal.element) {
            const modalContent = modal.element.querySelector('.modal-content');
            if (modalContent) {
                modalContent.addEventListener('click', (e) => {
                    e.stopPropagation();
                });
            }
        }
    };

/**
     * 移除背景遮罩
     * @private
     */
    ModalManager.prototype._removeBackdrop = function() {
        // 不执行任何操作，因为没有创建背景遮罩
    };

/**
 * 设置事件监听器
 * @private
 * @param {string} id - 模态框ID
 */
ModalManager.prototype._setupEventListeners = function(id) {
    const modal = this.modals[id];
    if (!modal) return;
    
    if (modal.config.closeOnBackdrop && this.overlay) {
        this.overlay.addEventListener('click', () => {
            this.close(id);
        });
    }
    
    if (modal.config.closeOnEscape) {
        const escapeHandler = (e) => {
            if (e.key === 'Escape' && this.activeModal === id) {
                this.close(id);
            }
        };
        document.addEventListener('keydown', escapeHandler);
        this.escapeHandler = escapeHandler;
    }
};

/**
 * 移除事件监听器
 * @private
 * @param {string} id - 模态框ID
 */
ModalManager.prototype._removeEventListeners = function(id) {
    if (this.escapeHandler) {
        document.removeEventListener('keydown', this.escapeHandler);
        this.escapeHandler = null;
    }
};

if (typeof window !== 'undefined') {
    window.ModalManager = ModalManager;
}
