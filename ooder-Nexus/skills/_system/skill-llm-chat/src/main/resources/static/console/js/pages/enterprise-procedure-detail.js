let procedureId = null;
let procedureData = null;
let fusionStep = 1;
let selectedSkillId = null;
let ruleEditor = null;
let procedureFlowChart = null;
let completenessDashboard = null;
let roleView = null;

$(document).ready(function() {
    procedureId = getUrlParam('id');
    if (procedureId) {
        loadProcedureData();
    } else {
        showError('缺少流程ID参数');
    }
});

function loadProcedureData() {
    $.get(`/api/v1/enterprise-procedures/${procedureId}`, function(data) {
        procedureData = data;
        renderBasicInfo(data);
        renderRoles(data.roles);
        renderSteps(data.steps);
        initRuleEditor(data.rules);
        loadCompleteness();
        loadKnowledgeBases();
        loadFusedTemplates();
    }).fail(function() {
        showError('加载流程数据失败');
    });
}

function renderBasicInfo(data) {
    $('#pageTitle').text(data.name);
    $('#infoProcedureId').text(data.procedureId);
    $('#infoName').text(data.name);
    $('#infoCategory').text(data.category || '-');
    $('#infoSource').text(getSourceLabel(data.source));
    $('#infoStatus').html(renderStatusBadge(data.status));
    $('#infoAuthor').text(data.author || '-');
    $('#infoCreateTime').text(formatTime(data.createTime));
    $('#infoUpdateTime').text(formatTime(data.updateTime));
    $('#infoDescription').text(data.description || '-');
}

function renderRoles(roles) {
    const container = $('#rolesContainer');
    container.empty();

    if (!roles || roles.length === 0) {
        container.html('<div class="empty-state">暂无角色定义</div>');
        return;
    }

    roles.forEach((role, index) => {
        container.append(`
            <div class="role-card ${role.required ? 'required' : ''}">
                <div class="role-header">
                    <h4>${role.name}</h4>
                    <div class="role-actions">
                        <button class="btn-icon" onclick="editRole(${index})">
                            <i class="ri-edit-line"></i>
                        </button>
                        <button class="btn-icon danger" onclick="deleteRole(${index})">
                            <i class="ri-delete-bin-line"></i>
                        </button>
                    </div>
                </div>
                <div class="role-body">
                    <div class="role-info">
                        <span class="role-id">${role.roleId}</span>
                        <span class="role-count">${role.minCount} - ${role.maxCount === 0 ? '∞' : role.maxCount} 人</span>
                        ${role.required ? '<span class="role-badge required">必需</span>' : ''}
                    </div>
                    <p class="role-description">${role.description || '-'}</p>
                </div>
            </div>
        `);
    });
}

function renderSteps(steps) {
    const container = $('#stepsContainer');
    container.empty();

    if (!steps || steps.length === 0) {
        container.html('<div class="empty-state">暂无流程步骤</div>');
        return;
    }

    steps.forEach((step, index) => {
        container.append(`
            <div class="step-item" data-index="${index}">
                <div class="step-order">${index + 1}</div>
                <div class="step-content">
                    <div class="step-header">
                        <h4>${step.name}</h4>
                        <div class="step-actions">
                            <button class="btn-icon" onclick="editStep(${index})">
                                <i class="ri-edit-line"></i>
                            </button>
                            <button class="btn-icon" onclick="moveStep(${index}, -1)" ${index === 0 ? 'disabled' : ''}>
                                <i class="ri-arrow-up-line"></i>
                            </button>
                            <button class="btn-icon" onclick="moveStep(${index}, 1)" ${index === steps.length - 1 ? 'disabled' : ''}>
                                <i class="ri-arrow-down-line"></i>
                            </button>
                            <button class="btn-icon danger" onclick="deleteStep(${index})">
                                <i class="ri-delete-bin-line"></i>
                            </button>
                        </div>
                    </div>
                    <div class="step-body">
                        <span class="step-type">${getStepTypeLabel(step.type)}</span>
                        <span class="step-id">${step.stepId}</span>
                    </div>
                    <p class="step-description">${step.description || '-'}</p>
                </div>
            </div>
        `);
    });
}

function initRuleEditor(rules) {
    const container = document.getElementById('rulesContainer');
    if (!container) return;
    
    ruleEditor = new RuleEditor(container, {
        editable: true,
        showTemplates: true,
        variables: ['roleCount', 'minCount', 'maxCount', 'currentStep', 'previousStep', 'duration', 'maxDuration'],
        onRuleChange: function(updatedRules) {
            procedureData.rules = updatedRules;
            updateProcedure();
        }
    });
    
    if (rules && rules.length > 0) {
        ruleEditor.loadRules(rules);
    }
}

