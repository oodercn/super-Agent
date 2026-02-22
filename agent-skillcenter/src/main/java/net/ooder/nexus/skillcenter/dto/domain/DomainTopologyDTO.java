package net.ooder.nexus.skillcenter.dto.domain;

import java.util.List;
import java.util.Map;

public class DomainTopologyDTO {
    private String topologyId;
    private String domainId;
    private List<TopologyNodeDTO> nodes;
    private List<TopologyEdgeDTO> edges;
    private Map<String, Object> metadata;
    private long lastUpdated;

    public String getTopologyId() { return topologyId; }
    public void setTopologyId(String topologyId) { this.topologyId = topologyId; }
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    public List<TopologyNodeDTO> getNodes() { return nodes; }
    public void setNodes(List<TopologyNodeDTO> nodes) { this.nodes = nodes; }
    public List<TopologyEdgeDTO> getEdges() { return edges; }
    public void setEdges(List<TopologyEdgeDTO> edges) { this.edges = edges; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

    public static class TopologyNodeDTO {
        private String nodeId;
        private String agentId;
        private String nodeType;
        private String role;
        private String status;
        private Map<String, Object> capabilities;
        private Map<String, Object> position;

        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getNodeType() { return nodeType; }
        public void setNodeType(String nodeType) { this.nodeType = nodeType; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
        public Map<String, Object> getPosition() { return position; }
        public void setPosition(Map<String, Object> position) { this.position = position; }
    }

    public static class TopologyEdgeDTO {
        private String edgeId;
        private String sourceNodeId;
        private String targetNodeId;
        private String edgeType;
        private String status;
        private Map<String, Object> properties;

        public String getEdgeId() { return edgeId; }
        public void setEdgeId(String edgeId) { this.edgeId = edgeId; }
        public String getSourceNodeId() { return sourceNodeId; }
        public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }
        public String getTargetNodeId() { return targetNodeId; }
        public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
        public String getEdgeType() { return edgeType; }
        public void setEdgeType(String edgeType) { this.edgeType = edgeType; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    }
}
