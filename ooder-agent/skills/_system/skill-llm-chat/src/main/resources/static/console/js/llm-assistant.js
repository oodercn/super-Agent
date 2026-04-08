(function(global) {
    'use strict';

    var LlmAssistant = {
        isOpen: false,
        messages: [],
        currentModel: null,
        currentProvider: null,
        providers: {},
        modelsByProvider: {},
        isStreaming: false,
        contextData: null,
        settings: {
            systemPrompt: '',
            temperature: 0.7,
            maxTokens: 2048,
            streamEnabled: true,
            timeout: 120000
        },
        
        abortController: null,
        streamTimeoutId: null,

        init: function() {
            if (document.getElementById('llm-assistant-container')) {
                return;
            }
            this.createContainer();
            this.loadSettings();
            this.loadProviders();
        },

        createContainer: function() {
            var container = document.createElement('div');
            container.id = 'llm-assistant-container';
            container.innerHTML = this.getTemplate();
            document.body.appendChild(container);
            
            this.bindEvents();
            this.addStyles();
        },

        getTemplate: function() {
            return '\
                <div class="llm-assistant-btn" id="llm-assistant-toggle" title="AI 助手">\
                    <i class="ri-robot-line"></i>\
                </div>\
                \
                <div class="llm-assistant-panel" id="llm-assistant-panel">\
                    <div class="llm-panel-header">\
                        <div class="llm-panel-title">\
                            <i class="ri-robot-line"></i>\
                            <span>AI 智能助手</span>\
                        </div>\
                        <div class="llm-panel-controls">\
                            <select class="llm-provider-select" id="llm-provider-select" title="选择提供商">\
                                <option value="">加载中...</option>\
                            </select>\
                            <select class="llm-model-select" id="llm-model-select" title="选择模型">\
                                <option value="">加载中...</option>\
                            </select>\
                            <button class="llm-btn llm-btn--icon" id="llm-settings-btn" title="设置">\
                                <i class="ri-settings-3-line"></i>\
                            </button>\
                            <button class="llm-btn llm-btn--icon" id="llm-clear-btn" title="清空对话">\
                                <i class="ri-delete-bin-line"></i>\
                            </button>\
                            <button class="llm-btn llm-btn--icon" id="llm-close-btn" title="关闭">\
                                <i class="ri-close-line"></i>\
                            </button>\
                        </div>\
                    </div>\
                    \
                    <div class="llm-panel-messages" id="llm-messages">\
                        <div class="llm-welcome">\
                            <div class="llm-welcome-icon">\
                                <i class="ri-robot-smile-line"></i>\
                            </div>\
                            <h3>AI 智能助手</h3>\
                            <p>我可以帮助您配置场景、分析数据、编写代码等</p>\
                            <div class="llm-quick-actions">\
                                <button class="llm-quick-btn" data-action="config">\
                                    <i class="ri-settings-4-line"></i> 自动配置\
                                </button>\
                                <button class="llm-quick-btn" data-action="analyze">\
                                    <i class="ri-bar-chart-line"></i> 分析数据\
                                </button>\
                                <button class="llm-quick-btn" data-action="code">\
                                    <i class="ri-code-line"></i> 生成代码\
                                </button>\
                                <button class="llm-quick-btn" data-action="help">\
                                    <i class="ri-question-line"></i> 使用帮助\
                                </button>\
                            </div>\
                        </div>\
                    </div>\
                    \
                    <div class="llm-panel-input">\
                        <div class="llm-context-badge" id="llm-context-badge" style="display: none;">\
                            <span id="llm-context-text"></span>\
                            <button class="llm-btn llm-btn--icon llm-btn--xs" id="llm-clear-context">\
                                <i class="ri-close-line"></i>\
                            </button>\
                        </div>\
                        <div class="llm-input-wrapper">\
                            <textarea \
                                id="llm-input" \
                                placeholder="输入问题，按 Enter 发送..."\
                                rows="1"\
                            ></textarea>\
                            <button class="llm-btn llm-btn--secondary" id="llm-cancel-btn" style="display: none;" title="取消">\
                                <i class="ri-stop-circle-line"></i>\
                            </button>\
                            <button class="llm-btn llm-btn--primary" id="llm-send-btn" disabled>\
                                <i class="ri-send-plane-fill"></i>\
                            </button>\
                        </div>\
                        <div class="llm-input-hint">\
                            <span id="llm-char-count">0</span> 字符 | \
                            <span id="llm-status">就绪</span>\
                        </div>\
                    </div>\
                </div>\
                \
                <div class="llm-settings-modal" id="llm-settings-modal">\
                    <div class="llm-modal-content">\
                        <div class="llm-modal-header">\
                            <h3>助手设置</h3>\
                            <button class="llm-btn llm-btn--icon" id="llm-close-settings">\
                                <i class="ri-close-line"></i>\
                            </button>\
                        </div>\
                        <div class="llm-modal-body">\
                            <div class="llm-form-group">\
                                <label>系统提示词</label>\
                                <textarea id="llm-system-prompt" rows="3" placeholder="设置 AI 的角色和行为..."></textarea>\
                            </div>\
                            <div class="llm-form-group">\
                                <label>温度 (Temperature): <span id="llm-temp-value">0.7</span></label>\
                                <input type="range" id="llm-temperature" min="0" max="1" step="0.1" value="0.7">\
                            </div>\
                            <div class="llm-form-group">\
                                <label>最大 Token 数</label>\
                                <input type="number" id="llm-max-tokens" value="2048" min="100" max="8192">\
                            </div>\
                            <div class="llm-form-group">\
                                <label class="llm-checkbox">\
                                    <input type="checkbox" id="llm-stream-enabled" checked>\
                                    <span>启用流式输出</span>\
                                </label>\
                            </div>\
                            <div class="llm-form-group">\
                                <label>请求超时 (秒)</label>\
                                <input type="number" id="llm-timeout" value="60" min="10" max="300">\
                            </div>\
                        </div>\
                        <div class="llm-modal-footer">\
                            <button class="llm-btn llm-btn--secondary" id="llm-reset-settings">重置</button>\
                            <button class="llm-btn llm-btn--primary" id="llm-save-settings">保存</button>\
                        </div>\
                    </div>\
                </div>';
        },

        addStyles: function() {
            if (document.getElementById('llm-assistant-styles')) {
                return;
            }
            var style = document.createElement('style');
            style.id = 'llm-assistant-styles';
            style.textContent = this.getStyles();
            document.head.appendChild(style);
        },

        getStyles: function() {
            return '\
.llm-assistant-btn {\
    position: fixed;\
    bottom: 24px;\
    right: 24px;\
    width: 56px;\
    height: 56px;\
    border-radius: 50%;\
    background: var(--nx-primary, #3b82f6);\
    color: white;\
    border: none;\
    cursor: pointer;\
    display: flex;\
    align-items: center;\
    justify-content: center;\
    font-size: 24px;\
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);\
    transition: all 0.3s ease;\
    z-index: 9998;\
}\
.llm-assistant-btn:hover {\
    transform: scale(1.1);\
    box-shadow: 0 6px 20px rgba(59, 130, 246, 0.5);\
    background: var(--nx-primary-hover, #2563eb);\
}\
.llm-assistant-btn.active {\
    background: var(--nx-success, #22c55e);\
}\
.llm-assistant-panel {\
    position: fixed;\
    bottom: 96px;\
    right: 24px;\
    width: 420px;\
    height: 600px;\
    max-height: calc(100vh - 140px);\
    background: var(--nx-bg-primary, #1a1a2e);\
    border-radius: 16px;\
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);\
    display: none;\
    flex-direction: column;\
    overflow: hidden;\
    z-index: 9997;\
    border: 1px solid var(--nx-border, #2a2a4a);\
}\
.llm-assistant-panel.open {\
    display: flex;\
}\
.llm-panel-header {\
    display: flex;\
    justify-content: space-between;\
    align-items: center;\
    padding: 12px 16px;\
    border-bottom: 1px solid var(--nx-border, #2a2a4a);\
    background: var(--nx-bg-elevated, #16213e);\
}\
.llm-panel-title {\
    display: flex;\
    align-items: center;\
    gap: 8px;\
    font-weight: 600;\
    color: var(--nx-text-primary, #fff);\
}\
.llm-panel-title i {\
    color: var(--nx-primary, #3b82f6);\
}\
.llm-panel-controls {\
    display: flex;\
    align-items: center;\
    gap: 8px;\
}\
.llm-model-select {\
    padding: 6px 12px;\
    border-radius: 6px;\
    border: 1px solid var(--nx-border, #2a2a4a);\
    background: var(--nx-bg-primary, #1a1a2e);\
    color: var(--nx-text-primary, #fff);\
    font-size: 12px;\
    max-width: 120px;\
}\
.llm-provider-select {\
    padding: 6px 12px;\
    border-radius: 6px;\
    border: 1px solid var(--nx-border, #2a2a4a);\
    background: var(--nx-bg-primary, #1a1a2e);\
    color: var(--nx-text-primary, #fff);\
    font-size: 12px;\
    max-width: 100px;\
}\
.llm-btn {\
    display: inline-flex;\
    align-items: center;\
    justify-content: center;\
    gap: 6px;\
    padding: 8px 16px;\
    border-radius: 8px;\
    border: none;\
    cursor: pointer;\
    font-size: 14px;\
    transition: all 0.2s;\
}\
.llm-btn--primary {\
    background: var(--nx-primary, #3b82f6);\
    color: white;\
}\
.llm-btn--primary:hover {\
    background: var(--nx-primary-hover, #2563eb);\
}\
.llm-btn--primary:disabled {\
    opacity: 0.5;\
    cursor: not-allowed;\
}\
.llm-btn--secondary {\
    background: var(--nx-bg-elevated, #16213e);\
    color: var(--nx-text-primary, #fff);\
    border: 1px solid var(--nx-border, #2a2a4a);\
}\
.llm-btn--icon {\
    padding: 6px;\
    background: transparent;\
    color: var(--nx-text-secondary, #888);\
}\
.llm-btn--icon:hover {\
    background: var(--nx-bg-hover, rgba(255,255,255,0.1));\
    color: var(--nx-text-primary, #fff);\
}\
.llm-btn--xs {\
    padding: 2px 4px;\
    font-size: 12px;\
}\
.llm-panel-messages {\
    flex: 1;\
    overflow-y: auto;\
    padding: 16px;\
}\
.llm-welcome {\
    text-align: center;\
    padding: 40px 20px;\
    color: var(--nx-text-secondary, #888);\
}\
.llm-welcome-icon {\
    font-size: 48px;\
    margin-bottom: 16px;\
    color: var(--nx-primary, #3b82f6);\
}\
.llm-welcome h3 {\
    margin: 0 0 8px 0;\
    color: var(--nx-text-primary, #fff);\
}\
.llm-welcome p {\
    margin: 0 0 24px 0;\
    font-size: 14px;\
}\
.llm-quick-actions {\
    display: flex;\
    flex-wrap: wrap;\
    gap: 8px;\
    justify-content: center;\
}\
.llm-quick-btn {\
    display: flex;\
    align-items: center;\
    gap: 6px;\
    padding: 8px 14px;\
    border-radius: 8px;\
    border: 1px solid var(--nx-border, #2a2a4a);\
    background: var(--nx-bg-primary, #1a1a2e);\
    color: var(--nx-text-primary, #fff);\
    cursor: pointer;\
    font-size: 13px;\
    transition: all 0.2s;\
}\
.llm-quick-btn:hover {\
    background: var(--nx-primary, #3b82f6);\
    border-color: var(--nx-primary, #3b82f6);\
}\
.llm-message {\
    margin-bottom: 12px;\
    display: flex;\
    gap: 10px;\
}\
.llm-message.user {\
    flex-direction: row-reverse;\
}\
.llm-message-avatar {\
    width: 32px;\
    height: 32px;\
    border-radius: 50%;\
    display: flex;\
    align-items: center;\
    justify-content: center;\
    flex-shrink: 0;\
}\
.llm-message.user .llm-message-avatar {\
    background: var(--nx-primary, #3b82f6);\
    color: white;\
}\
.llm-message.assistant .llm-message-avatar {\
    background: var(--nx-bg-elevated, #16213e);\
    color: var(--nx-primary, #3b82f6);\
}\
.llm-message-content {\
    max-width: 80%;\
    padding: 10px 14px;\
    border-radius: 12px;\
    font-size: 14px;\
    line-height: 1.5;\
}\
.llm-message.user .llm-message-content {\
    background: var(--nx-primary, #3b82f6);\
    color: white;\
}\
.llm-message.assistant .llm-message-content {\
    background: var(--nx-bg-elevated, #16213e);\
    color: var(--nx-text-primary, #fff);\
}\
.llm-message-content pre {\
    background: rgba(0,0,0,0.2);\
    padding: 8px 12px;\
    border-radius: 6px;\
    overflow-x: auto;\
    margin: 8px 0;\
}\
.llm-message-content code {\
    font-family: "Fira Code", monospace;\
    font-size: 13px;\
}\
.llm-panel-input {\
    padding: 12px 16px;\
    border-top: 1px solid var(--nx-border, #2a2a4a);\
    background: var(--nx-bg-elevated, #16213e);\
}\
.llm-context-badge {\
    display: flex;\
    align-items: center;\
    gap: 8px;\
    padding: 6px 10px;\
    background: var(--nx-primary-light, rgba(59, 130, 246, 0.1));\
    border-radius: 6px;\
    margin-bottom: 8px;\
    font-size: 12px;\
    color: var(--nx-primary, #3b82f6);\
}\
.llm-input-wrapper {\
    display: flex;\
    gap: 8px;\
}\
.llm-input-wrapper textarea {\
    flex: 1;\
    padding: 10px 14px;\
    border-radius: 8px;\
    border: 1px solid var(--nx-border, #2a2a4a);\
    background: var(--nx-bg-primary, #1a1a2e);\
    color: var(--nx-text-primary, #fff);\
    resize: none;\
    font-size: 14px;\
    font-family: inherit;\
}\
.llm-input-wrapper textarea:focus {\
    outline: none;\
    border-color: var(--nx-primary, #3b82f6);\
}\
.llm-input-hint {\
    margin-top: 6px;\
    font-size: 12px;\
    color: var(--nx-text-secondary, #888);\
}\
.llm-settings-modal {\
    position: fixed;\
    top: 0;\
    left: 0;\
    right: 0;\
    bottom: 0;\
    background: rgba(0,0,0,0.6);\
    display: none;\
    align-items: center;\
    justify-content: center;\
    z-index: 9999;\
}\
.llm-settings-modal.open {\
    display: flex;\
}\
.llm-modal-content {\
    background: var(--nx-bg-primary, #1a1a2e);\
    border-radius: 12px;\
    width: 400px;\
    max-width: 90%;\
    border: 1px solid var(--nx-border, #2a2a4a);\
}\
.llm-modal-header {\
    display: flex;\
    justify-content: space-between;\
    align-items: center;\
    padding: 16px;\
    border-bottom: 1px solid var(--nx-border, #2a2a4a);\
}\
.llm-modal-header h3 {\
    margin: 0;\
    color: var(--nx-text-primary, #fff);\
}\
.llm-modal-body {\
    padding: 16px;\
}\
.llm-form-group {\
    margin-bottom: 16px;\
}\
.llm-form-group label {\
    display: block;\
    margin-bottom: 8px;\
    font-weight: 500;\
    color: var(--nx-text-primary, #fff);\
    font-size: 14px;\
}\
.llm-form-group input[type="text"],\
.llm-form-group input[type="number"],\
.llm-form-group textarea {\
    width: 100%;\
    padding: 8px 12px;\
    border-radius: 6px;\
    border: 1px solid var(--nx-border, #2a2a4a);\
    background: var(--nx-bg-elevated, #16213e);\
    color: var(--nx-text-primary, #fff);\
    font-size: 14px;\
}\
.llm-form-group input[type="range"] {\
    width: 100%;\
}\
.llm-checkbox {\
    display: flex;\
    align-items: center;\
    gap: 8px;\
    cursor: pointer;\
}\
.llm-modal-footer {\
    display: flex;\
    justify-content: flex-end;\
    gap: 12px;\
    padding: 16px;\
    border-top: 1px solid var(--nx-border, #2a2a4a);\
}\
@media (max-width: 480px) {\
    .llm-assistant-panel {\
        width: calc(100vw - 48px);\
        right: 24px;\
    }\
}\
[data-theme="dark"] .llm-assistant-panel {\
    background: var(--nx-bg-primary, #0f0f1a);\
    border-color: var(--nx-border, #1a1a2a);\
}\
[data-theme="light"] .llm-assistant-panel {\
    background: var(--nx-bg-primary, #ffffff);\
    border-color: var(--nx-border, #e5e5e5);\
}\
[data-theme="light"] .llm-panel-header,\
[data-theme="light"] .llm-panel-input,\
[data-theme="light"] .llm-modal-content,\
[data-theme="light"] .llm-message.assistant .llm-message-avatar {\
    background: var(--nx-bg-elevated, #f5f5f5);\
}\
[data-theme="light"] .llm-input-wrapper textarea,\
[data-theme="light"] .llm-model-select,\
[data-theme="light"] .llm-provider-select,\
[data-theme="light"] .llm-quick-btn {\
    background: var(--nx-bg-primary, #ffffff);\
    color: var(--nx-text-primary, #333);\
}\
[data-theme="light"] .llm-panel-title,\
[data-theme="light"] .llm-welcome h3,\
[data-theme="light"] .llm-message.assistant .llm-message-content,\
[data-theme="light"] .llm-form-group label,\
[data-theme="light"] .llm-modal-header h3 {\
    color: var(--nx-text-primary, #333);\
}\
[data-theme="light"] .llm-welcome,\
[data-theme="light"] .llm-welcome p,\
[data-theme="light"] .llm-input-hint {\
    color: var(--nx-text-secondary, #666);\
}\
';
        },

        bindEvents: function() {
            var self = this;

            document.getElementById('llm-assistant-toggle').addEventListener('click', function() {
                self.togglePanel();
            });

            document.getElementById('llm-close-btn').addEventListener('click', function() {
                self.closePanel();
            });

            document.getElementById('llm-clear-btn').addEventListener('click', function() {
                self.clearChat();
            });

            document.getElementById('llm-settings-btn').addEventListener('click', function() {
                self.showSettings();
            });

            document.getElementById('llm-close-settings').addEventListener('click', function() {
                self.hideSettings();
            });

            document.getElementById('llm-save-settings').addEventListener('click', function() {
                self.saveSettings();
            });

            document.getElementById('llm-reset-settings').addEventListener('click', function() {
                self.resetSettings();
            });

            var input = document.getElementById('llm-input');
            input.addEventListener('input', function() {
                self.updateCharCount();
                self.updateSendButton();
                self.autoResize(this);
            });

            input.addEventListener('keydown', function(e) {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    self.sendMessage();
                }
            });

            document.getElementById('llm-send-btn').addEventListener('click', function() {
                self.sendMessage();
            });

            document.getElementById('llm-cancel-btn').addEventListener('click', function() {
                self.cancelStream();
            });

            document.getElementById('llm-provider-select').addEventListener('change', function() {
                self.currentProvider = this.value;
                self.updateModelSelect();
            });

            document.getElementById('llm-model-select').addEventListener('change', function() {
                self.currentModel = this.value;
            });

            document.getElementById('llm-temperature').addEventListener('input', function() {
                document.getElementById('llm-temp-value').textContent = this.value;
            });

            document.getElementById('llm-clear-context').addEventListener('click', function() {
                self.clearContext();
            });

            var quickBtns = document.querySelectorAll('.llm-quick-btn');
            quickBtns.forEach(function(btn) {
                btn.addEventListener('click', function() {
                    var action = this.getAttribute('data-action');
                    self.handleQuickAction(action);
                });
            });
        },

        togglePanel: function() {
            this.isOpen = !this.isOpen;
            var panel = document.getElementById('llm-assistant-panel');
            var btn = document.getElementById('llm-assistant-toggle');
            
            if (this.isOpen) {
                panel.classList.add('open');
                btn.classList.add('active');
            } else {
                panel.classList.remove('open');
                btn.classList.remove('active');
            }
        },

        closePanel: function() {
            this.isOpen = false;
            document.getElementById('llm-assistant-panel').classList.remove('open');
            document.getElementById('llm-assistant-toggle').classList.remove('active');
        },

        loadProviders: async function() {
            var self = this;
            try {
                var response = await fetch('/api/v1/llm/models', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });
                var data = await response.json();
                
                var resultData = data.data || data;
                var isSuccess = (data.code === 200 || data.requestStatus === 200 || data.status === 'success');
                
                if (isSuccess && resultData) {
                    self.providers = resultData.providers || [];
                    self.modelsByProvider = resultData.modelsByProvider || {};
                    self.currentProvider = resultData.currentProvider || 'qianwen';
                    self.currentModel = resultData.currentModel || 'qwen-plus';
                    
                    var providerSelect = document.getElementById('llm-provider-select');
                    providerSelect.innerHTML = '';
                    
                    var providerNames = {
                        'openai': 'OpenAI',
                        'qianwen': '通义千问',
                        'aliyun-bailian': '阿里云百炼',
                        'deepseek': 'DeepSeek',
                        'ollama': 'Ollama',
                        'volcengine': '火山引擎',
                        'siliconflow': 'SiliconFlow',
                        'google': 'Google Gemini',
                        'zhipu': '智谱AI',
                        'azure': 'Azure OpenAI',
                        'mock': '模拟器'
                    };
                    
                    self.providers.forEach(function(provider) {
                        var option = document.createElement('option');
                        option.value = provider;
                        option.textContent = providerNames[provider] || provider;
                        providerSelect.appendChild(option);
                    });
                    
                    providerSelect.value = self.currentProvider;
                    
                    self.updateModelSelect();
                }
            } catch (error) {
                console.error('Failed to load providers:', error);
                document.getElementById('llm-provider-select').innerHTML = '<option value="mock">模拟器</option>';
                document.getElementById('llm-model-select').innerHTML = '<option value="default">默认</option>';
            }
        },

        updateModelSelect: function() {
            var self = this;
            var modelSelect = document.getElementById('llm-model-select');
            modelSelect.innerHTML = '';
            
            var models = self.modelsByProvider[self.currentProvider] || [];
            
            models.forEach(function(model) {
                var option = document.createElement('option');
                option.value = model;
                option.textContent = model;
                modelSelect.appendChild(option);
            });
            
            if (models.length > 0) {
                self.currentModel = models[0];
                modelSelect.value = self.currentModel;
            }
        },

        loadSettings: function() {
            var saved = localStorage.getItem('llm-assistant-settings');
            if (saved) {
                this.settings = JSON.parse(saved);
                document.getElementById('llm-system-prompt').value = this.settings.systemPrompt;
                document.getElementById('llm-temperature').value = this.settings.temperature;
                document.getElementById('llm-temp-value').textContent = this.settings.temperature;
                document.getElementById('llm-max-tokens').value = this.settings.maxTokens;
                document.getElementById('llm-stream-enabled').checked = this.settings.streamEnabled;
                document.getElementById('llm-timeout').value = (this.settings.timeout || 60000) / 1000;
            }
        },

        saveSettings: function() {
            this.settings.systemPrompt = document.getElementById('llm-system-prompt').value;
            this.settings.temperature = parseFloat(document.getElementById('llm-temperature').value);
            this.settings.maxTokens = parseInt(document.getElementById('llm-max-tokens').value);
            this.settings.streamEnabled = document.getElementById('llm-stream-enabled').checked;
            this.settings.timeout = parseInt(document.getElementById('llm-timeout').value) * 1000;
            
            localStorage.setItem('llm-assistant-settings', JSON.stringify(this.settings));
            this.hideSettings();
        },

        resetSettings: function() {
            this.settings = {
                systemPrompt: '',
                temperature: 0.7,
                maxTokens: 2048,
                streamEnabled: true,
                timeout: 60000
            };
            document.getElementById('llm-system-prompt').value = '';
            document.getElementById('llm-temperature').value = 0.7;
            document.getElementById('llm-temp-value').textContent = '0.7';
            document.getElementById('llm-max-tokens').value = 2048;
            document.getElementById('llm-stream-enabled').checked = true;
            document.getElementById('llm-timeout').value = 60;
        },

        showSettings: function() {
            document.getElementById('llm-settings-modal').classList.add('open');
        },

        hideSettings: function() {
            document.getElementById('llm-settings-modal').classList.remove('open');
        },

        setContext: function(context) {
            this.contextData = context;
            var badge = document.getElementById('llm-context-badge');
            var text = document.getElementById('llm-context-text');
            
            if (context) {
                badge.style.display = 'flex';
                text.textContent = '上下文: ' + (context.name || context.type || '已选择');
            } else {
                badge.style.display = 'none';
            }
        },

        clearContext: function() {
            this.contextData = null;
            document.getElementById('llm-context-badge').style.display = 'none';
        },

        handleQuickAction: function(action) {
            var prompts = {
                'config': '请帮我分析当前页面并提供配置建议。',
                'analyze': '请帮我分析当前页面的数据。',
                'code': '请帮我生成相关代码。',
                'help': '请告诉我如何使用当前功能。'
            };
            
            document.getElementById('llm-input').value = prompts[action] || '';
            this.updateCharCount();
            this.updateSendButton();
        },

        sendMessage: async function() {
            var input = document.getElementById('llm-input');
            var message = input.value.trim();
            
            if (!message || this.isStreaming) return;

            this.addMessage('user', message);
            input.value = '';
            this.updateCharCount();
            this.updateSendButton();

            var contextPrompt = this.buildContextPrompt(message);

            if (this.settings.streamEnabled) {
                await this.sendStreamMessage(contextPrompt);
            } else {
                await this.sendSyncMessage(contextPrompt);
            }
        },

        buildContextPrompt: function(message) {
            var prompt = message;
            
            if (this.contextData) {
                prompt = '[上下文信息]\n';
                prompt += '类型: ' + (this.contextData.type || '未知') + '\n';
                if (this.contextData.name) {
                    prompt += '名称: ' + this.contextData.name + '\n';
                }
                if (this.contextData.data) {
                    prompt += '数据: ' + JSON.stringify(this.contextData.data, null, 2) + '\n';
                }
                prompt += '\n[用户问题]\n' + message;
            }
            
            return prompt;
        },

        sendSyncMessage: async function(message) {
            var self = this;
            
            if (this.abortController) {
                this.abortController.abort();
            }
            this.abortController = new AbortController();
            
            try {
                this.updateStatus('sending');
                
                var timeoutId = setTimeout(function() {
                    if (self.abortController) {
                        self.abortController.abort();
                    }
                }, self.settings.timeout);
                
                var response = await fetch('/api/v1/llm/chat', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        message: message,
                        model: this.currentModel,
                        provider: this.currentProvider,
                        temperature: this.settings.temperature,
                        maxTokens: this.settings.maxTokens
                    }),
                    signal: this.abortController.signal
                });
                
                clearTimeout(timeoutId);
                
                var data = await response.json();
                
                if (data.requestStatus === 200 && data.data) {
                    this.addMessage('assistant', data.data.response);
                    
                    var action = data.data.action || data.action;
                    if (action && typeof ContextTrustLayer !== 'undefined') {
                        this.executeAction(action);
                    }
                    
                    if (data.data.syncContext && data.data.syncData && typeof ContextTrustLayer !== 'undefined') {
                        ContextTrustLayer.syncToFrontend(data.data.syncData);
                    }
                } else {
                    this.addMessage('assistant', '抱歉，发生了错误: ' + data.message, true);
                }
                
                this.updateStatus('ready');
            } catch (error) {
                console.error('Chat error:', error);
                if (error.name === 'AbortError') {
                    this.addMessage('assistant', '请求超时，请重试', true);
                    this.updateStatus('timeout');
                } else {
                    this.addMessage('assistant', '抱歉，网络错误，请重试', true);
                    this.updateStatus('error');
                }
            } finally {
                this.abortController = null;
            }
        },

        executeAction: function(action) {
            var self = this;
            
            if (!action || !action.action) {
                return;
            }
            
            console.log('[LlmAssistant] Executing action:', action);
            
            this.addMessage('assistant', '正在执行操作: ' + action.action + '...');
            
            if (typeof ContextTrustLayer !== 'undefined') {
                ContextTrustLayer.executeAction({
                    name: action.action,
                    module: action.module || 'discovery',
                    params: action
                }).then(function(result) {
                    console.log('[LlmAssistant] Action result:', result);
                    if (result && result.success) {
                        self.addMessage('assistant', '操作执行成功！');
                    } else if (result && result.error) {
                        self.addMessage('assistant', '操作失败: ' + result.error, true);
                    }
                }).catch(function(err) {
                console.error('[LlmAssistant] Action execution error:', err);
                self.addMessage('assistant', '操作执行失败: ' + err.message, true);
            });
            } else {
                console.warn('[LlmAssistant] ContextTrustLayer not available');
            }
        },

        sendStreamMessage: async function(message) {
            var self = this;
            
            if (this.abortController) {
                this.abortController.abort();
            }
            
            this.isStreaming = true;
            this.updateStatus('streaming');
            this.abortController = new AbortController();

            var messageDiv = this.createMessageElement('assistant', '');
            var contentDiv = messageDiv.querySelector('.llm-message-content');
            document.getElementById('llm-messages').appendChild(messageDiv);

            var fullResponse = '';
            var lastReceiveTime = Date.now();

            try {
                var timeoutId = setTimeout(function() {
                    if (self.abortController) {
                        self.abortController.abort();
                    }
                }, self.settings.timeout);

                var response = await fetch('/api/v1/llm/chat/stream', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        message: message,
                        model: this.currentModel,
                        provider: this.currentProvider,
                        temperature: this.settings.temperature,
                        maxTokens: this.settings.maxTokens
                    }),
                    signal: this.abortController.signal
                });

                if (!response.ok) {
                    throw new Error('HTTP ' + response.status + ': ' + response.statusText);
                }

                if (!response.body) {
                    throw new Error('Response body is null');
                }

                var reader = response.body.getReader();
                var decoder = new TextDecoder();

                var buffer = '';
                var currentEvent = 'message';
                var idleTimeout = 90000;
                
                var idleCheckId = setInterval(function() {
                    if (Date.now() - lastReceiveTime > idleTimeout) {
                        clearInterval(idleCheckId);
                        if (self.isStreaming) {
                            self.abortController.abort();
                            contentDiv.innerHTML = '抱歉，响应超时，请重试';
                            self.updateStatus('timeout');
                            self.isStreaming = false;
                            self.updateSendButton();
                        }
                    }
                }, 5000);

                while (true) {
                    var result = await reader.read();
                    if (result.done) {
                        clearInterval(idleCheckId);
                        clearTimeout(timeoutId);
                        break;
                    }

                    lastReceiveTime = Date.now();

                    buffer += decoder.decode(result.value, { stream: true });
                    var lines = buffer.split('\n');
                    buffer = lines.pop() || '';

                    for (var i = 0; i < lines.length; i++) {
                        var line = lines[i].trim();
                        if (line.startsWith('event:')) {
                            currentEvent = line.substring(6).trim();
                        } else if (line.startsWith('data:')) {
                            var data = line.substring(5).trim();
                            if (data === '[DONE]') {
                                clearInterval(idleCheckId);
                                clearTimeout(timeoutId);
                                break;
                            }
                            if (data) {
                                if (currentEvent === 'action') {
                                    try {
                                        var actionData = JSON.parse(data);
                                        console.log('[LlmAssistant] Received action event:', actionData);
                                        if (actionData.data && actionData.data.action) {
                                            self.executeAction(actionData.data.action);
                                        } else if (actionData.action) {
                                            self.executeAction(actionData);
                                        }
                                    } catch (e) {
                                        console.error('[LlmAssistant] Failed to parse action data:', e);
                                    }
                                } else {
                                    fullResponse += data;
                                    contentDiv.innerHTML = self.formatResponse(fullResponse);
                                    self.scrollToBottom();
                                }
                            }
                        }
                    }
                }

                this.updateStatus('ready');
            } catch (error) {
                clearInterval(idleCheckId);
                clearTimeout(timeoutId);
                
                var errorMessage = '抱歉，流式输出发生错误: ';
                var errorType = 'error';
                
                if (error.name === 'AbortError') {
                    if (!fullResponse) {
                        errorMessage = '请求已取消或超时，请重试';
                    } else {
                        errorMessage = null;
                    }
                    errorType = 'cancelled';
                } else if (error.message && error.message.includes('Failed to fetch')) {
                    errorMessage += '网络连接失败，请检查：<br>' +
                        '1. 后端服务是否正常运行<br>' +
                        '2. 网络连接是否正常<br>' +
                        '3. 防火墙是否阻止了连接<br>' +
                        '错误详情: ' + error.message;
                } else if (error.message && error.message.includes('HTTP')) {
                    errorMessage += 'API请求失败: ' + error.message + '<br>' +
                        '可能原因：<br>' +
                        '1. API Key 无效或已过期<br>' +
                        '2. API 配额已用完<br>' +
                        '3. 服务端错误<br>' +
                        '建议：请检查 LLM 配置或联系管理员';
                } else if (error.message && error.message.includes('timeout')) {
                    errorMessage += '请求超时，可能原因：<br>' +
                        '1. 网络延迟过高<br>' +
                        '2. API 服务响应慢<br>' +
                        '建议：请稍后重试或检查网络连接';
                } else {
                    errorMessage += error.message || '未知错误<br>' +
                        '建议：请查看浏览器控制台获取详细错误信息';
                }
                
                if (errorMessage) {
                    console.error('[LlmAssistant] Stream error:', error);
                    contentDiv.innerHTML = '<div style="color: #ef4444; padding: 10px; background: #fee; border-radius: 4px; border-left: 3px solid #ef4444;">' + errorMessage + '</div>';
                }
                
                this.updateStatus(errorType);
            } finally {
                this.isStreaming = false;
                this.abortController = null;
                this.updateSendButton();
            }
        },

        cancelStream: function() {
            if (this.abortController) {
                this.abortController.abort();
                this.abortController = null;
            }
            if (this.streamTimeoutId) {
                clearTimeout(this.streamTimeoutId);
                this.streamTimeoutId = null;
            }
            this.isStreaming = false;
            this.updateStatus('ready');
            this.updateSendButton();
        },

        addMessage: function(role, content, isError) {
            var messagesContainer = document.getElementById('llm-messages');
            
            var welcomeMessage = messagesContainer.querySelector('.llm-welcome');
            if (welcomeMessage) {
                welcomeMessage.remove();
            }

            var messageDiv = this.createMessageElement(role, content, isError);
            messagesContainer.appendChild(messageDiv);
            this.scrollToBottom();

            this.messages.push({ role: role, content: content });
        },

        createMessageElement: function(role, content, isError) {
            var div = document.createElement('div');
            div.className = 'llm-message ' + role;

            var avatarIcon = role === 'user' ? 'ri-user-line' : 'ri-robot-line';
            
            div.innerHTML = '\
                <div class="llm-message-avatar">\
                    <i class="' + avatarIcon + '"></i>\
                </div>\
                <div class="llm-message-content">' + this.formatResponse(content) + '</div>\
            ';

            return div;
        },

        formatResponse: function(content) {
            if (!content) return '';
            
            return content
                .replace(/\\n/g, '\n')
                .replace(/```(\w+)?\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
                .replace(/`([^`]+)`/g, '<code>$1</code>')
                .replace(/\n/g, '<br>');
        },

        scrollToBottom: function() {
            var container = document.getElementById('llm-messages');
            container.scrollTop = container.scrollHeight;
        },

        updateCharCount: function() {
            var input = document.getElementById('llm-input');
            document.getElementById('llm-char-count').textContent = input.value.length;
        },

        updateSendButton: function() {
            var input = document.getElementById('llm-input');
            var sendBtn = document.getElementById('llm-send-btn');
            var cancelBtn = document.getElementById('llm-cancel-btn');
            
            sendBtn.disabled = !input.value.trim() || this.isStreaming;
            
            if (cancelBtn) {
                cancelBtn.style.display = this.isStreaming ? 'inline-flex' : 'none';
            }
        },

        updateStatus: function(status) {
            var statusEl = document.getElementById('llm-status');
            
            var statusText = {
                'ready': '就绪',
                'sending': '发送中...',
                'streaming': '输出中...',
                'error': '错误',
                'timeout': '超时',
                'cancelled': '已取消'
            };
            statusEl.textContent = statusText[status] || status;
        },

        clearChat: function() {
            this.messages = [];
            document.getElementById('llm-messages').innerHTML = '\
                <div class="llm-welcome">\
                    <div class="llm-welcome-icon">\
                        <i class="ri-robot-smile-line"></i>\
                    </div>\
                    <h3>AI 智能助手</h3>\
                    <p>我可以帮助您配置场景、分析数据、编写代码等</p>\
                    <div class="llm-quick-actions">\
                        <button class="llm-quick-btn" data-action="config">\
                            <i class="ri-settings-4-line"></i> 自动配置\
                        </button>\
                        <button class="llm-quick-btn" data-action="analyze">\
                            <i class="ri-bar-chart-line"></i> 分析数据\
                        </button>\
                        <button class="llm-quick-btn" data-action="code">\
                            <i class="ri-code-line"></i> 生成代码\
                        </button>\
                        <button class="llm-quick-btn" data-action="help">\
                            <i class="ri-question-line"></i> 使用帮助\
                        </button>\
                    </div>\
                </div>\
            ';
            
            this.bindQuickActions();
        },

        bindQuickActions: function() {
            var self = this;
            var quickBtns = document.querySelectorAll('.llm-quick-btn');
            quickBtns.forEach(function(btn) {
                btn.addEventListener('click', function() {
                    var action = this.getAttribute('data-action');
                    self.handleQuickAction(action);
                });
            });
        },

        autoResize: function(textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = Math.min(textarea.scrollHeight, 100) + 'px';
        }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            LlmAssistant.init();
        });
    } else {
        LlmAssistant.init();
    }

    global.LlmAssistant = LlmAssistant;
})(window);
