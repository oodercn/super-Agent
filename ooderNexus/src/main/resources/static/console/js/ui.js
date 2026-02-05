/**
 * UI交互模块
 */

/**
 * 显示加载状态
 * @param {string} message 加载消息
 */
function showLoading(message = '加载中...') {
    const loadingOverlay = document.getElementById('loading-overlay');
    const loadingText = document.getElementById('loading-text');
    
    if (loadingText) {
        loadingText.textContent = message;
    }
    
    if (loadingOverlay) {
        loadingOverlay.classList.add('active');
    }
}

/**
 * 隐藏加载状态
 */
function hideLoading() {
    const loadingOverlay = document.getElementById('loading-overlay');
    if (loadingOverlay) {
        loadingOverlay.classList.remove('active');
    }
}

/**
 * 显示错误提示
 * @param {string} message 错误消息
 */
function showErrorToast(message) {
    const errorToast = document.getElementById('error-toast');
    const errorMessage = document.getElementById('error-message');
    
    if (errorMessage) {
        errorMessage.textContent = message;
    }
    
    if (errorToast) {
        errorToast.classList.add('active');
        setTimeout(hideErrorToast, 3000);
    }
}

/**
 * 隐藏错误提示
 */
function hideErrorToast() {
    const errorToast = document.getElementById('error-toast');
    if (errorToast) {
        errorToast.classList.remove('active');
    }
}

/**
 * 显示成功提示
 * @param {string} message 成功消息
 */
function showSuccessToast(message) {
    const successToast = document.getElementById('success-toast');
    const successMessage = document.getElementById('success-message');
    
    if (successMessage) {
        successMessage.textContent = message;
    }
    
    if (successToast) {
        successToast.classList.add('active');
        setTimeout(hideSuccessToast, 3000);
    }
}

/**
 * 显示提示消息（通用）
 * @param {string} message 提示消息
 * @param {string} type 消息类型：success/error/warning/info
 */
function showToast(message, type = 'info') {
    switch(type) {
        case 'success':
            showSuccessToast(message);
            break;
        case 'error':
            showErrorToast(message);
            break;
        case 'warning':
            showErrorToast(message);
            break;
        default:
            showSuccessToast(message);
    }
}

/**
 * 隐藏成功提示
 */
function hideSuccessToast() {
    const successToast = document.getElementById('success-toast');
    if (successToast) {
        successToast.classList.remove('active');
    }
}

// 导出模块
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        showLoading,
        hideLoading,
        showErrorToast,
        hideErrorToast,
        showSuccessToast,
        hideSuccessToast,
        showToast
    };
}
