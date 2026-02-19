package net.ooder.sdk.core.scene.impl;

import net.ooder.sdk.api.scene.SceneDefinition;
import net.ooder.sdk.api.scene.SceneSnapshot;
import net.ooder.sdk.api.scene.SceneManager.SceneState;
import net.ooder.sdk.api.skill.Capability;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SceneManagerImplTest {
    
    private SceneManagerImpl sceneManager;
    
    @Before
    public void setUp() {
        sceneManager = new SceneManagerImpl();
    }
    
    private SceneDefinition createTestScene(String sceneId) {
        SceneDefinition definition = new SceneDefinition();
        definition.setSceneId(sceneId);
        definition.setName("Test Scene: " + sceneId);
        definition.setVersion("1.0.0");
        return definition;
    }
    
    @Test
    public void testCreate() throws Exception {
        SceneDefinition definition = createTestScene("scene-001");
        
        SceneDefinition result = sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertEquals("scene-001", result.getSceneId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNull() throws Exception {
        sceneManager.create(null).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testDelete() throws Exception {
        SceneDefinition definition = createTestScene("scene-002");
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        sceneManager.delete("scene-002").get(10, TimeUnit.SECONDS);
        
        SceneDefinition retrieved = sceneManager.get("scene-002").get(10, TimeUnit.SECONDS);
        assertNull(retrieved);
    }
    
    @Test
    public void testGet() throws Exception {
        SceneDefinition definition = createTestScene("scene-003");
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        SceneDefinition retrieved = sceneManager.get("scene-003").get(10, TimeUnit.SECONDS);
        
        assertNotNull(retrieved);
        assertEquals("scene-003", retrieved.getSceneId());
    }
    
    @Test
    public void testListAll() throws Exception {
        sceneManager.create(createTestScene("scene-004")).get(10, TimeUnit.SECONDS);
        sceneManager.create(createTestScene("scene-005")).get(10, TimeUnit.SECONDS);
        
        List<SceneDefinition> all = sceneManager.listAll().get(10, TimeUnit.SECONDS);
        
        assertEquals(2, all.size());
    }
    
    @Test
    public void testActivate() throws Exception {
        SceneDefinition definition = createTestScene("scene-006");
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        sceneManager.activate("scene-006").get(10, TimeUnit.SECONDS);
        
        SceneState state = sceneManager.getState("scene-006").get(10, TimeUnit.SECONDS);
        assertTrue(state.isActive());
    }
    
    @Test
    public void testDeactivate() throws Exception {
        SceneDefinition definition = createTestScene("scene-007");
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        sceneManager.activate("scene-007").get(10, TimeUnit.SECONDS);
        
        sceneManager.deactivate("scene-007").get(10, TimeUnit.SECONDS);
        
        SceneState state = sceneManager.getState("scene-007").get(10, TimeUnit.SECONDS);
        assertFalse(state.isActive());
    }
    
    @Test
    public void testGetState() throws Exception {
        SceneDefinition definition = createTestScene("scene-008");
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        SceneState state = sceneManager.getState("scene-008").get(10, TimeUnit.SECONDS);
        
        assertNotNull(state);
        assertEquals("scene-008", state.getSceneId());
    }
    
    @Test
    public void testAddCapability() throws Exception {
        SceneDefinition definition = createTestScene("scene-009");
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        Capability capability = new Capability();
        capability.setCapId("cap-001");
        capability.setName("Test Capability");
        
        sceneManager.addCapability("scene-009", capability).get(10, TimeUnit.SECONDS);
        
        SceneDefinition retrieved = sceneManager.get("scene-009").get(10, TimeUnit.SECONDS);
        assertNotNull(retrieved.getCapabilities());
        assertEquals(1, retrieved.getCapabilities().size());
    }
    
    @Test
    public void testRemoveCapability() throws Exception {
        SceneDefinition definition = createTestScene("scene-010");
        definition.setCapabilities(new ArrayList<Capability>());
        Capability capability = new Capability();
        capability.setCapId("cap-001");
        definition.getCapabilities().add(capability);
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        sceneManager.removeCapability("scene-010", "cap-001").get(10, TimeUnit.SECONDS);
        
        SceneDefinition retrieved = sceneManager.get("scene-010").get(10, TimeUnit.SECONDS);
        assertTrue(retrieved.getCapabilities().isEmpty());
    }
    
    @Test
    public void testCreateSnapshot() throws Exception {
        SceneDefinition definition = createTestScene("scene-011");
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        SceneSnapshot snapshot = sceneManager.createSnapshot("scene-011").get(10, TimeUnit.SECONDS);
        
        assertNotNull(snapshot);
        assertEquals("scene-011", snapshot.getSceneId());
    }
    
    @Test
    public void testGetCapability() throws Exception {
        SceneDefinition definition = createTestScene("scene-012");
        definition.setCapabilities(new ArrayList<Capability>());
        Capability capability = new Capability();
        capability.setCapId("cap-001");
        capability.setName("Test Capability");
        definition.getCapabilities().add(capability);
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        Capability retrieved = sceneManager.getCapability("scene-012", "cap-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(retrieved);
        assertEquals("cap-001", retrieved.getCapId());
    }
    
    @Test
    public void testListCapabilities() throws Exception {
        SceneDefinition definition = createTestScene("scene-013");
        definition.setCapabilities(new ArrayList<Capability>());
        Capability cap1 = new Capability();
        cap1.setCapId("cap-001");
        Capability cap2 = new Capability();
        cap2.setCapId("cap-002");
        definition.getCapabilities().add(cap1);
        definition.getCapabilities().add(cap2);
        sceneManager.create(definition).get(10, TimeUnit.SECONDS);
        
        List<Capability> capabilities = sceneManager.listCapabilities("scene-013").get(10, TimeUnit.SECONDS);
        
        assertEquals(2, capabilities.size());
    }
}
