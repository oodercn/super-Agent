package net.ooder.skillcenter.sdk.cloud;

import java.util.List;

public class ScaleConfig {
    private int minReplicas;
    private int maxReplicas;
    private List<ScaleRule> scaleUpRules;
    private List<ScaleRule> scaleDownRules;

    public int getMinReplicas() { return minReplicas; }
    public void setMinReplicas(int minReplicas) { this.minReplicas = minReplicas; }
    public int getMaxReplicas() { return maxReplicas; }
    public void setMaxReplicas(int maxReplicas) { this.maxReplicas = maxReplicas; }
    public List<ScaleRule> getScaleUpRules() { return scaleUpRules; }
    public void setScaleUpRules(List<ScaleRule> scaleUpRules) { this.scaleUpRules = scaleUpRules; }
    public List<ScaleRule> getScaleDownRules() { return scaleDownRules; }
    public void setScaleDownRules(List<ScaleRule> scaleDownRules) { this.scaleDownRules = scaleDownRules; }

    public static class ScaleRule {
        private String metricName;
        private double threshold;
        private String operator;
        private int step;

        public String getMetricName() { return metricName; }
        public void setMetricName(String metricName) { this.metricName = metricName; }
        public double getThreshold() { return threshold; }
        public void setThreshold(double threshold) { this.threshold = threshold; }
        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
        public int getStep() { return step; }
        public void setStep(int step) { this.step = step; }
    }
}
