/**
 * RuleEditor - 规则编辑器组件
 * 
 * 功能：
 * 1. 规则CRUD操作
 * 2. 可视化规则构建器
 * 3. 规则验证
 * 4. 规则模板
 */

class RuleEditor {
    constructor(container, options = {}) {
        this.container = typeof container === 'string' 
            ? document.querySelector(container) 
            : container;
        
        this.options = {
            editable: true,
            showTemplates: true,
            variables: [],
            onRuleChange: null,
            ...options
        };
        
        this.rules = [];
        this.currentEditingRule = null;
        
        this.init();
    }
    
    init() {
        this.container.classList.add('rule-editor-container');
        this.render();
    }
    
    render() {
        let html = `
            <div class="rule-editor-header">
                <div class="rule-editor-title">
                    <i class="ri-git-branch-line"></i>
                    约束规则
                </div>
                <div class="rule-editor-actions">
                    <button class="rule-btn rule-btn-secondary" onclick="this.closest('.rule-editor-container')._ruleEditor.showTemplates()">
                        <i class="ri-file-copy-line"></i> 模板
                    </button>
                    <button class="rule-btn rule-btn-primary" onclick="this.closest('.rule-editor-container')._ruleEditor.addRule()">
                        <i class="ri-add-line"></i> 添加规则
                    </button>
                </div>
            </div>
            <div class="rule-editor-body">
                <div class="rule-list" id="ruleList"></div>
            </div>
        `;
        
        this.container.innerHTML = html;
        this.container._ruleEditor = this;
        
        this.ruleListEl = this.container.querySelector('#ruleList');
        this.renderRules();
    }
    
    renderRules() {
        if (this.rules.length === 0) {
            this.ruleListEl.innerHTML = `
                <div class="rule-empty">
                    <div class="rule-empty-icon">
                        <i class="ri-git-branch-line"></i>
                    </div>
                    <div class="rule-empty-title">暂无约束规则</div>
                    <div class="rule-empty-desc">添加规则以约束流程执行条件</div>
                    ${this.options.showTemplates ? `
                        <div class="rule-templates">
                            <button class="rule-template-btn" onclick="this.closest('.rule-editor-container')._ruleEditor.addFromTemplate('role-count')">
                                <i class="ri-user-line"></i> 角色数量约束
                            </button>
                            <button class="rule-template-btn" onclick="this.closest('.rule-editor-container')._ruleEditor.addFromTemplate('step-order')">
                                <i class="ri-list-ordered"></i> 步骤顺序约束
                            </button>
                            <button class="rule-template-btn" onclick="this.closest('.rule-editor-container')._ruleEditor.addFromTemplate('time-limit')">
                                <i class="ri-time-line"></i> 时间限制
                            </button>
                        </div>
                    ` : ''}
                </div>
            `;
            return;
        }
        
        let html = '';
        this.rules.forEach((rule, index) => {
            html += this.renderRuleItem(rule, index);
        });
        
        this.ruleListEl.innerHTML = html;
    }
    
    renderRuleItem(rule, index) {
        const isEditing = this.currentEditingRule === index;
        
        return `
            <div class="rule-item ${isEditing ? 'editing' : ''}" data-rule-index="${index}">
                <div class="rule-item-header">
                    <div class="rule-item-info">
                        <div class="rule-item-icon ${rule.type}">
                            <i class="${this.getRuleIcon(rule.type)}"></i>
                        </div>
                        <div>
                            <div class="rule-item-name">${rule.name}</div>
                            <div class="rule-item-type">${this.getRuleTypeLabel(rule.type)}</div>
                        </div>
                    </div>
                    <div class="rule-item-actions">
                        <button class="rule-item-btn" onclick="this.closest('.rule-editor-container')._ruleEditor.editRule(${index})" title="编辑">
                            <i class="ri-edit-line"></i>
                        </button>
                        <button class="rule-item-btn danger" onclick="this.closest('.rule-editor-container')._ruleEditor.deleteRule(${index})" title="删除">
                            <i class="ri-delete-bin-line"></i>
                        </button>
                    </div>
                </div>
                <div class="rule-item-body">
                    ${isEditing ? this.renderEditForm(rule, index) : this.renderRulePreview(rule)}
                </div>
            </div>
        `;
    }
    
