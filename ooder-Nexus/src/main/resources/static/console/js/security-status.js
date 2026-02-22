let securityAPI;

// 工具函数对象
const utils = {
    showError: function(message, selector) {
        console.error(message);
        const el = document.querySelector(selector);
        if (el) {
            el.innerHTML = `<div class="error-message"><i class="ri-error-warning-line"></i> ${message}</div>`;
        }
    },
    showSuccess: function(message, selector) {
        console.log(message);
        const el = document.querySelector(selector);
        if (el) {
            el.innerHTML = `<div class="success-message"><i class="ri-check-line"></i> ${message}</div>`;
        }
    }
};

async function initSecurityStatus() {
    try {
        securityAPI = new SecurityAPI();
        await loadSecurityStatus();
    } catch (error) {
        console.error('初始化安全状态失败:', error);
        utils.showError('加载安全状态数据失败', '.security-status');
    }
}

async function loadSecurityStatus() {
    try {
        const response = await securityAPI.getSecurityStatus();
        if ((response.code === 200 || response.message === "success") && response.data) {
            const data = response.data;
            updateSecurityStatus(data);
            updateSecurityDetails(data);
            updateSecurityAlerts(data.alerts || []);
        }
    } catch (error) {
        console.error('加载安全状态失败:', error);
    }
}

function updateSecurityOverview(data) {
    document.getElementById('security-score').textContent = data.score || 85;
    document.getElementById('security-alerts').textContent = data.alerts ? data.alerts.length : 0;
    document.getElementById('security-status').textContent = data.status || '安全';
}

function updateSecurityDetails(data) {
    // 更新系统安全
    if (data.systemSecurity) {
        const systemSecurity = document.getElementById('system-security');
        if (systemSecurity) {
            systemSecurity.innerHTML = data.systemSecurity.map(item => `
                <div class="security-item">
                    <div class="security-item-icon ri-${item.status === 'good' ? 'check-circle-line success' : 'alert-circle-line warning'}"></div>
                    <div class="security-item-info">
                        <h4>${item.title}</h4>
                        <p>${item.description}</p>
                    </div>
                </div>
            `).join('');
        }
    }
    
    // 更新网络安全
    if (data.networkSecurity) {
        const networkSecurity = document.getElementById('network-security');
        if (networkSecurity) {
            networkSecurity.innerHTML = data.networkSecurity.map(item => `
                <div class="security-item">
                    <div class="security-item-icon ri-${item.status === 'good' ? 'check-circle-line success' : 'alert-circle-line warning'}"></div>
                    <div class="security-item-info">
                        <h4>${item.title}</h4>
                        <p>${item.description}</p>
                    </div>
                </div>
            `).join('');
        }
    }
    
    // 更新数据安全
    if (data.dataSecurity) {
        const dataSecurity = document.getElementById('data-security');
        if (dataSecurity) {
            dataSecurity.innerHTML = data.dataSecurity.map(item => `
                <div class="security-item">
                    <div class="security-item-icon ri-${item.status === 'good' ? 'check-circle-line success' : 'alert-circle-line warning'}"></div>
                    <div class="security-item-info">
                        <h4>${item.title}</h4>
                        <p>${item.description}</p>
                    </div>
                </div>
            `).join('');
        }
    }
}

function updateSecurityAlerts(alerts) {
    const alertsList = document.getElementById('alerts-list');
    if (alertsList) {
        if (alerts.length === 0) {
            alertsList.innerHTML = '<div class="empty-state">暂无安全告警</div>';
        } else {
            alertsList.innerHTML = alerts.map(alert => `
                <div class="alert-item ${alert.level}">
                    <div class="alert-icon ri-alert-line"></div>
                    <div class="alert-info">
                        <h4>${alert.title}</h4>
                        <p>${alert.description}</p>
                        <span class="alert-time">${alert.time || '刚刚'}</span>
                    </div>
                    <div class="alert-actions">
                        <button class="btn btn-sm btn-primary" onclick="handleAlert('${alert.id}')">处理</button>
                    </div>
                </div>
            `).join('');
        }
    }
}

async function refreshSecurityStatus() {
    await loadSecurityStatus();
    utils.showSuccess('安全状态已刷新', '.security-status');
}

async function runSecurityScan() {
    try {
        const response = await securityAPI.runSecurityScan();
        if (response.code === 200 || response.message === "success") {
            await loadSecurityStatus();
            utils.showSuccess('安全扫描完成', '.security-status');
        }
    } catch (error) {
        console.error('安全扫描失败:', error);
        utils.showError('安全扫描失败', '.security-status');
    }
}

async function handleAlert(alertId) {
    try {
        const response = await securityAPI.handleAlert(alertId);
        if (response.code === 200 || response.message === "success") {
            await loadSecurityStatus();
            utils.showSuccess('告警已处理', '.security-status');
        }
    } catch (error) {
        console.error('处理告警失败:', error);
        utils.showError('处理告警失败', '.security-status');
    }
}
