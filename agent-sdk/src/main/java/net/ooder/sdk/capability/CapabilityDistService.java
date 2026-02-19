package net.ooder.sdk.capability;

import net.ooder.sdk.capability.model.DistRequest;
import net.ooder.sdk.capability.model.DistResult;
import net.ooder.sdk.capability.model.DistStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CapabilityDistService {
    
    CompletableFuture<DistResult> distribute(DistRequest request);
    
    CompletableFuture<DistStatus> getDistStatus(String distId);
    
    CompletableFuture<Void> cancelDist(String distId);
    
    CompletableFuture<List<DistStatus>> listPendingDists();
    
    CompletableFuture<Void> confirmReceipt(String distId, String nodeId);
    
    CompletableFuture<List<String>> getDistTargets(String specId);
}