function addRule() {
    if (ruleEditor) {
        ruleEditor.addRule();
    }
}

function editRule(index) {
    if (ruleEditor) {
        ruleEditor.editRule(index);
    }
}

function deleteRule(index) {
    if (ruleEditor) {
        ruleEditor.deleteRule(index);
    }
}

function loadCompleteness() {
    $.get(`/api/v1/enterprise-procedures/${procedureId}/completeness`, function(data) {
        renderCompleteness(data);
    });
}

function renderCompleteness(data) {
    const score = data.overallScore || 0;
    $('#completenessScore').text(score);
    
    const needle = document.getElementById('completenessNeedle');
    if (needle) {
        const rotation = -90 + (score / 100) * 180;
        needle.style.transform = `translateX(-50%) rotate(${rotation}deg)`;
    }
    
    const dimensionsContainer = $('#completenessDimensions');
    dimensionsContainer.empty();
    
    if (data.dimensions) {
        data.dimensions.forEach(dim => {
            const status = dim.score >= 80 ? 'complete' : dim.score >= 50 ? 'partial' : 'missing';
            const fillClass = dim.score >= 80 ? 'excellent' : dim.score >= 50 ? 'good' : 'warning';
            
            dimensionsContainer.append(`
                <div class="dimension-item">
                    <div class="dimension-header">
                        <div class="dimension-name">
                            <i class="ri-${getDimensionIcon(dim.name)}"></i>
                            ${dim.name}
                        </div>
                        <span class="dimension-score ${status}">${dim.score}%</span>
                    </div>
                    <div class="dimension-bar">
                        <div class="dimension-fill ${fillClass}" style="width: ${dim.score}%"></div>
                    </div>
                </div>
            `);
        });
    }
    
    const issuesContainer = $('#completenessIssues');
    issuesContainer.empty();
    
    if (data.issues && data.issues.length > 0) {
        issuesContainer.append('<div class="issues-title"><i class="ri-alert-line"></i> 待完善项</div>');
        
        data.issues.forEach(issue => {
            issuesContainer.append(`
                <div class="issue-item ${issue.severity.toLowerCase()}">
                    <div class="issue-icon">
                        <i class="ri-${issue.severity === 'CRITICAL' ? 'error-warning' : 'information'}-line"></i>
                    </div>
                    <div class="issue-content">
                        <div class="issue-description">${issue.description}</div>
                        ${issue.suggestion ? `<div class="issue-suggestion">${issue.suggestion}</div>` : ''}
                    </div>
                    <button class="issue-action-btn" onclick="fixIssue('${issue.field}')">
                        修复
                    </button>
                </div>
            `);
        });
    }
}

function getDimensionIcon(name) {
    const icons = {
        '基础信息': 'information-line',
        '角色定义': 'team-line',
        '流程步骤': 'flow-chart',
        '规则约束': 'git-branch-line',
        '文档附件': 'file-text-line'
    };
    return icons[name] || 'checkbox-circle-line';
}

function fixIssue(field) {
    switch (field) {
        case 'name':
        case 'description':
            document.querySelector('.info-item:has(#info' + field.charAt(0).toUpperCase() + field.slice(1) + ')')?.scrollIntoView({ behavior: 'smooth' });
            break;
        case 'roles':
            addRole();
            break;
        case 'steps':
            addStep();
            break;
        case 'rules':
            addRule();
            break;
        default:
            showSuccess('请手动完善此项');
    }
}

function loadKnowledgeBases() {
    $.get(`/api/v1/enterprise-procedures/${procedureId}/knowledge-bases`, function(data) {
        renderKnowledgeBases(data);
    }).fail(function() {
        $('#knowledgeBases').html('<div class="empty-state">暂无关联知识库</div>');
    });
}

function renderKnowledgeBases(data) {
    const container = $('#knowledgeBases');
    container.empty();

    if (!data || data.length === 0) {
        container.html('<div class="empty-state">暂无关联知识库</div>');
        return;
    }

    data.forEach(kb => {
        container.append(`
            <div class="knowledge-item">
                <i class="ri-database-2-line"></i>
                <span>${kb.name}</span>
                <button class="btn-icon" onclick="unbindKnowledge('${kb.id}')">
                    <i class="ri-close-line"></i>
                </button>
            </div>
        `);
    });
}

function loadFusedTemplates() {
    $.get(`/api/v1/fused-templates?procedureId=${procedureId}`, function(response) {
        renderFusedTemplates(response.data);
    }).fail(function() {
        $('#fusedTemplates').html('<div class="empty-state">暂无融合模板</div>');
    });
}

