(function(global) {
    'use strict';

    var NetworkConfigManagement = {
        openwrtApi: null,
        currentFile: 'network',

        init: function() {
            NetworkConfigManagement.openwrtApi = new ApiClient('/api/openwrt');

            window.onPageInit = function() {
                NetworkConfigManagement.loadConfigFile('network');
            };
        },

        loadConfigFile: function(fileName) {
            document.querySelectorAll('.config-file-item').forEach(function(item) {
                item.classList.remove('active');
            });
            document.querySelector('[data-file="' + fileName + '"]').classList.add('active');

            NetworkConfigManagement.currentFile = fileName;
            document.getElementById('currentFileName').textContent = fileName;

            NetworkConfigManagement.openwrtApi.get('/config/' + fileName)
                .then(function(data) {
                    if (data.status === 'success' && data.data) {
                        document.getElementById('configEditor').value = data.data.content || '';
                    } else {
                        document.getElementById('configEditor').value = '# ' + fileName + ' configuration file\n# No content available';
                    }
                })
                .catch(function(error) {
                    console.error('加载配置文件失败:', error);
                    NetworkConfigManagement.showToast('加载配置文件失败', 'error');
                });
        },

        saveConfigFile: function() {
            var content = document.getElementById('configEditor').value;

            NetworkConfigManagement.openwrtApi.put('/config/' + NetworkConfigManagement.currentFile, {
                content: content
            })
                .then(function(data) {
                    if (data.status === 'success') {
                        NetworkConfigManagement.showToast('配置文件保存成功', 'success');
                    } else {
                        NetworkConfigManagement.showToast('保存失败: ' + data.message, 'error');
                    }
                })
                .catch(function(error) {
                    console.error('保存配置文件失败:', error);
                    NetworkConfigManagement.showToast('保存失败', 'error');
                });
        },

        showToast: function(message, type) {
            type = type || 'info';
            var toast = document.createElement('div');
            toast.className = 'toast ' + type;
            toast.textContent = message;
            toast.style.cssText = 
                'position: fixed;' +
                'top: 20px;' +
                'right: 20px;' +
                'padding: 12px 24px;' +
                'border-radius: 4px;' +
                'color: white;' +
                'font-size: 14px;' +
                'z-index: 9999;' +
                'animation: slideIn 0.3s ease-out;' +
                'background: ' + (type === 'success' ? '#52c41a' : type === 'error' ? '#ff4d4f' : type === 'warning' ? '#faad14' : '#1890ff') + ';';
            document.body.appendChild(toast);
            setTimeout(function() {
                toast.style.animation = 'slideOut 0.3s ease-in';
                setTimeout(function() {
                    document.body.removeChild(toast);
                }, 300);
            }, 3000);
        }
    };

    NetworkConfigManagement.init();

    global.loadConfigFile = NetworkConfigManagement.loadConfigFile;
    global.saveConfigFile = NetworkConfigManagement.saveConfigFile;

})(typeof window !== 'undefined' ? window : this);
