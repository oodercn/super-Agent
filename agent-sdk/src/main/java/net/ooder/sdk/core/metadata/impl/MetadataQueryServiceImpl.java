
package net.ooder.sdk.core.metadata.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.metadata.ChangeRecord;
import net.ooder.sdk.api.metadata.FourDimensionMetadata;
import net.ooder.sdk.api.metadata.MetadataQueryService;
import net.ooder.sdk.common.enums.ChangeType;
import net.ooder.sdk.core.metadata.model.IdentityInfo;

public class MetadataQueryServiceImpl implements MetadataQueryService {
    
    private static final Logger log = LoggerFactory.getLogger(MetadataQueryServiceImpl.class);
    
    private final Map<String, List<FourDimensionMetadata>> metadataByEntity = new ConcurrentHashMap<String, List<FourDimensionMetadata>>();
    private final Map<String, List<ChangeRecord>> changeRecords = new ConcurrentHashMap<String, List<ChangeRecord>>();
    
    @Override
    public CompletableFuture<FourDimensionMetadata> queryByAgent(String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<FourDimensionMetadata> metadataList = metadataByEntity.get(agentId);
            if (metadataList != null && !metadataList.isEmpty()) {
                return metadataList.get(metadataList.size() - 1);
            }
            
            FourDimensionMetadata metadata = new FourDimensionMetadata();
            metadata.setEntityId(agentId);
            metadata.setEntityType("agent");
            metadata.setTimestamp(System.currentTimeMillis());
            
            IdentityInfo identity = new IdentityInfo();
            identity.setAgentId(agentId);
            metadata.setIdentity(identity);
            
            return metadata;
        });
    }
    
    @Override
    public CompletableFuture<FourDimensionMetadata> queryByScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            FourDimensionMetadata metadata = new FourDimensionMetadata();
            metadata.setEntityId(sceneId);
            metadata.setEntityType("scene");
            metadata.setTimestamp(System.currentTimeMillis());
            
            IdentityInfo identity = new IdentityInfo();
            identity.setSceneId(sceneId);
            metadata.setIdentity(identity);
            
            return metadata;
        });
    }
    
    @Override
    public CompletableFuture<FourDimensionMetadata> queryBySkill(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            FourDimensionMetadata metadata = new FourDimensionMetadata();
            metadata.setEntityId(skillId);
            metadata.setEntityType("skill");
            metadata.setTimestamp(System.currentTimeMillis());
            
            IdentityInfo identity = new IdentityInfo();
            identity.setSkillId(skillId);
            metadata.setIdentity(identity);
            
            return metadata;
        });
    }
    
    @Override
    public CompletableFuture<FourDimensionMetadata> queryBySceneGroup(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> {
            FourDimensionMetadata metadata = new FourDimensionMetadata();
            metadata.setEntityId(sceneGroupId);
            metadata.setEntityType("sceneGroup");
            metadata.setTimestamp(System.currentTimeMillis());
            
            IdentityInfo identity = new IdentityInfo();
            identity.setSceneGroupId(sceneGroupId);
            metadata.setIdentity(identity);
            
            return metadata;
        });
    }
    
    @Override
    public CompletableFuture<List<FourDimensionMetadata>> queryByTimeRange(long startTime, long endTime) {
        return CompletableFuture.supplyAsync(() -> {
            List<FourDimensionMetadata> result = new ArrayList<>();
            
            for (List<FourDimensionMetadata> metadataList : metadataByEntity.values()) {
                for (FourDimensionMetadata metadata : metadataList) {
                    if (metadata.getTimestamp() >= startTime && metadata.getTimestamp() <= endTime) {
                        result.add(metadata);
                    }
                }
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<List<FourDimensionMetadata>> queryByChangeType(ChangeType changeType) {
        return CompletableFuture.supplyAsync(() -> {
            List<FourDimensionMetadata> result = new ArrayList<>();
            
            for (List<ChangeRecord> records : changeRecords.values()) {
                for (ChangeRecord record : records) {
                    if (record.getChangeType() == changeType) {
                        FourDimensionMetadata metadata = new FourDimensionMetadata();
                        metadata.setEntityId(record.getEntityId());
                        metadata.setEntityType(record.getEntityType());
                        metadata.setTimestamp(record.getTimestamp());
                        result.add(metadata);
                    }
                }
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<FourDimensionMetadata> getLatest(String entityId) {
        return CompletableFuture.supplyAsync(() -> {
            List<FourDimensionMetadata> metadataList = metadataByEntity.get(entityId);
            if (metadataList != null && !metadataList.isEmpty()) {
                return metadataList.get(metadataList.size() - 1);
            }
            return null;
        });
    }
    
    @Override
    public CompletableFuture<List<FourDimensionMetadata>> getHistory(String entityId, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            List<FourDimensionMetadata> metadataList = metadataByEntity.get(entityId);
            if (metadataList == null) {
                return new ArrayList<>();
            }
            
            int fromIndex = Math.max(0, metadataList.size() - limit);
            return new ArrayList<>(metadataList.subList(fromIndex, metadataList.size()));
        });
    }
    
    @Override
    public CompletableFuture<Void> record(FourDimensionMetadata metadata) {
        return CompletableFuture.runAsync(() -> {
            if (metadata == null || metadata.getEntityId() == null) {
                return;
            }
            
            String entityId = metadata.getEntityId();
            metadataByEntity.computeIfAbsent(entityId, k -> new CopyOnWriteArrayList<FourDimensionMetadata>()).add(metadata);
            
            log.debug("Recorded metadata for entity: {}", entityId);
        });
    }
    
    @Override
    public CompletableFuture<Void> recordChange(ChangeRecord record) {
        return CompletableFuture.runAsync(() -> {
            if (record == null || record.getEntityId() == null) {
                return;
            }
            
            String entityId = record.getEntityId();
            changeRecords.computeIfAbsent(entityId, k -> new CopyOnWriteArrayList<ChangeRecord>()).add(record);
            
            log.debug("Recorded change for entity {}: {}", entityId, record.getChangeType());
        });
    }
}
