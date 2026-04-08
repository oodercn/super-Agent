/**
 * LLM增强组件JavaScript
 * 包含：状态指示器、上下文层级展示、配置继承链
 */

const LLMEnhancement = {
    contextStatus: null,
    refreshInterval: null,

    init: function() {
        this.loadContextStatus();
        this.startAutoRefresh();
    },

    loadContextStatus: function() {
        fetch('/api/v1/context/status')
            .then(response => response.json())
            .then(data => {
                this.contextStatus = data;
                this.updateUI();
            })
            .catch(error => {
                console.error('[LLMEnhancement] Failed to load context status:', error);
            });
    },

    startAutoRefresh: function(intervalMs) {
        if (this.refreshInterval) {
            clearInterval(this.refreshInterval);
        }
        this.refreshInterval = setInterval(() => {
            this.loadContextStatus();
        }, intervalMs || 30000);
    },

    stopAutoRefresh: function() {
        if (this.refreshInterval) {
            clearInterval(this.refreshInterval);
            this.refreshInterval = null;
        }
    },

    updateUI: function() {
        this.updateStatusIndicator();
        this.updateContextLevelViewer();
    },

    updateStatusIndicator: function() {
        const indicators = document.querySelectorAll('.llm-status-indicator');
        indicators.forEach(indicator => {
            const skillEl = indicator.querySelector('#current-skill');
            const levelEl = indicator.querySelector('#context-level');
            const toolsEl = indicator.querySelector('#tools-count');
            const kbEl = indicator.querySelector('#kb-bindings');

            if (this.contextStatus) {
                if (skillEl) {
                    skillEl.textContent = (this.contextStatus.skill && this.contextStatus.skill.currentSkillId) || '-';
                }
                if (levelEl) {
                    levelEl.textContent = this.contextStatus.level || '-';
                }
                if (toolsEl) {
                    toolsEl.textContent = (this.contextStatus.skill && this.contextStatus.skill.toolsCount || 0) + '个';
                }
                if (kbEl) {
                    kbEl.textContent = (this.contextStatus.skill && this.contextStatus.skill.kbBindings || 0) + '个';
                }
            } else {
                if (skillEl) skillEl.textContent = '-';
                if (levelEl) levelEl.textContent = '-';
                if (toolsEl) toolsEl.textContent = '-';
                if (kbEl) kbEl.textContent = '-';
            }
        });
    },

    updateContextLevelViewer: function() {
        const viewers = document.querySelectorAll('.context-level-viewer');
        viewers.forEach(viewer => {
            const levels = viewer.querySelectorAll('.context-level');
            levels.forEach(level => {
                const levelNum = level.dataset.level;
                const isActive = this.isLevelActive(parseInt(levelNum));
                level.dataset.active = isActive ? 'true' : 'false';
                
                const statusIcon = level.querySelector('.level-status');
                if (statusIcon) {
                    statusIcon.className = 'level-status ' + (isActive ? 'ri-check-line' : 'ri-close-line inactive');
                }
            });
        });
    },

    isLevelActive: function(level) {
        if (!this.contextStatus) return false;
        
        switch(level) {
            case 0: return this.contextStatus.global && this.contextStatus.global.initialized;
            case 1: return this.contextStatus.skill && this.contextStatus.skill.active;
            case 2: return this.contextStatus.page && this.contextStatus.page.active;
            case 3: return this.contextStatus.session && this.contextStatus.session.active;
            default: return false;
        }
    },

    renderStatusIndicator: function(containerId, options) {
        const container = document.getElementById(containerId);
        if (!container) return;

        const compact = options && options.compact ? 'compact' : '';
        
        container.innerHTML = `
            <div class="llm-status-indicator ${compact}">
                <div class="llm-status-item">
                    <i class="ri-robot-line"></i>
                    <span class="label">当前技能</span>
                    <span class="value" id="current-skill">加载中...</span>
                </div>
                <div class="llm-status-item">
                    <i class="ri-layers-line"></i>
                    <span class="label">上下文层级</span>
                    <span class="value" id="context-level">-</span>
                </div>
                <div class="llm-status-item">
                    <i class="ri-tools-line"></i>
                    <span class="label">可用工具</span>
                    <span class="value" id="tools-count">-</span>
                </div>
                <div class="llm-status-item">
                    <i class="ri-database-2-line"></i>
                    <span class="label">知识库</span>
                    <span class="value" id="kb-bindings">-</span>
                </div>
            </div>
        `;
        
        this.updateStatusIndicator();
    },

    renderContextLevelViewer: function(containerId) {
        const container = document.getElementById(containerId);
        if (!container) return;

        container.innerHTML = `
            <div class="context-level-viewer">
                <div class="context-level" data-level="0" data-active="true">
                    <div class="level-header" onclick="LLMEnhancement.toggleLevel(this)">
                        <span class="level-badge l0">L0</span>
                        <span class="level-name">全局上下文</span>
                        <i class="ri-check-line level-status"></i>
                    </div>
                    <div class="level-content">
                        <div class="level-item clickable" onclick="LLMEnhancement.navigateToConfig('global')">
                            <i class="ri-menu-line"></i> 菜单模块: <span class="item-value" id="global-menu-count">-</span>
                        </div>
                        <div class="level-item clickable" onclick="LLMEnhancement.navigateToConfig('tools')">
                            <i class="ri-tools-line"></i> 全局工具: <span class="item-value" id="global-tools-count">-</span>
                        </div>
                    </div>
                </div>
                <div class="context-level" data-level="1" data-active="false">
                    <div class="level-header" onclick="LLMEnhancement.toggleLevel(this)">
                        <span class="level-badge l1">L1</span>
                        <span class="level-name">技能上下文</span>
                        <i class="ri-check-line level-status"></i>
                    </div>
                    <div class="level-content">
                        <div class="level-item clickable" onclick="LLMEnhancement.navigateToConfig('skill')">
                            <i class="ri-puzzle-line"></i> 技能: <span class="item-value" id="skill-id">-</span>
                        </div>
                        <div class="level-item clickable" onclick="LLMEnhancement.navigateToConfig('prompt')">
                            <i class="ri-file-text-line"></i> Prompt: <span class="item-value" id="skill-prompt">-</span>
                        </div>
                        <div class="level-item clickable" onclick="LLMEnhancement.navigateToConfig('tools')">
                            <i class="ri-tools-line"></i> 工具: <span class="item-value" id="skill-tools">-</span>
                        </div>
                        <div class="level-item clickable" onclick="LLMEnhancement.navigateToConfig('knowledge')">
                            <i class="ri-database-2-line"></i> 知识库: <span class="item-value" id="skill-kb">-</span>
                        </div>
                    </div>
                </div>
                <div class="context-level" data-level="2" data-active="false">
                    <div class="level-header" onclick="LLMEnhancement.toggleLevel(this)">
                        <span class="level-badge l2">L2</span>
                        <span class="level-name">页面上下文</span>
                        <i class="ri-check-line level-status"></i>
                    </div>
                    <div class="level-content">
                        <div class="level-item clickable" onclick="LLMEnhancement.navigateToConfig('page')">
                            <i class="ri-pages-line"></i> 页面: <span class="item-value" id="page-id">-</span>
                        </div>
                        <div class="level-item clickable" onclick="LLMEnhancement.navigateToConfig('api')">
                            <i class="ri-code-line"></i> API: <span class="item-value" id="page-api">-</span>
                        </div>
                    </div>
                </div>
                <div class="context-level" data-level="3" data-active="false">
                    <div class="level-header" onclick="LLMEnhancement.toggleLevel(this)">
                        <span class="level-badge l3">L3</span>
                        <span class="level-name">会话上下文</span>
                        <i class="ri-check-line level-status"></i>
                    </div>
                    <div class="level-content">
                        <div class="level-item">
                            <i class="ri-chat-3-line"></i> 会话ID: <span class="item-value" id="session-id">-</span>
                        </div>
                        <div class="level-item">
                            <i class="ri-message-3-line"></i> 消息数: <span class="item-value" id="session-messages">-</span>
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        this.updateContextLevelViewer();
        this.loadContextStatus();
    },

    navigateToConfig: function(type) {
        console.log('[LLMEnhancement] Navigate to config type:', type);
        
        const pageMap = {
            'global': 'llm-config.html',
            'tools': 'capability-management.html',
            'skill': 'capability-discovery.html',
            'prompt': 'llm-config.html',
            'knowledge': 'knowledge-center.html',
            'page': 'scene-group-management.html',
            'api': 'comm-config.html'
        };
        
        const targetPage = pageMap[type];
        if (targetPage && targetPage !== window.location.pathname.split('/').pop()) {
            window.location.href = targetPage;
        }
    },

    renderConfigChain: function(containerId, targetType, targetId) {
        const container = document.getElementById(containerId);
        if (!container) return;

        fetch(`/api/v1/config-meta/inheritance-detail/${targetType}/${targetId}`)
            .then(response => response.json())
            .then(data => {
                let html = '<div class="config-chain">';
                
                if (data.chain && data.chain.length > 0) {
                    data.chain.forEach((item, index) => {
                        const isLast = index === data.chain.length - 1;
                        const iconClass = this.getChainIconClass(item.level);
                        
                        html += `
                            <div class="chain-item" data-level="${item.level}" onclick="LLMEnhancement.onChainItemClick('${item.level}', '${item.source}')">
                                <div class="chain-icon ${item.level}">
                                    <i class="${iconClass}"></i>
                                </div>
                                <div class="chain-info">
                                    <div class="chain-name">${this.getChainName(item.level)}</div>
                                    <div class="chain-source">${item.source}</div>
                                </div>
                                ${!isLast ? '<div class="chain-arrow"><i class="ri-arrow-down-line"></i></div>' : ''}
                            </div>
                        `;
                    });
                } else {
                    html += '<div class="chain-empty"><i class="ri-link"></i><span>暂无配置继承链</span></div>';
                }
                
                html += '</div>';
                container.innerHTML = html;
            })
            .catch(error => {
                console.error('[LLMEnhancement] Failed to load config chain:', error);
                container.innerHTML = '<div class="config-chain"><div class="chain-empty"><i class="ri-error-warning-line"></i><span>加载配置继承链失败</span></div></div>';
            });
    },

    onChainItemClick: function(level, source) {
        console.log('[LLMEnhancement] Chain item clicked:', level, source);
        
        const pageMap = {
            'system': 'config-system.html',
            'skill': 'capability-discovery.html',
            'scene': 'scene-group-management.html',
            'capability': 'capability-management.html'
        };
        
        const targetPage = pageMap[level];
        if (targetPage) {
            if (confirm(`是否跳转到 ${this.getChainName(level)} 页面？`)) {
                window.location.href = targetPage;
            }
        }
    },

    getChainIconClass: function(level) {
        const icons = {
            'system': 'ri-server-line',
            'skill': 'ri-puzzle-line',
            'scene': 'ri-layout-line',
            'capability': 'ri-flashlight-line'
        };
        return icons[level] || 'ri-file-line';
    },

    getChainName: function(level) {
        const names = {
            'system': '系统配置',
            'skill': '技能配置',
            'scene': '场景配置',
            'capability': '能力配置'
        };
        return names[level] || level;
    },

    toggleLevel: function(header) {
        const level = header.closest('.context-level');
        if (level) {
            level.classList.toggle('expanded');
        }
    },

    renderKbBindings: function(containerId, kbId) {
        const container = document.getElementById(containerId);
        if (!container) return;

        fetch(`/api/v1/knowledge-bases/${kbId}/bindings`)
            .then(response => response.json())
            .then(data => {
                let html = `
                    <div class="kb-bindings-panel">
                        <div class="kb-bindings-header">
                            <div class="kb-bindings-title">
                                <i class="ri-link"></i> 知识库绑定
                            </div>
                            <span class="nx-text-secondary">${data.totalBindings || 0}个绑定</span>
                        </div>
                        <div class="kb-bindings-body">
                `;
                
                if (data.bindings && data.bindings.length > 0) {
                    data.bindings.forEach(binding => {
                        html += `
                            <div class="kb-binding-item">
                                <div class="kb-binding-icon ${binding.layer ? binding.layer.toLowerCase() : 'general'}">
                                    <i class="${this.getBindingIcon(binding.type)}"></i>
                                </div>
                                <div class="kb-binding-info">
                                    <div class="kb-binding-name">${binding.name}</div>
                                    <div class="kb-binding-purpose">${binding.purpose || ''}</div>
                                </div>
                                ${binding.priority ? `<span class="kb-binding-priority">P${binding.priority}</span>` : ''}
                            </div>
                        `;
                    });
                } else {
                    html += '<div class="nx-text-secondary" style="padding: 12px; text-align: center;">暂无绑定</div>';
                }
                
                html += '</div></div>';
                container.innerHTML = html;
            })
            .catch(error => {
                console.error('[LLMEnhancement] Failed to load kb bindings:', error);
                container.innerHTML = '<div class="nx-text-secondary">加载知识库绑定失败</div>';
            });
    },

    getBindingIcon: function(type) {
        const icons = {
            'skill': 'ri-puzzle-line',
            'scene': 'ri-layout-line',
            'prompt': 'ri-file-text-line',
            'capability': 'ri-flashlight-line'
        };
        return icons[type] || 'ri-link';
    },

    navigateToPage: function(pageId) {
        fetch('/api/v1/context/navigate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                toPage: pageId,
                fromPage: window.location.pathname.split('/').pop().replace('.html', '')
            })
        })
        .then(response => response.json())
        .then(data => {
            console.log('[LLMEnhancement] Navigate result:', data);
            if (data.success) {
                window.location.href = data.pagePath || `/console/pages/${pageId}.html`;
            }
        })
        .catch(error => {
            console.error('[LLMEnhancement] Navigate failed:', error);
        });
    },

    switchContext: function(skillId, pageId) {
        const requests = [];
        
        if (skillId) {
            requests.push(
                fetch('/api/v1/context/skill-change', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ toSkill: skillId })
                })
            );
        }
        
        if (pageId) {
            requests.push(
                fetch('/api/v1/context/navigate', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ toPage: pageId })
                })
            );
        }
        
        return Promise.all(requests)
            .then(() => {
                this.loadContextStatus();
                return true;
            })
            .catch(error => {
                console.error('[LLMEnhancement] Switch context failed:', error);
                return false;
            });
    },

    renderContextSwitcher: function(containerId, options) {
        const container = document.getElementById(containerId);
        if (!container) return;

        const skills = (options && options.skills) || [
            { id: 'skill-scene', name: '场景技能', icon: 'ri-puzzle-line' },
            { id: 'skill-knowledge', name: '知识库技能', icon: 'ri-database-2-line' },
            { id: 'skill-discovery', name: '发现技能', icon: 'ri-compass-3-line' },
            { id: 'skill-install', name: '安装技能', icon: 'ri-download-2-line' }
        ];

        let html = `
            <div class="context-switcher">
                <div class="context-switcher-header">
                    <span class="context-switcher-title"><i class="ri-swap-line"></i> 切换上下文</span>
                </div>
                <div class="context-switcher-body">
                    <div class="context-switcher-section">
                        <label>选择技能</label>
                        <div class="context-switcher-options">
        `;

        skills.forEach(skill => {
            html += `
                <div class="context-option" data-skill-id="${skill.id}" onclick="LLMEnhancement.selectSkill('${skill.id}')">
                    <i class="${skill.icon}"></i>
                    <span>${skill.name}</span>
                </div>
            `;
        });

        html += `
                        </div>
                    </div>
                </div>
            </div>
        `;

        container.innerHTML = html;
    },

    selectSkill: function(skillId) {
        document.querySelectorAll('.context-option').forEach(el => {
            el.classList.remove('active');
        });
        document.querySelector(`.context-option[data-skill-id="${skillId}"]`)?.classList.add('active');
        
        this.switchContext(skillId, null).then(success => {
            if (success) {
                this.showNotification('上下文已切换到: ' + skillId, 'success');
            }
        });
    },

    showNotification: function(message, type) {
        const notification = document.createElement('div');
        notification.className = `llm-notification llm-notification--${type || 'info'}`;
        notification.innerHTML = `
            <i class="${type === 'success' ? 'ri-check-line' : type === 'error' ? 'ri-error-warning-line' : 'ri-information-line'}"></i>
            <span>${message}</span>
        `;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.classList.add('llm-notification--show');
        }, 10);
        
        setTimeout(() => {
            notification.classList.remove('llm-notification--show');
            setTimeout(() => notification.remove(), 300);
        }, 3000);
    },

    renderPromptEditor: function(containerId, skillId) {
        const container = document.getElementById(containerId);
        if (!container) return;

        container.innerHTML = `
            <div class="prompt-editor">
                <div class="prompt-editor-header">
                    <span class="prompt-editor-title"><i class="ri-file-text-line"></i> SystemPrompt 编辑</span>
                    <div class="prompt-editor-actions">
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="LLMEnhancement.loadDefaultPrompt('${skillId}')">
                            <i class="ri-refresh-line"></i> 重置
                        </button>
                        <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="LLMEnhancement.savePrompt('${skillId}')">
                            <i class="ri-save-line"></i> 保存
                        </button>
                    </div>
                </div>
                <div class="prompt-editor-body">
                    <textarea id="prompt-content" class="prompt-textarea" placeholder="输入 SystemPrompt 内容..."></textarea>
                </div>
                <div class="prompt-editor-footer">
                    <span class="nx-text-sm nx-text-secondary">支持 Markdown 格式</span>
                    <span class="nx-text-sm nx-text-secondary" id="prompt-char-count">0 字符</span>
                </div>
            </div>
        `;

        this.loadPrompt(skillId);
    },

    loadPrompt: function(skillId) {
        fetch(`/api/v1/llm/prompt/${skillId}`)
            .then(response => response.json())
            .then(data => {
                const textarea = document.getElementById('prompt-content');
                if (textarea && data.prompt) {
                    textarea.value = data.prompt;
                    this.updateCharCount();
                }
            })
            .catch(error => {
                console.error('[LLMEnhancement] Load prompt failed:', error);
            });
    },

    savePrompt: function(skillId) {
        const content = document.getElementById('prompt-content')?.value;
        if (!content) return;

        fetch(`/api/v1/llm/prompt/${skillId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ prompt: content })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                this.showNotification('Prompt 保存成功', 'success');
            } else {
                this.showNotification('保存失败: ' + (data.error || '未知错误'), 'error');
            }
        })
        .catch(error => {
            console.error('[LLMEnhancement] Save prompt failed:', error);
            this.showNotification('保存失败', 'error');
        });
    },

    loadDefaultPrompt: function(skillId) {
        if (confirm('确定要重置为默认 Prompt 吗？')) {
            this.loadPrompt(skillId + '?default=true');
        }
    },

    updateCharCount: function() {
        const textarea = document.getElementById('prompt-content');
        const counter = document.getElementById('prompt-char-count');
        if (textarea && counter) {
            counter.textContent = textarea.value.length + ' 字符';
        }
    }
};

document.addEventListener('DOMContentLoaded', function() {
    LLMEnhancement.init();
});
