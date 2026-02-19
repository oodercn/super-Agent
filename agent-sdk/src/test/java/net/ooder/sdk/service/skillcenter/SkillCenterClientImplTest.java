package net.ooder.sdk.service.skillcenter;

import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillCenterClient;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SkillCenterClientImplTest {
    
    private SkillCenterClientImpl client;
    
    @Before
    public void setUp() {
        client = new SkillCenterClientImpl("http://localhost:8080");
    }
    
    @After
    public void tearDown() {
        client.disconnect();
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(client);
    }
    
    @Test
    public void testDefaultConstructor() {
        SkillCenterClientImpl defaultClient = new SkillCenterClientImpl();
        assertNotNull(defaultClient);
        defaultClient.disconnect();
    }
    
    @Test
    public void testConnect() throws Exception {
        client.connect().get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testDisconnect() {
        client.disconnect();
        
        assertFalse(client.isConnected());
    }
    
    @Test
    public void testIsConnected() {
        assertFalse(client.isConnected());
    }
    
    @Test
    public void testListSkills() throws Exception {
        List<SkillPackage> skills = client.listSkills().get(10, TimeUnit.SECONDS);
        
        assertNotNull(skills);
    }
    
    @Test
    public void testGetSkill() throws Exception {
        SkillPackage pkg = client.getSkill("skill-001").get(10, TimeUnit.SECONDS);
        
        assertNull(pkg);
    }
    
    @Test
    public void testSearchSkills() throws Exception {
        List<SkillPackage> results = client.searchSkills("test").get(10, TimeUnit.SECONDS);
        
        assertNotNull(results);
    }
    
    @Test
    public void testGetDownloadUrl() throws Exception {
        String url = client.getDownloadUrl("skill-001", "1.0.0").get(10, TimeUnit.SECONDS);
        
        assertNull(url);
    }
    
    @Test
    public void testGetManifest() throws Exception {
        SkillManifest manifest = client.getManifest("skill-001").get(10, TimeUnit.SECONDS);
        
        assertNull(manifest);
    }
    
    @Test
    public void testSetEndpoint() {
        client.setEndpoint("http://new-endpoint:8080");
    }
    
    @Test
    public void testGetEndpoint() {
        client.setEndpoint("http://test-endpoint:8080");
        
        assertEquals("http://test-endpoint:8080", client.getEndpoint());
    }
    
    @Test
    public void testListScenes() throws Exception {
        List<SkillCenterClient.SceneInfo> scenes = client.listScenes().get(10, TimeUnit.SECONDS);
        
        assertNotNull(scenes);
    }
    
    @Test
    public void testGetScene() throws Exception {
        SkillCenterClient.SceneInfo scene = client.getScene("scene-001").get(10, TimeUnit.SECONDS);
        
        assertNull(scene);
    }
}
