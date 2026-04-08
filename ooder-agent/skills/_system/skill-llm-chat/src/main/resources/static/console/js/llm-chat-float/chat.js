/**
 * LlmChatFloat 聊天模块
 * 包含发送消息、处理响应
 */
import { Utils } from './utils.js';

export const ChatMixin = {
    async sendMessage() {
        if (this.isStreaming) return;
        
        const input = document.getElementById('llmChatInput');
        const content = input ? input.value.trim() : '';
        
        if (!content) return;
        
        if (this.isSceneMode() && this.sceneGroupId) {
            await this.sendSceneMessage(content);
            input.value = '';
            return;
        }
        
        this.addMessage('user', content);
        this.messages.push({ role: 'user', content });
        input.value = '';
        
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

            if (result.code === 200 && result.data && result.data.content) {
                const responseContent = result.data.content;
                this.messages.push({ role: 'assistant', content: responseContent });
                this.updateStreamingMessage(messageId, responseContent);
            } else if (result.message) {
                this.updateStreamingMessage(messageId, result.message);
            } else {
                this.updateStreamingMessage(messageId, '抱歉，未收到有效回复。');
            }

        } catch (e) {
            console.error('[LlmChatFloat] Error:', e);
            this.updateStreamingMessage(messageId, '抱歉，网络连接失败，请稍后重试。');
        } finally {
            this.isStreaming = false;
            this.updateSendButton(false);
        }
    },

    async sendSceneMessage(content) {
        if (this.isStreaming) return;
        
        this.isStreaming = true;
        this.updateSendButton(true);
        
        const messageId = 'msg-' + Date.now();
        this.addSceneMessage({
            id: messageId,
            content: content,
            fromParticipant: { id: 'current-user', name: '我', type: 'USER' },
            createTime: Date.now(),
            messageType: 'P2A'
        });
        
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

            if (result.status === 'success' && result.data) {
                const messages = result.data.list || result.data;
                if (Array.isArray(messages) && messages.length > 0) {
                    const lastMsg = messages[messages.length - 1];
                    if (lastMsg && lastMsg.fromParticipant?.type === 'AGENT') {
                        this.addSceneMessage(lastMsg);
                    }
                }
            }
        } catch (e) {
            console.error('[LlmChatFloat] Scene message error:', e);
            this.addSceneMessage({
                id: 'msg-error-' + Date.now(),
                content: '抱歉，消息发送失败，请稍后重试。',
                fromParticipant: { id: 'agent-main', name: '智能助手', type: 'AGENT' },
                createTime: Date.now(),
                messageType: 'SYSTEM'
            });
        } finally {
            this.isStreaming = false;
            this.updateSendButton(false);
        }
    },

    addMessage(role, content) {
        const container = document.getElementById('llmChatMessages');
        if (!container) return;
        
        const msgEl = document.createElement('div');
        msgEl.className = `llm-chat-float-message ${role}`;
        msgEl.innerHTML = `
            <div class="llm-chat-float-message-content">
                ${this.escapeHtml(content)}
            </div>
        `;
        container.appendChild(msgEl);
        container.scrollTop = container.scrollHeight;
    },

    addStreamingMessage(messageId) {
        const container = document.getElementById('llmChatMessages');
        if (!container) return;
        
        const msgEl = document.createElement('div');
        msgEl.id = messageId;
        msgEl.className = 'llm-chat-float-message assistant';
        msgEl.innerHTML = `
            <div class="llm-chat-float-message-content">
                <span class="typing-indicator">正在输入...</span>
            </div>
        `;
        container.appendChild(msgEl);
        container.scrollTop = container.scrollHeight;
    },

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
    },

    addSceneMessage(msg) {
        const container = document.getElementById('llmChatMessages');
        if (!container) return;
        
        const isAgent = msg.fromParticipant?.type === 'AGENT' || msg.fromParticipant?.type === 'SUPER_AGENT';
        const isSystem = msg.messageType === 'SYSTEM';
        
        const msgEl = document.createElement('div');
        msgEl.className = `agent-chat-message ${isAgent ? 'is-agent' : ''} ${isSystem ? 'is-system' : ''}`;
        msgEl.innerHTML = `
            <div class="message-header">
                <span class="message-sender">${msg.fromParticipant?.name || '未知'}</span>
                <span class="message-time">${Utils.formatTime(msg.createTime)}</span>
            </div>
            <div class="message-content">${this.escapeHtml(msg.content)}</div>
        `;
        container.appendChild(msgEl);
        container.scrollTop = container.scrollHeight;
    },

    clearChat() {
        this.messages = [];
        const container = document.getElementById('llmChatMessages');
        if (container) {
            container.innerHTML = '';
        }
    },

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
};

export default ChatMixin;
