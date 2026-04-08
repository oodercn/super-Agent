/**
 * IM 消息窗口
 * 场景组内消息通讯
 * 集成WebSocket实时通信和@提及功能
 */
import { BaseWindow } from './base-window.js';
import { eventBus } from '../core/event-bus.js';
import { wsService } from '../core/websocket-service.js';
import { MentionPopup } from './mention-popup.js';
import { EmojiPicker } from './emoji-picker.js';

export class ImWindow extends BaseWindow {
    constructor(container, options = {}) {
        super(container, {
            id: 'im-window',
            title: 'IM 消息',
            icon: 'ri-message-3-line',
            width: 600,
            height: 600,
            ...options
        });
        
        this.messages = [];
        this.participants = [];
        this.selectedParticipant = null;
        this.currentTab = 'all';
        this.mentionPopup = null;
        this.emojiPicker = null;
        this.typingTimer = null;
        this.isTyping = false;
        this.currentUserId = 'current-user';
    }

    async loadParticipants() {
        if (!this.options.sceneGroupId) return;
        
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.options.sceneGroupId}/participants`);
            const result = await response.json();
            
            if (result.status === 'success' || result.code === 200) {
                const participants = result.data?.list || result.data || [];
                this.participants = participants.map(p => ({
                    id: p.participantId || p.id,
                    name: p.participantName || p.name || '未知',
                    type: p.participantType || p.type || 'USER',
                    status: p.status || 'ONLINE',
                    unreadCount: 0
                }));
                this.renderParticipantsList();
                
                if (this.mentionPopup) {
                    this.mentionPopup.setParticipants(this.participants);
                }
            }
        } catch (e) {
            console.error('[ImWindow] Load participants error:', e);
            this.loadMockParticipants();
        }
    }

    loadMockParticipants() {
        this.participants = [
            { id: 'agent-1', name: '智能助手', type: 'AGENT', status: 'ONLINE', unreadCount: 0 },
            { id: 'agent-2', name: '数据分析Agent', type: 'AGENT', status: 'ONLINE', unreadCount: 0 },
            { id: 'user-1', name: '用户A', type: 'USER', status: 'ONLINE', unreadCount: 0 },
            { id: 'user-2', name: '用户B', type: 'USER', status: 'OFFLINE', unreadCount: 1 }
        ];
        this.renderParticipantsList();
        
        if (this.mentionPopup) {
            this.mentionPopup.setParticipants(this.participants);
        }
    }

    async loadMessages() {
        if (!this.options.sceneGroupId) return;
        
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.options.sceneGroupId}/chat/messages?limit=50`);
            const result = await response.json();
            
            if (result.status === 'success' || result.code === 200) {
                this.messages = result.data?.list || result.data || [];
                this.renderMessagesList();
            }
        } catch (e) {
            console.error('[ImWindow] Load messages error:', e);
        }
    }

    renderContent() {
        return `
            <div class="im-container">
                <div class="im-sidebar">
                    <div class="im-tabs">
                        <div class="im-tab active" data-tab="all">
                            <i class="ri-chat-3-line"></i>
                            <span>全部</span>
                        </div>
                        <div class="im-tab" data-tab="agents">
                            <i class="ri-robot-line"></i>
                            <span>Agent</span>
                        </div>
                        <div class="im-tab" data-tab="users">
                            <i class="ri-user-line"></i>
                            <span>用户</span>
                        </div>
                    </div>
                    <div class="im-participants" id="imParticipants">
                        ${this.renderParticipants()}
                    </div>
                </div>
                <div class="im-main">
                    <div class="im-messages" id="imMessages">
                        ${this.renderMessages()}
                    </div>
                    <div class="im-typing-indicator" id="imTypingIndicator" style="display:none;">
                        <span class="typing-dots">
                            <i class="ri-loader-4-line ri-spin"></i>
                            <span id="typingText">正在输入...</span>
                        </span>
                    </div>
                    <div class="im-input">
                        <div class="im-input-header" id="imInputHeader">
                            <span class="im-target" id="imTarget" style="display:none;"></span>
                            <button class="im-target-clear" id="imTargetClear" style="display:none;">
                                <i class="ri-close-line"></i>
                            </button>
                        </div>
                        <div class="im-input-row">
                            <button class="emoji-trigger" id="emojiTrigger" title="表情">
                                <i class="ri-emotion-line"></i>
                            </button>
                            <textarea id="imInput" placeholder="输入消息... (@提及)" rows="2"></textarea>
                            <button class="im-send-btn" id="imSendBtn">
                                <i class="ri-send-plane-fill"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    renderParticipants() {
        if (this.participants.length === 0) {
            return '<div class="im-empty-sidebar">暂无参与者</div>';
        }
        
        let filtered = this.participants;
        if (this.currentTab === 'agents') {
            filtered = this.participants.filter(p => p.type === 'AGENT');
        } else if (this.currentTab === 'users') {
            filtered = this.participants.filter(p => p.type !== 'AGENT');
        }
        
        if (filtered.length === 0) {
            return '<div class="im-empty-sidebar">暂无匹配的参与者</div>';
        }
        
        return filtered.map(p => {
            const isAgent = p.type === 'AGENT';
            const isSelected = this.selectedParticipant?.id === p.id;
            const isOnline = p.status === 'ONLINE' || isAgent;
            return `
                <div class="im-participant ${isSelected ? 'selected' : ''}" 
                     data-id="${p.id}" data-type="${p.type}">
                    <div class="im-participant-avatar ${isAgent ? 'agent' : ''}">
                        <i class="${isAgent ? 'ri-robot-line' : 'ri-user-line'}"></i>
                        <span class="status-dot ${isOnline ? 'online' : 'offline'}"></span>
                    </div>
                    <div class="im-participant-info">
                        <div class="im-participant-name">${p.name || '未知'}</div>
                        <div class="im-participant-status">${isAgent ? 'Agent' : (isOnline ? '在线' : '离线')}</div>
                    </div>
                    ${p.unreadCount > 0 ? `<span class="im-unread-badge">${p.unreadCount > 99 ? '99+' : p.unreadCount}</span>` : ''}
                </div>
            `;
        }).join('');
    }

    renderMessages() {
        if (this.messages.length === 0) {
            return `
                <div class="im-empty">
                    <i class="ri-message-3-line"></i>
                    <p>暂无消息</p>
                    <span>发送消息开始对话</span>
                </div>
            `;
        }
        
        return this.messages.map(msg => this.renderMessage(msg)).join('');
    }

    renderMessage(msg) {
        const isSelf = msg.fromParticipant?.type === 'USER' && msg.fromParticipant?.id === 'current-user';
        const isAgent = msg.fromParticipant?.type === 'AGENT';
        const senderName = msg.fromParticipant?.name || '未知';
        const status = msg.status || 'DELIVERED';
        
        const statusIcon = this.getStatusIcon(status);
        
        if (isSelf) {
            return `
                <div class="im-message self" data-id="${msg.id}">
                    <div class="im-msg-content">
                        <div class="im-msg-bubble">${this.escapeHtml(msg.content)}</div>
                        <div class="im-msg-meta">
                            <span class="im-msg-time">${this.formatTime(msg.createTime)}</span>
                            <span class="im-msg-status ${status.toLowerCase()}">${statusIcon}</span>
                        </div>
                    </div>
                    <div class="im-msg-avatar self">
                        <i class="ri-user-line"></i>
                    </div>
                </div>
            `;
        } else {
            return `
                <div class="im-message other ${isAgent ? 'agent' : ''}" data-id="${msg.id}">
                    <div class="im-msg-avatar ${isAgent ? 'agent' : ''}">
                        <i class="${isAgent ? 'ri-robot-line' : 'ri-user-line'}"></i>
                    </div>
                    <div class="im-msg-content">
                        <div class="im-msg-sender">${senderName}</div>
                        <div class="im-msg-bubble">${this.escapeHtml(msg.content)}</div>
                        <div class="im-msg-meta">
                            <span class="im-msg-time">${this.formatTime(msg.createTime)}</span>
                        </div>
                    </div>
                </div>
            `;
        }
    }

    getStatusIcon(status) {
        switch (status) {
            case 'SENDING': return '<i class="ri-time-line"></i>';
            case 'DELIVERED': return '<i class="ri-check-line"></i>';
            case 'READ': return '<i class="ri-check-double-line"></i>';
            case 'FAILED': return '<i class="ri-error-warning-line"></i>';
            default: return '<i class="ri-check-line"></i>';
        }
    }

    formatTime(timestamp) {
        if (!timestamp) return '';
        const date = new Date(timestamp);
        return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
    }

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    bindContentEvents() {
        this.addContentStyles();
        
        const sidebar = this.element.querySelector('.im-sidebar');
        const input = this.element.querySelector('#imInput');
        const sendBtn = this.element.querySelector('#imSendBtn');
        const targetClear = this.element.querySelector('#imTargetClear');
        const emojiTrigger = this.element.querySelector('#emojiTrigger');
        
        sidebar.addEventListener('click', (e) => {
            e.stopPropagation();
            
            const tab = e.target.closest('.im-tab');
            if (tab) {
                this.switchTab(tab.dataset.tab);
                return;
            }
            
            const participant = e.target.closest('.im-participant');
            if (participant) {
                this.selectParticipant(participant.dataset.id);
            }
        });
        
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                this.sendMessage();
            }
        });
        
        input.addEventListener('input', () => {
            this.handleTyping();
        });
        
        sendBtn.addEventListener('click', () => this.sendMessage());
        
        targetClear.addEventListener('click', () => this.clearTarget());
        
        if (emojiTrigger) {
            emojiTrigger.addEventListener('click', (e) => {
                e.stopPropagation();
                if (this.emojiPicker) {
                    this.emojiPicker.toggle();
                }
            });
        }
        
        this.initMentionPopup();
        this.initEmojiPicker();
        this.bindWebSocketEvents();
    }

    initEmojiPicker() {
        const input = this.element.querySelector('#imInput');
        const inputContainer = this.element.querySelector('.im-input');
        
        this.emojiPicker = new EmojiPicker({
            container: inputContainer,
            input: input,
            onSelect: (emoji) => {
                this.handleTyping();
            }
        });
    }

    initMentionPopup() {
        const input = this.element.querySelector('#imInput');
        
        this.mentionPopup = new MentionPopup({
            container: this.element,
            input: input,
            participants: this.participants,
            onSelect: (participant) => {
                this.selectedParticipant = participant;
                this.updateTargetDisplay();
            }
        });
    }

    bindWebSocketEvents() {
        eventBus.on('im:message', (data) => {
            if (data.sceneGroupId === this.options.sceneGroupId) {
                this.handleIncomingMessage(data);
            }
        });
        
        eventBus.on('im:typing', (data) => {
            if (data.sceneGroupId === this.options.sceneGroupId) {
                this.showTypingIndicator(data);
            }
        });
        
        eventBus.on('im:participantStatus', (data) => {
            this.updateParticipantStatus(data);
        });
        
        eventBus.on('websocket:connected', () => {
            if (this.options.sceneGroupId) {
                wsService.joinSceneGroup(this.options.sceneGroupId);
            }
        });
    }

    handleTyping() {
        if (!this.isTyping) {
            this.isTyping = true;
            wsService.sendTyping(this.options.sceneGroupId, true);
        }
        
        clearTimeout(this.typingTimer);
        this.typingTimer = setTimeout(() => {
            this.isTyping = false;
            wsService.sendTyping(this.options.sceneGroupId, false);
        }, 2000);
    }

    showTypingIndicator(data) {
        const indicator = this.element.querySelector('#imTypingIndicator');
        const typingText = this.element.querySelector('#typingText');
        
        if (data.isTyping) {
            const participant = this.participants.find(p => p.id === data.participantId);
            typingText.textContent = `${participant?.name || '有人'} 正在输入...`;
            indicator.style.display = 'block';
        } else {
            indicator.style.display = 'none';
        }
    }

    handleIncomingMessage(data) {
        const exists = this.messages.find(m => m.id === data.id);
        if (!exists) {
            this.addMessage(data);
        }
    }

    updateParticipantStatus(data) {
        const participant = this.participants.find(p => p.id === data.participantId);
        if (participant) {
            participant.status = data.status;
            this.renderParticipantsList();
        }
    }

    switchTab(tabId) {
        this.currentTab = tabId;
        const tabs = this.element.querySelectorAll('.im-tab');
        tabs.forEach(t => t.classList.toggle('active', t.dataset.tab === tabId));
        this.renderParticipantsList();
    }

    selectParticipant(id) {
        const participant = this.participants.find(p => p.id === id);
        if (participant) {
            this.selectedParticipant = participant;
            this.updateTargetDisplay();
            this.highlightSelectedParticipant(id);
            participant.unreadCount = 0;
            this.updateTotalUnread();
        }
    }

    highlightSelectedParticipant(id) {
        const items = this.element.querySelectorAll('.im-participant');
        items.forEach(item => {
            item.classList.toggle('selected', item.dataset.id === id);
        });
    }

    updateTargetDisplay() {
        const target = this.element.querySelector('#imTarget');
        const targetClear = this.element.querySelector('#imTargetClear');
        
        if (this.selectedParticipant) {
            target.textContent = `@${this.selectedParticipant.name}`;
            target.style.display = 'inline';
            targetClear.style.display = 'inline';
        } else {
            target.style.display = 'none';
            targetClear.style.display = 'none';
        }
    }

    clearTarget() {
        this.selectedParticipant = null;
        this.updateTargetDisplay();
        this.renderParticipantsList();
    }

    async sendMessage() {
        const input = this.element.querySelector('#imInput');
        const content = input.value.trim();
        
        if (!content) return;
        
        input.value = '';
        
        const msg = {
            id: 'msg-' + Date.now(),
            content: content,
            fromParticipant: { id: 'current-user', name: '我', type: 'USER' },
            createTime: Date.now(),
            status: 'SENDING',
            toParticipantId: this.selectedParticipant?.id
        };
        
        this.messages.push(msg);
        this.renderMessagesList();
        
        const sent = wsService.sendChatMessage(
            this.options.sceneGroupId,
            content,
            this.selectedParticipant?.id
        );
        
        if (sent) {
            msg.status = 'DELIVERED';
        } else {
            try {
                const response = await fetch(`/api/v1/scene-groups/${this.options.sceneGroupId}/chat/messages`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        content: content,
                        toParticipantId: this.selectedParticipant?.id
                    })
                });
                
                const result = await response.json();
                
                if (result.status === 'success' || result.code === 200) {
                    msg.status = 'DELIVERED';
                } else {
                    msg.status = 'FAILED';
                }
            } catch (e) {
                console.error('[ImWindow] Send error:', e);
                msg.status = 'FAILED';
            }
        }
        
        this.renderMessagesList();
    }

    addMessage(msg) {
        const exists = this.messages.find(m => m.id === msg.id);
        if (!exists) {
            this.messages.push(msg);
            this.renderMessagesList();
        }
    }

    setMessages(messages) {
        this.messages = messages || [];
        this.renderMessagesList();
    }

    setParticipants(participants) {
        this.participants = participants || [];
        if (this.mentionPopup) {
            this.mentionPopup.setParticipants(this.participants);
        }
        this.renderParticipantsList();
    }

    updateTotalUnread() {
        const total = this.participants.reduce((sum, p) => sum + (p.unreadCount || 0), 0);
        eventBus.emit('im:unreadUpdate', { count: total });
    }

    connectWebSocket() {
        if (this.options.sceneGroupId) {
            const wsUrl = `ws://${window.location.host}/ws/scene-groups/${this.options.sceneGroupId}/chat`;
            wsService.connectWithToken(this.options.sceneGroupId, wsUrl);
        }
    }

    renderMessagesList() {
        const container = this.element.querySelector('#imMessages');
        container.innerHTML = this.renderMessages();
        container.scrollTop = container.scrollHeight;
    }

    renderParticipantsList() {
        const container = this.element.querySelector('#imParticipants');
        container.innerHTML = this.renderParticipants();
    }

    open() {
        super.open();
        this.loadParticipants();
        this.loadMessages();
    }

    addContentStyles() {
        if (document.getElementById('im-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'im-styles';
        style.textContent = `
            .im-container {
                display: flex;
                height: 100%;
            }
            .im-sidebar {
                width: 160px;
                min-width: 160px;
                background: #f8fafc;
                border-right: 1px solid #e2e8f0;
                display: flex;
                flex-direction: column;
            }
            .im-tabs {
                display: flex;
                flex-direction: column;
                border-bottom: 1px solid #e2e8f0;
            }
            .im-tab {
                display: flex;
                align-items: center;
                gap: 8px;
                padding: 12px 16px;
                cursor: pointer;
                font-size: 13px;
                color: #64748b;
                border-left: 3px solid transparent;
                transition: all 0.2s;
            }
            .im-tab:hover { background: #f1f5f9; }
            .im-tab.active {
                background: white;
                color: #6366f1;
                border-left-color: #6366f1;
            }
            .im-tab i { font-size: 18px; }
            .im-participants {
                flex: 1;
                overflow-y: auto;
                padding: 8px;
            }
            .im-empty-sidebar {
                padding: 24px;
                text-align: center;
                color: #94a3b8;
                font-size: 13px;
            }
            .im-participant {
                display: flex;
                align-items: center;
                gap: 10px;
                padding: 10px;
                border-radius: 10px;
                cursor: pointer;
                margin-bottom: 4px;
                transition: background 0.2s;
            }
            .im-participant:hover { background: #f1f5f9; }
            .im-participant.selected { background: rgba(99, 102, 241, 0.1); }
            .im-participant-avatar {
                position: relative;
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
            .im-participant-avatar.agent {
                background: linear-gradient(135deg, rgba(99, 102, 241, 0.2) 0%, rgba(139, 92, 246, 0.2) 100%);
                color: #6366f1;
            }
            .status-dot {
                position: absolute;
                bottom: 0;
                right: 0;
                width: 10px;
                height: 10px;
                border-radius: 50%;
                border: 2px solid white;
            }
            .status-dot.online { background: #22c55e; }
            .status-dot.offline { background: #94a3b8; }
            .im-participant-info { flex: 1; min-width: 0; }
            .im-participant-name {
                font-size: 13px;
                font-weight: 500;
                color: #334155;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .im-participant-status { font-size: 11px; color: #94a3b8; }
            .im-unread-badge {
                background: #ef4444;
                color: white;
                font-size: 11px;
                padding: 2px 6px;
                border-radius: 10px;
                font-weight: 600;
            }
            .im-main {
                flex: 1;
                display: flex;
                flex-direction: column;
                min-width: 0;
            }
            .im-messages {
                flex: 1;
                overflow-y: auto;
                padding: 16px;
                background: linear-gradient(180deg, #f8fafc 0%, #fff 100%);
            }
            .im-empty {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                height: 100%;
                color: #94a3b8;
            }
            .im-empty i { font-size: 48px; margin-bottom: 16px; opacity: 0.5; }
            .im-empty p { font-size: 16px; margin: 0 0 4px; color: #64748b; }
            .im-empty span { font-size: 13px; }
            .im-message {
                display: flex;
                gap: 12px;
                margin-bottom: 16px;
            }
            .im-message.self { flex-direction: row-reverse; }
            .im-msg-avatar {
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
            .im-msg-avatar.agent,
            .im-msg-avatar.self {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
            }
            .im-msg-content { max-width: 70%; }
            .im-msg-sender {
                font-size: 13px;
                font-weight: 500;
                color: #334155;
                margin-bottom: 4px;
            }
            .im-msg-bubble {
                padding: 12px 16px;
                background: #f1f5f9;
                border-radius: 16px;
                font-size: 14px;
                line-height: 1.5;
                word-break: break-word;
            }
            .im-message.self .im-msg-bubble {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
                border-radius: 16px 16px 4px 16px;
            }
            .im-message.other .im-msg-bubble { border-radius: 16px 16px 16px 4px; }
            .im-message.agent .im-msg-bubble {
                background: linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(139, 92, 246, 0.1) 100%);
                border: 1px solid rgba(99, 102, 241, 0.2);
            }
            .im-msg-meta {
                display: flex;
                align-items: center;
                gap: 4px;
                margin-top: 4px;
            }
            .im-message.self .im-msg-meta { justify-content: flex-end; }
            .im-msg-time { font-size: 11px; color: #94a3b8; }
            .im-msg-status { font-size: 12px; color: #94a3b8; }
            .im-msg-status.delivered { color: #22c55e; }
            .im-msg-status.read { color: #6366f1; }
            .im-msg-status.failed { color: #ef4444; }
            .im-typing-indicator {
                padding: 8px 16px;
                background: #f8fafc;
                border-top: 1px solid #e2e8f0;
            }
            .typing-dots {
                display: flex;
                align-items: center;
                gap: 8px;
                font-size: 13px;
                color: #64748b;
            }
            .im-input {
                padding: 16px;
                background: white;
                border-top: 1px solid #e2e8f0;
            }
            .im-input-header {
                display: flex;
                align-items: center;
                gap: 8px;
                margin-bottom: 8px;
                min-height: 24px;
            }
            .im-target {
                padding: 4px 12px;
                background: rgba(99, 102, 241, 0.1);
                border-radius: 20px;
                font-size: 13px;
                color: #6366f1;
            }
            .im-target-clear {
                background: none;
                border: none;
                cursor: pointer;
                color: #94a3b8;
                padding: 4px;
                transition: color 0.2s;
            }
            .im-target-clear:hover { color: #6366f1; }
            .im-input-row {
                display: flex;
                gap: 12px;
                align-items: flex-end;
            }
            .emoji-trigger {
                width: 44px;
                height: 44px;
                min-width: 44px;
                border-radius: 50%;
                background: #f1f5f9;
                color: #64748b;
                border: none;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 20px;
                transition: all 0.2s;
            }
            .emoji-trigger:hover {
                background: #e2e8f0;
                color: #6366f1;
            }
            #imInput {
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
            #imInput:focus {
                outline: none;
                border-color: #6366f1;
                background: white;
                box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
            }
            .im-send-btn {
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
                transition: transform 0.2s, box-shadow 0.2s;
            }
            .im-send-btn:hover { 
                transform: scale(1.05); 
                box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
            }
            .im-send-btn:active { transform: scale(0.95); }
        `;
        document.head.appendChild(style);
    }

    destroy() {
        if (this.mentionPopup) {
            this.mentionPopup.destroy();
        }
        if (this.emojiPicker) {
            this.emojiPicker.destroy();
        }
        if (this.options.sceneGroupId) {
            wsService.leaveSceneGroup(this.options.sceneGroupId);
        }
        super.destroy();
    }
}

export default ImWindow;
