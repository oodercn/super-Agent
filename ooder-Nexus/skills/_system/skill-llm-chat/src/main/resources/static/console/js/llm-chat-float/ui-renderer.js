/**
 * Agent Chat UI 渲染模块
 * 现代化 IM 风格设计
 * 支持拖拽、历史消息、双向记录
 */
import { Utils } from './utils.js';

export const UIRendererMixin = {
    render() {
        let container = document.getElementById('agent-chat-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'agent-chat-container';
            document.body.appendChild(container);
        }
        
        if (this.isSceneMode()) {
            container.innerHTML = this.renderSceneModeUI();
        } else {
            container.innerHTML = this.renderNormalModeUI();
        }
        
        this.addModernStyles();
        this.initDraggable();
    },

    renderNormalModeUI() {
        return `
            <div class="chat-fab" id="llmChatFloatBtn" title="AI助手">
                <i class="ri-robot-2-line"></i>
                <span class="chat-fab-badge" id="llmChatBadge" style="display: none;">0</span>
            </div>
            <div class="chat-window" id="llmChatFloatWindow">
                <div class="chat-window-header" id="chatWindowHeader">
                    <div class="chat-window-title">
                        <i class="ri-robot-2-line"></i>
                        <span>AI 助手</span>
                    </div>
                    <div class="chat-window-actions">
                        <button class="chat-action-btn" id="clearBtn" title="清空对话">
                            <i class="ri-delete-bin-line"></i>
                        </button>
                        <button class="chat-action-btn" id="closeBtn" title="关闭">
                            <i class="ri-close-line"></i>
                        </button>
                    </div>
                </div>
                <div class="chat-messages" id="llmChatMessages"></div>
                <div class="chat-input-box">
                    <textarea id="llmChatInput" placeholder="输入消息..." rows="2"></textarea>
                    <button class="chat-send-btn" id="llmChatSendBtn">
                        <i class="ri-send-plane-fill"></i>
                    </button>
                </div>
            </div>
        `;
    },

    renderSceneModeUI() {
        const sceneName = this.sceneContext?.name || '';
        return `
            <div class="chat-fab" id="llmChatFloatBtn" title="Agent Chat">
                <i class="ri-robot-2-line"></i>
                <span class="chat-fab-badge" id="llmChatBadge" style="display: none;">0</span>
            </div>
            <div class="chat-window scene-mode" id="llmChatFloatWindow">
                <div class="chat-window-header" id="chatWindowHeader">
                    <div class="chat-window-title">
                        <i class="ri-robot-2-line"></i>
                        <span>Agent Chat</span>
                        ${sceneName ? `<span class="chat-scene-badge">${sceneName}</span>` : ''}
                    </div>
                    <div class="chat-window-actions">
                        <button class="chat-action-btn" id="historyBtn" title="历史消息">
                            <i class="ri-history-line"></i>
                        </button>
                        <button class="chat-action-btn" id="closeBtn" title="关闭">
                            <i class="ri-close-line"></i>
                        </button>
                    </div>
                </div>
                <div class="chat-body">
                    <div class="chat-sidebar">
                        <div class="chat-tabs">
                            <div class="chat-tab active" data-tab="all">
                                <i class="ri-chat-3-line"></i>
                                <span>全部</span>
                            </div>
                            <div class="chat-tab" data-tab="agents">
                                <i class="ri-robot-line"></i>
                                <span>Agent</span>
                            </div>
                            <div class="chat-tab" data-tab="users">
                                <i class="ri-user-line"></i>
                                <span>用户</span>
                            </div>
                            <div class="chat-tab" data-tab="todos">
                                <i class="ri-task-line"></i>
                                <span>待办</span>
                            </div>
                        </div>
                        <div class="chat-sidebar-content" id="sidebarContent"></div>
                    </div>
                    <div class="chat-main">
                        <div class="chat-messages" id="llmChatMessages"></div>
                        <div class="chat-input-box">
                            <div class="chat-input-header" id="chatInputHeader">
                                <span class="chat-target-badge" id="chatTarget" style="display:none;"></span>
                                <button class="chat-target-clear" id="clearTargetBtn" style="display:none;">
                                    <i class="ri-close-line"></i>
                                </button>
                            </div>
                            <div class="chat-input-row">
                                <textarea id="llmChatInput" placeholder="输入消息... (@提及)" rows="2"></textarea>
                                <button class="chat-send-btn" id="llmChatSendBtn">
                                    <i class="ri-send-plane-fill"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
    },

    initDraggable() {
        const header = document.getElementById('chatWindowHeader');
        const windowEl = document.getElementById('llmChatFloatWindow');
        if (!header || !windowEl) return;

        let isDragging = false;
        let offsetX = 0;
        let offsetY = 0;

        header.style.cursor = 'move';
        
        header.addEventListener('mousedown', (e) => {
            if (e.target.closest('.chat-action-btn')) return;
            isDragging = true;
            const rect = windowEl.getBoundingClientRect();
            offsetX = e.clientX - rect.left;
            offsetY = e.clientY - rect.top;
            windowEl.style.transition = 'none';
        });

        document.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            const x = e.clientX - offsetX;
            const y = e.clientY - offsetY;
            const maxX = window.innerWidth - windowEl.offsetWidth - 10;
            const maxY = window.innerHeight - windowEl.offsetHeight - 10;
            windowEl.style.left = Math.max(10, Math.min(x, maxX)) + 'px';
            windowEl.style.top = Math.max(10, Math.min(y, maxY)) + 'px';
            windowEl.style.right = 'auto';
            windowEl.style.bottom = 'auto';
        });

        document.addEventListener('mouseup', () => {
            isDragging = false;
            windowEl.style.transition = 'box-shadow 0.2s';
        });
    },

    renderSceneMessages() {
        const container = document.getElementById('llmChatMessages');
        if (!container) return;

        const messages = this.filterMessagesByTab();
        
        if (messages.length === 0) {
            container.innerHTML = `
                <div class="chat-empty-state">
                    <i class="ri-chat-3-line"></i>
                    <p>暂无消息</p>
                    <span>发送消息开始对话</span>
                </div>
            `;
            return;
        }
        
        let html = '';
        let lastDate = '';
        
        messages.forEach(msg => {
            const msgDate = Utils.formatDate(msg.createTime);
            if (msgDate !== lastDate) {
                html += `<div class="chat-date-divider"><span>${msgDate}</span></div>`;
                lastDate = msgDate;
            }
            html += this.renderMessageItem(msg);
        });
        
        container.innerHTML = html;
        container.scrollTop = container.scrollHeight;
    },

    renderMessageItem(msg) {
        const isAgent = msg.fromParticipant?.type === 'AGENT' || msg.fromParticipant?.type === 'SUPER_AGENT';
        const isSelf = msg.fromParticipant?.type === 'USER' && msg.fromParticipant?.id === 'current-user';
        const senderName = msg.fromParticipant?.name || '未知';
        const time = Utils.formatTime(msg.createTime);
        
        if (isSelf) {
            return `
                <div class="chat-message self">
                    <div class="msg-content">
                        <div class="msg-bubble">${Utils.escapeHtml(msg.content)}</div>
                        <div class="msg-meta">
                            <span class="msg-time">${time}</span>
                            <span class="msg-status ${msg.status?.toLowerCase() || 'sent'}">
                                ${msg.status === 'SENDING' ? '<i class="ri-time-line"></i>' : 
                                  msg.status === 'FAILED' ? '<i class="ri-error-warning-line"></i>' : 
                                  '<i class="ri-check-double-line"></i>'}
                            </span>
                        </div>
                    </div>
                    <div class="msg-avatar self-avatar">
                        <i class="ri-user-line"></i>
                    </div>
                </div>
            `;
        } else {
            return `
                <div class="chat-message other ${isAgent ? 'agent' : ''}">
                    <div class="msg-avatar ${isAgent ? 'agent-avatar' : ''}">
                        <i class="${isAgent ? 'ri-robot-line' : 'ri-user-line'}"></i>
                    </div>
                    <div class="msg-content">
                        <div class="msg-sender">${senderName}</div>
                        <div class="msg-bubble">${Utils.escapeHtml(msg.content)}</div>
                        <div class="msg-meta">
                            <span class="msg-time">${time}</span>
                        </div>
                    </div>
                </div>
            `;
        }
    },

    renderParticipants() {
        const container = document.getElementById('sidebarContent');
        if (!container) return;

        const participants = this.sceneParticipants || [];
        if (participants.length === 0) {
            container.innerHTML = `
                <div class="sidebar-empty">
                    <i class="ri-team-line"></i>
                    <p>暂无参与者</p>
                </div>
            `;
            return;
        }

        let html = '<div class="participant-list">';
        participants.forEach(p => {
            const isAgent = p.type === 'AGENT' || p.type === 'SUPER_AGENT';
            const isSelected = this.selectedParticipant?.id === p.id;
            html += `
                <div class="participant-item ${isSelected ? 'selected' : ''}" data-id="${p.id || ''}" data-type="${p.type || 'USER'}">
                    <div class="participant-avatar ${isAgent ? 'agent' : ''}">
                        <i class="${isAgent ? 'ri-robot-line' : 'ri-user-line'}"></i>
                        <span class="online-dot"></span>
                    </div>
                    <div class="participant-info">
                        <div class="participant-name">${p.name || '未知'}</div>
                        <div class="participant-status">${isAgent ? 'Agent' : '在线'}</div>
                    </div>
                </div>
            `;
        });
        html += '</div>';
        container.innerHTML = html;
    },

    renderTodos() {
        const container = document.getElementById('sidebarContent');
        if (!container || this.currentTab !== 'todos') return;

        const todos = this.sceneTodos || [];
        if (todos.length === 0) {
            container.innerHTML = `
                <div class="sidebar-empty">
                    <i class="ri-task-line"></i>
                    <p>暂无待办事项</p>
                </div>
            `;
            return;
        }

        let html = '<div class="todo-list">';
        todos.forEach(todo => {
            const priority = (todo.priority || 'LOW').toLowerCase();
            html += `
                <div class="todo-card" data-id="${todo.id || ''}">
                    <div class="todo-header">
                        <span class="todo-priority ${priority}">${todo.priority || 'LOW'}</span>
                        <span class="todo-status">${todo.status || 'PENDING'}</span>
                    </div>
                    <div class="todo-title">${todo.title || todo.content || '无标题'}</div>
                    ${todo.description ? `<div class="todo-desc">${todo.description}</div>` : ''}
                    <div class="todo-actions">
                        <button class="todo-btn accept" data-action="accept">
                            <i class="ri-check-line"></i> 接受
                        </button>
                        <button class="todo-btn reject" data-action="reject">
                            <i class="ri-close-line"></i> 拒绝
                        </button>
                    </div>
                </div>
            `;
        });
        html += '</div>';
        container.innerHTML = html;
    },

    addModernStyles() {
        if (document.getElementById('agent-chat-modern-styles')) return;

        const style = document.createElement('style');
        style.id = 'agent-chat-modern-styles';
        style.textContent = `
            /* ========== 悬浮按钮 ========== */
            .chat-fab {
                position: fixed;
                bottom: 24px;
                right: 24px;
                width: 60px;
                height: 60px;
                border-radius: 50%;
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                border: none;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                box-shadow: 0 4px 20px rgba(99, 102, 241, 0.4);
                z-index: 9999;
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            }
            .chat-fab:hover {
                transform: scale(1.1);
                box-shadow: 0 6px 24px rgba(99, 102, 241, 0.5);
            }
            .chat-fab i { color: white; font-size: 26px; }
            .chat-fab-badge {
                position: absolute;
                top: -4px;
                right: -4px;
                background: #ef4444;
                color: white;
                font-size: 11px;
                min-width: 20px;
                height: 20px;
                border-radius: 10px;
                display: none;
                align-items: center;
                justify-content: center;
                font-weight: 600;
                border: 2px solid white;
            }

            /* ========== 聊天窗口 ========== */
            .chat-window {
                position: fixed;
                bottom: 100px;
                right: 24px;
                width: 420px;
                height: 560px;
                background: #ffffff;
                border-radius: 16px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.12);
                display: none;
                flex-direction: column;
                z-index: 9998;
                overflow: hidden;
                border: 1px solid rgba(0, 0, 0, 0.06);
            }
            .chat-window.open {
                display: flex;
                animation: chatSlideUp 0.3s ease;
            }
            .chat-window.scene-mode {
                width: 600px;
                height: 600px;
            }
            @keyframes chatSlideUp {
                from { opacity: 0; transform: translateY(20px) scale(0.95); }
                to { opacity: 1; transform: translateY(0) scale(1); }
            }

            /* ========== 窗口头部 ========== */
            .chat-window-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 16px 20px;
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
                flex-shrink: 0;
                user-select: none;
            }
            .chat-window-title {
                display: flex;
                align-items: center;
                gap: 10px;
                font-weight: 600;
                font-size: 16px;
            }
            .chat-window-title i { font-size: 20px; }
            .chat-scene-badge {
                font-size: 12px;
                padding: 3px 10px;
                background: rgba(255, 255, 255, 0.2);
                border-radius: 20px;
                font-weight: 500;
            }
            .chat-window-actions { display: flex; gap: 8px; }
            .chat-action-btn {
                background: rgba(255, 255, 255, 0.15);
                border: none;
                color: white;
                width: 32px;
                height: 32px;
                border-radius: 8px;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 18px;
                transition: background 0.2s;
            }
            .chat-action-btn:hover { background: rgba(255, 255, 255, 0.25); }

            /* ========== 主体区域 ========== */
            .chat-body {
                display: flex;
                flex: 1;
                min-height: 0;
                overflow: hidden;
            }

            /* ========== 侧边栏 ========== */
            .chat-sidebar {
                width: 180px;
                background: #f8fafc;
                border-right: 1px solid #e2e8f0;
                display: flex;
                flex-direction: column;
            }
            .chat-tabs {
                display: flex;
                flex-direction: column;
                border-bottom: 1px solid #e2e8f0;
                flex-shrink: 0;
            }
            .chat-tab {
                display: flex;
                align-items: center;
                gap: 8px;
                padding: 12px 16px;
                cursor: pointer;
                font-size: 13px;
                color: #64748b;
                transition: all 0.2s;
                border-left: 3px solid transparent;
            }
            .chat-tab:hover { background: #f1f5f9; }
            .chat-tab.active {
                background: #fff;
                color: #6366f1;
                border-left-color: #6366f1;
                font-weight: 500;
            }
            .chat-tab i { font-size: 18px; }
            .chat-sidebar-content {
                flex: 1;
                overflow-y: auto;
                padding: 12px;
            }
            .sidebar-empty {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                padding: 40px 20px;
                color: #94a3b8;
                text-align: center;
            }
            .sidebar-empty i { font-size: 32px; margin-bottom: 12px; }
            .sidebar-empty p { font-size: 14px; margin: 0; }

            /* ========== 主聊天区 ========== */
            .chat-main {
                flex: 1;
                display: flex;
                flex-direction: column;
                min-width: 0;
                background: #fff;
            }

            /* ========== 消息区域 ========== */
            .chat-messages {
                flex: 1;
                overflow-y: auto;
                padding: 20px;
                background: linear-gradient(180deg, #f8fafc 0%, #fff 100%);
            }
            .chat-empty-state {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                height: 100%;
                color: #94a3b8;
                text-align: center;
            }
            .chat-empty-state i { font-size: 48px; margin-bottom: 16px; opacity: 0.5; }
            .chat-empty-state p { font-size: 16px; margin: 0 0 4px; color: #64748b; }
            .chat-empty-state span { font-size: 13px; }

            /* ========== 日期分割线 ========== */
            .chat-date-divider {
                display: flex;
                align-items: center;
                margin: 16px 0;
            }
            .chat-date-divider::before,
            .chat-date-divider::after {
                content: '';
                flex: 1;
                height: 1px;
                background: #e2e8f0;
            }
            .chat-date-divider span {
                padding: 0 12px;
                font-size: 12px;
                color: #94a3b8;
            }

            /* ========== 消息项 ========== */
            .chat-message {
                display: flex;
                gap: 12px;
                margin-bottom: 16px;
                animation: msgFadeIn 0.2s ease;
            }
            @keyframes msgFadeIn {
                from { opacity: 0; transform: translateY(10px); }
                to { opacity: 1; transform: translateY(0); }
            }
            .chat-message.self { flex-direction: row-reverse; }
            .msg-avatar {
                width: 40px;
                height: 40px;
                min-width: 40px;
                border-radius: 50%;
                background: #e2e8f0;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #64748b;
                font-size: 18px;
            }
            .msg-avatar.agent-avatar,
            .msg-avatar.agent {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
            }
            .msg-avatar.self-avatar {
                background: #6366f1;
                color: white;
            }
            .msg-content { max-width: 70%; }
            .msg-sender {
                font-size: 13px;
                font-weight: 500;
                color: #334155;
                margin-bottom: 4px;
            }
            .msg-bubble {
                padding: 12px 16px;
                background: #f1f5f9;
                border-radius: 16px;
                font-size: 14px;
                line-height: 1.5;
                color: #334155;
                word-wrap: break-word;
                white-space: pre-wrap;
            }
            .chat-message.self .msg-bubble {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
                border-radius: 16px 16px 4px 16px;
            }
            .chat-message.other .msg-bubble {
                border-radius: 16px 16px 16px 4px;
            }
            .chat-message.agent .msg-bubble {
                background: linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(139, 92, 246, 0.1) 100%);
                border: 1px solid rgba(99, 102, 241, 0.2);
            }
            .msg-meta {
                display: flex;
                align-items: center;
                gap: 8px;
                margin-top: 4px;
                font-size: 11px;
                color: #94a3b8;
            }
            .chat-message.self .msg-meta { justify-content: flex-end; }
            .msg-status i { font-size: 14px; }
            .msg-status.sent i { color: #6366f1; }
            .msg-status.sending i { color: #94a3b8; }
            .msg-status.failed i { color: #ef4444; }

            /* ========== 输入区域 ========== */
            .chat-input-box {
                padding: 16px;
                background: #fff;
                border-top: 1px solid #e2e8f0;
                flex-shrink: 0;
            }
            .chat-input-header {
                display: flex;
                align-items: center;
                gap: 8px;
                margin-bottom: 8px;
                min-height: 24px;
            }
            .chat-target-badge {
                display: inline-flex;
                align-items: center;
                gap: 4px;
                padding: 4px 12px;
                background: linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(139, 92, 246, 0.1) 100%);
                border-radius: 20px;
                font-size: 13px;
                color: #6366f1;
                font-weight: 500;
            }
            .chat-target-clear {
                background: none;
                border: none;
                cursor: pointer;
                color: #94a3b8;
                padding: 4px;
                display: flex;
                align-items: center;
                justify-content: center;
                transition: color 0.2s;
            }
            .chat-target-clear:hover { color: #64748b; }
            .chat-input-row {
                display: flex;
                gap: 12px;
                align-items: flex-end;
            }
            #llmChatInput {
                flex: 1;
                padding: 12px 16px;
                border: 1px solid #e2e8f0;
                border-radius: 24px;
                resize: none;
                font-size: 14px;
                font-family: inherit;
                background: #f8fafc;
                color: #334155;
                line-height: 1.5;
                transition: all 0.2s;
            }
            #llmChatInput:focus {
                outline: none;
                border-color: #6366f1;
                background: #fff;
                box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
            }
            #llmChatInput::placeholder { color: #94a3b8; }
            .chat-send-btn {
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
                font-size: 20px;
                transition: all 0.2s;
            }
            .chat-send-btn:hover {
                transform: scale(1.05);
                box-shadow: 0 4px 12px rgba(99, 102, 241, 0.4);
            }
            .chat-send-btn:disabled {
                opacity: 0.6;
                cursor: not-allowed;
                transform: none;
            }

            /* ========== 参与者列表 ========== */
            .participant-list { display: flex; flex-direction: column; gap: 4px; }
            .participant-item {
                display: flex;
                align-items: center;
                gap: 12px;
                padding: 10px 12px;
                border-radius: 12px;
                cursor: pointer;
                transition: all 0.2s;
            }
            .participant-item:hover { background: #f1f5f9; }
            .participant-item.selected { background: rgba(99, 102, 241, 0.1); }
            .participant-avatar {
                position: relative;
                width: 40px;
                height: 40px;
                min-width: 40px;
                border-radius: 50%;
                background: #e2e8f0;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #64748b;
                font-size: 18px;
            }
            .participant-avatar.agent {
                background: linear-gradient(135deg, rgba(99, 102, 241, 0.2) 0%, rgba(139, 92, 246, 0.2) 100%);
                color: #6366f1;
            }
            .online-dot {
                position: absolute;
                bottom: 2px;
                right: 2px;
                width: 10px;
                height: 10px;
                border-radius: 50%;
                background: #22c55e;
                border: 2px solid #fff;
            }
            .participant-info { flex: 1; min-width: 0; }
            .participant-name {
                font-size: 14px;
                font-weight: 500;
                color: #334155;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .participant-status { font-size: 12px; color: #94a3b8; }

            /* ========== 待办列表 ========== */
            .todo-list { display: flex; flex-direction: column; gap: 12px; }
            .todo-card {
                padding: 12px;
                background: #fff;
                border-radius: 12px;
                border: 1px solid #e2e8f0;
                transition: box-shadow 0.2s;
            }
            .todo-card:hover { box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06); }
            .todo-header {
                display: flex;
                justify-content: space-between;
                margin-bottom: 8px;
            }
            .todo-priority {
                font-size: 11px;
                padding: 2px 8px;
                border-radius: 4px;
                font-weight: 500;
            }
            .todo-priority.high { background: #fef2f2; color: #dc2626; }
            .todo-priority.medium { background: #fffbeb; color: #d97706; }
            .todo-priority.low { background: #f0fdf4; color: #16a34a; }
            .todo-status { font-size: 11px; color: #94a3b8; }
            .todo-title {
                font-size: 14px;
                font-weight: 500;
                color: #334155;
                margin-bottom: 4px;
            }
            .todo-desc {
                font-size: 13px;
                color: #64748b;
                margin-bottom: 12px;
                line-height: 1.4;
            }
            .todo-actions {
                display: flex;
                gap: 8px;
            }
            .todo-btn {
                flex: 1;
                padding: 8px 12px;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 13px;
                font-weight: 500;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 4px;
                transition: all 0.2s;
            }
            .todo-btn.accept {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
            }
            .todo-btn.accept:hover { opacity: 0.9; }
            .todo-btn.reject {
                background: #f1f5f9;
                color: #64748b;
            }
            .todo-btn.reject:hover { background: #e2e8f0; }

            /* ========== 滚动条美化 ========== */
            .chat-messages::-webkit-scrollbar,
            .chat-sidebar-content::-webkit-scrollbar {
                width: 6px;
            }
            .chat-messages::-webkit-scrollbar-track,
            .chat-sidebar-content::-webkit-scrollbar-track {
                background: transparent;
            }
            .chat-messages::-webkit-scrollbar-thumb,
            .chat-sidebar-content::-webkit-scrollbar-thumb {
                background: #cbd5e1;
                border-radius: 3px;
            }
            .chat-messages::-webkit-scrollbar-thumb:hover,
            .chat-sidebar-content::-webkit-scrollbar-thumb:hover {
                background: #94a3b8;
            }
        `;
        document.head.appendChild(style);
    },

    updateBadge() {
        const badge = document.getElementById('llmChatBadge');
        const unread = this.sceneMessages?.filter(m => !m.read).length || 0;
        if (badge) {
            badge.textContent = unread > 99 ? '99+' : unread.toString();
            badge.style.display = unread > 0 ? 'flex' : 'none';
        }
    },

    updateSendButton(disabled) {
        const btn = document.getElementById('llmChatSendBtn');
        if (btn) {
            btn.disabled = disabled;
            btn.innerHTML = disabled 
                ? '<i class="ri-loader-4-line ri-spin"></i>'
                : '<i class="ri-send-plane-fill"></i>';
        }
    }
};

export default UIRendererMixin;
