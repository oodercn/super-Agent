package net.ooder.mvp.skill.scene.dto.knowledge;

import java.util.List;
import java.util.Map;

public class KnowledgeSearchRequestDTO {
    
    private String query;
    private int topK = 5;
    private double threshold = 0.7;
    private List<String> layers;
    private Map<String, Object> filters;
    
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public int getTopK() { return topK; }
    public void setTopK(int topK) { this.topK = topK; }
    public double getThreshold() { return threshold; }
    public void setThreshold(double threshold) { this.threshold = threshold; }
    public List<String> getLayers() { return layers; }
    public void setLayers(List<String> layers) { this.layers = layers; }
    public Map<String, Object> getFilters() { return filters; }
    public void setFilters(Map<String, Object> filters) { this.filters = filters; }
}
