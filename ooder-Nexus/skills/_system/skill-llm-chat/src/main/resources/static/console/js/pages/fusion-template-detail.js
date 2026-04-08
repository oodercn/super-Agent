let templateId = null;
let templateData = null;
let currentConflict = null;
let fusionFlowChart = null;
let activationFlowChart = null;
let fusionCompareView = null;
let conflictResolver = null;
let roleView = null;
let selectedBatchOption = null;
let currentConflictFilter = 'all';

$(document).ready(function() {
    templateId = getUrlParam('id');
    if (templateId) {
        loadTemplateData();
    } else {
        showError('缺少模板ID参数');
    }
});

function loadTemplateData() {
    $.get(`/api/v1/fused-templates/${templateId}`, function(data) {
        templateData = data;
        renderBasicInfo(data);
        renderFusionCompare(data);
        renderConflicts(data.fusionConflicts);
        renderRoles(data.roles);
        renderActivationSteps(data.activationSteps);
        renderMatchScore(data.matchScore);
        renderStrategy(data.fusionStrategy);
        loadVersionHistory();
        initFlowCharts(data);
    }).fail(function() {
        showError('加载模板数据失败');
    });
}

function renderBasicInfo(data) {
    $('#pageTitle').text(data.name);
    $('#infoTemplateId').text(data.templateId);
    $('#infoName').text(data.name);
    $('#infoMatchScore').html(`<span class="match-badge">${data.matchScore}%</span>`);
    $('#infoFusionTime').text(formatTime(data.fusionTime));
    $('#infoDescription').text(data.description || '-');
    
    if (data.enterpriseProcedureId) {
        loadProcedureInfo(data.enterpriseProcedureId);
    }
    if (data.skillId) {
        loadSkillInfo(data.skillId);
    }
}

function loadProcedureInfo(procedureId) {
    $.get(`/api/v1/enterprise-procedures/${procedureId}`, function(data) {
        $('#infoProcedureName').html(`<a href="enterprise-procedure-detail.html?id=${procedureId}">${data.name}</a>`);
        $('#backToProcedure').attr('href', `enterprise-procedure-detail.html?id=${procedureId}`);
    });
}

function loadSkillInfo(skillId) {
    $('#infoSkillName').text(skillId);
}

function renderFusionCompare(data) {
    renderEnterprisePanel(data.enterpriseProcedureId);
    renderSkillPanel(data.skillId);
    renderResultPanel(data);
}

function renderEnterprisePanel(procedureId) {
    if (!procedureId) {
        $('#enterprisePanel').html('<div class="empty-state">未关联企业规范流程</div>');
        return;
    }
    
    $.get(`/api/v1/enterprise-procedures/${procedureId}`, function(procedure) {
        let html = '<div class="panel-section"><h4>角色定义</h4><ul>';
        if (procedure.roles) {
            procedure.roles.forEach(role => {
                html += `<li>${role.name} (${role.minCount}-${role.maxCount === 0 ? '∞' : role.maxCount}人)</li>`;
            });
        }
        html += '</ul></div>';
        
        html += '<div class="panel-section"><h4>流程步骤</h4><ol>';
        if (procedure.steps) {
            procedure.steps.forEach(step => {
                html += `<li>${step.name}</li>`;
            });
        }
        html += '</ol></div>';
        
        $('#enterprisePanel').html(html);
    });
}

function renderSkillPanel(skillId) {
    if (!skillId) {
        $('#skillPanel').html('<div class="empty-state">未关联技能</div>');
        return;
    }
    
    $.get(`/api/v1/install/skills/${skillId}`, function(skill) {
        let html = '<div class="panel-section"><h4>技能信息</h4>';
        html += `<p><strong>名称:</strong> ${skill.name}</p>`;
        html += `<p><strong>分类:</strong> ${skill.category || '-'}</p>`;
        html += `<p><strong>意图:</strong> ${skill.intent || '-'}</p>`;
        html += '</div>';
        
        if (skill.sceneTemplate) {
            html += '<div class="panel-section"><h4>场景模板角色</h4><ul>';
            if (skill.sceneTemplate.roles) {
                skill.sceneTemplate.roles.forEach(role => {
                    html += `<li>${role.name}</li>`;
                });
            }
            html += '</ul></div>';
        }
        
        $('#skillPanel').html(html);
    }).fail(function() {
        $('#skillPanel').html('<div class="empty-state">加载技能信息失败</div>');
    });
}

