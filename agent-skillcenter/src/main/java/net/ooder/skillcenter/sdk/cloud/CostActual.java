package net.ooder.skillcenter.sdk.cloud;

public class CostActual {
    private String instanceId;
    private double totalCost;
    private String currency;
    private long startTime;
    private long endTime;
    private CostBreakdown breakdown;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public CostBreakdown getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(CostBreakdown breakdown) {
        this.breakdown = breakdown;
    }
}