    renderRulePreview(rule) {
        return `
            <div class="rule-expression">
                ${this.formatExpression(rule.expression)}
            </div>
            ${rule.description ? `<div class="rule-description">${rule.description}</div>` : ''}
        `;
    }
    
    renderEditForm(rule, index) {
        return `
            <div class="rule-edit-form">
                <div class="rule-form-group">
                    <label class="rule-form-label">规则名称</label>
                    <input type="text" class="rule-form-input" id="ruleName_${index}" value="${rule.name}" placeholder="输入规则名称">
                </div>
                <div class="rule-form-group">
                    <label class="rule-form-label">规则类型</label>
                    <select class="rule-form-select" id="ruleType_${index}">
                        <option value="constraint" ${rule.type === 'constraint' ? 'selected' : ''}>约束条件</option>
                        <option value="validation" ${rule.type === 'validation' ? 'selected' : ''}>验证规则</option>
                        <option value="trigger" ${rule.type === 'trigger' ? 'selected' : ''}>触发条件</option>
                    </select>
                </div>
                <div class="rule-form-group">
                    <label class="rule-form-label">规则表达式</label>
                    <div class="rule-builder">
                        <div class="rule-builder-toolbar">
                            <button class="rule-builder-btn" onclick="this.closest('.rule-editor-container')._ruleEditor.addCondition(${index})">
                                <i class="ri-add-line"></i> 添加条件
                            </button>
                            <button class="rule-builder-btn" onclick="this.closest('.rule-editor-container')._ruleEditor.addGroup(${index})">
                                <i class="ri-folder-line"></i> 添加分组
                            </button>
                        </div>
                        <div class="rule-builder-content" id="ruleExpression_${index}">
                            ${this.renderConditionBuilder(rule.conditions || [], index)}
                        </div>
                    </div>
                </div>
                <div class="rule-form-group">
                    <label class="rule-form-label">描述</label>
                    <textarea class="rule-form-textarea" id="ruleDescription_${index}" placeholder="输入规则描述">${rule.description || ''}</textarea>
                </div>
                ${this.options.variables.length > 0 ? `
                    <div class="rule-variables-panel">
                        <div class="rule-variables-title">可用变量</div>
                        <div class="rule-variables-list">
                            ${this.options.variables.map(v => `
                                <span class="rule-variable-chip" onclick="this.closest('.rule-editor-container')._ruleEditor.insertVariable('${v}', ${index})">${v}</span>
                            `).join('')}
                        </div>
                    </div>
                ` : ''}
                <div class="rule-form-group">
                    <div class="rule-validation success" id="ruleValidation_${index}">
                        <i class="ri-check-line"></i>
                        <span>规则表达式有效</span>
                    </div>
                </div>
                <div style="display: flex; gap: 10px; margin-top: 16px;">
                    <button class="rule-btn rule-btn-primary" onclick="this.closest('.rule-editor-container')._ruleEditor.saveRule(${index})">
                        <i class="ri-check-line"></i> 保存
                    </button>
                    <button class="rule-btn rule-btn-secondary" onclick="this.closest('.rule-editor-container')._ruleEditor.cancelEdit()">
                        <i class="ri-close-line"></i> 取消
                    </button>
                </div>
            </div>
        `;
    }
    
