/**
 * CompletenessDashboard - 完善度仪表盘组件
 * 
 * 功能：
 * 1. 完善度总分展示
 * 2. 各维度得分可视化
 * 3. 问题列表展示
 * 4. 改进建议
 */

class CompletenessDashboard {
    constructor(container, options = {}) {
        this.container = typeof container === 'string' 
            ? document.querySelector(container) 
            : container;
        
        this.options = {
            showGauge: true,
            showDimensions: true,
            showIssues: true,
            showSuggestions: true,
            showTimeline: false,
            animate: true,
            onIssueClick: null,
            onFixIssue: null,
            ...options
        };
        
        this.data = null;
        this.animationFrame = null;
        
        this.init();
    }
    
    init() {
        this.container.classList.add('completeness-dashboard-wrapper');
        this.render();
    }
    
    render() {
        if (!this.data) {
            this.renderEmpty();
            return;
        }
        
        let html = '';
        
        if (this.options.showGauge) {
            html += this.renderGauge();
        }
        
        if (this.options.showDimensions && this.data.dimensions) {
            html += this.renderDimensions();
        }
        
        if (this.options.showIssues && this.data.issues && this.data.issues.length > 0) {
            html += this.renderIssues();
        }
        
        if (this.options.showSuggestions && this.data.suggestions && this.data.suggestions.length > 0) {
            html += this.renderSuggestions();
        }
        
        if (this.options.showTimeline && this.data.history) {
            html += this.renderTimeline();
        }
        
        this.container.innerHTML = html;
        
        if (this.options.animate) {
            this.animateGauge();
        }
        
        this.bindEvents();
    }
    
    renderEmpty() {
        this.container.innerHTML = `
            <div class="completeness-empty">
                <div class="completeness-empty-icon">
                    <i class="ri-pie-chart-line"></i>
                </div>
                <div class="completeness-empty-title">暂无完善度数据</div>
                <div class="completeness-empty-desc">请先配置企业规范流程</div>
            </div>
        `;
    }
    
    renderGauge() {
        const score = this.data.overallScore || 0;
        const level = this.getScoreLevel(score);
        const trend = this.data.trend || 0;
        
        return `
            <div class="completeness-gauge-section">
                <div class="gauge-main">
                    <div class="gauge-ring">
                        <svg width="160" height="160" viewBox="0 0 160 160">
                            <defs>
                                <linearGradient id="gaugeGradient-${this.container.id || 'default'}" x1="0%" y1="0%" x2="100%" y2="0%">
                                    <stop offset="0%" style="stop-color:${level.color1}"/>
                                    <stop offset="100%" style="stop-color:${level.color2}"/>
                                </linearGradient>
                            </defs>
                            <circle class="gauge-bg-ring" cx="80" cy="80" r="70" fill="none" stroke-width="12"/>
                            <circle class="gauge-fill-ring" cx="80" cy="80" r="70" fill="none" 
                                stroke="url(#gaugeGradient-${this.container.id || 'default'})" 
                                stroke-width="12"
                                stroke-linecap="round"
                                stroke-dasharray="439.8"
                                stroke-dashoffset="439.8"
                                transform="rotate(-90 80 80)"/>
                        </svg>
                        <div class="gauge-center-content">
                            <div class="gauge-score">${score}</div>
                            <div class="gauge-unit">%</div>
                        </div>
                    </div>
                    <div class="gauge-info">
                        <div class="gauge-level ${level.class}">
                            <i class="${level.icon}"></i>
                            <span>${level.label}</span>
                        </div>
                        <div class="gauge-trend ${trend >= 0 ? 'up' : 'down'}">
                            <i class="ri-arrow-${trend >= 0 ? 'up' : 'down'}-line"></i>
                            <span>${Math.abs(trend)}%</span>
                        </div>
                    </div>
                </div>
                <div class="gauge-summary">
                    <div class="summary-item">
                        <span class="summary-label">已完成维度</span>
                        <span class="summary-value">${this.getCompletedDimensions()}</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-label">待改进项</span>
                        <span class="summary-value warning">${this.data.issues?.length || 0}</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-label">上次评估</span>
                        <span class="summary-value">${this.formatTime(this.data.lastEvalTime)}</span>
                    </div>
                </div>
            </div>
        `;
    }
    
    renderDimensions() {
        return `
            <div class="completeness-dimensions-section">
                <div class="section-header">
                    <h4><i class="ri-bar-chart-grouped-line"></i> 维度详情</h4>
                </div>
                <div class="dimensions-list">
                    ${this.data.dimensions.map((dim, index) => this.renderDimensionItem(dim, index)).join('')}
                </div>
            </div>
        `;
    }
    
