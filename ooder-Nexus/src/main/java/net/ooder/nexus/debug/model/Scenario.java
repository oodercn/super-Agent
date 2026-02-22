package net.ooder.nexus.debug.model;

import net.ooder.nexus.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景模型
 */
public class Scenario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String scenarioId;
    private String name;
    private String type;
    private String protocolType;
    private String description;
    private ScenarioConfig config;
    private List<ScenarioStep> steps;
    private String createdAt;

    public Scenario() {
        this.steps = new ArrayList<>();
    }

    public Scenario(String scenarioId, String name, String protocolType) {
        this();
        this.scenarioId = scenarioId;
        this.name = name;
        this.protocolType = protocolType;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("scenarioId", scenarioId);
        map.put("name", name);
        map.put("type", type);
        map.put("protocolType", protocolType);
        map.put("description", description);
        map.put("config", config != null ? config.toMap() : null);
        map.put("steps", steps);
        map.put("createdAt", createdAt);
        return map;
    }

    public String toJson() {
        return JsonUtils.toJson(this);
    }

    // Getters and Setters
    public String getScenarioId() { return scenarioId; }
    public void setScenarioId(String scenarioId) { this.scenarioId = scenarioId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ScenarioConfig getConfig() { return config; }
    public void setConfig(ScenarioConfig config) { this.config = config; }
    public List<ScenarioStep> getSteps() { return steps; }
    public void setSteps(List<ScenarioStep> steps) { this.steps = steps; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    /**
     * 场景配置
     */
    public static class ScenarioConfig implements Serializable {
        private static final long serialVersionUID = 1L;

        private int concurrentUsers = 1;
        private int requestsPerUser = 10;
        private int rampUpPeriod = 60;

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("concurrentUsers", concurrentUsers);
            map.put("requestsPerUser", requestsPerUser);
            map.put("rampUpPeriod", rampUpPeriod);
            return map;
        }

        public int getConcurrentUsers() { return concurrentUsers; }
        public void setConcurrentUsers(int concurrentUsers) { this.concurrentUsers = concurrentUsers; }
        public int getRequestsPerUser() { return requestsPerUser; }
        public void setRequestsPerUser(int requestsPerUser) { this.requestsPerUser = requestsPerUser; }
        public int getRampUpPeriod() { return rampUpPeriod; }
        public void setRampUpPeriod(int rampUpPeriod) { this.rampUpPeriod = rampUpPeriod; }
    }
}
