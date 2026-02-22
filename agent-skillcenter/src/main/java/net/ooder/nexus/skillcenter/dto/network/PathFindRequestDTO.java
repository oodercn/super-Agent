package net.ooder.nexus.skillcenter.dto.network;

public class PathFindRequestDTO {

    private String sourceNode;
    private String targetNode;
    private String algorithm;
    private int maxHops;
    private boolean preferLowLatency;

    public PathFindRequestDTO() {}

    public String getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(String sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(String targetNode) {
        this.targetNode = targetNode;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getMaxHops() {
        return maxHops;
    }

    public void setMaxHops(int maxHops) {
        this.maxHops = maxHops;
    }

    public boolean isPreferLowLatency() {
        return preferLowLatency;
    }

    public void setPreferLowLatency(boolean preferLowLatency) {
        this.preferLowLatency = preferLowLatency;
    }
}
