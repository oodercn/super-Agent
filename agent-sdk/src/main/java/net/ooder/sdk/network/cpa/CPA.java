package net.ooder.sdk.network.cpa;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CPA {
    /**
     * 构建 CPA
     */
    CompletableFuture<Boolean> buildCPA();
    
    /**
     * 验证 CPA
     */
    boolean validateCPA();
    
    /**
     * 获取 CPA 状态
     */
    CPAStatus getStatus();
    
    /**
     * 获取 CPA 数据
     */
    Map<String, Object> getCPAData();
    
    /**
     * 更新 CPA 数据
     */
    CompletableFuture<Boolean> updateCPAData(Map<String, Object> data);
    
    /**
     * 同步 CPA 到其他节点
     */
    CompletableFuture<Boolean> syncCPA();
    
    /**
     * 处理来自其他节点的 CPA 更新
     */
    void handleCPAUpdate(Map<String, Object> cpaData);
    
    /**
     * 检查 CPA 是否一致
     */
    boolean isCPAconsistent();
    
    /**
     * 重置 CPA
     */
    void resetCPA();
}
