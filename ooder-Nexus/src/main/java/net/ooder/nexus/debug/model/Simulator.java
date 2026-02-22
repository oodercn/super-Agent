package net.ooder.nexus.debug.model;

import net.ooder.nexus.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模拟器模型
 */
public class Simulator implements Serializable {

    private static final long serialVersionUID = 1L;

    private String simulatorId;
    private String name;
    private SimulatorType type;
    private String protocolType;
    private String description;
    private Map<String, Object> config;
    private List<ScenarioReference> scenarios;
    private BehaviorConfig behavior;
    private String status;
    private String createdAt;
    private String updatedAt;

    public Simulator() {
        this.config = new HashMap<>();
        this.scenarios = new ArrayList<>();
        this.behavior = new BehaviorConfig();
        this.status = "STOPPED";
    }

    public Simulator(String simulatorId, String name, SimulatorType type, String protocolType) {
        this();
        this.simulatorId = simulatorId;
        this.name = name;
        this.type = type;
        this.protocolType = protocolType;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("simulatorId", simulatorId);
        map.put("name", name);
        map.put("type", type != null ? type.name() : null);
        map.put("protocolType", protocolType);
        map.put("description", description);
        map.put("config", config);
        map.put("scenarios", scenarios);
        map.put("behavior", behavior != null ? behavior.toMap() : null);
        map.put("status", status);
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        return map;
    }

    public String toJson() {
        return JsonUtils.toJson(this);
    }

    // Getters and Setters
    public String getSimulatorId() { return simulatorId; }
    public void setSimulatorId(String simulatorId) { this.simulatorId = simulatorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public SimulatorType getType() { return type; }
    public void setType(SimulatorType type) { this.type = type; }
    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    public List<ScenarioReference> getScenarios() { return scenarios; }
    public void setScenarios(List<ScenarioReference> scenarios) { this.scenarios = scenarios; }
    public BehaviorConfig getBehavior() { return behavior; }
    public void setBehavior(BehaviorConfig behavior) { this.behavior = behavior; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    /**
     * 场景引用
     */
    public static class ScenarioReference implements Serializable {
        private static final long serialVersionUID = 1L;

        private String scenarioId;
        private String name;
        private String description;

        public ScenarioReference() {}

        public ScenarioReference(String scenarioId, String name) {
            this.scenarioId = scenarioId;
            this.name = name;
        }

        public String getScenarioId() { return scenarioId; }
        public void setScenarioId(String scenarioId) { this.scenarioId = scenarioId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    /**
     * 行为配置
     */
    public static class BehaviorConfig implements Serializable {
        private static final long serialVersionUID = 1L;

        private boolean autoRegister;
        private boolean respondToQueries;
        private boolean generateHeartbeat;
        private boolean generateStatus;
        private RandomDelayConfig randomDelay;

        public BehaviorConfig() {
            this.autoRegister = true;
            this.respondToQueries = true;
            this.generateHeartbeat = true;
            this.generateStatus = true;
            this.randomDelay = new RandomDelayConfig();
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("autoRegister", autoRegister);
            map.put("respondToQueries", respondToQueries);
            map.put("generateHeartbeat", generateHeartbeat);
            map.put("generateStatus", generateStatus);
            map.put("randomDelay", randomDelay != null ? randomDelay.toMap() : null);
            return map;
        }

        public boolean isAutoRegister() { return autoRegister; }
        public void setAutoRegister(boolean autoRegister) { this.autoRegister = autoRegister; }
        public boolean isRespondToQueries() { return respondToQueries; }
        public void setRespondToQueries(boolean respondToQueries) { this.respondToQueries = respondToQueries; }
        public boolean isGenerateHeartbeat() { return generateHeartbeat; }
        public void setGenerateHeartbeat(boolean generateHeartbeat) { this.generateHeartbeat = generateHeartbeat; }
        public boolean isGenerateStatus() { return generateStatus; }
        public void setGenerateStatus(boolean generateStatus) { this.generateStatus = generateStatus; }
        public RandomDelayConfig getRandomDelay() { return randomDelay; }
        public void setRandomDelay(RandomDelayConfig randomDelay) { this.randomDelay = randomDelay; }
    }

    /**
     * 随机延迟配置
     */
    public static class RandomDelayConfig implements Serializable {
        private static final long serialVersionUID = 1L;

        private boolean enabled;
        private int minMs;
        private int maxMs;

        public RandomDelayConfig() {
            this.enabled = true;
            this.minMs = 100;
            this.maxMs = 500;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("enabled", enabled);
            map.put("minMs", minMs);
            map.put("maxMs", maxMs);
            return map;
        }

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getMinMs() { return minMs; }
        public void setMinMs(int minMs) { this.minMs = minMs; }
        public int getMaxMs() { return maxMs; }
        public void setMaxMs(int maxMs) { this.maxMs = maxMs; }
    }
}
