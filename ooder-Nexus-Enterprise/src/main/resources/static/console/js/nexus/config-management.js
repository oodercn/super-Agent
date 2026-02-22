/**
 * 配置管理页面 JavaScript
 */

// 模拟配置历史数据
let configHistory = [
    {
        id: 1,
        timestamp: new Date(Date.now() - 3600000).toISOString(),
        user: "admin",
        action: "修改",
        details: "更新网络配置：UDP端口从8080改为8081"
    },
    {
        id: 2,
        timestamp: new Date(Date.now() - 7200000).toISOString(),
        user: "admin",
        action: "修改",
        details: "更新日志级别：从DEBUG改为INFO"
    },
    {
        id: 3,
        timestamp: new Date(Date.now() - 86400000).toISOString(),
        user: "system",
        action: "初始化",
        details: "系统首次启动，加载默认配置"
    }
];

/**
 * 加载配置历史
 */
function loadConfigHistory() {
    const tbody = document.getElementById('configHistoryTable');
    tbody.innerHTML = '';

    configHistory.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${nexusCommon.formatTimestamp(new Date(item.timestamp).getTime())}</td>
            <td>${item.user}</td>
            <td>${item.action}</td>
            <td>${item.details}</td>
            <td>
                <button class="nexus-btn nexus-btn-sm nexus-btn-primary" onclick="applyConfigHistory(${item.id})">应用</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

/**
 * 保存配置
 */
async function saveConfig() {
    try {
        // 模拟保存配置
        nexusCommon.showMessage('正在保存配置...', 'info');
        
        // 模拟保存成功
        setTimeout(() => {
            // 添加到配置历史
            const newHistory = {
                id: configHistory.length + 1,
                timestamp: new Date().toISOString(),
                user: "admin",
                action: "修改",
                details: "更新系统配置"
            };
            configHistory.unshift(newHistory);
            loadConfigHistory();
            
            nexusCommon.showMessage('配置保存成功', 'success');
        }, 1000);
    } catch (error) {
        nexusCommon.handleApiError(error, '保存配置失败');
    }
}

/**
 * 导出配置
 */
function exportConfig() {
    try {
        // 模拟导出配置
        const configData = {
            system: {
                version: document.getElementById('systemVersion').value,
                name: document.getElementById('systemName').value,
                mode: document.getElementById('systemMode').value,
                logLevel: document.getElementById('logLevel').value
            },
            network: {
                udpPort: document.getElementById('udpPort').value,
                heartbeatInterval: document.getElementById('heartbeatInterval').value,
                heartbeatTimeout: document.getElementById('heartbeatTimeout').value,
                networkTimeout: document.getElementById('networkTimeout').value
            },
            terminal: {
                maxTerminals: document.getElementById('maxTerminals').value,
                terminalTimeout: document.getElementById('terminalTimeout').value,
                terminalReconnectAttempts: document.getElementById('terminalReconnectAttempts').value,
                terminalReconnectDelay: document.getElementById('terminalReconnectDelay').value
            },
            service: {
                apiPort: document.getElementById('apiPort').value,
                webConsolePort: document.getElementById('webConsolePort').value,
                serviceTimeout: document.getElementById('serviceTimeout').value,
                maxConnections: document.getElementById('maxConnections').value
            }
        };

        const blob = new Blob([JSON.stringify(configData, null, 2)], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'nexus-config-' + new Date().toISOString().slice(0, 10) + '.json';
        a.click();
        URL.revokeObjectURL(url);
        nexusCommon.showMessage('配置已导出', 'success');
    } catch (error) {
        nexusCommon.handleApiError(error, '导出配置失败');
    }
}

/**
 * 导入配置
 */
function importConfig() {
    // 模拟导入配置
    nexusCommon.showMessage('配置导入功能开发中', 'info');
}

/**
 * 重置配置
 */
function resetConfig() {
    if (confirm('确定要重置为默认配置吗？')) {
        // 重置表单为默认值
        document.getElementById('systemName').value = 'Nexus';
        document.getElementById('systemMode').value = 'normal';
        document.getElementById('logLevel').value = 'INFO';
        document.getElementById('udpPort').value = '8080';
        document.getElementById('heartbeatInterval').value = '30000';
        document.getElementById('heartbeatTimeout').value = '60000';
        document.getElementById('networkTimeout').value = '10000';
        document.getElementById('maxTerminals').value = '100';
        document.getElementById('terminalTimeout').value = '300';
        document.getElementById('terminalReconnectAttempts').value = '3';
        document.getElementById('terminalReconnectDelay').value = '5';
        document.getElementById('apiPort').value = '8081';
        document.getElementById('webConsolePort').value = '8082';
        document.getElementById('serviceTimeout').value = '60';
        document.getElementById('maxConnections').value = '1000';
        
        // 添加到配置历史
        const newHistory = {
            id: configHistory.length + 1,
            timestamp: new Date().toISOString(),
            user: "admin",
            action: "重置",
            details: "重置为默认配置"
        };
        configHistory.unshift(newHistory);
        loadConfigHistory();
        
        nexusCommon.showMessage('配置已重置为默认值', 'success');
    }
}

/**
 * 应用配置历史
 */
function applyConfigHistory(historyId) {
    nexusCommon.showMessage('应用配置历史功能开发中', 'info');
}

/**
 * 初始化页面
 */
function initConfigManagementPage() {
    // 初始化菜单
    if (typeof initMenu === 'function') {
        initMenu();
    }
    
    // 初始化页面
    if (typeof nexusCommon !== 'undefined') {
        nexusCommon.initPage('config-management', loadConfigHistory);
    }
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', initConfigManagementPage);
