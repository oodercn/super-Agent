package net.ooder.nexus.skillcenter.dto.domain;

import java.util.List;
import java.util.Map;

public class StaticTopologyDTO {
    private String topologyId;
    private String domainId;
    private TopologySpec spec;
    private TopologyStatus status;
    private long createdAt;
    private long updatedAt;

    public String getTopologyId() { return topologyId; }
    public void setTopologyId(String topologyId) { this.topologyId = topologyId; }
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    public TopologySpec getSpec() { return spec; }
    public void setSpec(TopologySpec spec) { this.spec = spec; }
    public TopologyStatus getStatus() { return status; }
    public void setStatus(TopologyStatus status) { this.status = status; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public static class TopologySpec {
        private List<NodeSpec> nodes;
        private List<EdgeSpec> edges;
        private Map<String, Object> constraints;
        private String deploymentStrategy;

        public List<NodeSpec> getNodes() { return nodes; }
        public void setNodes(List<NodeSpec> nodes) { this.nodes = nodes; }
        public List<EdgeSpec> getEdges() { return edges; }
        public void setEdges(List<EdgeSpec> edges) { this.edges = edges; }
        public Map<String, Object> getConstraints() { return constraints; }
        public void setConstraints(Map<String, Object> constraints) { this.constraints = constraints; }
        public String getDeploymentStrategy() { return deploymentStrategy; }
        public void setDeploymentStrategy(String deploymentStrategy) { this.deploymentStrategy = deploymentStrategy; }
    }

    public static class NodeSpec {
        private String nodeId;
        private String nodeType;
        private String role;
        private int priority;
        private Map<String, Object> capabilities;
        private List<String> requiredSkills;

        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getNodeType() { return nodeType; }
        public void setNodeType(String nodeType) { this.nodeType = nodeType; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
    }

    public static class EdgeSpec {
        private String sourceNodeId;
        private String targetNodeId;
        private String edgeType;
        private int bandwidth;
        private int latency;
        private boolean bidirectional;

        public String getSourceNodeId() { return sourceNodeId; }
        public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }
        public String getTargetNodeId() { return targetNodeId; }
        public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
        public String getEdgeType() { return edgeType; }
        public void setEdgeType(String edgeType) { this.edgeType = edgeType; }
        public int getBandwidth() { return bandwidth; }
        public void setBandwidth(int bandwidth) { this.bandwidth = bandwidth; }
        public int getLatency() { return latency; }
        public void setLatency(int latency) { this.latency = latency; }
        public boolean isBidirectional() { return bidirectional; }
        public void setBidirectional(boolean bidirectional) { this.bidirectional = bidirectional; }
    }

    public static class TopologyStatus {
        private String phase;
        private String message;
        private int deployedNodes;
        private int totalNodes;
        private int activeEdges;
        private int totalEdges;

        public String getPhase() { return phase; }
        public void setPhase(String phase) { this.phase = phase; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public int getDeployedNodes() { return deployedNodes; }
        public void setDeployedNodes(int deployedNodes) { this.deployedNodes = deployedNodes; }
        public int getTotalNodes() { return totalNodes; }
        public void setTotalNodes(int totalNodes) { this.totalNodes = totalNodes; }
        public int getActiveEdges() { return activeEdges; }
        public void setActiveEdges(int activeEdges) { this.activeEdges = activeEdges; }
        public int getTotalEdges() { return totalEdges; }
        public void setTotalEdges(int totalEdges) { this.totalEdges = totalEdges; }
    }
}