function renderResultPanel(data) {
    let html = '<div class="panel-section"><h4>融合后角色</h4><ul>';
    if (data.roles) {
        data.roles.forEach(role => {
            html += `<li>${role.name} <span class="source-tag">${role.source || 'MERGED'}</span></li>`;
        });
    }
    html += '</ul></div>';
    
    html += '<div class="panel-section"><h4>激活步骤</h4>';
    if (data.activationSteps) {
        Object.keys(data.activationSteps).forEach(role => {
            html += `<p><strong>${role}:</strong> ${data.activationSteps[role].length} 个步骤</p>`;
        });
    }
    html += '</div>';
    
    $('#resultPanel').html(html);
}

function renderConflicts(conflicts) {
    if (!conflicts || conflicts.length === 0) {
        $('#conflictsCard').hide();
        return;
    }
    
    $('#conflictsCard').show();
    
    const resolvedCount = conflicts.filter(c => c.resolution !== null).length;
    const unresolvedCount = conflicts.length - resolvedCount;
    const progress = Math.round((resolvedCount / conflicts.length) * 100);
    
    $('#resolvedCount').text(`${resolvedCount} 已解决`).removeClass('success warning')
        .addClass(resolvedCount === conflicts.length ? 'success' : 'warning');
    $('#unresolvedCount').text(`${unresolvedCount} 待解决`).removeClass('success warning')
        .addClass(unresolvedCount > 0 ? 'warning' : 'success');
    $('#conflictProgressFill').css('width', `${progress}%`);
    $('#conflictProgressText').text(`${progress}%`);
    
    if (unresolvedCount > 0) {
        $('#batchResolution').show();
    } else {
        $('#batchResolution').hide();
    }
    
    let filteredConflicts = conflicts;
    if (currentConflictFilter === 'unresolved') {
        filteredConflicts = conflicts.filter(c => !c.resolution);
    } else if (currentConflictFilter === 'resolved') {
        filteredConflicts = conflicts.filter(c => c.resolution);
    }
    
    const container = $('#conflictsList');
    container.empty();
    
    if (filteredConflicts.length === 0) {
        container.html('<div class="empty-state">没有符合条件的冲突</div>');
        return;
    }
    
    filteredConflicts.forEach((conflict, index) => {
        const resolved = conflict.resolution !== null;
        const originalIndex = conflicts.indexOf(conflict);
        
        container.append(`
            <div class="conflict-item ${resolved ? 'resolved' : 'unresolved'}">
                <div class="conflict-item-header">
                    <div class="conflict-item-info">
                        <div class="conflict-item-icon">
                            <i class="ri-${resolved ? 'check' : 'alert'}-line"></i>
                        </div>
                        <div>
                            <div class="conflict-item-title">${conflict.field}</div>
                            <div class="conflict-item-type">${conflict.type}</div>
                        </div>
                    </div>
                    <span class="conflict-item-status ${resolved ? 'resolved' : 'unresolved'}">
                        ${resolved ? '已解决' : '待解决'}
                    </span>
                </div>
                <div class="conflict-item-body">
                    <div class="conflict-values">
                        <div class="conflict-value-card enterprise">
                            <div class="conflict-value-label">
                                <i class="ri-building-line"></i> 企业规范值
                            </div>
                            <div class="conflict-value-content ${conflict.enterpriseValue === null ? 'empty' : ''}">
                                ${formatValue(conflict.enterpriseValue)}
                            </div>
                        </div>
                        <div class="conflict-value-card skill">
                            <div class="conflict-value-label">
                                <i class="ri-robot-line"></i> 技能定义值
                            </div>
                            <div class="conflict-value-content ${conflict.skillValue === null ? 'empty' : ''}">
                                ${formatValue(conflict.skillValue)}
                            </div>
                        </div>
                    </div>
                    ${resolved ? `
                        <div class="conflict-resolution-section">
                            <div class="conflict-resolution-label">解决方案</div>
                            <div class="conflict-resolution-value">
                                <i class="ri-check-line"></i>
                                ${getResolutionLabel(conflict.resolution)}: ${formatValue(conflict.resolvedValue)}
                            </div>
                        </div>
                    ` : `
                        <div class="conflict-actions">
                            <button class="conflict-btn conflict-btn-primary" onclick="openConflictResolver(${originalIndex})">
                                <i class="ri-settings-4-line"></i> 解决冲突
                            </button>
                        </div>
                    `}
                </div>
            </div>
        `);
    });
}

