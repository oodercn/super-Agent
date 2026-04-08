let currentTemplate = null;
let templateId = null;
let availableCapabilities = [];
let orgTree = [];
let roles = [];
let templateCategoryDict = {};

document.addEventListener('DOMContentLoaded', async function() {
    templateId = getUrlParam('id');
    
    await initDicts();
    
    if (templateId) {
        loadTemplate(templateId);
    } else {
        loadMockTemplate();
    }
    
    loadAvailableCapabilities();
    loadOrgTree();
    loadRoles();
    initTabs();
    initLlmAssistant();
});

async function initDicts() {
    if (typeof DictCache !== 'undefined') {
        await DictCache.init();
        const dict = await DictCache.getDict(DictCache.DICT_CODES.TEMPLATE_CATEGORY);
        if (dict && dict.items) {
            dict.items.forEach(item => {
                templateCategoryDict[item.code] = item.name;
            });
        }
    }
}

function initLlmAssistant() {
    if (typeof LlmAssistant !== 'undefined') {
        LlmAssistant.init();
    }
}

function getUrlParam(name) {
    const params = new URLSearchParams(window.location.search);
    return params.get(name);
}

function initTabs() {
    const tabs = document.querySelectorAll('.nx-tabs__tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            tabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
            
            const tabId = this.getAttribute('data-tab');
            document.querySelectorAll('.tab-content').forEach(content => {
                content.classList.remove('active');
            });
            document.getElementById('tab-' + tabId).classList.add('active');
        });
    });
}

async function loadAvailableCapabilities() {
    try {
        const result = await ApiClient.get('/api/v1/capabilities');
        
        if (result.status === 'success' && result.data) {
            availableCapabilities = result.data;
        }
    } catch (error) {
        console.error('Failed to load capabilities:', error);
        availableCapabilities = getDefaultCapabilities();
    }
}

async function loadOrgTree() {
    try {
        const result = await ApiClient.get('/api/v1/org/tree');
        
        if (result.status === 'success' && result.data) {
            orgTree = result.data;
        }
    } catch (error) {
        console.error('Failed to load org tree:', error);
        orgTree = [];
    }
}

async function loadRoles() {
    try {
        const result = await ApiClient.get('/api/v1/org/roles');
        
        if (result.status === 'success' && result.data) {
            roles = result.data;
        }
    } catch (error) {
        console.error('Failed to load roles:', error);
        roles = getDefaultRoles();
    }
}

function getDefaultCapabilities() {
    return [
        { id: 'report-remind', name: '日志提醒', category: 'notification', description: '定时提醒员工提交日志' },
        { id: 'report-submit', name: '日志提交', category: 'data-input', description: '员工提交工作日志' },
        { id: 'report-aggregate', name: '日志汇总', category: 'data-processing', description: '汇总所有员工日志' },
        { id: 'report-analyze', name: '日志分析', category: 'intelligence', description: 'AI分析日志内容' },
        { id: 'notification-email', name: '邮件通知', category: 'notification', description: '发送邮件通知' },
        { id: 'notification-sms', name: '短信通知', category: 'notification', description: '发送短信通知' },
        { id: 'calendar-event', name: '日历事件', category: 'collaboration', description: '创建日历事件' }
    ];
}

function getDefaultRoles() {
    return [
        { id: 'manager', name: '管理者', description: '场景管理者，拥有完整管理权限' },
        { id: 'employee', name: '员工', description: '普通员工，参与场景执行' },
        { id: 'hr', name: 'HR', description: '人力资源，管理人事相关' },
        { id: 'llm-assistant', name: 'LLM助手', description: 'AI分析助手' },
        { id: 'coordinator', name: '协调Agent', description: '任务协调Agent' }
    ];
}

async function loadTemplate(id) {
    try {
        const result = await ApiClient.get('/api/v1/templates/' + id);
        
        if (result.status === 'success' && result.data) {
            currentTemplate = result.data;
            renderTemplate();
        } else {
            loadMockTemplate();
        }
    } catch (error) {
        console.error('Failed to load template:', error);
        loadMockTemplate();
    }
}

function loadMockTemplate() {
    currentTemplate = {
        templateId: templateId || 'tpl-new',
        name: '新模板',
        version: '1.0.0',
        description: '',
        category: 'business',
        type: 'PRIMARY',
        status: 'draft',
        createTime: Date.now(),
        updateTime: Date.now(),
        capabilities: [],
        roles: [],
        workflow: { triggers: [], steps: [] },
        securityPolicy: { dataIsolation: [], auditLogging: null, accessControl: null }
    };
    renderTemplate();
}

