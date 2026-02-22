(function(global) {
    'use strict';

    var RouterDashboard = {
        openwrtApi: {
            baseUrl: '/api/openwrt',
            get: async function(endpoint) {
                var response = await fetch(this.baseUrl + endpoint);
                return await response.json();
            },
            post: async function(endpoint, data) {
                var response = await fetch(this.baseUrl + endpoint, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data || {})
                });
                return await response.json();
            }
        },

        init: function() {
            var self = this;
            window.onPageInit = function() {
                console.log('OpenWrt 路由器管理页面初始化完成');
                self.checkConnectionStatus();
            };
        },

        checkConnectionStatus: async function() {
            try {
                var data = await this.openwrtApi.get('/status');
                if (data.connected) {
                    this.updateConnectionUI(true);
                    this.loadSystemStatus();
                } else {
                    this.updateConnectionUI(false);
                }
            } catch (error) {
                console.error('检查连接状态失败:', error);
                this.updateConnectionUI(false);
            }
        },

        updateConnectionUI: function(connected) {
            var statusBadge = document.getElementById('connectionStatus');
            var connectBtn = document.querySelector('button[onclick="showConnectModal()"]');
            var disconnectBtn = document.getElementById('disconnectBtn');
            var rebootBtn = document.getElementById('rebootBtn');

            if (connected) {
                statusBadge.textContent = '已连接';
                statusBadge.className = 'nx-text-success';
                connectBtn.style.display = 'none';
                disconnectBtn.style.display = 'inline-flex';
                rebootBtn.style.display = 'inline-flex';
            } else {
                statusBadge.textContent = '未连接';
                statusBadge.className = 'nx-text-danger';
                connectBtn.style.display = 'inline-flex';
                disconnectBtn.style.display = 'none';
                rebootBtn.style.display = 'none';
            }
        },

        showConnectModal: function() {
            document.getElementById('connectModal').style.display = 'flex';
        },

        hideConnectModal: function() {
            document.getElementById('connectModal').style.display = 'none';
        },

        connectRouter: async function() {
            var host = document.getElementById('hostInput').value;
            var username = document.getElementById('usernameInput').value;
            var password = document.getElementById('passwordInput').value;

            try {
                var data = await this.openwrtApi.post('/connect', { host: host, username: username, password: password });
                if (data.connected) {
                    this.hideConnectModal();
                    this.updateConnectionUI(true);
                    this.loadSystemStatus();
                    alert('连接成功');
                } else {
                    alert('连接失败: ' + data.message);
                }
            } catch (error) {
                alert('连接失败');
            }
        },

        disconnectRouter: async function() {
            try {
                await this.openwrtApi.post('/disconnect');
                this.updateConnectionUI(false);
                alert('已断开连接');
            } catch (error) {
                alert('断开连接失败');
            }
        },

        rebootRouter: async function() {
            if (!confirm('确定要重启路由器吗？')) return;
            try {
                await this.openwrtApi.post('/reboot');
                alert('重启命令已发送');
                var self = this;
                setTimeout(function() { self.updateConnectionUI(false); }, 5000);
            } catch (error) {
                alert('重启失败');
            }
        },

        loadSystemStatus: async function() {
            try {
                var data = await this.openwrtApi.get('/system-status');
                if (data.status === 'success' && data.data) {
                    document.getElementById('firmwareVersion').textContent = data.data.firmwareVersion || '-';
                    document.getElementById('uptime').textContent = data.data.uptime || '-';
                    document.getElementById('deviceModel').textContent = data.data.deviceModel || '-';
                }
                var versionData = await this.openwrtApi.get('/version');
                if (versionData.status === 'success' && versionData.data) {
                    document.getElementById('routerHost').textContent = versionData.data.deviceInfo || '-';
                }
            } catch (error) {
                console.error('加载系统状态失败:', error);
            }
        },

        refreshData: function() {
            this.checkConnectionStatus();
            alert('数据已刷新');
        },

        navigateTo: function(page) {
            var pages = {
                'network-settings': 'network-settings.html',
                'ip-management': 'ip-management.html',
                'blacklist': 'blacklist.html',
                'config-files': 'config-files.html',
                'system-status': 'system-status.html',
                'command': 'command.html'
            };
            if (pages[page]) {
                window.location.href = pages[page];
            }
        }
    };

    RouterDashboard.init();

    global.checkConnectionStatus = function() { RouterDashboard.checkConnectionStatus(); };
    global.updateConnectionUI = function(connected) { RouterDashboard.updateConnectionUI(connected); };
    global.showConnectModal = function() { RouterDashboard.showConnectModal(); };
    global.hideConnectModal = function() { RouterDashboard.hideConnectModal(); };
    global.connectRouter = function() { RouterDashboard.connectRouter(); };
    global.disconnectRouter = function() { RouterDashboard.disconnectRouter(); };
    global.rebootRouter = function() { RouterDashboard.rebootRouter(); };
    global.loadSystemStatus = function() { RouterDashboard.loadSystemStatus(); };
    global.refreshData = function() { RouterDashboard.refreshData(); };
    global.navigateTo = function(page) { RouterDashboard.navigateTo(page); };

})(typeof window !== 'undefined' ? window : this);
