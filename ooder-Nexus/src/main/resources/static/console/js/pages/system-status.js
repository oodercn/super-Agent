(function(global) {
    'use strict';

    var systemStatus = null;

    var defaultSystemStatus = {
        overview: {
            version: "v1.0.0",
            uptime: "24天 12小时 30分钟",
            status: "正常运行",
            lastRestart: new Date(Date.now() - 24 * 24 * 60 * 60 * 1000).toISOString()
        },
        resources: {
            cpu: { usage: 12, cores: 4, load: 0.8 },
            memory: { usage: 25, total: 8, used: 2 },
            disk: { usage: 15, total: 100, used: 15 },
            network: { usage: 10, upload: 2, download: 8 }
        },
        services: [
            { name: "Nexus Service", status: "running", pid: 1234, startTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(), memory: "512MB", cpu: "2%" },
            { name: "Web Console", status: "running", pid: 1235, startTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(), memory: "256MB", cpu: "1%" },
            { name: "API Service", status: "running", pid: 1236, startTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(), memory: "128MB", cpu: "1%" }
        ],
        events: [
            { timestamp: new Date(Date.now() - 300000).toISOString(), level: "INFO", source: "system", message: "系统状态检查完成: 所有服务正常" },
            { timestamp: new Date(Date.now() - 600000).toISOString(), level: "WARNING", source: "network", message: "网络连接暂时中断，已自动恢复" },
            { timestamp: new Date(Date.now() - 900000).toISOString(), level: "INFO", source: "service", message: "API Service 重启完成" }
        ]
    };

    var SystemStatus = {
        init: function() {
            window.onPageInit = function() {
                console.log('系统状态页面初始化完成');
                SystemStatus.loadSystemStatus();
            };
        },

        loadSystemStatus: async function() {
            try {
                var response = await fetch('/api/system/status', { method: 'POST' });
                var rs = await response.json();
                if ((rs.requestStatus === 200 || rs.code === 200) && rs.data) {
                    systemStatus = rs.data;
                } else {
                    systemStatus = defaultSystemStatus;
                }
            } catch (error) {
                console.error('加载系统状态失败:', error);
                systemStatus = defaultSystemStatus;
            }
            SystemStatus.updateSystemOverview();
            SystemStatus.updateServiceStatus();
            SystemStatus.updateSystemEvents();
        },

        formatTimestamp: function(timestamp) {
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },

        updateSystemOverview: function() {
            document.getElementById('systemVersion').textContent = systemStatus.overview.version;
            document.getElementById('uptime').textContent = systemStatus.overview.uptime;
            document.getElementById('systemStatus').textContent = systemStatus.overview.status;
            document.getElementById('lastRestart').textContent = SystemStatus.formatTimestamp(systemStatus.overview.lastRestart);
            document.getElementById('cpuUsage').textContent = systemStatus.resources.cpu.usage + '%';
            document.getElementById('memoryUsage').textContent = systemStatus.resources.memory.usage + '%';
            document.getElementById('diskUsage').textContent = systemStatus.resources.disk.usage + '%';
            document.getElementById('networkUsage').textContent = systemStatus.resources.network.usage + 'Mbps';
        },

        updateServiceStatus: function() {
            var tbody = document.getElementById('serviceTableBody');
            tbody.innerHTML = '';
            systemStatus.services.forEach(function(service) {
                var row = document.createElement('tr');
                var statusClass = service.status === 'running' ? 'nx-text-success' : 'nx-text-danger';
                var statusText = service.status === 'running' ? '运行中' : '停止';
                row.innerHTML = '\
                    <td>' + service.name + '</td>\
                    <td><span class="' + statusClass + '">' + statusText + '</span></td>\
                    <td>' + service.pid + '</td>\
                    <td>' + SystemStatus.formatTimestamp(service.startTime) + '</td>\
                    <td>' + service.memory + '</td>\
                    <td>' + service.cpu + '</td>\
                    <td>\
                        <button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="restartService(\'' + service.name + '\')">重启</button>\
                    </td>\
                ';
                tbody.appendChild(row);
            });
        },

        updateSystemEvents: function() {
            var container = document.getElementById('eventContainer');
            container.innerHTML = '';
            systemStatus.events.forEach(function(event) {
                var levelClass = event.level === 'ERROR' ? 'nx-text-danger' : event.level === 'WARNING' ? 'nx-text-warning' : 'nx-text-secondary';
                var item = document.createElement('div');
                item.className = 'nx-flex nx-items-center nx-gap-3 nx-py-3 nx-border-bottom';
                item.innerHTML = '\
                    <span class="nx-text-sm ' + levelClass + '" style="min-width: 60px;">' + event.level + '</span>\
                    <span class="nx-text-sm nx-text-secondary" style="min-width: 160px;">' + SystemStatus.formatTimestamp(event.timestamp) + '</span>\
                    <span class="nx-text-sm">' + event.message + '</span>\
                ';
                container.appendChild(item);
            });
        },

        refreshSystemStatus: function() {
            SystemStatus.loadSystemStatus();
        },

        exportSystemReport: function() {
            var reportData = JSON.stringify(systemStatus, null, 2);
            var blob = new Blob([reportData], { type: 'application/json' });
            var url = URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'system-status-' + new Date().toISOString().slice(0, 10) + '.json';
            a.click();
            URL.revokeObjectURL(url);
        },

        restartService: function(serviceName) {
            alert('重启服务: ' + serviceName);
        }
    };

    SystemStatus.init();

    global.refreshSystemStatus = SystemStatus.refreshSystemStatus;
    global.exportSystemReport = SystemStatus.exportSystemReport;
    global.restartService = SystemStatus.restartService;
    global.SystemStatus = SystemStatus;

})(typeof window !== 'undefined' ? window : this);
