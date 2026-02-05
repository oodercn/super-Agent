package net.ooder.sdk.network.link;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class LinkTableManagerImpl implements LinkTableManager {
    private static final Logger log = LoggerFactory.getLogger(LinkTableManagerImpl.class);
    private final LinkTable linkTable;
    private final Map<String, Long> blacklist;
    private final Map<String, Long> whitelist;
    
    public LinkTableManagerImpl() {
        this.linkTable = new LinkTable();
        this.blacklist = new ConcurrentHashMap<>();
        this.whitelist = new ConcurrentHashMap<>();
    }
    
    @Override
    public CompletableFuture<Boolean> refreshLinkTable() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Refreshing link table");
            
            // 刷新链路表逻辑
            // 1. 检查链路状态
            // 2. 更新链路质量
            // 3. 清理无效链路
            cleanInvalidLinks();
            
            log.info("Link table refreshed successfully");
            future.complete(true);
        } catch (Exception e) {
            log.error("Error refreshing link table: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    private void cleanInvalidLinks() {
        // 清理无效链路
        Map<String, LinkInfo> links = linkTable.getAllLinks();
        links.entrySet().removeIf(entry -> {
            LinkInfo linkInfo = entry.getValue();
            return linkInfo.getStatus() == LinkStatus.FAILED || linkInfo.getQuality() < 0.1;
        });
    }
    
    @Override
    public CompletableFuture<Boolean> forceRefreshLinkTable() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Force refreshing link table");
            
            // 强制刷新链路表逻辑
            // 1. 清理所有链路
            linkTable.getAllLinks().clear();
            
            // 2. 重新发现链路
            rediscoverLinks();
            
            log.info("Link table force refreshed successfully");
            future.complete(true);
        } catch (Exception e) {
            log.error("Error force refreshing link table: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    private void rediscoverLinks() {
        // 重新发现链路
        // 简化实现：这里不做实际的链路发现，假设链路已经存在
        log.debug("Rediscovering links");
    }
    
    @Override
    public CompletableFuture<Boolean> disableLink(String linkId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Disabling link: {}", linkId);
            
            // 禁用链路
            LinkInfo linkInfo = linkTable.getLink(linkId);
            if (linkInfo != null) {
                linkInfo.setStatus(LinkStatus.DISABLED);
                linkTable.addOrUpdateLink(linkInfo);
                log.info("Link disabled successfully: {}", linkId);
                future.complete(true);
            } else {
                log.error("Link not found: {}", linkId);
                future.complete(false);
            }
        } catch (Exception e) {
            log.error("Error disabling link: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> enableLink(String linkId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Enabling link: {}", linkId);
            
            // 启用链路
            LinkInfo linkInfo = linkTable.getLink(linkId);
            if (linkInfo != null) {
                linkInfo.setStatus(LinkStatus.ACTIVE);
                linkTable.addOrUpdateLink(linkInfo);
                log.info("Link enabled successfully: {}", linkId);
                future.complete(true);
            } else {
                log.error("Link not found: {}", linkId);
                future.complete(false);
            }
        } catch (Exception e) {
            log.error("Error enabling link: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<LinkStatus> getLinkStatus(String linkId) {
        CompletableFuture<LinkStatus> future = new CompletableFuture<>();
        
        try {
            log.info("Getting link status: {}", linkId);
            
            // 获取链路状态
            LinkInfo linkInfo = linkTable.getLink(linkId);
            if (linkInfo != null) {
                future.complete(linkInfo.getStatus());
            } else {
                log.error("Link not found: {}", linkId);
                future.complete(LinkStatus.UNKNOWN);
            }
        } catch (Exception e) {
            log.error("Error getting link status: {}", e.getMessage());
            future.complete(LinkStatus.UNKNOWN);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> addToBlacklist(String nodeId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Adding node to blacklist: {}", nodeId);
            
            // 添加到黑名单
            blacklist.put(nodeId, System.currentTimeMillis());
            
            // 禁用与该节点相关的所有链路
            disableLinksForNode(nodeId);
            
            log.info("Node added to blacklist successfully: {}", nodeId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error adding node to blacklist: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    private void disableLinksForNode(String nodeId) {
        // 禁用与该节点相关的所有链路
        Map<String, LinkInfo> links = linkTable.getAllLinks();
        links.forEach((linkId, linkInfo) -> {
            if (linkInfo.getSourceNodeId().equals(nodeId) || linkInfo.getTargetNodeId().equals(nodeId)) {
                linkInfo.setStatus(LinkStatus.DISABLED);
                linkTable.addOrUpdateLink(linkInfo);
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> removeFromBlacklist(String nodeId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Removing node from blacklist: {}", nodeId);
            
            // 从黑名单中移除
            if (blacklist.remove(nodeId) != null) {
                log.info("Node removed from blacklist successfully: {}", nodeId);
                future.complete(true);
            } else {
                log.warn("Node not found in blacklist: {}", nodeId);
                future.complete(false);
            }
        } catch (Exception e) {
            log.error("Error removing node from blacklist: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public boolean isInBlacklist(String nodeId) {
        return blacklist.containsKey(nodeId);
    }
    
    @Override
    public CompletableFuture<Boolean> addToWhitelist(String nodeId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Adding node to whitelist: {}", nodeId);
            
            // 添加到白名单
            whitelist.put(nodeId, System.currentTimeMillis());
            
            // 启用与该节点相关的所有链路
            enableLinksForNode(nodeId);
            
            log.info("Node added to whitelist successfully: {}", nodeId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error adding node to whitelist: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    private void enableLinksForNode(String nodeId) {
        // 启用与该节点相关的所有链路
        Map<String, LinkInfo> links = linkTable.getAllLinks();
        links.forEach((linkId, linkInfo) -> {
            if (linkInfo.getSourceNodeId().equals(nodeId) || linkInfo.getTargetNodeId().equals(nodeId)) {
                linkInfo.setStatus(LinkStatus.ACTIVE);
                linkTable.addOrUpdateLink(linkInfo);
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> removeFromWhitelist(String nodeId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Removing node from whitelist: {}", nodeId);
            
            // 从白名单中移除
            if (whitelist.remove(nodeId) != null) {
                log.info("Node removed from whitelist successfully: {}", nodeId);
                future.complete(true);
            } else {
                log.warn("Node not found in whitelist: {}", nodeId);
                future.complete(false);
            }
        } catch (Exception e) {
            log.error("Error removing node from whitelist: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public boolean isInWhitelist(String nodeId) {
        return whitelist.containsKey(nodeId);
    }
    
    @Override
    public CompletableFuture<Boolean> updateLinkSceneInfo(String linkId, String sceneId, String groupId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Updating link scene info: {} - {} - {}", linkId, sceneId, groupId);
            
            // 更新链路场景信息
            LinkInfo linkInfo = linkTable.getLink(linkId);
            if (linkInfo != null) {
                linkInfo.setSceneId(sceneId);
                linkInfo.setGroupId(groupId);
                linkTable.addOrUpdateLink(linkInfo);
                log.info("Link scene info updated successfully: {}", linkId);
                future.complete(true);
            } else {
                log.error("Link not found: {}", linkId);
                future.complete(false);
            }
        } catch (Exception e) {
            log.error("Error updating link scene info: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public LinkTable getLinkTable() {
        return linkTable;
    }
    
    @Override
    public Map<String, Long> getBlacklist() {
        return blacklist;
    }
    
    @Override
    public Map<String, Long> getWhitelist() {
        return whitelist;
    }
}
