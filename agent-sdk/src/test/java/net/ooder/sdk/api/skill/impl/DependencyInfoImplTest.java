package net.ooder.sdk.api.skill.impl;

import net.ooder.sdk.api.skill.DependencyItem;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DependencyInfoImplTest {
    
    private DependencyInfoImpl dependencyInfo;
    
    @Before
    public void setUp() {
        dependencyInfo = new DependencyInfoImpl("skill-001");
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(dependencyInfo);
        assertEquals("skill-001", dependencyInfo.getSkillId());
    }
    
    @Test
    public void testDefaultConstructor() {
        DependencyInfoImpl info = new DependencyInfoImpl();
        assertNull(info.getSkillId());
    }
    
    @Test
    public void testSetSkillId() {
        dependencyInfo.setSkillId("skill-002");
        
        assertEquals("skill-002", dependencyInfo.getSkillId());
    }
    
    @Test
    public void testGetDependencies() {
        List<DependencyItem> deps = dependencyInfo.getDependencies();
        
        assertNotNull(deps);
        assertTrue(deps.isEmpty());
    }
    
    @Test
    public void testSetDependencies() {
        List<DependencyItem> deps = new ArrayList<DependencyItem>();
        DependencyItem item = new DependencyItem();
        item.setName("dep-skill-001");
        deps.add(item);
        
        dependencyInfo.setDependencies(deps);
        
        assertEquals(1, dependencyInfo.getDependencies().size());
    }
    
    @Test
    public void testSetDependenciesNull() {
        dependencyInfo.setDependencies(null);
        
        assertNotNull(dependencyInfo.getDependencies());
        assertTrue(dependencyInfo.getDependencies().isEmpty());
    }
    
    @Test
    public void testAddDependency() {
        DependencyItem item = new DependencyItem();
        item.setName("dep-skill-001");
        
        dependencyInfo.addDependency(item);
        
        assertEquals(1, dependencyInfo.getDependencies().size());
    }
    
    @Test
    public void testAddDependencyNull() {
        dependencyInfo.addDependency(null);
        
        assertTrue(dependencyInfo.getDependencies().isEmpty());
    }
    
    @Test
    public void testIsSatisfiedEmpty() {
        assertTrue(dependencyInfo.isSatisfied());
    }
    
    @Test
    public void testGetMissing() {
        List<DependencyItem> missing = dependencyInfo.getMissing();
        
        assertNotNull(missing);
    }
    
    @Test
    public void testGetOutdated() {
        List<DependencyItem> outdated = dependencyInfo.getOutdated();
        
        assertNotNull(outdated);
    }
}
