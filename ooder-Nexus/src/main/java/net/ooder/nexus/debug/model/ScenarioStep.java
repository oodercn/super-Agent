package net.ooder.nexus.debug.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 场景步骤模型
 */
public class ScenarioStep implements Serializable {

    private static final long serialVersionUID = 1L;

    private int stepId;
    private String action;
    private String description;
    private int delay;
    private int repeat;
    private int interval;
    private int batchSize;
    private boolean concurrent;
    private Map<String, Object> request;

    public ScenarioStep() {}

    public ScenarioStep(int stepId, String action, String description) {
        this.stepId = stepId;
        this.action = action;
        this.description = description;
    }

    // Getters and Setters
    public int getStepId() { return stepId; }
    public void setStepId(int stepId) { this.stepId = stepId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDelay() { return delay; }
    public void setDelay(int delay) { this.delay = delay; }
    public int getRepeat() { return repeat; }
    public void setRepeat(int repeat) { this.repeat = repeat; }
    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
    public boolean isConcurrent() { return concurrent; }
    public void setConcurrent(boolean concurrent) { this.concurrent = concurrent; }
    public Map<String, Object> getRequest() { return request; }
    public void setRequest(Map<String, Object> request) { this.request = request; }

    /**
     * 获取请求负载
     */
    public Map<String, Object> buildRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("action", action);
        request.put("description", description);
        request.put("delay", delay);
        request.put("repeat", repeat);
        request.put("interval", interval);
        request.put("batchSize", batchSize);
        request.put("concurrent", concurrent);
        return request;
    }

    /**
     * 步骤动作类型
     */
    public static class Actions {
        public static final String REGISTER = "REGISTER";
        public static final String DEREGISTER = "DEREGISTER";
        public static final String HEARTBEAT = "HEARTBEAT";
        public static final String STATUS = "STATUS";
        public static final String QUERY = "QUERY";
        public static final String CONFIG = "CONFIG";
        public static final String DISCOVER = "DISCOVER";
        public static final String WAIT = "WAIT";
        public static final String SEND = "SEND";
        public static final String RECEIVE = "RECEIVE";
    }
}
