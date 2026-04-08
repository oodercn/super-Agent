package net.ooder.spi.llm;

import net.ooder.spi.llm.model.LlmResponse;

public interface LlmStreamHandler {
    
    void onStart();
    
    void onChunk(String chunk);
    
    void onComplete(LlmResponse completeResponse);
    
    void onError(Throwable error);
}
