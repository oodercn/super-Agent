/**
 * 仪表盘页面脚本
 */

function showLoading(containerId, message) {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = `<div class="loading-placeholder"><i class="ri-loader-4-line spin"></i> ${message || '加载中...'}</div>`;
    }
}

function showError(containerId, message) {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = `<div class="error-placeholder"><i class="ri-error-warning-line"></i> ${message || '加载失败'}</div>`;
    }
}

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
        
        const api = window.apiService || window.ApiClient;
        if (!api) {
            throw new Error('API服务未初始化');
        }
        
        const skillCount = document.getElementById('skill-count');
        const sceneCount = document.getElementById('scene-count');
        const todoCount = document.getElementById('todo-count');
        const executionSuccessRate = document.getElementById('execution-success-rate');
        const cpuUsage = document.getElementById('cpu-usage');
        const cpuValue = document.getElementById('cpu-value');
        const memoryUsage = document.getElementById('memory-usage');
        const memoryValue = document.getElementById('memory-value');
        const diskUsage = document.getElementById('disk-usage');
        const diskValue = document.getElementById('disk-value');
        
        if (skillCount) skillCount.textContent = '5';
        if (sceneCount) sceneCount.textContent = '3';
        if (todoCount) todoCount.textContent = '8';
        if (executionSuccessRate) executionSuccessRate.textContent = '95%';
        
        if (cpuUsage) cpuUsage.style.width = '35%';
        if (cpuValue) cpuValue.textContent = '35%';
        if (memoryUsage) memoryUsage.style.width = '62%';
        if (memoryValue) memoryValue.textContent = '62%';
        if (diskUsage) diskUsage.style.width = '48%';
        if (diskValue) diskValue.textContent = '48%';
        
        const dashboardStats = document.getElementById('dashboard-stats');
        if (dashboardStats) {
            dashboardStats.innerHTML = '';
        }
        
    } catch (error) {
        console.error('加载仪表盘数据错误:', error);
        showError('dashboard-stats', '加载仪表盘数据失败: ' + error.message);
        
        const skillCount = document.getElementById('skill-count');
        if (skillCount) skillCount.textContent = '0';
        
        const executionSuccessRate = document.getElementById('execution-success-rate');
        if (executionSuccessRate) executionSuccessRate.textContent = '0%';
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
