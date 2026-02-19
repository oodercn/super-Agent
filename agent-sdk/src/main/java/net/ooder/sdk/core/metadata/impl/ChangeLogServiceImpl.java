
package net.ooder.sdk.core.metadata.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.metadata.ChangeLogService;
import net.ooder.sdk.api.metadata.ChangeRecord;
import net.ooder.sdk.common.enums.ChangeType;

public class ChangeLogServiceImpl implements ChangeLogService {
    
    private static final Logger log = LoggerFactory.getLogger(ChangeLogServiceImpl.class);
    
    private final List<ChangeRecord> allRecords = new CopyOnWriteArrayList<ChangeRecord>();
    private final Map<String, List<ChangeRecord>> recordsByEntity = new ConcurrentHashMap<String, List<ChangeRecord>>();
    private final Map<String, List<ChangeRecord>> recordsBySceneGroup = new ConcurrentHashMap<String, List<ChangeRecord>>();
    private final Map<String, List<ChangeRecord>> recordsByAgent = new ConcurrentHashMap<String, List<ChangeRecord>>();
    
    @Override
    public CompletableFuture<Void> log(ChangeRecord record) {
        return CompletableFuture.runAsync(() -> {
            if (record == null) {
                return;
            }
            
            if (record.getRecordId() == null) {
                record.setRecordId(java.util.UUID.randomUUID().toString());
            }
            if (record.getTimestamp() == 0) {
                record.setTimestamp(System.currentTimeMillis());
            }
            
            allRecords.add(record);
            
            if (record.getEntityId() != null) {
                recordsByEntity.computeIfAbsent(record.getEntityId(), k -> new CopyOnWriteArrayList<ChangeRecord>()).add(record);
            }
            
            if (record.getSceneGroupId() != null) {
                recordsBySceneGroup.computeIfAbsent(record.getSceneGroupId(), k -> new CopyOnWriteArrayList<ChangeRecord>()).add(record);
            }
            
            if (record.getOperatorId() != null) {
                recordsByAgent.computeIfAbsent(record.getOperatorId(), k -> new CopyOnWriteArrayList<ChangeRecord>()).add(record);
            }
            
            log.debug("Logged change record: {} for entity: {}", record.getChangeType(), record.getEntityId());
        });
    }
    
    @Override
    public CompletableFuture<List<ChangeRecord>> queryByEntity(String entityId) {
        return CompletableFuture.supplyAsync(() -> {
            List<ChangeRecord> records = recordsByEntity.get(entityId);
            return records != null ? new ArrayList<>(records) : new ArrayList<>();
        });
    }
    
    @Override
    public CompletableFuture<List<ChangeRecord>> queryByType(ChangeType type) {
        return CompletableFuture.supplyAsync(() -> {
            return allRecords.stream()
                .filter(r -> r.getChangeType() == type)
                .collect(Collectors.toList());
        });
    }
    
    @Override
    public CompletableFuture<List<ChangeRecord>> queryByTimeRange(long startTime, long endTime) {
        return CompletableFuture.supplyAsync(() -> {
            return allRecords.stream()
                .filter(r -> r.getTimestamp() >= startTime && r.getTimestamp() <= endTime)
                .collect(Collectors.toList());
        });
    }
    
    @Override
    public CompletableFuture<List<ChangeRecord>> queryBySceneGroup(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> {
            List<ChangeRecord> records = recordsBySceneGroup.get(sceneGroupId);
            return records != null ? new ArrayList<>(records) : new ArrayList<>();
        });
    }
    
    @Override
    public CompletableFuture<List<ChangeRecord>> queryByAgent(String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<ChangeRecord> records = recordsByAgent.get(agentId);
            return records != null ? new ArrayList<>(records) : new ArrayList<>();
        });
    }
    
    @Override
    public CompletableFuture<List<ChangeRecord>> getRecent(int limit) {
        return CompletableFuture.supplyAsync(() -> {
            int fromIndex = Math.max(0, allRecords.size() - limit);
            return new ArrayList<>(allRecords.subList(fromIndex, allRecords.size()));
        });
    }
    
    @Override
    public CompletableFuture<Void> clearOldRecords(long beforeTime) {
        return CompletableFuture.runAsync(() -> {
            allRecords.removeIf(r -> r.getTimestamp() < beforeTime);
            
            for (List<ChangeRecord> records : recordsByEntity.values()) {
                records.removeIf(r -> r.getTimestamp() < beforeTime);
            }
            
            for (List<ChangeRecord> records : recordsBySceneGroup.values()) {
                records.removeIf(r -> r.getTimestamp() < beforeTime);
            }
            
            for (List<ChangeRecord> records : recordsByAgent.values()) {
                records.removeIf(r -> r.getTimestamp() < beforeTime);
            }
            
            log.info("Cleared change records before {}", beforeTime);
        });
    }
    
    @Override
    public CompletableFuture<int[]> getStatistics(long startTime, long endTime) {
        return CompletableFuture.supplyAsync(() -> {
            int[] stats = new int[ChangeType.values().length];
            
            for (ChangeRecord record : allRecords) {
                if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                    stats[record.getChangeType().ordinal()]++;
                }
            }
            
            return stats;
        });
    }
}
