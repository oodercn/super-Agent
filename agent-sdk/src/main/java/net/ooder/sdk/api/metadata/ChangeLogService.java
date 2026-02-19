
package net.ooder.sdk.api.metadata;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.common.enums.ChangeType;

public interface ChangeLogService {
    
    CompletableFuture<Void> log(ChangeRecord record);
    
    CompletableFuture<List<ChangeRecord>> queryByEntity(String entityId);
    
    CompletableFuture<List<ChangeRecord>> queryByType(ChangeType type);
    
    CompletableFuture<List<ChangeRecord>> queryByTimeRange(long startTime, long endTime);
    
    CompletableFuture<List<ChangeRecord>> queryBySceneGroup(String sceneGroupId);
    
    CompletableFuture<List<ChangeRecord>> queryByAgent(String agentId);
    
    CompletableFuture<List<ChangeRecord>> getRecent(int limit);
    
    CompletableFuture<Void> clearOldRecords(long beforeTime);
    
    CompletableFuture<int[]> getStatistics(long startTime, long endTime);
}
