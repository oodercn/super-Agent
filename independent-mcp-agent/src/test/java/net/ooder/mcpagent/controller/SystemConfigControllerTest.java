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
 * SystemConfigController 单元测试
 */
class SystemConfigControllerTest {

    private MockMvc mockMvc;
    
    @InjectMocks
    private SystemConfigController systemConfigController;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(systemConfigController).build();
        objectMapper = new ObjectMapper();
    }
    
    /**
     * 测试获取所有系统配置API
     */
    @Test
    void testGetAllConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/system/config/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.basic").exists())
                .andExpect(jsonPath("$.data.network").exists())
                .andExpect(jsonPath("$.data.service").exists())
                .andExpect(jsonPath("$.data.security").exists())
                .andExpect(jsonPath("$.data.log").exists())
                .andExpect(jsonPath("$.data.storage").exists())
                .andExpect(jsonPath("$.data.performance").exists());
    }
    
    /**
     * 测试获取指定类型的配置API
     */
    @Test
    void testGetConfigByType() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/system/config/basic"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.configType").value("basic"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.systemName").exists())
                .andExpect(jsonPath("$.data.systemVersion").exists());
    }
    
    /**
     * 测试获取不存在的配置类型API
     */
    @Test
    void testGetConfigByTypeNotFound() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/system/config/non-existent"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("CONFIG_TYPE_NOT_FOUND"));
    }
    
    /**
     * 测试更新指定类型的配置API
     */
    @Test
    void testUpdateConfigByType() throws Exception {
        // 构建请求参数
        Map<String, Object> configData = new HashMap<>();
        configData.put("systemName", "Test System");
        configData.put("systemVersion", "1.0.0");
        
        // 执行请求并验证响应
        mockMvc.perform(put("/api/system/config/basic")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configData)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.configType").value("basic"))
                .andExpect(jsonPath("$.data.systemName").value("Test System"))
                .andExpect(jsonPath("$.data.systemVersion").value("1.0.0"));
    }
    
    /**
     * 测试更新不存在的配置类型API
     */
    @Test
    void testUpdateConfigByTypeNotFound() throws Exception {
        // 构建请求参数
        Map<String, Object> configData = new HashMap<>();
        configData.put("test", "value");
        
        // 执行请求并验证响应
        mockMvc.perform(put("/api/system/config/non-existent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configData)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("CONFIG_TYPE_NOT_FOUND"));
    }
    
    /**
     * 测试更新单个配置项API
     */
    @Test
    void testUpdateConfigItem() throws Exception {
        // 构建请求参数
        Map<String, Object> configData = new HashMap<>();
        configData.put("value", "Test System Updated");
        
        // 执行请求并验证响应
        mockMvc.perform(put("/api/system/config/basic/systemName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configData)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.configType").value("basic"))
                .andExpect(jsonPath("$.configKey").value("systemName"))
                .andExpect(jsonPath("$.configValue").value("Test System Updated"));
    }
    
    /**
     * 测试更新单个配置项API - 缺少value参数
     */
    @Test
    void testUpdateConfigItemMissingValue() throws Exception {
        // 构建请求参数
        Map<String, Object> configData = new HashMap<>();
        // 故意不设置value参数
        
        // 执行请求并验证响应
        mockMvc.perform(put("/api/system/config/basic/systemName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configData)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("CONFIG_VALUE_REQUIRED"));
    }
    
    /**
     * 测试重置配置到默认值API
     */
    @Test
    void testResetConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/system/config/reset/basic"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.configType").value("basic"))
                .andExpect(jsonPath("$.data").exists());
    }
    
    /**
     * 测试重置不存在的配置类型API
     */
    @Test
    void testResetConfigNotFound() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/system/config/reset/non-existent"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("CONFIG_TYPE_NOT_FOUND"));
    }
    
    /**
     * 测试重置所有配置到默认值API
     */
    @Test
    void testResetAllConfig() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/system/config/reset-all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists());
    }
    
    /**
     * 测试获取配置状态API
     */
    @Test
    void testGetConfigStatus() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(get("/api/system/config/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.configCount").exists())
                .andExpect(jsonPath("$.data.lastModified").exists())
                .andExpect(jsonPath("$.data.configTypeCounts").exists());
    }
}
