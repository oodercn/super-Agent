package net.ooder.skillcenter.sdk.cloud;

public class CostBreakdown {
    private double compute;
    private double storage;
    private double network;
    private double other;
    private double memory;

    public double getCompute() {
        return compute;
    }

    public void setCompute(double compute) {
        this.compute = compute;
    }

    public double getStorage() {
        return storage;
    }

    public void setStorage(double storage) {
        this.storage = storage;
    }

    public double getNetwork() {
        return network;
    }

    public void setNetwork(double network) {
        this.network = network;
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        this.other = other;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }
}
