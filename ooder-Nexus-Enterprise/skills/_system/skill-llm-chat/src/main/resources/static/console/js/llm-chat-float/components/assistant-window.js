/**
 * 智能助手窗口
 * 提供AI对话功能
 * 集成流式输出和Markdown渲染
 */
import { BaseWindow } from './base-window.js';
import { eventBus } from '../core/event-bus.js';
import { streamingService } from '../core/streaming-service.js';
import { markdownService } from '../core/markdown-service.js';

export class AssistantWindow extends BaseWindow {
    constructor(container, options = {}) {
        super(container, {
            id: 'assistant-window',
            title: '智能助手',
            icon: 'ri-robot-line',
            width: 420,
            height: 560,
            ...options
        });
        
        this.messages = [];
        this.isStreaming = false;
        this.currentStreamId = null;
        this.sessionId = 'session-' + Date.now();
        this.sessions = [];
    }

    async loadSessions() {
        try {
            const response = await fetch('/api/v1/llm/monitor/sessions?limit=20');
            const result = await response.json();
            
            if (result.status === 'success' && result.data) {
                this.sessions = result.data;
                this.renderSessionSelector();
            }
        } catch (e) {
            console.error('[AssistantWindow] Load sessions error:', e);
        }
    }

    renderSessionSelector() {
        const select = this.element.querySelector('#assistantSession');
        if (!select) return;
        
        select.innerHTML = '<option value="new">+ 新建会话</option>';
        
        this.sessions.forEach(session => {
            const option = document.createElement('option');
            option.value = session.sessionId;
            const preview = session.firstPrompt ? 
                (session.firstPrompt.length > 30 ? session.firstPrompt.substring(0, 30) + '...' : session.firstPrompt) 
                : '新会话';
            const date = new Date(session.lastTime).toLocaleDateString('zh-CN');
            option.textContent = `${preview} (${session.messageCount}条) - ${date}`;
            if (session.sessionId === this.sessionId) {
                option.selected = true;
            }
            select.appendChild(option);
        });
    }

    async switchSession(sessionId) {
        if (sessionId === 'new') {
            this.sessionId = 'session-' + Date.now();
            this.messages = [];
            this.renderMessagesList();
        } else {
            this.sessionId = sessionId;
            await this.loadHistory();
        }
    }

    async loadHistory() {
        try {
            const response = await fetch(`/api/v1/llm/monitor/logs/session/${this.sessionId}`);
            const result = await response.json();
            
            if (result.status === 'success' && result.data) {
                this.messages = result.data.map(log => ({
                    id: log.logId,
                    role: 'user',
                    content: log.prompt,
                    time: new Date(log.createTime).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
                }));
                
                for (let i = 0; i < result.data.length; i++) {
                    const log = result.data[i];
                    if (log.response) {
                        this.messages.push({
                            id: log.logId + '-response',
                            role: 'assistant',
                            content: log.response,
                            time: new Date(log.createTime).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
                        });
                    }
                }
                
                this.renderMessagesList();
            }
        } catch (e) {
            console.error('[AssistantWindow] Load history error:', e);
        }
    }

    open() {
        super.open();
        this.loadSessions();
        this.loadHistory();
    }

    renderContent() {
        return `
            <div class="assistant-container">
                <div class="assistant-header">
                    <div class="assistant-session-selector">
                        <select id="assistantSession">
                            <option value="new">新建会话</option>
                        </select>
                    </div>
                    <div class="assistant-model-selector">
                        <select id="assistantModel">
                        <option value="qpt-3.5-turbo">GPT-3.5 Turbo</option>
                        <option value="claude-3">Claude 3</option>
                        <option value="qwen-plus" selected>Qwen Plus</option>
                        <option value="qwen-turbo">Qwen Turbo</option>
                        <option value="deepseek-chat">DeepSeek</option>
                    </select>
                    </div>
                    <button class="assistant-clear-btn" id="assistantClearBtn" title="清空对话">
                        <i class="ri-delete-bin-line"></i>
                    </button>
                </div>
                <div class="assistant-messages" id="assistantMessages">
                    ${this.renderMessages()}
                </div>
                <div class="assistant-input">
                    <textarea id="assistantInput" placeholder="输入消息... (Shift+Enter换行)" rows="2"></textarea>
                    <button class="send-btn" id="assistantSendBtn">
                        <i class="ri-send-plane-fill"></i>
                    </button>
                </div>
            </div>
        `;
    }

