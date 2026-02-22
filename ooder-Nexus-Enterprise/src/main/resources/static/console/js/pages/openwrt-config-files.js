(function(global) {
    'use strict';

    var ConfigFiles = {
        currentConfigPath: '',
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
            window.onPageInit = function() {
                console.log('配置文件页面初始化完成');
            };
        },

        loadConfig: async function(path) {
            this.currentConfigPath = path;
            document.getElementById('currentFile').textContent = path;

            try {
                var data = await this.openwrtApi.get('/config?path=' + encodeURIComponent(path));
                if (data.status === 'success' && data.data) {
                    document.getElementById('configContent').value = data.data.content || '';
                } else {
                    document.getElementById('configContent').value = '# 加载配置文件失败';
                }
            } catch (error) {
                console.error('加载配置文件失败:', error);
                document.getElementById('configContent').value = '# 加载配置文件失败';
            }
        },

        saveConfig: async function() {
            if (!this.currentConfigPath) {
                alert('请先选择一个配置文件');
                return;
            }

            var content = document.getElementById('configContent').value;

            try {
                var data = await this.openwrtApi.post('/config', {
                    path: this.currentConfigPath,
                    content: content
                });

                if (data.status === 'success') {
                    alert('配置保存成功');
                } else {
                    alert('保存失败: ' + (data.message || '未知错误'));
                }
            } catch (error) {
                console.error('保存配置失败:', error);
                alert('保存配置失败');
            }
        }
    };

    ConfigFiles.init();

    global.loadConfig = function(path) { ConfigFiles.loadConfig(path); };
    global.saveConfig = function() { ConfigFiles.saveConfig(); };

})(typeof window !== 'undefined' ? window : this);
