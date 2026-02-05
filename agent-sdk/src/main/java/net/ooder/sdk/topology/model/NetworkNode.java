package net.ooder.sdk.topology.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class NetworkNode {
    @JSONField(name = "nodeId")
    private String nodeId;
    
    @JSONField(name = "nodeName")
    private String nodeName;
    
    @JSONField(name = "nodeType")
    private NodeType nodeType;
    
    @JSONField(name = "ipAddress")
    private String ipAddress;
    
    @JSONField(name = "macAddress")
    private String macAddress;
    
    @JSONField(name = "status")
    private NodeStatus status;
    
    @JSONField(name = "position")
    private NodePosition position;
    
    @JSONField(name = "properties")
    private Map<String, Object> properties;
    
    @JSONField(name = "lastUpdated")
    private long lastUpdated;
    
    public NetworkNode() {
        this.nodeId = UUID.randomUUID().toString();
        this.status = NodeStatus.UNKNOWN;
        this.lastUpdated = System.currentTimeMillis();
    }
    
    public NetworkNode(String nodeName, NodeType nodeType, String ipAddress) {
        this();
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.ipAddress = ipAddress;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
        this.lastUpdated = System.currentTimeMillis();
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        this.lastUpdated = System.currentTimeMillis();
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
        this.lastUpdated = System.currentTimeMillis();
    }

    public NodePosition getPosition() {
        return position;
    }

    public void setPosition(NodePosition position) {
        this.position = position;
        this.lastUpdated = System.currentTimeMillis();
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "NetworkNode{" +
                "nodeId='" + nodeId + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodeType=" + nodeType +
                ", status=" + status +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
