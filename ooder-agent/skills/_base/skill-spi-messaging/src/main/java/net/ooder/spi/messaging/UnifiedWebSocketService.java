package net.ooder.spi.messaging;

import net.ooder.spi.messaging.model.UnifiedMessage;
import net.ooder.spi.messaging.model.WsToken;

public interface UnifiedWebSocketService {
    
    WsToken generateToken(String userId, String sceneGroupId, long expireSeconds);
    
    WsToken refreshToken(String currentToken);
    
    void broadcast(String sceneGroupId, UnifiedMessage message);
    
    void sendToUser(String userId, UnifiedMessage message);
    
    void sendToConversation(String conversationId, UnifiedMessage message);
    
    void subscribe(String conversationId, String userId);
    
    void unsubscribe(String conversationId, String userId);
}