function getResolutionLabel(resolution) {
    const labels = {
        'ENTERPRISE': '使用企业规范',
        'SKILL': '使用技能定义',
        'CUSTOM': '自定义值'
    };
    return labels[resolution] || resolution;
}

function renderRoles(roles) {
    const container = $('#rolesContainer');
    container.empty();
    
    if (!roles || roles.length === 0) {
        container.html('<div class="empty-state">暂无角色定义</div>');
        return;
    }
    
    roles.forEach(role => {
        container.append(`
            <div class="role-card">
                <div class="role-header">
                    <h4>${role.name}</h4>
                    <span class="source-tag">${role.source || 'MERGED'}</span>
                </div>
                <div class="role-body">
                    <div class="role-info">
                        <span class="role-id">${role.roleId}</span>
                        <span class="role-count">${role.minCount} - ${role.maxCount === 0 ? '∞' : role.maxCount} 人</span>
                        ${role.required ? '<span class="role-badge required">必需</span>' : ''}
                    </div>
                </div>
            </div>
        `);
    });
}

function renderActivationSteps(activationSteps) {
    const container = $('#activationStepsContainer');
    container.empty();
    
    if (!activationSteps || Object.keys(activationSteps).length === 0) {
        container.html('<div class="empty-state">暂无激活步骤</div>');
        return;
    }
    
    Object.keys(activationSteps).forEach(role => {
        const steps = activationSteps[role];
        let html = `<div class="role-steps"><h4>${role}</h4><ol class="steps-list">`;
        steps.forEach(step => {
            html += `<li>${step.name} <span class="step-type">${step.stepType}</span></li>`;
        });
        html += '</ol></div>';
        container.append(html);
    });
}