    renderMessages() {
        if (this.messages.length === 0) {
            return `
                <div class="assistant-empty">
                    <i class="ri-robot-line"></i>
                    <p>智能助手已就绪</p>
                    <span>输入消息开始对话</span>
                </div>
            `;
        }
        
        return this.messages.map(msg => this.renderMessage(msg)).join('');
    }

    renderMessage(msg) {
        const isUser = msg.role === 'user';
        const content = isUser ? this.escapeHtml(msg.content) : markdownService.render(msg.content);
        
        return `
            <div class="assistant-message ${isUser ? 'user' : 'assistant'}" data-id="${msg.id}">
                <div class="msg-avatar">
                    <i class="${isUser ? 'ri-user-line' : 'ri-robot-line'}"></i>
                </div>
                <div class="msg-content">
                    <div class="msg-bubble">${content}</div>
                    <div class="msg-footer">
                        <span class="msg-time">${msg.time}</span>
                        ${!isUser ? `
                            <div class="msg-actions">
                                <button class="msg-action-btn copy-btn" title="复制">
                                    <i class="ri-file-copy-line"></i>
                                </button>
                                <button class="msg-action-btn regenerate-btn" title="重新生成">
                                    <i class="ri-refresh-line"></i>
                                </button>
                            </div>
                        ` : ''}
                    </div>
                </div>
            </div>
        `;
    }

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    bindContentEvents() {
        this.addContentStyles();
        
        const input = this.element.querySelector('#assistantInput');
        const sendBtn = this.element.querySelector('#assistantSendBtn');
        const clearBtn = this.element.querySelector('#assistantClearBtn');
        const sessionSelect = this.element.querySelector('#assistantSession');
        
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                this.sendMessage();
            }
        });
        
        sendBtn.addEventListener('click', () => this.sendMessage());
        clearBtn.addEventListener('click', () => this.clearChat());
        
        if (sessionSelect) {
            sessionSelect.addEventListener('change', (e) => {
                this.switchSession(e.target.value);
            });
        }
        
        this.element.addEventListener('click', (e) => {
            const copyBtn = e.target.closest('.copy-btn');
            if (copyBtn) {
                const msgEl = copyBtn.closest('.assistant-message');
                const msg = this.messages.find(m => m.id === msgEl?.dataset.id);
                if (msg) {
                    navigator.clipboard.writeText(msg.content);
                    this.showToast('已复制到剪贴板');
                }
            }
            
            const regenerateBtn = e.target.closest('.regenerate-btn');
            if (regenerateBtn) {
                const msgEl = regenerateBtn.closest('.assistant-message');
                const msgIndex = this.messages.findIndex(m => m.id === msgEl?.dataset.id);
                if (msgIndex > 0) {
                    const userMsg = this.messages[msgIndex - 1];
                    if (userMsg?.role === 'user') {
                        this.regenerateResponse(userMsg.content, msgIndex);
                    }
                }
            }
        });
        
        markdownService.loadHighlightJs();
        markdownService.addStyles();
    }

    async sendMessage() {
        const input = this.element.querySelector('#assistantInput');
        const content = input.value.trim();
        
        if (!content || this.isStreaming) return;
        
        input.value = '';
        
        this.addMessage({
            id: 'msg-' + Date.now(),
            role: 'user',
            content: content,
            time: new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
        });
        
        this.isStreaming = true;
        this.updateSendButton(true);
        
        const assistantMsgId = 'msg-assistant-' + Date.now();
        this.addStreamingMessage(assistantMsgId);
        
        try {
            const model = this.element.querySelector('#assistantModel')?.value || 'qwen-plus';
            
            const historyMessages = this.messages.slice(0, -1).map(m => ({
                role: m.role,
                content: m.content
            }));
            
            await streamingService.streamChat({
                url: '/api/v1/llm/chat/stream',
                message: content,
                sessionId: this.sessionId,
                model: model,
                history: historyMessages,
                onChunk: (chunk, fullContent) => {
                    this.updateStreamingMessage(assistantMsgId, fullContent);
                },
                onComplete: (fullContent) => {
                    this.finalizeMessage(assistantMsgId, fullContent);
                    this.isStreaming = false;
                    this.updateSendButton(false);
                },
                onError: (error) => {
                    console.error('[AssistantWindow] Stream error:', error);
                    this.updateStreamingMessage(assistantMsgId, '抱歉，发生错误，请稍后重试。');
                    this.finalizeMessage(assistantMsgId, '抱歉，发生错误，请稍后重试。');
                    this.isStreaming = false;
                    this.updateSendButton(false);
                }
            });
            
        } catch (e) {
            console.error('[AssistantWindow] Error:', e);
            
            try {
                const historyMessages = this.messages.map(m => ({
                    role: m.role,
                    content: m.content
                }));
                
                const response = await fetch('/api/v1/llm/chat', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ 
                        message: content,
                        sessionId: this.sessionId,
                        history: historyMessages
                    })
                });
                
                const result = await response.json();
                
                if (result.code === 200 || result.status === 'success') {
                    const responseContent = result.data?.content || result.data?.message || '收到';
                    this.updateStreamingMessage(assistantMsgId, responseContent);
                    this.finalizeMessage(assistantMsgId, responseContent);
                } else {
                    this.updateStreamingMessage(assistantMsgId, '抱歉，未收到有效回复。');
                    this.finalizeMessage(assistantMsgId, '抱歉，未收到有效回复。');
                }
            } catch (fallbackError) {
                this.updateStreamingMessage(assistantMsgId, '抱歉，网络连接失败，请稍后重试。');
                this.finalizeMessage(assistantMsgId, '抱歉，网络连接失败，请稍后重试。');
            }
            
            this.isStreaming = false;
            this.updateSendButton(false);
        }
    }

    async regenerateResponse(content, fromIndex) {
        if (this.isStreaming) return;
        
        this.messages = this.messages.slice(0, fromIndex);
        this.renderMessagesList();
        
        this.isStreaming = true;
        this.updateSendButton(true);
        
        const assistantMsgId = 'msg-assistant-' + Date.now();
        this.addStreamingMessage(assistantMsgId);
        
        try {
            await streamingService.streamChat({
                url: '/api/v1/llm/chat/stream',
                message: content,
                sessionId: this.sessionId,
                onChunk: (chunk, fullContent) => {
                    this.updateStreamingMessage(assistantMsgId, fullContent);
                },
                onComplete: (fullContent) => {
                    this.finalizeMessage(assistantMsgId, fullContent);
                    this.isStreaming = false;
                    this.updateSendButton(false);
                },
                onError: (error) => {
                    this.updateStreamingMessage(assistantMsgId, '抱歉，重新生成失败。');
                    this.finalizeMessage(assistantMsgId, '抱歉，重新生成失败。');
                    this.isStreaming = false;
                    this.updateSendButton(false);
                }
            });
        } catch (e) {
            this.updateStreamingMessage(assistantMsgId, '抱歉，发生错误。');
            this.finalizeMessage(assistantMsgId, '抱歉，发生错误。');
            this.isStreaming = false;
            this.updateSendButton(false);
        }
    }

    addMessage(msg) {
        this.messages.push(msg);
        
        const container = this.element.querySelector('#assistantMessages');
        if (this.messages.length === 1) {
            container.innerHTML = this.renderMessages();
        } else {
            container.insertAdjacentHTML('beforeend', this.renderMessage(msg));
        }
        
        container.scrollTop = container.scrollHeight;
    }

    addStreamingMessage(messageId) {
        const container = this.element.querySelector('#assistantMessages');
        
        const msgEl = document.createElement('div');
        msgEl.id = messageId;
        msgEl.className = 'assistant-message assistant streaming';
        msgEl.innerHTML = `
            <div class="msg-avatar">
                <i class="ri-robot-line"></i>
            </div>
            <div class="msg-content">
                <div class="msg-bubble">
                    <span class="streaming-cursor">
                        <span class="cursor-dot"></span>
                    </span>
                </div>
            </div>
        `;
        container.appendChild(msgEl);
        container.scrollTop = container.scrollHeight;
    }

    updateStreamingMessage(messageId, content) {
        const msgEl = this.element.querySelector(`#${messageId}`);
        if (!msgEl) return;
        
        const bubble = msgEl.querySelector('.msg-bubble');
        if (bubble) {
            bubble.innerHTML = markdownService.render(content) + '<span class="streaming-cursor"><span class="cursor-dot"></span></span>';
        }
        
        const container = this.element.querySelector('#assistantMessages');
        if (container) {
            container.scrollTop = container.scrollHeight;
        }
    }

    finalizeMessage(messageId, content) {
        const msgEl = this.element.querySelector(`#${messageId}`);
        if (!msgEl) return;
        
        msgEl.classList.remove('streaming');
        
        const msg = {
            id: messageId,
            role: 'assistant',
            content: content,
            time: new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
        };
        
        this.messages.push(msg);
        
        msgEl.outerHTML = this.renderMessage(msg);
        
        const container = this.element.querySelector('#assistantMessages');
        if (container) {
            container.scrollTop = container.scrollHeight;
        }
    }

    updateSendButton(loading) {
        const btn = this.element.querySelector('#assistantSendBtn');
        btn.disabled = loading;
        btn.innerHTML = loading 
            ? '<i class="ri-loader-4-line ri-spin"></i>'
            : '<i class="ri-send-plane-fill"></i>';
    }

    clearChat() {
        this.messages = [];
        this.sessionId = 'session-' + Date.now();
        
        const container = this.element.querySelector('#assistantMessages');
        container.innerHTML = `
            <div class="assistant-empty">
                <i class="ri-robot-line"></i>
                <p>智能助手已就绪</p>
                <span>输入消息开始对话</span>
            </div>
        `;
    }

    renderMessagesList() {
        const container = this.element.querySelector('#assistantMessages');
        container.innerHTML = this.renderMessages();
        container.scrollTop = container.scrollHeight;
    }

    showToast(message) {
        const toast = document.createElement('div');
        toast.className = 'assistant-toast';
        toast.textContent = message;
        document.body.appendChild(toast);
        
        setTimeout(() => {
            toast.classList.add('show');
        }, 10);
        
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 2000);
    }

    addContentStyles() {
        if (document.getElementById('assistant-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'assistant-styles';
        style.textContent = `
            .assistant-container {
                display: flex;
                flex-direction: column;
                height: 100%;
            }
            .assistant-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 8px 16px;
                border-bottom: 1px solid #e2e8f0;
                background: #f8fafc;
            }
            .assistant-session-selector {
                flex: 1;
                margin-right: 8px;
            }
            .assistant-session-selector select {
                width: 100%;
                padding: 6px 8px;
                border: 1px solid #e2e8f0;
                border-radius: 6px;
                font-size: 12px;
                background: white;
                cursor: pointer;
                max-width: 140px;
            }
            .assistant-session-selector select:focus {
                outline: none;
                border-color: #6366f1;
            }
            .assistant-model-selector select {
                padding: 6px 12px;
                border: 1px solid #e2e8f0;
                border-radius: 6px;
                font-size: 13px;
                background: white;
                cursor: pointer;
            }
            .assistant-model-selector select:focus {
                outline: none;
                border-color: #6366f1;
            }
            .assistant-clear-btn {
                background: none;
                border: none;
                color: #94a3b8;
                cursor: pointer;
                padding: 6px;
                border-radius: 6px;
                transition: all 0.2s;
            }
            .assistant-clear-btn:hover {
                background: #f1f5f9;
                color: #ef4444;
            }
            .assistant-messages {
                flex: 1;
                overflow-y: auto;
                padding: 16px;
                background: linear-gradient(180deg, #f8fafc 0%, #fff 100%);
            }
            .assistant-empty {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                height: 100%;
                color: #94a3b8;
            }
            .assistant-empty i { font-size: 48px; margin-bottom: 16px; opacity: 0.5; }
            .assistant-empty p { font-size: 16px; margin: 0 0 4px; color: #64748b; }
            .assistant-empty span { font-size: 13px; }
            .assistant-message {
                display: flex;
                gap: 12px;
                margin-bottom: 16px;
            }
            .assistant-message.user { flex-direction: row-reverse; }
            .msg-avatar {
                width: 36px;
                height: 36px;
                min-width: 36px;
                border-radius: 50%;
                background: #e2e8f0;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #64748b;
            }
            .assistant-message.user .msg-avatar {
                background: #6366f1;
                color: white;
            }
            .msg-content { max-width: 80%; }
            .msg-bubble {
                padding: 12px 16px;
                background: #f1f5f9;
                border-radius: 16px;
                font-size: 14px;
                line-height: 1.6;
            }
            .assistant-message.user .msg-bubble {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
                border-radius: 16px 16px 4px 16px;
            }
            .assistant-message:not(.user) .msg-bubble {
                border-radius: 16px 16px 16px 4px;
            }
            .msg-footer {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-top: 4px;
            }
            .msg-time {
                font-size: 11px;
                color: #94a3b8;
            }
            .msg-actions {
                display: flex;
                gap: 4px;
                opacity: 0;
                transition: opacity 0.2s;
            }
            .assistant-message:hover .msg-actions {
                opacity: 1;
            }
            .msg-action-btn {
                background: none;
                border: none;
                color: #94a3b8;
                cursor: pointer;
                padding: 4px;
                border-radius: 4px;
                font-size: 14px;
                transition: all 0.2s;
            }
            .msg-action-btn:hover {
                background: #f1f5f9;
                color: #6366f1;
            }
            .streaming-cursor {
                display: inline-block;
                margin-left: 2px;
            }
            .cursor-dot {
                display: inline-block;
                width: 8px;
                height: 8px;
                background: #6366f1;
                border-radius: 50%;
                animation: blink 1s infinite;
            }
            @keyframes blink {
                0%, 50% { opacity: 1; }
                51%, 100% { opacity: 0; }
            }
            .assistant-input {
                padding: 16px;
                background: white;
                border-top: 1px solid #e2e8f0;
                display: flex;
                gap: 12px;
            }
            .assistant-input textarea {
                flex: 1;
                padding: 12px 16px;
                border: 1px solid #e2e8f0;
                border-radius: 24px;
                resize: none;
                font-size: 14px;
                font-family: inherit;
                background: #f8fafc;
                transition: all 0.2s;
            }
            .assistant-input textarea:focus {
                outline: none;
                border-color: #6366f1;
                background: white;
                box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
            }
            .send-btn {
                width: 44px;
                height: 44px;
                min-width: 44px;
                border-radius: 50%;
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
                border: none;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 18px;
                transition: all 0.2s;
            }
            .send-btn:hover { 
                transform: scale(1.05); 
                box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
            }
            .send-btn:disabled { 
                opacity: 0.6; 
                cursor: not-allowed;
                transform: none;
            }
            .assistant-toast {
                position: fixed;
                bottom: 100px;
                left: 50%;
                transform: translateX(-50%) translateY(20px);
                background: #334155;
                color: white;
                padding: 10px 20px;
                border-radius: 8px;
                font-size: 14px;
                opacity: 0;
                transition: all 0.3s;
                z-index: 10000;
            }
            .assistant-toast.show {
                opacity: 1;
                transform: translateX(-50%) translateY(0);
            }
        `;
        document.head.appendChild(style);
    }
}

export default AssistantWindow;
