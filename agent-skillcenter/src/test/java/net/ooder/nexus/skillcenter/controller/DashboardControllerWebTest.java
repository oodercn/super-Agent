package net.ooder.nexus.skillcenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.market.SkillMarketManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 仪表盘API的Web测试用例 - 符合 ooderNexus 规范
 */
@WebMvcTest(DashboardController.class)
class DashboardControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SkillManager skillManager;

    @MockBean
    private SkillMarketManager marketManager;

    @BeforeEach
    void setUp() {
        when(skillManager.getAllSkills()).thenReturn(new ArrayList<>());
        when(marketManager.getAllSkills()).thenReturn(new ArrayList<>());
    }

    /**
     * 测试获取系统概览统计数据
     */
    @Test
    void testGetDashboardStats() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/dashboard")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.totalSkills").exists())
                .andExpect(jsonPath("$.data.totalMarketSkills").exists())
                .andExpect(jsonPath("$.data.systemInfo").exists())
                .andExpect(jsonPath("$.data.systemInfo.javaVersion").exists())
                .andExpect(jsonPath("$.data.systemInfo.osName").exists())
                .andExpect(jsonPath("$.data.appInfo").exists())
                .andExpect(jsonPath("$.data.appInfo.version").exists())
                .andExpect(jsonPath("$.data.appInfo.name").exists());
    }

    /**
     * 测试获取技能执行统计数据
     */
    @Test
    void testGetExecutionStats() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/dashboard/execution-stats")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.totalExecutions").exists())
                .andExpect(jsonPath("$.data.successfulExecutions").exists())
                .andExpect(jsonPath("$.data.failedExecutions").exists())
                .andExpect(jsonPath("$.data.successRate").exists())
                .andExpect(jsonPath("$.data.averageExecutionTime").exists())
                .andExpect(jsonPath("$.data.topExecutedSkills").exists())
                .andExpect(jsonPath("$.data.executionTrend").exists());
    }

    /**
     * 测试获取市场活跃度统计数据
     */
    @Test
    void testGetMarketStats() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/dashboard/market-stats")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.totalMarketSkills").exists())
                .andExpect(jsonPath("$.data.totalDownloads").exists())
                .andExpect(jsonPath("$.data.totalReviews").exists())
                .andExpect(jsonPath("$.data.averageRating").exists())
                .andExpect(jsonPath("$.data.topDownloadedSkills").exists())
                .andExpect(jsonPath("$.data.marketTrend").exists())
                .andExpect(jsonPath("$.data.categoryDistribution").exists());
    }

    /**
     * 测试获取系统资源使用统计数据
     */
    @Test
    void testGetSystemStats() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/dashboard/system-stats")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.cpuUsage").exists())
                .andExpect(jsonPath("$.data.memoryUsage").exists())
                .andExpect(jsonPath("$.data.totalMemory").exists())
                .andExpect(jsonPath("$.data.usedMemory").exists())
                .andExpect(jsonPath("$.data.freeMemory").exists())
                .andExpect(jsonPath("$.data.systemLoad").exists())
                .andExpect(jsonPath("$.data.availableProcessors").exists())
                .andExpect(jsonPath("$.data.uptime").exists());
    }
}

