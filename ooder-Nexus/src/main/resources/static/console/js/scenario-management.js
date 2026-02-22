/**
 * 场景管理页面脚本
 * 处理场景的创建、编辑、删除、导入导出等功能
 */

let scenarios = [];
let editingScenarioId = null;

// 添加CSS动画
const toastStyle = document.createElement('style');
toastStyle.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(toastStyle);

window.onload = function() {
    initMenu();
    loadScenarios();
};

async function loadScenarios() {
    try {
        const response = await fetch('/api/scenario/list');
        const result = await response.json();
        
        if (result.code === 200 && result.data) {
            scenarios = result.data;
            renderScenarios();
            updateStats();
        } else {
            showToast('加载场景列表失败: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('加载场景列表失败:', error);
        showToast('加载场景列表失败', 'error');
    }
}

function renderScenarios() {
    const container = document.getElementById('scenarioList');
    
    if (scenarios.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="ri-scene-line empty-icon"></i>
                <div class="empty-text">暂无场景</div>
                <button class="btn-primary" onclick="createScenario()">
                    <i class="ri-add-line"></i>
                    创建第一个场景
                </button>
            </div>
        `;
        return;
    }
    
    container.innerHTML = scenarios.map(scenario => `
        <div class="scenario-card">
            <div class="scenario-header">
                <div>
                    <div class="scenario-title">${scenario.name}</div>
                    <div class="scenario-id">ID: ${scenario.id}</div>
                </div>
                <div>
                    <span class="scenario-status status-${scenario.status}">${getStatusText(scenario.status)}</span>
                    <span class="scenario-type">${getTypeText(scenario.type)}</span>
                </div>
            </div>
            <div class="scenario-description">${scenario.description || '暂无描述'}</div>
            ${renderScenarioConfig(scenario.config)}
            <div class="scenario-actions">
                <button class="btn-secondary btn-sm" onclick="viewScenario('${scenario.id}')">
                    <i class="ri-eye-line"></i>
                    查看
                </button>
                <button class="btn-secondary btn-sm" onclick="editScenario('${scenario.id}')">
                    <i class="ri-edit-line"></i>
                    编辑
                </button>
                <button class="btn-primary btn-sm" onclick="activateScenario('${scenario.id}')">
                    <i class="ri-play-line"></i>
                    激活
                </button>
                <button class="btn-danger btn-sm" onclick="deleteScenario('${scenario.id}')">
                    <i class="ri-delete-bin-line"></i>
                    删除
                </button>
            </div>
        </div>
    `).join('');
}

function renderScenarioConfig(config) {
    if (!config) return '';
    
    const nodes = config.nodes || [];
    const links = config.links || [];
    const configSettings = config.config || {};
    
    return `
        <div class="scenario-config">
            <div class="config-section">
                <div class="config-label">节点数量</div>
                <div class="config-value">${nodes.length}</div>
            </div>
            <div class="config-section">
                <div class="config-label">链路数量</div>
                <div class="config-value">${links.length}</div>
            </div>
            ${configSettings.timeout ? `
            <div class="config-section">
                <div class="config-label">超时时间</div>
                <div class="config-value">${configSettings.timeout}ms</div>
            </div>
            ` : ''}
            ${configSettings.retry ? `
            <div class="config-section">
                <div class="config-label">重试次数</div>
                <div class="config-value">${configSettings.retry}</div>
            </div>
            ` : ''}
        </div>
    `;
}

function getStatusText(status) {
    const statusMap = {
        'active': '活跃',
        'inactive': '非活跃'
    };
    return statusMap[status] || status;
}

function getTypeText(type) {
    const typeMap = {
        'enterprise': '企业网络',
        'personal': '个人网络',
        'multi-network': '多网络',
        'test': '测试网络',
        'development': '开发环境'
    };
    return typeMap[type] || type;
}

function updateStats() {
    document.getElementById('totalScenarios').textContent = scenarios.length;
    document.getElementById('activeScenarios').textContent = scenarios.filter(s => s.status === 'active').length;
    document.getElementById('scenarioTypes').textContent = [...new Set(scenarios.map(s => s.type))].length;
}

async function saveScenario(event) {
    event.preventDefault();
    
    const scenarioData = {
        name: document.getElementById('scenarioName').value,
        type: document.getElementById('scenarioType').value,
        status: document.getElementById('scenarioStatus').value,
        description: document.getElementById('scenarioDescription').value,
        config: {}
    };
    
    try {
        const url = editingScenarioId 
            ? `/api/scenario/update/${editingScenarioId}`
            : '/api/scenario/create';
        
        const method = editingScenarioId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(scenarioData)
        });
        
        const result = await response.json();
        
        if (result.code === 200) {
            showToast(editingScenarioId ? '场景更新成功' : '场景创建成功', 'success');
            closeModal();
            loadScenarios();
        } else {
            showToast('保存场景失败: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('保存场景失败:', error);
        showToast('保存场景失败: ' + error.message, 'error');
    }
}

async function viewScenario(scenarioId) {
    try {
        const response = await fetch(`/api/scenario/detail/${scenarioId}`);
        const result = await response.json();
        
        if (result.code === 200 && result.data) {
            alert(`场景详情:\n\n名称: ${result.data.name}\n类型: ${result.data.type}\n状态: ${result.data.status}\n描述: ${result.data.description || '无'}`);
        } else {
            showToast('获取场景详情失败: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('获取场景详情失败:', error);
        showToast('获取场景详情失败', 'error');
    }
}

async function activateScenario(scenarioId) {
    try {
        const response = await fetch(`/api/scenario/update/${scenarioId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                status: 'active'
            })
        });
        
        const result = await response.json();
        
        if (result.code === 200) {
            showToast('场景激活成功', 'success');
            loadScenarios();
        } else {
            showToast('场景激活失败: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('场景激活失败:', error);
        showToast('场景激活失败', 'error');
    }
}

async function deleteScenario(scenarioId) {
    if (!confirm('确定要删除此场景吗？')) return;
    
    try {
        const response = await fetch(`/api/scenario/delete/${scenarioId}`, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.code === 200) {
            showToast('场景删除成功', 'success');
            loadScenarios();
        } else {
            showToast('场景删除失败: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('场景删除失败:', error);
        showToast('场景删除失败', 'error');
    }
}

async function importScenario() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json';
    input.onchange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;
        
        try {
            const text = await file.text();
            let data;
            
            try {
                data = JSON.parse(text);
            } catch (parseError) {
                showToast('文件格式错误：不是有效的JSON格式', 'error');
                return;
            }
            
            if (!Array.isArray(data) && !data.scenarios) {
                showToast('文件格式错误：缺少scenarios字段或不是数组格式', 'error');
                return;
            }
            
            const scenariosToImport = Array.isArray(data) ? data : (data.scenarios || []);
            
            if (scenariosToImport.length === 0) {
                showToast('文件中没有场景数据', 'error');
                return;
            }
            
            const invalidScenarios = scenariosToImport.filter(s => {
                return !s.id || !s.name || !s.type || !s.status;
            });
            
            if (invalidScenarios.length > 0) {
                showToast(`发现 ${invalidScenarios.length} 个无效的场景，请检查文件格式`, 'error');
                return;
            }
            
            const response = await fetch('/api/scenario/import', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    scenarios: scenariosToImport
                })
            });
            
            const result = await response.json();
            
            if (result.code === 200) {
                const importResult = result.data;
                const importedCount = importResult.imported ? importResult.imported.length : 0;
                const successCount = importResult.successCount || 0;
                const failCount = importResult.failCount || 0;
                
                if (importResult.errorMessages && importResult.errorMessages.length > 0) {
                    showToast(`导入完成：成功 ${successCount} 个，失败 ${failCount} 个`, 'error');
                    console.log('导入错误:', importResult.errorMessages);
                } else {
                    showToast(`成功导入 ${importedCount} 个场景`, 'success');
                    loadScenarios();
                }
            } else {
                showToast('场景导入失败: ' + result.message, 'error');
            }
        } catch (error) {
            console.error('场景导入失败:', error);
            if (error instanceof SyntaxError) {
                showToast('JSON解析失败：文件格式不正确', 'error');
            } else {
                showToast('场景导入失败: ' + error.message, 'error');
            }
        }
    };
    input.click();
}