function renderFusedTemplates(data) {
    const container = $('#fusedTemplates');
    container.empty();

    if (!data || data.length === 0) {
        container.html('<div class="empty-state">暂无融合模板</div>');
        return;
    }

    data.forEach(template => {
        container.append(`
            <div class="fusion-item">
                <div class="fusion-info">
                    <span class="fusion-name">${template.name}</span>
                    <span class="fusion-score">匹配度: ${template.matchScore}%</span>
                </div>
                <a href="fusion-template-detail.html?id=${template.templateId}" class="btn btn-sm btn-secondary">
                    查看
                </a>
            </div>
        `);
    });
}

function addRole() {
    $('#roleIndex').val(-1);
    $('#roleId').val('');
    $('#roleName').val('');
    $('#roleDescription').val('');
    $('#roleMinCount').val(1);
    $('#roleMaxCount').val(1);
    $('#roleRequired').prop('checked', false);
    $('#roleModal').show();
}

function editRole(index) {
    const role = procedureData.roles[index];
    $('#roleIndex').val(index);
    $('#roleId').val(role.roleId);
    $('#roleName').val(role.name);
    $('#roleDescription').val(role.description);
    $('#roleMinCount').val(role.minCount);
    $('#roleMaxCount').val(role.maxCount);
    $('#roleRequired').prop('checked', role.required);
    $('#roleModal').show();
}

function saveRole() {
    const index = parseInt($('#roleIndex').val());
    const role = {
        roleId: $('#roleId').val(),
        name: $('#roleName').val(),
        description: $('#roleDescription').val(),
        minCount: parseInt($('#roleMinCount').val()),
        maxCount: parseInt($('#roleMaxCount').val()),
        required: $('#roleRequired').is(':checked')
    };

    if (index === -1) {
        procedureData.roles.push(role);
    } else {
        procedureData.roles[index] = role;
    }

    updateProcedure();
    closeModal('roleModal');
}

function deleteRole(index) {
    if (!confirm('确定要删除该角色吗？')) return;
    procedureData.roles.splice(index, 1);
    updateProcedure();
}

function addStep() {
    $('#stepIndex').val(-1);
    $('#stepId').val('');
    $('#stepName').val('');
    $('#stepDescription').val('');
    $('#stepType').val('MANUAL');
    $('#stepOrder').val((procedureData.steps?.length || 0) + 1);
    $('#stepModal').show();
}

function editStep(index) {
    const step = procedureData.steps[index];
    $('#stepIndex').val(index);
    $('#stepId').val(step.stepId);
    $('#stepName').val(step.name);
    $('#stepDescription').val(step.description);
    $('#stepType').val(step.type);
    $('#stepOrder').val(step.order);
    $('#stepModal').show();
}

function saveStep() {
    const index = parseInt($('#stepIndex').val());
    const step = {
        stepId: $('#stepId').val(),
        name: $('#stepName').val(),
        description: $('#stepDescription').val(),
        type: $('#stepType').val(),
        order: parseInt($('#stepOrder').val())
    };

    if (index === -1) {
        procedureData.steps.push(step);
    } else {
        procedureData.steps[index] = step;
    }

    procedureData.steps.sort((a, b) => a.order - b.order);
    updateProcedure();
    closeModal('stepModal');
}

function deleteStep(index) {
    if (!confirm('确定要删除该步骤吗？')) return;
    procedureData.steps.splice(index, 1);
    updateProcedure();
}

function moveStep(index, direction) {
    const newIndex = index + direction;
    if (newIndex < 0 || newIndex >= procedureData.steps.length) return;

    const temp = procedureData.steps[index];
    procedureData.steps[index] = procedureData.steps[newIndex];
    procedureData.steps[newIndex] = temp;

    procedureData.steps.forEach((step, i) => step.order = i + 1);
    updateProcedure();
}

function addRule() {
    if (!ruleEditor) {
        showError('规则编辑器未初始化');
        return;
    }
    
    $('#ruleIndex').val(-1);
    $('#ruleId').val('');
    $('#ruleName').val('');
    $('#ruleDescription').val('');
    $('#ruleType').val('COMPLIANCE');
    $('#ruleCondition').val('');
    $('#ruleAction').val('');
    $('#rulePriority').val(1);
    $('#ruleEnabled').prop('checked', true);
    $('#ruleModal').show();
}

