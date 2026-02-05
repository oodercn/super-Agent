package net.ooder.skillcenter.p2p;

/**
 * P2P事件类型枚举
 */
public enum P2PEventType {
    /**
     * P2P服务启动事件
     */
    SERVICE_STARTED,
    
    /**
     * P2P服务停止事件
     */
    SERVICE_STOPPED,
    
    /**
     * 节点发现事件
     */
    NODE_DISCOVERED,
    
    /**
     * 节点丢失事件
     */
    NODE_LOST,
    
    /**
     * 节点状态更新事件
     */
    NODE_STATUS_UPDATED,
    
    /**
     * 技能共享事件
     */
    SKILL_SHARED,
    
    /**
     * 技能取消共享事件
     */
    SKILL_UNSHARED,
    
    /**
     * 共享技能更新事件
     */
    SKILLS_UPDATED,
    
    /**
     * 技能执行请求事件
     */
    SKILL_EXECUTION_REQUEST,
    
    /**
     * 技能执行结果事件
     */
    SKILL_EXECUTION_RESULT,
    
    /**
     * 数据共享事件
     */
    DATA_SHARED,
    
    /**
     * 数据请求事件
     */
    DATA_REQUESTED,
    
    /**
     * 数据传输完成事件
     */
    DATA_TRANSFER_COMPLETED,
    
    /**
     * P2P网络连接事件
     */
    CONNECTION_ESTABLISHED,
    
    /**
     * P2P网络断开连接事件
     */
    CONNECTION_CLOSED
}