function renderTemplate() {
    document.getElementById('pageTitle').textContent = currentTemplate.name;
    document.getElementById('templateName').textContent = currentTemplate.name;
    document.getElementById('templateId').textContent = currentTemplate.templateId;
    document.getElementById('templateVersion').textContent = currentTemplate.version;
    document.getElementById('templateCategory').textContent = getCategoryName(currentTemplate.category);
    document.getElementById('templateDescription').textContent = currentTemplate.description;
    document.getElementById('templateType').textContent = currentTemplate.type === 'PRIMARY' ? '主场景' : '协作场景';
    document.getElementById('templateCreateTime').textContent = formatTime(currentTemplate.createTime);
    
    const statusBadge = document.getElementById('templateStatus');
    statusBadge.textContent = currentTemplate.status === 'published' ? '已发布' : '草稿';
    statusBadge.className = 'nx-badge ' + (currentTemplate.status === 'published' ? 'nx-badge--success' : 'nx-badge--warning');
    
    document.getElementById('capabilityCount').textContent = currentTemplate.capabilities?.length || 0;
    document.getElementById('roleCount').textContent = currentTemplate.roles?.length || 0;
    document.getElementById('instanceCount').textContent = Math.floor(Math.random() * 10);
    
    renderCapabilityOverview();
    renderCapabilities();
    renderRoles();
    renderWorkflow();
    renderSecurity();
}

function getCategoryName(category) {
    if (templateCategoryDict[category]) {
        return templateCategoryDict[category];
    }
    if (typeof DictCache !== 'undefined') {
        const item = DictCache.getDictItem(DictCache.DICT_CODES.TEMPLATE_CATEGORY, category);
        if (item && item.name) {
            return item.name;
        }
    }
    const names = {
        'business': '业务场景',
        'iot': '物联网场景',
        'collaboration': '协作场景',
        'governance': '治理场景',
        'notification': '通知推送',
        'data-input': '数据输入',
        'data-processing': '数据处理',
        'intelligence': '智能分析',
        'ui': '界面展示',
        'actuation': '设备控制',
        'sensing': '传感器读取'
    };
    return names[category] || category;
}

function formatTime(timestamp) {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', {hour: '2-digit', minute: '2-digit'});
}

function renderCapabilityOverview() {
    const container = document.getElementById('capabilityOverview');
    if (!currentTemplate.capabilities?.length) {
        container.innerHTML = '<p class="nx-text-secondary">暂无能力定义</p>';
        return;
    }
    
    const categories = {};
    currentTemplate.capabilities.forEach(cap => {
        const cat = cap.category || 'other';
        if (!categories[cat]) categories[cat] = [];
        categories[cat].push(cap);
    });
    
    let html = '';
    Object.entries(categories).forEach(([cat, caps]) => {
        html += `<div class="nx-mb-3">
            <div class="nx-text-sm nx-font-medium nx-mb-2">${getCategoryName(cat)}</div>
            <div class="nx-flex nx-flex-wrap nx-gap-2">
                ${caps.map(cap => `<span class="nx-badge nx-badge--secondary">${cap.name}</span>`).join('')}
            </div>
        </div>`;
    });
    container.innerHTML = html;
}

function renderCapabilities() {
    const container = document.getElementById('capabilityList');
    if (!currentTemplate.capabilities?.length) {
        container.innerHTML = '<p class="nx-text-secondary">暂无能力定义</p>';
        return;
    }
    
    let html = '';
    currentTemplate.capabilities.forEach(cap => {
        html += `<div class="capability-card">
            <div class="nx-flex nx-items-start nx-justify-between">
                <div>
                    <div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">
                        <i class="ri-flashlight-line nx-text-primary"></i>
                        <span class="nx-font-medium">${cap.name}</span>
                        <code class="nx-text-sm">${cap.capId}</code>
                        <span class="nx-badge nx-badge--secondary">${getCategoryName(cap.category)}</span>
                    </div>
                    <p class="nx-text-secondary nx-text-sm nx-mb-2">${cap.description}</p>
                    ${cap.llmHint ? `<div class="nx-p-2 nx-bg-secondary nx-rounded nx-text-sm"><strong>LLM提示:</strong> ${cap.llmHint}</div>` : ''}
                </div>
                <div class="nx-flex nx-gap-2">
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="editCapability('${cap.capId}')">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="deleteCapability('${cap.capId}')">
                        <i class="ri-delete-bin-line"></i>
                    </button>
                </div>
            </div>
        </div>`;
    });
    container.innerHTML = html;
}

function renderRoles() {
    const container = document.getElementById('roleList');
    if (!currentTemplate.roles?.length) {
        container.innerHTML = '<p class="nx-text-secondary">暂无角色定义</p>';
        return;
    }
    
    let html = '';
    currentTemplate.roles.forEach(role => {
        let orgBindingHtml = '';
        if (role.orgBinding) {
            if (role.orgBinding.type === 'role') {
                orgBindingHtml = `<span class="nx-badge nx-badge--info">组织角色: ${role.orgBinding.value}</span>`;
            } else if (role.orgBinding.type === 'department') {
                orgBindingHtml = `<span class="nx-badge nx-badge--info">部门: ${role.orgBinding.value}</span>`;
            } else if (role.orgBinding.type === 'user') {
                orgBindingHtml = `<span class="nx-badge nx-badge--info">指定用户: ${role.orgBinding.value}</span>`;
            }
        }
        
        html += `<div class="role-card">
            <div class="nx-flex nx-items-start nx-justify-between">
                <div>
                    <div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">
                        <i class="ri-user-line nx-text-primary"></i>
                        <span class="nx-font-medium">${role.description}</span>
                        <code class="nx-text-sm">${role.name}</code>
                        ${role.required ? '<span class="nx-badge nx-badge--warning">必需</span>' : '<span class="nx-badge nx-badge--secondary">可选</span>'}
                        ${orgBindingHtml}
                    </div>
                    <div class="nx-flex nx-items-center nx-gap-4 nx-text-sm nx-text-secondary nx-mb-2">
                        <span>人数: ${role.minCount} - ${role.maxCount}</span>
                    </div>
                    <div class="nx-text-sm">
                        <span class="nx-text-secondary">能力:</span>
                        ${role.capabilities?.map(c => `<span class="nx-badge nx-badge--secondary nx-mr-1">${c}</span>`).join('') || '-'}
                    </div>
                </div>
                <div class="nx-flex nx-gap-2">
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="editRole('${role.name}')">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="deleteRole('${role.name}')">
                        <i class="ri-delete-bin-line"></i>
                    </button>
                </div>
            </div>
        </div>`;
    });
    container.innerHTML = html;
}

