package net.ooder.skill.config.cli;

import net.ooder.skill.config.dto.CliConfigDTO;
import net.ooder.skill.config.dto.CliMockRequestDTO;
import net.ooder.skill.config.service.CliConfigService;
import net.ooder.skill.config.service.CliMockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class CliFunctionTools {

    private static final Logger log = LoggerFactory.getLogger(CliFunctionTools.class);

    @Autowired(required = false)
    private CliConfigService cliConfigService;

    @Autowired(required = false)
    private CliMockService cliMockService;

    public List<Map<String, Object>> getCliFunctionSchemas() {
        List<Map<String, Object>> schemas = new ArrayList<>();
        
        schemas.add(createFunctionSchema(
            "cli_send_message",
            "通过CLI工具发送消息到指定用户或群组",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID，如 dingding, wecom, feishu", true, Arrays.asList("dingding", "wecom", "feishu")),
                "receiver", createParameter("string", "消息接收者ID，可以是用户ID或群组ID", true),
                "content", createParameter("string", "消息内容", true),
                "messageType", createParameter("string", "消息类型", false, Arrays.asList("text", "markdown", "actionCard"))
            ),
            Arrays.asList("cliId", "receiver", "content")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_get_user_info",
            "获取CLI平台上的用户信息",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID", true, Arrays.asList("dingding", "wecom", "feishu")),
                "userId", createParameter("string", "用户ID", true)
            ),
            Arrays.asList("cliId", "userId")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_get_department_list",
            "获取CLI平台上的部门列表",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID", true, Arrays.asList("dingding", "wecom", "feishu")),
                "parentDeptId", createParameter("string", "父部门ID，不传则获取根部门", false)
            ),
            Arrays.asList("cliId")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_sync_organization",
            "同步CLI平台的组织架构到本地系统",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID", true, Arrays.asList("dingding", "wecom", "feishu")),
                "syncType", createParameter("string", "同步类型", false, Arrays.asList("full", "incremental")),
                "includeUsers", createParameter("boolean", "是否包含用户数据", false),
                "includeDepartments", createParameter("boolean", "是否包含部门数据", false)
            ),
            Arrays.asList("cliId")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_test_connection",
            "测试CLI工具的连接状态",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID", true, Arrays.asList("dingding", "wecom", "feishu"))
            ),
            Arrays.asList("cliId")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_get_config",
            "获取CLI工具的配置信息",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID", true, Arrays.asList("dingding", "wecom", "feishu"))
            ),
            Arrays.asList("cliId")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_update_config",
            "更新CLI工具的配置",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID", true, Arrays.asList("dingding", "wecom", "feishu")),
                "config", createParameter("object", "配置参数对象", true)
            ),
            Arrays.asList("cliId", "config")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_generate_qrcode",
            "生成CLI绑定二维码",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID", true, Arrays.asList("dingding", "wecom", "feishu")),
                "userId", createParameter("string", "要绑定的用户ID", false)
            ),
            Arrays.asList("cliId")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_check_bind_status",
            "检查CLI绑定状态",
            Map.of(
                "cliId", createParameter("string", "CLI工具ID", true, Arrays.asList("dingding", "wecom", "feishu")),
                "userId", createParameter("string", "用户ID", false)
            ),
            Arrays.asList("cliId")
        ));
        
        schemas.add(createFunctionSchema(
            "cli_list_available",
            "列出所有可用的CLI工具",
            Map.of(
                "enabledOnly", createParameter("boolean", "是否只返回已启用的CLI", false)
            ),
            Collections.emptyList()
        ));
        
        return schemas;
    }

    private Map<String, Object> createFunctionSchema(String name, String description, 
            Map<String, Object> properties, List<String> required) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("name", name);
        schema.put("description", description);
        
        Map<String, Object> paramsSchema = new LinkedHashMap<>();
        paramsSchema.put("type", "object");
        paramsSchema.put("properties", properties);
        paramsSchema.put("required", required);
        
        schema.put("parameters", paramsSchema);
        return schema;
    }

    private Map<String, Object> createParameter(String type, String description, boolean required) {
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("type", type);
        param.put("description", description);
        return param;
    }

    private Map<String, Object> createParameter(String type, String description, boolean required, List<String> enumValues) {
        Map<String, Object> param = createParameter(type, description, required);
        if (enumValues != null && !enumValues.isEmpty()) {
            param.put("enum", enumValues);
        }
        return param;
    }

    public Map<String, Function<Map<String, Object>, Object>> getFunctionHandlers() {
        Map<String, Function<Map<String, Object>, Object>> handlers = new HashMap<>();
        
        handlers.put("cli_send_message", this::handleSendMessage);
        handlers.put("cli_get_user_info", this::handleGetUserInfo);
        handlers.put("cli_get_department_list", this::handleGetDepartmentList);
        handlers.put("cli_sync_organization", this::handleSyncOrganization);
        handlers.put("cli_test_connection", this::handleTestConnection);
        handlers.put("cli_get_config", this::handleGetConfig);
        handlers.put("cli_update_config", this::handleUpdateConfig);
        handlers.put("cli_generate_qrcode", this::handleGenerateQrCode);
        handlers.put("cli_check_bind_status", this::handleCheckBindStatus);
        handlers.put("cli_list_available", this::handleListAvailable);
        
        return handlers;
    }

    private Object handleSendMessage(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        String receiver = (String) args.get("receiver");
        String content = (String) args.get("content");
        String messageType = (String) args.getOrDefault("messageType", "text");
        
        log.info("[CLI Function] Sending message via {}: receiver={}, type={}", cliId, receiver, messageType);
        
        if (cliMockService != null) {
            CliMockRequestDTO request = new CliMockRequestDTO();
            request.setAction("text".equals(messageType) ? "sendMessage" : "sendMarkdown");
            request.setParams(Map.of("receiver", receiver, "content", content, "type", messageType));
            return cliMockService.mockCall(cliId, request);
        }
        
        return Map.of(
            "success", true,
            "cliId", cliId,
            "action", "sendMessage",
            "receiver", receiver,
            "messageId", "msg_" + UUID.randomUUID().toString().substring(0, 8),
            "timestamp", System.currentTimeMillis()
        );
    }

    private Object handleGetUserInfo(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        String userId = (String) args.get("userId");
        
        log.info("[CLI Function] Getting user info from {}: userId={}", cliId, userId);
        
        if (cliMockService != null) {
            CliMockRequestDTO request = new CliMockRequestDTO();
            request.setAction("getUserInfo");
            request.setParams(Map.of("userId", userId));
            return cliMockService.mockCall(cliId, request);
        }
        
        return Map.of(
            "success", true,
            "cliId", cliId,
            "userId", userId,
            "name", "测试用户",
            "department", "技术部",
            "mobile", "138****8888"
        );
    }

    private Object handleGetDepartmentList(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        String parentDeptId = (String) args.get("parentDeptId");
        
        log.info("[CLI Function] Getting department list from {}: parentDeptId={}", cliId, parentDeptId);
        
        if (cliMockService != null) {
            CliMockRequestDTO request = new CliMockRequestDTO();
            request.setAction("getDeptList");
            request.setParams(parentDeptId != null ? Map.of("parentDeptId", parentDeptId) : Collections.emptyMap());
            return cliMockService.mockCall(cliId, request);
        }
        
        return Map.of(
            "success", true,
            "cliId", cliId,
            "departments", Arrays.asList(
                Map.of("deptId", "001", "name", "技术部", "memberCount", 50),
                Map.of("deptId", "002", "name", "产品部", "memberCount", 30)
            )
        );
    }

    private Object handleSyncOrganization(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        String syncType = (String) args.getOrDefault("syncType", "incremental");
        boolean includeUsers = args.get("includeUsers") == null || (Boolean) args.get("includeUsers");
        boolean includeDepartments = args.get("includeDepartments") == null || (Boolean) args.get("includeDepartments");
        
        log.info("[CLI Function] Syncing organization from {}: type={}, users={}, depts={}", 
            cliId, syncType, includeUsers, includeDepartments);
        
        return Map.of(
            "success", true,
            "cliId", cliId,
            "syncType", syncType,
            "syncedUsers", includeUsers ? 100 : 0,
            "syncedDepartments", includeDepartments ? 10 : 0,
            "syncTime", System.currentTimeMillis()
        );
    }

    private Object handleTestConnection(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        
        log.info("[CLI Function] Testing connection for {}", cliId);
        
        if (cliMockService != null) {
            return cliMockService.testConnection(cliId);
        }
        
        return Map.of(
            "success", true,
            "cliId", cliId,
            "message", "连接测试成功",
            "latency", new Random().nextInt(100) + 50
        );
    }

    private Object handleGetConfig(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        
        log.info("[CLI Function] Getting config for {}", cliId);
        
        if (cliConfigService != null) {
            CliConfigDTO config = cliConfigService.getCliConfig(cliId);
            if (config != null) {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("success", true);
                result.put("cliId", config.getCliId());
                result.put("name", config.getName());
                result.put("enabled", config.isEnabled());
                result.put("status", config.getStatus());
                return result;
            }
        }
        
        return Map.of("success", false, "error", "CLI not found: " + cliId);
    }

    @SuppressWarnings("unchecked")
    private Object handleUpdateConfig(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        Map<String, Object> config = (Map<String, Object>) args.get("config");
        
        log.info("[CLI Function] Updating config for {}", cliId);
        
        if (cliConfigService != null && config != null) {
            CliConfigDTO dto = new CliConfigDTO();
            dto.setCliId(cliId);
            dto.setEnabled(Boolean.TRUE.equals(config.get("enabled")));
            dto.setSettings((Map<String, Object>) config.get("settings"));
            cliConfigService.updateCliConfig(cliId, dto);
            return Map.of("success", true, "cliId", cliId);
        }
        
        return Map.of("success", true, "cliId", cliId, "message", "配置更新成功（模拟）");
    }

    private Object handleGenerateQrCode(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        String userId = (String) args.get("userId");
        
        log.info("[CLI Function] Generating QR code for {} - user: {}", cliId, userId);
        
        return Map.of(
            "success", true,
            "cliId", cliId,
            "qrcodeUrl", "/api/v1/config/cli/" + cliId + "/bind?qrcode=true",
            "expireTime", System.currentTimeMillis() + 300000,
            "scanTip", "请使用" + getCliName(cliId) + "扫描二维码进行绑定"
        );
    }

    private Object handleCheckBindStatus(Map<String, Object> args) {
        String cliId = (String) args.get("cliId");
        String userId = (String) args.get("userId");
        
        log.info("[CLI Function] Checking bind status for {} - user: {}", cliId, userId);
        
        return Map.of(
            "success", true,
            "cliId", cliId,
            "userId", userId,
            "bound", false,
            "message", "未绑定"
        );
    }

    private Object handleListAvailable(Map<String, Object> args) {
        boolean enabledOnly = args.get("enabledOnly") != null && (Boolean) args.get("enabledOnly");
        
        log.info("[CLI Function] Listing available CLIs, enabledOnly={}", enabledOnly);
        
        List<Map<String, Object>> clis = new ArrayList<>();
        
        if (cliConfigService != null) {
            List<CliConfigDTO> configs = cliConfigService.getAllCliConfigs();
            for (CliConfigDTO config : configs) {
                if (!enabledOnly || config.isEnabled()) {
                    Map<String, Object> cli = new LinkedHashMap<>();
                    cli.put("cliId", config.getCliId());
                    cli.put("name", config.getName());
                    cli.put("enabled", config.isEnabled());
                    cli.put("status", config.getStatus());
                    clis.add(cli);
                }
            }
        } else {
            clis.add(Map.of("cliId", "dingding", "name", "钉钉", "enabled", false, "status", "inactive"));
            clis.add(Map.of("cliId", "wecom", "name", "企业微信", "enabled", false, "status", "inactive"));
            clis.add(Map.of("cliId", "feishu", "name", "飞书", "enabled", false, "status", "inactive"));
        }
        
        return Map.of("success", true, "clis", clis, "count", clis.size());
    }

    private String getCliName(String cliId) {
        switch (cliId) {
            case "dingding": return "钉钉";
            case "wecom": return "企业微信";
            case "feishu": return "飞书";
            default: return cliId;
        }
    }
}
