(function(global) {
'use strict';

var DiscoveryCore = {
    init: function() {
        var self = this;
        window.onPageInit = function() {
            console.log('能力发现页面初始化完成');
            self.loadDiscoveryMethods().then(function() {
                self.loadCategories().then(function() {
                    DiscoveryCore.renderMethods();
                    DiscoveryFilter.renderBusinessCategoryFilter();
                    DiscoveryFilter.initFilters();
                });
            });
        };
    },

    loadDiscoveryMethods: function() {
        return fetch('/api/v1/discovery/methods')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    DiscoveryState.DISCOVERY_METHODS = result.data;
                }
            })
            .catch(function(error) {
                console.error('加载发现方法失败:', error);
                DiscoveryState.DISCOVERY_METHODS = [];
            });
    },

    loadCategories: function() {
        return CategoryService.loadCategories().then(function(categories) {
            DiscoveryState.BUSINESS_CATEGORIES = categories;
        });
    },

    renderMethods: function() {
        var container = document.getElementById('methodList');
        if (!container) return;
        var html = '';
        var defaultMethodId = null;
        DiscoveryState.DISCOVERY_METHODS.forEach(function(method) {
            if (method.isDefault) {
                defaultMethodId = method.id;
            }
            html += '<div class="method-item' + (method.isDefault ? ' active' : '') + '" data-method="' + method.id + '" onclick="selectMethod(\'' + method.id + '\')">' +
                '<div class="method-icon" style="background: ' + method.color + '20; color: ' + method.color + ';">' +
                '<i class="' + method.icon + '"></i></div>' +
                '<div class="method-info">' +
                '<div class="method-name">' + method.name + '</div>' +
                '<div class="method-desc">' + method.desc + '</div></div>' +
                '<span class="method-badge ready" id="badge-' + method.id + '">就绪</span></div>';
        });
        container.innerHTML = html;
        if (defaultMethodId) {
            DiscoveryState.currentMethod = defaultMethodId;
            var method = DiscoveryState.DISCOVERY_METHODS.find(function(m) { return m.id === defaultMethodId; });
            if (method) {
                document.getElementById('radarTitle').textContent = method.name + ' 扫描';
            }
        }
    },

    selectMethod: function(methodId) {
        DiscoveryState.currentMethod = methodId;
        document.querySelectorAll('.method-item').forEach(function(item) {
            item.classList.remove('active');
            if (item.dataset.method === methodId) {
                item.classList.add('active');
            }
        });
        var method = DiscoveryState.DISCOVERY_METHODS.find(function(m) { return m.id === methodId; });
        if (method) {
            document.getElementById('radarTitle').textContent = method.name + ' 扫描';
            DiscoveryCore.hideConfig();
        }
    },

    showConfig: function(method) {
        var panel = document.getElementById('configPanel');
        var form = document.getElementById('configForm');
        var title = document.getElementById('configTitle');
        title.textContent = method.name + ' 配置';
        var html = '';
        method.configFields.forEach(function(field) {
            html += '<div class="form-group">' +
                '<label class="form-label">' + field.label + '</label>';
            if (field.type === 'password') {
                html += '<input type="password" class="form-input" id="config_' + field.name + '" placeholder="' + (field.placeholder || '') + '">';
            } else {
                html += '<input type="text" class="form-input" id="config_' + field.name + '" value="' + (field.default || '') + '" placeholder="' + (field.placeholder || '') + '">';
            }
            html += '</div>';
        });
        form.innerHTML = html;
        panel.classList.add('show');
    },

    hideConfig: function() {
        document.getElementById('configPanel').classList.remove('show');
    },

    startScan: function() {
        console.log('[startScan] Starting scan, currentMethod:', DiscoveryState.currentMethod);
        if (!DiscoveryState.currentMethod) {
            alert('请先选择发现途径');
            return;
        }
        if (DiscoveryState.isScanning) return;
        DiscoveryState.isScanning = true;
        DiscoveryState.discoveredCapabilities = [];
        DiscoveryState.scanStats = { scanned: 0, found: 0, new: 0, installed: 0 };
        
        var sweep = document.getElementById('radarSweep');
        if (sweep) sweep.classList.remove('idle');
        var startBtn = document.getElementById('startBtn');
        if (startBtn) startBtn.disabled = true;
        var statusText = document.getElementById('radarStatusText');
        if (statusText) statusText.textContent = '扫描中...';
        var badge = document.getElementById('badge-' + DiscoveryState.currentMethod);
        if (badge) {
            badge.textContent = '扫描中';
            badge.className = 'method-badge running';
        }
        DiscoveryCore.updateStats();
        DiscoveryUtils.addLog('info', '开始扫描: ' + DiscoveryState.currentMethod);
        
        var method = DiscoveryState.DISCOVERY_METHODS.find(function(m) { return m.id === DiscoveryState.currentMethod; });
        console.log('[startScan] Found method:', method);
        
        switch (DiscoveryState.currentMethod) {
            case 'GITHUB':
                DiscoveryScan.discoverFromGitService('/api/v1/discovery/github', method);
                break;
            case 'GITEE':
                DiscoveryScan.discoverFromGitService('/api/v1/discovery/gitee', method);
                break;
            case 'GIT_REPOSITORY':
                DiscoveryScan.discoverFromGitRepository(method);
                break;
            case 'SKILL_CENTER':
                DiscoveryScan.discoverFromSkillCenter(method);
                break;
            case 'LOCAL_FS':
                DiscoveryScan.discoverFromLocal();
                break;
            case 'UDP_BROADCAST':
                DiscoveryScan.discoverFromUdpBroadcast(method);
                break;
            case 'MDNS':
                DiscoveryScan.discoverFromMdns(method);
                break;
            case 'REST_API':
                DiscoveryScan.discoverFromRestApi(method);
                break;
            case 'AUTO':
                DiscoveryScan.discoverFromLocal();
                break;
            default:
                DiscoveryScan.discoverFromLocal();
        }
    },

    addRadarDots: function() {
        var container = document.getElementById('radarDots');
        if (!container) return;
        container.innerHTML = '';
        var count = Math.min(DiscoveryState.discoveredCapabilities.length, 20);
        for (var i = 0; i < count; i++) {
            var dot = document.createElement('div');
            dot.className = 'radar-dot';
            dot.style.left = (20 + Math.random() * 60) + '%';
            dot.style.top = (20 + Math.random() * 60) + '%';
            dot.style.animationDelay = (Math.random() * 2) + 's';
            container.appendChild(dot);
        }
    },

    updateStats: function() {
        document.getElementById('statScanned').textContent = DiscoveryState.scanStats.scanned;
        document.getElementById('statFound').textContent = DiscoveryState.scanStats.found;
        document.getElementById('statNew').textContent = DiscoveryState.scanStats.new;
        document.getElementById('statInstalled').textContent = DiscoveryState.scanStats.installed;
    }
};

global.DiscoveryCore = DiscoveryCore;
global.selectMethod = function(methodId) { DiscoveryCore.selectMethod(methodId); };
global.startDiscovery = function() { DiscoveryCore.startScan(); };
global.forceRefresh = function() { DiscoveryCore.startScan(); };
global.hideConfig = function() { DiscoveryCore.hideConfig(); };
global.applyConfig = function() { DiscoveryCore.startScan(); };
global.toggleLogs = function() {
    var panel = document.getElementById('logsPanel');
    if (panel) { panel.classList.toggle('collapsed'); }
};

})(typeof window !== 'undefined' ? window : this);
