import { BaseWindow } from './base-window.js';
import { FileUploadBar } from './file-upload-bar.js';
import { KnowledgePanel } from './knowledge-panel.js';

export class UnifiedChatWindow extends BaseWindow {

    constructor(container, options = {}) {
        super(container, {
            id: 'unified-chat-window',
            width: 860,
            height: 640,
            title: '消息中心',
            icon: 'ri-wechat-line',
            minWidth: 680,
            minHeight: 480,
            ...options
        });

        this.currentTab = 'all';
        this.messages = [];
        this.todos = [];
        this.participants = [];
        this.selectedTarget = null;
        this.sceneGroupId = options.sceneGroupId || null;
        this.isSceneMode = !!this.sceneGroupId;

        this.fileUploadBar = null;
        this.knowledgePanel = null;
        this.participantsCollapsed = false;

        this.renderContent();
        this.bindEvents();
    }

    renderContent() {
        const theme = document.documentElement.getAttribute('data-theme') || '';
        
        this.contentEl.innerHTML = `
        <div class="unified-chat-container" data-theme="${theme}">
            <div class="uc-sidebar">
                <button class="uc-tab active" data-tab="all" title="全部消息">
                    <i class="ri-message-3-line"></i><span>全部</span>
                    <span class="tab-badge" id="ucBadgeAll">0</span>
                </button>
                <button class="uc-tab" data-tab="a2a" title="Agent协作">
                    <i class="ri-robot-2-line"></i><span>A2A</span>
                    <span class="tab-badge" id="ucBadgeA2a">0</span>
                </button>
                <button class="uc-tab" data-tab="p2a" title="与Agent对话">
                    <i class="ri-customer-service-2-line"></i><span>P2A</span>
                    <span class="tab-badge" id="ucBadgeP2a">0</span>
                </button>
                <button class="uc-tab" data-tab="p2p" title="私聊">
                    <i class="ri-user-voice-line"></i><span>P2P</span>
                    <span class="tab-badge" id="ucBadgeP2p">0</span>
                </button>
                <button class="uc-tab" data-tab="todo" title="待办事项">
                    <i class="ri-checkbox-circle-line"></i><span>待办</span>
                    <span class="tab-badge" id="ucBadgeTodo">0</span>
                </button>
                <div class="uc-sidebar-bottom">
                    <button class="uc-tool-btn" id="ucToggleKp" title="知识面板">
                        <i class="ri-book-open-line"></i>
                    </button>
                    <button class="uc-tool-btn" id="ucToggleParts" title="参与者面板">
                        <i class="ri-team-line"></i>
                    </button>
                </div>
            </div>

            <div class="uc-main">
                <div class="uc-header">
                    <div class="uc-header-left">
                        <h4 class="uc-header-title">${this.isSceneMode ? '场景组对话' : '智能助手'}</h4>
                        ${this.isSceneMode ? `<span class="uc-header-subtitle">ID: ${this.sceneGroupId}</span>` : ''}
                        <span class="uc-online-count" id="ucOnlineCount"><i class="ri-user-follow-line"></i> 0 在线</span>
                    </div>
                    <div class="uc-header-right">
                        <button class="uc-header-btn" id="ucSearchBtn" title="搜索消息"><i class="ri-search-line"></i></button>
                        <button class="uc-header-btn" id="ucPinBtn" title="置顶"><i class="ri-pushpin-line"></i></button>
                    </div>
                </div>

                <div class="uc-messages" id="ucMessages">
                    <div class="uc-welcome" id="ucWelcome">
                        <i class="ri-chat-smile-3-line uc-welcome-icon"></i>
                        <p>开始对话吧</p>
                        <p class="uc-welcome-hint">@提及 Agent 可触发自动回复</p>
                    </div>
                </div>

                <div class="uc-input-area">
                    <div class="uc-input-toolbar" id="ucToolbar">
                        <label class="uc-toolbar-btn" id="ucAttachBtn" title="上传附件">
                            <i class="ri-attachment-2"></i>
                        </label>
                        <button class="uc-toolbar-btn" id="ucEmojiBtn" title="表情"><i class="ri-emotion-line"></i></button>
                        <button class="uc-toolbar-btn" id="ucAtBtn" title="@提及"><i class="ri-at-line"></i></button>
                        <div class="uc-file-preview" id="ucFilePreview"></div>
                    </div>
                    <textarea class="uc-textarea" id="ucTextarea"
                              placeholder="${this.isSceneMode ? '输入消息，@AgentName 触发回复...' : '输入您的问题...'}"
                              rows="1"></textarea>
                    <div class="uc-send-row">
                        <span class="uc-hint" id="ucHint">Enter 发送 · Shift+Enter 换行</span>
                        <button class="uc-send-btn" id="ucSendBtn">
                            <i class="ri-send-plane-fill"></i> 发送
                        </button>
                    </div>
                </div>
            </div>

            <div class="uc-participants" id="ucParticipants">
                <div class="uc-parts-header">
                    <span>参与者</span>
                    <button class="uc-parts-toggle" id="ucPartsCollapse"><i class="ri-arrow-right-s-line"></i></button>
                </div>
                <div class="uc-parts-list" id="ucPartsList"></div>
                <div class="uc-knowledge-panel" id="ucKnowledgePanel" style="display:none"></div>
            </div>
        </div>`;

        this.initFileUpload();
        this.initKnowledgePanel();
    }