function renderMatchScore(score) {
    $('#matchScoreValue').text(score);
    
    const circumference = 2 * Math.PI * 58;
    const offset = circumference - (score / 100) * circumference;
    
    const circle = document.getElementById('matchScoreCircle');
    if (circle) {
        circle.style.strokeDashoffset = offset;
        circle.classList.remove('high', 'medium', 'low');
        if (score >= 80) {
            circle.classList.add('high');
        } else if (score >= 50) {
            circle.classList.add('medium');
        } else {
            circle.classList.add('low');
        }
    }
    
    const levelIcon = document.getElementById('matchLevelIcon');
    const levelText = document.getElementById('matchLevelText');
    const levelDesc = document.getElementById('matchLevelDesc');
    
    if (levelIcon && levelText && levelDesc) {
        levelIcon.classList.remove('high', 'medium', 'low');
        
        if (score >= 80) {
            levelIcon.classList.add('high');
            levelIcon.innerHTML = '<i class="ri-emotion-happy-line"></i>';
            levelText.textContent = '优秀';
            levelDesc.textContent = '高度匹配，可直接使用';
        } else if (score >= 50) {
            levelIcon.classList.add('medium');
            levelIcon.innerHTML = '<i class="ri-emotion-normal-line"></i>';
            levelText.textContent = '良好';
            levelDesc.textContent = '部分匹配，建议调整';
        } else {
            levelIcon.classList.add('low');
            levelIcon.innerHTML = '<i class="ri-emotion-unhappy-line"></i>';
            levelText.textContent = '较差';
            levelDesc.textContent = '匹配度较低，需重新评估';
        }
    }
    
    const dimensions = [
        { name: 'Intent匹配', score: Math.min(100, score + Math.random() * 10), icon: 'ri-focus-3-line' },
        { name: 'Category匹配', score: Math.min(100, score + Math.random() * 5), icon: 'ri-folder-line' },
        { name: '角色匹配', score: Math.min(100, score - Math.random() * 10), icon: 'ri-user-line' },
        { name: '流程匹配', score: Math.min(100, score - Math.random() * 5), icon: 'ri-flow-chart' }
    ];
    
    const container = $('#matchDimensions');
    container.empty();
    
    dimensions.forEach(dim => {
        const status = dim.score >= 80 ? 'matched' : dim.score >= 50 ? 'partial' : 'mismatched';
        const iconClass = dim.score >= 80 ? 'ri-check-line' : dim.score >= 50 ? 'ri-question-line' : 'ri-close-line';
        
        container.append(`
            <div class="match-dimension-item">
                <div class="match-dimension-header">
                    <div class="match-dimension-name">
                        <span class="match-dimension-icon ${status}"><i class="${iconClass}"></i></span>
                        ${dim.name}
                    </div>
                    <span class="match-dimension-score ${status}">${Math.round(dim.score)}%</span>
                </div>
                <div class="match-dimension-bar">
                    <div class="match-dimension-fill ${status}" style="width: ${dim.score}%"></div>
                </div>
            </div>
        `);
    });
    
    if (score < 80) {
        $('#matchRecommendations').show();
        const recommendations = [];
        
        if (score < 50) {
            recommendations.push({
                title: '建议重新选择技能',
                desc: '当前技能与企业规范流程匹配度较低，建议选择更合适的技能'
            });
        }
        
        recommendations.push({
            title: '检查角色定义',
            desc: '部分角色定义存在差异，建议在融合前统一角色名称和数量要求'
        });
        
        if (score < 80) {
            recommendations.push({
                title: '优化激活步骤',
                desc: '激活步骤存在冲突，建议手动调整步骤顺序和依赖关系'
            });
        }
        
        const recContainer = $('#recommendationList');
        recContainer.empty();
        
        recommendations.forEach(rec => {
            recContainer.append(`
                <div class="recommendation-item">
                    <i class="ri-lightbulb-line"></i>
                    <div class="recommendation-content">
                        <div class="recommendation-title">${rec.title}</div>
                        <div class="recommendation-desc">${rec.desc}</div>
                    </div>
                </div>
            `);
        });
    } else {
        $('#matchRecommendations').hide();
    }
}

function renderStrategy(strategy) {
    if (!strategy) {
        $('#strategyInfo').html('<div class="empty-state">未配置融合策略</div>');
        return;
    }
    
    const html = `
        <div class="strategy-item">
            <label>角色定义优先级</label>
            <span>${getPriorityLabel(strategy.rolePriority)}</span>
        </div>
        <div class="strategy-item">
            <label>激活步骤优先级</label>
            <span>${getPriorityLabel(strategy.activationStepPriority)}</span>
        </div>
        <div class="strategy-item">
            <label>菜单配置优先级</label>
            <span>${getPriorityLabel(strategy.menuPriority)}</span>
        </div>
        <div class="strategy-item">
            <label>自动解决冲突</label>
            <span>${strategy.autoResolveConflict ? '是' : '否'}</span>
        </div>
    `;
    
    $('#strategyInfo').html(html);
}

function loadVersionHistory() {
    $.get(`/api/v1/fused-templates/${templateId}/versions`, function(data) {
        renderVersionHistory(data);
    }).fail(function() {
        $('#versionHistory').html('<div class="empty-state">暂无版本历史</div>');
    });
}

function renderVersionHistory(versions) {
    const container = $('#versionHistory');
    container.empty();
    
    if (!versions || versions.length === 0) {
        container.html('<div class="empty-state">暂无版本历史</div>');
        return;
    }
    
    versions.forEach(version => {
        container.append(`
            <div class="version-item">
                <div class="version-info">
                    <span class="version-number">v${version.version}</span>
                    <span class="version-time">${formatTime(version.createTime)}</span>
                </div>
                <button class="btn btn-sm btn-secondary" onclick="rollbackToVersion(${version.version})">
                    回滚
                </button>
            </div>
        `);
    });
}

