package net.ooder.sdk.network.gossip;

public enum GossipMessageType {
    /**
     * 链路表更新
     */
    LINK_TABLE_UPDATE,
    
    /**
     * 节点状态更新
     */
    NODE_STATUS_UPDATE,
    
    /**
     * CPA 构建
     */
    CPA_BUILD,
    
    /**
     * 心跳消息
     */
    HEARTBEAT,
    
    /**
     * 其他消息
     */
    OTHER
}
