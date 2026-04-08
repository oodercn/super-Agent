/**
 * LlmChatFloat WebSocket模块
 * 包含WebSocket连接和消息处理
 * 支持Token认证
 */
import { wsService } from './core/websocket-service.js';

export const WebSocketMixin = {
    connectWebSocket() {
        if (!this.sceneGroupId) return;
        
        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const wsUrl = `${protocol}//${window.location.host}/ws/scene-groups/${this.sceneGroupId}/chat`;
        
        this.setupWebSocketEvents();
        
        wsService.connectWithToken(this.sceneGroupId, wsUrl);
    },

    setupWebSocketEvents() {
        wsService.subscribe('connected', (data) => {
            console.log('[LlmChatFloat] WebSocket connected, authenticated:', data.authenticated);
        });
        
        wsService.subscribe('message', (data) => {
            this.handleWebSocketMessage(data);
        });
        
        wsService.subscribe('disconnected', (data) => {
            console.log('[LlmChatFloat] WebSocket disconnected:', data.code, data.reason);
            if (data.code !== 1000) {
                setTimeout(() => {
                    if (this.sceneGroupId) {
                        this.connectWebSocket();
                    }
                }, 5000);
            }
        });
        
        wsService.subscribe('auth_failed', (data) => {
            console.error('[LlmChatFloat] WebSocket authentication failed:', data.error);
        });
        
        wsService.subscribe('permission_denied', (data) => {
            console.error('[LlmChatFloat] WebSocket permission denied:', data.error);
        });
    },

    handleWebSocketMessage(data) {
        switch (data.type) {
            case 'message':
            case 'chat_message':
                this.handleNewMessage(data.data || data);
                break;
            case 'typing':
                this.handleTyping(data.data || data);
                break;
            case 'participant_status':
                this.handleParticipantStatus(data.data || data);
                break;
            case 'todo_update':
                this.handleTodoUpdate(data.data || data);
                break;
            case 'offline_messages':
                this.handleOfflineMessages(data.messages || data);
                break;
            default:
                console.log('[LlmChatFloat] Unknown message type:', data.type);
        }
    },

    handleNewMessage(message) {
        this.sceneMessages.push(message);
        this.renderSceneMessages();
        this.updateBadge();
    },

    handleTyping(data) {
        const indicator = document.getElementById('typingIndicator');
        if (indicator) {
            if (data.isTyping) {
                indicator.textContent = `${data.userName} 正在输入...`;
                indicator.style.display = 'block';
            } else {
                indicator.style.display = 'none';
            }
        }
    },

    handleParticipantStatus(data) {
        const participant = this.sceneParticipants.find(p => p.id === data.participantId);
        if (participant) {
            participant.status = data.status;
            this.renderParticipants();
        }
    },

    handleTodoUpdate(data) {
        if (data.action === 'add') {
            this.sceneTodos.push(data.todo);
        } else if (data.action === 'update') {
            const index = this.sceneTodos.findIndex(t => t.id === data.todo.id);
            if (index !== -1) {
                this.sceneTodos[index] = data.todo;
            }
        } else if (data.action === 'delete') {
            this.sceneTodos = this.sceneTodos.filter(t => t.id !== data.todo.id);
        }
        this.renderTodos();
        this.updateBadge();
    },
    
    handleOfflineMessages(messages) {
        if (messages && messages.length > 0) {
            messages.forEach(msg => {
                this.sceneMessages.push(msg);
            });
            this.renderSceneMessages();
            this.updateBadge();
        }
    },

    sendWebSocketMessage(type, data) {
        wsService.send({ type, ...data });
    },

    disconnectWebSocket() {
        wsService.disconnect();
    }
};

export default WebSocketMixin;
