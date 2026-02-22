(function(global) {
    'use strict';

    var logs = [
        { id: 'log-001', timestamp: new Date(Date.now() - 300000).toISOString(), level: 'ERROR', source: 'network', message: '网络连接失败: 无法连接到终端 endagent-003' },
        { id: 'log-002', timestamp: new Date(Date.now() - 600000).toISOString(), level: 'WARNING', source: 'service', message: '服务重启: API Service 自动重启' },
        { id: 'log-003', timestamp: new Date(Date.now() - 900000).toISOString(), level: 'INFO', source: 'system', message: '系统状态检查: 所有服务正常运行' },
        { id: 'log-004', timestamp: new Date(Date.now() - 1200000).toISOString(), level: 'DEBUG', source: 'terminal', message: '终端注册: 新终端 endagent-004 注册成功' },
        { id: 'log-005', timestamp: new Date(Date.now() - 1500000).toISOString(), level: 'INFO', source: 'network', message: '网络拓扑更新: 新增连接' }
    ];

    var LogManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('日志管理页面初始化完成');
                LogManagement.renderLogs();
                LogManagement.updateStats();
            };
        },

        renderLogs: function(filteredLogs) {
            var container = document.getElementById('logContainer');
            container.innerHTML = '';
            var displayLogs = filteredLogs || logs;

            displayLogs.forEach(function(log) {
                var levelClass = log.level === 'ERROR' ? 'nx-text-danger' : log.level === 'WARNING' ? 'nx-text-warning' : 'nx-text-secondary';
                var item = document.createElement('div');
                item.className = 'nx-flex nx-items-center nx-gap-3 nx-py-3 nx-border-bottom';
                item.innerHTML = '<span class="nx-text-sm ' + levelClass + '" style="min-width: 60px;">' + log.level + '</span>' +
                    '<span class="nx-text-sm nx-text-secondary" style="min-width: 160px;">' + LogManagement.formatTime(log.timestamp) + '</span>' +
                    '<span class="nx-text-sm nx-text-secondary" style="min-width: 80px;">' + log.source + '</span>' +
                    '<span class="nx-text-sm">' + log.message + '</span>';
                container.appendChild(item);
            });

            document.getElementById('logCount').textContent = '共 ' + displayLogs.length + ' 条日志';
        },

        updateStats: function() {
            var errorCount = 0, warningCount = 0, infoCount = 0, debugCount = 0;
            logs.forEach(function(l) {
                if (l.level === 'ERROR') errorCount++;
                else if (l.level === 'WARNING') warningCount++;
                else if (l.level === 'INFO') infoCount++;
                else if (l.level === 'DEBUG') debugCount++;
            });
            document.getElementById('errorCount').textContent = errorCount;
            document.getElementById('warningCount').textContent = warningCount;
            document.getElementById('infoCount').textContent = infoCount;
            document.getElementById('debugCount').textContent = debugCount;
        },

        formatTime: function(timestamp) {
            return new Date(timestamp).toLocaleString('zh-CN');
        },

        filterLogs: function() {
            var level = document.getElementById('logLevel').value;
            var source = document.getElementById('logSource').value;
            var search = document.getElementById('logSearch').value.toLowerCase();

            var filtered = logs.filter(function(log) {
                var matchLevel = level === 'all' || log.level === level;
                var matchSource = source === 'all' || log.source === source;
                var matchSearch = !search || log.message.toLowerCase().indexOf(search) !== -1;
                return matchLevel && matchSource && matchSearch;
            });

            LogManagement.renderLogs(filtered);
        },

        refreshLogs: function() {
            LogManagement.renderLogs();
        },

        exportLogs: function() {
            var data = JSON.stringify(logs, null, 2);
            var blob = new Blob([data], { type: 'application/json' });
            var url = URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'nexus-logs.json';
            a.click();
            URL.revokeObjectURL(url);
        }
    };

    LogManagement.init();

    global.filterLogs = LogManagement.filterLogs;
    global.refreshLogs = LogManagement.refreshLogs;
    global.exportLogs = LogManagement.exportLogs;

})(typeof window !== 'undefined' ? window : this);
