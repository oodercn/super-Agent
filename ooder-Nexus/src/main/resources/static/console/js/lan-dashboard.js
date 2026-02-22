let buttonManager;

// 工具函数对象 - 使用 window.utils 避免重复声明
if (!window.utils) {
    window.utils = {};
}

// 扩展 utils 对象
Object.assign(window.utils, {
    getStatusText: function(status, mapping) {
        if (mapping && mapping[status]) {
            return mapping[status];
        }
        const defaultMapping = {
            'online': '在线',
            'offline': '离线',
            'active': '活跃',
            'inactive': '非活跃',
            'normal': '正常',
            'abnormal': '异常'
        };
        return defaultMapping[status] || status;
    },
    getDeviceIcon: function(type) {
        const icons = {
            'router': 'router-line',
            'switch': 'switch-line',
            'ap': 'wifi-line',
            'phone': 'smartphone-line',
            'computer': 'computer-line',
            'tv': 'tv-line',
            'iot': 'sensor-line'
        };
        return icons[type] || 'device-line';
    },
    escapeHtml: function(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    },
    getStatusClass: function(status) {
        const classes = {
            'online': 'status-online',
            'offline': 'status-offline',
            'active': 'status-active',
            'inactive': 'status-inactive'
        };
        return classes[status] || 'status-unknown';
    },
    parallel: async function(promises) {
        return Promise.all(promises);
    },
    showSuccess: function(message) {
        console.log('[Success]', message);
        alert(message);
    },
    showError: function(message) {
        console.error('[Error]', message);
        alert(message);
    },
    navigate: function(url) {
        window.location.href = url;
    },
    toggleTheme: function() {
        if (window.themeManager) {
            window.themeManager.toggleTheme();
        }
    }
});

function init() {
    buttonManager = new ButtonManager();
    buttonManager.register('refresh-btn', {
        loadingText: '<i class="ri-loader-3-line"></i> 刷新中...'
    });
    
    loadDashboardData();
    loadDeviceData();
}

async function loadDashboardData() {
    try {
        const response = await fetch('/api/lan/dashboard');
        const data = await response.json();
        
        if (data.status === 'success') {
            const dashboard = data.dashboard;
            document.getElementById('totalDevices').textContent = dashboard.totalDevices;
            document.getElementById('networkStatus').textContent = utils.getStatusText(dashboard.networkStatus, {
                normal: '正常',
                abnormal: '异常'
            });
            document.getElementById('bandwidthUsage').textContent = dashboard.bandwidthUsage;
            document.getElementById('ipUsage').textContent = dashboard.ipUsage;
        }
    } catch (error) {
        console.error('加载仪表盘数据错误:', error);
    }
}

async function loadDeviceData() {
    try {
        const response = await fetch('/api/lan/devices');
        const data = await response.json();
        
        if (data.status === 'success') {
            const deviceList = document.getElementById('deviceList');
            deviceList.innerHTML = '';
            
            data.devices.forEach(device => {
                const deviceItem = document.createElement('div');
                deviceItem.className = 'device-item';
                
                deviceItem.innerHTML = `
                    <div class="device-info">
                        <div class="device-icon">
                            <i class="ri-${utils.getDeviceIcon(device.type)}"></i>
                        </div>
                        <div class="device-details">
                            <h3>${utils.escapeHtml(device.name)}</h3>
                            <p>${utils.escapeHtml(device.ip)} · ${utils.escapeHtml(device.mac)}</p>
                        </div>
                    </div>
                    <div class="device-status">
                        <div class="${utils.getStatusClass(device.status)}"></div>
                        <span>${utils.getStatusText(device.status)}</span>
                    </div>
                `;
                
                deviceList.appendChild(deviceItem);
            });
        }
    } catch (error) {
        console.error('加载设备数据错误:', error);
    }
}

async function refreshData() {
    try {
        await buttonManager.executeWithLoading('refresh-btn', async () => {
            await utils.parallel([
                loadDashboardData(),
                loadDeviceData()
            ]);
            utils.showSuccess('数据已刷新');
        });
    } catch (error) {
        console.error('刷新数据错误:', error);
        buttonManager.removeLoading('refresh-btn');
        utils.showError('刷新数据失败，请重试');
    }
}

async function viewTopology() {
    try {
        const response = await fetch('/api/lan/topology');
        const data = await response.json();
        
        if (data.status === 'success') {
            utils.showSuccess('网络拓扑数据加载成功');
        }
    } catch (error) {
        console.error('加载拓扑数据错误:', error);
        utils.showError('加载拓扑数据失败');
    }
}

function viewBandwidthDetails() {
    utils.navigate('bandwidth-monitor.html');
}

function viewAllDevices() {
    utils.navigate('network-devices.html');
}

function toggleTheme() {
    utils.toggleTheme();
}
