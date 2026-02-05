package net.ooder.sdk.persistence;

import net.ooder.sdk.system.config.AgentProperties;
import net.ooder.sdk.network.packet.LinkInfo;
import net.ooder.sdk.skill.SkillStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 业务基础对象持久化服务测试
 */
public class BusinessObjectPersistenceTest {

    private BusinessObjectPersistenceService persistenceService;

    @BeforeEach
    public void setUp() {
        persistenceService = BusinessObjectPersistenceService.getInstance();
    }

    @Test
    public void testSaveAndLoadAgentProperties() {
        // 创建代理属性对象
        AgentProperties properties = new AgentProperties();
        properties.setAgentId("test-agent-123");
        properties.setAgentName("Test Agent");
        properties.setAgentType("general");
        properties.setEndpoint("http://localhost:8080");
        properties.setUdpPort(9876);
        properties.setHeartbeatInterval(30000);

        // 保存代理属性
        boolean saveResult = persistenceService.saveAgentProperties(properties);
        assertTrue(saveResult, "Failed to save agent properties");

        // 加载代理属性
        AgentProperties loadedProperties = persistenceService.loadAgentProperties();
        assertNotNull(loadedProperties, "Failed to load agent properties");
        assertEquals(properties.getAgentId(), loadedProperties.getAgentId());
        assertEquals(properties.getAgentName(), loadedProperties.getAgentName());
        assertEquals(properties.getAgentType(), loadedProperties.getAgentType());
        assertEquals(properties.getEndpoint(), loadedProperties.getEndpoint());
        assertEquals(properties.getUdpPort(), loadedProperties.getUdpPort());
        assertEquals(properties.getHeartbeatInterval(), loadedProperties.getHeartbeatInterval());
    }

    @Test
    public void testSaveAndLoadLinkInfo() {
        // 创建链路信息对象
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setLinkStatus("active");
        linkInfo.setLastConnected(System.currentTimeMillis());
        linkInfo.setDisconnectedAt(null);

        // 保存链路信息
        boolean saveResult = persistenceService.saveLinkInfo(linkInfo);
        assertTrue(saveResult, "Failed to save link info");

        // 注意：由于saveLinkInfo使用时间戳作为键，我们无法直接加载刚才保存的链路信息
        // 这里只测试保存功能，加载功能需要知道具体的键
    }

    @Test
    public void testSaveAndLoadSkillStatus() {
        // 保存技能状态
        boolean saveResult = persistenceService.saveSkillStatus("skill-123", SkillStatus.READY);
        assertTrue(saveResult, "Failed to save skill status");

        // 加载技能状态
        SkillStatus loadedStatus = persistenceService.loadSkillStatus("skill-123");
        assertNotNull(loadedStatus, "Failed to load skill status");
        assertEquals(SkillStatus.READY, loadedStatus);
    }

    @Test
    public void testSaveBatch() {
        // 创建测试对象
        AgentProperties properties = new AgentProperties();
        properties.setAgentId("test-agent-batch");
        properties.setAgentName("Test Agent Batch");

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setLinkStatus("active");
        linkInfo.setLastConnected(System.currentTimeMillis());

        // 构建批量保存映射
        java.util.Map<String, Object> objects = new java.util.HashMap<>();
        objects.put("business/config/agent/properties", properties);
        objects.put("business/route/link/link-batch", linkInfo);

        // 批量保存
        boolean saveResult = persistenceService.saveBatch(objects);
        assertTrue(saveResult, "Failed to save batch objects");

        // 验证保存结果
        AgentProperties loadedProperties = persistenceService.loadAgentProperties();
        assertNotNull(loadedProperties, "Failed to load properties from batch save");

        LinkInfo loadedLinkInfo = persistenceService.loadLinkInfo("link-batch");
        assertNotNull(loadedLinkInfo, "Failed to load link info from batch save");
    }
}
