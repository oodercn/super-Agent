/**
 * LLM Chat Float Component - Enhanced Version
 * LLM聊天悬浮窗口组件 - 增强版
 * 支持：多模型切换、上下文管理、Prompt模板、Session级别对话、Skills上下文注入
 */

(function() {
    'use strict';

    const LLMChatFloat = {
        initialized: false,
        isOpen: false,
        useKnowledge: false,
        isStreaming: false,
        messages: [],
        sessionId: null,
        currentModel: null,
        availableModels: [],
        promptTemplates: [],
        contextHistory: [],
        config: {
            enabled: true,
            position: 'bottom-right',
            title: 'AI助手',
            provider: 'mock',
            model: 'default',
            maxHistory: 10,
            maxContextLength: 4000,
            temperature: 0.7,
            streamEnabled: true
        },

        async init() {
            if (this.initialized) return;

            const savedConfig = localStorage.getItem('llm-chat-float-config');
            if (savedConfig) {
                try {
                    this.config = { ...this.config, ...JSON.parse(savedConfig) };
                } catch (e) {}
            }

            if (!this.config.enabled) return;

            await Promise.all([
                this.loadLlmConfig(),
                this.loadAvailableModels(),
                this.loadPromptTemplates()
            ]);
            
            this.sessionId = this.generateSessionId();
            this.render();
            this.bindEvents();
            this.initialized = true;

            console.log('[LLMChatFloat] Initialized with provider:', this.config.provider, 'model:', this.config.model);
        },

        generateSessionId() {
            return 'session-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
        },

        async loadLlmConfig() {
            try {
                const response = await fetch('/api/v1/llm/config');
                if (response.ok) {
                    const result = await response.json();
                    if (result.status === 'success' && result.data) {
                        this.config.provider = result.data.provider || 'mock';
                        this.config.model = result.data.model || 'default';
                        this.currentModel = this.config.model;
                    }
                }
            } catch (e) {
                console.error('[LLMChatFloat] Failed to load LLM config:', e);
            }
        },

        async loadAvailableModels() {
            try {
                const response = await fetch('/api/v1/llm/models');
                if (response.ok) {
                    const result = await response.json();
                    if (result.status === 'success' && result.data) {
                        this.availableModels = result.data.models || [];
                        if (this.availableModels.length > 0 && !this.currentModel) {
                            this.currentModel = this.availableModels[0].id;
                        }
                    }
                }
            } catch (e) {
                console.error('[LLMChatFloat] Failed to load available models:', e);
                this.availableModels = [
                    { id: 'qwen-plus', name: '通义千问 Plus', provider: 'qianwen' },
                    { id: 'qwen-turbo', name: '通义千问 Turbo', provider: 'qianwen' },
                    { id: 'deepseek-chat', name: 'DeepSeek Chat', provider: 'deepseek' }
                ];
            }
        },

        async loadPromptTemplates() {
            try {
                const response = await fetch('/api/v1/llm/prompt-templates');
                if (response.ok) {
                    const result = await response.json();
                    if (result.status === 'success' && result.data) {
                        this.promptTemplates = result.data.templates || [];
                    }
                }
            } catch (e) {
                console.error('[LLMChatFloat] Failed to load prompt templates:', e);
                this.promptTemplates = [
                    { id: 'default', name: '默认助手', template: '你是一个有帮助的AI助手。' },
                    { id: 'code', name: '代码助手', template: '你是一个专业的编程助手，擅长解决代码问题和提供最佳实践建议。' },
                    { id: 'translate', name: '翻译助手', template: '你是一个专业的翻译助手，能够准确地在不同语言之间进行翻译。' }
                ];
            }
        },

        getSkillContext() {
            const context = {
                pageUrl: window.location.href,
                pageTitle: document.title,
                skillId: null,
                skillName: null,
                pageData: null,
                timestamp: Date.now()
            };

            if (window.__OODER_SKILL_CONTEXT__) {
                context.skillId = window.__OODER_SKILL_CONTEXT__.skillId;
                context.skillName = window.__OODER_SKILL_CONTEXT__.skillName;
                context.pageData = window.__OODER_SKILL_CONTEXT__.data;
            }

            const pathParts = window.location.pathname.split('/');
            const skillsIndex = pathParts.indexOf('skills');
            if (skillsIndex !== -1 && pathParts.length > skillsIndex + 1) {
                context.skillId = context.skillId || pathParts[skillsIndex + 1];
            }

            return context;
        },

        buildContextString() {
            const ctx = this.getSkillContext();
            const parts = [];
            
            if (ctx.skillName) {
                parts.push(`当前Skill: ${ctx.skillName}`);
            }
            if (ctx.skillId) {
                parts.push(`Skill ID: ${ctx.skillId}`);
            }
            parts.push(`页面: ${ctx.pageTitle}`);
            parts.push(`URL: ${ctx.pageUrl}`);
            
            if (ctx.pageData) {
                try {
                    parts.push(`页面数据: ${JSON.stringify(ctx.pageData).substring(0, 200)}`);
                } catch (e) {}
            }

            return parts.join('\n');
        },

        getHistoryMessages() {
            const history = [];
            const start = Math.max(0, this.messages.length - this.config.maxHistory);
            
            for (let i = start; i < this.messages.length; i++) {
                const msg = this.messages[i];
                history.push({
                    role: msg.role,
                    content: msg.content
                });
            }
            
            return history;
        },

        getContextTokens() {
            let tokens = 0;
            for (const msg of this.messages) {
                tokens += msg.content.length / 2;
            }
            return Math.floor(tokens);
        },

        render() {
            const container = document.createElement('div');
            container.id = 'llm-chat-float-container';
            container.innerHTML = `
                <div class="llm-chat-float-btn" id="llmChatFloatBtn" title="AI助手">
                    <i class="ri-robot-2-line"></i>
                    <span class="llm-chat-float-badge" id="llmChatBadge" style="display: none;">1</span>
                </div>
                <div class="llm-chat-float-window" id="llmChatFloatWindow">
                    <div class="llm-chat-float-header">
                        <div class="llm-chat-float-title">
                            <i class="ri-robot-2-line"></i>
                            <span>${this.config.title}</span>
                        </div>
                        <div class="llm-chat-float-actions">
                            <button class="llm-chat-float-action-btn" id="llmChatModelBtn" title="切换模型">
                                <i class="ri-cpu-line"></i>
                            </button>
                            <button class="llm-chat-float-action-btn" id="llmChatTemplateBtn" title="Prompt模板">
                                <i class="ri-file-text-line"></i>
                            </button>
                            <button class="llm-chat-float-action-btn" id="llmChatContextBtn" title="上下文管理">
                                <i class="ri-stack-line"></i>
                            </button>
                            <button class="llm-chat-float-action-btn" id="llmChatNewBtn" title="新对话">
                                <i class="ri-add-line"></i>
                            </button>
                            <button class="llm-chat-float-action-btn" id="llmChatCloseBtn" title="关闭">
                                <i class="ri-close-line"></i>
                            </button>
                        </div>
                    </div>
                    <div class="llm-chat-float-model-bar" id="llmChatModelBar">
                        <span class="llm-chat-float-model-label">模型:</span>
                        <select class="llm-chat-float-model-select" id="llmChatModelSelect">
                            ${this.availableModels.map(m => 
                                `<option value="${m.id}" ${m.id === this.currentModel ? 'selected' : ''}>${m.name}</option>`
                            ).join('')}
                        </select>
                        <span class="llm-chat-float-context-tokens" id="llmChatContextTokens">0 tokens</span>
                    </div>
                    <div class="llm-chat-float-messages" id="llmChatMessages">
                        <div class="llm-chat-float-message llm-chat-float-message--assistant">
                            <div class="llm-chat-float-avatar">
                                <i class="ri-robot-2-line"></i>
                            </div>
                            <div class="llm-chat-float-content">
                                您好！我是AI助手，有什么可以帮助您的吗？
                            </div>
                        </div>
                    </div>
                    <div class="llm-chat-float-input-area">
                        <div class="llm-chat-float-context-indicator" id="llmChatContextIndicator">
                            <i class="ri-link"></i>
                            <span>已获取页面上下文</span>
                        </div>
                        <div class="llm-chat-float-input-wrapper">
                            <textarea class="llm-chat-float-input" id="llmChatInput" 
                                placeholder="输入您的问题..." rows="1"></textarea>
                            <button class="llm-chat-float-send" id="llmChatSendBtn">
                                <i class="ri-send-plane-line"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="llm-chat-float-modal" id="llmChatModal" style="display: none;">
                    <div class="llm-chat-float-modal-content" id="llmChatModalContent"></div>
                </div>
            `;

            document.body.appendChild(container);
            this.addStyles();
        },

        addStyles() {
            if (document.getElementById('llm-chat-float-styles')) return;

            const style = document.createElement('style');
            style.id = 'llm-chat-float-styles';
            style.textContent = `
                #llm-chat-float-container {
                    position: fixed;
                    z-index: 9999;
                    font-family: var(--nx-font-family, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
                }

                .llm-chat-float-btn {
                    position: fixed;
                    bottom: 24px;
                    right: 24px;
                    width: 56px;
                    height: 56px;
                    border-radius: 50%;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    cursor: pointer;
                    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
                    transition: all 0.3s ease;
                    font-size: 24px;
                }

                .llm-chat-float-btn:hover {
                    transform: scale(1.1);
                    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
                }

                .llm-chat-float-badge {
                    position: absolute;
                    top: -4px;
                    right: -4px;
                    background: #f44336;
                    color: white;
                    font-size: 12px;
                    min-width: 18px;
                    height: 18px;
                    border-radius: 9px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    padding: 0 4px;
                }

                .llm-chat-float-window {
                    position: fixed;
                    bottom: 96px;
                    right: 24px;
                    width: 420px;
                    height: 580px;
                    background: var(--nx-bg-primary, #fff);
                    border-radius: 16px;
                    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
                    display: none;
                    flex-direction: column;
                    overflow: hidden;
                    border: 1px solid var(--nx-border-color, #e0e0e0);
                    color: var(--nx-text-primary, #333);
                }

                .llm-chat-float-window.open {
                    display: flex;
                    animation: slideUp 0.3s ease;
                }

                @keyframes slideUp {
                    from { opacity: 0; transform: translateY(20px); }
                    to { opacity: 1; transform: translateY(0); }
                }

                .llm-chat-float-header {
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    padding: 12px 16px;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                }

                .llm-chat-float-title {
                    display: flex;
                    align-items: center;
                    gap: 8px;
                    font-weight: 600;
                    font-size: 15px;
                }

                .llm-chat-float-actions {
                    display: flex;
                    gap: 6px;
                }

                .llm-chat-float-action-btn {
                    background: rgba(255, 255, 255, 0.2);
                    border: none;
                    color: white;
                    width: 28px;
                    height: 28px;
                    border-radius: 6px;
                    cursor: pointer;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    transition: background 0.2s;
                    font-size: 14px;
                }

                .llm-chat-float-action-btn:hover {
                    background: rgba(255, 255, 255, 0.3);
                }

                .llm-chat-float-model-bar {
                    display: flex;
                    align-items: center;
                    gap: 8px;
                    padding: 8px 16px;
                    background: var(--nx-bg-secondary, #f5f5f5);
                    border-bottom: 1px solid var(--nx-border-color, #e0e0e0);
                }

                .llm-chat-float-model-label {
                    font-size: 12px;
                    color: var(--nx-text-secondary, #666);
                }

                .llm-chat-float-model-select {
                    flex: 1;
                    padding: 4px 8px;
                    border: 1px solid var(--nx-border-color, #e0e0e0);
                    border-radius: 4px;
                    font-size: 12px;
                    background: var(--nx-bg-primary, #fff);
                    color: var(--nx-text-primary, #333);
                    cursor: pointer;
                }

                .llm-chat-float-context-tokens {
                    font-size: 11px;
                    color: var(--nx-text-tertiary, #999);
                    padding: 2px 6px;
                    background: var(--nx-bg-tertiary, #eee);
                    border-radius: 4px;
                }

                .llm-chat-float-messages {
                    flex: 1;
                    overflow-y: auto;
                    padding: 16px;
                    display: flex;
                    flex-direction: column;
                    gap: 12px;
                    background: var(--nx-bg-secondary, #f9f9f9);
                }

                .llm-chat-float-message {
                    display: flex;
                    gap: 10px;
                    max-width: 90%;
                }

                .llm-chat-float-message--user {
                    flex-direction: row-reverse;
                    margin-left: auto;
                }

                .llm-chat-float-avatar {
                    width: 32px;
                    height: 32px;
                    border-radius: 50%;
                    background: var(--nx-bg-tertiary, #e0e0e0);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    flex-shrink: 0;
                    font-size: 16px;
                    color: var(--nx-text-secondary, #666);
                }

                .llm-chat-float-message--user .llm-chat-float-avatar {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                }

                .llm-chat-float-content {
                    padding: 10px 14px;
                    border-radius: 12px;
                    font-size: 14px;
                    line-height: 1.6;
                    background: var(--nx-bg-elevated, #fff);
                    border: 1px solid var(--nx-border-color, #e0e0e0);
                    color: var(--nx-text-primary, #333);
                    word-break: break-word;
                }

                .llm-chat-float-message--user .llm-chat-float-content {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    border: none;
                }

                .llm-chat-float-content pre {
                    background: rgba(0,0,0,0.05);
                    padding: 8px 12px;
                    border-radius: 6px;
                    overflow-x: auto;
                    margin: 8px 0;
                    font-size: 13px;
                }

                .llm-chat-float-content code {
                    font-family: 'Fira Code', 'Consolas', monospace;
                    font-size: 13px;
                }

                .llm-chat-float-script-block {
                    margin: 8px 0;
                    border: 1px solid #667eea;
                    border-radius: 8px;
                    overflow: hidden;
                }

                .llm-chat-float-script-header {
                    background: rgba(102, 126, 234, 0.1);
                    padding: 6px 10px;
                    font-size: 12px;
                    color: #667eea;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }

                .llm-chat-float-script-run {
                    background: #667eea;
                    color: white;
                    border: none;
                    padding: 4px 12px;
                    border-radius: 4px;
                    font-size: 12px;
                    cursor: pointer;
                    display: flex;
                    align-items: center;
                    gap: 4px;
                }

                .llm-chat-float-script-run:hover {
                    background: #5a6fd6;
                }

                .llm-chat-float-script-run.executed {
                    background: #4caf50;
                }

                .llm-chat-float-script-code {
                    padding: 10px;
                    background: #1e1e2e;
                    color: #cdd6f4;
                    font-family: 'Fira Code', 'Consolas', monospace;
                    font-size: 12px;
                    overflow-x: auto;
                    white-space: pre-wrap;
                }

                .llm-chat-float-input-area {
                    padding: 12px 16px;
                    border-top: 1px solid var(--nx-border-color, #e0e0e0);
                    background: var(--nx-bg-primary, #fff);
                }

                .llm-chat-float-context-indicator {
                    display: flex;
                    align-items: center;
                    gap: 6px;
                    font-size: 12px;
                    color: #667eea;
                    margin-bottom: 8px;
                    padding: 4px 8px;
                    background: rgba(102, 126, 234, 0.1);
                    border-radius: 4px;
                }

                .llm-chat-float-input-wrapper {
                    display: flex;
                    gap: 8px;
                    align-items: flex-end;
                }

                .llm-chat-float-input {
                    flex: 1;
                    border: 1px solid var(--nx-border-color, #e0e0e0);
                    border-radius: 20px;
                    padding: 10px 16px;
                    font-size: 14px;
                    resize: none;
                    max-height: 100px;
                    outline: none;
                    font-family: inherit;
                    background: var(--nx-bg-secondary, #f9f9f9);
                    color: var(--nx-text-primary, #333);
                }

                .llm-chat-float-input:focus {
                    border-color: #667eea;
                    background: var(--nx-bg-primary, #fff);
                }

                .llm-chat-float-input::placeholder {
                    color: var(--nx-text-tertiary, #999);
                }

                .llm-chat-float-send {
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    border: none;
                    cursor: pointer;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    transition: all 0.2s;
                }

                .llm-chat-float-send:hover {
                    transform: scale(1.1);
                }

                .llm-chat-float-send:disabled {
                    opacity: 0.5;
                    cursor: not-allowed;
                    transform: none;
                }

                .llm-chat-float-typing {
                    display: flex;
                    gap: 4px;
                    padding: 8px 0;
                }

                .llm-chat-float-typing span {
                    width: 8px;
                    height: 8px;
                    background: #667eea;
                    border-radius: 50%;
                    animation: typing 1.4s infinite ease-in-out both;
                }

                .llm-chat-float-typing span:nth-child(1) { animation-delay: -0.32s; }
                .llm-chat-float-typing span:nth-child(2) { animation-delay: -0.16s; }

                @keyframes typing {
                    0%, 80%, 100% { transform: scale(0); }
                    40% { transform: scale(1); }
                }

                .llm-chat-float-result {
                    margin-top: 8px;
                    padding: 8px 12px;
                    background: rgba(76, 175, 80, 0.1);
                    border-left: 3px solid #4caf50;
                    border-radius: 4px;
                    font-size: 12px;
                    color: #2e7d32;
                }

                .llm-chat-float-result.error {
                    background: rgba(244, 67, 54, 0.1);
                    border-color: #f44336;
                    color: #c62828;
                }

                .llm-chat-float-modal {
                    position: fixed;
                    top: 0;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    background: rgba(0, 0, 0, 0.5);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    z-index: 10000;
                }

                .llm-chat-float-modal-content {
                    background: var(--nx-bg-primary, #fff);
                    border-radius: 12px;
                    padding: 20px;
                    max-width: 400px;
                    width: 90%;
                    max-height: 80vh;
                    overflow-y: auto;
                }

                .llm-chat-float-modal-title {
                    font-size: 16px;
                    font-weight: 600;
                    margin-bottom: 16px;
                    color: var(--nx-text-primary, #333);
                }

                .llm-chat-float-modal-close {
                    float: right;
                    background: none;
                    border: none;
                    font-size: 20px;
                    cursor: pointer;
                    color: var(--nx-text-secondary, #666);
                }

                .llm-chat-float-template-item,
                .llm-chat-float-context-item {
                    padding: 12px;
                    border: 1px solid var(--nx-border-color, #e0e0e0);
                    border-radius: 8px;
                    margin-bottom: 8px;
                    cursor: pointer;
                    transition: all 0.2s;
                }

                .llm-chat-float-template-item:hover,
                .llm-chat-float-context-item:hover {
                    border-color: #667eea;
                    background: rgba(102, 126, 234, 0.05);
                }

                .llm-chat-float-template-item.active {
                    border-color: #667eea;
                    background: rgba(102, 126, 234, 0.1);
                }

                .llm-chat-float-template-name {
                    font-weight: 500;
                    margin-bottom: 4px;
                    color: var(--nx-text-primary, #333);
                }

                .llm-chat-float-template-preview {
                    font-size: 12px;
                    color: var(--nx-text-secondary, #666);
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }

                .llm-chat-float-context-stats {
                    display: grid;
                    grid-template-columns: repeat(2, 1fr);
                    gap: 12px;
                    margin-bottom: 16px;
                }

                .llm-chat-float-context-stat {
                    padding: 12px;
                    background: var(--nx-bg-secondary, #f5f5f5);
                    border-radius: 8px;
                    text-align: center;
                }

                .llm-chat-float-context-stat-value {
                    font-size: 20px;
                    font-weight: 600;
                    color: #667eea;
                }

                .llm-chat-float-context-stat-label {
                    font-size: 12px;
                    color: var(--nx-text-secondary, #666);
                }

                .llm-chat-float-context-actions {
                    display: flex;
                    gap: 8px;
                    margin-top: 16px;
                }

                .llm-chat-float-context-action {
                    flex: 1;
                    padding: 10px;
                    border: 1px solid var(--nx-border-color, #e0e0e0);
                    border-radius: 8px;
                    background: var(--nx-bg-primary, #fff);
                    cursor: pointer;
                    font-size: 13px;
                    transition: all 0.2s;
                }

                .llm-chat-float-context-action:hover {
                    border-color: #667eea;
                    background: rgba(102, 126, 234, 0.05);
                }

                .llm-chat-float-context-action.danger:hover {
                    border-color: #f44336;
                    background: rgba(244, 67, 54, 0.05);
                }

                /* 深色主题 */
                [data-theme="dark"] .llm-chat-float-window,
                .nx-theme-dark .llm-chat-float-window {
                    background: var(--nx-bg-primary, #1a1a2e) !important;
                    border-color: var(--nx-border-color, #2a2a3e) !important;
                }

                [data-theme="dark"] .llm-chat-float-model-bar,
                .nx-theme-dark .llm-chat-float-model-bar {
                    background: var(--nx-bg-secondary, #16162a) !important;
                    border-color: var(--nx-border-color, #2a2a3e) !important;
                }

                [data-theme="dark"] .llm-chat-float-messages,
                .nx-theme-dark .llm-chat-float-messages {
                    background: var(--nx-bg-secondary, #16162a) !important;
                }

                [data-theme="dark"] .llm-chat-float-content,
                .nx-theme-dark .llm-chat-float-content {
                    background: var(--nx-bg-elevated, #1e1e32) !important;
                    border-color: var(--nx-border-color, #2a2a3e) !important;
                    color: var(--nx-text-primary, #e0e0e0) !important;
                }

                [data-theme="dark"] .llm-chat-float-input-area,
                .nx-theme-dark .llm-chat-float-input-area {
                    background: var(--nx-bg-primary, #1a1a2e) !important;
                    border-color: var(--nx-border-color, #2a2a3e) !important;
                }

                [data-theme="dark"] .llm-chat-float-input,
                .nx-theme-dark .llm-chat-float-input {
                    background: var(--nx-bg-secondary, #16162a) !important;
                    border-color: var(--nx-border-color, #2a2a3e) !important;
                    color: var(--nx-text-primary, #e0e0e0) !important;
                }

                [data-theme="dark"] .llm-chat-float-input::placeholder,
                .nx-theme-dark .llm-chat-float-input::placeholder {
                    color: var(--nx-text-tertiary, #666) !important;
                }

                [data-theme="dark"] .llm-chat-float-avatar,
                .nx-theme-dark .llm-chat-float-avatar {
                    background: var(--nx-bg-tertiary, #2a2a3e) !important;
                    color: var(--nx-text-secondary, #a0a0a0) !important;
                }

                [data-theme="dark"] .llm-chat-float-content pre,
                .nx-theme-dark .llm-chat-float-content pre {
                    background: rgba(0,0,0,0.3);
                }

                [data-theme="dark"] .llm-chat-float-modal-content,
                .nx-theme-dark .llm-chat-float-modal-content {
                    background: var(--nx-bg-primary, #1a1a2e) !important;
                }

                [data-theme="dark"] .llm-chat-float-model-select,
                .nx-theme-dark .llm-chat-float-model-select {
                    background: var(--nx-bg-primary, #1a1a2e) !important;
                    border-color: var(--nx-border-color, #2a2a3e) !important;
                    color: var(--nx-text-primary, #e0e0e0) !important;
                }
            `;

            document.head.appendChild(style);
        },

        bindEvents() {
            const btn = document.getElementById('llmChatFloatBtn');
            const closeBtn = document.getElementById('llmChatCloseBtn');
            const sendBtn = document.getElementById('llmChatSendBtn');
            const input = document.getElementById('llmChatInput');
            const contextBtn = document.getElementById('llmChatContextBtn');
            const newBtn = document.getElementById('llmChatNewBtn');
            const modelBtn = document.getElementById('llmChatModelBtn');
            const templateBtn = document.getElementById('llmChatTemplateBtn');
            const modelSelect = document.getElementById('llmChatModelSelect');

            btn.addEventListener('click', () => this.toggle());
            closeBtn.addEventListener('click', () => this.close());
            sendBtn.addEventListener('click', () => this.sendMessage());
            
            input.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    this.sendMessage();
                }
            });

            contextBtn.addEventListener('click', () => this.showContextModal());
            newBtn.addEventListener('click', () => this.clearChat());
            modelBtn.addEventListener('click', () => this.showModelModal());
            templateBtn.addEventListener('click', () => this.showTemplateModal());
            
            modelSelect.addEventListener('change', (e) => {
                this.currentModel = e.target.value;
                this.saveConfig();
            });
        },

        toggle() {
            this.isOpen = !this.isOpen;
            const window = document.getElementById('llmChatFloatWindow');
            window.classList.toggle('open', this.isOpen);
            
            if (this.isOpen) {
                document.getElementById('llmChatInput').focus();
                this.updateContextTokens();
            }
        },

        close() {
            this.isOpen = false;
            document.getElementById('llmChatFloatWindow').classList.remove('open');
        },

        closeModal() {
            const modal = document.getElementById('llmChatModal');
            modal.style.display = 'none';
        },

        showContextModal() {
            const modal = document.getElementById('llmChatModal');
            const content = document.getElementById('llmChatModalContent');
            
            const ctx = this.getSkillContext();
            const tokens = this.getContextTokens();
            
            content.innerHTML = `
                <button class="llm-chat-float-modal-close" onclick="LLMChatFloat.closeModal()">
                    <i class="ri-close-line"></i>
                </button>
                <div class="llm-chat-float-modal-title">上下文管理</div>
                
                <div class="llm-chat-float-context-stats">
                    <div class="llm-chat-float-context-stat">
                        <div class="llm-chat-float-context-stat-value">${this.messages.length}</div>
                        <div class="llm-chat-float-context-stat-label">消息数</div>
                    </div>
                    <div class="llm-chat-float-context-stat">
                        <div class="llm-chat-float-context-stat-value">${tokens}</div>
                        <div class="llm-chat-float-context-stat-label">Tokens</div>
                    </div>
                    <div class="llm-chat-float-context-stat">
                        <div class="llm-chat-float-context-stat-value">${this.config.maxContextLength}</div>
                        <div class="llm-chat-float-context-stat-label">最大长度</div>
                    </div>
                    <div class="llm-chat-float-context-stat">
                        <div class="llm-chat-float-context-stat-value">${Math.round(tokens / this.config.maxContextLength * 100)}%</div>
                        <div class="llm-chat-float-context-stat-label">使用率</div>
                    </div>
                </div>
                
                <div class="llm-chat-float-modal-title" style="font-size: 14px; margin-top: 16px;">当前上下文</div>
                <div style="font-size: 12px; color: var(--nx-text-secondary, #666); padding: 8px; background: var(--nx-bg-secondary, #f5f5f5); border-radius: 8px; margin-bottom: 16px;">
                    ${this.buildContextString().replace(/\n/g, '<br>')}
                </div>
                
                <div class="llm-chat-float-context-actions">
                    <button class="llm-chat-float-context-action" onclick="LLMChatFloat.exportContext()">
                        <i class="ri-download-line"></i> 导出对话
                    </button>
                    <button class="llm-chat-float-context-action" onclick="LLMChatFloat.trimContext()">
                        <i class="ri-scissors-cut-line"></i> 裁剪上下文
                    </button>
                    <button class="llm-chat-float-context-action danger" onclick="LLMChatFloat.clearContext()">
                        <i class="ri-delete-bin-line"></i> 清空上下文
                    </button>
                </div>
            `;
            
            modal.style.display = 'flex';
        },

        showModelModal() {
            const modal = document.getElementById('llmChatModal');
            const content = document.getElementById('llmChatModalContent');
            
            content.innerHTML = `
                <button class="llm-chat-float-modal-close" onclick="LLMChatFloat.closeModal()">
                    <i class="ri-close-line"></i>
                </button>
                <div class="llm-chat-float-modal-title">选择模型</div>
                
                ${this.availableModels.map(m => `
                    <div class="llm-chat-float-template-item ${m.id === this.currentModel ? 'active' : ''}" 
                         onclick="LLMChatFloat.selectModel('${m.id}')">
                        <div class="llm-chat-float-template-name">${m.name}</div>
                        <div class="llm-chat-float-template-preview">Provider: ${m.provider} | ID: ${m.id}</div>
                    </div>
                `).join('')}
                
                <div style="margin-top: 16px; padding: 12px; background: var(--nx-bg-secondary, #f5f5f5); border-radius: 8px;">
                    <div style="font-size: 12px; color: var(--nx-text-secondary, #666); margin-bottom: 8px;">高级设置</div>
                    <div style="display: flex; align-items: center; gap: 8px;">
                        <label style="font-size: 12px;">Temperature:</label>
                        <input type="range" min="0" max="1" step="0.1" value="${this.config.temperature}" 
                               onchange="LLMChatFloat.setTemperature(this.value)"
                               style="flex: 1;">
                        <span style="font-size: 12px;">${this.config.temperature}</span>
                    </div>
                </div>
            `;
            
            modal.style.display = 'flex';
        },

        showTemplateModal() {
            const modal = document.getElementById('llmChatModal');
            const content = document.getElementById('llmChatModalContent');
            
            content.innerHTML = `
                <button class="llm-chat-float-modal-close" onclick="LLMChatFloat.closeModal()">
                    <i class="ri-close-line"></i>
                </button>
                <div class="llm-chat-float-modal-title">Prompt模板</div>
                
                ${this.promptTemplates.map(t => `
                    <div class="llm-chat-float-template-item" onclick="LLMChatFloat.applyTemplate('${t.id}')">
                        <div class="llm-chat-float-template-name">${t.name}</div>
                        <div class="llm-chat-float-template-preview">${t.template.substring(0, 50)}...</div>
                    </div>
                `).join('')}
                
                <div style="margin-top: 16px;">
                    <button class="llm-chat-float-context-action" onclick="LLMChatFloat.showCustomTemplate()">
                        <i class="ri-add-line"></i> 自定义模板
                    </button>
                </div>
            `;
            
            modal.style.display = 'flex';
        },

        selectModel(modelId) {
            this.currentModel = modelId;
            document.getElementById('llmChatModelSelect').value = modelId;
            this.saveConfig();
            this.closeModal();
        },

        setTemperature(value) {
            this.config.temperature = parseFloat(value);
            this.saveConfig();
        },

        applyTemplate(templateId) {
            const template = this.promptTemplates.find(t => t.id === templateId);
            if (template) {
                this.addMessage('system', `已应用模板: ${template.name}`);
                this.closeModal();
            }
        },

        showCustomTemplate() {
            const content = document.getElementById('llmChatModalContent');
            content.innerHTML = `
                <button class="llm-chat-float-modal-close" onclick="LLMChatFloat.showTemplateModal()">
                    <i class="ri-arrow-left-line"></i>
                </button>
                <div class="llm-chat-float-modal-title">自定义Prompt模板</div>
                
                <div style="margin-bottom: 12px;">
                    <label style="font-size: 12px; color: var(--nx-text-secondary, #666);">模板名称</label>
                    <input type="text" id="customTemplateName" placeholder="输入模板名称"
                           style="width: 100%; padding: 8px; border: 1px solid var(--nx-border-color, #e0e0e0); border-radius: 6px; margin-top: 4px;">
                </div>
                
                <div style="margin-bottom: 12px;">
                    <label style="font-size: 12px; color: var(--nx-text-secondary, #666);">Prompt内容</label>
                    <textarea id="customTemplateContent" placeholder="输入Prompt内容" rows="6"
                              style="width: 100%; padding: 8px; border: 1px solid var(--nx-border-color, #e0e0e0); border-radius: 6px; margin-top: 4px; resize: vertical;"></textarea>
                </div>
                
                <button class="llm-chat-float-context-action" onclick="LLMChatFloat.saveCustomTemplate()">
                    <i class="ri-save-line"></i> 保存模板
                </button>
            `;
        },

        saveCustomTemplate() {
            const name = document.getElementById('customTemplateName').value.trim();
            const content = document.getElementById('customTemplateContent').value.trim();
            
            if (name && content) {
                const template = {
                    id: 'custom-' + Date.now(),
                    name: name,
                    template: content
                };
                this.promptTemplates.push(template);
                this.closeModal();
            }
        },

        exportContext() {
            const data = {
                sessionId: this.sessionId,
                messages: this.messages,
                context: this.getSkillContext(),
                exportedAt: new Date().toISOString()
            };
            
            const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `chat-export-${this.sessionId}.json`;
            a.click();
            URL.revokeObjectURL(url);
        },

        trimContext() {
            if (this.messages.length > 2) {
                this.messages = this.messages.slice(-2);
                this.reRenderMessages();
                this.updateContextTokens();
            }
        },

        clearContext() {
            this.messages = [];
            this.reRenderMessages();
            this.updateContextTokens();
            this.closeModal();
        },

        reRenderMessages() {
            const container = document.getElementById('llmChatMessages');
            container.innerHTML = `
                <div class="llm-chat-float-message llm-chat-float-message--assistant">
                    <div class="llm-chat-float-avatar">
                        <i class="ri-robot-2-line"></i>
                    </div>
                    <div class="llm-chat-float-content">
                        对话已清空，有什么可以帮助您的吗？
                    </div>
                </div>
            `;
            
            for (const msg of this.messages) {
                if (msg.role !== 'system') {
                    this.addMessageToDOM(msg.role, msg.content);
                }
            }
        },

        addMessageToDOM(role, content) {
            const container = document.getElementById('llmChatMessages');
            const isUser = role === 'user';

            const messageHtml = `
                <div class="llm-chat-float-message llm-chat-float-message--${role}">
                    <div class="llm-chat-float-avatar">
                        <i class="ri-${isUser ? 'user' : 'robot-2'}-line"></i>
                    </div>
                    <div class="llm-chat-float-content">${this.escapeHtml(content)}</div>
                </div>
            `;

            container.insertAdjacentHTML('beforeend', messageHtml);
            container.scrollTop = container.scrollHeight;
        },

        updateContextTokens() {
            const tokens = this.getContextTokens();
            const el = document.getElementById('llmChatContextTokens');
            if (el) {
                el.textContent = `${tokens} tokens`;
                el.style.color = tokens > this.config.maxContextLength * 0.8 ? '#f44336' : '';
            }
        },

        async sendMessage() {
            if (this.isStreaming) return;
            
            const input = document.getElementById('llmChatInput');
            const content = input.value.trim();
            
            if (!content) return;

            this.addMessage('user', content);
            this.messages.push({ role: 'user', content });
            input.value = '';
            
            this.updateContextTokens();

            this.isStreaming = true;
            this.updateSendButton(true);

            const messageId = 'msg-' + Date.now();
            this.addStreamingMessage(messageId);

            try {
                const skillContext = this.buildContextString();
                const history = this.getHistoryMessages();

                const response = await fetch('/api/v1/llm/chat', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ 
                        message: content,
                        providerId: this.config.provider,
                        model: this.currentModel,
                        sessionId: this.sessionId,
                        skillContext: skillContext,
                        history: history,
                        temperature: this.config.temperature,
                        stream: this.config.streamEnabled
                    })
                });

                const result = await response.json();

                if (result.status === 'success' && result.data && result.data.content) {
                    const responseContent = result.data.content;
                    this.messages.push({ role: 'assistant', content: responseContent });
                    await this.renderMessage(messageId, responseContent);
                    this.updateContextTokens();
                } else if (result.message) {
                    this.updateStreamingMessage(messageId, result.message);
                } else {
                    this.updateStreamingMessage(messageId, '抱歉，未收到有效回复。');
                }

            } catch (e) {
                console.error('[LLMChatFloat] Error:', e);
                this.updateStreamingMessage(messageId, '抱歉，网络连接失败，请稍后重试。');
            } finally {
                this.isStreaming = false;
                this.updateSendButton(false);
            }
        },

        async renderMessage(messageId, text) {
            const messageEl = document.getElementById(messageId);
            if (!messageEl) return;

            const contentEl = messageEl.querySelector('.llm-chat-float-content');
            if (!contentEl) return;

            const processedHtml = this.processContent(text);
            contentEl.innerHTML = processedHtml;

            this.bindScriptButtons(contentEl);

            const container = document.getElementById('llmChatMessages');
            container.scrollTop = container.scrollHeight;
        },

        processContent(text) {
            let html = this.escapeHtml(text);
            
            html = html.replace(/```script\n([\s\S]*?)```/g, (match, code) => {
                const scriptId = 'script-' + Date.now() + '-' + Math.random().toString(36).substr(2, 6);
                return `<div class="llm-chat-float-script-block" data-script-id="${scriptId}">
                    <div class="llm-chat-float-script-header">
                        <span><i class="ri-code-line"></i> 可执行脚本</span>
                        <button class="llm-chat-float-script-run" data-script="${this.escapeAttr(code)}">
                            <i class="ri-play-line"></i> 执行
                        </button>
                    </div>
                    <pre class="llm-chat-float-script-code">${this.escapeHtml(code)}</pre>
                </div>`;
            });

            html = html.replace(/```(\w*)\n([\s\S]*?)```/g, (match, lang, code) => {
                if (lang === 'script') return match;
                return `<pre><code class="language-${lang}">${this.escapeHtml(code)}</code></pre>`;
            });

            html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
            html = html.replace(/\*(.*?)\*/g, '<em>$1</em>');
            html = html.replace(/^### (.*$)/gm, '<h4>$1</h4>');
            html = html.replace(/^## (.*$)/gm, '<h3>$1</h3>');
            html = html.replace(/^# (.*$)/gm, '<h2>$1</h2>');
            html = html.replace(/^- (.*$)/gm, '<li>$1</li>');
            html = html.replace(/\n/g, '<br>');

            return html;
        },

        bindScriptButtons(container) {
            const buttons = container.querySelectorAll('.llm-chat-float-script-run');
            buttons.forEach(btn => {
                btn.addEventListener('click', async () => {
                    const script = btn.getAttribute('data-script');
                    const result = await this.executeScript(script);
                    
                    const scriptBlock = btn.closest('.llm-chat-float-script-block');
                    let resultEl = scriptBlock.querySelector('.llm-chat-float-result');
                    
                    if (!resultEl) {
                        resultEl = document.createElement('div');
                        resultEl.className = 'llm-chat-float-result';
                        scriptBlock.appendChild(resultEl);
                    }
                    
                    if (result.success) {
                        resultEl.className = 'llm-chat-float-result';
                        resultEl.innerHTML = `<i class="ri-check-line"></i> 执行成功: ${this.formatResult(result.data)}`;
                        btn.classList.add('executed');
                        btn.innerHTML = '<i class="ri-check-line"></i> 已执行';
                    } else {
                        resultEl.className = 'llm-chat-float-result error';
                        resultEl.innerHTML = `<i class="ri-error-warning-line"></i> 执行失败: ${result.error}`;
                    }
                });
            });
        },

        async executeScript(script) {
            try {
                const AsyncFunction = Object.getPrototypeOf(async function(){}).constructor;
                const fn = new AsyncFunction(script);
                const result = await fn();
                return { success: true, data: result };
            } catch (e) {
                console.error('[LLMChatFloat] Script execution error:', e);
                return { success: false, error: e.message };
            }
        },

        formatResult(data) {
            if (data === undefined) return 'undefined';
            if (data === null) return 'null';
            if (typeof data === 'object') {
                try {
                    return JSON.stringify(data).substring(0, 100);
                } catch (e) {
                    return String(data);
                }
            }
            return String(data);
        },

        addMessage(role, content) {
            const container = document.getElementById('llmChatMessages');
            const isUser = role === 'user';

            const messageHtml = `
                <div class="llm-chat-float-message llm-chat-float-message--${role}">
                    <div class="llm-chat-float-avatar">
                        <i class="ri-${isUser ? 'user' : 'robot-2'}-line"></i>
                    </div>
                    <div class="llm-chat-float-content">${this.escapeHtml(content)}</div>
                </div>
            `;

            container.insertAdjacentHTML('beforeend', messageHtml);
            container.scrollTop = container.scrollHeight;
        },

        addStreamingMessage(messageId) {
            const container = document.getElementById('llmChatMessages');

            const messageHtml = `
                <div class="llm-chat-float-message llm-chat-float-message--assistant" id="${messageId}">
                    <div class="llm-chat-float-avatar">
                        <i class="ri-robot-2-line"></i>
                    </div>
                    <div class="llm-chat-float-content">
                        <div class="llm-chat-float-typing">
                            <span></span>
                            <span></span>
                            <span></span>
                        </div>
                    </div>
                </div>
            `;

            container.insertAdjacentHTML('beforeend', messageHtml);
            container.scrollTop = container.scrollHeight;
        },

        updateStreamingMessage(messageId, content) {
            const messageEl = document.getElementById(messageId);
            if (!messageEl) return;

            const contentEl = messageEl.querySelector('.llm-chat-float-content');
            if (contentEl) {
                contentEl.innerHTML = this.escapeHtml(content);
            }

            const container = document.getElementById('llmChatMessages');
            container.scrollTop = container.scrollHeight;
        },

        updateSendButton(disabled) {
            const sendBtn = document.getElementById('llmChatSendBtn');
            sendBtn.disabled = disabled;
        },

        escapeHtml(text) {
            const div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        },

        escapeAttr(text) {
            return text.replace(/"/g, '&quot;').replace(/'/g, '&#39;');
        },

        clearChat() {
            this.messages = [];
            this.sessionId = this.generateSessionId();
            document.getElementById('llmChatMessages').innerHTML = `
                <div class="llm-chat-float-message llm-chat-float-message--assistant">
                    <div class="llm-chat-float-avatar">
                        <i class="ri-robot-2-line"></i>
                    </div>
                    <div class="llm-chat-float-content">
                        对话已清空，有什么可以帮助您的吗？
                    </div>
                </div>
            `;
            this.updateContextTokens();
        },

        enable() {
            this.config.enabled = true;
            this.saveConfig();
            if (!this.initialized) {
                this.init();
            } else {
                document.getElementById('llm-chat-float-container').style.display = '';
            }
        },

        disable() {
            this.config.enabled = false;
            this.saveConfig();
            const container = document.getElementById('llm-chat-float-container');
            if (container) {
                container.style.display = 'none';
            }
        },

        saveConfig() {
            localStorage.setItem('llm-chat-float-config', JSON.stringify(this.config));
        }
    };

    window.LLMChatFloat = LLMChatFloat;

    window.__OODER_SKILL_CONTEXT__ = window.__OODER_SKILL_CONTEXT__ || {};

    document.addEventListener('DOMContentLoaded', () => {
        LLMChatFloat.init();
    });

})();
