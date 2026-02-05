package net.ooder.nexus.model.system;

import java.io.Serializable;

public class ResourceUsage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String unit;
    private double value;
    private double minValue;
    private double maxValue;
    private double warningThreshold;
    private double criticalThreshold;
    private long lastUpdated;

    public ResourceUsage() {
    }

    public ResourceUsage(String id, String name, String unit, double value) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.value = value;
        this.minValue = 0;
        this.maxValue = 100;
        this.warningThreshold = 80;
        this.criticalThreshold = 90;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getWarningThreshold() {
        return warningThreshold;
    }

    public void setWarningThreshold(double warningThreshold) {
        this.warningThreshold = warningThreshold;
    }

    public double getCriticalThreshold() {
        return criticalThreshold;
    }

    public void setCriticalThreshold(double criticalThreshold) {
        this.criticalThreshold = criticalThreshold;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
