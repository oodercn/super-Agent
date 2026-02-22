/**
 * 模态框管理模块
 */

import { showNotification } from './utils.js';

/**
 * 模态框管理类
 */
export class ModalManager {
    /**
     * 创建并显示模态框
     * @param {Object} options - 配置选项
     * @returns {string} 模态框ID
     */
    static createModal(options = {}) {
        const modalId = options.id || `modal-${Date.now()}`;
        const title = options.title || '提示';
        const content = options.content || '';
        const size = options.size || 'medium'; // small, medium, large
        const showClose = options.showClose !== false;
        const showFooter = options.showFooter !== false;
        const confirmText = options.confirmText || '确定';
        const cancelText = options.cancelText || '取消';
        const onConfirm = options.onConfirm;
        const onCancel = options.onCancel;
        const onClose = options.onClose;

        // 模态框大小样式
        const sizeClasses = {
            small: 'modal-small',
            medium: 'modal-medium',
            large: 'modal-large'
        };

        // 创建模态框HTML
        const modalHTML = `
            <div id="${modalId}" class="modal">
                <div class="modal-overlay"></div>
                <div class="modal-content ${sizeClasses[size]}">
                    <div class="modal-header">
                        <h3>${title}</h3>
                        ${showClose ? `<button class="modal-close">&times;</button>` : ''}
                    </div>
                    <div class="modal-body">
                        ${content}
                    </div>
                    ${showFooter ? `
                        <div class="modal-footer">
                            <button class="modal-cancel">${cancelText}</button>
                            <button class="modal-confirm">${confirmText}</button>
                        </div>
                    ` : ''}
                </div>
            </div>
        `;

        // 添加模态框到页面
        document.body.insertAdjacentHTML('beforeend', modalHTML);

        // 获取模态框元素
        const modal = document.getElementById(modalId);
        const overlay = modal.querySelector('.modal-overlay');
        const closeBtn = modal.querySelector('.modal-close');
        const cancelBtn = modal.querySelector('.modal-cancel');
        const confirmBtn = modal.querySelector('.modal-confirm');

        // 添加样式
        ModalManager.addModalStyles();

        // 显示模态框
        setTimeout(() => {
            modal.classList.add('show');
        }, 10);

        // 添加事件监听器
        const handleClose = () => {
            ModalManager.closeModal(modalId);
            if (onClose) onClose();
        };

        const handleCancel = (e) => {
            e.stopPropagation();
            if (onCancel) {
                if (onCancel() !== false) {
                    handleClose();
                }
            } else {
                handleClose();
            }
        };

        const handleConfirm = (e) => {
            e.stopPropagation();
            if (onConfirm) {
                if (onConfirm() !== false) {
                    handleClose();
                }
            } else {
                handleClose();
            }
        };

        // 关闭按钮事件
        if (closeBtn) {
            closeBtn.addEventListener('click', handleClose);
        }

        // 取消按钮事件
        if (cancelBtn) {
            cancelBtn.addEventListener('click', handleCancel);
        }

        // 确定按钮事件
        if (confirmBtn) {
            confirmBtn.addEventListener('click', handleConfirm);
        }

        // 点击遮罩关闭
        if (overlay) {
            overlay.addEventListener('click', handleClose);
        }

        // 按ESC键关闭
        const handleEsc = (e) => {
            if (e.key === 'Escape') {
                handleClose();
                document.removeEventListener('keydown', handleEsc);
            }
        };

        document.addEventListener('keydown', handleEsc);

        return modalId;
    }

    /**
     * 关闭模态框
     * @param {string} modalId - 模态框ID
     */
    static closeModal(modalId) {
        const modal = document.getElementById(modalId);
        if (!modal) return;

        modal.classList.remove('show');

        // 动画结束后移除模态框
        setTimeout(() => {
            modal.remove();
        }, 300);
    }

    /**
     * 显示确认模态框
     * @param {Object} options - 配置选项
     * @returns {string} 模态框ID
     */
    static confirm(options = {}) {
        return ModalManager.createModal({
            title: options.title || '确认',
            content: options.content || '确定要执行此操作吗？',
            confirmText: options.confirmText || '确定',
            cancelText: options.cancelText || '取消',
            onConfirm: options.onConfirm,
            onCancel: options.onCancel,
            onClose: options.onClose
        });
    }

