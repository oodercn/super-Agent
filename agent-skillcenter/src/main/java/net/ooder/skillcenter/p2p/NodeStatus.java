package net.ooder.skillcenter.p2p;

/**
 * P2P节点状态枚举
 */
public enum NodeStatus {
    /**
     * 在线状态
     * 节点正常运行，可以提供服务
     */
    ONLINE,
    
    /**
     * 离线状态
     * 节点无法连接，不提供服务
     */
    OFFLINE,
    
    /**
     * 繁忙状态
     * 节点正在处理请求，暂时无法接受新的请求
     */
    BUSY,
    
    /**
     * 维护状态
     * 节点正在进行维护，暂时无法提供服务
     */
    MAINTENANCE,
    
    /**
     * 发现中状态
     * 节点正在被发现，尚未确认可用
     */
    DISCOVERING,
    
    /**
     * 连接中状态
     * 正在尝试连接到节点
     */
    CONNECTING,
    
    /**
     * 错误状态
     * 节点出现错误，需要进行修复
     */
    ERROR
}