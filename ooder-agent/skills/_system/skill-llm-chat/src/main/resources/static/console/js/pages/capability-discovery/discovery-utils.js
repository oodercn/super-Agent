(function(global) {
'use strict';

var DiscoveryState = {
    currentMethod: null,
    isScanning: false,
    discoveredCapabilities: [],
    logs: [],
    scanStats: { scanned: 0, found: 0, new: 0, installed: 0 },
    selectedBusinessCategory: null,
    currentInstallCap: null,
    currentInstallStep: 1,
    installSteps: [1, 5, 7, 8],
    selectedCapabilities: [],
    batchMode: false,
    DISCOVERY_METHODS: [],
    BUSINESS_CATEGORIES: []
};

var DiscoveryUtils = {
    getState: function() {
        return DiscoveryState;
    },
    
    resetState: function() {
        DiscoveryState.currentMethod = null;
        DiscoveryState.isScanning = false;
        DiscoveryState.discoveredCapabilities = [];
        DiscoveryState.logs = [];
        DiscoveryState.scanStats = { scanned: 0, found: 0, new: 0, installed: 0 };
        DiscoveryState.selectedBusinessCategory = null;
        DiscoveryState.currentInstallCap = null;
        DiscoveryState.currentInstallStep = 1;
        DiscoveryState.installSteps = [1, 5, 7, 8];
        DiscoveryState.selectedCapabilities = [];
        DiscoveryState.batchMode = false;
    },
    
    addLog: function(level, message) {
        var log = { level: level, message: message, time: new Date() };
        DiscoveryState.logs.unshift(log);
        if (DiscoveryState.logs.length > 100) {
            DiscoveryState.logs.pop();
        }
        
        var container = document.getElementById('logsBody');
        if (container) {
            var item = document.createElement('div');
            item.className = 'log-item log-' + level;
            item.innerHTML = '<span class="log-time">' + log.time.toLocaleTimeString() + '</span><span class="log-message">' + message + '</span>';
            container.insertBefore(item, container.firstChild);
            if (container.children.length > 50) {
                container.removeChild(container.lastChild);
            }
        }
        
        if (level === 'error') {
            DiscoveryUtils.showToast(message, 'error');
        } else if (level === 'success') {
            DiscoveryUtils.showToast(message, 'success');
        }
    },
    
    showToast: function(message, type) {
        type = type || 'info';
        var existing = document.querySelector('.nx-notification');
        if (existing) existing.remove();
        
        var notification = document.createElement('div');
        notification.className = 'nx-notification nx-notification--' + type;
        
        var icon = '';
        if (type === 'error') {
            icon = '<i class="ri-error-warning-line" style="margin-right: 8px;"></i>';
        } else if (type === 'success') {
            icon = '<i class="ri-checkbox-circle-line" style="margin-right: 8px;"></i>';
        } else if (type === 'warning') {
            icon = '<i class="ri-alert-line" style="margin-right: 8px;"></i>';
        } else {
            icon = '<i class="ri-information-line" style="margin-right: 8px;"></i>';
        }
        
        notification.innerHTML = icon + message;
        document.body.appendChild(notification);
        
        setTimeout(function() {
            notification.style.opacity = '0';
            notification.style.transform = 'translateX(100%)';
            setTimeout(function() { notification.remove(); }, 300);
        }, 5000);
    },
    
    getCategoryInfo: function(skillForm, sceneType) {
        if (skillForm === 'SCENE') {
            if (sceneType === 'AUTO') {
                return { code: 'SCENE_AUTO', name: '自主场景', icon: 'ri-robot-line' };
            } else if (sceneType === 'TRIGGER') {
                return { code: 'SCENE_TRIGGER', name: '触发场景', icon: 'ri-hand-coin-line' };
            } else if (sceneType === 'HYBRID') {
                return { code: 'SCENE_HYBRID', name: '混合场景', icon: 'ri-shuffle-line' };
            }
            return { code: 'SCENE', name: '场景应用', icon: 'ri-layout-grid-line' };
        } else if (skillForm === 'PROVIDER') {
            return { code: 'PROVIDER', name: '能力服务', icon: 'ri-cpu-line' };
        } else if (skillForm === 'DRIVER') {
            return { code: 'DRIVER', name: '驱动适配', icon: 'ri-steering-line' };
        }
        return { code: 'PROVIDER', name: '能力服务', icon: 'ri-cpu-line' };
    },
    
    getBusinessCategoryInfo: function(bc) {
        if (typeof CategoryService !== 'undefined' && CategoryService.getInfo) {
            return CategoryService.getInfo(bc);
        }
        return { name: bc || '-', color: '#8c8c8c' };
    }
};

global.DiscoveryState = DiscoveryState;
global.DiscoveryUtils = DiscoveryUtils;

})(typeof window !== 'undefined' ? window : this);
