(function(global) {
    'use strict';

    var CommandExecution = {
        openwrtApi: {
            baseUrl: '/api/openwrt',
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
                console.log('命令执行页面初始化完成');
            };

            document.getElementById('commandInput').addEventListener('keyup', function(e) {
                if (e.key === 'Enter') {
                    CommandExecution.executeCommand();
                }
            });
        },

        quickCommand: function(cmd) {
            document.getElementById('commandInput').value = cmd;
            this.executeCommand();
        },

        executeCommand: async function() {
            var command = document.getElementById('commandInput').value.trim();
            if (!command) {
                alert('请输入命令');
                return;
            }

            var outputArea = document.getElementById('outputArea');
            outputArea.textContent = '执行: ' + command + '\n请稍候...';

            try {
                var data = await this.openwrtApi.post('/command', { command: command });

                if (data.status === 'success') {
                    outputArea.textContent = '$ ' + command + '\n\n' + (data.data && data.data.output ? data.data.output : '命令执行完成');
                } else {
                    outputArea.textContent = '$ ' + command + '\n\n错误: ' + (data.message || '执行失败');
                }
            } catch (error) {
                console.error('执行命令失败:', error);
                outputArea.textContent = '$ ' + command + '\n\n错误: 执行命令失败';
            }
        },

        clearOutput: function() {
            document.getElementById('outputArea').textContent = '等待执行命令...';
        }
    };

    CommandExecution.init();

    global.quickCommand = function(cmd) { CommandExecution.quickCommand(cmd); };
    global.executeCommand = function() { CommandExecution.executeCommand(); };
    global.clearOutput = function() { CommandExecution.clearOutput(); };

})(typeof window !== 'undefined' ? window : this);