function openConflictResolver(index) {
    currentConflict = templateData.fusionConflicts[index];
    
    $('#conflictDetail').html(`
        <h4><i class="ri-alert-line"></i> ${currentConflict.field}</h4>
        <p>冲突类型: ${currentConflict.type}</p>
    `);
    
    $('#enterpriseValue').text(formatValue(currentConflict.enterpriseValue));
    $('#skillValue').text(formatValue(currentConflict.skillValue));
    $('#enterprisePreview').text(formatValue(currentConflict.enterpriseValue));
    $('#skillPreview').text(formatValue(currentConflict.skillValue));
    $('#customValue').val('');
    
    $('.resolution-option').removeClass('selected');
    $('#optionEnterprise').addClass('selected');
    $('#customInputArea').hide();
    
    $('#conflictModal').show();
}

function selectResolution(type) {
    $('.resolution-option').removeClass('selected');
    $(`#option${type}`).addClass('selected');
    
    if (type === 'CUSTOM') {
        $('#customInputArea').show();
    } else {
        $('#customInputArea').hide();
    }
}

function applyResolution() {
    const selectedOption = $('.resolution-option.selected');
    if (!selectedOption.length) {
        showError('请选择解决方案');
        return;
    }
    
    const resolution = selectedOption.attr('id').replace('option', '');
    let resolvedValue;
    
    switch (resolution) {
        case 'ENTERPRISE':
            resolvedValue = currentConflict.enterpriseValue;
            break;
        case 'SKILL':
            resolvedValue = currentConflict.skillValue;
            break;
        case 'CUSTOM':
            resolvedValue = $('#customValue').val();
            if (!resolvedValue) {
                showError('请输入自定义值');
                return;
            }
            break;
    }
    
    const request = {
        conflictId: currentConflict.conflictId,
        resolution: resolution,
        resolvedValue: resolvedValue
    };
    
    $.ajax({
        url: `/api/v1/fused-templates/${templateId}/resolve-conflict`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(request),
        success: function(data) {
            templateData = data;
            renderConflicts(data.fusionConflicts);
            closeModal('conflictModal');
            showSuccess('冲突已解决');
        },
        error: function(xhr) {
            showError('解决冲突失败: ' + (xhr.responseJSON?.message || xhr.statusText));
        }
    });
}

function resolveAllConflicts() {
    if (!confirm('确定要自动解决所有冲突吗？')) return;
    
    const unresolvedConflicts = templateData.fusionConflicts.filter(c => !c.resolution);
    let resolved = 0;
    
    unresolvedConflicts.forEach(conflict => {
        const request = {
            conflictId: conflict.conflictId,
            resolution: 'ENTERPRISE',
            resolvedValue: conflict.enterpriseValue
        };
        
        $.ajax({
            url: `/api/v1/fused-templates/${templateId}/resolve-conflict`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(request),
            async: false,
            success: function() {
                resolved++;
            }
        });
    });
    
    if (resolved === unresolvedConflicts.length) {
        showSuccess(`已解决 ${resolved} 个冲突`);
        loadTemplateData();
    }
}

function createSceneGroup() {
    $('#sceneGroupName').val(templateData.name + ' - 场景组');
    $('#sceneGroupDescription').val('');
    $('#sceneGroupModal').show();
}

function submitSceneGroup() {
    const name = $('#sceneGroupName').val();
    if (!name) {
        showError('请输入场景组名称');
        return;
    }
    
    const request = {
        templateId: templateId,
        name: name,
        description: $('#sceneGroupDescription').val()
    };
    
    $.ajax({
        url: '/api/v1/scene-groups',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(request),
        success: function(data) {
            closeModal('sceneGroupModal');
            showSuccess('场景组创建成功');
            window.location.href = `scene-group-detail.html?id=${data.sceneGroupId}`;
        },
        error: function(xhr) {
            showError('创建场景组失败: ' + (xhr.responseJSON?.message || xhr.statusText));
        }
    });
}

function rollbackToVersion(version) {
    if (!confirm(`确定要回滚到版本 ${version} 吗？`)) return;
    
    $.ajax({
        url: `/api/v1/fused-templates/${templateId}/rollback?version=${version}`,
        type: 'POST',
        success: function(data) {
            templateData = data;
            loadTemplateData();
            showSuccess('回滚成功');
        },
        error: function(xhr) {
            showError('回滚失败: ' + (xhr.responseJSON?.message || xhr.statusText));
        }
    });
}

