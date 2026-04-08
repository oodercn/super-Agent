// 初始化页面
function init() {
    loadSystemStatus();
}

// 更新时间戳
function updateTimestamp() {
    const now = new Date();
    const timestamp = document.getElementById('timestamp');
    if (timestamp) {
        timestamp.textContent = now.toLocaleString('zh-CN');
    }
}

// 系统管理相关函数

// 加载系统状态
async function loadSystemStatus() {
    try {
        // 获取系统状态
        const statusResponse = await fetch(`${utils.API_BASE_URL}/system/status`);
        if (!statusResponse.ok) {
            throw new Error('获取系统状态失败');
        }
        const status = await statusResponse.json();
        
        // 获取系统配置
        const configResponse = await fetch(`${utils.API_BASE_URL}/system/config`);
        if (!configResponse.ok) {
            throw new Error('获取系统配置失败');
        }
        const config = await configResponse.json();
        
        // 获取系统资源使用情况
        const resourcesResponse = await fetch(`${utils.API_BASE_URL}/system/resources`);
        if (!resourcesResponse.ok) {
            throw new Error('获取系统资源使用情况失败');
        }
        const resources = await resourcesResponse.json();
        
        // 获取系统版本
        const versionResponse = await fetch(`${utils.API_BASE_URL}/system/version`);
        if (!versionResponse.ok) {
            throw new Error('获取系统版本失败');
        }
        const version = await versionResponse.json();
        
        // 渲染系统状态
        renderSystemStatus(status, config, resources, version);
    } catch (error) {
        console.error('加载系统状态错误:', error);
        alert('加载系统状态失败: ' + error.message);
    }
}

// 渲染系统状态
function renderSystemStatus(status, config, resources, version) {
    const statusGrid = document.querySelector('.status-grid');
    statusGrid.innerHTML = '';
    
    // 系统版本
    const versionItem = document.createElement('div');
    versionItem.className = 'status-item ok';
    versionItem.innerHTML = `
        <div class="key">系统版本</div>
        <div class="value">${version.version || '未知'}</div>
    `;
    statusGrid.appendChild(versionItem);
    
    // Java版本
    const javaItem = document.createElement('div');
    javaItem.className = 'status-item ok';
    javaItem.innerHTML = `
        <div class="key">Java版本</div>
        <div class="value">${config.javaVersion || '未知'}</div>
    `;
    statusGrid.appendChild(javaItem);
    
    // 服务端口
    const portItem = document.createElement('div');
    portItem.className = 'status-item ok';
    portItem.innerHTML = `
        <div class="key">服务端口</div>
        <div class="value">8080</div>
    `;
    statusGrid.appendChild(portItem);
    
    // 运行时间
    const uptimeItem = document.createElement('div');
    uptimeItem.className = 'status-item ok';
    uptimeItem.innerHTML = `
        <div class="key">运行时间</div>
        <div class="value">${resources.uptimeHuman || '未知'}</div>
    `;
    statusGrid.appendChild(uptimeItem);
}

// 重启系统
async function restartSystem() {
    if (!confirm('确定要重启系统吗? 这将导致服务暂时不可用。')) {
        return;
    }
    
    try {
        const response = await fetch(`${utils.API_BASE_URL}/system/restart`, {
            method: 'POST'
        });
        if (!response.ok) {
            throw new Error('重启系统失败');
        }
        const result = await response.json();
        if (result.success) {
            alert('系统重启命令已发出，请等待系统重启完成。');
        } else {
            alert('重启系统失败: ' + result.message);
        }
    } catch (error) {
        console.error('重启系统错误:', error);
        alert('重启系统失败: ' + error.message);
    }
}

// 关闭系统
async function shutdownSystem() {
    if (!confirm('确定要关闭系统吗? 这将导致服务完全不可用。')) {
        return;
    }
    
    try {
        const response = await fetch(`${utils.API_BASE_URL}/system/shutdown`, {
            method: 'POST'
        });
        if (!response.ok) {
            throw new Error('关闭系统失败');
        }
        const result = await response.json();
        if (result.success) {
            alert('系统关闭命令已发出，系统将在短时间内关闭。');
        } else {
            alert('关闭系统失败: ' + result.message);
        }
    } catch (error) {
        console.error('关闭系统错误:', error);
        alert('关闭系统失败: ' + error.message);
    }
}

// 标签页切换函数
function switchTab(tabId) {
    // 隐藏所有标签页内容
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.add('hidden');
    });
    
    // 显示选中的标签页内容
    document.getElementById(`${tabId}-tab`).classList.remove('hidden');
    
    // 更新标签页按钮状态
    document.querySelectorAll('.btn-secondary').forEach(btn => {
        btn.style.backgroundColor = '#1a1a1a';
        btn.style.color = 'var(--ooder-secondary)';
    });
    
    // 高亮当前标签页按钮
    event.target.style.backgroundColor = 'var(--ooder-primary)';
    event.target.style.color = 'white';
    
    // 根据标签页ID加载相应的数据
    if (tabId === 'system-status') {
        loadSystemStatus();
    } else if (tabId === 'system-logs') {
        loadSystemLogs();
    } else if (tabId === 'system-control') {
        loadSystemOperationLog();
    }
}

