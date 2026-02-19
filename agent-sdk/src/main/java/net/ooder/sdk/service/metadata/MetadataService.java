
package net.ooder.sdk.service.metadata;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.metadata.ChangeLogService;
import net.ooder.sdk.api.metadata.ChangeRecord;
import net.ooder.sdk.api.metadata.FourDimensionMetadata;
import net.ooder.sdk.api.metadata.MetadataQueryService;
import net.ooder.sdk.common.enums.ChangeType;

public class MetadataService {
    
    private static final Logger log = LoggerFactory.getLogger(MetadataService.class);
    
    private final MetadataQueryService queryService;
    private final ChangeLogService changeLogService;
    
    public MetadataService(MetadataQueryService queryService, ChangeLogService changeLogService) {
        this.queryService = queryService;
        this.changeLogService = changeLogService;
    }
    
    public CompletableFuture<FourDimensionMetadata> queryByAgent(String agentId) {
        log.debug("Querying metadata by agent: {}", agentId);
        return queryService.queryByAgent(agentId);
    }
    
    public CompletableFuture<FourDimensionMetadata> queryByScene(String sceneId) {
        log.debug("Querying metadata by scene: {}", sceneId);
        return queryService.queryByScene(sceneId);
    }
    
    public CompletableFuture<FourDimensionMetadata> queryBySkill(String skillId) {
        log.debug("Querying metadata by skill: {}", skillId);
        return queryService.queryBySkill(skillId);
    }
    
    public CompletableFuture<FourDimensionMetadata> queryBySceneGroup(String sceneGroupId) {
        log.debug("Querying metadata by scene group: {}", sceneGroupId);
        return queryService.queryBySceneGroup(sceneGroupId);
    }
    
    public CompletableFuture<List<FourDimensionMetadata>> queryByTimeRange(long startTime, long endTime) {
        log.debug("Querying metadata by time range: {} - {}", startTime, endTime);
        return queryService.queryByTimeRange(startTime, endTime);
    }
    
    public CompletableFuture<List<FourDimensionMetadata>> queryByChangeType(ChangeType changeType) {
        log.debug("Querying metadata by change type: {}", changeType);
        return queryService.queryByChangeType(changeType);
    }
    
    public CompletableFuture<FourDimensionMetadata> getLatest(String entityId) {
        return queryService.getLatest(entityId);
    }
    
    public CompletableFuture<List<FourDimensionMetadata>> getHistory(String entityId, int limit) {
        return queryService.getHistory(entityId, limit);
    }
    
    public CompletableFuture<Void> recordMetadata(FourDimensionMetadata metadata) {
        log.debug("Recording metadata for entity: {}", metadata.getEntityId());
        return queryService.record(metadata);
    }
    
    public CompletableFuture<Void> logChange(ChangeRecord record) {
        log.debug("Logging change: {} for entity: {}", record.getChangeType(), record.getEntityId());
        return changeLogService.log(record);
    }
    
    public CompletableFuture<List<ChangeRecord>> queryChangesByEntity(String entityId) {
        return changeLogService.queryByEntity(entityId);
    }
    
    public CompletableFuture<List<ChangeRecord>> queryChangesByType(ChangeType type) {
        return changeLogService.queryByType(type);
    }
    
    public CompletableFuture<List<ChangeRecord>> queryChangesByTimeRange(long startTime, long endTime) {
        return changeLogService.queryByTimeRange(startTime, endTime);
    }
    
    public CompletableFuture<List<ChangeRecord>> getRecentChanges(int limit) {
        return changeLogService.getRecent(limit);
    }
    
    public ChangeRecord createChangeRecord(String entityId, String entityType, ChangeType changeType) {
        ChangeRecord record = new ChangeRecord();
        record.setEntityId(entityId);
        record.setEntityType(entityType);
        record.setChangeType(changeType);
        record.setTimestamp(System.currentTimeMillis());
        record.setRecordId(java.util.UUID.randomUUID().toString());
        return record;
    }
    
    public CompletableFuture<Void> recordSkillInstall(String skillId, String agentId) {
        ChangeRecord record = createChangeRecord(skillId, "skill", ChangeType.SKILL_INSTALL);
        record.setOperatorId(agentId);
        record.setDescription("Skill installed: " + skillId);
        return logChange(record);
    }
    
    public CompletableFuture<Void> recordSceneJoin(String sceneId, String agentId, String sceneGroupId) {
        ChangeRecord record = createChangeRecord(sceneId, "scene", ChangeType.SCENE_JOIN);
        record.setOperatorId(agentId);
        record.setSceneGroupId(sceneGroupId);
        record.setDescription("Agent joined scene: " + agentId);
        return logChange(record);
    }
    
    public CompletableFuture<Void> recordFailover(String sceneGroupId, String failedMemberId, String newPrimaryId) {
        ChangeRecord record = createChangeRecord(sceneGroupId, "sceneGroup", ChangeType.FAILOVER);
        record.setSceneGroupId(sceneGroupId);
        record.setDescription("Failover: " + failedMemberId + " -> " + newPrimaryId);
        return logChange(record);
    }
}
