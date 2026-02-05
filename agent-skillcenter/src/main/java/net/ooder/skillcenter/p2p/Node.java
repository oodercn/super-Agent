package net.ooder.skillcenter.p2p;

/**
 * P2P网络中的节点信息
 */
public class Node {
    // 节点唯一标识符
    private String id;
    
    // 节点名称
    private String name;
    
    // 节点类型
    private NodeType type;
    
    // 节点IP地址
    private String ip;
    
    // 节点端口
    private int port;
    
    // 节点状态
    private NodeStatus status;
    
    // 最后一次心跳时间戳
    private long lastSeen;
    
    // 节点描述信息
    private String description;
    
    // 节点所有者信息
    private String owner;
    
    // 节点所在网络位置
    private String networkLocation;
    
    // Getters and Setters
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
    
    public NodeType getType() {
        return type;
    }
    
    public void setType(NodeType type) {
        this.type = type;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public NodeStatus getStatus() {
        return status;
    }
    
    public void setStatus(NodeStatus status) {
        this.status = status;
    }
    
    public long getLastSeen() {
        return lastSeen;
    }
    
    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public String getNetworkLocation() {
        return networkLocation;
    }
    
    public void setNetworkLocation(String networkLocation) {
        this.networkLocation = networkLocation;
    }
}