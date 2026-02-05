// 配置文件
var config = {
    // API配置
    api: {
        baseUrl: '/api'
    },
    // 主题配置
    theme: {
        default: 'dark'
    },
    // 其他配置
    debug: true
};

// 全局变量
var currProjectName = 'ooder';
var domainId = 'ooder';

// 图标配置
if (!ood) var ood = {};
ood.builtinFontIcon = {
    'ri-paint-brush-line': true,
    'ri-home': true,
    'ri-menu': true,
    'ri-settings': true,
    'ri-user': true,
    'ri-logout': true,
    'ri-plus': true,
    'ri-minus': true,
    'ri-edit': true,
    'ri-delete': true,
    'ri-save': true,
    'ri-search': true,
    'ri-refresh': true,
    'ri-arrow-left': true,
    'ri-arrow-right': true,
    'ri-arrow-up': true,
    'ri-arrow-down': true,
    'ri-check': true,
    'ri-close': true,
    'ri-alert': true,
    'ri-info': true,
    'ri-success': true,
    'ri-error': true
};

// 首先修复基础的JavaScript方法，确保它们在任何代码执行前可用

// 修复Object.entries方法
if (!Object.entries) {
    Object.entries = function(obj) {
        var ownProps = Object.keys(obj),
            i = ownProps.length,
            resArray = new Array(i);
        while (i--) {
            resArray[i] = [ownProps[i], obj[ownProps[i]]];
        }
        return resArray;
    };
}

// 修复Array.prototype.map方法
if (!Array.prototype.map) {
    Array.prototype.map = function(callback, thisArg) {
        var T, A, k;
        if (this == null) {
            throw new TypeError('this is null or not defined');
        }
        var O = Object(this);
        var len = O.length >>> 0;
        if (typeof callback !== 'function') {
            throw new TypeError(callback + ' is not a function');
        }
        if (arguments.length > 1) {
            T = thisArg;
        }
        A = new Array(len);
        k = 0;
        while (k < len) {
            var kValue, mappedValue;
            if (k in O) {
                kValue = O[k];
                mappedValue = callback.call(T, kValue, k, O);
                A[k] = mappedValue;
            }
            k++;
        }
        return A;
    };
}

// 修复Array.prototype.filter方法
if (!Array.prototype.filter) {
    Array.prototype.filter = function(callback, thisArg) {
        var T, A, k;
        if (this == null) {
            throw new TypeError('this is null or not defined');
        }
        var O = Object(this);
        var len = O.length >>> 0;
        if (typeof callback !== 'function') {
            throw new TypeError(callback + ' is not a function');
        }
        if (arguments.length > 1) {
            T = thisArg;
        }
        A = [];
        k = 0;
        while (k < len) {
            var kValue;
            if (k in O) {
                kValue = O[k];
                if (callback.call(T, kValue, k, O)) {
                    A.push(kValue);
                }
            }
            k++;
        }
        return A;
    };
}

// 修复getThemeColors函数
if (typeof getThemeColors === 'undefined') {
    function getThemeColors() {
        // 直接返回颜色对象，不添加额外方法
        return {
            primary: '#3b82f6',
            secondary: '#64748b',
            success: '#10b981',
            warning: '#f59e0b',
            error: '#ef4444',
            dark: '#1e293b',
            light: '#f8fafc'
        };
    }
}

// 重写getThemeColors函数，确保它返回正确的格式
window.getThemeColors = function() {
    var colors = {
        primary: '#3b82f6',
        secondary: '#64748b',
        success: '#10b981',
        warning: '#f59e0b',
        error: '#ef4444',
        dark: '#1e293b',
        light: '#f8fafc'
    };
    return colors;
};

// 修复ood.ready函数
if (typeof ood.ready === 'undefined') {
    ood.ready = function(callback) {
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', callback);
        } else {
            callback();
        }
    };
}
