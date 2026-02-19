package net.ooder.sdk.core.agent.impl;

import net.ooder.sdk.api.agent.Agent;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.scene.SceneGroupKey;
import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.common.enums.MemberRole;
import net.ooder.sdk.core.agent.model.AgentConfig;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EndAgentImplTest {
    
    private EndAgentImpl endAgent;
    private AgentConfig config;
    
    @Before
    public void setUp() {
        config = new AgentConfig();
        config.setAgentId("test-end-001");
        config.setAgentName("Test End Agent");
        config.setEndpoint("http://localhost:8080");
        
        endAgent = new EndAgentImpl(config);
    }
    
    @After
    public void tearDown() {
        if (endAgent != null) {
            endAgent.stop();
        }
    }
    
    @Test
    public void testInitialization() {
        assertEquals("test-end-001", endAgent.getAgentId());
        assertEquals("Test End Agent", endAgent.getAgentName());
        assertEquals(AgentType.END, endAgent.getAgentType());
        assertEquals("http://localhost:8080", endAgent.getEndpoint());
    }
    
    @Test
    public void testStart() {
        endAgent.start();
        assertEquals(Agent.AgentState.RUNNING, endAgent.getState());
    }
    
    @Test
    public void testStop() {
        endAgent.start();
        endAgent.stop();
        assertEquals(Agent.AgentState.STOPPED, endAgent.getState());
    }
    
    @Test
    public void testInstallSkill() throws Exception {
        endAgent.start();
        
        SkillPackage skillPackage = new SkillPackage();
        skillPackage.setSkillId("skill-001");
        skillPackage.setName("Test Skill");
        skillPackage.setVersion("1.0.0");
        
        endAgent.installSkill(skillPackage).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testUninstallSkill() throws Exception {
        endAgent.start();
        
        SkillPackage skillPackage = new SkillPackage();
        skillPackage.setSkillId("skill-001");
        endAgent.installSkill(skillPackage).get(10, TimeUnit.SECONDS);
        
        endAgent.uninstallSkill("skill-001").get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testInvokeSkill() throws Exception {
        endAgent.start();
        
        SkillPackage skillPackage = new SkillPackage();
        skillPackage.setSkillId("skill-001");
        endAgent.installSkill(skillPackage).get(10, TimeUnit.SECONDS);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", "value");
        
        Map<String, Object> result = endAgent.invokeSkill("skill-001", params).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
    }
    
    @Test
    public void testConfigureSkill() throws Exception {
        endAgent.start();
        
        SkillPackage skillPackage = new SkillPackage();
        skillPackage.setSkillId("skill-001");
        endAgent.installSkill(skillPackage).get(10, TimeUnit.SECONDS);
        
        Map<String, Object> configMap = new HashMap<String, Object>();
        configMap.put("timeout", 5000);
        
        endAgent.configureSkill("skill-001", configMap).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testStartSkill() throws Exception {
        endAgent.start();
        
        SkillPackage skillPackage = new SkillPackage();
        skillPackage.setSkillId("skill-001");
        endAgent.installSkill(skillPackage).get(10, TimeUnit.SECONDS);
        
        endAgent.startSkill("skill-001").get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testStopSkill() throws Exception {
        endAgent.start();
        
        SkillPackage skillPackage = new SkillPackage();
        skillPackage.setSkillId("skill-001");
        endAgent.installSkill(skillPackage).get(10, TimeUnit.SECONDS);
        endAgent.startSkill("skill-001").get(10, TimeUnit.SECONDS);
        
        endAgent.stopSkill("skill-001").get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testJoinSceneGroup() throws Exception {
        endAgent.start();
        
        SceneGroupKey key = new SceneGroupKey();
        key.setSceneGroupId("scene-group-001");
        
        endAgent.joinSceneGroup("scene-group-001", key).get(10, TimeUnit.SECONDS);
        
        String role = endAgent.getCurrentRole("scene-group-001").get(10, TimeUnit.SECONDS);
        assertEquals(MemberRole.BACKUP.getCode(), role);
    }
    
    @Test
    public void testLeaveSceneGroup() throws Exception {
        endAgent.start();
        
        SceneGroupKey key = new SceneGroupKey();
        key.setSceneGroupId("scene-group-001");
        
        endAgent.joinSceneGroup("scene-group-001", key).get(10, TimeUnit.SECONDS);
        endAgent.leaveSceneGroup("scene-group-001").get(10, TimeUnit.SECONDS);
        
        String role = endAgent.getCurrentRole("scene-group-001").get(10, TimeUnit.SECONDS);
        assertNull(role);
    }
    
    @Test
    public void testUpdateConfig() throws Exception {
        endAgent.start();
        
        Map<String, Object> newConfig = new HashMap<String, Object>();
        newConfig.put("timeout", 10000);
        
        endAgent.updateConfig(newConfig).get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testReset() throws Exception {
        endAgent.start();
        
        SkillPackage skillPackage = new SkillPackage();
        skillPackage.setSkillId("skill-001");
        endAgent.installSkill(skillPackage).get(10, TimeUnit.SECONDS);
        
        endAgent.reset().get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testGetRouteAgentId() {
        assertNull(endAgent.getRouteAgentId());
    }
}
