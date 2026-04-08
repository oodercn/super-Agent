// 远程托管管理 JavaScript

var hostingTable = null;
var hostingSkillSelector = null;
var currentDetailInstanceId = null;

function initRemoteHostingPage() {
    console.log('[RemoteHosting] 初始化远程托管页面');

    if (typeof initMenu === 'function') {
        initMenu('remote-hosting');
    }

    initHostingTable();
    initSkillSelector();
    initHostingForm();
    initSearchEvent();
    loadStats();
}

function initHostingTable() {
    console.log('[RemoteHosting] 开始初始化托管实例表格');

    if (!utils || !utils.DataTable) {
        console.error('[RemoteHosting] DataTable 组件未加载, utils:', utils);
        return;
    }

    const tableBody = document.getElementById('hostingTableBody');
    if (!tableBody) {
        console.error('[RemoteHosting] 找不到表格体元素 hostingTableBody');
        return;
    }

    hostingTable = utils.DataTable({
        tableId: 'hostingTableBody',
        apiUrl: '/api/hosting/instances',
        pageName: 'RemoteHosting',
        columns: [
            { field: 'id', title: '实例ID', width: '120px' },
            { field: 'name', title: '实例名称', width: '150px' },
            { field: 'skillName', title: '关联技能', width: '150px', formatter: (value, row) => value || row.skillId || '-' },
            {
                field: 'status',
                title: '状态',
                width: '100px',
                align: 'center',
                formatter: (value) => `<span class="hosting-status status-${value || 'unknown'}">${getStatusText(value)}</span>`
            },
            {
                field: 'healthStatus',
                title: '健康状态',
                width: '100px',
                align: 'center',
                formatter: (value) => `<span class="health-status status-${value || 'unknown'}">${getHealthText(value)}</span>`
            },
            {
                field: 'createdAt',
                title: '创建时间',
                width: '160px',
                formatter: (value) => value ? utils.date.format(value) : '-'
            },
            {
                field: 'actions',
                title: '操作',
                width: '280px',
                align: 'center',
                formatter: (value, row) => `
                    <div class="action-buttons">
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewInstanceDetail('${row.id}')" title="详情">
                            <i class="ri-eye-line"></i>
                        </button>
                        <button class="nx-btn nx-btn--success nx-btn--sm" onclick="startHosting('${row.id}')" ${row.status === 'running' ? 'disabled' : ''} title="启动">
                            <i class="ri-play-line"></i>
                        </button>
                        <button class="nx-btn nx-btn--warning nx-btn--sm" onclick="stopHosting('${row.id}')" ${row.status === 'stopped' ? 'disabled' : ''} title="停止">
                            <i class="ri-stop-line"></i>
                        </button>
                        <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="restartHosting('${row.id}')" title="重启">
                            <i class="ri-refresh-line"></i>
                        </button>
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="openScaleModal('${row.id}', ${row.currentInstances || 0}, ${row.maxInstances || 1})" title="扩缩容">
                            <i class="ri-stack-line"></i>
                        </button>
                        <button class="nx-btn nx-btn--danger nx-btn--sm" onclick="deleteHosting('${row.id}')" title="删除">
                            <i class="ri-delete-line"></i>
                        </button>
                    </div>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[RemoteHosting] 表格数据加载完成，共', data.length, '条记录');
            updateStatsFromData(data);
        },
        onError: (error) => {
            console.error('[RemoteHosting] 表格数据加载失败:', error);
        }
    });
}

function initSkillSelector() {
    if (utils && utils.SkillSelector) {
        hostingSkillSelector = utils.SkillSelector({
            selectId: 'skillId',
            emptyText: '请选择技能',
            pageName: 'RemoteHosting',
            onLoad: (skills) => {
                console.log('[RemoteHosting] 技能选择器加载完成，共', skills.length, '个技能');
            },
            onError: (error) => {
                console.error('[RemoteHosting] 技能选择器加载失败:', error);
            }
        });
    } else {
        console.warn('[RemoteHosting] SkillSelector 组件未加载');
    }
}

function initHostingForm() {
    console.log('[RemoteHosting] 表单事件初始化完成');
}

function initSearchEvent() {
    console.log('[RemoteHosting] 初始化搜索事件');

    const btnSearch = document.getElementById('btnSearchHosting');
    if (btnSearch) {
        btnSearch.addEventListener('click', function(e) {
            e.preventDefault();
            searchHosting();
        });
    }

    const btnReset = document.getElementById('btnResetFilters');
    if (btnReset) {
        btnReset.addEventListener('click', function(e) {
            e.preventDefault();
            resetFilters();
        });
    }

    const hostingSearch = document.getElementById('hostingSearch');
    if (hostingSearch) {
        hostingSearch.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchHosting();
            }
        });
    }

    const statusFilter = document.getElementById('statusFilter');
    if (statusFilter) {
        statusFilter.addEventListener('change', function() {
            searchHosting();
        });
    }
}

window.searchHosting = function() {
    console.log('[RemoteHosting] 执行搜索');

    const keyword = document.getElementById('hostingSearch')?.value?.trim() || '';
    const status = document.getElementById('statusFilter')?.value || '';

    if (hostingTable && typeof hostingTable.load === 'function') {
        const params = {};
        if (keyword) params.keyword = keyword;
        if (status) params.status = status;
        hostingTable.load(params);
    }
};

window.resetFilters = function() {
    console.log('[RemoteHosting] 重置筛选条件');

    const keywordInput = document.getElementById('hostingSearch');
    if (keywordInput) keywordInput.value = '';

    const statusFilter = document.getElementById('statusFilter');
    if (statusFilter) statusFilter.value = '';

    if (hostingTable) {
        hostingTable.refresh();
    }
};

function getStatusText(status) {
    switch (status) {
        case 'running': return '运行中';
        case 'stopped': return '已停止';
        case 'pending': return '待处理';
        case 'error': return '错误';
        case 'restarting': return '重启中';
        default: return status || '未知';
    }
}

function getHealthText(health) {
    switch (health) {
        case 'healthy': return '健康';
        case 'unhealthy': return '不健康';
        case 'unknown': return '未知';
        default: return health || '未知';
    }
}

window.loadStats = async function() {
    console.log('[RemoteHosting] 加载统计数据');
    try {
        const result = await utils.api.post('/hosting/stats', {});
        if (result.code === 200 && result.data) {
            const stats = result.data;
            document.getElementById('totalInstances').textContent = stats.totalInstances || 0;
            document.getElementById('runningInstances').textContent = stats.runningInstances || 0;
            document.getElementById('stoppedInstances').textContent = stats.stoppedInstances || 0;
            document.getElementById('errorInstances').textContent = 0;
        }
    } catch (error) {
        console.error('[RemoteHosting] 加载统计数据错误:', error);
    }
};

function updateStatsFromData(data) {
    if (!data || !Array.isArray(data)) return;
    
    let running = 0, stopped = 0, error = 0;
    data.forEach(item => {
        if (item.status === 'running') running++;
        else if (item.status === 'stopped') stopped++;
        else if (item.status === 'error') error++;
    });
    
    document.getElementById('totalInstances').textContent = data.length;
    document.getElementById('runningInstances').textContent = running;
    document.getElementById('stoppedInstances').textContent = stopped;
    document.getElementById('errorInstances').textContent = error;
}

window.openAddHostingModal = function() {
    console.log('[RemoteHosting] 打开添加托管模态框');
    
    document.getElementById('modalTitle').textContent = '创建实例';
    document.getElementById('hostingId').value = '';
    document.getElementById('hostingName').value = '';
    document.getElementById('skillId').value = '';
    document.getElementById('hostingDescription').value = '';
    document.getElementById('hostingStatus').value = 'stopped';
    document.getElementById('hostingModal').style.display = 'flex';
};

window.editHosting = async function(instanceId) {
    console.log('[RemoteHosting] 编辑托管实例:', instanceId);

    try {
        const data = await utils.api.post('/hosting/instances/get', { instanceId: instanceId });

        if (data.code === 200 && data.data) {
            const instance = data.data;

            document.getElementById('modalTitle').textContent = '编辑实例';
            document.getElementById('hostingId').value = instance.id || '';
            document.getElementById('hostingName').value = instance.name || '';
            document.getElementById('skillId').value = instance.skillId || '';
            document.getElementById('hostingDescription').value = instance.description || '';
            document.getElementById('hostingStatus').value = instance.status || 'stopped';
            document.getElementById('hostingModal').style.display = 'block';
        } else {
            utils.msg.error('获取实例详情失败');
        }
    } catch (error) {
        console.error('[RemoteHosting] 获取实例详情错误:', error);
        utils.msg.error('获取实例详情失败');
    }
};

window.closeHostingModal = function() {
    document.getElementById('hostingModal').style.display = 'none';
};

window.viewInstanceDetail = async function(instanceId) {
    console.log('[RemoteHosting] 查看实例详情:', instanceId);
    currentDetailInstanceId = instanceId;

    try {
        const result = await utils.api.post('/hosting/instances/get', { instanceId: instanceId });
        
        if (result.code === 200 && result.data) {
            const instance = result.data;
            
            document.getElementById('detailId').textContent = instance.id || '-';
            document.getElementById('detailName').textContent = instance.name || '-';
            document.getElementById('detailSkill').textContent = instance.skillName || instance.skillId || '-';
            document.getElementById('detailStatus').innerHTML = `<span class="hosting-status status-${instance.status}">${getStatusText(instance.status)}</span>`;
            document.getElementById('detailCreatedAt').textContent = instance.createdAt ? utils.date.format(instance.createdAt) : '-';
            document.getElementById('detailHealth').innerHTML = `<span class="health-status status-${instance.healthStatus}">${getHealthText(instance.healthStatus)}</span>`;
            
            document.getElementById('detailCpu').textContent = instance.cpuLimit ? instance.cpuLimit + ' 核' : '未设置';
            document.getElementById('detailMemory').textContent = instance.memoryLimit ? instance.memoryLimit + ' MB' : '未设置';
            document.getElementById('detailReplicas').textContent = instance.currentInstances || 0;
            document.getElementById('detailMaxReplicas').textContent = instance.maxInstances || 1;
            
            document.getElementById('detailHost').textContent = instance.host || '-';
            document.getElementById('detailPort').textContent = instance.port || '-';
            document.getElementById('detailProtocol').textContent = instance.protocol || '-';
            document.getElementById('detailEndpoint').textContent = instance.endpoint || '-';
            
            const startBtn = document.getElementById('detailStartBtn');
            const stopBtn = document.getElementById('detailStopBtn');
            if (instance.status === 'running') {
                startBtn.disabled = true;
                stopBtn.disabled = false;
            } else {
                startBtn.disabled = false;
                stopBtn.disabled = true;
            }
            
            document.getElementById('instanceDetailModal').style.display = 'block';
        } else {
            utils.msg.error('获取实例详情失败');
        }
    } catch (error) {
        console.error('[RemoteHosting] 获取实例详情错误:', error);
        utils.msg.error('获取实例详情失败');
    }
};

window.closeDetailModal = function() {
    document.getElementById('instanceDetailModal').style.display = 'none';
    currentDetailInstanceId = null;
};

window.startFromDetail = async function() {
    if (!currentDetailInstanceId) return;
    await startHosting(currentDetailInstanceId);
    closeDetailModal();
};

window.stopFromDetail = async function() {
    if (!currentDetailInstanceId) return;
    await stopHosting(currentDetailInstanceId);
    closeDetailModal();
};

window.restartFromDetail = async function() {
    if (!currentDetailInstanceId) return;
    await restartHosting(currentDetailInstanceId);
    closeDetailModal();
};

window.openScaleModal = function(instanceId, currentReplicas, maxReplicas) {
    console.log('[RemoteHosting] 打开扩缩容模态框:', instanceId);
    
    document.getElementById('scaleInstanceId').value = instanceId;
    document.getElementById('currentReplicas').value = currentReplicas || 0;
    document.getElementById('targetReplicas').value = maxReplicas || 1;
    document.getElementById('scaleModal').style.display = 'flex';
};

window.closeScaleModal = function() {
    document.getElementById('scaleModal').style.display = 'none';
};

window.submitScale = async function() {
    const instanceId = document.getElementById('scaleInstanceId').value;
    const replicas = parseInt(document.getElementById('targetReplicas').value);
    
    if (isNaN(replicas) || replicas < 0 || replicas > 10) {
        utils.msg.error('请输入有效的副本数(0-10)');
        return;
    }
    
    console.log('[RemoteHosting] 提交扩缩容:', instanceId, replicas);
    
    try {
        const result = await utils.api.post('/hosting/instances/scale', { 
            instanceId: instanceId, 
            replicas: replicas 
        });
        
        if (result.code === 200 && result.data) {
            closeScaleModal();
            if (hostingTable) hostingTable.refresh();
            loadStats();
            utils.msg.success('扩缩容成功！目标副本数: ' + replicas);
        } else {
            utils.msg.error('扩缩容失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[RemoteHosting] 扩缩容错误:', error);
        utils.msg.error('扩缩容失败');
    }
};

window.startHosting = async function(instanceId) {
    console.log('[RemoteHosting] 启动实例:', instanceId);
    try {
        const result = await utils.api.post('/hosting/instances/start', { instanceId: instanceId });

        if (result.code === 200 && result.data) {
            if (hostingTable) hostingTable.refresh();
            loadStats();
            utils.msg.success('实例启动成功！');
        } else {
            utils.msg.error('启动失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[RemoteHosting] 启动实例错误:', error);
        utils.msg.error('启动失败');
    }
};

window.stopHosting = async function(instanceId) {
    console.log('[RemoteHosting] 停止实例:', instanceId);
    try {
        const result = await utils.api.post('/hosting/instances/stop', { instanceId: instanceId });

        if (result.code === 200 && result.data) {
            if (hostingTable) hostingTable.refresh();
            loadStats();
            utils.msg.success('实例停止成功！');
        } else {
            utils.msg.error('停止失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[RemoteHosting] 停止实例错误:', error);
        utils.msg.error('停止失败');
    }
};

window.restartHosting = async function(instanceId) {
    console.log('[RemoteHosting] 重启实例:', instanceId);
    try {
        const result = await utils.api.post('/hosting/instances/restart', { instanceId: instanceId });
        if (result.code === 200 && result.data) {
            if (hostingTable) hostingTable.refresh();
            loadStats();
            utils.msg.success('实例重启成功！');
        } else {
            utils.msg.error('重启失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[RemoteHosting] 重启实例错误:', error);
        utils.msg.error('重启失败');
    }
};

window.deleteHosting = async function(instanceId) {
    if (!utils.msg.confirm('确定要删除这个实例吗？此操作不可恢复！')) {
        return;
    }

    console.log('[RemoteHosting] 删除实例:', instanceId);
    try {
        const result = await utils.api.post('/hosting/instances/delete', { instanceId: instanceId });

        if (result.code === 200 && result.data) {
            if (hostingTable) hostingTable.refresh();
            loadStats();
            utils.msg.success('实例删除成功！');
        } else {
            utils.msg.error('删除失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[RemoteHosting] 删除实例错误:', error);
        utils.msg.error('删除失败');
    }
};

window.submitHostingForm = async function() {
    console.log('[RemoteHosting] 提交托管表单');

    const instanceId = document.getElementById('hostingId').value;
    const hostingName = document.getElementById('hostingName').value;
    const skillId = document.getElementById('skillId').value;
    const hostingDescription = document.getElementById('hostingDescription').value;
    const hostingStatus = document.getElementById('hostingStatus').value;

    if (!hostingName || hostingName.trim().length < 2) {
        utils.msg.error('实例名称至少2个字符');
        return;
    }
    if (!skillId) {
        utils.msg.error('请选择关联技能');
        return;
    }

    const hostingData = {
        name: hostingName.trim(),
        skillId: skillId,
        description: hostingDescription.trim(),
        status: hostingStatus
    };

    const submitBtn = document.querySelector('#hostingModal .nx-btn--primary');
    const originalText = submitBtn ? submitBtn.textContent : '保存';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 保存中...';
    }

    try {
        let result;
        if (instanceId) {
            result = await utils.api.post('/hosting/instances/update', { 
                instanceId: instanceId, 
                instance: hostingData 
            });
        } else {
            result = await utils.api.post('/hosting/instances/create', hostingData);
        }

        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }

        if (result.code === 200) {
            if (hostingTable) hostingTable.refresh();
            loadStats();
            closeHostingModal();
            utils.msg.success('实例保存成功！');
        } else {
            utils.msg.error('保存失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
        console.error('[RemoteHosting] 保存实例错误:', error);
        utils.msg.error('保存失败');
    }
};

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function () {
        initRemoteHostingPage();
    });
} else {
    initRemoteHostingPage();
}
