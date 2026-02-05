package net.ooder.mcpagent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ConfigController 单元测试
 */
class ConfigControllerTest {

    private MockMvc mockMvc;
    
    @InjectMocks
    private ConfigController configController;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(configController).build();
        objectMapper = new ObjectMapper();
    }
    
    /**
     * 测试获取MCP配置API
     */
    @Test
    void testGetMcpConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/config/mcp"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.version").exists())
                .andExpect(jsonPath("$.data.servicePort").exists())
                .andExpect(jsonPath("$.data.heartbeatInterval").exists());
    }
    
    /**
     * 测试更新MCP配置API
     */
    @Test
    void testUpdateMcpConfig() throws Exception {
        // 构建请求参数
        Map<String, Object> configData = new HashMap<>();
        configData.put("version", "1.0.0");
        configData.put("servicePort", 8081);
        
        // 执行请求并验证响应
        mockMvc.perform(put("/api/config/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configData)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.version").value("1.0.0"))
                .andExpect(jsonPath("$.data.servicePort").value(8081));
    }
    
    /**
     * 测试获取Route配置API
     */
    @Test
    void testGetRouteConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/config/route"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.maxRoutes").exists())
                .andExpect(jsonPath("$.data.routeTimeout").exists())
                .andExpect(jsonPath("$.data.routeRetryCount").exists());
    }
    
    /**
     * 测试更新Route配置API
     */
    @Test
    void testUpdateRouteConfig() throws Exception {
        // 构建请求参数
        Map<String, Object> configData = new HashMap<>();
        configData.put("maxRoutes", 2000);
        configData.put("routeTimeout", 60000);
        
        // 执行请求并验证响应
        mockMvc.perform(put("/api/config/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configData)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.maxRoutes").value(2000))
                .andExpect(jsonPath("$.data.routeTimeout").value(60000));
    }
    
    /**
     * 测试获取End配置API
     */
    @Test
    void testGetEndConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/config/end"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.maxEndAgents").exists())
                .andExpect(jsonPath("$.data.endAgentTimeout").exists())
                .andExpect(jsonPath("$.data.endAgentHeartbeatInterval").exists());
    }
    
    /**
     * 测试更新End配置API
     */
    @Test
    void testUpdateEndConfig() throws Exception {
        // 构建请求参数
        Map<String, Object> configData = new HashMap<>();
        configData.put("maxEndAgents", 1000);
        configData.put("endAgentTimeout", 120000);
        
        // 执行请求并验证响应
        mockMvc.perform(put("/api/config/end")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configData)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.maxEndAgents").value(1000))
                .andExpect(jsonPath("$.data.endAgentTimeout").value(120000));
    }
    
    /**
     * 测试获取所有配置API
     */
    @Test
    void testGetAllConfigs() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/config/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.mcp").exists())
                .andExpect(jsonPath("$.data.route").exists())
                .andExpect(jsonPath("$.data.end").exists());
    }
    
    /**
     * 测试重置MCP配置为默认值API
     */
    @Test
    void testResetMcpConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/config/reset/mcp"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.containsString("mcp config reset to default values successfully")))
                .andExpect(jsonPath("$.data").exists());
    }
    
    /**
     * 测试重置Route配置为默认值API
     */
    @Test
    void testResetRouteConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/config/reset/route"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.containsString("route config reset to default values successfully")))
                .andExpect(jsonPath("$.data").exists());
    }
    
    /**
     * 测试重置End配置为默认值API
     */
    @Test
    void testResetEndConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/config/reset/end"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.containsString("end config reset to default values successfully")))
                .andExpect(jsonPath("$.data").exists());
    }
    
    /**
     * 测试重置所有配置为默认值API
     */
    @Test
    void testResetAllConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/config/reset/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.containsString("all config reset to default values successfully")))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.mcp").exists())
                .andExpect(jsonPath("$.data.route").exists())
                .andExpect(jsonPath("$.data.end").exists());
    }
    
    /**
     * 测试重置无效配置类型API
     */
    @Test
    void testResetInvalidConfigType() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/config/reset/invalid"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.containsString("Invalid config type")))
                .andExpect(jsonPath("$.code").value(400));
    }
    
    /**
     * 测试获取配置变更历史API
     */
    @Test
    void testGetConfigHistory() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/config/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    /**
     * 测试获取配置变更历史API - 带参数
     */
    @Test
    void testGetConfigHistoryWithParams() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/config/history")
                .param("limit", "10")
                .param("configType", "MCP"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    /**
     * 测试获取配置统计API
     */
    @Test
    void testGetConfigStats() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/config/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.mcpConfigCount").exists())
                .andExpect(jsonPath("$.data.routeConfigCount").exists())
                .andExpect(jsonPath("$.data.endConfigCount").exists())
                .andExpect(jsonPath("$.data.totalConfigCount").exists())
                .andExpect(jsonPath("$.data.configChangeCount").exists());
    }
}