    /**
     * 显示提示模态框
     * @param {Object} options - 配置选项
     * @returns {string} 模态框ID
     */
    static alert(options = {}) {
        return ModalManager.createModal({
            title: options.title || '提示',
            content: options.content || '',
            showFooter: true,
            confirmText: options.confirmText || '确定',
            showClose: options.showClose !== false,
            onConfirm: options.onConfirm,
            onClose: options.onClose
        });
    }

    /**
     * 显示输入模态框
     * @param {Object} options - 配置选项
     * @returns {string} 模态框ID
     */
    static prompt(options = {}) {
        const inputId = `prompt-input-${Date.now()}`;
        const content = `
            <div class="modal-input">
                <label for="${inputId}">${options.label || '请输入'}:</label>
                <input type="${options.type || 'text'}" id="${inputId}" name="${options.name || 'input'}" value="${options.value || ''}" placeholder="${options.placeholder || ''}">
            </div>
        `;

        return ModalManager.createModal({
            title: options.title || '输入',
            content: content,
            confirmText: options.confirmText || '确定',
            cancelText: options.cancelText || '取消',
            onConfirm: () => {
                const input = document.getElementById(inputId);
                const value = input ? input.value : '';
                if (options.onConfirm) {
                    return options.onConfirm(value);
                }
            },
            onCancel: options.onCancel,
            onClose: options.onClose
        });
    }

    /**
     * 添加模态框样式
     */
    static addModalStyles() {
        // 检查是否已添加样式
        if (document.getElementById('modal-styles')) {
            return;
        }

        // 创建样式元素
        const style = document.createElement('style');
        style.id = 'modal-styles';
        style.textContent = `
            .modal {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                z-index: 10000;
                display: flex;
                align-items: center;
                justify-content: center;
                opacity: 0;
                pointer-events: none;
                transition: opacity 0.3s ease;
            }

            .modal.show {
                opacity: 1;
                pointer-events: auto;
            }

            .modal-overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                backdrop-filter: blur(2px);
            }

            .modal-content {
                position: relative;
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
                max-height: 80vh;
                overflow: hidden;
                transform: scale(0.9);
                transition: transform 0.3s ease;
            }

            .modal.show .modal-content {
                transform: scale(1);
            }

            .modal-small {
                width: 400px;
                max-width: 90vw;
            }

            .modal-medium {
                width: 600px;
                max-width: 90vw;
            }

            .modal-large {
                width: 800px;
                max-width: 90vw;
            }

            .modal-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 16px 20px;
                border-bottom: 1px solid #eaeaea;
                background-color: #f5f5f5;
            }

            .modal-header h3 {
                margin: 0;
                font-size: 18px;
                font-weight: 600;
            }

            .modal-close {
                background: none;
                border: none;
                font-size: 24px;
                cursor: pointer;
                color: #666;
                padding: 0;
                width: 24px;
                height: 24px;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 4px;
                transition: background-color 0.2s ease;
            }

            .modal-close:hover {
                background-color: #eaeaea;
            }

            .modal-body {
                padding: 20px;
                max-height: 60vh;
                overflow-y: auto;
            }

            .modal-footer {
                display: flex;
                align-items: center;
                justify-content: flex-end;
                padding: 16px 20px;
                border-top: 1px solid #eaeaea;
                background-color: #f5f5f5;
                gap: 10px;
            }

            .modal-footer button {
                padding: 8px 16px;
                border: 1px solid #ddd;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
                transition: all 0.2s ease;
            }

            .modal-cancel {
                background-color: #fff;
                color: #666;
            }

            .modal-cancel:hover {
                background-color: #f5f5f5;
            }

            .modal-confirm {
                background-color: #2196F3;
                color: #fff;
                border-color: #2196F3;
            }

            .modal-confirm:hover {
                background-color: #1976D2;
                border-color: #1976D2;
            }

            .modal-input {
                margin-bottom: 0;
            }

            .modal-input label {
                display: block;
                margin-bottom: 8px;
                font-weight: 500;
            }

            .modal-input input {
                width: 100%;
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }

            .modal-input input:focus {
                outline: none;
                border-color: #2196F3;
                box-shadow: 0 0 0 2px rgba(33, 150, 243, 0.1);
            }

            /* 响应式设计 */
            @media (max-width: 768px) {
                .modal-small,
                .modal-medium,
                .modal-large {
                    width: 90vw;
                    max-width: 90vw;
                }
            }
        `;

        // 添加样式到页面
        document.head.appendChild(style);
    }

    /**
     * 关闭所有模态框
     */
    static closeAllModals() {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(modal => {
            const modalId = modal.id;
            ModalManager.closeModal(modalId);
        });
    }
}