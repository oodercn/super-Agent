package net.ooder.mvp.skill.scene.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "scene")
public class SceneProperties {
    
    private DecisionConfig decision = new DecisionConfig();
    private RuleConfig rule = new RuleConfig();
    private KnowledgeConfig knowledge = new KnowledgeConfig();
    private LlmConfig llm = new LlmConfig();
    private VectorConfig vector = new VectorConfig();
    
    public DecisionConfig getDecision() { return decision; }
    public void setDecision(DecisionConfig decision) { this.decision = decision; }
    public RuleConfig getRule() { return rule; }
    public void setRule(RuleConfig rule) { this.rule = rule; }
    public KnowledgeConfig getKnowledge() { return knowledge; }
    public void setKnowledge(KnowledgeConfig knowledge) { this.knowledge = knowledge; }
    public LlmConfig getLlm() { return llm; }
    public void setLlm(LlmConfig llm) { this.llm = llm; }
    public VectorConfig getVector() { return vector; }
    public void setVector(VectorConfig vector) { this.vector = vector; }
    
    public static class DecisionConfig {
        private DecisionMode mode = DecisionMode.ONLINE_FIRST;
        private CacheConfig cache = new CacheConfig();
        private long timeout = 30000;
        
        public DecisionMode getMode() { return mode; }
        public void setMode(DecisionMode mode) { this.mode = mode; }
        public CacheConfig getCache() { return cache; }
        public void setCache(CacheConfig cache) { this.cache = cache; }
        public long getTimeout() { return timeout; }
        public void setTimeout(long timeout) { this.timeout = timeout; }
        
        public static class CacheConfig {
            private boolean enabled = true;
            private long ttl = 300000;
            
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public long getTtl() { return ttl; }
            public void setTtl(long ttl) { this.ttl = ttl; }
        }
    }
    
    public enum DecisionMode {
        ONLINE_ONLY,
        OFFLINE_ONLY,
        ONLINE_FIRST
    }
    
    public static class RuleConfig {
        private EngineConfig engine = new EngineConfig();
        private SandboxConfig sandbox = new SandboxConfig();
        
        public EngineConfig getEngine() { return engine; }
        public void setEngine(EngineConfig engine) { this.engine = engine; }
        public SandboxConfig getSandbox() { return sandbox; }
        public void setSandbox(SandboxConfig sandbox) { this.sandbox = sandbox; }
        
        public static class EngineConfig {
            private int cacheSize = 1000;
            private SandboxEnabledConfig sandbox = new SandboxEnabledConfig();
            private long timeout = 5000;
            
            public int getCacheSize() { return cacheSize; }
            public void setCacheSize(int cacheSize) { this.cacheSize = cacheSize; }
            public SandboxEnabledConfig getSandbox() { return sandbox; }
            public void setSandbox(SandboxEnabledConfig sandbox) { this.sandbox = sandbox; }
            public long getTimeout() { return timeout; }
            public void setTimeout(long timeout) { this.timeout = timeout; }
            
            public static class SandboxEnabledConfig {
                private boolean enabled = true;
                
                public boolean isEnabled() { return enabled; }
                public void setEnabled(boolean enabled) { this.enabled = enabled; }
            }
        }
        
        public static class SandboxConfig {
            private List<String> allowedPackages = new ArrayList<String>();
            private List<String> deniedMethods = new ArrayList<String>();
            
            public SandboxConfig() {
                allowedPackages.add("java.lang");
                allowedPackages.add("java.util");
            }
            
            public List<String> getAllowedPackages() { return allowedPackages; }
            public void setAllowedPackages(List<String> allowedPackages) { this.allowedPackages = allowedPackages; }
            public List<String> getDeniedMethods() { return deniedMethods; }
            public void setDeniedMethods(List<String> deniedMethods) { this.deniedMethods = deniedMethods; }
        }
    }
    
