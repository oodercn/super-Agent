package net.ooder.nexus.adapter.inbound.controller.system;

import net.ooder.nexus.NexusSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 系统监控控制器测试 (ResultModel 2.0 规范)
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@SpringBootTest(classes = NexusSpringApplication.class)
@AutoConfigureMockMvc
public class SystemMonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetSystemHealth_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/system/monitor/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.status").exists())
                .andExpect(jsonPath("$.data.components").exists())
                .andExpect(jsonPath("$.data.systemInfo").exists());
    }

    @Test
    public void testGetSystemMetrics_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/system/monitor/metrics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.memory").exists())
                .andExpect(jsonPath("$.data.cpu").exists())
                .andExpect(jsonPath("$.data.disk").exists())
                .andExpect(jsonPath("$.data.network").exists());
    }

    @Test
    public void testGetMetricsHistory_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/system/monitor/metrics/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetAlerts_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/system/monitor/alerts/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testClearAlerts_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/system/monitor/alerts/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testClearAlert_NotFound() throws Exception {
        String requestJson = "{\"alertId\":\"non-existent-id\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/system/monitor/alerts/clearById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    public void testCreateTestAlert_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/system/monitor/alerts/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.type").value("test"));
    }
}