    renderConditionBuilder(conditions, ruleIndex) {
        if (!conditions || conditions.length === 0) {
            return `
                <div class="rule-condition-group">
                    <div class="rule-condition-row">
                        <div class="rule-condition-field">
                            <input type="text" class="rule-form-input" placeholder="字段名">
                        </div>
                        <div class="rule-condition-operator">
                            <select class="rule-form-select">
                                <option value="eq">等于</option>
                                <option value="ne">不等于</option>
                                <option value="gt">大于</option>
                                <option value="lt">小于</option>
                                <option value="ge">大于等于</option>
                                <option value="le">小于等于</option>
                                <option value="contains">包含</option>
                                <option value="matches">匹配</option>
                            </select>
                        </div>
                        <div class="rule-condition-value">
                            <input type="text" class="rule-form-input" placeholder="值">
                        </div>
                    </div>
                </div>
            `;
        }
        
        let html = '<div class="rule-condition-group">';
        
        conditions.forEach((condition, condIndex) => {
            if (condIndex > 0) {
                html += `
                    <div class="rule-logic-operator">
                        <button class="rule-logic-btn ${condition.logic === 'AND' ? 'active' : ''}">AND</button>
                        <button class="rule-logic-btn ${condition.logic === 'OR' ? 'active' : ''}">OR</button>
                    </div>
                `;
            }
            
            html += `
                <div class="rule-condition-row">
                    <div class="rule-condition-field">
                        <input type="text" class="rule-form-input" value="${condition.field || ''}" placeholder="字段名">
                    </div>
                    <div class="rule-condition-operator">
                        <select class="rule-form-select">
                            <option value="eq" ${condition.operator === 'eq' ? 'selected' : ''}>等于</option>
                            <option value="ne" ${condition.operator === 'ne' ? 'selected' : ''}>不等于</option>
                            <option value="gt" ${condition.operator === 'gt' ? 'selected' : ''}>大于</option>
                            <option value="lt" ${condition.operator === 'lt' ? 'selected' : ''}>小于</option>
                            <option value="ge" ${condition.operator === 'ge' ? 'selected' : ''}>大于等于</option>
                            <option value="le" ${condition.operator === 'le' ? 'selected' : ''}>小于等于</option>
                            <option value="contains" ${condition.operator === 'contains' ? 'selected' : ''}>包含</option>
                            <option value="matches" ${condition.operator === 'matches' ? 'selected' : ''}>匹配</option>
                        </select>
                    </div>
                    <div class="rule-condition-value">
                        <input type="text" class="rule-form-input" value="${condition.value || ''}" placeholder="值">
                    </div>
                    <button class="rule-condition-remove" onclick="this.closest('.rule-editor-container')._ruleEditor.removeCondition(${ruleIndex}, ${condIndex})">
                        <i class="ri-close-line"></i>
                    </button>
                </div>
            `;
        });
        
        html += '</div>';
        return html;
    }
    