function renderWorkflow() {
    const triggerContainer = document.getElementById('triggerList');
    const stepContainer = document.getElementById('stepList');
    
    if (currentTemplate.workflow?.triggers?.length) {
        triggerContainer.innerHTML = currentTemplate.workflow.triggers.map(t => `
            <div class="nx-flex nx-items-center nx-gap-3 nx-p-3 nx-bg-secondary nx-rounded nx-mb-2">
                <i class="ri-timer-line nx-text-primary"></i>
                <span>${t.type === 'schedule' ? '定时触发' : '手动触发'}</span>
                ${t.cron ? `<code>${t.cron}</code>` : ''}
                <span class="nx-text-secondary">→ ${t.action}</span>
            </div>
        `).join('');
    }
    
    if (currentTemplate.workflow?.steps?.length) {
        stepContainer.innerHTML = currentTemplate.workflow.steps.map((step, index) => `
            <div class="workflow-step">
                <div class="step-icon" style="background: var(--nx-color-primary-bg);">
                    <span class="nx-font-bold nx-text-primary">${index + 1}</span>
                </div>
                <div class="nx-flex-1">
                    <div class="nx-font-medium">${step.name}</div>
                    <div class="nx-text-sm nx-text-secondary">
                        ${step.capability ? `能力: ${step.capability}` : `类型: ${step.type || 'action'}`}
                        ${step.executor ? ` | 执行者: ${step.executor}` : ''}
                    </div>
                </div>
                ${step.dependsOn?.length ? `<div class="nx-text-sm nx-text-secondary">依赖: ${step.dependsOn.join(', ')}</div>` : ''}
            </div>
        `).join('');
    }
}

function renderSecurity() {
    const isolationContainer = document.getElementById('dataIsolationList');
    const auditContainer = document.getElementById('auditConfig');
    
    if (currentTemplate.securityPolicy?.dataIsolation?.length) {
        isolationContainer.innerHTML = currentTemplate.securityPolicy.dataIsolation.map(rule => `
            <div class="nx-flex nx-items-center nx-justify-between nx-p-3 nx-bg-secondary nx-rounded nx-mb-2">
                <div>
                    <span class="nx-font-medium">${rule.domain}</span>
                    ${rule.description ? `<span class="nx-text-secondary nx-ml-2">(${rule.description})</span>` : ''}
                </div>
                <div class="nx-flex nx-gap-1">
                    ${rule.access.map(a => `<span class="nx-badge nx-badge--secondary">${a}</span>`).join('')}
                </div>
            </div>
        `).join('');
    }
    
    if (currentTemplate.securityPolicy?.auditLogging) {
        const audit = currentTemplate.securityPolicy.auditLogging;
        auditContainer.innerHTML = `
            <div class="nx-p-3 nx-bg-secondary nx-rounded">
                <div class="nx-flex nx-items-center nx-justify-between nx-mb-2">
                    <span>日志级别</span>
                    <span class="nx-badge nx-badge--info">${audit.level}</span>
                </div>
                <div class="nx-flex nx-items-center nx-justify-between nx-mb-2">
                    <span>保留天数</span>
                    <span>${audit.retention} 天</span>
                </div>
                ${audit.events ? `
                <div class="nx-flex nx-items-center nx-justify-between">
                    <span>审计事件</span>
                    <span>${audit.events.join(', ')}</span>
                </div>
                ` : ''}
            </div>
        `;
    }
}

function addCapability() {
    const modal = document.getElementById('capabilityModal');
    
    document.getElementById('capabilityModalTitle').textContent = '添加能力';
    document.getElementById('capabilityForm').reset();
    document.getElementById('capId').value = '';
    document.getElementById('capName').value = '';
    document.getElementById('capDescription').value = '';
    document.getElementById('capLlmHint').value = '';
    document.getElementById('selectedCapabilityDisplay').textContent = '-';
    modal.classList.add('open');
}

