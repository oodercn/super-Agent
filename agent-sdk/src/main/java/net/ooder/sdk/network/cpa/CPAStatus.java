package net.ooder.sdk.network.cpa;

public enum CPAStatus {
    /**
     * 初始化状态
     */
    INITIALIZED,
    
    /**
     * 构建中
     */
    BUILDING,
    
    /**
     * 已构建
     */
    BUILT,
    
    /**
     * 验证中
     */
    VALIDATING,
    
    /**
     * 已验证
     */
    VALIDATED,
    
    /**
     * 不一致
     */
    INCONSISTENT,
    
    /**
     * 错误
     */
    ERROR
}
