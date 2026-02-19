
package net.ooder.sdk.api.metadata;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.common.enums.ChangeType;

public interface MetadataQueryService {
    
    CompletableFuture<FourDimensionMetadata> queryByAgent(String agentId);
    
    CompletableFuture<FourDimensionMetadata> queryByScene(String sceneId);
    
    CompletableFuture<FourDimensionMetadata> queryBySkill(String skillId);
    
    CompletableFuture<FourDimensionMetadata> queryBySceneGroup(String sceneGroupId);
    
    CompletableFuture<List<FourDimensionMetadata>> queryByTimeRange(long startTime, long endTime);
    
    CompletableFuture<List<FourDimensionMetadata>> queryByChangeType(ChangeType changeType);
    
    CompletableFuture<FourDimensionMetadata> getLatest(String entityId);
    
    CompletableFuture<List<FourDimensionMetadata>> getHistory(String entityId, int limit);
    
    CompletableFuture<Void> record(FourDimensionMetadata metadata);
    
    CompletableFuture<Void> recordChange(ChangeRecord record);
}
