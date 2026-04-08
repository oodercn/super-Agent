/**
 * ConflictResolver - 冲突解决组件
 * 
 * 功能：
 * 1. 冲突列表展示
 * 2. 冲突详情查看
 * 3. 解决方案选择
 * 4. 批量解决
 */

class ConflictResolver {
    constructor(container, options = {}) {
        this.container = typeof container === 'string' 
            ? document.querySelector(container) 
            : container;
        
        this.options = {
            showBatchResolution: true,
            showProgress: true,
            autoSelectRecommended: true,
            onResolve: null,
            onBatchResolve: null,
            ...options
        };
        
        this.conflicts = [];
        this.currentConflict = null;
        this.selectedResolution = null;
        this.batchOption = null;
        
        this.init();
    }
    
    init() {
        this.container.classList.add('conflict-resolver-wrapper');
        this.render();
    }
    
    render() {
        if (this.conflicts.length === 0) {
            this.renderEmpty();
            return;
        }
        
        let html = '';
        
        if (this.options.showProgress) {
            html += this.renderProgress();
        }
        
        html += this.renderFilters();
        html += this.renderConflictList();
        
        if (this.options.showBatchResolution && this.getUnresolvedCount() > 0) {
            html += this.renderBatchResolution();
        }
        
        this.container.innerHTML = html;
        this.bindEvents();
    }
    
    renderEmpty() {
        this.container.innerHTML = `
            <div class="conflict-empty-state">
                <div class="empty-icon">
                    <i class="ri-check-double-line"></i>
                </div>
                <div class="empty-title">没有冲突</div>
                <div class="empty-desc">所有冲突已解决或未检测到冲突</div>
            </div>
        `;
    }
    
    renderProgress() {
        const total = this.conflicts.length;
        const resolved = this.getResolvedCount();
        const unresolved = total - resolved;
        const progress = total > 0 ? Math.round((resolved / total) * 100) : 0;
        
        return `
            <div class="conflict-progress-section">
                <div class="progress-header">
                    <span class="progress-title">解决进度</span>
                    <span class="progress-stats">
                        <span class="stat resolved">${resolved} 已解决</span>
                        <span class="stat unresolved">${unresolved} 待解决</span>
                    </span>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${progress}%"></div>
                </div>
                <div class="progress-label">${progress}% 完成</div>
            </div>
        `;
    }
    
    renderFilters() {
        return `
            <div class="conflict-filters">
                <button class="filter-btn active" data-filter="all" onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.filterConflicts('all')">
                    全部 (${this.conflicts.length})
                </button>
                <button class="filter-btn" data-filter="unresolved" onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.filterConflicts('unresolved')">
                    待解决 (${this.getUnresolvedCount()})
                </button>
                <button class="filter-btn" data-filter="resolved" onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.filterConflicts('resolved')">
                    已解决 (${this.getResolvedCount()})
                </button>
            </div>
        `;
    }
    
    renderConflictList() {
        return `
            <div class="conflict-list-container">
                ${this.conflicts.map((conflict, index) => this.renderConflictItem(conflict, index)).join('')}
            </div>
        `;
    }
    
    renderConflictItem(conflict, index) {
        const isResolved = conflict.resolution !== null;
        const statusClass = isResolved ? 'resolved' : 'unresolved';
        const severityClass = conflict.severity ? conflict.severity.toLowerCase() : 'medium';
        
        return `
            <div class="conflict-item ${statusClass} ${severityClass}" data-conflict-id="${conflict.conflictId}" data-index="${index}">
                <div class="conflict-item-header">
                    <div class="conflict-item-left">
                        <div class="conflict-status-icon ${statusClass}">
                            <i class="${isResolved ? 'ri-check-line' : 'ri-alert-line'}"></i>
                        </div>
                        <div class="conflict-item-info">
                            <div class="conflict-field-name">${conflict.field}</div>
                            <div class="conflict-type-badge ${conflict.type}">${this.getConflictTypeLabel(conflict.type)}</div>
                        </div>
                    </div>
                    <div class="conflict-item-right">
                        <span class="conflict-status-badge ${statusClass}">
                            ${isResolved ? '已解决' : '待解决'}
                        </span>
                    </div>
                </div>
                
                <div class="conflict-item-body">
                    <div class="conflict-values">
                        <div class="value-card enterprise">
                            <div class="value-label">
                                <i class="ri-building-line"></i>
                                企业规范值
                            </div>
                            <div class="value-content ${conflict.enterpriseValue === null ? 'empty' : ''}">
                                ${this.formatValue(conflict.enterpriseValue)}
                            </div>
                        </div>
                        <div class="value-card skill">
                            <div class="value-label">
                                <i class="ri-robot-line"></i>
                                技能定义值
                            </div>
                            <div class="value-content ${conflict.skillValue === null ? 'empty' : ''}">
                                ${this.formatValue(conflict.skillValue)}
                            </div>
                        </div>
                    </div>
                    
                    ${isResolved ? this.renderResolvedInfo(conflict) : this.renderResolutionOptions(conflict, index)}
                </div>
            </div>
        `;
    }
    
