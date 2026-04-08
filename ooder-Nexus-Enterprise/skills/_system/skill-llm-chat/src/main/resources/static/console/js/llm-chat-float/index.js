import { eventBus } from './core/event-bus.js';
import { FloatBar } from './components/float-bar.js';
import { UnifiedChatWindow } from './components/unified-chat-window.js';
import { TodoCard } from './components/todo-card.js';
import { wsService } from './core/websocket-service.js';

class AgentChat {

    constructor(options = {}) {
        this.options = {
            sceneGroupId: null,
            autoConnect: true,
            unifiedMode: true,
            ...options
        };

        this.container = null;
        this.floatBar = null;
        this.unifiedWindow = null;
        this.legacyWindows = new Map();
        this.initialized = false;

        this.init();
    }

    init() {
        this.createContainer();
        
        if (this.options.unifiedMode) {
            this.createUnifiedEntry();
        } else {
            this.createLegacyEntry();
        }
        
        this.bindEvents();
        this.addGlobalStyles();

        if (this.options.autoConnect && this.options.sceneGroupId) {
            this.connectWebSocket();
        }

        this.initialized = true;
    }

    createContainer() {
        let container = document.getElementById('agent-chat-app');
        if (!container) {
            container = document.createElement('div');
            container.id = 'agent-chat-app';
            document.body.appendChild(container);
        }
        this.container = container;
    }

    createUnifiedEntry() {
        this.floatBar = new FloatBar(this.container, {
            menus: [
                { id: 'chat', icon: 'ri-wechat-line', label: '消息中心', badge: 0 }
            ]
        });

        const windowsContainer = document.createElement('div');
        windowsContainer.id = 'agent-chat-windows';
        this.container.appendChild(windowsContainer);

        try {
            this.unifiedWindow = new UnifiedChatWindow(windowsContainer, {
                sceneGroupId: this.options.sceneGroupId
            });
            
            this.unifiedWindow.on('message:sent', ({ sceneGroupId, message }) => {
                this.sendMessageToBackend(sceneGroupId, message);
            });

            console.log('[AgentChat] Unified window created');
        } catch (e) {
            console.error('[AgentChat] Failed to create unified window:', e);
        }
    }

    createLegacyEntry() {
        this.floatBar = new FloatBar(this.container, {
            menus: [
                { id: 'todos', icon: 'ri-task-line', label: '待办', badge: 0 },
                { id: 'assistant', icon: 'ri-robot-line', label: '智能助手', badge: 0 },
                { id: 'im', icon: 'ri-message-3-line', label: 'IM消息', badge: 0 }
            ]
        });

        const windowsContainer = document.createElement('div');
        windowsContainer.id = 'agent-chat-windows';
        this.container.appendChild(windowsContainer);

        import('./components/assistant-window.js').then(m => {
            const w = new m.AssistantWindow(windowsContainer, { sceneGroupId: this.options.sceneGroupId });
            this.legacyWindows.set('assistant', w);
        }).catch(() => {});
        import('./components/todo-window.js').then(m => {
            const w = new m.TodoWindow(windowsContainer, { sceneGroupId: this.options.sceneGroupId });
            this.legacyWindows.set('todos', w);
        }).catch(() => {});
        import('./components/im-window.js').then(m => {
            const w = new m.ImWindow(windowsContainer, { sceneGroupId: this.options.sceneGroupId });
            this.legacyWindows.set('im', w);
        }).catch(() => {});
    }

    bindEvents() {
        eventBus.on('floatBar:menuSelect', ({ menuId }) => {
            if (this.options.unifiedMode && menuId === 'chat') {
                this.openUnified();
            } else if (!this.options.unifiedMode) {
                this.openWindow(menuId);
            }
        });

        eventBus.on('window:open', ({ id }) => {
            this.legacyWindows.forEach((w, key) => {
                if ((w.options?.id || key) !== id && w.isOpen) w.minimize();
            });
        });

        eventBus.on('im:message:new', (msg) => {
            if (this.unifiedWindow) {
                this.unifiedWindow.addMessage({
                    messageId: msg.messageId || ('msg-' + Date.now()),
                    messageType: msg.type || 'P2A',
                    sender: msg.senderName || msg.sender,
                    senderType: msg.senderType || 'USER',
                    content: msg.content || '',
                    createTime: Date.now(),
                    status: 'SENT'
                });
            }
            this.floatBar?.updateBadge('chat', (this.floatBar?.getBadge('chat') || 0) + 1);
        });

        eventBus.on('todo:new', (todo) => {
            if (this.unifiedWindow) {
                this.unifiedWindow.addTodo(todo);
            }
            this.floatBar?.updateBadge('chat', (this.floatBar?.getBadge('chat') || 0) + 1);
        });

        eventBus.on('websocket:connected', () => {
            if (this.unifiedWindow) this.unifiedWindow.showToast('已连接到服务器');
        });

        eventBus.on('websocket:message', (data) => {
            this.handleWebSocketMessage(data);
        });

        this.bindKeyboardShortcuts();
    }

