package net.ooder.sdk.core.skill.impl;

import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.api.skill.InstallResult;
import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.skill.SkillPackageObserver;
import net.ooder.sdk.api.skill.UninstallResult;
import net.ooder.sdk.api.skill.UpdateResult;
import net.ooder.sdk.common.enums.DiscoveryMethod;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SkillPackageManagerImplTest {
    
    private SkillPackageManagerImpl manager;
    
    @Before
    public void setUp() {
        manager = new SkillPackageManagerImpl();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(manager);
    }
    
    @Test
    public void testDiscoverAllLocal() throws Exception {
        List<SkillPackage> packages = manager.discoverAll(DiscoveryMethod.LOCAL_FS).get(10, TimeUnit.SECONDS);
        
        assertNotNull(packages);
    }
    
    @Test
    public void testDiscoverAllSkillCenter() throws Exception {
        List<SkillPackage> packages = manager.discoverAll(DiscoveryMethod.SKILL_CENTER).get(10, TimeUnit.SECONDS);
        
        assertNotNull(packages);
    }
    
    @Test
    public void testDiscoverAllUdp() throws Exception {
        List<SkillPackage> packages = manager.discoverAll(DiscoveryMethod.UDP_BROADCAST).get(10, TimeUnit.SECONDS);
        
        assertNotNull(packages);
    }
    
    @Test
    public void testDiscoverByScene() throws Exception {
        List<SkillPackage> packages = manager.discoverByScene("scene-001", DiscoveryMethod.LOCAL_FS).get(10, TimeUnit.SECONDS);
        
        assertNotNull(packages);
    }
    
    @Test
    public void testInstall() throws Exception {
        InstallRequest request = new InstallRequest();
        request.setSkillId("skill-001");
        
        InstallResult result = manager.install(request).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertEquals("skill-001", result.getSkillId());
    }
    
    @Test
    public void testUninstall() throws Exception {
        InstallRequest request = new InstallRequest();
        request.setSkillId("skill-001");
        manager.install(request).get(10, TimeUnit.SECONDS);
        
        UninstallResult result = manager.uninstall("skill-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertEquals("skill-001", result.getSkillId());
    }
    
    @Test
    public void testUpdate() throws Exception {
        InstallRequest request = new InstallRequest();
        request.setSkillId("skill-001");
        manager.install(request).get(10, TimeUnit.SECONDS);
        
        UpdateResult result = manager.update("skill-001", "2.0.0").get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertEquals("skill-001", result.getSkillId());
    }
    
    @Test
    public void testListInstalled() throws Exception {
        InstallRequest request = new InstallRequest();
        request.setSkillId("skill-001");
        manager.install(request).get(10, TimeUnit.SECONDS);
        
        List<InstalledSkill> installed = manager.listInstalled().get(10, TimeUnit.SECONDS);
        
        assertNotNull(installed);
    }
    
    @Test
    public void testGetInstalled() throws Exception {
        InstallRequest request = new InstallRequest();
        request.setSkillId("skill-001");
        manager.install(request).get(10, TimeUnit.SECONDS);
        
        InstalledSkill skill = manager.getInstalled("skill-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(skill);
        assertEquals("skill-001", skill.getSkillId());
    }
    
    @Test
    public void testGetInstalledNotExists() throws Exception {
        InstalledSkill skill = manager.getInstalled("non-existent").get(10, TimeUnit.SECONDS);
        
        assertNull(skill);
    }
    
    @Test
    public void testIsInstalled() throws Exception {
        InstallRequest request = new InstallRequest();
        request.setSkillId("skill-001");
        manager.install(request).get(10, TimeUnit.SECONDS);
        
        Boolean installed = manager.isInstalled("skill-001").get(10, TimeUnit.SECONDS);
        
        assertTrue(installed);
    }
    
    @Test
    public void testIsInstalledNotExists() throws Exception {
        Boolean installed = manager.isInstalled("non-existent").get(10, TimeUnit.SECONDS);
        
        assertFalse(installed);
    }
    
    @Test
    public void testRegisterObserver() throws Exception {
        SkillPackageObserver observer = new SkillPackageObserver() {
            @Override
            public void onInstalling(String skillId) {}
            @Override
            public void onInstalled(String skillId, InstallResult result) {}
            @Override
            public void onUninstalling(String skillId) {}
            @Override
            public void onUninstalled(String skillId) {}
            @Override
            public void onUpdateStarted(String skillId, String targetVersion) {}
            @Override
            public void onUpdateCompleted(String skillId, String newVersion) {}
            @Override
            public void onError(String skillId, String error) {}
        };
        
        manager.registerObserver(observer).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testUnregisterObserver() throws Exception {
        SkillPackageObserver observer = new SkillPackageObserver() {
            @Override
            public void onInstalling(String skillId) {}
            @Override
            public void onInstalled(String skillId, InstallResult result) {}
            @Override
            public void onUninstalling(String skillId) {}
            @Override
            public void onUninstalled(String skillId) {}
            @Override
            public void onUpdateStarted(String skillId, String targetVersion) {}
            @Override
            public void onUpdateCompleted(String skillId, String newVersion) {}
            @Override
            public void onError(String skillId, String error) {}
        };
        
        manager.registerObserver(observer).get(10, TimeUnit.SECONDS);
        manager.unregisterObserver(observer).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testGetSkillRootPath() {
        String path = manager.getSkillRootPath();
        
        assertNotNull(path);
    }
    
    @Test
    public void testSetSkillRootPath() {
        manager.setSkillRootPath("/custom/skills/");
        
        assertEquals("/custom/skills/", manager.getSkillRootPath());
    }
}