    renderResolvedInfo(conflict) {
        return `
            <div class="resolved-info">
                <div class="resolved-label">
                    <i class="ri-check-line"></i>
                    解决方案: ${this.getResolutionLabel(conflict.resolution)}
                </div>
                <div class="resolved-value">
                    ${this.formatValue(conflict.resolvedValue)}
                </div>
            </div>
        `;
    }
    
    renderResolutionOptions(conflict, index) {
        const recommendedOption = this.getRecommendedResolution(conflict);
        
        return `
            <div class="resolution-options">
                <div class="options-title">选择解决方案</div>
                <div class="options-list">
                    <div class="option-item ${recommendedOption === 'ENTERPRISE' ? 'recommended' : ''}" 
                         data-option="ENTERPRISE" data-index="${index}"
                         onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.selectOption(this, 'ENTERPRISE', ${index})">
                        <div class="option-radio"></div>
                        <div class="option-content">
                            <div class="option-title">
                                使用企业规范值
                                ${recommendedOption === 'ENTERPRISE' ? '<span class="recommended-badge">推荐</span>' : ''}
                            </div>
                            <div class="option-preview">${this.formatValue(conflict.enterpriseValue)}</div>
                        </div>
                    </div>
                    <div class="option-item ${recommendedOption === 'SKILL' ? 'recommended' : ''}"
                         data-option="SKILL" data-index="${index}"
                         onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.selectOption(this, 'SKILL', ${index})">
                        <div class="option-radio"></div>
                        <div class="option-content">
                            <div class="option-title">
                                使用技能定义值
                                ${recommendedOption === 'SKILL' ? '<span class="recommended-badge">推荐</span>' : ''}
                            </div>
                            <div class="option-preview">${this.formatValue(conflict.skillValue)}</div>
                        </div>
                    </div>
                    <div class="option-item"
                         data-option="CUSTOM" data-index="${index}"
                         onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.selectOption(this, 'CUSTOM', ${index})">
                        <div class="option-radio"></div>
                        <div class="option-content">
                            <div class="option-title">自定义值</div>
                            <div class="option-custom-input" style="display: none;">
                                <input type="text" class="custom-value-input" placeholder="请输入自定义值..." 
                                       onclick="event.stopPropagation()"
                                       onchange="this.closest('.conflict-resolver-wrapper')._conflictResolver.setCustomValue(${index}, this.value)">
                            </div>
                        </div>
                    </div>
                </div>
                <button class="apply-resolution-btn" onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.applyResolution(${index})">
                    <i class="ri-check-line"></i>
                    应用解决方案
                </button>
            </div>
        `;
    }
    
    renderBatchResolution() {
        return `
            <div class="batch-resolution-section">
                <div class="batch-header">
                    <i class="ri-flashlight-line"></i>
                    <span>批量解决</span>
                </div>
                <div class="batch-options">
                    <div class="batch-option ${this.batchOption === 'ENTERPRISE' ? 'selected' : ''}" 
                         onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.selectBatchOption('ENTERPRISE')">
                        <div class="batch-option-icon">
                            <i class="ri-building-line"></i>
                        </div>
                        <div class="batch-option-label">全部使用企业规范</div>
                    </div>
                    <div class="batch-option ${this.batchOption === 'SKILL' ? 'selected' : ''}"
                         onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.selectBatchOption('SKILL')">
                        <div class="batch-option-icon">
                            <i class="ri-robot-line"></i>
                        </div>
                        <div class="batch-option-label">全部使用技能定义</div>
                    </div>
                </div>
                <button class="batch-apply-btn" onclick="this.closest('.conflict-resolver-wrapper')._conflictResolver.applyBatchResolution()">
                    <i class="ri-check-double-line"></i>
                    批量应用 (${this.getUnresolvedCount()} 项)
                </button>
            </div>
        `;
    }
    
    selectOption(element, option, index) {
        const conflictItem = element.closest('.conflict-item');
        conflictItem.querySelectorAll('.option-item').forEach(item => {
            item.classList.remove('selected');
        });
        element.classList.add('selected');
        
        const customInput = conflictItem.querySelector('.option-custom-input');
        if (customInput) {
            customInput.style.display = option === 'CUSTOM' ? 'block' : 'none';
        }
        
        this.conflicts[index].selectedOption = option;
    }
    
    setCustomValue(index, value) {
        this.conflicts[index].customValue = value;
    }
    
