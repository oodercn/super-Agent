let buttonManager;

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