function editRule(index) {
    if (!ruleEditor) {
        showError('规则编辑器未初始化');
        return;
    }
    
    const rule = procedureData.rules[index];
    if (!rule) {
        showError('规则不存在');
        return;
    }
    
    $('#ruleIndex').val(index);
    $('#ruleId').val(rule.ruleId || '');
    $('#ruleName').val(rule.name || '');
    $('#ruleDescription').val(rule.description || '');
    $('#ruleType').val(rule.type || 'COMPLIANCE');
    $('#ruleCondition').val(rule.condition || '');
    $('#ruleAction').val(rule.action || '');
    $('#rulePriority').val(rule.priority || 1);
    $('#ruleEnabled').prop('checked', rule.enabled !== false);
    $('#ruleModal').show();
}

function saveRule() {
    const index = parseInt($('#ruleIndex').val());
    const rule = {
        ruleId: $('#ruleId').val() || 'rule_' + Date.now(),
        name: $('#ruleName').val(),
        description: $('#ruleDescription').val(),
        type: $('#ruleType').val(),
        condition: $('#ruleCondition').val(),
        action: $('#ruleAction').val(),
        priority: parseInt($('#rulePriority').val()),
        enabled: $('#ruleEnabled').is(':checked')
    };
    
    if (!rule.name) {
        showError('请输入规则名称');
        return;
    }
    
    if (!procedureData.rules) {
        procedureData.rules = [];
    }
    
    if (index === -1) {
        procedureData.rules.push(rule);
    } else {
        procedureData.rules[index] = rule;
    }
    
    updateProcedure();
    closeModal('ruleModal');
}

function deleteRule(index) {
    if (!confirm('确定要删除该规则吗？')) return;
    procedureData.rules.splice(index, 1);
    updateProcedure();
}

function updateProcedure() {
    $.ajax({
        url: `/api/v1/enterprise-procedures/${procedureId}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            name: procedureData.name,
            description: procedureData.description,
            roles: procedureData.roles,
            steps: procedureData.steps,
            rules: procedureData.rules
        }),
        success: function(data) {
            procedureData = data;
            renderBasicInfo(data);
            renderRoles(data.roles);
            renderSteps(data.steps);
            renderRules(data.rules);
            loadCompleteness();
            showSuccess('保存成功');
        },
        error: function(xhr) {
            showError('保存失败: ' + (xhr.responseJSON?.message || xhr.statusText));
        }
    });
}

function createFusedTemplate() {
    fusionStep = 1;
    selectedSkillId = null;
    updateFusionStepUI();
    loadSkills();
    $('#fusionModal').show();
}

function loadSkills() {
    $.get('/api/v1/install/installed-skills', function(data) {
        renderSkillList(data);
    }).fail(function() {
        $('#skillList').html('<div class="empty-state">加载技能列表失败</div>');
    });
}

function renderSkillList(skills) {
    const container = $('#skillList');
    container.empty();

    if (!skills || skills.length === 0) {
        container.html('<div class="empty-state">暂无已安装的技能</div>');
        return;
    }

    skills.forEach(skill => {
        container.append(`
            <div class="skill-item ${selectedSkillId === skill.id ? 'selected' : ''}" 
                 onclick="selectSkill('${skill.id}')">
                <div class="skill-info">
                    <span class="skill-name">${skill.name}</span>
                    <span class="skill-category">${skill.category || '-'}</span>
                </div>
                <div class="skill-match" id="match-${skill.id}"></div>
            </div>
        `);
    });

    skills.forEach(skill => {
        $.get(`/api/v1/fused-templates/match-score?procedureId=${procedureId}&skillId=${skill.id}`, function(data) {
            $(`#match-${skill.id}`).html(`<span class="match-score">${data.matchScore}%</span>`);
        });
    });
}

function selectSkill(skillId) {
    selectedSkillId = skillId;
    $('.skill-item').removeClass('selected');
    $(`.skill-item[onclick="selectSkill('${skillId}')"]`).addClass('selected');
}

function nextFusionStep() {
    if (fusionStep === 1) {
        if (!selectedSkillId) {
            showError('请选择一个技能');
            return;
        }
        fusionStep = 2;
    } else if (fusionStep === 2) {
        previewFusion();
        fusionStep = 3;
    } else if (fusionStep === 3) {
        executeFusion();
        return;
    }
    updateFusionStepUI();
}

function prevFusionStep() {
    if (fusionStep > 1) {
        fusionStep--;
        updateFusionStepUI();
    }
}

function updateFusionStepUI() {
    $('.wizard-step').removeClass('active');
    $(`.wizard-step[data-step="${fusionStep}"]`).addClass('active');

    $('#fusionPrevBtn').toggle(fusionStep > 1);
    
    if (fusionStep === 3) {
        $('#fusionNextBtn').text('创建融合模板');
    } else {
        $('#fusionNextBtn').text('下一步');
    }
}