    renderDimensionItem(dim, index) {
        const status = this.getDimensionStatus(dim.score);
        const delay = this.options.animate ? `animation-delay: ${index * 0.1}s` : '';
        
        return `
            <div class="dimension-item ${status.class}" style="${delay}">
                <div class="dimension-header">
                    <div class="dimension-info">
                        <span class="dimension-icon ${status.class}">
                            <i class="${dim.icon || this.getDimensionIcon(dim.name)}"></i>
                        </span>
                        <span class="dimension-name">${dim.name}</span>
                    </div>
                    <div class="dimension-score-section">
                        <span class="dimension-score ${status.class}">${dim.score}%</span>
                        ${dim.weight ? `<span class="dimension-weight">权重 ${dim.weight}%</span>` : ''}
                    </div>
                </div>
                <div class="dimension-bar">
                    <div class="dimension-fill ${status.class}" style="width: 0%" data-target="${dim.score}"></div>
                </div>
                <div class="dimension-footer">
                    <span class="dimension-status ${status.class}">
                        <i class="${status.icon}"></i>
                        ${status.label}
                    </span>
                    ${dim.details ? `<span class="dimension-details">${dim.details}</span>` : ''}
                </div>
            </div>
        `;
    }
    
    renderIssues() {
        const criticalIssues = this.data.issues.filter(i => i.severity === 'CRITICAL');
        const warningIssues = this.data.issues.filter(i => i.severity === 'WARNING');
        const infoIssues = this.data.issues.filter(i => i.severity === 'INFO');
        
        return `
            <div class="completeness-issues-section">
                <div class="section-header">
                    <h4><i class="ri-alert-line"></i> 待完善项</h4>
                    <span class="issues-count">${this.data.issues.length}</span>
                </div>
                
                ${criticalIssues.length > 0 ? `
                    <div class="issues-group critical">
                        <div class="group-header">
                            <i class="ri-error-warning-line"></i>
                            <span>严重问题 (${criticalIssues.length})</span>
                        </div>
                        <div class="issues-list">
                            ${criticalIssues.map(issue => this.renderIssueItem(issue)).join('')}
                        </div>
                    </div>
                ` : ''}
                
                ${warningIssues.length > 0 ? `
                    <div class="issues-group warning">
                        <div class="group-header">
                            <i class="ri-alarm-warning-line"></i>
                            <span>警告 (${warningIssues.length})</span>
                        </div>
                        <div class="issues-list">
                            ${warningIssues.map(issue => this.renderIssueItem(issue)).join('')}
                        </div>
                    </div>
                ` : ''}
                
                ${infoIssues.length > 0 ? `
                    <div class="issues-group info">
                        <div class="group-header">
                            <i class="ri-information-line"></i>
                            <span>建议 (${infoIssues.length})</span>
                        </div>
                        <div class="issues-list">
                            ${infoIssues.map(issue => this.renderIssueItem(issue)).join('')}
                        </div>
                    </div>
                ` : ''}
            </div>
        `;
    }
    
    renderIssueItem(issue) {
        return `
            <div class="issue-item ${issue.severity.toLowerCase()}" data-field="${issue.field}">
                <div class="issue-icon">
                    <i class="${this.getIssueIcon(issue.severity)}"></i>
                </div>
                <div class="issue-content">
                    <div class="issue-description">${issue.description}</div>
                    ${issue.suggestion ? `<div class="issue-suggestion">${issue.suggestion}</div>` : ''}
                </div>
                <button class="issue-fix-btn" onclick="this.closest('.completeness-dashboard-wrapper')._dashboard.fixIssue('${issue.field}')">
                    <i class="ri-tools-line"></i>
                    修复
                </button>
            </div>
        `;
    }
    
    renderSuggestions() {
        return `
            <div class="completeness-suggestions-section">
                <div class="section-header">
                    <h4><i class="ri-lightbulb-line"></i> 改进建议</h4>
                </div>
                <div class="suggestions-list">
                    ${this.data.suggestions.map(suggestion => `
                        <div class="suggestion-item">
                            <div class="suggestion-icon">
                                <i class="ri-arrow-right-circle-line"></i>
                            </div>
                            <div class="suggestion-content">
                                <div class="suggestion-title">${suggestion.title}</div>
                                ${suggestion.description ? `<div class="suggestion-desc">${suggestion.description}</div>` : ''}
                                ${suggestion.impact ? `<div class="suggestion-impact">预计提升: +${suggestion.impact}%</div>` : ''}
                            </div>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;
    }
    
    renderTimeline() {
        return `
            <div class="completeness-timeline-section">
                <div class="section-header">
                    <h4><i class="ri-history-line"></i> 历史趋势</h4>
                </div>
                <div class="timeline-chart">
                    <svg width="100%" height="100" class="timeline-svg">
                        ${this.renderTimelineBars()}
                    </svg>
                </div>
                <div class="timeline-labels">
                    ${this.data.history.map(h => `<span class="timeline-label">${this.formatDate(h.time)}</span>`).join('')}
                </div>
            </div>
        `;
    }
    
