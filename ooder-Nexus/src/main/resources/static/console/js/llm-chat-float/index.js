/**
 * Agent Chat 入口
 * 整合所有组件
 */
import { eventBus } from './core/event-bus.js';
import { FloatBar } from './components/float-bar.js';
import { AssistantWindow } from './components/assistant-window.js';
import { TodoWindow } from './components/todo-window.js';
import { ImWindow } from './components/im-window.js';
import { wsService } from './core/websocket-service.js';

class AgentChat {
    constructor(options = {}) {
        this.options = {
            sceneGroupId: null,
            autoConnect: true,
            ...options
        };
        
        this.windows = new Map();
        this.floatBar = null;
        this.initialized = false;
        
        this.init();
    }

    init() {
        this.createContainer();
        this.createFloatBar();
        this.createWindows();
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

    createFloatBar() {
        this.floatBar = new FloatBar(this.container, {
            menus: [
                { id: 'todos', icon: 'ri-task-line', label: '待办', badge: 0 },
                { id: 'assistant', icon: 'ri-robot-line', label: '智能助手', badge: 0 },
                { id: 'im', icon: 'ri-message-3-line', label: 'IM消息', badge: 0 }
            ]
        });
    }

    createWindows() {
        const windowsContainer = document.createElement('div');
        windowsContainer.id = 'agent-chat-windows';
        this.container.appendChild(windowsContainer);
        
        try {
            const assistantWindow = new AssistantWindow(windowsContainer, {
                sceneGroupId: this.options.sceneGroupId
            });
            const todoWindow = new TodoWindow(windowsContainer, {
                sceneGroupId: this.options.sceneGroupId
            });
            const imWindow = new ImWindow(windowsContainer, {
                sceneGroupId: this.options.sceneGroupId
            });
            
            this.windows.set('assistant', assistantWindow);
            this.windows.set('todos', todoWindow);
            this.windows.set('im', imWindow);
            
            console.log('[AgentChat] Windows created:', this.windows.keys());
        } catch (e) {
            console.error('[AgentChat] Failed to create windows:', e);
        }
    }

    bindEvents() {
        eventBus.on('floatBar:menuSelect', ({ menuId }) => {
            console.log('[AgentChat] Menu selected:', menuId);
            this.openWindow(menuId);
        });
        
        eventBus.on('window:open', ({ id }) => {
            console.log('[AgentChat] Window opened:', id);
            this.windows.forEach((window, key) => {
                const windowId = window.options?.id || key;
                if (windowId !== id && window.isOpen) {
                    window.minimize();
                }
            });
        });
        
        eventBus.on('im:unreadUpdate', ({ count }) => {
            this.floatBar.updateBadge('im', count);
        });
        
        eventBus.on('todo:added', () => {
            const currentBadge = this.floatBar.options.menus.find(m => m.id === 'todos')?.badge || 0;
            this.floatBar.updateBadge('todos', currentBadge + 1);
        });
        
        eventBus.on('websocket:connected', () => {
            console.log('[AgentChat] WebSocket connected');
        });
        
        eventBus.on('websocket:disconnected', () => {
            console.log('[AgentChat] WebSocket disconnected');
        });
        
        this.bindKeyboardShortcuts();
    }

    bindKeyboardShortcuts() {
        document.addEventListener('keydown', (e) => {
            if (e.ctrlKey || e.metaKey) {
                switch (e.key.toLowerCase()) {
                    case '1':
                        e.preventDefault();
                        this.openWindow('todos');
                        break;
                    case '2':
                        e.preventDefault();
                        this.openWindow('assistant');
                        break;
                    case '3':
                        e.preventDefault();
                        this.openWindow('im');
                        break;
                    case '`':
                        e.preventDefault();
                        this.floatBar.toggle();
                        break;
                }
            }
            
            if (e.key === 'Escape') {
                this.windows.forEach(window => {
                    if (window.isOpen) {
                        window.minimize();
                    }
                });
                if (this.floatBar.isOpen) {
                    this.floatBar.close();
                }
            }
        });
    }

    openWindow(id) {
        console.log('[AgentChat] openWindow called with id:', id);
        console.log('[AgentChat] Available windows:', Array.from(this.windows.keys()));
        const window = this.windows.get(id);
        if (window) {
            console.log('[AgentChat] Window found, opening...');
            window.open();
        } else {
            console.warn('[AgentChat] Window not found for id:', id);
        }
    }

    closeWindow(id) {
        const window = this.windows.get(id);
        if (window) {
            window.close();
        }
    }

    setSceneGroupId(sceneGroupId) {
        this.options.sceneGroupId = sceneGroupId;
        
        const imWindow = this.windows.get('im');
        if (imWindow) {
            imWindow.options.sceneGroupId = sceneGroupId;
            imWindow.connectWebSocket();
        }
        
        const todoWindow = this.windows.get('todos');
        if (todoWindow) {
            todoWindow.options.sceneGroupId = sceneGroupId;
            todoWindow.loadTodos();
        }
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

    updateBadges(badges) {
        Object.entries(badges).forEach(([id, count]) => {
            this.floatBar.updateBadge(id, count);
        });
    }

    getAssistantWindow() {
        return this.windows.get('assistant');
    }

    getTodoWindow() {
        return this.windows.get('todos');
    }

    getImWindow() {
        return this.windows.get('im');
    }

    addGlobalStyles() {
        if (document.getElementById('agent-chat-global-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'agent-chat-global-styles';
        style.textContent = `
            * {
                box-sizing: border-box;
            }
            #agent-chat-app {
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            }
            #agent-chat-app * {
                box-sizing: border-box;
            }
            .ri-spin {
                animation: ri-spin 1s linear infinite;
            }
            @keyframes ri-spin {
                from { transform: rotate(0deg); }
                to { transform: rotate(360deg); }
            }
        `;
        document.head.appendChild(style);
    }

    destroy() {
        this.disconnectWebSocket();
        this.windows.forEach(window => window.destroy());
        this.container.remove();
    }
}

window.AgentChat = AgentChat;

export default AgentChat;