function selectCapabilityFromList() {
    NxSelectors.showListSelector({
        title: '选择能力',
        dataUrl: '/api/v1/selectors/capabilities',
        valueField: 'id',
        displayField: 'name',
        descField: 'description',
        onSelect: function(item) {
            document.getElementById('capId').value = item.id;
            document.getElementById('capName').value = item.name;
            document.getElementById('capCategory').value = item.type || '';
            document.getElementById('capDescription').value = item.description || '';
            document.getElementById('selectedCapabilityDisplay').textContent = item.name + ' (' + item.id + ')';
        }
    });
}

function closeCapabilityModal() {
    document.getElementById('capabilityModal').style.display = 'none';
}

function onCapabilitySelect() {
    const select = document.getElementById('capSelect');
    const option = select.options[select.selectedIndex];
    
    if (option.value) {
        document.getElementById('capId').value = option.value;
        document.getElementById('capName').value = option.dataset.name || '';
        document.getElementById('capCategory').value = option.dataset.category || '';
        document.getElementById('capDescription').value = option.dataset.description || '';
    }
}

async function saveCapability() {
    const capability = {
        capId: document.getElementById('capId').value || document.getElementById('capSelect').value,
        name: document.getElementById('capName').value,
        description: document.getElementById('capDescription').value,
        category: document.getElementById('capCategory').value,
        llmHint: document.getElementById('capLlmHint').value
    };
    
    if (!capability.capId) {
        alert('请选择能力');
        return;
    }
    
    try {
        const result = await ApiClient.post('/api/v1/templates/' + templateId + '/capabilities', capability);
        
        if (result.status === 'success') {
            if (!currentTemplate.capabilities) currentTemplate.capabilities = [];
            const existingIndex = currentTemplate.capabilities.findIndex(c => c.capId === capability.capId);
            if (existingIndex >= 0) {
                currentTemplate.capabilities[existingIndex] = capability;
            } else {
                currentTemplate.capabilities.push(capability);
            }
            closeCapabilityModal();
            renderCapabilities();
            renderCapabilityOverview();
            document.getElementById('capabilityCount').textContent = currentTemplate.capabilities.length;
        } else {
            alert('保存失败: ' + result.message);
        }
    } catch (error) {
        console.error('Save capability failed:', error);
        alert('保存失败');
    }
}

function editCapability(capId) {
    const cap = currentTemplate.capabilities?.find(c => c.capId === capId);
    if (cap) {
        document.getElementById('capabilityModalTitle').textContent = '编辑能力';
        document.getElementById('capSelect').innerHTML = `<option value="${cap.capId}">${cap.name} (${cap.capId})</option>`;
        document.getElementById('capId').value = cap.capId;
        document.getElementById('capName').value = cap.name;
        document.getElementById('capCategory').value = cap.category || '';
        document.getElementById('capDescription').value = cap.description || '';
        document.getElementById('capLlmHint').value = cap.llmHint || '';
        document.getElementById('capabilityModal').style.display = 'flex';
    }
}

async function deleteCapability(capId) {
    if (!confirm('确定要删除此能力吗？')) return;
    
    try {
        const result = await ApiClient.delete('/api/v1/templates/' + templateId + '/capabilities/' + capId);
        
        if (result.status === 'success') {
            currentTemplate.capabilities = currentTemplate.capabilities?.filter(c => c.capId !== capId);
            renderCapabilities();
            renderCapabilityOverview();
            document.getElementById('capabilityCount').textContent = currentTemplate.capabilities?.length || 0;
        } else {
            alert('删除失败: ' + result.message);
        }
    } catch (error) {
        console.error('Delete capability failed:', error);
        alert('删除失败');
    }
}

function addRole() {
    const modal = document.getElementById('roleModal');
    
    const roleSelect = document.getElementById('roleTypeSelect');
    roleSelect.innerHTML = '<option value="">请选择角色类型</option>';
    roles.forEach(r => {
        roleSelect.innerHTML += `<option value="${r.id}" data-description="${r.description}">${r.name}</option>`;
    });
    
    const orgSelect = document.getElementById('roleOrgBinding');
    orgSelect.innerHTML = '<option value="">不绑定组织</option>';
    orgSelect.innerHTML += '<optgroup label="按角色绑定">';
    orgSelect.innerHTML += `<option value="role:manager">管理者角色</option>`;
    orgSelect.innerHTML += `<option value="role:employee">员工角色</option>`;
    orgSelect.innerHTML += `<option value="role:hr">HR角色</option>`;
    orgSelect.innerHTML += '</optgroup>';
    orgSelect.innerHTML += '<optgroup label="按部门绑定">';
    orgTree.forEach(dept => {
        orgSelect.innerHTML += `<option value="department:${dept.id}">${dept.name}</option>`;
    });
    orgSelect.innerHTML += '</optgroup>';
    
    const capSelect = document.getElementById('roleCapabilities');
    capSelect.innerHTML = '';
    availableCapabilities.forEach(cap => {
        capSelect.innerHTML += `<option value="${cap.id}">${cap.name}</option>`;
    });
    
    document.getElementById('roleModalTitle').textContent = '添加角色';
    document.getElementById('roleForm').reset();
    modal.style.display = 'flex';
}

function closeRoleModal() {
    document.getElementById('roleModal').style.display = 'none';
}