    handleWebSocketMessage(data) {
        if (!data || !data.payload) return;

        switch (data.type || data.eventType) {
            case 'message:new':
                if (this.unifiedWindow) {
                    const p = data.payload;
                    this.unifiedWindow.addMessage({
                        messageId: p.messageId,
                        messageType: p.conversationType || 'P2A',
                        sender: p.senderName,
                        senderType: p.senderType,
                        content: p.content,
                        createTime: Date.now(),
                        status: 'SENT',
                        availableActions: p.availableActions
                    });
                }
                break;

            case 'todo:new':
                if (this.unifiedWindow) {
                    this.unifiedWindow.addTodo(data.payload);
                }
                break;

            case 'participant:online':
            case 'participant:offline':
                this.refreshParticipants(data.payload);
                break;

            case 'agent:response':
                if (this.unifiedWindow && data.payload) {
                    this.unifiedWindow.addMessage({
                        messageId: data.payload.messageId,
                        messageType: 'A2A',
                        sender: data.payload.agentName,
                        senderType: 'AGENT',
                        content: data.payload.content,
                        createTime: Date.now(),
                        status: 'SENT'
                    });
                }
                break;
        }
    }

    refreshParticipants(participantsData) {
        if (!this.unifiedWindow || !participantsData) return;
        
        const parts = [];
        if (Array.isArray(participantsData)) {
            participantsData.forEach(p => {
                parts.push({ id: p.id || p.userId, name: p.name || p.displayName || p.nickname || '?', 
                             type: p.type || p.role || 'PARTICIPANT', role: p.role, online: !!p.online });
            });
        } else if (participantsData.participants) {
            participantsData.participants.forEach(p => {
                parts.push({ id: p.id, name: p.name, type: p.type || 'PARTICIPANT', online: p.status === 'ONLINE' });
            });
        }
        this.unifiedWindow.setParticipants(parts);
    }

    bindKeyboardShortcuts() {
        document.addEventListener('keydown', (e) => {
            if ((e.ctrlKey || e.metaKey) && e.key === '`') {
                e.preventDefault();
                if (this.unifiedWindow) {
                    this.unifiedWindow.toggle();
                } else {
                    this.floatBar?.toggle();
                }
            }
            if (e.key === 'Escape') {
                if (this.unifiedWindow && this.unifiedWindow.isOpen) {
                    this.unifiedWindow.close();
                } else {
                    this.legacyWindows.forEach(w => { if (w.isOpen) w.minimize(); });
                    this.floatBar?.close();
                }
            }
        });
    }

    openUnified() {
        if (this.unifiedWindow) this.unifiedWindow.open();
    }

    openWindow(id) {
        if (this.options.unifiedMode && id === 'chat') return this.openUnified();
        const w = this.legacyWindows.get(id);
        if (w) w.open();
    }

    async sendMessageToBackend(sceneGroupId, message) {
        const url = sceneGroupId
            ? `/api/v1/scene-groups/${sceneGroupId}/chat/messages`
            : '/api/v1/llm/chat';

        try {
            const resp = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(message)
            });
            if (!resp.ok) console.warn('[AgentChat] Send failed:', resp.status);
        } catch (e) {
            console.warn('[AgentChat] Send error:', e);
        }
    }

    setSceneGroupId(sceneGroupId) {
        this.options.sceneGroupId = sceneGroupId;
        if (this.unifiedWindow) {
            this.unifiedWindow.sceneGroupId = sceneGroupId;
            this.unifiedWindow.isSceneMode = !!sceneGroupId;
        }
        this.connectWebSocket();
    }

    connectWebSocket() {
        if (this.options.sceneGroupId) {
            const wsUrl = `ws://${window.location.host}/ws/scene-groups/${this.options.sceneGroupId}/chat`;
            wsService.connectWithToken(this.options.sceneGroupId, wsUrl);
        }
    }

    disconnectWebSocket() {
        wsService.disconnect();
    }

    getUnifiedWindow() { return this.unifiedWindow; }
    getAssistantWindow() { return this.legacyWindows.get('assistant'); }
    getTodoWindow() { return this.legacyWindows.get('todos'); }
    getImWindow() { return this.legacyWindows.get('im'); }

    addGlobalStyles() {
        if (document.getElementById('agent-chat-global-styles')) return;
        const style = document.createElement('style');
        style.id = 'agent-chat-global-styles';
        style.textContent = `
            * { box-sizing: border-box; }
            #agent-chat-app { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }
            #agent-chat-app * { box-sizing: border-box; }
            .spin { animation: spin 1s linear infinite; display: inline-block; }
            @keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
            @keyframes fadeInUp { from { opacity:0; transform:translateY(8px); } to { opacity:1; transform:translateY(0); } }
        `;
        document.head.appendChild(style);
    }

    destroy() {
        this.disconnectWebSocket();
        if (this.unifiedWindow) this.unifiedWindow.destroy();
        this.legacyWindows.forEach(w => w.destroy());
        this.container.remove();
    }
}

window.AgentChat = AgentChat;
export default AgentChat;
