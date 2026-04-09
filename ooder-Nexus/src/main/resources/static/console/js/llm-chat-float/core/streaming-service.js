/**
 * SSE 流式输出服务
 * 支持AI回复流式显示
 */
import { eventBus } from './event-bus.js';

export class StreamingService {
    constructor() {
        this.activeConnections = new Map();
    }

    async streamChat(options) {
        const {
            url = '/api/v1/llm/chat/stream',
            message,
            sessionId,
            provider,
            model,
            onChunk,
            onComplete,
            onError
        } = options;
        
        const connectionId = 'stream-' + Date.now();
        
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'text/event-stream'
                },
                body: JSON.stringify({
                    message,
                    sessionId,
                    provider,
                    model,
                    stream: true
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const reader = response.body.getReader();
            const decoder = new TextDecoder();
            let buffer = '';
            let fullContent = '';

            this.activeConnections.set(connectionId, { reader, aborted: false });

            while (true) {
                const { done, value } = await reader.read();
                
                if (done) break;
                
                buffer += decoder.decode(value, { stream: true });
                
                const lines = buffer.split('\n');
                buffer = lines.pop() || '';
                
                let currentEvent = '';
                
                for (const line of lines) {
                    if (line.startsWith('event:')) {
                        currentEvent = line.slice(6).trim();
                        continue;
                    }
                    
                    if (line.startsWith('data:')) {
                        const data = line.slice(5);
                        
                        if (data === '[DONE]' || currentEvent === 'done') {
                            currentEvent = '';
                            continue;
                        }
                        
                        if (currentEvent === 'message' || !currentEvent) {
                            fullContent += data;
                            
                            if (onChunk) {
                                onChunk(data, fullContent);
                            }
                            
                            eventBus.emit('streaming:chunk', {
                                connectionId,
                                chunk: data,
                                fullContent
                            });
                        }
                        
                        continue;
                    }
                    
                    if (line.trim() && !line.startsWith(':')) {
                        fullContent += line;
                        
                        if (onChunk) {
                            onChunk(line, fullContent);
                        }
                        
                        eventBus.emit('streaming:chunk', {
                            connectionId,
                            chunk: line,
                            fullContent
                        });
                    }
                }
            }

            this.activeConnections.delete(connectionId);
            
            if (onComplete) {
                onComplete(fullContent);
            }
            
            eventBus.emit('streaming:complete', {
                connectionId,
                content: fullContent
            });
            
            return fullContent;

        } catch (error) {
            this.activeConnections.delete(connectionId);
            
            if (onError) {
                onError(error);
            }
            
            eventBus.emit('streaming:error', {
                connectionId,
                error
            });
            
            throw error;
        }
    }

    abort(connectionId) {
        const connection = this.activeConnections.get(connectionId);
        if (connection) {
            connection.aborted = true;
            if (connection.reader) {
                connection.reader.cancel();
            }
            this.activeConnections.delete(connectionId);
        }
    }

    abortAll() {
        for (const [id, connection] of this.activeConnections) {
            this.abort(id);
        }
    }

    isStreaming(connectionId) {
        return this.activeConnections.has(connectionId);
    }
}

export const streamingService = new StreamingService();

export default StreamingService;