    public static class KnowledgeConfig {
        private LayerConfig layer = new LayerConfig();
        private SearchConfig search = new SearchConfig();
        
        public LayerConfig getLayer() { return layer; }
        public void setLayer(LayerConfig layer) { this.layer = layer; }
        public SearchConfig getSearch() { return search; }
        public void setSearch(SearchConfig search) { this.search = search; }
        
        public static class LayerConfig {
            private LayerEnabledConfig general = new LayerEnabledConfig();
            private LayerEnabledConfig professional = new LayerEnabledConfig();
            private LayerEnabledConfig scene = new LayerEnabledConfig();
            
            public LayerEnabledConfig getGeneral() { return general; }
            public void setGeneral(LayerEnabledConfig general) { this.general = general; }
            public LayerEnabledConfig getProfessional() { return professional; }
            public void setProfessional(LayerEnabledConfig professional) { this.professional = professional; }
            public LayerEnabledConfig getScene() { return scene; }
            public void setScene(LayerEnabledConfig scene) { this.scene = scene; }
            
            public static class LayerEnabledConfig {
                private boolean enabled = true;
                
                public boolean isEnabled() { return enabled; }
                public void setEnabled(boolean enabled) { this.enabled = enabled; }
            }
        }
        
        public static class SearchConfig {
            private int defaultTopK = 5;
            private double defaultThreshold = 0.7;
            private CrossLayerConfig crossLayer = new CrossLayerConfig();
            
            public int getDefaultTopK() { return defaultTopK; }
            public void setDefaultTopK(int defaultTopK) { this.defaultTopK = defaultTopK; }
            public double getDefaultThreshold() { return defaultThreshold; }
            public void setDefaultThreshold(double defaultThreshold) { this.defaultThreshold = defaultThreshold; }
            public CrossLayerConfig getCrossLayer() { return crossLayer; }
            public void setCrossLayer(CrossLayerConfig crossLayer) { this.crossLayer = crossLayer; }
            
            public static class CrossLayerConfig {
                private boolean enabled = true;
                
                public boolean isEnabled() { return enabled; }
                public void setEnabled(boolean enabled) { this.enabled = enabled; }
            }
        }
    }
    
    public static class LlmConfig {
        private ProviderConfig provider = new ProviderConfig();
        private FunctionCallingConfig functionCalling = new FunctionCallingConfig();
        
        public ProviderConfig getProvider() { return provider; }
        public void setProvider(ProviderConfig provider) { this.provider = provider; }
        public FunctionCallingConfig getFunctionCalling() { return functionCalling; }
        public void setFunctionCalling(FunctionCallingConfig functionCalling) { this.functionCalling = functionCalling; }
        
        public static class ProviderConfig {
            private String defaultModel = "gpt-3.5-turbo";
            private long timeout = 60000;
            private int maxRetries = 3;
            
            public String getDefaultModel() { return defaultModel; }
            public void setDefaultModel(String defaultModel) { this.defaultModel = defaultModel; }
            public long getTimeout() { return timeout; }
            public void setTimeout(long timeout) { this.timeout = timeout; }
            public int getMaxRetries() { return maxRetries; }
            public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
        }
        
        public static class FunctionCallingConfig {
            private boolean enabled = true;
            private int maxIterations = 5;
            
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public int getMaxIterations() { return maxIterations; }
            public void setMaxIterations(int maxIterations) { this.maxIterations = maxIterations; }
        }
    }
    
    public static class VectorConfig {
        private int dimension = 1536;
        private StoreConfig store = new StoreConfig();
        
        public int getDimension() { return dimension; }
        public void setDimension(int dimension) { this.dimension = dimension; }
        public StoreConfig getStore() { return store; }
        public void setStore(StoreConfig store) { this.store = store; }
        
        public static class StoreConfig {
            private String type = "memory";
            
            public String getType() { return type; }
            public void setType(String type) { this.type = type; }
        }
    }
}