function previewFusion() {
    const request = {
        enterpriseProcedureId: procedureId,
        skillId: selectedSkillId,
        fusionStrategy: {
            rolePriority: $('#rolePriority').val(),
            activationStepPriority: $('#activationStepPriority').val(),
            menuPriority: $('#menuPriority').val(),
            autoResolveConflict: $('#autoResolveConflict').is(':checked')
        }
    };

    $.ajax({
        url: '/api/v1/fused-templates/preview',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(request),
        success: function(data) {
            renderFusionPreview(data);
        },
        error: function(xhr) {
            showError('预览失败: ' + (xhr.responseJSON?.message || xhr.statusText));
            fusionStep = 2;
            updateFusionStepUI();
        }
    });
}

function renderFusionPreview(data) {
    const container = $('#fusionPreview');
    container.empty();

    container.append(`
        <div class="preview-header">
            <h4>融合结果预览</h4>
            <span class="match-score">匹配度: ${data.matchScore}%</span>
        </div>
    `);

    if (data.conflicts && data.conflicts.length > 0) {
        container.append(`
            <div class="conflicts-section">
                <h5>检测到 ${data.conflicts.length} 个冲突</h5>
                <div class="conflicts-list">
                    ${data.conflicts.map(c => `
                        <div class="conflict-item">
                            <span class="conflict-field">${c.field}</span>
                            <span class="conflict-type">${c.type}</span>
                        </div>
                    `).join('')}
                </div>
            </div>
        `);
    }

    if (data.template) {
        container.append(`
            <div class="result-section">
                <h5>融合后角色 (${data.template.roles?.length || 0})</h5>
                <ul>
                    ${(data.template.roles || []).map(r => `<li>${r.name}</li>`).join('')}
                </ul>
            </div>
        `);
    }
}

function executeFusion() {
    const request = {
        enterpriseProcedureId: procedureId,
        skillId: selectedSkillId,
        name: `${procedureData.name} - 融合模板`,
        fusedBy: getCurrentUserId(),
        fusionStrategy: {
            rolePriority: $('#rolePriority').val(),
            activationStepPriority: $('#activationStepPriority').val(),
            menuPriority: $('#menuPriority').val(),
            autoResolveConflict: $('#autoResolveConflict').is(':checked')
        }
    };

    $.ajax({
        url: '/api/v1/fused-templates/fuse',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(request),
        success: function(data) {
            closeModal('fusionModal');
            loadFusedTemplates();
            showSuccess('融合模板创建成功');
        },
        error: function(xhr) {
            showError('创建失败: ' + (xhr.responseJSON?.message || xhr.statusText));
        }
    });
}

function closeModal(modalId) {
    $(`#${modalId}`).hide();
}

function editProcedure() {
    window.location.href = `enterprise-procedure-create.html?id=${procedureId}`;
}

function bindKnowledge() {
    loadAvailableKnowledgeBases();
    $('#knowledgeBindModal').show();
}

function loadAvailableKnowledgeBases() {
    $.get('/api/v1/knowledge-bases', function(data) {
        renderAvailableKnowledgeBases(data);
    }).fail(function() {
        $('#availableKnowledgeBases').html('<div class="empty-state">加载知识库列表失败</div>');
    });
}

function renderAvailableKnowledgeBases(data) {
    const container = $('#availableKnowledgeBases');
    container.empty();
    
    if (!data || data.length === 0) {
        container.html('<div class="empty-state">暂无可用的知识库</div>');
        return;
    }
    
    data.forEach(kb => {
        container.append(`
            <div class="knowledge-option" onclick="selectKnowledge('${kb.id}', '${kb.name}')">
                <i class="ri-database-2-line"></i>
                <span>${kb.name}</span>
                <span class="knowledge-type">${kb.type || '-'}</span>
            </div>
        `);
    });
}

function selectKnowledge(kbId, kbName) {
    $('.knowledge-option').removeClass('selected');
    $(`.knowledge-option[onclick="selectKnowledge('${kbId}', '${kbName}')"]`).addClass('selected');
    $('#selectedKbId').val(kbId);
    $('#selectedKbName').val(kbName);
}

function submitKnowledgeBind() {
    const kbId = $('#selectedKbId').val();
    if (!kbId) {
        showError('请选择要绑定的知识库');
        return;
    }
    
    $.ajax({
        url: `/api/v1/enterprise-procedures/${procedureId}/knowledge-bases`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ knowledgeBaseId: kbId }),
        success: function() {
            closeModal('knowledgeBindModal');
            loadKnowledgeBases();
            showSuccess('知识库绑定成功');
        },
        error: function(xhr) {
            showError('绑定失败: ' + (xhr.responseJSON?.message || xhr.statusText));
        }
    });
}

