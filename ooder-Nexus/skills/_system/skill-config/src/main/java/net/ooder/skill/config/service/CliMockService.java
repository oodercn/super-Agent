package net.ooder.skill.config.service;

import net.ooder.skill.config.dto.CliCallLogDTO;
import net.ooder.skill.config.dto.CliMockRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CliMockService {

    private static final Logger log = LoggerFactory.getLogger(CliMockService.class);

    @Autowired(required = false)
    private CliConfigService cliConfigService;

    public Map<String, Object> testConnection(String cliId) {
        log.info("[CliMockService] Testing connection for CLI: {}", cliId);
        Map<String, Object> result = new HashMap<>();
        result.put("cliId", cliId);
        result.put("success", true);
        result.put("message", "连接测试成功");
        result.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> details = new HashMap<>();
        details.put("latency", new Random().nextInt(100) + 50);
        details.put("serverVersion", "1.0.0");
        details.put("apiVersion", "v1");
        result.put("details", details);
        
        return result;
    }

    public Map<String, Object> mockCall(String cliId, CliMockRequestDTO request) {
        log.info("[CliMockService] Mock call for CLI: {} - action: {}", cliId, request.getAction());
        
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        result.put("cliId", cliId);
        result.put("action", request.getAction());
        result.put("dryRun", request.isDryRun());
        result.put("timestamp", startTime);
        
        try {
            Map<String, Object> mockResponse = executeMockAction(cliId, request.getAction(), request.getParams());
            result.put("success", true);
            result.put("response", mockResponse);
            result.put("message", "模拟调用成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("message", "模拟调用失败: " + e.getMessage());
        }
        
        long duration = System.currentTimeMillis() - startTime;
        result.put("duration", duration);
        
        if (cliConfigService != null && !request.isDryRun()) {
            CliCallLogDTO logEntry = new CliCallLogDTO();
            logEntry.setLogId(UUID.randomUUID().toString());
            logEntry.setCliId(cliId);
            logEntry.setAction(request.getAction());
            logEntry.setRequest(request.getParams());
            logEntry.setResponse((Map<String, Object>) result.get("response"));
            logEntry.setSuccess((Boolean) result.get("success"));
            logEntry.setDuration(duration);
            logEntry.setTimestamp(startTime);
            cliConfigService.addCallLog(logEntry);
        }
        
        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> executeMockAction(String cliId, String action, Map<String, Object> params) {
        Map<String, Object> response = new HashMap<>();
        
        switch (action) {
            case "sendMessage":
                response.put("messageId", "msg_" + UUID.randomUUID().toString().substring(0, 8));
                response.put("receiver", params != null ? params.get("receiver") : "unknown");
                response.put("sentAt", System.currentTimeMillis());
                response.put("status", "sent");
                break;
                
            case "sendMarkdown":
                response.put("messageId", "msg_" + UUID.randomUUID().toString().substring(0, 8));
                response.put("title", params != null ? params.get("title") : "无标题");
                response.put("sentAt", System.currentTimeMillis());
                response.put("status", "sent");
                break;
                
            case "getUserInfo":
                response.put("userId", params != null ? params.get("userId") : "user_001");
                response.put("name", "测试用户");
                response.put("avatar", "https://example.com/avatar.png");
                response.put("department", "技术部");
                response.put("position", "工程师");
                response.put("mobile", "138****8888");
                response.put("email", "test@example.com");
                break;
                
            case "getDeptList":
                List<Map<String, Object>> depts = new ArrayList<>();
                Map<String, Object> dept1 = new HashMap<>();
                dept1.put("deptId", "001");
                dept1.put("name", "技术部");
                dept1.put("parentDeptId", "0");
                dept1.put("memberCount", 50);
                depts.add(dept1);
                
                Map<String, Object> dept2 = new HashMap<>();
                dept2.put("deptId", "002");
                dept2.put("name", "产品部");
                dept2.put("parentDeptId", "0");
                dept2.put("memberCount", 30);
                depts.add(dept2);
                
                Map<String, Object> dept3 = new HashMap<>();
                dept3.put("deptId", "003");
                dept3.put("name", "运营部");
                dept3.put("parentDeptId", "0");
                dept3.put("memberCount", 20);
                depts.add(dept3);
                
                response.put("departments", depts);
                response.put("total", depts.size());
                break;
                
            case "getAccessToken":
                response.put("accessToken", "mock_access_token_" + UUID.randomUUID().toString().substring(0, 16));
                response.put("expiresIn", 7200);
                response.put("refreshToken", "mock_refresh_token_" + UUID.randomUUID().toString().substring(0, 16));
                break;
                
            default:
                response.put("result", "success");
                response.put("action", action);
                response.put("params", params);
        }
        
        return response;
    }

    public Map<String, Object> getMockData(String cliId, String dataType) {
        Map<String, Object> data = new HashMap<>();
        data.put("cliId", cliId);
        data.put("dataType", dataType);
        data.put("timestamp", System.currentTimeMillis());
        
        switch (dataType) {
            case "users":
                List<Map<String, Object>> users = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", "user_00" + i);
                    user.put("name", "用户" + i);
                    user.put("department", "技术部");
                    users.add(user);
                }
                data.put("data", users);
                break;
                
            case "departments":
                List<Map<String, Object>> depts = new ArrayList<>();
                Map<String, Object> dept1 = new HashMap<>();
                dept1.put("deptId", "001");
                dept1.put("name", "技术部");
                depts.add(dept1);
                Map<String, Object> dept2 = new HashMap<>();
                dept2.put("deptId", "002");
                dept2.put("name", "产品部");
                depts.add(dept2);
                data.put("data", depts);
                break;
                
            default:
                data.put("data", Collections.emptyList());
        }
        
        return data;
    }
}
