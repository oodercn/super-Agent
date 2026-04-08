/**
 * LlmChatFloat 核心模块
 * 包含初始化、配置、状态管理
 */
import { Utils } from './utils.js';

export class LlmChatFloatCore {
    constructor() {
        this.isOpen = false;
        this.isStreaming = false;
        this.messages = [];
        this.config = {
            model: 'default',
            temperature: 0.7,
            context: '',
            maxTokens: 2000
        };
        this.models = [];
        this.templates = [];
        this.sessionId = this.generateSessionId();
        
        this.sceneGroupId = null;
        this.sceneContext = null;
        this.sceneParticipants = [];
        this.sceneMessages = [];
        this.sceneTodos = [];
        this.selectedParticipant = null;
        this.currentTab = 'all';
        this.ws = null;
        this.mentionDropdownVisible = false;
    }

    generateSessionId() {
        return 'session-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
    }

    async init(sceneGroupId) {
        this.sceneGroupId = sceneGroupId || this.detectSceneGroupId();
        
        await this.loadLlmConfig();
        await this.loadAvailableModels();
        await this.loadPromptTemplates();
        
        if (this.isSceneMode()) {
            await this.loadSceneContext();
            await this.loadSceneMessages();
            await this.loadSceneTodos();
            this.connectWebSocket();
        }
        
        this.render();
        this.bindEvents();
        
        console.log('[LlmChatFloat] Initialized with session:', this.sessionId);
    }

    detectSceneGroupId() {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id') || urlParams.get('sceneGroupId');
        if (id) {
            console.log('[LlmChatFloat] Detected sceneGroupId from URL:', id);
        }
        return id;
    }

    isSceneMode() {
        return this.sceneGroupId !== null;
    }

    async loadLlmConfig() {
        try {
            const response = await fetch('/api/v1/llm/config');
            const result = await response.json();
            if (result.code === 200 && result.data) {
                this.config = { ...this.config, ...result.data };
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to load LLM config:', error);
        }
    }

    async loadAvailableModels() {
        try {
            const response = await fetch('/api/v1/llm/models', {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });
            const result = await response.json();
            if (result.code === 200 && result.data) {
                this.models = result.data;
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to load models:', error);
        }
    }

    async loadPromptTemplates() {
        try {
            const response = await fetch('/api/v1/llm/templates');
            const result = await response.json();
            if (result.code === 200 && result.data) {
                this.templates = result.data;
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to load templates:', error);
        }
    }

    async loadSceneContext() {
        if (!this.sceneGroupId) return;
        
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/context`);
            const result = await response.json();
            console.log('[LlmChatFloat] Load scene context response:', result);
            if ((result.status === 'success' || result.code === 200) && result.data) {
                this.sceneContext = result.data;
                this.sceneParticipants = result.data.participants || [];
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to load scene context:', error);
        }
    }

    async loadSceneMessages() {
        if (!this.sceneGroupId) return;
        
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/messages`);
            const result = await response.json();
            console.log('[LlmChatFloat] Load scene messages response:', result);
            if ((result.status === 'success' || result.code === 200) && result.data) {
                this.sceneMessages = result.data.list || result.data || [];
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to load scene messages:', error);
        }
    }

    async loadSceneTodos() {
        if (!this.sceneGroupId) return;

        try {
            const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/todos`);
            const result = await response.json();
            console.log('[LlmChatFloat] Load scene todos response:', result);
            if ((result.status === 'success' || result.code === 200) && result.data) {
                this.sceneTodos = result.data;
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to load scene todos:', error);
        }
    }

    saveConfig() {
        localStorage.setItem('llmChatConfig', JSON.stringify(this.config));
    }

    async sendMessage() {
        const input = document.getElementById('llmChatInput');
        const content = input ? input.value.trim() : '';
        
        if (!content || this.isStreaming) return;
        
        input.value = '';
        
        if (this.isSceneMode()) {
            await this.sendSceneMessage(content);
        } else {
            await this.sendNormalMessage(content);
        }
    }

    async sendNormalMessage(content) {
        this.messages.push({ role: 'user', content });
        this.addMessage('user', content);
        
        this.isStreaming = true;
        this.updateSendButton(true);
        
        const messageId = 'msg-' + Date.now();
        this.addStreamingMessage(messageId);
        
        try {
            const response = await fetch('/api/v1/llm/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    message: content,
                    provider: this.config.provider,
                    model: this.config.model,
                    sessionId: this.sessionId,
                    temperature: this.config.temperature
                })
            });

            const result = await response.json();

            if (result.code === 200 && result.data) {
                const responseContent = result.data.content || result.data.message || '收到回复';
                this.messages.push({ role: 'assistant', content: responseContent });
                this.updateStreamingMessage(messageId, responseContent);
            } else {
                this.updateStreamingMessage(messageId, result.message || '抱歉，未收到有效回复。');
            }
        } catch (e) {
            console.error('[LlmChatFloat] Error:', e);
            this.updateStreamingMessage(messageId, '抱歉，网络连接失败，请稍后重试。');
        } finally {
            this.isStreaming = false;
            this.updateSendButton(false);
        }
    }

    async sendSceneMessage(content) {
        this.isStreaming = true;
        this.updateSendButton(true);
        
        const userMsg = {
            id: 'msg-' + Date.now(),
            content: content,
            fromParticipant: { id: 'current-user', name: '我', type: 'USER' },
            createTime: Date.now(),
            messageType: 'USER_MESSAGE'
        };
        
        this.sceneMessages.push(userMsg);
        this.addSceneMessage(userMsg);
        
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/messages`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    content: content,
                    messageType: this.selectedParticipant ? 'P2P' : 'P2A',
                    toParticipantId: this.selectedParticipant?.id
                })
            });

