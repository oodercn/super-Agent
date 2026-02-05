package net.ooder.mcpagent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.mcpagent.skill.McpAgentSkill;
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
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BatchApiController 单元测试
 */
class BatchApiControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private McpAgentSkill mcpAgentSkill;
    
    @InjectMocks
    private BatchApiController batchApiController;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(batchApiController).build();
        objectMapper = new ObjectMapper();
    }
    
    /**
     * 测试批量获取数据API
     */
    @Test
    void testBatchFetch() throws Exception {
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        request.put("page", 1);
        request.put("pageSize", 10);
        request.put("sortBy", "id");
        request.put("sortOrder", "asc");
        
        Map<String, Object> filters = new HashMap<>();
        filters.put("status", "active");
        request.put("filters", filters);
        
        // 执行请求并验证响应
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
                .andExpect(jsonPath("$.pagination.total").value(50))
                .andExpect(jsonPath("$.pagination.totalPages").value(5));
    }
    
    /**
     * 测试批量创建数据API
     */
    @Test
    void testBatchCreate() throws Exception {
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        
        Map<String, Object> agent1 = new HashMap<>();
        agent1.put("name", "Test Agent 1");
        agent1.put("type", "mcp");
        agent1.put("status", "active");
        
        Map<String, Object> agent2 = new HashMap<>();
        agent2.put("name", "Test Agent 2");
        agent2.put("type", "route");
        agent2.put("status", "inactive");
        
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(agent1);
        items.add(agent2);
        request.put("items", items);
        
        // 执行请求并验证响应
        mockMvc.perform(post("/api/batch/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.createdCount").value(2));
    }
    
    /**
     * 测试批量更新数据API
     */
    @Test
    void testBatchUpdate() throws Exception {
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        
        Map<String, Object> agent1 = new HashMap<>();
        agent1.put("id", "agent-1");
        agent1.put("name", "Updated Agent 1");
        agent1.put("status", "active");
        
        Map<String, Object> agent2 = new HashMap<>();
        agent2.put("id", "agent-2");
        agent2.put("name", "Updated Agent 2");
        agent2.put("status", "active");
        
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(agent1);
        items.add(agent2);
        request.put("items", items);
        
        // 执行请求并验证响应
        mockMvc.perform(post("/api/batch/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.updatedCount").value(2));
    }
    
    /**
     * 测试批量删除数据API
     */
    @Test
    void testBatchDelete() throws Exception {
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        List<String> ids = new ArrayList<>();
        ids.add("agent-1");
        ids.add("agent-2");
        ids.add("agent-3");
        request.put("ids", ids);
        
        // 执行请求并验证响应
        mockMvc.perform(post("/api/batch/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.deletedCount").value(3));
    }
    
    /**
     * 测试批量执行操作API
     */
    @Test
    void testBatchExecute() throws Exception {
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "test");
        
        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", "agent-1");
        
        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", "agent-2");
        
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        request.put("items", items);
        
        Map<String, Object> params = new HashMap<>();
        params.put("testParam", "testValue");
        request.put("params", params);
        
        // 执行请求并验证响应
        mockMvc.perform(post("/api/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.executedCount").value(2));
    }
    
    /**
     * 测试批量获取数据API - 异常情况
     */
    @Test
    void testBatchFetchException() throws Exception {
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        // 故意不设置resource参数，测试异常处理
        
        // 执行请求并验证响应
        mockMvc.perform(post("/api/batch/fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    /**
     * 测试批量创建数据API - 空数据情况
     */
    @Test
    void testBatchCreateEmpty() throws Exception {
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        request.put("items", new ArrayList<>()); // 空数据列表
        
        // 执行请求并验证响应
        mockMvc.perform(post("/api/batch/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0))
                .andExpect(jsonPath("$.createdCount").value(0));
    }
    
    /**
     * 测试批量删除数据API - 空ID列表情况
     */
    @Test
    void testBatchDeleteEmpty() throws Exception {
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("resource", "agents");
        request.put("ids", new ArrayList<>()); // 空ID列表
        
        // 执行请求并验证响应
        mockMvc.perform(post("/api/batch/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.deletedCount").value(0));
    }
}