function editTemplate() {
    if (!templateData) {
        showError('模板数据未加载');
        return;
    }
    
    $('#editTemplateName').val(templateData.name);
    $('#editTemplateDescription').val(templateData.description || '');
    $('#editTemplateModal').show();
}

function saveTemplateEdit() {
    const name = $('#editTemplateName').val().trim();
    const description = $('#editTemplateDescription').val().trim();
    
    if (!name) {
        showError('请输入模板名称');
        return;
    }
    
    const request = {
        name: name,
        description: description
    };
    
    $.ajax({
        url: `/api/v1/fused-templates/${templateId}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(request),
        success: function(data) {
            templateData = data;
            renderBasicInfo(data);
            closeModal('editTemplateModal');
            showSuccess('模板已更新');
        },
        error: function(xhr) {
            showError('更新失败: ' + (xhr.responseJSON?.message || xhr.statusText));
        }
    });
}

function closeModal(modalId) {
    $(`#${modalId}`).hide();
}

function formatValue(value) {
    if (value === null || value === undefined) return '-';
    if (typeof value === 'object') return JSON.stringify(value);
    return String(value);
}

function getPriorityLabel(priority) {
    const map = {
        'ENTERPRISE_FIRST': '企业规范优先',
        'SKILL_FIRST': '技能定义优先',
        'MERGE': '合并',
        'USER_DECIDE': '用户决定'
    };
    return map[priority] || priority;
}

function formatTime(timestamp) {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleString('zh-CN');
}

function getUrlParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

function showSuccess(message) {
    alert(message);
}

function showError(message) {
    alert(message);
}

function initFlowCharts(data) {
    initFusionFlowChart(data);
    initActivationFlowChart(data);
    initFusionCompareView(data);
    initConflictResolver(data);
    initRoleView(data);
}

function initFusionCompareView(data) {
    const container = document.getElementById('tableView');
    if (!container) return;
    
    fusionCompareView = new FusionCompareView('#tableView', {
        mode: 'table',
        showDiff: true,
        showMatchScore: true,
        onConflictClick: function(conflict) {
            const index = templateData.fusionConflicts.findIndex(c => c.conflictId === conflict.conflictId);
            if (index >= 0) {
                openConflictResolver(index);
            }
        }
    });
    
    loadEnterpriseData(data.enterpriseProcedureId, function(enterpriseData) {
        loadSkillData(data.skillId, function(skillData) {
            fusionCompareView.loadData(enterpriseData, skillData, data, data.fusionConflicts);
        });
    });
}

function loadEnterpriseData(procedureId, callback) {
    if (!procedureId) {
        callback(null);
        return;
    }
    
    $.get(`/api/v1/enterprise-procedures/${procedureId}`, function(data) {
        callback(data);
    }).fail(function() {
        callback(null);
    });
}

function loadSkillData(skillId, callback) {
    if (!skillId) {
        callback(null);
        return;
    }
    
    $.get(`/api/v1/install/skills/${skillId}`, function(data) {
        callback(data);
    }).fail(function() {
        callback(null);
    });
}

function initConflictResolver(data) {
    const container = document.getElementById('conflictsList');
    if (!container || !data.fusionConflicts || data.fusionConflicts.length === 0) return;
    
    conflictResolver = new ConflictResolver('#conflictsList', {
        showBatchResolution: true,
        showProgress: true,
        onResolve: function(conflict) {
            resolveConflict(conflict);
        },
        onBatchResolve: function(conflicts) {
            batchResolveConflicts(conflicts);
        }
    });
    
    conflictResolver.loadConflicts(data.fusionConflicts);
}

function resolveConflict(conflict) {
    const request = {
        conflictId: conflict.conflictId,
        resolution: conflict.resolution,
        resolvedValue: conflict.resolvedValue
    };
    
    $.ajax({
        url: `/api/v1/fused-templates/${templateId}/resolve-conflict`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(request),
        success: function(data) {
            templateData = data;
            renderConflicts(data.fusionConflicts);
            showSuccess('冲突已解决');
        },
        error: function(xhr) {
            showError('解决冲突失败: ' + (xhr.responseJSON?.message || xhr.statusText));
        }
    });
}

function batchResolveConflicts(conflicts) {
    const promises = conflicts.map(conflict => {
        return $.ajax({
            url: `/api/v1/fused-templates/${templateId}/resolve-conflict`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                conflictId: conflict.conflictId,
                resolution: conflict.resolution,
                resolvedValue: conflict.resolvedValue
            })
        });
    });
    
    Promise.all(promises).then(() => {
        showSuccess(`已解决 ${conflicts.length} 个冲突`);
        loadTemplateData();
    }).catch((err) => {
        showError('部分冲突解决失败');
        loadTemplateData();
    });
}

