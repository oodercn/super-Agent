package net.ooder.sdk.network.cpa;

import net.ooder.sdk.network.gossip.GossipProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CPAManagerImpl implements CPAManager {
    private static final Logger log = LoggerFactory.getLogger(CPAManagerImpl.class);
    private final CPA cpa;
    
    public CPAManagerImpl(GossipProtocol gossipProtocol, String localNodeId) {
        this.cpa = new CPAImpl(gossipProtocol, localNodeId);
    }
    
    @Override
    public CompletableFuture<Boolean> buildCPA() {
        return cpa.buildCPA();
    }
    
    @Override
    public CompletableFuture<Boolean> validateCPA() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Validating CPA");
            boolean isValid = cpa.validateCPA();
            log.info("CPA validation result: {}", isValid);
            future.complete(isValid);
        } catch (Exception e) {
            log.error("Error validating CPA: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> syncCPA() {
        return cpa.syncCPA();
    }
    
    @Override
    public CPAStatus getCPAStatus() {
        return cpa.getStatus();
    }
    
    @Override
    public CompletableFuture<Boolean> updateCPAData(Map<String, Object> data) {
        return cpa.updateCPAData(data);
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getCPAData() {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        
        try {
            log.info("Getting CPA data");
            Map<String, Object> cpaData = cpa.getCPAData();
            future.complete(cpaData);
        } catch (Exception e) {
            log.error("Error getting CPA data: {}", e.getMessage());
            future.completeExceptionally(e);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> resetCPA() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Resetting CPA");
            cpa.resetCPA();
            log.info("CPA reset successfully");
            future.complete(true);
        } catch (Exception e) {
            log.error("Error resetting CPA: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public boolean isCPAconsistent() {
        return cpa.isCPAconsistent();
    }
}
