package net.ooder.nexus.domain.end.model;

import java.io.Serializable;
import java.util.Map;

/**
 * EndAgent Stats Result
 */
public class EndAgentStatsResult implements Serializable {
    private int totalAgents;
    private Map<String, Integer> statusSummary;
    private Map<String, Integer> typeSummary;
    private double onlineRate;
    private long lastDiscoveryTime;
    private Map<String, Map<String, Object>> networkNodes;

    public int getTotalAgents() { return totalAgents; }
    public void setTotalAgents(int totalAgents) { this.totalAgents = totalAgents; }
    public Map<String, Integer> getStatusSummary() { return statusSummary; }
    public void setStatusSummary(Map<String, Integer> statusSummary) { this.statusSummary = statusSummary; }
    public Map<String, Integer> getTypeSummary() { return typeSummary; }
    public void setTypeSummary(Map<String, Integer> typeSummary) { this.typeSummary = typeSummary; }
    public double getOnlineRate() { return onlineRate; }
    public void setOnlineRate(double onlineRate) { this.onlineRate = onlineRate; }
    public long getLastDiscoveryTime() { return lastDiscoveryTime; }
    public void setLastDiscoveryTime(long lastDiscoveryTime) { this.lastDiscoveryTime = lastDiscoveryTime; }
    public Map<String, Map<String, Object>> getNetworkNodes() { return networkNodes; }
    public void setNetworkNodes(Map<String, Map<String, Object>> networkNodes) { this.networkNodes = networkNodes; }
}