async function exportScenario() {
    try {
        const response = await fetch('/api/scenario/export');
        const result = await response.json();
        
        if (result.code === 200 && result.data) {
            const blob = new Blob([JSON.stringify(result.data, null, 2)], { type: 'application/json' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'scenarios.json';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
            showToast('场景导出成功', 'success');
        } else {
            showToast('场景导出失败: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('场景导出失败:', error);
        showToast('场景导出失败', 'error');
    }
}

function refreshScenarios() {
    loadScenarios();
    showToast('刷新成功', 'success');
}

function closeModal() {
    document.getElementById('scenarioModal').classList.remove('show');
}

window.onclick = function(event) {
    const modal = document.getElementById('scenarioModal');
    if (event.target === modal) {
        closeModal();
    }
}

function createScenario() {
    editingScenarioId = null;
    document.getElementById('modalTitle').textContent = '创建场景';
    document.getElementById('scenarioForm').reset();
    document.getElementById('scenarioModal').classList.add('show');
}

function editScenario(scenarioId) {
    const scenario = scenarios.find(s => s.id === scenarioId);
    if (!scenario) return;
    
    editingScenarioId = scenarioId;
    document.getElementById('modalTitle').textContent = '编辑场景';
    document.getElementById('scenarioName').value = scenario.name;
    document.getElementById('scenarioType').value = scenario.type;
    document.getElementById('scenarioStatus').value = scenario.status;
    document.getElementById('scenarioDescription').value = scenario.description || '';
    document.getElementById('scenarioModal').classList.add('show');
}

function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 12px 20px;
        border-radius: 4px;
        color: white;
        font-size: 14px;
        z-index: 10000;
        animation: slideIn 0.3s ease-out;
    `;
    
    switch(type) {
        case 'success':
            toast.style.backgroundColor = '#52c41a';
            break;
        case 'error':
            toast.style.backgroundColor = '#f44336';
            break;
        case 'warning':
            toast.style.backgroundColor = '#ff9800';
            break;
        default:
            toast.style.backgroundColor = '#2196f3';
    }
    
    toast.textContent = message;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.style.animation = 'slideOut 0.3s ease-in';
        setTimeout(() => {
            document.body.removeChild(toast);
        }, 300);
    }, 3000);
}
