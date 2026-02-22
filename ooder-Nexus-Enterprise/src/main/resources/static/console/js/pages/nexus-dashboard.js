(function(global) {
    'use strict';

    var NexusDashboard = {
        init: function() {
            window.onPageInit = function() {
                console.log('Nexus仪表盘页面初始化完成');
                NexusDashboard.loadDashboardData();
            };
        },

        loadDashboardData: async function() {
            try {
                var results = await Promise.all([
                    fetch('/api/devices/list', { method: 'POST' }).catch(function() { return null; }),
                    fetch('/api/command/stats', { method: 'POST' }).catch(function() { return null; }),
                    fetch('/api/health/overview', { method: 'POST' }).catch(function() { return null; })
                ]);

                var devicesRes = results[0];
                var commandsRes = results[1];
                var healthRes = results[2];

                var deviceData = { total: 0, online: 0 };
                var commandData = { total: 0, successful: 0, failed: 0 };
                var healthData = { status: '正常' };

                if (devicesRes) {
                    var rs = await devicesRes.json();
                    if ((rs.requestStatus === 200 || rs.code === 200) && rs.data) {
                        deviceData = rs.data.stats || { total: 0, online: 0 };
                    }
                }

                if (commandsRes) {
                    var rs = await commandsRes.json();
                    if ((rs.requestStatus === 200 || rs.code === 200) && rs.data) {
                        commandData = rs.data;
                    }
                }

                if (healthRes) {
                    var rs = await healthRes.json();
                    if ((rs.requestStatus === 200 || rs.code === 200) && rs.data) {
                        healthData = rs.data;
                    }
                }

                document.getElementById('endAgentCount').textContent = deviceData.total || 0;
                document.getElementById('linkCount').textContent = deviceData.online || 0;
                document.getElementById('networkStatus').textContent = healthData.status || '正常';
                document.getElementById('commandCount').textContent = commandData.total || 0;
                document.getElementById('packetsSent').textContent = commandData.sent || 0;
                document.getElementById('packetsReceived').textContent = commandData.received || 0;
                document.getElementById('packetsFailed').textContent = commandData.failed || 0;
                document.getElementById('networkStatusText').textContent = healthData.status || '正常';
                document.getElementById('totalCommands').textContent = commandData.total || 0;
                document.getElementById('successfulCommands').textContent = commandData.successful || 0;
                document.getElementById('failedCommands').textContent = commandData.failed || 0;
                document.getElementById('timeoutCommands').textContent = commandData.timeout || 0;

            } catch (error) {
                console.error('加载仪表盘数据失败:', error);
            }
        },

        refreshNetworkStatus: function() {
            NexusDashboard.loadDashboardData();
        },

        refreshCommandStats: function() {
            NexusDashboard.loadDashboardData();
        }
    };

    NexusDashboard.init();

    global.refreshNetworkStatus = NexusDashboard.refreshNetworkStatus;
    global.refreshCommandStats = NexusDashboard.refreshCommandStats;
    global.NexusDashboard = NexusDashboard;

})(typeof window !== 'undefined' ? window : this);
