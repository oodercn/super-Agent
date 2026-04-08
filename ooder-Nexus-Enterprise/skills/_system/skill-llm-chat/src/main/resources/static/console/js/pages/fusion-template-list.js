(function() {
    'use strict';

    let templates = [];
    let currentStep = 1;
    let wizardData = {
        procedureId: null,
        skillId: null,
        fusionStrategy: null,
        name: '',
        description: ''
    };

    function $(selector) {
        return document.querySelector(selector);
    }

    function $$(selector) {
        return document.querySelectorAll(selector);
    }

    document.addEventListener('DOMContentLoaded', function() {
        loadTemplates();
        loadSkills();
    });

    function loadTemplates() {
        fetch('/api/v1/fused-templates')
            .then(res => res.json())
            .then(data => {
                templates = (data && data.list) ? data.list : (data || []);
                renderTemplates(templates);
            })
            .catch(error => {
                console.error('Failed to load templates:', error);
                showError('加载融合模板失败');
            });
    }

    function loadSkills() {
        fetch('/api/v1/capabilities?type=SCENE')
            .then(res => res.json())
            .then(data => {
                if (data && data.content) {
                    const select = $('#skillFilter');
                    data.content.forEach(function(skill) {
                        const option = document.createElement('option');
                        option.value = skill.capId;
                        option.textContent = skill.name;
                        select.appendChild(option);
                    });
                }
            });
    }

    function renderTemplates(data) {
        const grid = $('#templateGrid');
        
        if (!data || data.length === 0) {
            grid.innerHTML = `
                <div class="empty-state">
                    <i class="ri-git-merge-line"></i>
                    <p>暂无融合模板</p>
                    <p>点击"创建融合模板"开始</p>
                </div>
            `;
            return;
        }
        
        let html = '';
        data.forEach(function(template) {
            html += renderTemplateCard(template);
        });
        grid.innerHTML = html;
    }

    function renderTemplateCard(template) {
        const statusClass = getStatusClass(template.status);
        const statusText = getStatusText(template.status);
        const scoreClass = getScoreClass(template.matchScore);
        const conflictBadge = template.fusionConflicts && template.fusionConflicts.length > 0 
            ? `<span class="conflict-badge"><i class="ri-alert-line"></i> ${template.fusionConflicts.length} 个冲突</span>` 
            : '';
        
        return `
            <div class="nx-card template-card" data-id="${template.templateId}" data-status="${template.status}" data-skill="${template.skillId || ''}">
                <div class="nx-card-header">
                    <h3 class="nx-card-title">${template.name || '未命名模板'}</h3>
                    <span class="template-status ${statusClass}">${statusText}</span>
                </div>
                <div class="nx-card-body">
                    <div class="template-info">
                        <div class="info-row">
                            <i class="ri-file-list-3-line"></i>
                            <span>企业规范: ${template.enterpriseProcedureName || template.enterpriseProcedureId || '-'}</span>
                        </div>
                        <div class="info-row">
                            <i class="ri-puzzle-line"></i>
                            <span>技能: ${template.skillName || template.skillId || '-'}</span>
                        </div>
                        <div class="info-row">
                            <i class="ri-calendar-line"></i>
                            <span>创建时间: ${formatTime(template.fusionTime || template.createTime)}</span>
                        </div>
                        <div class="info-row match-score">
                            <i class="ri-percent-line"></i>
                            <span>匹配度:</span>
                            <div class="score-bar">
                                <div class="score-fill ${scoreClass}" style="width: ${template.matchScore || 0}%"></div>
                            </div>
                            <span>${template.matchScore || 0}%</span>
                        </div>
                    </div>
                    ${conflictBadge}
                    <div class="template-tags">
                        ${template.fusionStrategy ? `<span class="tag">${getStrategyText(template.fusionStrategy)}</span>` : ''}
                    </div>
                </div>
                <div class="template-actions">
                    <span class="nx-card-meta">${template.fusedBy || '系统'}</span>
                    <div class="action-btns">
                        <button class="btn-sm" onclick="viewTemplate('${template.templateId}')">
                            <i class="ri-eye-line"></i> 查看
                        </button>
                        <button class="btn-sm btn-primary" onclick="createSceneGroup('${template.templateId}')">
                            <i class="ri-play-line"></i> 创建场景组
                        </button>
                    </div>
                </div>
            </div>
        `;
    }

    function getStatusClass(status) {
        switch (status) {
            case 'ACTIVE': return 'status-active';
            case 'ARCHIVED': return 'status-archived';
            default: return 'status-draft';
        }
    }

    function getStatusText(status) {
        switch (status) {
            case 'ACTIVE': return '已激活';
            case 'ARCHIVED': return '已归档';
            default: return '草稿';
        }
    }

    function getScoreClass(score) {
        if (score >= 80) return 'score-high';
        if (score >= 50) return 'score-medium';
        return 'score-low';
    }

    function getStrategyText(strategy) {
        if (!strategy) return '';
        if (typeof strategy === 'object') {
            return strategy.priority || '自定义策略';
        }
        return strategy;
    }

    function formatTime(timestamp) {
        if (!timestamp) return '-';
        const date = new Date(timestamp);
        return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
    }

    window.filterTemplates = function() {
        const searchText = $('#searchInput').value.toLowerCase();
        const statusFilter = $('#statusFilter').value;
        const skillFilter = $('#skillFilter').value;
        
        let filtered = templates.filter(function(t) {
            const matchName = !searchText || (t.name && t.name.toLowerCase().includes(searchText));
            const matchStatus = !statusFilter || t.status === statusFilter;
            const matchSkill = !skillFilter || t.skillId === skillFilter;
            return matchName && matchStatus && matchSkill;
        });
        
        renderTemplates(filtered);
    };

    window.viewTemplate = function(templateId) {
        window.location.href = '/console/pages/fusion-template-detail.html?id=' + templateId;
    };

    window.createSceneGroup = function(templateId) {
        if (!confirm('确定要基于此融合模板创建场景组吗？')) {
            return;
        }
        
        fetch('/api/v1/scene-groups/from-fusion', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ fusionTemplateId: templateId })
        })
        .then(res => res.json())
        .then(data => {
            showSuccess('场景组创建成功');
            window.location.href = '/console/pages/scene-group-detail.html?id=' + data.sceneGroupId;
        })
        .catch(error => {
            showError('创建场景组失败: ' + error.message);
        });
    };

    window.openFusionWizard = function() {
        currentStep = 1;
        wizardData = {
            procedureId: null,
            skillId: null,
            fusionStrategy: null,
            name: '',
            description: ''
        };
        updateWizardSteps();
        loadProceduresForWizard();
        $('#fusionWizard').style.display = 'flex';
    };

    window.closeFusionWizard = function() {
        $('#fusionWizard').style.display = 'none';
    };

    function updateWizardSteps() {
        $$('.wizard-step').forEach(function(step, index) {
            step.classList.remove('active', 'completed');
            const stepNum = index + 1;
            if (stepNum < currentStep) {
                step.classList.add('completed');
            } else if (stepNum === currentStep) {
                step.classList.add('active');
            }
        });
        
        const prevBtn = $('#prevBtn');
        const nextBtn = $('#nextBtn');
        
        prevBtn.style.display = currentStep > 1 ? 'inline-flex' : 'none';
        
        if (currentStep === 4) {
            nextBtn.innerHTML = '<i class="ri-check-line"></i> 创建';
        } else {
            nextBtn.innerHTML = '下一步 <i class="ri-arrow-right-line"></i>';
        }
    }

    function loadProceduresForWizard() {
        fetch('/api/v1/enterprise-procedures')
            .then(res => res.json())
            .then(data => {
                const procedures = (data && data.data) ? data.data : (data || []);
                renderWizardStep1(procedures);
            })
            .catch(function() {
                showError('加载企业规范失败');
            });
    }

    function renderWizardStep1(procedures) {
        let html = `
            <div class="wizard-step-content">
            <h3>选择企业规范流程</h3>
            <div class="procedure-list">
        `;
        
        if (procedures.length === 0) {
            html += `
                <div class="empty-state">
                    <i class="ri-file-list-3-line"></i>
                    <p>暂无企业规范流程</p>
                    <a href="/console/pages/enterprise-procedure-list.html" class="nx-btn nx-btn--primary">
                        去创建
                    </a>
                </div>
            `;
        } else {
            procedures.forEach(function(proc) {
                const selected = wizardData.procedureId === proc.procedureId ? 'selected' : '';
                html += `
                    <div class="procedure-item ${selected}" onclick="selectProcedure('${proc.procedureId}', '${proc.name}')">
                        <div class="title">${proc.name}</div>
                        <div class="meta">
                            ${proc.category || '未分类'} · 完善度: ${proc.completeness || 0}%
                        </div>
                    </div>
                `;
            });
        }
        
        html += '</div></div>';
        $('#wizardStepContent').innerHTML = html;
    }

    window.selectProcedure = function(procedureId, name) {
        wizardData.procedureId = procedureId;
        wizardData.procedureName = name;
        $$('.procedure-item').forEach(function(item) {
            item.classList.remove('selected');
        });
        event.currentTarget.classList.add('selected');
    };

    window.nextStep = function() {
        if (currentStep === 1 && !wizardData.procedureId) {
            showError('请选择企业规范流程');
            return;
        }
        
        if (currentStep === 2 && !wizardData.skillId) {
            showError('请选择技能');
            return;
        }
        
        if (currentStep === 4) {
            createFusionTemplate();
            return;
        }
        
        currentStep++;
        updateWizardSteps();
        renderCurrentStep();
    };

    window.prevStep = function() {
        if (currentStep > 1) {
            currentStep--;
            updateWizardSteps();
            renderCurrentStep();
        }
    };

    function renderCurrentStep() {
        switch (currentStep) {
            case 1:
                loadProceduresForWizard();
                break;
            case 2:
                renderWizardStep2();
                break;
            case 3:
                renderWizardStep3();
                break;
            case 4:
                renderWizardStep4();
                break;
        }
    }

    function renderWizardStep2() {
        let html = `
            <div class="wizard-step-content">
            <h3>选择技能</h3>
            <div class="skill-list">
        `;
        
        fetch('/api/v1/capabilities?type=SCENE')
            .then(res => res.json())
            .then(data => {
                if (data && data.content) {
                    data.content.forEach(function(skill) {
                        const selected = wizardData.skillId === skill.capId ? 'selected' : '';
                        html += `
                            <div class="skill-item ${selected}" onclick="selectSkill('${skill.capId}', '${skill.name}')">
                                <div class="title">${skill.name}</div>
                                <div class="meta">
                                    ${skill.description || '无描述'}
                                </div>
                            </div>
                        `;
                    });
                }
                html += '</div></div>';
                $('#wizardStepContent').innerHTML = html;
            });
    }

    window.selectSkill = function(skillId, name) {
        wizardData.skillId = skillId;
        wizardData.skillName = name;
        $$('.skill-item').forEach(function(item) {
            item.classList.remove('selected');
        });
        event.currentTarget.classList.add('selected');
    };

    function renderWizardStep3() {
        let html = `
            <div class="wizard-step-content">
            <h3>融合配置</h3>
            <div class="form-group">
                <label>模板名称</label>
                <input type="text" id="templateName" value="${wizardData.name || wizardData.procedureName || ''}">
            </div>
            <div class="form-group">
                <label>描述</label>
                <textarea id="templateDesc" rows="3">${wizardData.description || ''}</textarea>
            </div>
            <div class="form-group">
                <label>融合策略</label>
                <select id="fusionStrategy">
                    <option value="ENTERPRISE_FIRST">企业规范优先</option>
                    <option value="SKILL_FIRST">技能定义优先</option>
                    <option value="MERGE">智能合并</option>
                    <option value="USER_DECIDE">用户决定</option>
                </select>
            </div>
            <div class="form-group">
                <label>
                    <input type="checkbox" id="autoResolve">
                    自动解决冲突
                </label>
            </div>
            </div>
        `;
        $('#wizardStepContent').innerHTML = html;
    }

    function renderWizardStep4() {
        wizardData.name = $('#templateName').value;
        wizardData.description = $('#templateDesc').value;
        wizardData.fusionStrategy = $('#fusionStrategy').value;
        wizardData.autoResolve = $('#autoResolve').checked;
        
        let html = `
            <div class="wizard-step-content">
            <h3>确认创建</h3>
            <div class="confirm-section">
                <div class="confirm-row">
                    <strong>模板名称:</strong> ${wizardData.name}
                </div>
                <div class="confirm-row">
                    <strong>企业规范:</strong> ${wizardData.procedureName}
                </div>
                <div class="confirm-row">
                    <strong>技能:</strong> ${wizardData.skillName}
                </div>
                <div class="confirm-row">
                    <strong>融合策略:</strong> ${getStrategyText(wizardData.fusionStrategy)}
                </div>
                <div class="confirm-row">
                    <strong>自动解决冲突:</strong> ${wizardData.autoResolve ? '是' : '否'}
                </div>
            </div>
            </div>
        `;
        $('#wizardStepContent').innerHTML = html;
    }

    function createFusionTemplate() {
        const request = {
            enterpriseProcedureId: wizardData.procedureId,
            skillId: wizardData.skillId,
            name: wizardData.name,
            description: wizardData.description,
            fusionStrategy: {
                priority: wizardData.fusionStrategy,
                autoResolveConflict: wizardData.autoResolve
            }
        };
        
        fetch('/api/v1/fused-templates/fuse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        })
        .then(res => res.json())
        .then(data => {
            showSuccess('融合模板创建成功');
            closeFusionWizard();
            loadTemplates();
        })
        .catch(error => {
            showError('创建融合模板失败: ' + error.message);
        });
    }

    function showSuccess(message) {
        alert(message);
    }

    function showError(message) {
        alert(message);
    }
})();