    applyResolution(index) {
        const conflict = this.conflicts[index];
        if (!conflict.selectedOption) {
            alert('请选择解决方案');
            return;
        }
        
        let resolvedValue;
        switch (conflict.selectedOption) {
            case 'ENTERPRISE':
                resolvedValue = conflict.enterpriseValue;
                break;
            case 'SKILL':
                resolvedValue = conflict.skillValue;
                break;
            case 'CUSTOM':
                resolvedValue = conflict.customValue;
                if (!resolvedValue) {
                    alert('请输入自定义值');
                    return;
                }
                break;
        }
        
        conflict.resolution = conflict.selectedOption;
        conflict.resolvedValue = resolvedValue;
        
        if (this.options.onResolve) {
            this.options.onResolve(conflict);
        }
        
        this.render();
    }
    
    selectBatchOption(option) {
        this.batchOption = option;
        this.container.querySelectorAll('.batch-option').forEach(item => {
            item.classList.remove('selected');
        });
        this.container.querySelector(`.batch-option:nth-child(${option === 'ENTERPRISE' ? '1' : '2'})`).classList.add('selected');
    }
    
    applyBatchResolution() {
        if (!this.batchOption) {
            alert('请选择批量解决策略');
            return;
        }
        
        const unresolvedConflicts = this.conflicts.filter(c => c.resolution === null);
        if (unresolvedConflicts.length === 0) {
            alert('没有待解决的冲突');
            return;
        }
        
        if (!confirm(`确定要批量解决 ${unresolvedConflicts.length} 个冲突吗？`)) {
            return;
        }
        
        unresolvedConflicts.forEach(conflict => {
            conflict.resolution = this.batchOption;
            conflict.resolvedValue = this.batchOption === 'ENTERPRISE' ? conflict.enterpriseValue : conflict.skillValue;
        });
        
        if (this.options.onBatchResolve) {
            this.options.onBatchResolve(unresolvedConflicts);
        }
        
        this.render();
    }
    
    filterConflicts(filter) {
        this.container.querySelectorAll('.filter-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        this.container.querySelector(`[data-filter="${filter}"]`).classList.add('active');
        
        this.container.querySelectorAll('.conflict-item').forEach(item => {
            const index = parseInt(item.dataset.index);
            const conflict = this.conflicts[index];
            const isResolved = conflict.resolution !== null;
            
            if (filter === 'all') {
                item.style.display = 'block';
            } else if (filter === 'resolved') {
                item.style.display = isResolved ? 'block' : 'none';
            } else if (filter === 'unresolved') {
                item.style.display = isResolved ? 'none' : 'block';
            }
        });
    }
    
    getRecommendedResolution(conflict) {
        if (conflict.enterpriseValue !== null && conflict.skillValue === null) {
            return 'ENTERPRISE';
        }
        if (conflict.skillValue !== null && conflict.enterpriseValue === null) {
            return 'SKILL';
        }
        return 'ENTERPRISE';
    }
    
    getConflictTypeLabel(type) {
        const labels = {
            'VALUE_MISMATCH': '值不匹配',
            'TYPE_MISMATCH': '类型不匹配',
            'MISSING_FIELD': '字段缺失',
            'STRUCTURE_MISMATCH': '结构不匹配'
        };
        return labels[type] || type;
    }
    
    getResolutionLabel(resolution) {
        const labels = {
            'ENTERPRISE': '使用企业规范值',
            'SKILL': '使用技能定义值',
            'CUSTOM': '自定义值'
        };
        return labels[resolution] || resolution;
    }
    
    formatValue(value) {
        if (value === null || value === undefined) {
            return '<span class="empty-value">无</span>';
        }
        if (typeof value === 'object') {
            return JSON.stringify(value);
        }
        return String(value);
    }
    
    getResolvedCount() {
        return this.conflicts.filter(c => c.resolution !== null).length;
    }
    
    getUnresolvedCount() {
        return this.conflicts.filter(c => c.resolution === null).length;
    }
    
    bindEvents() {
        this.container._conflictResolver = this;
    }
    
    loadConflicts(conflicts) {
        this.conflicts = conflicts || [];
        this.render();
    }
    
    addConflict(conflict) {
        this.conflicts.push(conflict);
        this.render();
    }
    
    updateConflict(index, conflict) {
        if (index >= 0 && index < this.conflicts.length) {
            this.conflicts[index] = conflict;
            this.render();
        }
    }
    
    removeConflict(index) {
        if (index >= 0 && index < this.conflicts.length) {
            this.conflicts.splice(index, 1);
            this.render();
        }
    }
    
    getConflicts() {
        return this.conflicts;
    }
    
    getResolvedConflicts() {
        return this.conflicts.filter(c => c.resolution !== null);
    }
    
    getUnresolvedConflicts() {
        return this.conflicts.filter(c => c.resolution === null);
    }
    
    isAllResolved() {
        return this.conflicts.every(c => c.resolution !== null);
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = ConflictResolver;
}

window.ConflictResolver = ConflictResolver;