function unbindKnowledge(kbId) {
    if (!confirm('确定要解除绑定吗？')) return;
    $.ajax({
        url: `/api/v1/enterprise-procedures/${procedureId}/knowledge-bases/${kbId}`,
        type: 'DELETE',
        success: function() {
            loadKnowledgeBases();
            showSuccess('解除绑定成功');
        },
        error: function(xhr) {
            showError('解除绑定失败');
        }
    });
}

function getSourceLabel(source) {
    const map = {
        'KNOWLEDGE_BASE': '知识库梳理',
        'INDUSTRY_BEST_PRACTICE': '行业最佳实践',
        'MANUAL': '手动创建'
    };
    return map[source] || source;
}

function getStepTypeLabel(type) {
    const map = {
        'MANUAL': '人工执行',
        'AUTOMATIC': '自动执行',
        'APPROVAL': '审批节点',
        'PARALLEL': '并行网关',
        'EXCLUSIVE': '排他网关'
    };
    return map[type] || type;
}

function getRuleTypeLabel(type) {
    const map = {
        'COMPLIANCE': '合规规则',
        'APPROVAL': '审批规则',
        'CONSTRAINT': '约束规则',
        'VALIDATION': '验证规则',
        'BUSINESS': '业务规则'
    };
    return map[type] || type;
}

function renderStatusBadge(status) {
    const map = {
        'DRAFT': '<span class="badge badge-secondary">草稿</span>',
        'ACTIVE': '<span class="badge badge-success">激活</span>',
        'DEPRECATED': '<span class="badge badge-warning">废弃</span>'
    };
    return map[status] || status;
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

function getCurrentUserId() {
    return localStorage.getItem('userId') || 'system';
}

function showSuccess(message) {
    alert(message);
}

function showError(message) {
    alert(message);
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
        initProcedureFlowChart();
    }
}

function initProcedureFlowChart() {
    const container = document.getElementById('procedureFlowChart');
    if (!container || !procedureData) return;
    
    if (procedureFlowChart) {
        procedureFlowChart.clear();
    } else {
        procedureFlowChart = new FlowChart(container, {
            editable: true,
            showMinimap: true,
            showToolbar: true,
            showLegend: true,
            onNodeSelected: function(node) {
                showNodeDetail(node);
            },
            onNodeMoved: function(node) {
                updateStepPosition(node);
            }
        });
    }
    
    const flowData = buildProcedureFlowData(procedureData);
    procedureFlowChart.loadFlowData(flowData);
}

