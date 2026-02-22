(function(global) {
    'use strict';

    var OpenWrtSystemStatus = {
        openwrtApi: {
            baseUrl: '/api/openwrt',
            get: async function(endpoint) {
                var response = await fetch(this.baseUrl + endpoint);
                return await response.json();
            }
        },

        init: function() {
            var self = this;
            window.onPageInit = function() {
                console.log('OpenWrt 系统状态页面初始化完成');
                self.loadSystemStatus();
                self.loadVersionInfo();
            };
        },

        loadSystemStatus: async function() {
            try {
                var data = await this.openwrtApi.get('/system-status');
                if (data.status === 'success' && data.data) {
                    var status = data.data;
                    document.getElementById('deviceModel').textContent = status.deviceModel || '-';
                    document.getElementById('firmwareVersion').textContent = status.firmwareVersion || '-';
                    document.getElementById('uptime').textContent = status.uptime || '-';
                    document.getElementById('cpuUsage').textContent = status.cpuUsage || '-';
                    document.getElementById('kernelVersion').textContent = status.kernelVersion || '-';
                    document.getElementById('loadAverage').textContent = status.loadAverage || '-';
                    document.getElementById('temperature').textContent = status.temperature || '-';
                    if (status.memory) {
                        var memoryParts = status.memory.split(',');
                        document.getElementById('totalMemory').textContent = memoryParts[0] ? memoryParts[0].replace('total:', '').trim() : '-';
                        document.getElementById('usedMemory').textContent = memoryParts[1] ? memoryParts[1].replace('used:', '').trim() : '-';
                        document.getElementById('freeMemory').textContent = memoryParts[2] ? memoryParts[2].replace('free:', '').trim() : '-';
                    }
                }
            } catch (error) {
                console.error('加载系统状态失败:', error);
            }
        },

        loadVersionInfo: async function() {
            try {
                var data = await this.openwrtApi.get('/version');
                if (data.status === 'success' && data.data) {
                    var version = data.data;
                    document.getElementById('openwrtVersion').textContent = version.openWrtVersion || '-';
                    document.getElementById('versionDetected').textContent = version.versionDetected ? '已检测' : '未检测';
                    document.getElementById('isSupported').textContent = version.isSupported ? '支持' : '不支持';
                }
            } catch (error) {
                console.error('加载版本信息失败:', error);
            }
        }
    };

    OpenWrtSystemStatus.init();

    global.loadSystemStatus = function() { OpenWrtSystemStatus.loadSystemStatus(); };
    global.loadVersionInfo = function() { OpenWrtSystemStatus.loadVersionInfo(); };

})(typeof window !== 'undefined' ? window : this);
