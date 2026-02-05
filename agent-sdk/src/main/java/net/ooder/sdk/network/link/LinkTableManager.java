package net.ooder.sdk.network.link;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface LinkTableManager {
    /**
     * 刷新链路表
     */
    CompletableFuture<Boolean> refreshLinkTable();
    
    /**
     * 强制刷新链路表
     */
    CompletableFuture<Boolean> forceRefreshLinkTable();
    
    /**
     * 禁止链路
     */
    CompletableFuture<Boolean> disableLink(String linkId);
    
    /**
     * 启用链路
     */
    CompletableFuture<Boolean> enableLink(String linkId);
    
    /**
     * 获取链路状态
     */
    CompletableFuture<LinkStatus> getLinkStatus(String linkId);
    
    /**
     * 添加黑白名单
     */
    CompletableFuture<Boolean> addToBlacklist(String nodeId);
    
    /**
     * 从黑白名单中移除
     */
    CompletableFuture<Boolean> removeFromBlacklist(String nodeId);
    
    /**
     * 检查节点是否在黑名单中
     */
    boolean isInBlacklist(String nodeId);
    
    /**
     * 添加白名单
     */
    CompletableFuture<Boolean> addToWhitelist(String nodeId);
    
    /**
     * 从白名单中移除
     */
    CompletableFuture<Boolean> removeFromWhitelist(String nodeId);
    
    /**
     * 检查节点是否在白名单中
     */
    boolean isInWhitelist(String nodeId);
    
    /**
     * 更新链路场景信息
     */
    CompletableFuture<Boolean> updateLinkSceneInfo(String linkId, String sceneId, String groupId);
    
    /**
     * 获取链路表
     */
    LinkTable getLinkTable();
    
    /**
     * 获取黑名单
     */
    Map<String, Long> getBlacklist();
    
    /**
     * 获取白名单
     */
    Map<String, Long> getWhitelist();
}
