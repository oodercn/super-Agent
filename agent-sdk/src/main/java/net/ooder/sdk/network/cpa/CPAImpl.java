package net.ooder.sdk.network.cpa;

import net.ooder.sdk.network.gossip.GossipProtocol;
import net.ooder.sdk.network.gossip.GossipMessage;
import net.ooder.sdk.network.gossip.GossipMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

public class CPAImpl implements CPA {
    private static final Logger log = LoggerFactory.getLogger(CPAImpl.class);
    private final GossipProtocol gossipProtocol;
    private final String localNodeId;
    private volatile CPAStatus status;
    private final Map<String, Object> cpaData;
    
    public CPAImpl(GossipProtocol gossipProtocol, String localNodeId) {
        this.gossipProtocol = gossipProtocol;
        this.localNodeId = localNodeId;
        this.status = CPAStatus.INITIALIZED;
        this.cpaData = new ConcurrentHashMap<>();
    }
    
    @Override
    public CompletableFuture<Boolean> buildCPA() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            status = CPAStatus.BUILDING;
            log.info("Building CPA for node: {}", localNodeId);
            
            // 构建 CPA 数据
            buildCPAData();
            
            // 验证 CPA
            if (validateCPA()) {
                status = CPAStatus.BUILT;
                log.info("CPA built successfully for node: {}", localNodeId);
                
                // 同步 CPA 到其他节点
                syncCPA().thenAccept(synced -> {
                    if (synced) {
                        status = CPAStatus.VALIDATED;
                        future.complete(true);
                    } else {
                        status = CPAStatus.INCONSISTENT;
                        future.complete(false);
                    }
                });
            } else {
                status = CPAStatus.ERROR;
                log.error("CPA validation failed for node: {}", localNodeId);
                future.complete(false);
            }
        } catch (Exception e) {
            status = CPAStatus.ERROR;
            log.error("Error building CPA: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    private void buildCPAData() {
        // 构建 CPA 数据
        cpaData.put("nodeId", localNodeId);
        cpaData.put("timestamp", System.currentTimeMillis());
        cpaData.put("version", "1.0.0");
        cpaData.put("protocol", "OoderAgent-CPA-1.0");
        // 添加其他必要的 CPA 数据
    }
    
    @Override
    public boolean validateCPA() {
        status = CPAStatus.VALIDATING;
        log.info("Validating CPA for node: {}", localNodeId);
        
        // 验证 CPA 数据
        try {
            // 检查必要的字段
            if (!cpaData.containsKey("nodeId") || !cpaData.containsKey("timestamp")) {
                log.error("CPA validation failed: missing required fields");
                return false;
            }
            
            // 检查版本号
            if (!cpaData.containsKey("version")) {
                log.error("CPA validation failed: missing version");
                return false;
            }
            
            // 检查协议
            if (!cpaData.containsKey("protocol")) {
                log.error("CPA validation failed: missing protocol");
                return false;
            }
            
            log.info("CPA validated successfully for node: {}", localNodeId);
            return true;
        } catch (Exception e) {
            log.error("Error validating CPA: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public CPAStatus getStatus() {
        return status;
    }
    
    @Override
    public Map<String, Object> getCPAData() {
        return cpaData;
    }
    
    @Override
    public CompletableFuture<Boolean> updateCPAData(Map<String, Object> data) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            cpaData.putAll(data);
            log.info("CPA data updated for node: {}", localNodeId);
            
            // 验证更新后的 CPA
            if (validateCPA()) {
                // 同步更新到其他节点
                syncCPA().thenAccept(synced -> {
                    if (synced) {
                        status = CPAStatus.VALIDATED;
                        future.complete(true);
                    } else {
                        status = CPAStatus.INCONSISTENT;
                        future.complete(false);
                    }
                });
            } else {
                status = CPAStatus.ERROR;
                future.complete(false);
            }
        } catch (Exception e) {
            log.error("Error updating CPA data: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> syncCPA() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Syncing CPA for node: {}", localNodeId);
            
            // 使用 Gossip 协议同步 CPA
            GossipMessage cpaMessage = new GossipMessage(
                    localNodeId,
                    "", // 广播
                    GossipMessageType.CPA_BUILD,
                    cpaData
            );
            
            gossipProtocol.sendGossip(cpaMessage).thenAccept(sent -> {
                if (sent) {
                    log.info("CPA synced successfully for node: {}", localNodeId);
                    future.complete(true);
                } else {
                    log.error("Failed to sync CPA for node: {}", localNodeId);
                    future.complete(false);
                }
            });
        } catch (Exception e) {
            log.error("Error syncing CPA: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public void handleCPAUpdate(Map<String, Object> cpaData) {
        log.info("Handling CPA update from another node");
        
        try {
            // 处理来自其他节点的 CPA 更新
            this.cpaData.putAll(cpaData);
            
            // 验证更新后的 CPA
            if (validateCPA()) {
                status = CPAStatus.VALIDATED;
                log.info("CPA update handled successfully");
            } else {
                status = CPAStatus.INCONSISTENT;
                log.error("CPA update validation failed");
            }
        } catch (Exception e) {
            log.error("Error handling CPA update: {}", e.getMessage());
            status = CPAStatus.ERROR;
        }
    }
    
    @Override
    public boolean isCPAconsistent() {
        return status == CPAStatus.VALIDATED;
    }
    
    @Override
    public void resetCPA() {
        status = CPAStatus.INITIALIZED;
        cpaData.clear();
        log.info("CPA reset for node: {}", localNodeId);
    }
}