// 保存系统配置
async function saveSystemConfig() {
    const serverPort = document.getElementById('server-port').value;
    const apiPath = document.getElementById('api-path').value;
    const maxSkills = document.getElementById('max-skills').value;
    
    try {
        const response = await fetch(`${utils.API_BASE_URL}/system/config`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                serverPort: parseInt(serverPort),
                apiPath: apiPath,
                maxSkills: parseInt(maxSkills)
            })
        });
        
        if (!response.ok) {
            throw new Error('保存系统配置失败');
        }
        
        const result = await response.json();
        if (result.success) {
            alert('系统配置保存成功!');
        } else {
            alert('系统配置保存失败: ' + result.message);
        }
    } catch (error) {
        console.error('保存系统配置错误:', error);
        alert('系统配置保存失败: ' + error.message);
    }
}

// 清理系统缓存
async function clearSystemCache() {
    if (!confirm('确定要清理系统缓存吗? 这将清除所有临时缓存数据。')) {
        return;
    }
    
    try {
        const response = await fetch(`${utils.API_BASE_URL}/system/cache`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('清理系统缓存失败');
        }
        
        const result = await response.json();
        if (result.success) {
            alert('系统缓存清理成功!');
            loadSystemOperationLog();
        } else {
            alert('系统缓存清理失败: ' + result.message);
        }
    } catch (error) {
        console.error('清理系统缓存错误:', error);
        alert('系统缓存清理失败: ' + error.message);
    }
}

// 加载系统操作日志
async function loadSystemOperationLog() {
    try {
        const response = await fetch(`${utils.API_BASE_URL}/system/operations`);
        if (!response.ok) {
            throw new Error('获取系统操作日志失败');
        }
        const operations = await response.json();
        const operationLog = document.getElementById('system-operation-log');
        
        if (operations.length === 0) {
            operationLog.innerHTML = '<p>没有系统操作日志</p>';
            return;
        }
        
        operationLog.innerHTML = `
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>操作时间</th>
                            <th>操作类型</th>
                            <th>操作结果</th>
                            <th>操作描述</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${operations.map(op => `
                            <tr>
                                <td>${new Date(op.timestamp).toLocaleString('zh-CN')}</td>
                                <td>${op.type}</td>
                                <td>${op.success ? '成功' : '失败'}</td>
                                <td>${op.description}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;
    } catch (error) {
        console.error('获取系统操作日志错误:', error);
        document.getElementById('system-operation-log').innerHTML = '<p style="color: var(--ooder-danger);">获取系统操作日志失败: ' + error.message + '</p>';
    }
}

// 加载系统日志
async function loadSystemLogs() {
    const logLevel = document.getElementById('log-level').value;
    
    try {
        const response = await fetch(`${utils.API_BASE_URL}/system/logs?level=${logLevel}`);
        if (!response.ok) {
            throw new Error('获取系统日志失败');
        }
        const logs = await response.json();
        const logsContent = document.getElementById('system-logs-content');
        
        if (logs.length === 0) {
            logsContent.innerHTML = '<p>没有系统日志</p>';
            return;
        }
        
        logsContent.innerHTML = logs.map(log => `
            <div style="margin-bottom: 8px; padding: 4px 0; border-bottom: 1px solid var(--ooder-border);">
                <span style="color: var(--ooder-secondary);">${new Date(log.timestamp).toLocaleString('zh-CN')}</span> 
                <span style="color: ${log.level === 'ERROR' ? 'var(--ooder-danger)' : log.level === 'WARN' ? 'var(--ooder-warning)' : 'var(--ooder-primary)' };">${log.level}</span> 
                <span>${log.message}</span>
            </div>
        `).join('');
    } catch (error) {
        console.error('获取系统日志错误:', error);
        document.getElementById('system-logs-content').innerHTML = '<p style="color: var(--ooder-danger);">获取系统日志失败: ' + error.message + '</p>';
    }
}

// 清空系统日志
async function clearSystemLogs() {
    if (!confirm('确定要清空系统日志吗? 这将删除所有系统日志记录。')) {
        return;
    }
    
    try {
        const response = await fetch(`${utils.API_BASE_URL}/system/logs`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('清空系统日志失败');
        }
        
        const result = await response.json();
        if (result.success) {
            alert('系统日志清空成功!');
            document.getElementById('system-logs-content').innerHTML = '';
        } else {
            alert('系统日志清空失败: ' + result.message);
        }
    } catch (error) {
        console.error('清空系统日志错误:', error);
        alert('系统日志清空失败: ' + error.message);
    }
}

// 页面加载完成后初始化
window.onload = function() {
    initMenu('system');
    init();
};
