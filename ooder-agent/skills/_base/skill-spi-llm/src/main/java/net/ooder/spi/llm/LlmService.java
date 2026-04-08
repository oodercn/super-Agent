package net.ooder.spi.llm;

import net.ooder.spi.llm.model.LlmRequest;
import net.ooder.spi.llm.model.LlmResponse;
import net.ooder.spi.llm.model.LlmModel;
import net.ooder.spi.llm.model.LlmConfig;

import java.util.List;

public interface LlmService {
    
    String getProviderId();
    
    String getProviderName();
    
    List<LlmModel> getAvailableModels();
    
    LlmResponse chat(LlmRequest request);
    
    void chatStream(LlmRequest request, LlmStreamHandler handler);
    
    boolean isAvailable();
    
    LlmConfig getDefaultConfig();
    
    int getMaxTokens(String modelId);
    
    boolean supportsStreaming(String modelId);
}