    renderTimelineBars() {
        if (!this.data.history || this.data.history.length === 0) return '';
        
        const maxScore = 100;
        const barWidth = 100 / this.data.history.length;
        
        return this.data.history.map((h, i) => {
            const height = (h.score / maxScore) * 80;
            const x = i * barWidth + barWidth / 2 - 5;
            const y = 90 - height;
            
            return `
                <rect x="${x}%" y="${y}" width="10" height="${height}" 
                    fill="url(#gaugeGradient-${this.container.id || 'default'})" 
                    rx="2" class="timeline-bar"/>
            `;
        }).join('');
    }
    
    getScoreLevel(score) {
        if (score >= 90) {
            return { label: '优秀', class: 'excellent', icon: 'ri-trophy-line', color1: '#22c55e', color2: '#16a34a' };
        } else if (score >= 70) {
            return { label: '良好', class: 'good', icon: 'ri-thumb-up-line', color1: '#3b82f6', color2: '#2563eb' };
        } else if (score >= 50) {
            return { label: '一般', class: 'warning', icon: 'ri-alert-line', color1: '#f59e0b', color2: '#d97706' };
        } else {
            return { label: '较差', class: 'danger', icon: 'ri-error-warning-line', color1: '#ef4444', color2: '#dc2626' };
        }
    }
    
    getDimensionStatus(score) {
        if (score >= 80) {
            return { label: '完善', class: 'complete', icon: 'ri-check-line' };
        } else if (score >= 50) {
            return { label: '部分完善', class: 'partial', icon: 'ri-question-line' };
        } else {
            return { label: '待完善', class: 'missing', icon: 'ri-close-line' };
        }
    }
    
    getDimensionIcon(name) {
        const icons = {
            '基础信息': 'ri-information-line',
            '角色定义': 'ri-team-line',
            '流程步骤': 'ri-flow-chart',
            '规则约束': 'ri-git-branch-line',
            '文档附件': 'ri-file-text-line',
            '权限配置': 'ri-shield-check-line',
            '知识库': 'ri-database-2-line'
        };
        return icons[name] || 'ri-checkbox-circle-line';
    }
    
    getIssueIcon(severity) {
        const icons = {
            'CRITICAL': 'ri-error-warning-fill',
            'WARNING': 'ri-alarm-warning-fill',
            'INFO': 'ri-information-fill'
        };
        return icons[severity] || 'ri-information-fill';
    }
    
    getCompletedDimensions() {
        if (!this.data.dimensions) return 0;
        return this.data.dimensions.filter(d => d.score >= 80).length + '/' + this.data.dimensions.length;
    }
    
    formatTime(timestamp) {
        if (!timestamp) return '-';
        const date = new Date(timestamp);
        return date.toLocaleString('zh-CN', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
    }
    
    formatDate(timestamp) {
        if (!timestamp) return '-';
        const date = new Date(timestamp);
        return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' });
    }
    
    animateGauge() {
        const score = this.data?.overallScore || 0;
        const circumference = 2 * Math.PI * 70;
        const offset = circumference - (score / 100) * circumference;
        
        requestAnimationFrame(() => {
            const fillRing = this.container.querySelector('.gauge-fill-ring');
            if (fillRing) {
                fillRing.style.transition = 'stroke-dashoffset 1s ease-out';
                fillRing.style.strokeDashoffset = offset;
            }
            
            const dimensionFills = this.container.querySelectorAll('.dimension-fill');
            dimensionFills.forEach(fill => {
                const target = fill.dataset.target;
                setTimeout(() => {
                    fill.style.transition = 'width 0.5s ease-out';
                    fill.style.width = target + '%';
                }, 100);
            });
        });
    }
    
    bindEvents() {
        this.container._dashboard = this;
        
        this.container.querySelectorAll('.issue-item').forEach(item => {
            item.addEventListener('click', (e) => {
                if (!e.target.closest('.issue-fix-btn')) {
                    const field = item.dataset.field;
                    if (this.options.onIssueClick) {
                        this.options.onIssueClick(field);
                    }
                }
            });
        });
    }
    
    fixIssue(field) {
        if (this.options.onFixIssue) {
            this.options.onFixIssue(field);
        }
    }
    
    loadData(data) {
        this.data = data;
        this.render();
    }
    
    refresh() {
        if (this.data) {
            this.render();
        }
    }
    
    getScore() {
        return this.data?.overallScore || 0;
    }
    
    getIssues() {
        return this.data?.issues || [];
    }
    
    getDimensions() {
        return this.data?.dimensions || [];
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = CompletenessDashboard;
}

window.CompletenessDashboard = CompletenessDashboard;