    formatExpression(expression) {
        if (!expression) return '';
        
        return expression
            .replace(/\b(AND|OR|NOT)\b/g, '<span class="rule-expression-keyword">$1</span>')
            .replace(/\b(eq|ne|gt|lt|ge|le|contains|matches)\b/g, '<span class="rule-expression-operator">$1</span>')
            .replace(/'([^']*)'/g, '<span class="rule-expression-string">\'$1\'</span>')
            .replace(/\b(\d+)\b/g, '<span class="rule-expression-number">$1</span>')
            .replace(/\b(true|false)\b/g, '<span class="rule-expression-boolean">$1</span>')
            .replace(/\$\{([^}]+)\}/g, '<span class="rule-expression-variable">\${$1}</span>');
    }
    
    getRuleIcon(type) {
        const icons = {
            'constraint': 'ri-lock-line',
            'validation': 'ri-shield-check-line',
            'trigger': 'ri-flashlight-line'
        };
        return icons[type] || 'ri-git-branch-line';
    }
    
    getRuleTypeLabel(type) {
        const labels = {
            'constraint': '约束条件',
            'validation': '验证规则',
            'trigger': '触发条件'
        };
        return labels[type] || type;
    }
    
    addRule() {
        const newRule = {
            id: `rule_${Date.now()}`,
            name: '新规则',
            type: 'constraint',
            expression: '',
            conditions: [],
            description: ''
        };
        
        this.rules.push(newRule);
        this.currentEditingRule = this.rules.length - 1;
        this.renderRules();
    }
    
    editRule(index) {
        this.currentEditingRule = index;
        this.renderRules();
    }
    
    saveRule(index) {
        const name = document.getElementById(`ruleName_${index}`).value;
        const type = document.getElementById(`ruleType_${index}`).value;
        const description = document.getElementById(`ruleDescription_${index}`).value;
        
        this.rules[index].name = name;
        this.rules[index].type = type;
        this.rules[index].description = description;
        this.rules[index].expression = this.buildExpression(this.rules[index].conditions);
        
        this.currentEditingRule = null;
        this.renderRules();
        
        if (this.options.onRuleChange) {
            this.options.onRuleChange(this.rules);
        }
    }
    
    cancelEdit() {
        this.currentEditingRule = null;
        this.renderRules();
    }
    
    deleteRule(index) {
        if (confirm('确定要删除此规则吗？')) {
            this.rules.splice(index, 1);
            this.currentEditingRule = null;
            this.renderRules();
            
            if (this.options.onRuleChange) {
                this.options.onRuleChange(this.rules);
            }
        }
    }
    
    addCondition(ruleIndex) {
        if (!this.rules[ruleIndex].conditions) {
            this.rules[ruleIndex].conditions = [];
        }
        
        this.rules[ruleIndex].conditions.push({
            field: '',
            operator: 'eq',
            value: '',
            logic: this.rules[ruleIndex].conditions.length > 0 ? 'AND' : null
        });
        
        this.renderRules();
    }
    
    removeCondition(ruleIndex, condIndex) {
        this.rules[ruleIndex].conditions.splice(condIndex, 1);
        if (this.rules[ruleIndex].conditions.length > 0) {
            this.rules[ruleIndex].conditions[0].logic = null;
        }
        this.renderRules();
    }
    
    addGroup(ruleIndex) {
        this.addCondition(ruleIndex);
    }
    
    insertVariable(variable, ruleIndex) {
        const textarea = document.getElementById(`ruleDescription_${ruleIndex}`);
        if (textarea) {
            const start = textarea.selectionStart;
            const end = textarea.selectionEnd;
            const text = textarea.value;
            textarea.value = text.substring(0, start) + '${' + variable + '}' + text.substring(end);
            textarea.focus();
            textarea.selectionStart = textarea.selectionEnd = start + variable.length + 3;
        }
    }
    
    buildExpression(conditions) {
        if (!conditions || conditions.length === 0) return '';
        
        return conditions.map((c, i) => {
            const prefix = i > 0 ? ` ${c.logic || 'AND'} ` : '';
            return `${prefix}${c.field} ${c.operator} ${this.formatValue(c.value)}`;
        }).join('');
    }
    
    formatValue(value) {
        if (typeof value === 'string') {
            if (value.startsWith('${')) return value;
            return `'${value}'`;
        }
        return value;
    }
    
    addFromTemplate(templateType) {
        const templates = {
            'role-count': {
                id: `rule_${Date.now()}`,
                name: '角色数量约束',
                type: 'constraint',
                expression: "${roleCount} ge ${minCount} AND ${roleCount} le ${maxCount}",
                conditions: [
                    { field: '${roleCount}', operator: 'ge', value: '${minCount}', logic: null },
                    { field: '${roleCount}', operator: 'le', value: '${maxCount}', logic: 'AND' }
                ],
                description: '约束角色数量在指定范围内'
            },
            'step-order': {
                id: `rule_${Date.now()}`,
                name: '步骤顺序约束',
                type: 'constraint',
                expression: "${currentStep} gt ${previousStep}",
                conditions: [
                    { field: '${currentStep}', operator: 'gt', value: '${previousStep}', logic: null }
                ],
                description: '确保步骤按正确顺序执行'
            },
            'time-limit': {
                id: `rule_${Date.now()}`,
                name: '时间限制',
                type: 'validation',
                expression: "${duration} le ${maxDuration}",
                conditions: [
                    { field: '${duration}', operator: 'le', value: '${maxDuration}', logic: null }
                ],
                description: '限制流程执行时间'
            }
        };
        
        const template = templates[templateType];
        if (template) {
            this.rules.push(template);
            this.currentEditingRule = this.rules.length - 1;
            this.renderRules();
            
            if (this.options.onRuleChange) {
                this.options.onRuleChange(this.rules);
            }
        }
    }
    
    showTemplates() {
        alert('规则模板功能开发中...');
    }
    
    loadRules(rules) {
        this.rules = rules || [];
        this.currentEditingRule = null;
        this.renderRules();
    }
    
    getRules() {
        return this.rules.map(r => ({...r}));
    }
    
    validateRules() {
        const errors = [];
        
        this.rules.forEach((rule, index) => {
            if (!rule.name) {
                errors.push({ index, field: 'name', message: '规则名称不能为空' });
            }
            if (!rule.expression) {
                errors.push({ index, field: 'expression', message: '规则表达式不能为空' });
            }
        });
        
        return {
            valid: errors.length === 0,
            errors
        };
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = RuleEditor;
}

window.RuleEditor = RuleEditor;