function onRoleTypeSelect() {
    const select = document.getElementById('roleTypeSelect');
    const option = select.options[select.selectedIndex];
    
    if (option.value) {
        document.getElementById('roleName').value = option.value;
        document.getElementById('roleDescription').value = option.dataset.description || '';
    }
}

async function saveRole() {
    const orgBindingValue = document.getElementById('roleOrgBinding').value;
    let orgBinding = null;
    
    if (orgBindingValue) {
        const [type, value] = orgBindingValue.split(':');
        orgBinding = { type, value };
    }
    
    const role = {
        name: document.getElementById('roleName').value,
        description: document.getElementById('roleDescription').value,
        minCount: parseInt(document.getElementById('roleMinCount').value) || 1,
        maxCount: parseInt(document.getElementById('roleMaxCount').value) || 10,
        required: document.getElementById('roleRequired').value === 'true',
        capabilities: Array.from(document.getElementById('roleCapabilities').selectedOptions).map(o => o.value),
        orgBinding: orgBinding
    };
    
    if (!role.name) {
        alert('请输入角色名称');
        return;
    }
    
    try {
        const result = await ApiClient.post('/api/v1/templates/' + templateId + '/roles', role);
        
        if (result.status === 'success') {
            if (!currentTemplate.roles) currentTemplate.roles = [];
            const existingIndex = currentTemplate.roles.findIndex(r => r.name === role.name);
            if (existingIndex >= 0) {
                currentTemplate.roles[existingIndex] = role;
            } else {
                currentTemplate.roles.push(role);
            }
            closeRoleModal();
            renderRoles();
            document.getElementById('roleCount').textContent = currentTemplate.roles.length;
        } else {
            alert('保存失败: ' + result.message);
        }
    } catch (error) {
        console.error('Save role failed:', error);
        alert('保存失败');
    }
}

function editRole(roleName) {
    const role = currentTemplate.roles?.find(r => r.name === roleName);
    if (role) {
        document.getElementById('roleModalTitle').textContent = '编辑角色';
        document.getElementById('roleName').value = role.name;
        document.getElementById('roleDescription').value = role.description || '';
        document.getElementById('roleMinCount').value = role.minCount || 1;
        document.getElementById('roleMaxCount').value = role.maxCount || 10;
        document.getElementById('roleRequired').value = role.required ? 'true' : 'false';
        
        if (role.orgBinding) {
            document.getElementById('roleOrgBinding').value = `${role.orgBinding.type}:${role.orgBinding.value}`;
        }
        
        const capSelect = document.getElementById('roleCapabilities');
        Array.from(capSelect.options).forEach(opt => {
            opt.selected = role.capabilities?.includes(opt.value);
        });
        
        document.getElementById('roleModal').style.display = 'flex';
    }
}

async function deleteRole(roleName) {
    if (!confirm('确定要删除此角色吗？')) return;
    
    try {
        const result = await ApiClient.delete('/api/v1/templates/' + templateId + '/roles/' + encodeURIComponent(roleName));
        
        if (result.status === 'success') {
            currentTemplate.roles = currentTemplate.roles?.filter(r => r.name !== roleName);
            renderRoles();
            document.getElementById('roleCount').textContent = currentTemplate.roles?.length || 0;
        } else {
            alert('删除失败: ' + result.message);
        }
    } catch (error) {
        console.error('Delete role failed:', error);
        alert('删除失败');
    }
}

