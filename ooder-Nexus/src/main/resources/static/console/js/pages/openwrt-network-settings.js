(function(global) {
    'use strict';

    var NetworkSettings = {
        openwrtApi: {
            baseUrl: '/api/openwrt',
            get: async function(endpoint) {
                var response = await fetch(this.baseUrl + endpoint);
                return await response.json();
            },
            put: async function(endpoint, data) {
                var response = await fetch(this.baseUrl + endpoint, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data || {})
                });
                return await response.json();
            }
        },

        init: function() {
            var self = this;
            window.onPageInit = function() {
                console.log('网络设置页面初始化完成');
                self.loadSettings();
            };
        },

        loadSettings: async function() {
            try {
                var basicData = await this.openwrtApi.get('/settings/basic');
                if (basicData.status === 'success' && basicData.data) {
                    document.getElementById('networkName').value = basicData.data.networkName || '';
                    document.getElementById('domainName').value = basicData.data.domainName || '';
                    document.getElementById('timezone').value = basicData.data.timezone || 'Asia/Shanghai';
                    document.getElementById('ntpServer').value = basicData.data.ntpServer || '';
                }
            } catch (error) {
                console.error('加载设置失败:', error);
            }
        },

        saveAllSettings: async function() {
            try {
                await this.openwrtApi.put('/settings/basic', {
                    networkName: document.getElementById('networkName').value,
                    domainName: document.getElementById('domainName').value,
                    timezone: document.getElementById('timezone').value,
                    ntpServer: document.getElementById('ntpServer').value
                });
                alert('设置保存成功');
            } catch (error) {
                console.error('保存设置失败:', error);
                alert('保存设置失败');
            }
        }
    };

    NetworkSettings.init();

    global.loadSettings = function() { NetworkSettings.loadSettings(); };
    global.saveAllSettings = function() { NetworkSettings.saveAllSettings(); };

})(typeof window !== 'undefined' ? window : this);
