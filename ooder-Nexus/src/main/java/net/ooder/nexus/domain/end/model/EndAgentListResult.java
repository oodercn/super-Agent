package net.ooder.nexus.domain.end.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import net.ooder.nexus.domain.network.model.EndAgent;

/**
 * EndAgent列表结果
 * 用于EndAgentController中getEndAgents方法的返回类型
 */
public class EndAgentListResult implements Serializable {
    private List<EndAgent> agents;
    private int total;
    private Map<String, Integer> statusSummary;
    private Map<String, Integer> typeSummary;
    private long lastDiscoveryTime;
    private boolean discoveryInProgress;

    public List<EndAgent> getAgents() {
        return agents;
    }

    public void setAgents(List<EndAgent> agents) {
        this.agents = agents;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Map<String, Integer> getStatusSummary() {
        return statusSummary;
    }

    public void setStatusSummary(Map<String, Integer> statusSummary) {
        this.statusSummary = statusSummary;
    }

    public Map<String, Integer> getTypeSummary() {
        return typeSummary;
    }

    public void setTypeSummary(Map<String, Integer> typeSummary) {
        this.typeSummary = typeSummary;
    }

    public long getLastDiscoveryTime() {
        return lastDiscoveryTime;
    }

    public void setLastDiscoveryTime(long lastDiscoveryTime) {
        this.lastDiscoveryTime = lastDiscoveryTime;
    }

    public boolean isDiscoveryInProgress() {
        return discoveryInProgress;
    }

    public void setDiscoveryInProgress(boolean discoveryInProgress) {
        this.discoveryInProgress = discoveryInProgress;
    }
}
