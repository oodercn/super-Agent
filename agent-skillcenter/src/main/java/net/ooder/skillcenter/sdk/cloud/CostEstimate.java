package net.ooder.skillcenter.sdk.cloud;

public class CostEstimate {
    private double hourlyCost;
    private double dailyCost;
    private double monthlyCost;
    private String currency;
    private CostBreakdown breakdown;

    public double getHourlyCost() {
        return hourlyCost;
    }

    public void setHourlyCost(double hourlyCost) {
        this.hourlyCost = hourlyCost;
    }

    public double getDailyCost() {
        return dailyCost;
    }

    public void setDailyCost(double dailyCost) {
        this.dailyCost = dailyCost;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(double monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CostBreakdown getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(CostBreakdown breakdown) {
        this.breakdown = breakdown;
    }
}
