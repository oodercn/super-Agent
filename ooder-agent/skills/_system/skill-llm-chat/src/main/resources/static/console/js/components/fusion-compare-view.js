/**
 * FusionCompareView - 融合对比视图组件
 * 
 * 功能：
 * 1. 企业规范与技能定义对比
 * 2. 融合结果展示
 * 3. 差异高亮
 * 4. 冲突标记
 */

class FusionCompareView {
    constructor(container, options = {}) {
        this.container = typeof container === 'string' 
            ? document.querySelector(container) 
            : container;
        
        this.options = {
            mode: 'table',
            showDiff: true,
            showMatchScore: true,
            highlightConflicts: true,
            onConflictClick: null,
            onItemMerge: null,
            ...options
        };
        
        this.enterpriseData = null;
        this.skillData = null;
        this.fusedData = null;
        this.conflicts = [];
        this.selectedItem = null;
        
        this.init();
    }
    
    init() {
        this.container.classList.add('fusion-compare-view');
        this.render();
    }
    
    render() {
        let html = '';
        
        html += this.renderToolbar();
        
        if (this.options.mode === 'table') {
            html += this.renderTableView();
        } else {
            html += this.renderFlowView();
        }
        
        this.container.innerHTML = html;
        this.bindEvents();
    }
    
    renderToolbar() {
        return `
            <div class="fusion-toolbar">
                <div class="toolbar-left">
                    <div class="view-mode-toggle">
                        <button class="mode-btn ${this.options.mode === 'table' ? 'active' : ''}" 
                                onclick="this.closest('.fusion-compare-view')._fusionCompare.setViewMode('table')">
                            <i class="ri-table-line"></i>
                            表格视图
                        </button>
                        <button class="mode-btn ${this.options.mode === 'flow' ? 'active' : ''}"
                                onclick="this.closest('.fusion-compare-view')._fusionCompare.setViewMode('flow')">
                            <i class="ri-flow-chart"></i>
                            流程图视图
                        </button>
                    </div>
                </div>
                <div class="toolbar-right">
                    ${this.options.showDiff ? `
                        <label class="diff-toggle">
                            <input type="checkbox" checked onchange="this.closest('.fusion-compare-view')._fusionCompare.toggleDiff(this.checked)">
                            <span>显示差异</span>
                        </label>
                    ` : ''}
                    <button class="toolbar-btn" onclick="this.closest('.fusion-compare-view')._fusionCompare.exportCompare()">
                        <i class="ri-download-line"></i>
                        导出对比
                    </button>
                </div>
            </div>
        `;
    }
    
    renderTableView() {
        return `
            <div class="compare-table-container">
                <div class="compare-columns">
                    ${this.renderEnterpriseColumn()}
                    ${this.renderFusedColumn()}
                    ${this.renderSkillColumn()}
                </div>
            </div>
        `;
    }
    
    renderEnterpriseColumn() {
        return `
            <div class="compare-column enterprise">
                <div class="column-header">
                    <div class="column-title">
                        <i class="ri-building-line"></i>
                        企业规范流程
                    </div>
                    <div class="column-badge enterprise-badge">
                        ${this.enterpriseData?.name || '未关联'}
                    </div>
                </div>
                <div class="column-body">
                    ${this.renderEnterpriseContent()}
                </div>
            </div>
        `;
    }
    
    renderEnterpriseContent() {
        if (!this.enterpriseData) {
            return `<div class="empty-column">未关联企业规范流程</div>`;
        }
        
        let html = '';
        
        html += this.renderSection('基本信息', [
            { label: '名称', value: this.enterpriseData.name },
            { label: '分类', value: this.enterpriseData.category || '-' },
            { label: '来源', value: this.getSourceLabel(this.enterpriseData.source) }
        ]);
        
        if (this.enterpriseData.roles && this.enterpriseData.roles.length > 0) {
            html += this.renderSection('角色定义', this.enterpriseData.roles.map(role => ({
                label: role.name,
                value: `${role.minCount}-${role.maxCount === 0 ? '∞' : role.maxCount}人`,
                required: role.required,
                id: role.roleId
            })));
        }
        
        if (this.enterpriseData.steps && this.enterpriseData.steps.length > 0) {
            html += this.renderSection('流程步骤', this.enterpriseData.steps.map((step, index) => ({
                label: `${index + 1}. ${step.name}`,
                value: this.getStepTypeLabel(step.type),
                id: step.stepId
            })));
        }
        
        return html;
    }
    
