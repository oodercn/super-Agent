package net.ooder.spi.messaging;

import net.ooder.spi.messaging.model.UnifiedMessage;

public interface MessageStreamHandler {
    
    void onStart();
    
    void onChunk(String chunk);
    
    void onComplete(UnifiedMessage completeMessage);
    
    void onError(Throwable error);
}
