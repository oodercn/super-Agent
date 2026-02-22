let currentPage = 'dashboard';
let agentListManager = null;

// 工具函数对象
const utils = {
    showNotification: function(message, type = 'info') {
        console.log(`[${type}] ${message}`);
        // 简单的通知实现
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `<i class="ri-${type === 'success' ? 'check' : type === 'error' ? 'error-warning' : 'information'}-line"></i> ${message}`;
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 12px 20px;
            background: var(--nexus-card-bg);
            border: 1px solid var(--nexus-border);
            border-radius: var(--nexus-radius);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            z-index: 9999;
            display: flex;
            align-items: center;
            gap: 8px;
        `;
        document.body.appendChild(notification);
        setTimeout(() => notification.remove(), 3000);
    },
    showLoader: function(message = '加载中...') {
        console.log(`[Loader] ${message}`);
        return {
            hide: function() {
                console.log('[Loader] Hidden');
            }
        };
    },
    formatTimestamp: function(timestamp) {
        return new Date(timestamp).toLocaleString('zh-CN');
    }
};

function init() {
    updateTimestamp();
    initListManagers();
    loadDashboard();
    setInterval(updateTimestamp, 60000);
}

function initListManagers() {
    agentListManager = new ListManager({
        resource: 'agents',
        page: 1,
        pageSize: 10,
        sortBy: 'id',
        sortOrder: 'asc',
        onLoad: function(response) {
            console.log('Agent data loaded:', response);
        },
        onError: function(error) {
            console.error('Error loading agent data:', error);
            utils.showNotification('加载Agent数据失败', 'error');
        }
    });
}

async function loadDashboard() {
    try {
        const loader = utils.showLoader('加载仪表盘数据...');
        
        // 从API获取系统状态数据
        try {
            const [healthResponse, systemResponse, networkResponse] = await Promise.all([
                fetch('/api/health/overview').catch(() => null),
                fetch('/api/mcp/system/status').catch(() => null),
                fetch('/api/mcp/network/status').catch(() => null)
            ]);
            
            let healthData = null;
            let systemData = null;
            let networkData = null;
            
            if (healthResponse && healthResponse.ok) {
                healthData = await healthResponse.json();
            }
            if (systemResponse && systemResponse.ok) {
                systemData = await systemResponse.json();
            }
            if (networkResponse && networkResponse.ok) {
                networkData = await networkResponse.json();
            }
            
            // 更新仪表盘数据
            updateDashboardData(healthData, systemData, networkData);
        } catch (apiError) {
            console.warn('API数据获取失败，使用默认数据:', apiError);
            // 使用默认数据
            updateDashboardData(null, null, null);
        }
        
        generateNetworkTopology();
        await loadAgentData();
        
        loader.hide();
        utils.showNotification('仪表盘数据加载成功', 'success');
    } catch (error) {
        console.error('加载仪表盘数据失败:', error);
        utils.showNotification('加载仪表盘数据失败', 'error');
    }
}

function updateDashboardData(healthData, systemData, networkData) {
    // 系统状态
    const systemStatus = document.getElementById('system-status');
    if (systemStatus) {
        systemStatus.textContent = healthData && healthData.status === 'healthy' ? '正常' : '异常';
    }
    
    // 网络连接数
    const networkCount = document.getElementById('network-count');
    if (networkCount) {
        networkCount.textContent = networkData && networkData.connections ? networkData.connections.length : '12';
    }
    
    // CPU使用率
    const cpuUsage = document.getElementById('cpu-usage');
    if (cpuUsage) {
        const cpu = systemData && systemData.cpu ? systemData.cpu.usage : 25;
        cpuUsage.textContent = cpu + '%';
    }
    
    // 内存使用率
    const memoryUsage = document.getElementById('memory-usage');
    if (memoryUsage) {
        const memory = systemData && systemData.memory ? systemData.memory.usage : 45;
        memoryUsage.textContent = memory + '%';
    }
    
    // 磁盘使用率
    const diskUsage = document.getElementById('disk-usage');
    if (diskUsage) {
        const disk = systemData && systemData.disk ? systemData.disk.usage : 32;
        diskUsage.textContent = disk + '%';
    }
    
    // 系统负载
    const systemLoad = document.getElementById('system-load');
    if (systemLoad) {
        const load = systemData && systemData.load ? systemData.load['1min'] : 0.8;
        systemLoad.textContent = load;
    }
    
    // 请求处理速率
    const requestRate = document.getElementById('request-rate');
    if (requestRate) {
        const rate = networkData && networkData.requestRate ? networkData.requestRate : 1000;
        requestRate.textContent = rate + '/秒';
    }
    
    // 平均响应时间
    const responseTime = document.getElementById('response-time');
    if (responseTime) {
        const time = networkData && networkData.responseTime ? networkData.responseTime : 120;
        responseTime.textContent = time + 'ms';
    }
    
    // 活跃任务数
    const activeTasks = document.getElementById('active-tasks');
    if (activeTasks) {
        const tasks = systemData && systemData.tasks ? systemData.tasks.active : 15;
        activeTasks.textContent = tasks;
    }
    
    // 系统温度
    const systemTemp = document.getElementById('system-temp');
    if (systemTemp) {
        const temp = systemData && systemData.temperature ? systemData.temperature : 45;
        systemTemp.textContent = temp + '°C';
    }
    
    // 网络带宽
    const networkBandwidth = document.getElementById('network-bandwidth');
    if (networkBandwidth) {
        const bandwidth = networkData && networkData.bandwidth ? networkData.bandwidth : 2.5;
        networkBandwidth.textContent = bandwidth + 'Gbps';
    }
    
    // 健康评分
    const healthScore = document.getElementById('health-score');
    if (healthScore) {
        const score = healthData && healthData.score ? healthData.score : 95;
        healthScore.textContent = score;
    }
}

async function loadAgentData() {
    try {
        // 检查 agentListManager 是否存在
        if (!agentListManager) {
            console.warn('agentListManager 未初始化');
            return;
        }
        await agentListManager.load();
        const agentData = agentListManager.data || [];
        console.log('Agent data:', agentData);
    } catch (error) {
        console.warn('加载Agent数据失败:', error);
        // 不抛出错误，避免影响其他功能
    }
}

function generateNetworkTopology() {
    const topologyContainer = document.getElementById('network-topology');
    if (!topologyContainer) {
        console.error('Network topology container not found');
        return;
    }
    
    topologyContainer.innerHTML = '';
    
    const containerWidth = topologyContainer.clientWidth || 800;
    const containerHeight = 320;
    const centerX = containerWidth / 2;
    const centerY = containerHeight / 2;
    
    const nodes = [
        { id: 'mcp', name: 'MCP', x: centerX, y: centerY - 80 },
        { id: 'route1', name: 'Route1', x: centerX - 200, y: centerY - 40 },
        { id: 'route2', name: 'Route2', x: centerX - 200, y: centerY + 40 },
        { id: 'end1', name: 'End1', x: centerX + 200, y: centerY - 60 },
        { id: 'end2', name: 'End2', x: centerX + 200, y: centerY },
        { id: 'end3', name: 'End3', x: centerX + 200, y: centerY + 60 },
        { id: 'end4', name: 'End4', x: centerX + 200, y: centerY + 120 }
    ];
    
    const links = [
        { source: 'mcp', target: 'route1' },
        { source: 'mcp', target: 'route2' },
        { source: 'route1', target: 'end1' },
        { source: 'route1', target: 'end2' },
        { source: 'route2', target: 'end3' },
        { source: 'route2', target: 'end4' }
    ];
    
    nodes.forEach(node => {
        const nodeElement = document.createElement('div');
        nodeElement.className = 'topology-node';
        nodeElement.style.left = `${node.x - 32}px`;
        nodeElement.style.top = `${node.y - 32}px`;
        nodeElement.textContent = node.name;
        nodeElement.title = node.name;
        topologyContainer.appendChild(nodeElement);
    });
    
    links.forEach(link => {
        const sourceNode = nodes.find(n => n.id === link.source);
        const targetNode = nodes.find(n => n.id === link.target);
        
        if (sourceNode && targetNode) {
            const linkElement = document.createElement('div');
            linkElement.className = 'topology-link';
            
            const dx = targetNode.x - sourceNode.x;
            const dy = targetNode.y - sourceNode.y;
            const length = Math.sqrt(dx * dx + dy * dy);
            const angle = Math.atan2(dy, dx) * 180 / Math.PI;
            
            linkElement.style.width = `${length}px`;
            linkElement.style.left = `${sourceNode.x}px`;
            linkElement.style.top = `${sourceNode.y}px`;
            linkElement.style.transform = `rotate(${angle}deg)`;
            
            topologyContainer.appendChild(linkElement);
        }
    });
}

function updateTimestamp() {
    const now = new Date();
    const timestampEl = document.getElementById('timestamp');
    if (timestampEl) {
        timestampEl.textContent = now.toLocaleString('zh-CN');
    }
}

window.onload = init;
