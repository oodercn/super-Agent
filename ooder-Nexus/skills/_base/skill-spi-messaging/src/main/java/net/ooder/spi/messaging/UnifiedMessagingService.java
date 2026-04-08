package net.ooder.spi.messaging;

import net.ooder.spi.messaging.model.*;

import java.util.List;

public interface UnifiedMessagingService {
    
    UnifiedMessage sendMessage(SendMessageRequest request);
    
    void streamMessage(SendMessageRequest request, MessageStreamHandler handler);
    
    List<UnifiedMessage> getMessages(String conversationId, int limit, Long before, Long after);
    
    void markAsRead(String conversationId, String userId, String messageId);
    
    void addReaction(String messageId, String userId, String emoji);
    
    void removeReaction(String messageId, String userId, String emoji);
    
    void executeAction(String messageId, String userId, String actionId, java.util.Map<String, Object> params);
}
