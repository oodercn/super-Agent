/**
 * 仪表盘页面脚本
 */

// 页面初始化回调
window.onPageInit = function() {
    if (document.querySelector('.content-header h1')) {
        document.querySelector('.content-header h1').innerHTML = '<i class="ri-dashboard-line"></i> 仪表盘';
    }
    initDashboard();
};

function updateTimestamp() {
    const now = new Date();
    const timestamp = document.getElementById('timestamp');
    if (timestamp) {
        timestamp.textContent = now.toLocaleString('zh-CN');
    }
}

async function loadDashboardData() {
    try {
        showLoading('dashboard-stats', '加载仪表盘数据...');
        
        // 获取系统概览统计数据
        const statsResult = await ApiClient.post('/api/dashboard/stats');
        if (statsResult && statsResult.data) {
            const skillCount = document.getElementById('skill-count');
            if (skillCount) skillCount.textContent = statsResult.data.totalSkills || 0;
        }
        
        // 获取技能执行统计数据
        const executionStatsResult = await ApiClient.post('/api/dashboard/execution-stats');
        if (executionStatsResult && executionStatsResult.data) {
            const executionSuccessRate = document.getElementById('execution-success-rate');
            if (executionSuccessRate) executionSuccessRate.textContent = `${executionStatsResult.data.successRate || 0}%`;
        }
        
        // 获取市场活跃度统计数据
        const marketStatsResult = await ApiClient.post('/api/dashboard/market-stats');
        if (marketStatsResult && marketStatsResult.data) {
            const downloads = marketStatsResult.data.totalDownloads || 0;
            const reviews = marketStatsResult.data.totalReviews || 0;
            const activityScore = Math.min(100, Math.floor((downloads + reviews) / 40));
            
            const marketActivity = document.getElementById('market-activity');
            if (marketActivity) marketActivity.textContent = activityScore;
        }
        
        // 获取系统资源使用统计数据
        const systemStatsResult = await ApiClient.post('/api/dashboard/system-stats');
        if (systemStatsResult && systemStatsResult.data) {
            const cpuUsage = document.getElementById('cpu-usage');
            if (cpuUsage) cpuUsage.textContent = `${systemStatsResult.data.cpuUsage || 0}%`;
            
            const memoryUsage = document.getElementById('memory-usage');
            if (memoryUsage) memoryUsage.textContent = `${systemStatsResult.data.memoryUsage || 0}%`;
        }
        
        const systemStatus = document.getElementById('system-status');
        if (systemStatus) systemStatus.textContent = '运行中';
        
        const dashboardStats = document.getElementById('dashboard-stats');
        if (dashboardStats) {
            dashboardStats.innerHTML = '';
        }
        
    } catch (error) {
        console.error('加载仪表盘数据错误:', error);
        showError('dashboard-stats', '加载仪表盘数据失败: ' + error.message);
        
        const systemStatus = document.getElementById('system-status');
        if (systemStatus) systemStatus.textContent = '运行中';
        
        const skillCount = document.getElementById('skill-count');
        if (skillCount) skillCount.textContent = '0';
        
        const executionSuccessRate = document.getElementById('execution-success-rate');
        if (executionSuccessRate) executionSuccessRate.textContent = '0%';
        
        const marketActivity = document.getElementById('market-activity');
        if (marketActivity) marketActivity.textContent = '0';
        
        const cpuUsage = document.getElementById('cpu-usage');
        if (cpuUsage) cpuUsage.textContent = '0%';
        
        const memoryUsage = document.getElementById('memory-usage');
        if (memoryUsage) memoryUsage.textContent = '0%';
    }
}

function initDashboard() {
    updateTimestamp();
    loadDashboardData();
    setInterval(updateTimestamp, 60000);
    setInterval(loadDashboardData, 300000);
}

// 确保在 DOM 加载完成后初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initDashboard);
} else {
    initDashboard();
}