function editWorkflow() {
    if (!currentTemplate) {
        alert('请先加载模板');
        return;
    }
    
    const workflow = currentTemplate.workflow || { triggers: [], steps: [] };
    
    const modalHtml = `
        <div class="modal-overlay" id="workflowModal" style="display: flex;">
            <div class="modal" style="max-width: 800px;">
                <div class="modal-header">
                    <h3><i class="ri-flow-chart"></i> 编辑工作流</h3>
                    <button class="modal-close" onclick="closeWorkflowModal()">
                        <i class="ri-close-line"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="nx-mb-4">
                        <h4 class="nx-text-sm nx-font-semibold nx-mb-2">触发器</h4>
                        <div id="workflowTriggers">
                            ${(workflow.triggers || []).map((t, i) => `
                                <div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">
                                    <select class="nx-input" style="width: 150px;" data-trigger-type="${i}">
                                        <option value="MANUAL" ${t.type === 'MANUAL' ? 'selected' : ''}>手动触发</option>
                                        <option value="SCHEDULE" ${t.type === 'SCHEDULE' ? 'selected' : ''}>定时触发</option>
                                        <option value="EVENT" ${t.type === 'EVENT' ? 'selected' : ''}>事件触发</option>
                                    </select>
                                    <input type="text" class="nx-input" style="flex: 1;" value="${t.config || ''}" placeholder="配置参数" data-trigger-config="${i}">
                                    <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="removeWorkflowTrigger(${i})">
                                        <i class="ri-delete-bin-line"></i>
                                    </button>
                                </div>
                            `).join('')}
                        </div>
                        <button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="addWorkflowTrigger()">
                            <i class="ri-add-line"></i> 添加触发器
                        </button>
                    </div>
                    <div>
                        <h4 class="nx-text-sm nx-font-semibold nx-mb-2">执行步骤</h4>
                        <div id="workflowSteps">
                            ${(workflow.steps || []).map((s, i) => `
                                <div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">
                                    <span class="nx-badge nx-badge--secondary">${i + 1}</span>
                                    <input type="text" class="nx-input" style="flex: 1;" value="${s.name || ''}" placeholder="步骤名称" data-step-name="${i}">
                                    <select class="nx-input" style="width: 150px;" data-step-action="${i}">
                                        <option value="INVOKE" ${s.action === 'INVOKE' ? 'selected' : ''}>调用能力</option>
                                        <option value="WAIT" ${s.action === 'WAIT' ? 'selected' : ''}>等待</option>
                                        <option value="NOTIFY" ${s.action === 'NOTIFY' ? 'selected' : ''}>通知</option>
                                        <option value="APPROVE" ${s.action === 'APPROVE' ? 'selected' : ''}>审批</option>
                                    </select>
                                    <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="removeWorkflowStep(${i})">
                                        <i class="ri-delete-bin-line"></i>
                                    </button>
                                </div>
                            `).join('')}
                        </div>
                        <button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="addWorkflowStep()">
                            <i class="ri-add-line"></i> 添加步骤
                        </button>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="nx-btn nx-btn--secondary" onclick="closeWorkflowModal()">取消</button>
                    <button class="nx-btn nx-btn--primary" onclick="saveWorkflow()">保存</button>
                </div>
            </div>
        </div>
    `;
    
    const existingModal = document.getElementById('workflowModal');
    if (existingModal) existingModal.remove();
    
    document.body.insertAdjacentHTML('beforeend', modalHtml);
}

function closeWorkflowModal() {
    const modal = document.getElementById('workflowModal');
    if (modal) modal.remove();
}

function addWorkflowTrigger() {
    const container = document.getElementById('workflowTriggers');
    const index = container.children.length;
    const html = `
        <div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">
            <select class="nx-input" style="width: 150px;" data-trigger-type="${index}">
                <option value="MANUAL">手动触发</option>
                <option value="SCHEDULE">定时触发</option>
                <option value="EVENT">事件触发</option>
            </select>
            <input type="text" class="nx-input" style="flex: 1;" placeholder="配置参数" data-trigger-config="${index}">
            <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="this.parentElement.remove()">
                <i class="ri-delete-bin-line"></i>
            </button>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', html);
}

function addWorkflowStep() {
    const container = document.getElementById('workflowSteps');
    const index = container.children.length;
    const html = `
        <div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">
            <span class="nx-badge nx-badge--secondary">${index + 1}</span>
            <input type="text" class="nx-input" style="flex: 1;" placeholder="步骤名称" data-step-name="${index}">
            <select class="nx-input" style="width: 150px;" data-step-action="${index}">
                <option value="INVOKE">调用能力</option>
                <option value="WAIT">等待</option>
                <option value="NOTIFY">通知</option>
                <option value="APPROVE">审批</option>
            </select>
            <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="this.parentElement.remove()">
                <i class="ri-delete-bin-line"></i>
            </button>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', html);
}

function saveWorkflow() {
    if (!currentTemplate) return;
    
    const triggers = [];
    document.querySelectorAll('#workflowTriggers > div').forEach((div, i) => {
        const typeSelect = div.querySelector('[data-trigger-type]');
        const configInput = div.querySelector('[data-trigger-config]');
        if (typeSelect && configInput) {
            triggers.push({
                type: typeSelect.value,
                config: configInput.value
            });
        }
    });
    
    const steps = [];
    document.querySelectorAll('#workflowSteps > div').forEach((div, i) => {
        const nameInput = div.querySelector('[data-step-name]');
        const actionSelect = div.querySelector('[data-step-action]');
        if (nameInput && actionSelect) {
            steps.push({
                name: nameInput.value,
                action: actionSelect.value
            });
        }
    });
    
    currentTemplate.workflow = { triggers, steps };
    closeWorkflowModal();
    renderWorkflow();
    alert('工作流已更新，请保存模板以生效');
}

function renderWorkflow() {
    if (!currentTemplate || !currentTemplate.workflow) return;
    
    const workflow = currentTemplate.workflow;
    
    const triggerList = document.getElementById('triggerList');
    if (triggerList && workflow.triggers && workflow.triggers.length > 0) {
        triggerList.innerHTML = workflow.triggers.map(t => `
            <div class="nx-flex nx-items-center nx-gap-2 nx-py-2">
                <i class="ri-flashlight-line nx-text-primary"></i>
                <span>${t.type === 'MANUAL' ? '手动触发' : t.type === 'SCHEDULE' ? '定时触发' : '事件触发'}</span>
                <span class="nx-text-secondary nx-text-sm">${t.config || ''}</span>
            </div>
        `).join('');
    }
    
    const stepList = document.getElementById('stepList');
    if (stepList && workflow.steps && workflow.steps.length > 0) {
        stepList.innerHTML = workflow.steps.map((s, i) => `
            <div class="nx-flex nx-items-center nx-gap-2 nx-py-2">
                <span class="nx-badge nx-badge--primary">${i + 1}</span>
                <span>${s.name || '未命名步骤'}</span>
                <span class="nx-badge nx-badge--secondary">${s.action}</span>
            </div>
        `).join('');
    }
}

