package net.ooder.sdk.network.cpa;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CPAManager {
    /**
     * 构建 CPA
     */
    CompletableFuture<Boolean> buildCPA();
    
    /**
     * 验证 CPA
     */
    CompletableFuture<Boolean> validateCPA();
    
    /**
     * 同步 CPA
     */
    CompletableFuture<Boolean> syncCPA();
    
    /**
     * 获取 CPA 状态
     */
    CPAStatus getCPAStatus();
    
    /**
     * 更新 CPA 数据
     */
    CompletableFuture<Boolean> updateCPAData(Map<String, Object> data);
    
    /**
     * 获取 CPA 数据
     */
    CompletableFuture<Map<String, Object>> getCPAData();
    
    /**
     * 重置 CPA
     */
    CompletableFuture<Boolean> resetCPA();
    
    /**
     * 检查 CPA 是否一致
     */
    boolean isCPAconsistent();
}