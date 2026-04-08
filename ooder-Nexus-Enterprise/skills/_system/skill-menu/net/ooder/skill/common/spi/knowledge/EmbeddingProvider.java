package net.ooder.skill.common.spi.knowledge;

import java.util.List;

public interface EmbeddingProvider {
    
    float[] embed(String text);
    
    List<float[]> embedBatch(List<String> texts);
    
    int getDimension();
    
    String getModelName();
    
    boolean isAvailable();
}