    initFileUpload() {
        const toolbar = this.contentEl.querySelector('#ucToolbar');
        if (!toolbar) return;
        
        this.fileUploadBar = new FileUploadBar(toolbar);
        
        this.fileUploadBar.container.addEventListener('fileUpload:change', (e) => {
            const btn = this.contentEl.querySelector('#ucSendBtn');
            if (btn) btn.innerHTML = `<i classri-upload-cloud-line"></i> 发送 (${e.detail.count})`;
        });
    }

    initKnowledgePanel() {
        const kpContainer = this.contentEl.querySelector('#ucKnowledgePanel');
        if (kpContainer) {
            this.knowledgePanel = new KnowledgePanel(kpContainer);
            
            this.knowledgePanel.container.addEventListener('kp:use-in-chat', (e) => {
                const textarea = this.contentEl.querySelector('#ucTextarea');
                if (textarea && e.detail.docId) {
                    textarea.value += ` [引用:${e.detail.docId}] `;
                    textarea.focus();
                }
            });

            this.knowledgePanel.container.addEventListener('kp:ingest-from-chat', () => {
                const msgs = this.messages.filter(m => m.messageType === 'P2A' || m.messageType === 'SYSTEM');
                if (msgs.length > 0) {
                    this.showToast(`已提取 ${msgs.length} 条对话内容到知识库`);
                } else {
                    this.showToast('当前没有可提取的对话内容');
                }
            });
        }
    }

