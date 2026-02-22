(function(global) {
    'use strict';

    var RemoteTerminal = {
        openwrtApi: null,

        init: function() {
            RemoteTerminal.openwrtApi = new ApiClient('/api/openwrt');

            window.onPageInit = function() {
                RemoteTerminal.checkConnectionStatus();
            };
        },

        checkConnectionStatus: function() {
            RemoteTerminal.openwrtApi.get('/status')
                .then(function(data) {
                    var statusBadge = document.getElementById('connectionStatus');

                    if (data.connected) {
                        statusBadge.textContent = '已连接';
                        statusBadge.className = 'badge badge-success';
                    } else {
                        statusBadge.textContent = '未连接';
                        statusBadge.className = 'badge badge-secondary';
                    }
                })
                .catch(function(error) {
                    console.error('检查连接状态失败:', error);
                    document.getElementById('connectionStatus').textContent = '未连接';
                });
        },

        handleKeyPress: function(event) {
            if (event.key === 'Enter') {
                RemoteTerminal.executeCommand();
            }
        },

        setCommand: function(command) {
            document.getElementById('commandInput').value = command;
            document.getElementById('commandInput').focus();
        },

        executeCommand: function() {
            var commandInput = document.getElementById('commandInput');
            var command = commandInput.value.trim();

            if (!command) {
                RemoteTerminal.showToast('请输入命令', 'warning');
                return;
            }

            var outputDiv = document.getElementById('terminalOutput');

            outputDiv.innerHTML += '\n\n$ ' + command + '\n';
            outputDiv.scrollTop = outputDiv.scrollHeight;

            RemoteTerminal.openwrtApi.post('/execute', { command: command })
                .then(function(data) {
                    if (data.status === 'success' && data.data) {
                        var result = data.data;
                        if (result.success) {
                            outputDiv.innerHTML += '<span class="output-success">' + RemoteTerminal.escapeHtml(result.stdout || '命令执行成功') + '</span>';
                        } else {
                            outputDiv.innerHTML += '<span class="output-error">' + RemoteTerminal.escapeHtml(result.stderr || '命令执行失败') + '</span>';
                        }
                    } else {
                        outputDiv.innerHTML += '<span class="output-error">错误: ' + (data.message || '执行失败') + '</span>';
                    }
                })
                .catch(function(error) {
                    console.error('执行命令失败:', error);
                    outputDiv.innerHTML += '<span class="output-error">错误: ' + (error.message || '网络请求失败') + '</span>';
                });

            outputDiv.scrollTop = outputDiv.scrollHeight;
            commandInput.value = '';
        },

        clearOutput: function() {
            document.getElementById('terminalOutput').innerHTML = '欢迎使用 OpenWrt 命令执行终端\n请输入命令并点击执行按钮...\n';
        },

        escapeHtml: function(text) {
            if (!text) return '';
            return text
                .replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;')
                .replace(/"/g, '&quot;')
                .replace(/'/g, '&#039;');
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

    RemoteTerminal.init();

    global.handleKeyPress = RemoteTerminal.handleKeyPress;
    global.setCommand = RemoteTerminal.setCommand;
    global.executeCommand = RemoteTerminal.executeCommand;
    global.clearOutput = RemoteTerminal.clearOutput;

})(typeof window !== 'undefined' ? window : this);
