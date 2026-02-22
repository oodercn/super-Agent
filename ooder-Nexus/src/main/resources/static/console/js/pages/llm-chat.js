(function(global) {
    'use strict';

    var LlmChat = {
        messages: [],
        currentModel: null,
        isStreaming: false,
        settings: {
            systemPrompt: '',
            temperature: 0.7,
            maxTokens: 2048,
            streamEnabled: true
        },

        init: function() {
            this.bindEvents();
            this.loadModels();
            this.loadSettings();
        },

        bindEvents: function() {
            var self = this;

            var input = document.getElementById('chat-input');
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

            document.getElementById('send-btn').addEventListener('click', function() {
                self.sendMessage();
            });

            document.getElementById('clear-btn').addEventListener('click', function() {
                self.clearChat();
            });

            document.getElementById('settings-btn').addEventListener('click', function() {
                self.showSettings();
            });

            document.getElementById('close-settings').addEventListener('click', function() {
                self.hideSettings();
            });

            document.getElementById('save-settings').addEventListener('click', function() {
                self.saveSettings();
            });

            document.getElementById('reset-settings').addEventListener('click', function() {
                self.resetSettings();
            });

            document.getElementById('model-select').addEventListener('change', function() {
                self.currentModel = this.value;
            });

            document.getElementById('temperature').addEventListener('input', function() {
                document.getElementById('temperature-value').textContent = this.value;
            });

            var quickActions = document.querySelectorAll('.quick-action-btn');
            quickActions.forEach(function(btn) {
                btn.addEventListener('click', function() {
                    var prompt = this.getAttribute('data-prompt');
                    document.getElementById('chat-input').value = prompt;
                    self.updateCharCount();
                    self.updateSendButton();
                });
            });
        },

        loadModels: async function() {
            try {
                var response = await fetch('/api/llm/models', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });
                var data = await response.json();
                
                if (data.requestStatus === 200 && data.data) {
                    var select = document.getElementById('model-select');
                    select.innerHTML = '';
                    
                    var models = data.data.models || [];
                    models.forEach(function(model) {
                        var option = document.createElement('option');
                        option.value = model;
                        option.textContent = model;
                        select.appendChild(option);
                    });
                    
                    this.currentModel = data.data.currentModel || models[0];
                    select.value = this.currentModel;
                }
            } catch (error) {
                console.error('Failed to load models:', error);
                document.getElementById('model-select').innerHTML = '<option value="gpt-4">gpt-4</option>';
            }
        },

        loadSettings: function() {
            var saved = localStorage.getItem('llm-chat-settings');
            if (saved) {
                this.settings = JSON.parse(saved);
                document.getElementById('system-prompt').value = this.settings.systemPrompt;
                document.getElementById('temperature').value = this.settings.temperature;
                document.getElementById('temperature-value').textContent = this.settings.temperature;
                document.getElementById('max-tokens').value = this.settings.maxTokens;
                document.getElementById('stream-enabled').checked = this.settings.streamEnabled;
            }
        },

        saveSettings: function() {
            this.settings.systemPrompt = document.getElementById('system-prompt').value;
            this.settings.temperature = parseFloat(document.getElementById('temperature').value);
            this.settings.maxTokens = parseInt(document.getElementById('max-tokens').value);
            this.settings.streamEnabled = document.getElementById('stream-enabled').checked;
            
            localStorage.setItem('llm-chat-settings', JSON.stringify(this.settings));
            this.hideSettings();
        },

        resetSettings: function() {
            this.settings = {
                systemPrompt: '',
                temperature: 0.7,
                maxTokens: 2048,
                streamEnabled: true
            };
            document.getElementById('system-prompt').value = '';
            document.getElementById('temperature').value = 0.7;
            document.getElementById('temperature-value').textContent = '0.7';
            document.getElementById('max-tokens').value = 2048;
            document.getElementById('stream-enabled').checked = true;
        },

        showSettings: function() {
            document.getElementById('settings-modal').style.display = 'flex';
        },

        hideSettings: function() {
            document.getElementById('settings-modal').style.display = 'none';
        },

        sendMessage: async function() {
            var input = document.getElementById('chat-input');
            var message = input.value.trim();
            
            if (!message || this.isStreaming) return;

            this.addMessage('user', message);
            input.value = '';
            this.updateCharCount();
            this.updateSendButton();

            if (this.settings.streamEnabled) {
                await this.sendStreamMessage(message);
            } else {
                await this.sendSyncMessage(message);
            }
        },

        sendSyncMessage: async function(message) {
            try {
                var response = await fetch('/api/llm/chat', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        prompt: message,
                        systemPrompt: this.settings.systemPrompt
                    })
                });
                
                var data = await response.json();
                
                if (data.requestStatus === 200 && data.data) {
                    this.addMessage('assistant', data.data.response);
                    this.updateTokenUsage(data.data.tokenUsage);
                } else {
                    this.addMessage('assistant', '抱歉，发生了错误: ' + data.message, true);
                }
            } catch (error) {
                console.error('Chat error:', error);
                this.addMessage('assistant', '抱歉，网络错误，请重试', true);
            }
        },

        sendStreamMessage: async function(message) {
            var self = this;
            this.isStreaming = true;
            this.updateStreamStatus('streaming');

            var messageDiv = this.createMessageElement('assistant', '');
            var contentDiv = messageDiv.querySelector('.message-content');
            document.getElementById('chat-messages').appendChild(messageDiv);

            var fullResponse = '';

            try {
                var response = await fetch('/api/llm/chat/stream', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        prompt: message,
                        systemPrompt: this.settings.systemPrompt
                    })
                });

                var reader = response.body.getReader();
                var decoder = new TextDecoder();

                while (true) {
                    var result = await reader.read();
                    if (result.done) break;

                    var chunk = decoder.decode(result.value);
                    var lines = chunk.split('\n');

                    for (var i = 0; i < lines.length; i++) {
                        var line = lines[i];
                        if (line.startsWith('data:')) {
                            var data = line.substring(5).trim();
                            if (data === '[DONE]') {
                                break;
                            }
                            if (data) {
                                fullResponse += data;
                                contentDiv.innerHTML = self.formatResponse(fullResponse);
                                self.scrollToBottom();
                            }
                        }
                    }
                }

                this.updateStreamStatus('ready');
            } catch (error) {
                console.error('Stream error:', error);
                contentDiv.innerHTML = '抱歉，流式输出发生错误';
                this.updateStreamStatus('error');
            } finally {
                this.isStreaming = false;
                this.updateSendButton();
            }
        },

        addMessage: function(role, content, isError) {
            var messagesContainer = document.getElementById('chat-messages');
            
            var welcomeMessage = messagesContainer.querySelector('.welcome-message');
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
            div.className = 'message ' + role;

            var avatarIcon = role === 'user' ? 'ri-user-line' : 'ri-robot-line';
            
            div.innerHTML = '\
                <div class="message-avatar">\
                    <i class="' + avatarIcon + '"></i>\
                </div>\
                <div class="message-content">' + this.formatResponse(content) + '</div>\
            ';

            return div;
        },

        formatResponse: function(content) {
            if (!content) return '';
            
            return content
                .replace(/```(\w+)?\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
                .replace(/`([^`]+)`/g, '<code>$1</code>')
                .replace(/\n/g, '<br>');
        },

        scrollToBottom: function() {
            var container = document.getElementById('chat-messages');
            container.scrollTop = container.scrollHeight;
        },

        updateCharCount: function() {
            var input = document.getElementById('chat-input');
            document.getElementById('char-count').textContent = input.value.length;
        },

        updateSendButton: function() {
            var input = document.getElementById('chat-input');
            var btn = document.getElementById('send-btn');
            btn.disabled = !input.value.trim() || this.isStreaming;
        },

        updateStreamStatus: function(status) {
            var statusEl = document.getElementById('stream-status');
            statusEl.className = status;
            
            var statusText = {
                'ready': '就绪',
                'streaming': '输出中...',
                'error': '错误'
            };
            statusEl.textContent = statusText[status] || status;
        },

        updateTokenUsage: function(usage) {
            if (usage) {
                document.getElementById('token-count').textContent = 
                    (usage.totalTokens || usage.promptTokens + usage.completionTokens || 0);
            }
        },

        clearChat: function() {
            if (confirm('确定要清空所有对话记录吗？')) {
                this.messages = [];
                document.getElementById('chat-messages').innerHTML = '\
                    <div class="welcome-message">\
                        <div class="welcome-icon">\
                            <i class="ri-robot-smile-line"></i>\
                        </div>\
                        <h2>欢迎使用 AI 智能对话</h2>\
                        <p>选择模型后开始对话，支持流式输出和多种 AI 能力</p>\
                    </div>\
                ';
                document.getElementById('token-count').textContent = '0';
            }
        },

        autoResize: function(textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = Math.min(textarea.scrollHeight, 150) + 'px';
        }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            LlmChat.init();
        });
    } else {
        LlmChat.init();
    }

    global.LlmChat = LlmChat;
})(window);
