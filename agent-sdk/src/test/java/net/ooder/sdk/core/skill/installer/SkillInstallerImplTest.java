package net.ooder.sdk.core.skill.installer;

import net.ooder.sdk.api.skill.*;
import net.ooder.sdk.api.skill.SkillInstaller.InstallMode;
import net.ooder.sdk.api.skill.SkillInstaller.InstallProgress;
import net.ooder.sdk.api.skill.SkillInstaller.ValidateResult;
import net.ooder.sdk.api.skill.SkillInstaller.RollbackResult;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SkillInstallerImplTest {
    
    private SkillInstallerImpl installer;
    
    @Before
    public void setUp() {
        installer = new SkillInstallerImpl();
    }
    
    @After
    public void tearDown() {
        installer.shutdown();
    }
    
    private SkillPackage createTestPackage(String skillId, String version) {
        SkillPackage pkg = new SkillPackage();
        pkg.setSkillId(skillId);
        pkg.setName("Test Skill");
        pkg.setVersion(version);
        pkg.setDescription("Test skill package");
        return pkg;
    }
    
    @Test
    public void testInstall() throws Exception {
        SkillPackage pkg = createTestPackage("skill-001", "1.0.0");
        
        InstallResult result = installer.install(pkg, InstallMode.NORMAL).get(30, TimeUnit.SECONDS);
        
        assertTrue(result.isSuccess());
        assertEquals("skill-001", result.getSkillId());
        assertEquals("1.0.0", result.getVersion());
        assertNotNull(result.getInstallPath());
    }
    
    @Test
    public void testInstallWithForceMode() throws Exception {
        SkillPackage pkg1 = createTestPackage("skill-002", "1.0.0");
        installer.install(pkg1, InstallMode.NORMAL).get(30, TimeUnit.SECONDS);
        
        SkillPackage pkg2 = createTestPackage("skill-002", "2.0.0");
        InstallResult result = installer.install(pkg2, InstallMode.FORCE).get(30, TimeUnit.SECONDS);
        
        assertTrue(result.isSuccess());
        assertEquals("2.0.0", result.getVersion());
    }
    
    @Test
    public void testInstallWithDependencies() throws Exception {
        SkillPackage pkg = createTestPackage("skill-003", "1.0.0");
        pkg.setDependencies(Arrays.asList("dep-001", "dep-002"));
        
        InstallResult result = installer.installWithDependencies(pkg, InstallMode.NORMAL)
            .get(30, TimeUnit.SECONDS);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getInstalledDependencies());
        assertEquals(2, result.getInstalledDependencies().size());
    }
    
    @Test
    public void testUninstall() throws Exception {
        SkillPackage pkg = createTestPackage("skill-004", "1.0.0");
        installer.install(pkg, InstallMode.NORMAL).get(30, TimeUnit.SECONDS);
        
        UninstallResult result = installer.uninstall("skill-004", true).get(10, TimeUnit.SECONDS);
        
        assertTrue(result.isSuccess());
        assertTrue(result.isDataRemoved());
    }
    
    @Test
    public void testUninstallNotInstalled() throws Exception {
        UninstallResult result = installer.uninstall("non-existent", false).get(10, TimeUnit.SECONDS);
        
        assertFalse(result.isSuccess());
        assertNotNull(result.getError());
    }
    
    @Test
    public void testUpdate() throws Exception {
        SkillPackage pkg = createTestPackage("skill-005", "1.0.0");
        installer.install(pkg, InstallMode.NORMAL).get(30, TimeUnit.SECONDS);
        
        UpdateResult result = installer.update("skill-005", "2.0.0").get(10, TimeUnit.SECONDS);
        
        assertTrue(result.isSuccess());
        assertEquals("1.0.0", result.getPreviousVersion());
        assertEquals("2.0.0", result.getNewVersion());
    }
    
    @Test
    public void testUpdateNotInstalled() throws Exception {
        UpdateResult result = installer.update("non-existent", "2.0.0").get(10, TimeUnit.SECONDS);
        
        assertFalse(result.isSuccess());
        assertNotNull(result.getError());
    }
    
    @Test
    public void testRollback() throws Exception {
        SkillPackage pkg = createTestPackage("skill-006", "2.0.0");
        installer.install(pkg, InstallMode.NORMAL).get(30, TimeUnit.SECONDS);
        
        RollbackResult result = installer.rollback("skill-006", "1.0.0").get(10, TimeUnit.SECONDS);
        
        assertTrue(result.isSuccess());
        assertEquals("2.0.0", result.getPreviousVersion());
        assertEquals("1.0.0", result.getCurrentVersion());
    }
    
    @Test
    public void testValidate() throws Exception {
        SkillPackage pkg = createTestPackage("skill-007", "1.0.0");
        
        ValidateResult result = installer.validate(pkg).get(10, TimeUnit.SECONDS);
        
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }
    
    @Test
    public void testValidateInvalidPackage() throws Exception {
        SkillPackage pkg = new SkillPackage();
        
        ValidateResult result = installer.validate(pkg).get(10, TimeUnit.SECONDS);
        
        assertFalse(result.isValid());
        assertFalse(result.getErrors().isEmpty());
    }
    
    @Test
    public void testCheckDependencies() throws Exception {
        SkillPackage pkg = createTestPackage("skill-008", "1.0.0");
        pkg.setDependencies(Arrays.asList("missing-dep-001", "missing-dep-002"));
        
        List<String> missing = installer.checkDependencies(pkg).get(10, TimeUnit.SECONDS);
        
        assertEquals(2, missing.size());
        assertTrue(missing.contains("missing-dep-001"));
        assertTrue(missing.contains("missing-dep-002"));
    }
    
    @Test
    public void testCheckDependenciesAllInstalled() throws Exception {
        SkillPackage dep1 = createTestPackage("installed-dep-001", "1.0.0");
        installer.install(dep1, InstallMode.NORMAL).get(30, TimeUnit.SECONDS);
        
        SkillPackage pkg = createTestPackage("skill-009", "1.0.0");
        pkg.setDependencies(Arrays.asList("installed-dep-001"));
        
        List<String> missing = installer.checkDependencies(pkg).get(10, TimeUnit.SECONDS);
        
        assertTrue(missing.isEmpty());
    }
    
    @Test
    public void testPrepareInstallEnvironment() throws Exception {
        SkillPackage pkg = createTestPackage("skill-010", "1.0.0");
        
        installer.prepareInstallEnvironment(pkg).get(10, TimeUnit.SECONDS);
        
        InstallProgress progress = installer.getProgress("skill-010").get(10, TimeUnit.SECONDS);
        assertNotNull(progress);
        assertEquals("prepared", progress.getPhase());
    }
    
    @Test
    public void testCleanupInstallEnvironment() throws Exception {
        SkillPackage pkg = createTestPackage("skill-011", "1.0.0");
        installer.prepareInstallEnvironment(pkg).get(10, TimeUnit.SECONDS);
        
        installer.cleanupInstallEnvironment("skill-011").get(10, TimeUnit.SECONDS);
        
        InstallProgress progress = installer.getProgress("skill-011").get(10, TimeUnit.SECONDS);
        assertNull(progress);
    }
    
    @Test
    public void testGetProgress() throws Exception {
        SkillPackage pkg = createTestPackage("skill-012", "1.0.0");
        
        installer.install(pkg, InstallMode.NORMAL).get(30, TimeUnit.SECONDS);
        
        InstallProgress progress = installer.getProgress("skill-012").get(10, TimeUnit.SECONDS);
        assertNotNull(progress);
        assertEquals("completed", progress.getPhase());
        assertEquals(100, progress.getPercentage());
    }
}
