package net.ooder.mcpagent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API 健壮性测试
 * 测试各种API端点的稳定性、错误处理和边界情况
 */
class ApiRobustnessTest {

    private MockMvc mockMvc;
    
    @InjectMocks
    private BatchApiController batchApiController;
    
    @InjectMocks
    private McpAgentController mcpAgentController;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(batchApiController, mcpAgentController).build();
        objectMapper = new ObjectMapper();
    }
    
    /**
     * 测试批量获取数据API - 正常情况
     */
    @Test
    void testBatchFetchNormal() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        request.put("page", 1);
        request.put("pageSize", 10);
        request.put("sortBy", "id");
        request.put("sortOrder", "asc");
        
        mockMvc.perform(post("/api/batch/fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pagination").exists())
                .andExpect(jsonPath("$.pagination.page").value(1))
                .andExpect(jsonPath("$.pagination.pageSize").value(10))
                .andExpect(jsonPath("$.pagination.total").exists())
                .andExpect(jsonPath("$.pagination.totalPages").exists());
    }
    
    /**
     * 测试批量获取数据API - 边界情况（大量数据）
     */
    @Test
    void testBatchFetchLargeData() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        request.put("page", 1);
        request.put("pageSize", 1000); // 大页面大小
        request.put("sortBy", "id");
        request.put("sortOrder", "asc");
        
        mockMvc.perform(post("/api/batch/fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    /**
     * 测试批量获取数据API - 边界情况（空请求）
     */
    @Test
    void testBatchFetchEmptyRequest() throws Exception {
        Map<String, Object> request = new HashMap<>();
        
        mockMvc.perform(post("/api/batch/fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    /**
     * 测试批量创建数据API - 正常情况
     */
    @Test
    void testBatchCreateNormal() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        
        ArrayList<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> agent1 = new HashMap<>();
        agent1.put("name", "Test Agent 1");
        agent1.put("type", "mcp");
        agent1.put("status", "active");
        items.add(agent1);
        
        Map<String, Object> agent2 = new HashMap<>();
        agent2.put("name", "Test Agent 2");
        agent2.put("type", "route");
        agent2.put("status", "inactive");
        items.add(agent2);
        
        request.put("items", items);
        
        mockMvc.perform(post("/api/batch/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.createdCount").value(2));
    }
    
    /**
     * 测试批量创建数据API - 边界情况（空数据）
     */
    @Test
    void testBatchCreateEmptyData() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        request.put("items", new ArrayList<>()); // 空数据列表
        
        mockMvc.perform(post("/api/batch/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.createdCount").value(0));
    }
    
    /**
     * 测试批量更新数据API - 正常情况
     */
    @Test
    void testBatchUpdateNormal() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        
        ArrayList<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> agent1 = new HashMap<>();
        agent1.put("id", "agent-1");
        agent1.put("name", "Updated Agent 1");
        agent1.put("status", "active");
        items.add(agent1);
        
        Map<String, Object> agent2 = new HashMap<>();
        agent2.put("id", "agent-2");
        agent2.put("name", "Updated Agent 2");
        agent2.put("status", "active");
        items.add(agent2);
        
        request.put("items", items);
        
        mockMvc.perform(post("/api/batch/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.updatedCount").value(2));
    }
    
    /**
     * 测试批量删除数据API - 正常情况
     */
    @Test
    void testBatchDeleteNormal() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        
        ArrayList<String> ids = new ArrayList<>();
        ids.add("agent-1");
        ids.add("agent-2");
        ids.add("agent-3");
        request.put("ids", ids);
        
        mockMvc.perform(post("/api/batch/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.deletedCount").value(3));
    }
    
    /**
     * 测试批量删除数据API - 边界情况（空ID列表）
     */
    @Test
    void testBatchDeleteEmptyIds() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        request.put("ids", new ArrayList<>()); // 空ID列表
        
        mockMvc.perform(post("/api/batch/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.deletedCount").value(0));
    }
    
    /**
     * 测试批量执行操作API - 正常情况
     */
    @Test
    void testBatchExecuteNormal() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "test");
        
        ArrayList<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", "agent-1");
        items.add(item1);
        
        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", "agent-2");
        items.add(item2);
        
        request.put("items", items);
        
        Map<String, Object> params = new HashMap<>();
        params.put("testParam", "testValue");
        request.put("params", params);
        
        mockMvc.perform(post("/api/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.executedCount").value(2));
    }
    
    /**
     * 测试健康检查API
     */
    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/mcp/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("healthy"))
                .andExpect(jsonPath("$.service").value("ooder-mcp-agent"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    
    /**
     * 测试网络状态API
     */
    @Test
    void testNetworkStatus() throws Exception {
        mockMvc.perform(get("/api/mcp/network/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists());
    }
    
    /**
     * 测试网络拓扑API
     */
    @Test
    void testNetworkTopology() throws Exception {
        mockMvc.perform(get("/api/mcp/network/topology"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists());
    }
    
    /**
     * 测试系统状态API
     */
    @Test
    void testSystemStatus() throws Exception {
        mockMvc.perform(get("/api/mcp/system/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists());
    }
    
    /**
     * 测试测试命令API - 正常情况
     */
    @Test
    void testTestCommandNormal() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("commandType", "MCP_STATUS");
        request.put("agentId", "test-agent-123");
        
        mockMvc.perform(post("/api/mcp/command/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists());
    }
    
    /**
     * 测试测试命令API - 异常情况（未知命令）
     */
    @Test
    void testTestCommandUnknown() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("commandType", "UNKNOWN_COMMAND");
        request.put("agentId", "test-agent-123");
        
        mockMvc.perform(post("/api/mcp/command/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("UNKNOWN_COMMAND"));
    }
    
    /**
     * 测试添加网络链路API
     */
    @Test
    void testAddNetworkLink() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("linkId", "link-123");
        request.put("sourceAgentId", "source-123");
        request.put("targetAgentId", "target-123");
        request.put("linkType", "direct");
        
        mockMvc.perform(post("/api/mcp/network/link")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists());
    }
    
    /**
     * 测试重置网络统计数据API
     */
    @Test
    void testResetNetworkStats() throws Exception {
        mockMvc.perform(post("/api/mcp/network/reset"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists());
    }
    
    /**
     * 测试获取RouteAgent详情API
     */
    @Test
    void testGetRouteAgentDetails() throws Exception {
        String routeAgentId = "route-123";
        
        mockMvc.perform(get("/api/mcp/network/routeagent/{routeAgentId}", routeAgentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.id").value(routeAgentId));
    }
    
    /**
     * 测试获取RouteAgent VFS API
     */
    @Test
    void testGetRouteAgentVFS() throws Exception {
        String routeAgentId = "route-123";
        String path = "/";
        
        mockMvc.perform(get("/api/mcp/network/routeagent/{routeAgentId}/vfs", routeAgentId)
                .param("path", path))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.path").value(path));
    }
    
    /**
     * 测试获取RouteAgent能力列表API
     */
    @Test
    void testGetRouteAgentCapabilities() throws Exception {
        String routeAgentId = "route-123";
        
        mockMvc.perform(get("/api/mcp/network/routeagent/{routeAgentId}/capabilities", routeAgentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists());
    }
    
    /**
     * 测试获取RouteAgent链路API
     */
    @Test
    void testGetRouteAgentLinks() throws Exception {
        String routeAgentId = "route-123";
        
        mockMvc.perform(get("/api/mcp/network/routeagent/{routeAgentId}/links", routeAgentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    /**
     * 测试API响应时间 - 确保响应时间在合理范围内
     */
    @Test
    void testApiResponseTime() throws Exception {
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/mcp/health"))
                .andExpect(status().isOk());
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        // 响应时间应该在1秒内
        assert responseTime < 1000 : "API响应时间过长: " + responseTime + "ms";
    }
}
