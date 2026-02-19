package net.ooder.sdk.core.metadata.impl;

import net.ooder.sdk.api.metadata.ChangeRecord;
import net.ooder.sdk.common.enums.ChangeType;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChangeLogServiceImplTest {
    
    private ChangeLogServiceImpl service;
    
    @Before
    public void setUp() {
        service = new ChangeLogServiceImpl();
    }
    
    private ChangeRecord createTestRecord(String entityId, ChangeType changeType) {
        ChangeRecord record = new ChangeRecord();
        record.setEntityId(entityId);
        record.setEntityType("test");
        record.setChangeType(changeType);
        record.setOperatorId("operator-001");
        record.setSceneGroupId("scene-group-001");
        return record;
    }
    
    @Test
    public void testLog() throws Exception {
        ChangeRecord record = createTestRecord("entity-001", ChangeType.SKILL_INSTALL);
        
        service.log(record).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.queryByEntity("entity-001").get(10, TimeUnit.SECONDS);
        assertEquals(1, results.size());
    }
    
    @Test
    public void testLogNull() throws Exception {
        service.log(null).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.getRecent(10).get(10, TimeUnit.SECONDS);
        assertTrue(results.isEmpty());
    }
    
    @Test
    public void testLogAutoGenerateId() throws Exception {
        ChangeRecord record = new ChangeRecord();
        record.setEntityId("entity-001");
        record.setChangeType(ChangeType.SKILL_INSTALL);
        
        service.log(record).get(10, TimeUnit.SECONDS);
        
        assertNotNull(record.getRecordId());
    }
    
    @Test
    public void testLogAutoGenerateTimestamp() throws Exception {
        ChangeRecord record = new ChangeRecord();
        record.setEntityId("entity-001");
        record.setChangeType(ChangeType.SKILL_INSTALL);
        
        service.log(record).get(10, TimeUnit.SECONDS);
        
        assertTrue(record.getTimestamp() > 0);
    }
    
    @Test
    public void testQueryByEntity() throws Exception {
        service.log(createTestRecord("entity-001", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        service.log(createTestRecord("entity-001", ChangeType.SKILL_UPDATE)).get(10, TimeUnit.SECONDS);
        service.log(createTestRecord("entity-002", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.queryByEntity("entity-001").get(10, TimeUnit.SECONDS);
        
        assertEquals(2, results.size());
    }
    
    @Test
    public void testQueryByEntityEmpty() throws Exception {
        List<ChangeRecord> results = service.queryByEntity("non-existent").get(10, TimeUnit.SECONDS);
        
        assertTrue(results.isEmpty());
    }
    
    @Test
    public void testQueryByType() throws Exception {
        service.log(createTestRecord("entity-001", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        service.log(createTestRecord("entity-002", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        service.log(createTestRecord("entity-003", ChangeType.SKILL_UPDATE)).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.queryByType(ChangeType.SKILL_INSTALL).get(10, TimeUnit.SECONDS);
        
        assertEquals(2, results.size());
    }
    
    @Test
    public void testQueryByTimeRange() throws Exception {
        long now = System.currentTimeMillis();
        
        service.log(createTestRecord("entity-001", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.queryByTimeRange(now - 60000, now + 60000).get(10, TimeUnit.SECONDS);
        
        assertNotNull(results);
        assertEquals(1, results.size());
    }
    
    @Test
    public void testQueryBySceneGroup() throws Exception {
        service.log(createTestRecord("entity-001", ChangeType.SCENE_JOIN)).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.queryBySceneGroup("scene-group-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(results);
        assertEquals(1, results.size());
    }
    
    @Test
    public void testQueryByAgent() throws Exception {
        service.log(createTestRecord("entity-001", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.queryByAgent("operator-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(results);
        assertEquals(1, results.size());
    }
    
    @Test
    public void testGetRecent() throws Exception {
        for (int i = 0; i < 15; i++) {
            service.log(createTestRecord("entity-" + i, ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        }
        
        List<ChangeRecord> results = service.getRecent(10).get(10, TimeUnit.SECONDS);
        
        assertEquals(10, results.size());
    }
    
    @Test
    public void testGetRecentAll() throws Exception {
        service.log(createTestRecord("entity-001", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        service.log(createTestRecord("entity-002", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.getRecent(10).get(10, TimeUnit.SECONDS);
        
        assertEquals(2, results.size());
    }
    
    @Test
    public void testClearOldRecords() throws Exception {
        service.log(createTestRecord("entity-001", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        service.log(createTestRecord("entity-002", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        
        long now = System.currentTimeMillis();
        
        service.clearOldRecords(now + 1000).get(10, TimeUnit.SECONDS);
        
        List<ChangeRecord> results = service.getRecent(10).get(10, TimeUnit.SECONDS);
        assertTrue(results.isEmpty());
    }
    
    @Test
    public void testGetStatistics() throws Exception {
        service.log(createTestRecord("entity-001", ChangeType.SKILL_INSTALL)).get(10, TimeUnit.SECONDS);
        service.log(createTestRecord("entity-002", ChangeType.SKILL_UPDATE)).get(10, TimeUnit.SECONDS);
        
        long now = System.currentTimeMillis();
        int[] stats = service.getStatistics(now - 60000, now + 60000).get(10, TimeUnit.SECONDS);
        
        assertNotNull(stats);
        assertEquals(1, stats[ChangeType.SKILL_INSTALL.ordinal()]);
        assertEquals(1, stats[ChangeType.SKILL_UPDATE.ordinal()]);
    }
}