function editSecurity() {
    if (!currentTemplate) {
        alert('请先加载模板');
        return;
    }
    
    const security = currentTemplate.security || { dataIsolation: [], auditConfig: {} };
    
    const modalHtml = `
        <div class="modal-overlay" id="securityModal" style="display: flex;">
            <div class="modal" style="max-width: 800px;">
                <div class="modal-header">
                    <h3><i class="ri-shield-check-line"></i> 编辑安全策略</h3>
                    <button class="modal-close" onclick="closeSecurityModal()">
                        <i class="ri-close-line"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="nx-mb-4">
                        <h4 class="nx-text-sm nx-font-semibold nx-mb-2">数据隔离规则</h4>
                        <div id="dataIsolationRules">
                            ${(security.dataIsolation || []).map((r, i) => `
                                <div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">
                                    <select class="nx-input" style="width: 150px;" data-isolation-type="${i}">
                                        <option value="ORG" ${r.type === 'ORG' ? 'selected' : ''}>组织隔离</option>
                                        <option value="ROLE" ${r.type === 'ROLE' ? 'selected' : ''}>角色隔离</option>
                                        <option value="USER" ${r.type === 'USER' ? 'selected' : ''}>用户隔离</option>
                                    </select>
                                    <input type="text" class="nx-input" style="flex: 1;" value="${r.field || ''}" placeholder="隔离字段" data-isolation-field="${i}">
                                    <label class="nx-flex nx-items-center nx-gap-1">
                                        <input type="checkbox" ${r.enforced ? 'checked' : ''} data-isolation-enforced="${i}">
                                        <span class="nx-text-sm">强制执行</span>
                                    </label>
                                    <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="this.parentElement.remove()">
                                        <i class="ri-delete-bin-line"></i>
                                    </button>
                                </div>
                            `).join('')}
                        </div>
                        <button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="addDataIsolationRule()">
                            <i class="ri-add-line"></i> 添加规则
                        </button>
                    </div>
                    <div>
                        <h4 class="nx-text-sm nx-font-semibold nx-mb-2">审计日志配置</h4>
                        <div class="nx-grid nx-grid-cols-2 nx-gap-4">
                            <label class="nx-flex nx-items-center nx-gap-2">
                                <input type="checkbox" id="auditEnabled" ${security.auditConfig?.enabled !== false ? 'checked' : ''}>
                                <span>启用审计日志</span>
                            </label>
                            <label class="nx-flex nx-items-center nx-gap-2">
                                <input type="checkbox" id="auditDataAccess" ${security.auditConfig?.dataAccess === true ? 'checked' : ''}>
                                <span>记录数据访问</span>
                            </label>
                            <label class="nx-flex nx-items-center nx-gap-2">
                                <input type="checkbox" id="auditCapabilityCall" ${security.auditConfig?.capabilityCall !== false ? 'checked' : ''}>
                                <span>记录能力调用</span>
                            </label>
                            <label class="nx-flex nx-items-center nx-gap-2">
                                <input type="checkbox" id="auditSensitiveOp" ${security.auditConfig?.sensitiveOp === true ? 'checked' : ''}>
                                <span>记录敏感操作</span>
                            </label>
                        </div>
                        <div class="nx-mt-3">
                            <label class="nx-form-label">日志保留天数</label>
                            <input type="number" class="nx-input" style="width: 150px;" id="auditRetentionDays" value="${security.auditConfig?.retentionDays || 90}" min="1" max="365">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="nx-btn nx-btn--secondary" onclick="closeSecurityModal()">取消</button>
                    <button class="nx-btn nx-btn--primary" onclick="saveSecurity()">保存</button>
                </div>
            </div>
        </div>
    `;
    
    const existingModal = document.getElementById('securityModal');
    if (existingModal) existingModal.remove();
    
    document.body.insertAdjacentHTML('beforeend', modalHtml);
}

function closeSecurityModal() {
    const modal = document.getElementById('securityModal');
    if (modal) modal.remove();
}

