package net.ooder.nexus.skillcenter.dto.hosting;

import java.util.List;
import java.util.Map;

public class AutoScalePolicyDTO {
    private String policyId;
    private String instanceId;
    private String name;
    private boolean enabled;
    private int minReplicas;
    private int maxReplicas;
    private List<ScaleRule> scaleUpRules;
    private List<ScaleRule> scaleDownRules;
    private long cooldownPeriod;
    private Map<String, Object> metadata;

    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getMinReplicas() { return minReplicas; }
    public void setMinReplicas(int minReplicas) { this.minReplicas = minReplicas; }
    public int getMaxReplicas() { return maxReplicas; }
    public void setMaxReplicas(int maxReplicas) { this.maxReplicas = maxReplicas; }
    public List<ScaleRule> getScaleUpRules() { return scaleUpRules; }
    public void setScaleUpRules(List<ScaleRule> scaleUpRules) { this.scaleUpRules = scaleUpRules; }
    public List<ScaleRule> getScaleDownRules() { return scaleDownRules; }
    public void setScaleDownRules(List<ScaleRule> scaleDownRules) { this.scaleDownRules = scaleDownRules; }
    public long getCooldownPeriod() { return cooldownPeriod; }
    public void setCooldownPeriod(long cooldownPeriod) { this.cooldownPeriod = cooldownPeriod; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public static class ScaleRule {
        private String metricName;
        private String metricType;
        private double threshold;
        private String operator;
        private int step;
        private long evaluationPeriod;

        public String getMetricName() { return metricName; }
        public void setMetricName(String metricName) { this.metricName = metricName; }
        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
        public double getThreshold() { return threshold; }
        public void setThreshold(double threshold) { this.threshold = threshold; }
        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
        public int getStep() { return step; }
        public void setStep(int step) { this.step = step; }
        public long getEvaluationPeriod() { return evaluationPeriod; }
        public void setEvaluationPeriod(long evaluationPeriod) { this.evaluationPeriod = evaluationPeriod; }
    }
}