            const result = await response.json();
            console.log('[LlmChatFloat] Send message response:', result);

            if (result.status === 'success' || result.code === 200) {
                const agentMsg = {
                    id: 'msg-agent-' + Date.now(),
                    content: '收到您的消息，我会尽快处理。',
                    fromParticipant: { id: 'agent-main', name: '智能助手', type: 'AGENT' },
                    createTime: Date.now(),
                    messageType: 'AGENT_REPLY'
                };
                this.sceneMessages.push(agentMsg);
                this.addSceneMessage(agentMsg);
            } else {
                const fallbackMsg = {
                    id: 'msg-agent-' + Date.now(),
                    content: result.message || '消息已发送。',
                    fromParticipant: { id: 'agent-main', name: '智能助手', type: 'AGENT' },
                    createTime: Date.now(),
                    messageType: 'AGENT_REPLY'
                };
                this.sceneMessages.push(fallbackMsg);
                this.addSceneMessage(fallbackMsg);
            }
        } catch (e) {
            console.error('[LlmChatFloat] Scene message error:', e);
            const errorMsg = {
                id: 'msg-error-' + Date.now(),
                content: '抱歉，消息发送失败，请稍后重试。',
                fromParticipant: { id: 'agent-main', name: '智能助手', type: 'AGENT' },
                createTime: Date.now(),
                messageType: 'SYSTEM'
            };
            this.sceneMessages.push(errorMsg);
            this.addSceneMessage(errorMsg);
        } finally {
            this.isStreaming = false;
            this.updateSendButton(false);
        }
    }

    addMessage(role, content) {
        const container = document.getElementById('llmChatMessages');
        if (!container) return;
        
        const msgEl = document.createElement('div');
        msgEl.className = `llm-chat-float-message ${role}`;
        msgEl.innerHTML = `<div class="llm-chat-float-message-content">${this.escapeHtml(content)}</div>`;
        container.appendChild(msgEl);
        container.scrollTop = container.scrollHeight;
    }

    addStreamingMessage(messageId) {
        const container = document.getElementById('llmChatMessages');
        if (!container) return;
        
        const msgEl = document.createElement('div');
        msgEl.id = messageId;
        msgEl.className = 'llm-chat-float-message assistant';
        msgEl.innerHTML = `<div class="llm-chat-float-message-content"><span class="typing-indicator">正在输入...</span></div>`;
        container.appendChild(msgEl);
        container.scrollTop = container.scrollHeight;
    }

    updateStreamingMessage(messageId, content) {
        const msgEl = document.getElementById(messageId);
        if (!msgEl) return;
        
        const contentEl = msgEl.querySelector('.llm-chat-float-message-content');
        if (contentEl) {
            contentEl.innerHTML = this.escapeHtml(content);
        }
        
        const container = document.getElementById('llmChatMessages');
        if (container) {
            container.scrollTop = container.scrollHeight;
        }
    }

    addSceneMessage(msg) {
        const container = document.getElementById('llmChatMessages');
        if (!container) return;
        
        const isAgent = msg.fromParticipant?.type === 'AGENT' || msg.fromParticipant?.type === 'SUPER_AGENT';
        
        const msgEl = document.createElement('div');
        msgEl.className = `agent-chat-message ${isAgent ? 'is-agent' : ''}`;
        msgEl.innerHTML = `
            <div class="message-header">
                <span class="message-sender">${msg.fromParticipant?.name || '未知'}</span>
                <span class="message-time">${Utils.formatTime(msg.createTime)}</span>
            </div>
            <div class="message-content">${this.escapeHtml(msg.content)}</div>
        `;
        container.appendChild(msgEl);
        container.scrollTop = container.scrollHeight;
    }

    updateSendButton(loading) {
        const sendBtn = document.getElementById('llmChatSendBtn');
        if (sendBtn) {
            sendBtn.disabled = loading;
            sendBtn.innerHTML = loading 
                ? '<i class="ri-loader-4-line ri-spin"></i>' 
                : '<i class="ri-send-plane-fill"></i>';
        }
    }

    clearChat() {
        this.messages = [];
        const container = document.getElementById('llmChatMessages');
        if (container) {
            container.innerHTML = '';
        }
    }

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    enable() {
        const btn = document.getElementById('llmChatFloatBtn');
        if (btn) {
            btn.style.display = 'flex';
        }
    }

    disable() {
        const btn = document.getElementById('llmChatFloatBtn');
        const window = document.getElementById('llmChatFloatWindow');
        if (btn) {
            btn.style.display = 'none';
        }
        if (window) {
            window.classList.remove('open');
        }
        this.isOpen = false;
    }
}

export default LlmChatFloatCore;