    bindEvents() {
        this.contentEl.querySelectorAll('.uc-tab').forEach(tab => {
            tab.addEventListener('click', (e) => {
                e.stopPropagation();
                this.switchTab(e.currentTarget.dataset.tab);
            });
        });

        const sendBtn = this.contentEl.querySelector('#ucSendBtn');
        if (sendBtn) sendBtn.addEventListener('click', () => this.sendMessage());

        const textarea = this.contentEl.querySelector('#ucTextarea');
        if (textarea) {
            textarea.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' && !e.shiftKey) { e.preventDefault(); this.sendMessage(); }
            });
            textarea.addEventListener('input', () => this.autoResizeTextarea(textarea));
        }

        this.contentEl.querySelector('#ucToggleKp')?.addEventListener('click', () => {
            const kp = this.contentEl.querySelector('#ucKnowledgePanel');
            if (kp) kp.style.display = kp.style.display === 'none' ? 'block' : 'none';
        });

        this.contentEl.querySelector('#ucToggleParts')?.addEventListener('click', () => {
            const parts = this.contentEl.querySelector('#ucParticipants');
            parts.classList.toggle('collapsed');
            this.participantsCollapsed = parts.classList.contains('collapsed');
        });

        this.contentEl.querySelector('#ucAtBtn')?.addEventListener('click', () => this.showMentionPopup());
    }

    switchTab(tab) {
        this.currentTab = tab;
        this.contentEl.querySelectorAll('.uc-tab').forEach(t => t.classList.remove('active'));
        this.contentEl.querySelector(`[data-tab="${tab}"]`)?.classList.add('active');
        this.renderMessageStream();
    }

    async sendMessage() {
        const textarea = this.contentEl.querySelector('#ucTextarea');
        if (!textarea || !textarea.value.trim()) return;

        let attachments = [];
        if (this.fileUploadBar && this.fileUploadBar.hasPendingFiles()) {
            attachments = await this.fileUploadBar.uploadAll(this.sceneGroupId || 'global');
        }

        const content = textarea.value.trim();
        const msg = {
            messageId: 'msg-' + Date.now(),
            messageType: 'P2P',
            sender: '我',
            senderType: 'USER',
            content,
            attachments,
            createTime: Date.now(),
            status: 'SENT'
        };

        this.messages.push(msg);
        textarea.value = '';
        this.autoResizeTextarea(textarea);

        this.appendMessage(msg);

        this.emit('message:sent', { sceneGroupId: this.sceneGroupId, message: msg });
    }

    appendMessage(msg) {
        const welcome = this.contentEl.querySelector('#ucWelcome');
        if (welcome) welcome.style.display = 'none';

        const messagesEl = this.contentEl.querySelector('#ucMessages');
        if (!messagesEl) return;

        const typeClass = msg.messageType ? `msg-type-${msg.messageType.toLowerCase()}` : '';

        const html = `
        <div class="uc-msg-item ${typeClass}" data-msg-id="${msg.messageId}">
            <div class="uc-msg-avatar">
                ${this.getAvatarIcon(msg.senderType)}
            </div>
            <div class="uc-msg-body">
                <div class="uc-msg-meta">
                    <span class="uc-msg-name">${msg.sender || '系统'}</span>
                    <span class="uc-msg-time">${this.formatTime(msg.createTime)}</span>
                    ${msg.senderType === 'AGENT' ? '<span class="uc-msg-tag agent">Agent</span>' : ''}
                </div>
                <div class="uc-msg-content">${this.formatContent(msg)}</div>
                ${msg.attachments && msg.attachments.length > 0 ? this.renderAttachments(msg.attachments) : ''}
                ${msg.availableActions ? this.renderActions(msg) : ''}
            </div>
        </div>`;
        
        messagesEl.insertAdjacentHTML('beforeend', html);
        messagesEl.scrollTop = messagesEl.scrollHeight;
    }

    appendTodoCard(todo) {
        const messagesEl = this.contentEl.querySelector('#ucMessages');
        if (!messagesEl) return;

        const urgencyClass = todo.deadline && Date.now() > todo.deadline ? 'urgent' : '';
        const statusIcon = todo.status === 'COMPLETED' ? 'ri-check-line' :
                           todo.status === 'REJECTED' ? 'ri-close-line' : 'ri-time-line';

        const html = `
        <div class="todo-card ${urgencyClass}" data-todo-id="${todo.id}">
            <div class="todo-card-header">
                <span class="todo-card-type"><i class="${statusIcon}"></i> ${todo.type || 'TODO'}</span>
                ${todo.deadline ? `<span class="todo-card-deadline ${urgencyClass}"><i class="ri-time-line"></i> ${new Date(todo.deadline).toLocaleDateString()}</span>` : ''}
            </div>
            <div class="todo-card-title">${this.escapeHtml(todo.title)}</div>
            ${todo.description ? `<div class="todo-card-desc">${this.escapeHtml(todo.description)}</div>` : ''}
            ${todo.status !== 'COMPLETED' && todo.status !== 'REJECTED' ? `
            <div class="todo-card-actions">
                <button class="todo-action accept" data-action="accept">✓ 接受</button>
                <button class="todo-action reject" data-action="reject">✗ 拒绝</button>
                ${todo.status === 'ACCEPTED' ? '<button class="todo-action complete" data-action="complete">✓ 完成</button>' : ''}
            </div>` : todo.status === 'COMPLETED' ? 
              '<span class="todo-card-done"><i class="ri-check-line"></i> 已完成</span>' :
              '<span class="todo-card-rejected"><i class="ri-close-line"></i> 已拒绝</span>'}
        </div>`;

        messagesEl.insertAdjacentHTML('beforeend', html);
        messagesEl.scrollTop = messagesEl.scrollHeight;
        
        const todoCard = messagesEl.querySelector(`[data-todo-id="${todo.id}"]`);
        if (todoCard) {
            todoCard.querySelectorAll('.todo-action').forEach(btn => {
                btn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    this.handleTodoAction(todo.id, btn.dataset.action);
                });
            });
        }
    }
    
    async handleTodoAction(todoId, action) {
        const actionUrls = {
            accept: `/api/v1/scene-groups/${this.sceneGroupId}/chat/todos/${todoId}/accept`,
            reject: `/api/v1/scene-groups/${this.sceneGroupId}/chat/todos/${todoId}/reject`,
            complete: `/api/v1/scene-groups/${this.sceneGroupId}/chat/todos/${todoId}/complete`
        };
        
        const url = actionUrls[action];
        if (!url) {
            this.showToast('未知操作');
            return;
        }
        
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + (localStorage.getItem('token') || sessionStorage.getItem('token') || '')
                },
                credentials: 'same-origin'
            });
            
            const result = await response.json();
            
            if (result.status === 'success' || result.data === true) {
                this.showToast(action === 'accept' ? '已接受待办' : 
                              action === 'reject' ? '已拒绝待办' : '已完成待办');
                
                const todoIndex = this.todos.findIndex(t => t.id === todoId);
                if (todoIndex !== -1) {
                    const statusMap = { accept: 'ACCEPTED', reject: 'REJECTED', complete: 'COMPLETED' };
                    this.todos[todoIndex].status = statusMap[action] || action.toUpperCase();
                    this.renderMessageStream();
                }
                
                this.emit('todo:updated', { todoId, action });
            } else {
                this.showToast(result.message || '操作失败');
            }
        } catch (err) {
            console.error('[handleTodoAction] Error:', err);
            this.showToast('操作失败，请重试');
        }
    }

    renderMessageStream() {
        const el = this.contentEl.querySelector('#ucMessages');
        if (!el) return;

        let filtered = this.messages;
        if (this.currentTab !== 'all') {
            filtered = this.messages.filter(m => m.messageType === this.currentTab.toUpperCase());
        }

        if (this.currentTab === 'todo') {
            el.innerHTML = this.todos.map(t => this.renderTodoCardHtml(t)).join('');
            return;
        }

        if (filtered.length === 0) {
            el.innerHTML = `<div class="uc-empty"><i class="ri-inbox-line"></i><p>暂无${this.getTabLabel(this.currentTab)}消息</p></div>`;
            return;
        }

        el.innerHTML = filtered.map(m => {
            const typeClass = m.messageType ? `msg-type-${m.messageType.toLowerCase()}` : '';
            return `
            <div class="uc-msg-item ${typeClass}" data-msg-id="${m.messageId}">
                <div class="uc-msg-avatar">${this.getAvatarIcon(m.senderType)}</div>
                <div class="uc-msg-body">
                    <div class="uc-msg-meta">
                        <span class="uc-msg-name">${m.sender || '未知'}</span>
                        <span class="uc-msg-time">${this.formatTime(m.createTime)}</span>
                    </div>
                    <div class="uc-msg-content">${this.formatContent(m)}</div>
                    ${m.attachments?.length ? this.renderAttachments(m.attachments) : ''}
                </div>
            </div>`;
        }).join('');
    }

    renderTodoCardHtml(todo) {
        const urgencyClass = todo.deadline && Date.now() > todo.deadline ? 'urgent' : '';
        return `
        <div class="todo-card ${urgencyClass}">
            <div class="todo-card-header">
                <span class="todo-card-type"><i class="ri-checkbox-circle-line"></i> ${todo.type || 'TODO'}</span>
            </div>
            <div class="todo-card-title">${this.escapeHtml(todo.title)}</div>
            ${todo.description ? `<div class="todo-card-desc">${this.escapeHtml(todo.description)}</div>` : ''}
        </div>`;
    }

    showMentionPopup() {
        if (this.participants.length === 0) {
            this.showToast('暂无可@提及的参与者');
            return;
        }
        const popup = document.createElement('div');
        popup.className = 'mention-popup active';
        popup.style.cssText = 'position:absolute;bottom:100%;left:0;background:#fff;border-radius:10px;box-shadow:0 4px 20px rgba(0,0,0,.12);padding:8px;z-index:99999;min-width:180px;';
        popup.innerHTML = '<div style="font-size:11px;color:#888;margin-bottom:6px;padding:0 4px;">选择@提及对象</div>' +
            this.participants.map(p => `
                <div class="mention-item" data-id="${p.id}" style="display:flex;align-items:center;gap:8px;padding:6px 10px;cursor:pointer;border-radius:6px;font-size:13px;">
                    <span>${p.name}</span>
                    <span style="color:#888;font-size:11px;">${p.type}</span>
                </div>`).join('');

        const toolbar = this.contentEl.querySelector('#ucToolbar');
        toolbar.appendChild(popup);

        popup.querySelectorAll('.mention-item').forEach(item => {
            item.addEventListener('click', () => {
                const textarea = this.contentEl.querySelector('#ucTextarea');
                if (textarea) {
                    textarea.value += `@${item.dataset.id} `;
                    textarea.focus();
                }
                popup.remove();
            });
        });

        setTimeout(() => { if (popup.parentNode) popup.remove(); }, 5000);
    }

    setParticipants(participants) {
        this.participants = participants || [];
        const list = this.contentEl.querySelector('#ucPartsList');
        if (!list) return;

        list.innerHTML = participants.map(p => `
            <div class="uc-participant" data-id="${p.id}">
                <div class="uc-part-avatar ${p.type === 'AGENT' ? 'agent' : ''}">${(p.name||'?')[0]}</div>
                <div class="uc-part-info">
                    <span class="uc-part-name">${p.name}</span>
                    <span class="uc-part-role">${p.type || '成员'}${p.role ? '/' + p.role : ''}</span>
                </div>
                <div class="uc-part-status ${p.online ? 'online' : ''}"></div>
            </div>`).join('');

        const countEl = this.contentEl.querySelector('#ucOnlineCount');
        if (countEl) countEl.innerHTML = `<i class="ri-user-follow-line"></i> ${participants.filter(p=>p.online).length} 在线`;
    }

    addMessage(msg) {
        this.messages.push(msg);
        if (this.currentTab === 'all' || msg.messageType === this.currentTab.toUpperCase()) {
            this.appendMessage(msg);
        }
        this.updateBadges();
    }

    addTodo(todo) {
        this.todos.push(todo);
        if (this.currentTab === 'todo' || this.currentTab === 'all') {
            this.appendTodoCard(todo);
        }
        this.updateBadges();
    }

    updateBadges() {
        const counts = { all: this.messages.length, a2a: 0, p2a: 0, p2p: 0, todo: this.todos.length };
        this.messages.forEach(m => { if (counts[m.messageType.toLowerCase()] !== undefined) counts[m.messageType.toLowerCase()]++; });
        
        Object.entries(counts).forEach(([tab, count]) => {
            const badge = this.contentEl.querySelector(`#ucBadge${tab.charAt(0).toUpperCase()+tab.slice(1)}`);
            if (badge) badge.textContent = count > 0 ? count > 99 ? '99+' : count : '';
            badge.style.display = count > 0 ? 'flex' : 'none';
        });
    }

    getAvatarIcon(type) {
        switch(type) {
            case 'AGENT': return '<i class="ri-robot-2-line"></i>';
            case 'SYSTEM': return '<i class="ri-notification-3-line"></i>';
            default: return '<i class="ri-user-3-line"></i>';
        }
    }

    formatContent(msg) {
        let content = msg.content || '';
        content = this.escapeHtml(content);
        content = content.replace(/```(\w*)\n?([\s\S]*?)```/g, '<pre class="uc-code-block"><code>$2</code></pre>');
        content = content.replace(/`([^`]+)`/g, '<code class="uc-inline-code">$1</code>');
        content = content.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>');
        content = content.replace(/\n/g, '<br>');
        return content;
    }

    renderAttachments(attachments) {
        return `<div class="uc-msg-attachments">${attachments.map(a =>
            `<div class="msg-attachment" onclick="window.open('/api/v1/chat/files/${a.fileId}/preview','_blank')">
                <i class="${this.getFileIcon(a.mimeType)}"></i>
                <div class="msg-attachment-info">
                    <span class="msg-attachment-name">${a.name}</span>
                    <span class="msg-attachment-meta">${a.formattedSize || ''}</span>
                </div>
            </div>`
        ).join('')}</div>`;
    }

    renderActions(msg) {
        return `<div class="msg-actions-row">${msg.availableActions.map(a =>
            `<button class="msg-action-btn" data-action="${a.actionId}">${a.icon||''} ${a.label}</button>`
        ).join('')}</div>`;
    }

    getFileIcon(mime) {
        if (!mime) return 'ri-file-line';
        if (mime.includes('pdf')) return 'ri-file-pdf-line';
        if (mime.includes('word')) return 'ri-file-word-line';
        if (mime.includes('sheet') || mime.includes('excel')) return 'ri-file-excel-line';
        if (mime.includes('image')) return 'ri-image-line';
        if (mime.includes('zip')) return 'ri-file-zip-line';
        return 'ri-file-line';
    }

    formatTime(ts) {
        if (!ts) return '';
        const d = new Date(ts);
        const now = new Date();
        if (d.toDateString() === now.toDateString()) return d.toLocaleTimeString([], {hour:'2-digit',minute:'2-digit'});
        return d.toLocaleDateString() + ' ' + d.toLocaleTimeString([], {hour:'2-digit',minute:'2-digit'});
    }

    getTabLabel(tab) {
        return { all: '', a2a: 'A2A ', p2a: 'P2A ', p2p: 'P2P ', todo: '待办' }[tab] || '';
    }

    autoResizeTextarea(textarea) {
        textarea.style.height = 'auto';
        textarea.style.height = Math.min(textarea.scrollHeight, 120) + 'px';
    }

    escapeHtml(str) {
        if (!str) return '';
        const div = document.createElement('div');
        div.textContent = str;
        return div.innerHTML;
    }

    showToast(msg) {
        const toast = document.createElement('div');
        toast.className = 'uc-toast';
        toast.textContent = msg;
        toast.style.cssText = 'position:fixed;top:50%;left:50%;transform:translate(-50%,-50%);background:rgba(0,0,0,.75);color:#fff;padding:12px 24px;border-radius:10px;z-index:999999;font-size:13px;animation:fadeInUp .2s;';
        document.body.appendChild(toast);
        setTimeout(() => toast.remove(), 2000);
    }

    destroy() {
        if (this.fileUploadBar) this.fileUploadBar.destroy();
        if (this.knowledgePanel) this.knowledgePanel.destroy();
        super.destroy();
    }
}
