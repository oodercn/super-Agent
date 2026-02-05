package net.ooder.examples.endagent.service;

import com.alibaba.fastjson.JSON;
import net.ooder.examples.endagent.model.AiBridgeMessage;
import net.ooder.examples.endagent.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * AI Bridge 协议服务测试类
 * 对应协议文档章节：4. 命令系统、5. 安全机制、6. 数据模型、7. 扩展机制、8. 性能优化
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AiBridgeProtocolServiceTest {

    @Mock
    private NetworkService networkService;

    @Mock
    private SkillManager skillManager;

    @Mock
    private SecurityService securityService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private CacheService cacheService;

    private AiBridgeProtocolService aiBridgeProtocolService;

    @BeforeEach
    void setUp() {
        aiBridgeProtocolService = new AiBridgeProtocolService(
                networkService, skillManager, securityService, authorizationService, cacheService
        );

        // 模拟技能管理器返回技能列表
        List<Skill> skills = new ArrayList<>();
        Skill skill = new Skill();
        skill.setSkillId("test-skill-001");
        skill.setName("Test Skill");
        skill.setDescription("Test Description");
        skills.add(skill);
        when(skillManager.getAllSkills()).thenReturn(skills);

        // 模拟安全服务验证证书
        when(securityService.validateCertificateSn("1234567890ABCDEF")).thenReturn(true);

        // 模拟授权服务
        when(authorizationService.isAuthorizedRouteAgent("test-skill-001", "1234567890ABCDEF")).thenReturn(true);
        when(authorizationService.hasSceneDeclarationPermission("test-skill-001", "SMART_HOME")).thenReturn(true);
    }

    /**
     * 测试消息格式 - 对应协议文档章节：3. 消息格式
     */
    @Test
    void testMessageFormat() {
        // 创建测试消息
        Map<String, Object> params = new HashMap<>();
        params.put("space_id", "test-space-001");
        AiBridgeMessage message = aiBridgeProtocolService.createMessage(
                "skill.discover", "source-agent-001", "target-agent-001", params
        );

        // 验证消息格式
        assertNotNull(message);
        assertEquals("0.6.0", message.getVersion());
        assertNotNull(message.getMessageId());
        assertNotNull(message.getId());
        assertEquals("skill.discover", message.getCommand());
        assertEquals("source-agent-001", message.getSource());
        assertEquals("target-agent-001", message.getTarget());
        assertNotNull(message.getParams());
        assertEquals("test-space-001", message.getParams().get("space_id"));
    }

    /**
     * 测试技能发现命令 - 对应协议文档章节：4.1 核心命令
     */
    @Test
    void testSkillDiscoverCommand() throws Exception {
        // 创建技能发现请求
        Map<String, Object> params = new HashMap<>();
        params.put("space_id", "test-space-001");
        params.put("capability", "test-capability");

        AiBridgeMessage request = aiBridgeProtocolService.createMessage(
                "skill.discover", "source-agent-001", "target-agent-001", params
        );

        // 执行命令
        AiBridgeMessage response = aiBridgeProtocolService.handleMessage(request);

        // 验证响应
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getParams());
        assertNotNull(response.getParams().get("skills"));
    }

    /**
     * 测试场景声明命令 - 对应协议文档章节：4.2 场景管理命令
     */
    @Test
    void testSceneDeclareCommand() throws Exception {
        // 创建场景声明请求
        Map<String, Object> params = new HashMap<>();
        params.put("scene_type", "SMART_HOME");
        params.put("skill_id", "test-skill-001");
        params.put("skill_role", "agent_route");
        params.put("is_owner_declaration", false);
        params.put("cap_id", "test-cap-001");
        params.put("cert_sn", "1234567890ABCDEF");

        AiBridgeMessage request = aiBridgeProtocolService.createMessage(
                "scene.declare", "source-agent-001", "target-agent-001", params
        );

        // 执行命令
        AiBridgeMessage response = aiBridgeProtocolService.handleMessage(request);

        // 验证响应
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getParams());
        assertEquals("SMART_HOME", response.getParams().get("scene_type"));
        assertEquals("test-skill-001", response.getParams().get("skill_id"));
        assertEquals("test-cap-001", response.getParams().get("cap_id"));
    }

    /**
     * 测试Cap声明命令 - 对应协议文档章节：4.3 Cap管理命令
     */
    @Test
    void testCapDeclareCommand() throws Exception {
        // 创建Cap声明请求
        Map<String, Object> params = new HashMap<>();
        params.put("cap_id", "test-cap-001");
        params.put("cap_name", "Test Capability");
        params.put("skill_id", "test-skill-001");
        params.put("cap_type", "device_control");
        params.put("max_members", 10);
        params.put("data_storage_type", "json");
        params.put("link_type", "bidirectional");
        params.put("disk_path", "/var/cap_data/test");
        params.put("group_file_path", "/var/cap_data/test/group.json");
        params.put("links_file_path", "/var/cap_data/test/links.json");
        params.put("sync_enabled", true);
        params.put("sync_interval", 60);

        AiBridgeMessage request = aiBridgeProtocolService.createMessage(
                "cap.declare", "source-agent-001", "target-agent-001", params
        );

        // 执行命令
        AiBridgeMessage response = aiBridgeProtocolService.handleMessage(request);

        // 验证响应
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getParams());
        assertEquals("test-cap-001", response.getParams().get("cap_id"));
        assertEquals("Test Capability", response.getParams().get("cap_name"));
        assertEquals("test-skill-001", response.getParams().get("skill_id"));
    }

    /**
     * 测试VFS同步命令 - 对应协议文档章节：4.5 VFS同步命令
     */
    @Test
    void testCapVfsSyncCommand() throws Exception {
        // 创建VFS同步请求
        Map<String, Object> params = new HashMap<>();
        params.put("cap_id", "test-cap-001");
        params.put("vfs_path", "/vfs/cap/test-cap-001/group.json");

        AiBridgeMessage request = aiBridgeProtocolService.createMessage(
                "cap.vfs.sync", "source-agent-001", "target-agent-001", params
        );

        // 执行命令
        AiBridgeMessage response = aiBridgeProtocolService.handleMessage(request);

        // 验证响应
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getParams());
        assertEquals("test-cap-001", response.getParams().get("cap_id"));
        assertEquals("/vfs/cap/test-cap-001/group.json", response.getParams().get("vfs_path"));
        assertEquals("in_progress", response.getParams().get("sync_status"));
    }

    /**
     * 测试批量命令执行 - 对应协议文档章节：7. 扩展机制、8.1 批量操作
     */
    @Test
    void testBatchExecuteCommand() throws Exception {
        // 创建批量执行请求
        List<Map<String, Object>> commands = new ArrayList<>();

        // 技能发现命令
        Map<String, Object> cmd1 = new HashMap<>();
        cmd1.put("command", "skill.discover");
        Map<String, Object> cmd1Params = new HashMap<>();
        cmd1Params.put("space_id", "test-space-001");
        cmd1.put("params", cmd1Params);
        commands.add(cmd1);

        // Cap查询命令
        Map<String, Object> cmd2 = new HashMap<>();
        cmd2.put("command", "cap.query");
        Map<String, Object> cmd2Params = new HashMap<>();
        cmd2Params.put("skill_id", "test-skill-001");
        cmd2.put("params", cmd2Params);
        commands.add(cmd2);

        Map<String, Object> params = new HashMap<>();
        params.put("commands", commands);

        AiBridgeMessage request = aiBridgeProtocolService.createMessage(
                "batch.execute", "source-agent-001", "target-agent-001", params
        );

        // 执行命令
        AiBridgeMessage response = aiBridgeProtocolService.handleMessage(request);

        // 验证响应
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getParams());
        assertNotNull(response.getParams().get("results"));

        // 验证结果数量
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getParams().get("results");
        assertEquals(2, results.size());
        assertEquals("skill.discover", results.get(0).get("command"));
        assertEquals("cap.query", results.get(1).get("command"));
    }

    /**
     * 测试错误处理 - 对应协议文档章节：14. 错误处理
     */
    @Test
    void testErrorHandling() throws Exception {
        // 创建无效的场景声明请求（缺少必要参数）
        Map<String, Object> params = new HashMap<>();
        params.put("scene_type", "SMART_HOME");
        params.put("skill_id", "test-skill-001");
        // 缺少skill_role、is_owner_declaration和cap_id参数

        AiBridgeMessage request = aiBridgeProtocolService.createMessage(
                "scene.declare", "source-agent-001", "target-agent-001", params
        );

        // 执行命令
        AiBridgeMessage response = aiBridgeProtocolService.handleSceneDeclare(request);

        // 验证错误响应
        assertNotNull(response);
        assertEquals("error", response.getStatus());
        assertNotNull(response.getError());
        assertEquals(400, response.getError().getCode());
        assertEquals("Invalid parameters", response.getError().getMessage());
        assertNotNull(response.getError().getDetails());
    }

    /**
     * 测试命令处理 - 对应协议文档章节：4. 命令系统
     */
    @Test
    void testCommandHandling() throws Exception {
        // 创建技能发现请求
        Map<String, Object> params = new HashMap<>();
        params.put("space_id", "test-space-001");
        AiBridgeMessage request = aiBridgeProtocolService.createMessage(
                "skill.discover", "source-agent-001", "target-agent-001", params
        );

        // 转换为JSON并处理
        String jsonRequest = JSON.toJSONString(request);
        AiBridgeMessage response = aiBridgeProtocolService.handleMessage(jsonRequest);

        // 验证响应
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getParams());
    }
}
