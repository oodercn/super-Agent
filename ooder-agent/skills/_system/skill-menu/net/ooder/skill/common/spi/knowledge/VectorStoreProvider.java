package net.ooder.skill.common.spi.knowledge;

import java.util.List;
import java.util.Map;

public interface VectorStoreProvider {
    
    void add(String collectionName, List<VectorData> vectors);
    
    void update(String collectionName, List<VectorData> vectors);
    
    void delete(String collectionName, List<String> ids);
    
    List<VectorData> search(String collectionName, float[] queryVector, int topK);
    
    List<VectorData> search(String collectionName, float[] queryVector, int topK, Map<String, Object> filter);
    
    void createCollection(String collectionName, int dimension);
    
    void dropCollection(String collectionName);
    
    boolean collectionExists(String collectionName);
    
    long count(String collectionName);
    
    class VectorData {
        private String id;
        private float[] vector;
        private Map<String, Object> metadata;
        private String content;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public float[] getVector() { return vector; }
        public void setVector(float[] vector) { this.vector = vector; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