function addDataIsolationRule() {
    const container = document.getElementById('dataIsolationRules');
    const index = container.children.length;
    const html = `
        <div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">
            <select class="nx-input" style="width: 150px;" data-isolation-type="${index}">
                <option value="ORG">组织隔离</option>
                <option value="ROLE">角色隔离</option>
                <option value="USER">用户隔离</option>
            </select>
            <input type="text" class="nx-input" style="flex: 1;" placeholder="隔离字段" data-isolation-field="${index}">
            <label class="nx-flex nx-items-center nx-gap-1">
                <input type="checkbox" data-isolation-enforced="${index}">
                <span class="nx-text-sm">强制执行</span>
            </label>
            <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="this.parentElement.remove()">
                <i class="ri-delete-bin-line"></i>
            </button>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', html);
}

function saveSecurity() {
    if (!currentTemplate) return;
    
    const dataIsolation = [];
    document.querySelectorAll('#dataIsolationRules > div').forEach((div, i) => {
        const typeSelect = div.querySelector('[data-isolation-type]');
        const fieldInput = div.querySelector('[data-isolation-field]');
        const enforcedCheckbox = div.querySelector('[data-isolation-enforced]');
        if (typeSelect && fieldInput) {
            dataIsolation.push({
                type: typeSelect.value,
                field: fieldInput.value,
                enforced: enforcedCheckbox ? enforcedCheckbox.checked : false
            });
        }
    });
    
    const auditConfig = {
        enabled: document.getElementById('auditEnabled')?.checked !== false,
        dataAccess: document.getElementById('auditDataAccess')?.checked === true,
        capabilityCall: document.getElementById('auditCapabilityCall')?.checked !== false,
        sensitiveOp: document.getElementById('auditSensitiveOp')?.checked === true,
        retentionDays: parseInt(document.getElementById('auditRetentionDays')?.value || '90', 10)
    };
    
    currentTemplate.security = { dataIsolation, auditConfig };
    closeSecurityModal();
    renderSecurity();
    alert('安全策略已更新，请保存模板以生效');
}

function renderSecurity() {
    if (!currentTemplate || !currentTemplate.security) return;
    
    const security = currentTemplate.security;
    
    const dataIsolationList = document.getElementById('dataIsolationList');
    if (dataIsolationList && security.dataIsolation && security.dataIsolation.length > 0) {
        dataIsolationList.innerHTML = security.dataIsolation.map(r => `
            <div class="nx-flex nx-items-center nx-gap-2 nx-py-2">
                <i class="ri-lock-line nx-text-primary"></i>
                <span>${r.type === 'ORG' ? '组织隔离' : r.type === 'ROLE' ? '角色隔离' : '用户隔离'}</span>
                <span class="nx-text-secondary nx-text-sm">${r.field || ''}</span>
                ${r.enforced ? '<span class="nx-badge nx-badge--warning">强制</span>' : ''}
            </div>
        `).join('');
    }
    
    const auditConfig = document.getElementById('auditConfig');
    if (auditConfig && security.auditConfig) {
        const ac = security.auditConfig;
        auditConfig.innerHTML = `
            <div class="nx-space-y-2">
                <div class="nx-flex nx-items-center nx-gap-2">
                    <i class="ri-${ac.enabled !== false ? 'checkbox-circle' : 'close-circle'}-line ${ac.enabled !== false ? 'nx-text-success' : 'nx-text-secondary'}"></i>
                    <span>审计日志: ${ac.enabled !== false ? '已启用' : '已禁用'}</span>
                </div>
                <div class="nx-flex nx-items-center nx-gap-2">
                    <i class="ri-${ac.capabilityCall !== false ? 'checkbox-circle' : 'close-circle'}-line ${ac.capabilityCall !== false ? 'nx-text-success' : 'nx-text-secondary'}"></i>
                    <span>能力调用记录: ${ac.capabilityCall !== false ? '已启用' : '已禁用'}</span>
                </div>
                <div class="nx-text-sm nx-text-secondary">日志保留: ${ac.retentionDays || 90} 天</div>
            </div>
        `;
    }
}

function editTemplate() {
    window.location.href = 'template-detail.html?id=' + currentTemplate.templateId + '&edit=true';
}

async function saveTemplate() {
    if (!currentTemplate || !currentTemplate.templateId) {
        alert('模板数据无效');
        return;
    }
    
    try {
        const result = await ApiClient.put('/api/v1/templates/' + currentTemplate.templateId, currentTemplate);
        
        if (result.status === 'success') {
            alert('保存成功');
            loadTemplate(currentTemplate.templateId);
        } else {
            alert('保存失败: ' + result.message);
        }
    } catch (error) {
        console.error('Save template failed:', error);
        alert('保存失败');
    }
}

function createSceneGroup() {
    document.getElementById('sceneGroupForm').reset();
    document.getElementById('sgName').value = currentTemplate.name + ' - 实例';
    document.getElementById('sceneGroupModal').style.display = 'flex';
}

function closeSceneGroupModal() {
    document.getElementById('sceneGroupModal').style.display = 'none';
}

async function saveSceneGroup() {
    const config = {
        name: document.getElementById('sgName').value,
        description: document.getElementById('sgDescription').value,
        minMembers: parseInt(document.getElementById('sgMinMembers').value) || 1,
        maxMembers: parseInt(document.getElementById('sgMaxMembers').value) || 100,
        creatorId: 'current-user',
        creatorType: 'USER'
    };
    
    const request = {
        templateId: currentTemplate.templateId,
        config: config
    };
    
    try {
        const result = await ApiClient.post('/api/v1/scene-groups', request);
        
        if (result.status === 'success') {
            closeSceneGroupModal();
            window.location.href = 'scene-group-detail.html?id=' + result.data.sceneGroupId;
        } else {
            alert('创建失败: ' + result.message);
        }
    } catch (error) {
        console.error('Failed to create scene group:', error);
        alert('创建失败');
    }
}