function initRoleView(data) {
    const container = document.getElementById('rolesContainer');
    if (!container || !data.roles) return;
    
    roleView = new RoleView('#rolesContainer', {
        editable: false,
        showStatistics: true,
        showRelationships: false,
        onRoleClick: function(role, index) {
            console.log('Role clicked:', role);
        }
    });
    
    const roles = data.roles.map(role => ({
        roleId: role.roleId,
        name: role.name,
        minCount: role.minCount,
        maxCount: role.maxCount,
        required: role.required,
        source: role.source,
        description: role.description
    }));
    
    roleView.loadRoles(roles);
}

function initFusionFlowChart(data) {
    const container = document.getElementById('fusionFlowChart');
    if (!container) return;
    
    fusionFlowChart = new FlowChart(container, {
        editable: false,
        showMinimap: true,
        showToolbar: true,
        showLegend: true
    });
    
    const flowData = buildFusionFlowData(data);
    fusionFlowChart.loadFlowData(flowData);
}

function buildFusionFlowData(data) {
    const nodes = [];
    const edges = [];
    let yOffset = 50;
    
    nodes.push({
        id: 'start',
        type: 'start',
        title: '开始',
        x: 300,
        y: yOffset,
        status: 'completed'
    });
    
    yOffset += 100;
    
    if (data.roles && data.roles.length > 0) {
        data.roles.forEach((role, index) => {
            nodes.push({
                id: `role_${index}`,
                type: 'step',
                title: role.name,
                content: `${role.minCount}-${role.maxCount === 0 ? '∞' : role.maxCount}人`,
                role: role.source || 'MERGED',
                x: 150 + (index % 3) * 200,
                y: yOffset + Math.floor(index / 3) * 100,
                status: 'completed'
            });
        });
        
        yOffset += Math.ceil(data.roles.length / 3) * 100 + 50;
    }
    
    if (data.activationSteps) {
        const roles = Object.keys(data.activationSteps);
        roles.forEach((roleName, roleIndex) => {
            const steps = data.activationSteps[roleName];
            steps.forEach((step, stepIndex) => {
                const prevNodeId = stepIndex === 0 ? `role_${roleIndex}` : `step_${roleIndex}_${stepIndex - 1}`;
                nodes.push({
                    id: `step_${roleIndex}_${stepIndex}`,
                    type: 'step',
                    title: step.name,
                    content: step.stepType,
                    role: roleName,
                    x: 150 + (roleIndex % 3) * 200,
                    y: yOffset + stepIndex * 80,
                    status: 'pending'
                });
                
                if (stepIndex > 0) {
                    edges.push({
                        source: prevNodeId,
                        target: `step_${roleIndex}_${stepIndex}`,
                        status: 'pending'
                    });
                }
            });
        });
    }
    
    yOffset += 100;
    nodes.push({
        id: 'end',
        type: 'end',
        title: '结束',
        x: 300,
        y: yOffset,
        status: 'pending'
    });
    
    return { nodes, edges };
}

function initActivationFlowChart(data) {
    const container = document.getElementById('activationFlowChart');
    if (!container) return;
    
    activationFlowChart = new FlowChart(container, {
        editable: false,
        showMinimap: true,
        showToolbar: true,
        showLegend: true
    });
    
    const flowData = buildActivationFlowData(data);
    activationFlowChart.loadFlowData(flowData);
}

