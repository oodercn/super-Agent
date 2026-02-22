(function(global) {
    'use strict';

    var currentDeviceId = 'router1';
    var isMockMode = false;

    var NetworkOverview = {
        init: function() {
            window.onPageInit = function() {
                NetworkOverview.loadDeviceStats();
            };
        },

        loadDeviceStats: async function() {
            try {
                var devicesResponse = await fetch('/api/devices/list', { method: 'POST' });
                var rs = await devicesResponse.json();

                if ((rs.requestStatus === 200 || rs.code === 200) && rs.data) {
                    var stats = rs.data.stats;
                    var devices = rs.data.devices;

                    document.getElementById('deviceCount').textContent = stats.total || 0;
                    document.getElementById('onlineDevices').textContent = stats.online || 0;
                }
            } catch (error) {
                console.error('加载统计数据失败:', error);
            }
        },

        disconnectDevice: function() {
            if (confirm('确定要断开设备连接吗？')) {
                document.getElementById('connectionStatus').textContent = '未连接';
                document.getElementById('connectionStatus').className = 'status-badge status-offline';
                alert('设备已断开连接');
            }
        },

        rebootDevice: function() {
            if (confirm('确定要重启设备吗？')) {
                alert('设备重启命令已发送');
            }
        },

        refreshData: function() {
            var btn = document.getElementById('refresh-btn');
            btn.innerHTML = '<i class="ri-refresh-line ri-spin"></i> 刷新中...';

            setTimeout(function() {
                btn.innerHTML = '<i class="ri-refresh-line"></i> 刷新数据';
                alert('数据已刷新');
            }, 1000);
        },

        toggleMockMode: function() {
            isMockMode = !isMockMode;
            var btn = document.getElementById('mockToggleBtn');
            if (isMockMode) {
                btn.classList.add('btn-warning');
                btn.innerHTML = '<i class="ri-bug-line"></i> 模拟模式: 开';
            } else {
                btn.classList.remove('btn-warning');
                btn.innerHTML = '<i class="ri-bug-line"></i> 模拟模式';
            }
        },

        navigateTo: function(page) {
            window.location.href = page + '.html';
        },

        viewFullTopology: function() {
            window.location.href = '/console/pages/nexus/network-topology.html';
        }
    };

    NetworkOverview.init();

    global.disconnectDevice = NetworkOverview.disconnectDevice;
    global.rebootDevice = NetworkOverview.rebootDevice;
    global.refreshData = NetworkOverview.refreshData;
    global.toggleMockMode = NetworkOverview.toggleMockMode;
    global.navigateTo = NetworkOverview.navigateTo;
    global.viewFullTopology = NetworkOverview.viewFullTopology;
    global.NetworkOverview = NetworkOverview;

})(typeof window !== 'undefined' ? window : this);