function buildProcedureFlowData(data) {
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
    
    if (data.steps && data.steps.length > 0) {
        const sortedSteps = [...data.steps].sort((a, b) => (a.order || 0) - (b.order || 0));
        
        sortedSteps.forEach((step, index) => {
            const nodeType = getStepNodeType(step.type);
            const prevNodeId = index === 0 ? 'start' : `step_${index - 1}`;
            
            nodes.push({
                id: `step_${index}`,
                type: nodeType,
                title: step.name,
                content: getStepTypeLabel(step.type),
                role: step.roleId || '',
                x: 220,
                y: yOffset,
                status: 'pending',
                stepData: step
            });
            
            edges.push({
                id: `edge_${index}`,
                source: prevNodeId,
                target: `step_${index}`,
                status: 'pending'
            });
            
            yOffset += 100;
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
    
    if (data.steps && data.steps.length > 0) {
        edges.push({
            id: 'edge_end',
            source: `step_${data.steps.length - 1}`,
            target: 'end',
            status: 'pending'
        });
    } else {
        edges.push({
            id: 'edge_end',
            source: 'start',
            target: 'end',
            status: 'pending'
        });
    }
    
    return { nodes, edges };
}

function getStepNodeType(stepType) {
    const typeMap = {
        'MANUAL': 'step',
        'AUTOMATIC': 'step',
        'APPROVAL': 'approval',
        'PARALLEL': 'gateway',
        'EXCLUSIVE': 'gateway'
    };
    return typeMap[stepType] || 'step';
}

function showNodeDetail(node) {
    if (node.type === 'start' || node.type === 'end') return;
    
    if (node.stepData) {
        const index = procedureData.steps.findIndex(s => s.stepId === node.stepData.stepId);
        if (index >= 0) {
            editStep(index);
        }
    }
}

function updateStepPosition(node) {
    if (!node.stepData) return;
    
    const stepIndex = procedureData.steps.findIndex(s => s.stepId === node.stepData.stepId);
    if (stepIndex >= 0) {
        const newY = node.y;
        const newOrder = Math.round((newY - 150) / 100) + 1;
        
        if (newOrder !== procedureData.steps[stepIndex].order) {
            procedureData.steps[stepIndex].order = newOrder;
            procedureData.steps.sort((a, b) => (a.order || 0) - (b.order || 0));
            procedureData.steps.forEach((step, i) => step.order = i + 1);
            
            updateProcedure();
        }
    }
}

function exportFlowChart() {
    if (!procedureFlowChart) return;
    
    procedureFlowChart.exportAsImage().then(data => {
        const blob = new Blob([data], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${procedureData.name}_flow.json`;
        a.click();
        URL.revokeObjectURL(url);
    });
}

function initCompletenessDashboard(data) {
    const container = document.getElementById('completenessDashboard');
    if (!container) return;
    
    completenessDashboard = new CompletenessDashboard(container, {
        showGauge: true,
        showDimensions: true,
        showIssues: true,
        showSuggestions: true,
        animate: true,
        onIssueClick: function(field) {
            console.log('Issue clicked:', field);
        },
        onFixIssue: function(field) {
            console.log('Fix issue:', field);
        }
    });
    
    const completenessData = calculateCompleteness(data);
    completenessDashboard.loadData(completenessData);
}

function calculateCompleteness(data) {
    const dimensions = [];
    const issues = [];
    const suggestions = [];
    let overallScore = 0;
    
    const basicInfoScore = calculateBasicInfoScore(data);
    dimensions.push({
        name: '基础信息',
        score: basicInfoScore.score,
        weight: 15,
        icon: 'ri-information-line',
        details: basicInfoScore.details
    });
    if (basicInfoScore.issues) {
        issues.push(...basicInfoScore.issues);
    }
    
    const rolesScore = calculateRolesScore(data);
    dimensions.push({
        name: '角色定义',
        score: rolesScore.score,
        weight: 25,
        icon: 'ri-team-line',
        details: rolesScore.details
    });
    if (rolesScore.issues) {
        issues.push(...rolesScore.issues);
    }
    
    const stepsScore = calculateStepsScore(data);
    dimensions.push({
        name: '流程步骤',
        score: stepsScore.score,
        weight: 30,
        icon: 'ri-flow-chart',
        details: stepsScore.details
    });
    if (stepsScore.issues) {
        issues.push(...stepsScore.issues);
    }
    
    const rulesScore = calculateRulesScore(data);
    dimensions.push({
        name: '规则约束',
        score: rulesScore.score,
        weight: 20,
        icon: 'ri-git-branch-line',
        details: rulesScore.details
    });
    if (rulesScore.issues) {
        issues.push(...rulesScore.issues);
    }
    
    const docsScore = calculateDocsScore(data);
    dimensions.push({
        name: '文档附件',
        score: docsScore.score,
        weight: 10,
        icon: 'ri-file-text-line',
        details: docsScore.details
    });
    if (docsScore.issues) {
        issues.push(...docsScore.issues);
    }
    
    overallScore = dimensions.reduce((sum, dim) => sum + (dim.score * dim.weight / 100), 0);
    
    if (overallScore < 100) {
        suggestions.push({
            title: '完善基础信息',
            description: '补充流程描述、分类等基础信息',
            impact: 10
        });
        suggestions.push({
            title: '优化角色配置',
            description: '确保每个角色都有明确的职责和人数要求',
            impact: 15
        });
    }
    
    return {
        overallScore: Math.round(overallScore),
        dimensions: dimensions,
        issues: issues,
        suggestions: suggestions,
        lastEvalTime: new Date().toISOString(),
        trend: 0
    };
}

function calculateBasicInfoScore(data) {
    let score = 0;
    const issues = [];
    const details = [];
    
    if (data.name && data.name.length > 0) {
        score += 30;
        details.push('名称已设置');
    } else {
        issues.push({
            severity: 'CRITICAL',
            field: 'name',
            description: '流程名称未设置',
            suggestion: '请设置流程名称'
        });
    }
    
    if (data.description && data.description.length > 0) {
        score += 30;
        details.push('描述已设置');
    } else {
        issues.push({
            severity: 'WARNING',
            field: 'description',
            description: '流程描述未设置',
            suggestion: '建议添加流程描述'
        });
    }
    
    if (data.category) {
        score += 20;
        details.push('分类已设置');
    } else {
        issues.push({
            severity: 'INFO',
            field: 'category',
            description: '流程分类未设置',
            suggestion: '建议设置流程分类'
        });
    }
    
    if (data.source) {
        score += 20;
        details.push('来源已设置');
    }
    
    return { score, issues, details: details.join(', ') };
}

function calculateRolesScore(data) {
    let score = 0;
    const issues = [];
    const details = [];
    
    if (data.roles && data.roles.length > 0) {
        score += 40;
        details.push(`${data.roles.length} 个角色`);
        
        const requiredRoles = data.roles.filter(r => r.required);
        if (requiredRoles.length > 0) {
            score += 20;
            details.push(`${requiredRoles.length} 个必需角色`);
        }
        
        const rolesWithDescription = data.roles.filter(r => r.description);
        if (rolesWithDescription.length === data.roles.length) {
            score += 20;
        } else {
            issues.push({
                severity: 'INFO',
                field: 'roles.description',
                description: `${data.roles.length - rolesWithDescription.length} 个角色缺少描述`,
                suggestion: '建议为每个角色添加描述'
            });
        }
        
        const rolesWithValidCount = data.roles.filter(r => r.minCount > 0);
        if (rolesWithValidCount.length === data.roles.length) {
            score += 20;
        } else {
            issues.push({
                severity: 'WARNING',
                field: 'roles.minCount',
                description: '部分角色最小人数为0',
                suggestion: '建议设置合理的最小人数'
            });
        }
    } else {
        issues.push({
            severity: 'CRITICAL',
            field: 'roles',
            description: '未定义任何角色',
            suggestion: '请添加流程所需的角色'
        });
    }
    
    return { score, issues, details: details.join(', ') };
}

function calculateStepsScore(data) {
    let score = 0;
    const issues = [];
    const details = [];
    
    if (data.steps && data.steps.length > 0) {
        score += 50;
        details.push(`${data.steps.length} 个步骤`);
        
        const stepsWithType = data.steps.filter(s => s.type);
        if (stepsWithType.length === data.steps.length) {
            score += 20;
        }
        
        const stepsWithRole = data.steps.filter(s => s.roleId);
        if (stepsWithRole.length === data.steps.length) {
            score += 20;
        } else {
            issues.push({
                severity: 'WARNING',
                field: 'steps.roleId',
                description: `${data.steps.length - stepsWithRole.length} 个步骤未分配角色`,
                suggestion: '建议为每个步骤分配执行角色'
            });
        }
        
        const stepsWithDescription = data.steps.filter(s => s.description);
        if (stepsWithDescription.length > 0) {
            score += 10;
        }
    } else {
        issues.push({
            severity: 'CRITICAL',
            field: 'steps',
            description: '未定义任何流程步骤',
            suggestion: '请添加流程步骤'
        });
    }
    
    return { score, issues, details: details.join(', ') };
}

function calculateRulesScore(data) {
    let score = 50;
    const issues = [];
    const details = [];
    
    if (data.rules && data.rules.length > 0) {
        score += 50;
        details.push(`${data.rules.length} 条规则`);
    } else {
        issues.push({
            severity: 'INFO',
            field: 'rules',
            description: '未定义业务规则',
            suggestion: '建议添加业务规则约束'
        });
    }
    
    return { score, issues, details: details.join(', ') };
}

function calculateDocsScore(data) {
    let score = 50;
    const issues = [];
    const details = [];
    
    if (data.documents && data.documents.length > 0) {
        score += 50;
        details.push(`${data.documents.length} 个文档`);
    } else {
        issues.push({
            severity: 'INFO',
            field: 'documents',
            description: '未关联任何文档',
            suggestion: '建议关联相关文档'
        });
    }
    
    return { score, issues, details: details.join(', ') };
}

function initRoleView(data) {
    const container = document.getElementById('rolesViewContainer');
    if (!container || !data.roles) return;
    
    roleView = new RoleView(container, {
        editable: true,
        showStatistics: true,
        showRelationships: true,
        onRoleClick: function(role, index) {
            console.log('Role clicked:', role);
        },
        onRoleEdit: function(role, index) {
            editRole(index);
        },
        onRoleDelete: function(role, index) {
            deleteRole(index);
        },
        onAssignUser: function(role, index) {
            assignUserToRole(index);
        }
    });
    
    const roles = data.roles.map(role => ({
        roleId: role.roleId,
        name: role.name,
        minCount: role.minCount,
        maxCount: role.maxCount,
        required: role.required,
        description: role.description,
        department: role.department,
        level: role.level,
        skills: role.skills || [],
        assignedUsers: role.assignedUsers || []
    }));
    
    roleView.loadRoles(roles);
}

function editRole(index) {
    console.log('Edit role:', index);
}

function deleteRole(index) {
    if (confirm('确定要删除该角色吗？')) {
        console.log('Delete role:', index);
    }
}

function assignUserToRole(index) {
    console.log('Assign user to role:', index);
}