function buildActivationFlowData(data) {
    const nodes = [];
    const edges = [];
    let yOffset = 50;
    
    nodes.push({
        id: 'start',
        type: 'start',
        title: '开始',
        x: 300,
        y: yOffset,
        status: 'completed'
    });
    
    if (data.activationSteps) {
        const roles = Object.keys(data.activationSteps);
        roles.forEach((roleName, roleIndex) => {
            yOffset += 100;
            const steps = data.activationSteps[roleName];
            
            steps.forEach((step, stepIndex) => {
                const prevId = stepIndex === 0 ? 'start' : `step_${roleIndex}_${stepIndex - 1}`;
                nodes.push({
                    id: `step_${roleIndex}_${stepIndex}`,
                    type: 'step',
                    title: step.name,
                    content: step.stepType,
                    role: roleName,
                    x: 100 + roleIndex * 250,
                    y: yOffset + stepIndex * 80,
                    status: stepIndex === 0 ? 'in-progress' : 'pending'
                });
                
                edges.push({
                    source: prevId,
                    target: `step_${roleIndex}_${stepIndex}`,
                    label: stepIndex === 0 ? roleName : '',
                    status: stepIndex === 0 ? 'active' : 'pending'
                });
            });
            
            yOffset += steps.length * 80;
        });
    }
    
    yOffset += 50;
    nodes.push({
        id: 'end',
        type: 'end',
        title: '结束',
        x: 300,
        y: yOffset,
        status: 'pending'
    });
    
    return { nodes, edges };
}

function switchCompareView(view) {
    if (view === 'table') {
        $('#tableView').show();
        $('#flowView').hide();
        $('#tableViewBtn').addClass('active');
        $('#flowViewBtn').removeClass('active');
    } else {
        $('#tableView').hide();
        $('#flowView').show();
        $('#flowViewBtn').addClass('active');
        $('#tableViewBtn').removeClass('active');
        if (fusionFlowChart) {
            setTimeout(() => fusionFlowChart.fitView(), 100);
        }
    }
}

function switchStepsView(view) {
    if (view === 'list') {
        $('#stepsListView').show();
        $('#stepsFlowView').hide();
        $('#stepsListViewBtn').addClass('active');
        $('#stepsFlowViewBtn').removeClass('active');
    } else {
        $('#stepsListView').hide();
        $('#stepsFlowView').show();
        $('#stepsFlowViewBtn').addClass('active');
        $('#stepsListViewBtn').removeClass('active');
        if (activationFlowChart) {
            setTimeout(() => activationFlowChart.fitView(), 100);
        }
    }
}

function filterConflicts(filter) {
    currentConflictFilter = filter;
    $('.conflict-filter-btn').removeClass('active');
    $(`.conflict-filter-btn:contains('${filter === 'all' ? '全部' : filter === 'unresolved' ? '待解决' : '已解决'}')`).addClass('active');
    
    renderConflicts(templateData.fusionConflicts);
}

function selectBatchOption(option) {
    selectedBatchOption = option;
    $('.batch-option').removeClass('selected');
    $(`.batch-option.${option.toLowerCase()}`).addClass('selected');
}

function applyBatchResolution() {
    if (!selectedBatchOption) {
        showError('请选择批量解决策略');
        return;
    }
    
    const unresolvedConflicts = templateData.fusionConflicts.filter(c => !c.resolution);
    if (unresolvedConflicts.length === 0) {
        showSuccess('没有待解决的冲突');
        return;
    }
    
    if (!confirm(`确定要批量解决 ${unresolvedConflicts.length} 个冲突吗？`)) return;
    
    let resolved = 0;
    const promises = [];
    
    unresolvedConflicts.forEach(conflict => {
        const resolvedValue = selectedBatchOption === 'ENTERPRISE' ? conflict.enterpriseValue : conflict.skillValue;
        
        const promise = $.ajax({
            url: `/api/v1/fused-templates/${templateId}/resolve-conflict`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                conflictId: conflict.conflictId,
                resolution: selectedBatchOption,
                resolvedValue: resolvedValue
            })
        }).then(() => {
            resolved++;
        });
        
        promises.push(promise);
    });
    
    Promise.all(promises).then(() => {
        showSuccess(`已解决 ${resolved} 个冲突`);
        loadTemplateData();
    }).catch((err) => {
        showError('部分冲突解决失败');
        loadTemplateData();
    });
}
