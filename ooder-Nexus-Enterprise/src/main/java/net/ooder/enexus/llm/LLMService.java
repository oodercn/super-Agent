package net.ooder.enexus.llm;

import java.util.List;

public interface LLMService {
    
    LLMResponse chat(LLMRequest request);
    
    List<Double> getEmbedding(String text);
}
