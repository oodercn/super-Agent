package net.ooder.sdk.core.metadata.impl;

import net.ooder.sdk.api.metadata.FourDimensionMetadata;
import net.ooder.sdk.api.metadata.ChangeRecord;
import net.ooder.sdk.common.enums.ChangeType;
import net.ooder.sdk.core.metadata.model.IdentityInfo;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MetadataQueryServiceImplTest {
    
    private MetadataQueryServiceImpl service;
    
    @Before
    public void setUp() {
        service = new MetadataQueryServiceImpl();
    }
    
    private FourDimensionMetadata createTestMetadata(String entityId, String entityType) {
        FourDimensionMetadata metadata = FourDimensionMetadata.builder()
            .entityId(entityId)
            .entityType(entityType)
            .agentId(entityId)
            .build();
        metadata.setTimestamp(System.currentTimeMillis());
        return metadata;
    }
    
    private ChangeRecord createTestChangeRecord(String entityId, ChangeType changeType) {
        ChangeRecord record = new ChangeRecord();
        record.setEntityId(entityId);
        record.setEntityType("test");
        record.setChangeType(changeType);
        record.setOperatorId("operator-001");
        record.setTimestamp(System.currentTimeMillis());
        return record;
    }
    
    @Test
    public void testQueryByAgent() throws Exception {
        FourDimensionMetadata result = service.queryByAgent("agent-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertEquals("agent-001", result.getEntityId());
        assertEquals("agent", result.getEntityType());
    }
    
    @Test
    public void testQueryByScene() throws Exception {
        FourDimensionMetadata result = service.queryByScene("scene-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertEquals("scene-001", result.getEntityId());
        assertEquals("scene", result.getEntityType());
    }
    
    @Test
    public void testQueryBySkill() throws Exception {
        FourDimensionMetadata result = service.queryBySkill("skill-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertEquals("skill-001", result.getEntityId());
        assertEquals("skill", result.getEntityType());
    }
    
    @Test
    public void testQueryBySceneGroup() throws Exception {
        FourDimensionMetadata result = service.queryBySceneGroup("sceneGroup-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertEquals("sceneGroup-001", result.getEntityId());
        assertEquals("sceneGroup", result.getEntityType());
    }
    
    @Test
    public void testRecord() throws Exception {
        FourDimensionMetadata metadata = createTestMetadata("entity-001", "test");
        
        service.record(metadata).get(10, TimeUnit.SECONDS);
        
        FourDimensionMetadata result = service.getLatest("entity-001").get(10, TimeUnit.SECONDS);
        assertNotNull(result);
        assertEquals("entity-001", result.getEntityId());
    }
    
    @Test
    public void testRecordNull() throws Exception {
        service.record(null).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testRecordChange() throws Exception {
        ChangeRecord record = createTestChangeRecord("entity-001", ChangeType.SKILL_INSTALL);
        
        service.recordChange(record).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testRecordChangeNull() throws Exception {
        service.recordChange(null).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testQueryByTimeRange() throws Exception {
        FourDimensionMetadata metadata = createTestMetadata("entity-001", "test");
        service.record(metadata).get(10, TimeUnit.SECONDS);
        
        long now = System.currentTimeMillis();
        List<FourDimensionMetadata> results = service.queryByTimeRange(now - 60000, now + 60000).get(10, TimeUnit.SECONDS);
        
        assertNotNull(results);
    }
    
    @Test
    public void testQueryByChangeType() throws Exception {
        ChangeRecord record = createTestChangeRecord("entity-001", ChangeType.SKILL_INSTALL);
        service.recordChange(record).get(10, TimeUnit.SECONDS);
        
        List<FourDimensionMetadata> results = service.queryByChangeType(ChangeType.SKILL_INSTALL).get(10, TimeUnit.SECONDS);
        
        assertNotNull(results);
    }
    
    @Test
    public void testGetLatest() throws Exception {
        FourDimensionMetadata metadata1 = createTestMetadata("entity-001", "test");
        service.record(metadata1).get(10, TimeUnit.SECONDS);
        
        FourDimensionMetadata metadata2 = createTestMetadata("entity-001", "test");
        service.record(metadata2).get(10, TimeUnit.SECONDS);
        
        FourDimensionMetadata latest = service.getLatest("entity-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(latest);
    }
    
    @Test
    public void testGetLatestNotExists() throws Exception {
        FourDimensionMetadata latest = service.getLatest("non-existent").get(10, TimeUnit.SECONDS);
        
        assertNull(latest);
    }
    
    @Test
    public void testGetHistory() throws Exception {
        FourDimensionMetadata metadata1 = createTestMetadata("entity-001", "test");
        service.record(metadata1).get(10, TimeUnit.SECONDS);
        
        FourDimensionMetadata metadata2 = createTestMetadata("entity-001", "test");
        service.record(metadata2).get(10, TimeUnit.SECONDS);
        
        List<FourDimensionMetadata> history = service.getHistory("entity-001", 10).get(10, TimeUnit.SECONDS);
        
        assertNotNull(history);
        assertEquals(2, history.size());
    }
    
    @Test
    public void testGetHistoryNotExists() throws Exception {
        List<FourDimensionMetadata> history = service.getHistory("non-existent", 10).get(10, TimeUnit.SECONDS);
        
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }
    
    @Test
    public void testGetHistoryWithLimit() throws Exception {
        for (int i = 0; i < 10; i++) {
            FourDimensionMetadata metadata = createTestMetadata("entity-002", "test");
            service.record(metadata).get(10, TimeUnit.SECONDS);
        }
        
        List<FourDimensionMetadata> history = service.getHistory("entity-002", 5).get(10, TimeUnit.SECONDS);
        
        assertNotNull(history);
        assertEquals(5, history.size());
    }
}