    renderFusedColumn() {
        return `
            <div class="compare-column fused">
                <div class="column-header">
                    <div class="column-title">
                        <i class="ri-git-merge-line"></i>
                        融合结果
                    </div>
                    ${this.options.showMatchScore && this.fusedData ? `
                        <div class="match-score-badge ${this.getMatchScoreClass(this.fusedData.matchScore)}">
                            匹配度 ${this.fusedData.matchScore}%
                        </div>
                    ` : ''}
                </div>
                <div class="column-body">
                    ${this.renderFusedContent()}
                </div>
            </div>
        `;
    }
    
    renderFusedContent() {
        if (!this.fusedData) {
            return `<div class="empty-column">尚未执行融合</div>`;
        }
        
        let html = '';
        
        if (this.fusedData.roles && this.fusedData.roles.length > 0) {
            html += this.renderSection('融合后角色', this.fusedData.roles.map(role => ({
                label: role.name,
                value: `${role.minCount}-${role.maxCount === 0 ? '∞' : role.maxCount}人`,
                source: role.source,
                id: role.roleId,
                hasConflict: this.hasConflict('role', role.roleId)
            })), true);
        }
        
        if (this.fusedData.activationSteps) {
            const steps = [];
            Object.keys(this.fusedData.activationSteps).forEach(role => {
                this.fusedData.activationSteps[role].forEach(step => {
                    steps.push({
                        label: step.name,
                        value: step.stepType,
                        role: role,
                        id: step.stepId,
                        hasConflict: this.hasConflict('step', step.stepId)
                    });
                });
            });
            html += this.renderSection('激活步骤', steps, true);
        }
        
        if (this.conflicts.length > 0) {
            html += `
                <div class="conflicts-summary">
                    <div class="conflicts-header">
                        <i class="ri-alert-line"></i>
                        <span>发现 ${this.conflicts.length} 个冲突</span>
                    </div>
                    <div class="conflicts-list">
                        ${this.conflicts.map(conflict => `
                            <div class="conflict-item ${conflict.resolution ? 'resolved' : 'unresolved'}" 
                                 data-conflict-id="${conflict.conflictId}"
                                 onclick="this.closest('.fusion-compare-view')._fusionCompare.showConflictDetail('${conflict.conflictId}')">
                                <div class="conflict-icon">
                                    <i class="${conflict.resolution ? 'ri-check-line' : 'ri-alert-line'}"></i>
                                </div>
                                <div class="conflict-info">
                                    <div class="conflict-field">${conflict.field}</div>
                                    <div class="conflict-type">${conflict.type}</div>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                </div>
            `;
        }
        
        return html;
    }
    
    renderSkillColumn() {
        return `
            <div class="compare-column skill">
                <div class="column-header">
                    <div class="column-title">
                        <i class="ri-robot-line"></i>
                        技能场景定义
                    </div>
                    <div class="column-badge skill-badge">
                        ${this.skillData?.name || '未关联'}
                    </div>
                </div>
                <div class="column-body">
                    ${this.renderSkillContent()}
                </div>
            </div>
        `;
    }
    
    renderSkillContent() {
        if (!this.skillData) {
            return `<div class="empty-column">未关联技能</div>`;
        }
        
        let html = '';
        
        html += this.renderSection('基本信息', [
            { label: '名称', value: this.skillData.name },
            { label: '分类', value: this.skillData.category || '-' },
            { label: '意图', value: this.skillData.intent || '-' }
        ]);
        
        if (this.skillData.sceneTemplate?.roles) {
            html += this.renderSection('场景角色', this.skillData.sceneTemplate.roles.map(role => ({
                label: role.name,
                value: role.description || '-',
                id: role.id
            })));
        }
        
        if (this.skillData.sceneTemplate?.activationSteps) {
            html += this.renderSection('激活步骤', this.skillData.sceneTemplate.activationSteps.map((step, index) => ({
                label: `${index + 1}. ${step.name}`,
                value: step.stepType,
                id: step.id
            })));
        }
        
        return html;
    }
    
    renderSection(title, items, showSource = false) {
        return `
            <div class="compare-section">
                <div class="section-title">${title}</div>
                <div class="section-items">
                    ${items.map(item => this.renderItem(item, showSource)).join('')}
                </div>
            </div>
        `;
    }
    
    renderItem(item, showSource = false) {
        const diffClass = item.hasConflict ? 'has-conflict' : '';
        const sourceTag = showSource && item.source ? `<span class="source-tag ${item.source.toLowerCase()}">${item.source}</span>` : '';
        
        return `
            <div class="compare-item ${diffClass}" data-id="${item.id || ''}">
                <div class="item-header">
                    <span class="item-label">${item.label}</span>
                    ${item.required ? '<span class="required-tag">必需</span>' : ''}
                    ${sourceTag}
                </div>
                <div class="item-value">${item.value}</div>
                ${item.hasConflict ? `
                    <div class="conflict-indicator" onclick="event.stopPropagation(); this.closest('.fusion-compare-view')._fusionCompare.showConflictForItem('${item.id}')">
                        <i class="ri-alert-line"></i>
                    </div>
                ` : ''}
            </div>
        `;
    }
    
    renderFlowView() {
        return `
            <div class="compare-flow-container">
                <div class="flow-diagram" id="fusionFlowDiagram">
                    <div class="flow-empty">
                        <i class="ri-flow-chart"></i>
                        <p>流程图视图开发中...</p>
                    </div>
                </div>
            </div>
        `;
    }
    
    setViewMode(mode) {
        this.options.mode = mode;
        this.render();
    }
    
    toggleDiff(show) {
        this.options.showDiff = show;
        this.render();
    }
    
    showConflictDetail(conflictId) {
        const conflict = this.conflicts.find(c => c.conflictId === conflictId);
        if (conflict && this.options.onConflictClick) {
            this.options.onConflictClick(conflict);
        }
    }
    
    showConflictForItem(itemId) {
        const conflict = this.conflicts.find(c => c.field === itemId || c.itemId === itemId);
        if (conflict && this.options.onConflictClick) {
            this.options.onConflictClick(conflict);
        }
    }
    
    hasConflict(type, id) {
        return this.conflicts.some(c => c.type === type && (c.field === id || c.itemId === id));
    }
    
    getMatchScoreClass(score) {
        if (score >= 80) return 'high';
        if (score >= 50) return 'medium';
        return 'low';
    }
    
    getSourceLabel(source) {
        const labels = {
            'KNOWLEDGE_BASE': '知识库梳理',
            'INDUSTRY_BEST_PRACTICE': '行业最佳实践',
            'MANUAL': '手动创建'
        };
        return labels[source] || source;
    }
    
    getStepTypeLabel(type) {
        const labels = {
            'MANUAL': '人工执行',
            'AUTOMATIC': '自动执行',
            'APPROVAL': '审批节点',
            'PARALLEL': '并行网关',
            'EXCLUSIVE': '排他网关'
        };
        return labels[type] || type;
    }
    
    exportCompare() {
        const data = {
            enterprise: this.enterpriseData,
            skill: this.skillData,
            fused: this.fusedData,
            conflicts: this.conflicts,
            exportTime: new Date().toISOString()
        };
        
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `fusion-compare-${Date.now()}.json`;
        a.click();
        URL.revokeObjectURL(url);
    }
    
    bindEvents() {
        this.container._fusionCompare = this;
        
        this.container.querySelectorAll('.compare-item').forEach(item => {
            item.addEventListener('click', () => {
                this.selectItem(item.dataset.id);
            });
        });
    }
    
    selectItem(id) {
        this.container.querySelectorAll('.compare-item').forEach(item => {
            item.classList.remove('selected');
        });
        
        if (id) {
            this.container.querySelectorAll(`[data-id="${id}"]`).forEach(item => {
                item.classList.add('selected');
            });
            this.selectedItem = id;
        }
    }
    
    loadData(enterpriseData, skillData, fusedData, conflicts) {
        this.enterpriseData = enterpriseData;
        this.skillData = skillData;
        this.fusedData = fusedData;
        this.conflicts = conflicts || [];
        this.render();
    }
    
    setEnterpriseData(data) {
        this.enterpriseData = data;
        this.render();
    }
    
    setSkillData(data) {
        this.skillData = data;
        this.render();
    }
    
    setFusedData(data) {
        this.fusedData = data;
        this.render();
    }
    
    setConflicts(conflicts) {
        this.conflicts = conflicts || [];
        this.render();
    }
    
    getConflicts() {
        return this.conflicts;
    }
    
    getUnresolvedConflicts() {
        return this.conflicts.filter(c => !c.resolution);
    }
    
    getResolvedConflicts() {
        return this.conflicts.filter(c => c.resolution);
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = FusionCompareView;
}

window.FusionCompareView = FusionCompareView;
