/**
 * WebSocket 服务
 * 实时消息推送
 * 支持Token认证
 */
import { eventBus } from './event-bus.js';

export class WebSocketService {
    constructor(options = {}) {
        this.options = {
            url: null,
            reconnectInterval: 3000,
            maxReconnectAttempts: 5,
            heartbeatInterval: 30000,
            tokenRefreshBeforeSeconds: 300,
            ...options
        };
        
        this.ws = null;
        this.isConnected = false;
        this.reconnectAttempts = 0;
        this.heartbeatTimer = null;
        this.messageQueue = [];
        this.subscriptions = new Map();
        this.currentToken = null;
        this.currentSceneGroupId = null;
        this.tokenRefreshTimer = null;
    }

    async connectWithToken(sceneGroupId, url) {
        this.currentSceneGroupId = sceneGroupId;
        
        try {
            const tokenResponse = await fetch(`/api/v1/scene-groups/${sceneGroupId}/chat/ws-token`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            });
            
            const result = await tokenResponse.json();
            
            if (result.status === 'success' && result.data?.token) {
                this.currentToken = result.data;
                console.log('[WebSocketService] Token obtained, expires at:', new Date(result.data.expireAt));
                
                this.scheduleTokenRefresh(result.data.expireAt);
                
                const wsUrl = url || this.options.url;
                const separator = wsUrl.includes('?') ? '&' : '?';
                const authenticatedUrl = `${wsUrl}${separator}token=${result.data.token}`;
                
                this.connect(authenticatedUrl);
            } else {
                console.warn('[WebSocketService] Token auth not available, connecting without token');
                this.connect(url);
            }
        } catch (e) {
            console.warn('[WebSocketService] Failed to get token, connecting without auth:', e);
            this.connect(url);
        }
    }

    scheduleTokenRefresh(expireAt) {
        if (this.tokenRefreshTimer) {
            clearTimeout(this.tokenRefreshTimer);
        }
        
        const now = Date.now();
        const refreshTime = expireAt - (this.options.tokenRefreshBeforeSeconds * 1000);
        const delay = Math.max(0, refreshTime - now);
        
        if (delay > 0) {
            console.log('[WebSocketService] Token refresh scheduled in', Math.round(delay / 1000), 'seconds');
            this.tokenRefreshTimer = setTimeout(() => {
                this.refreshToken();
            }, delay);
        }
    }

    async refreshToken() {
        if (!this.currentToken || !this.currentSceneGroupId) {
            return;
        }
        
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.currentSceneGroupId}/chat/ws-token/refresh`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ token: this.currentToken.token })
            });
            
            const result = await response.json();
            
            if (result.status === 'success' && result.data?.token) {
                this.currentToken = result.data;
                console.log('[WebSocketService] Token refreshed, new expire at:', new Date(result.data.expireAt));
                this.scheduleTokenRefresh(result.data.expireAt);
            } else {
                console.warn('[WebSocketService] Token refresh failed, will reconnect');
            }
        } catch (e) {
            console.error('[WebSocketService] Token refresh error:', e);
        }
    }

    connect(url) {
        if (url) {
            this.options.url = url;
        }
        
        if (!this.options.url) {
            console.error('[WebSocketService] No URL provided');
            return;
        }
        
        try {
            const wsUrl = this.options.url;
            console.log('[WebSocketService] Connecting to:', wsUrl);
            
            this.ws = new WebSocket(wsUrl);
            
            this.ws.onopen = () => {
                console.log('[WebSocketService] Connected');
                this.isConnected = true;
                this.reconnectAttempts = 0;
                this.startHeartbeat();
                this.flushMessageQueue();
                
                eventBus.emit('websocket:connected', { url: wsUrl, authenticated: !!this.currentToken });
            };
            
            this.ws.onmessage = (event) => {
                try {
                    const data = JSON.parse(event.data);
                    this.handleMessage(data);
                } catch (e) {
                    console.error('[WebSocketService] Parse error:', e);
                }
            };
            
            this.ws.onclose = (event) => {
                console.log('[WebSocketService] Disconnected:', event.code, event.reason);
                this.isConnected = false;
                this.stopHeartbeat();
                
                if (event.code === 1003 || event.code === 1008) {
                    console.error('[WebSocketService] Authentication failed');
                    eventBus.emit('websocket:auth_failed', { code: event.code, reason: event.reason });
                    return;
                }
                
                eventBus.emit('websocket:disconnected', { 
                    code: event.code, 
                    reason: event.reason 
                });
                
                if (event.code !== 1000 && this.reconnectAttempts < this.options.maxReconnectAttempts) {
                    this.scheduleReconnect();
                }
            };
            
            this.ws.onerror = (error) => {
                console.error('[WebSocketService] Error:', error);
                eventBus.emit('websocket:error', { error });
            };
            
        } catch (e) {
            console.error('[WebSocketService] Connection failed:', e);
            this.scheduleReconnect();
        }
    }

    disconnect() {
        this.stopHeartbeat();
        if (this.tokenRefreshTimer) {
            clearTimeout(this.tokenRefreshTimer);
            this.tokenRefreshTimer = null;
        }
        if (this.ws) {
            this.ws.close(1000, 'Client disconnect');
            this.ws = null;
        }
        this.isConnected = false;
        this.currentToken = null;
    }

    scheduleReconnect() {
        this.reconnectAttempts++;
        console.log(`[WebSocketService] Reconnecting in ${this.options.reconnectInterval}ms (attempt ${this.reconnectAttempts})`);
        
        setTimeout(() => {
            if (this.currentSceneGroupId && this.currentToken) {
                this.connectWithToken(this.currentSceneGroupId);
            } else {
                this.connect();
            }
        }, this.options.reconnectInterval);
    }

    startHeartbeat() {
        this.heartbeatTimer = setInterval(() => {
            if (this.isConnected) {
                this.send({ type: 'ping' });
            }
        }, this.options.heartbeatInterval);
    }

    stopHeartbeat() {
        if (this.heartbeatTimer) {
            clearInterval(this.heartbeatTimer);
            this.heartbeatTimer = null;
        }
    }

    send(data) {
        if (!this.isConnected) {
            this.messageQueue.push(data);
            return false;
        }
        
        try {
            const message = typeof data === 'string' ? data : JSON.stringify(data);
            this.ws.send(message);
            return true;
        } catch (e) {
            console.error('[WebSocketService] Send error:', e);
            return false;
        }
    }

    flushMessageQueue() {
        while (this.messageQueue.length > 0 && this.isConnected) {
            const data = this.messageQueue.shift();
            this.send(data);
        }
    }

    handleMessage(data) {
        const { type, payload } = data;
        
        switch (type) {
            case 'pong':
                break;
                
            case 'connected':
                console.log('[WebSocketService] Server confirmed connection, authenticated:', data.authenticated);
                eventBus.emit('websocket:ready', data);
                break;
                
            case 'auth_failed':
                console.error('[WebSocketService] Authentication failed:', data.error);
                eventBus.emit('websocket:auth_failed', data);
                break;
                
            case 'permission_denied':
                console.error('[WebSocketService] Permission denied:', data.error);
                eventBus.emit('websocket:permission_denied', data);
                break;
                
            case 'chat_message':
            case 'message':
                eventBus.emit('im:message', payload || data);
                break;
                
            case 'agent_response':
                eventBus.emit('assistant:response', payload);
                break;
                
            case 'todo_update':
                eventBus.emit('todo:update', payload);
                break;
                
            case 'typing':
                eventBus.emit('im:typing', payload);
                break;
                
            case 'participant_status':
                eventBus.emit('im:participantStatus', payload);
                break;
                
            case 'offline_messages':
                eventBus.emit('im:offline_messages', payload || data.messages);
                break;
                
            default:
                eventBus.emit('websocket:message', data);
        }
    }

    subscribe(channel, callback) {
        const unsubscribe = eventBus.on(`websocket:${channel}`, callback);
        this.subscriptions.set(callback, unsubscribe);
        return unsubscribe;
    }

    unsubscribe(callback) {
        const unsubscribe = this.subscriptions.get(callback);
        if (unsubscribe) {
            unsubscribe();
            this.subscriptions.delete(callback);
        }
    }

    sendChatMessage(sceneGroupId, content, toParticipantId = null) {
        return this.send({
            type: 'message',
            sceneGroupId,
            content,
            toParticipantId
        });
    }

    joinSceneGroup(sceneGroupId) {
        return this.send({
            type: 'join_scene',
            sceneGroupId
        });
    }

    leaveSceneGroup(sceneGroupId) {
        return this.send({
            type: 'leave_scene',
            sceneGroupId
        });
    }

    sendTyping(sceneGroupId, isTyping) {
        return this.send({
            type: 'typing',
            sceneGroupId,
            isTyping
        });
    }
    
    sendReadAck(messageId) {
        return this.send({
            type: 'read_ack',
            messageId
        });
    }
}

export const wsService = new WebSocketService();

export default WebSocketService;
